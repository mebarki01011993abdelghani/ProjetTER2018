package tkgate;



import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;




import tkgate.modele.Component;
import tkgate.modele.Joint;
import tkgate.modele.Module;
import tkgate.modele.Wire;

public class EditorControler {

	Module module;

	EditorControler(Module module) {
		this.module = module;
	}

	public Module getModule() {

		return module;
	}

	// ****************************************************************************************************
	// *********** Methodes de localisation
	// ***********************************************
	// ****************************************************************************************************

	// Trouver tous les Composants qui contiennent un certain point :

	public List<Component> ComponentsContaining(Point2D.Double p) {
		List<Component> l = new ArrayList<Component>();
		for (Component c : module.getComponentList())
			if (c.contains(p))
				l.add(c);
		return l;
	}

	// Retourne un Composant qui contient le point :

	public Component ComponentContaining(Point2D.Double p) {
		List<Component> l = ComponentsContaining(p);
		if (l.isEmpty())
			return null;
		else
			return l.get(0);
	}

	// Vérifier si une certaine zone du ModulePanel n'est pas déjà occupée par
	// un IGate:

	public boolean isFree(Rectangle2D.Double rect) {
		for (Component c : module.getComponentList()) {
			if (c.intersects(rect)) {
				return false;
			}
		}

		return true;
	}

	// Verifier si il y a la place de placer un composant:
	// TODO cela dépend de la taille du composant à placer !!! 

	public boolean isFreePlace(Point2D.Double point) {
		Rectangle2D.Double rect = new Rectangle2D.Double(point.x - Settings.GATE_WIDTH / 2, point.y - Settings.GATE_HEIGHT / 2, Settings.GATE_WIDTH, Settings.GATE_HEIGHT);
		return isFree(rect);
	}

	// Vérifier si un Component est bien le seul à se trouver dans sa zone

	public boolean isAlone(Component component) {
		for (Component c : module.getComponentList()) {
			if (!c.equals(component) && c.intersects(component))
				return false;
		}
		return true;
	}

	// Find wires closed to the point
	public List<Wire> wiresContaining(Point2D.Double point) {
		List<Wire> wireList = new ArrayList<Wire>();
		for (Component c : module.getComponentList()) {
			wireList.addAll(c.findWires(point));
		}
		return wireList;

	}

	// Return a wire containing the point
	public Wire wireContaining(Point2D.Double point) {
		List<Wire> wireList = wiresContaining(point);
		if (wireList.isEmpty())
			return null;
		else
			return wireList.get(0);
	}

	// Return a wire containing the point which is not the wire argument
	public Wire wireContaining(Point2D.Double point, Wire wire) {
		List<Wire> wireList = wiresContaining(point);
		for (Wire w : wireList) {
			if (w != wire)
				return w;
		}
		return null;
	}

	// return -1 if point is not near to an edge of the wire, and the proximal
	// edge otherwise
	// FIXME ne verifie pas que l'edge est null, car l'etat selected ne sait pas
	// le
	// gerer.
	int wireEdge(Wire wire, Point2D.Double point) {

		if (wire == null || wire.isEmpty())
			return -1;
		else {
			int prec = Settings.MOUSE_PRECISION * 2;
			Rectangle2D.Double rect = new Rectangle2D.Double(point.x - prec, point.y - prec, prec * 2, prec * 2);
			if (rect.contains(wire.getPoint(0)))
				return 0;
			else if (rect.contains(wire.getLastPoint()))
				return wire.getSize() - 1;
			else
				return -1;
		}
	}

	// ********************************** ACCESSES
	// *************************************************

	public void setSelectedWire(Wire wire, boolean b) {
		if (wire != null && wire.getSelected() != b) {
			module.setSelected(wire, b);
		}
	}

	// ************************* WIRES MOVES
	// ***************************************************

	// move a wire by segment
	public void moveWireSegment(Wire wire, Point2D.Double point, int selSegment) {
		if (selSegment < 0 || selSegment >= wire.getSize() - 1)
			return;
		else
			module.moveWireSegment(wire, point, selSegment);
	}

	// move a wire by edge but without connexion
	public void moveWireEdge(Wire wire, int edge, Point2D.Double point) {
		if (!(wire == null && edge != -1)) {
			if (edge == 0)
				module.moveWireHead(wire, point);
			else
				module.moveWireTail(wire, point);
		}
	}

	// move a wire by edge and connect if possible

	public void releaseWireEdge(Wire wire, int edge, Point2D.Double point) {
		if (wire == null || edge == -1)
			return;
		Wire wire2 = wireContaining(point, wire); // TODO attention ici on
													// empeche les boucles
		if (wire2 == null)// || wire2.getName() == wire.getName())
			moveWireEdge(wire, edge, point);
		else {
			int edge2 = wireEdge(wire2, point);
			if (edge2 != -1) {
				if (edge2 == 0 && wire2.getHead() == null) {
					glueEdgeToHead(wire, edge, wire2);
					System.out.println("glueEdgeToHead");
					return;
				}
				if (edge2 != 0 && wire2.getTail() == null)
					glueEdgeToTail(wire, edge, wire2);
				else
					moveWireEdge(wire, edge, point);
			} else
				joint(wire, edge, wire2, point);
		}
	}

	private void glueEdgeToHead(Wire wire, int edge, Wire wire2) {
		if (edge == 0)
			wire.glueHeadToHead(wire2);
		else
			wire.glueTailToHead(wire2);
	}

	private void glueEdgeToTail(Wire wire, int edge, Wire wire2) {
		if (edge == 0)
			wire.glueHeadToTail(wire2);
		else
			wire.glueTailToTail(wire2);

	}

	private void joint(Wire wire, int edge, Wire wire2, Point2D.Double point) {
		Joint joint = new Joint(module.getFreshGateName(), point, wire, edge, wire2);
		module.addComponent(joint);
	}

	public int indexOfSelectedPoint(Wire wire, Point2D.Double point) {
		return wire.proximalPoint(point);
	}

	public boolean isFreeEdge(int selIndex, Wire wire) {
		if (selIndex == 0 && wire.getHead() == null)
			return true;
		if (selIndex == wire.getSize() - 1 && wire.getTail() == null)
			return true;
		return false;
	}

	public void refactorConnections() {
		for (Component c : module.getComponentList()) {
			c.refactorConnections();
		}
	}

}
