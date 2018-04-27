package fr.univmrs.lif.model.component;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.tools.ComponentType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;


public abstract class RectangleGate extends Pane implements Component {

	protected boolean showName = false;
	protected Rectangle2D rect;
	protected StringProperty name = new SimpleStringProperty();
	protected int orientation = EAST_ORIENTATION;
	public static int EAST_ORIENTATION = 0;
	public static int SOUTH_ORIENTATION = 1;
	public static int WEST_ORIENTATION = 2;
	public static int NORTH_ORIENTATION = 3;
	protected ArrayList<Connection> inputs;
	protected ArrayList<Connection> outputs;
	protected ComponentType gateType;
	protected boolean selected = false;

	// ************** Methods from Component
	// ***************************************//

	@Override
	public ComponentType getType() {
		return gateType;
	}

	@Override
	public void draw(GraphicsContext gc) {
		for (Iterator<Connection> iter = inputs.iterator(); iter.hasNext();) {
			Wire wire = iter.next().getWire();
			wire.draw(gc);
			
		}
		for (Iterator<Connection> iter = outputs.iterator(); iter.hasNext();) {
			Wire wire = iter.next().getWire();
			wire.draw(gc);
	
		}
	}

	/**
	 * Set the rectangle at position p and set wires.
	 */
	@Override
	public void setPosition(Point2D p) {
		rect = new Rectangle2D((p.getX() - rect.getWidth() / 2),(p.getY() - rect.getHeight() / 2), rect.getWidth(), rect.getHeight());
	}

	
	@Override
	public Point2D getPosition() {
		return new Point2D(rect.getMinX()+ (rect.getWidth() / 2), rect.getMinY() + (rect.getHeight() / 2));
	}

	@Override
	public final String getName() {
		return name.get();
	}

	@Override
	public final void setName(String name) {
		this.name.set(name);
	}
	
	@Override
	public final StringProperty nameProperty() {return name;}
	 
	

	@Override
	public void setShowName(boolean b) {
		showName = b;
	}

	@Override
	public boolean getShowName() {
		return showName;
	}

	@Override
	public void setOrientation(int or) {
		if (or == EAST_ORIENTATION || or == SOUTH_ORIENTATION || or == WEST_ORIENTATION || or == NORTH_ORIENTATION)
			orientation = or;
		else {
			System.err.println("Bad orientation identifyer");
		}
	}

	@Override
	public int getOrientation() {
		return orientation;
	}

	@Override
	public List<Wire> findWires(Point2D point) {
		ArrayList<Wire> wireList = new ArrayList<Wire>();
		for (Connection connection : outputs)
			if (connection.getWire().intersects(point))
				wireList.add(connection.getWire());
		for (Connection connection : inputs)
			if (connection.getWire().intersects(point))
				wireList.add(connection.getWire());
		return wireList;
	}

	@Override
	public List<Wire> allWires() {
		ArrayList<Wire> wireList = new ArrayList<Wire>();
		for (Connection connection : outputs)
			wireList.add(connection.getWire());
		for (Connection connection : inputs)
			wireList.add(connection.getWire());
		return wireList;
	}

	// @Override
	public void setInputs(ArrayList<Connection> inputList) {
		inputs = inputList;

	}

	@Override
	public ArrayList<Connection> getInputs() {
		return inputs;
	}

	// @Override
	public void setOutputs(ArrayList<Connection> outputs) {
		this.outputs = outputs;
	}

	@Override
	public ArrayList<Connection> getOutputs() {
		return outputs;
	}

	@Override
	public int getInputSize() {
		return inputs.size();
	}

	@Override
	public int getOutputSize() {
		return outputs.size();
	}

	@Override
	public boolean intersects(Rectangle2D rect) {
		return getRectangle().intersects(rect);
	}

	@Override
	public boolean contains(Point2D p) {
		// System.out.println("RectGate " + p );
		return getRectangle().contains(p);
	}

	@Override
	public boolean intersects(Component component) {
		if (component instanceof RectangleGate) {
			Rectangle2D r = ((RectangleGate) component).getRectangle();
			return (this.getRectangle().intersects(r));
		} else
			return false; // TODO
	}

	@Override
	public void setSelected(boolean b) {
		selected = b;
	}

	@Override
	public void changeConnection(Connection connectionOld, Connection connectionNew) {
		for (Connection c : inputs)
			if (c.equals(connectionOld)) {
				c.setHead(connectionNew.head);
				c.setWire(connectionNew.wire);
				return;
			}
		for (Connection c : outputs)
			if (c.equals(connectionOld)) {
				c.setHead(connectionNew.head);
				c.setWire(connectionNew.wire);
				return;
			}
	}

	// *************************************************************************************

	public Rectangle2D getRectangle() {
		return new Rectangle2D(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
	}

	@Override
	public void move(Point2D p) {
		Point2D delta = new Point2D(p.getX() - getPosition().getX(), p.getY() - getPosition().getY());
		System.out.println("move(Point2D p) delta = " +delta );
		for (Connection co : inputs) {
			if (co.isHead())
				co.getWire().translateFromHead(delta);
			else
				co.getWire().translateFromTail(delta);
			co.getWire().align();
		}
		for (Connection co : outputs) {
			if (co.isHead())
				co.getWire().translateFromHead(delta);
			else
				co.getWire().translateFromTail(delta);
			co.getWire().align();
		}
		setPosition(p);
	}

	@Override
	public String toString() {
		String s = this.getType() + " inputs: ";
		for (Connection c : inputs)
			s = s + c.toString() + ", ";
		for (Connection c : outputs)
			s = s + c.toString() + ", ";
		return s;
	}

	// *************************************** Refactor connections

	// Appelé après déplacement d'une porte, pour replacer les fils correctement
	// (évite par exemple qu'un fil traverse une porte)

	@Override
	public void refactorConnections() {
		
		for (Connection connect : inputs) {
			refactorConnection(connect);
			connect.getWire().removeU();
		}
		for (Connection connect : outputs) {
			refactorConnection(connect);
			connect.getWire().removeU();
		}
	}

	public void debugg() throws Exception {
		for (Connection connect : inputs)
			connect.getWire().debugg();
		for (Connection connect : outputs)
			connect.getWire().debugg();
	}

	private void refactorConnection(Connection connect) {
		int index0, dir;
		if (connect.isHead()) {
			index0 = 0;
			dir = 1;
		} else {
			index0 = connect.getWire().getSize() - 1;
			dir = -1;
		}
		Point2D p0 = connect.getWire().getPoint(index0);
		
		if (rect.getMinX() == p0.getX())
			refactorFromEast(connect, index0, dir);
		else if (rect.getMinX() + rect.getWidth() == p0.getX())
			refactorFromWest(connect, index0, dir);
		else if (rect.getMinY() == p0.getY())
			refactorFromNorth(connect, index0, dir);
		else if (rect.getMinY() + rect.getHeight() == p0.getY())
			refactorFromSouth(connect, index0, dir);
	}

	private void refactorFromEast(Connection connect, int index0, int dir) {
		
		Wire wire = connect.getWire();
		Boolean fromHead = connect.isHead();
		Point2D p0 = wire.getPoint(index0);
		Point2D p1 = wire.getPoint(index0 + dir);
		if (p0.getX() < p1.getX()) { // horizontal but wrong orientation
			wire.addPointFrom(1, new Point2D(p0.getX() - Settings.WIRE_LENGTH, p0.getY()), fromHead);
			wire.addPointFrom(2, new Point2D(p0.getX() - Settings.WIRE_LENGTH, p0.getY() - rect.getHeight()), fromHead);
			wire.addPointFrom(3, new Point2D(p1.getX(), p0.getY() - rect.getHeight()), fromHead);
			if (wire.getSize() == 5) {
				Component c = connect.oppositeComponent();
				if (c != null)
					c.refactorConnections();
			}
		} else if (p0.getX() == p1.getX()) { // vertical
			wire.addPointFrom(1, new Point2D(p0.getX() - Settings.WIRE_LENGTH, p0.getY()), fromHead);
			wire.addPointFrom(2, new Point2D(p0.getX() - Settings.WIRE_LENGTH, p1.getY()), fromHead);
			if (wire.getSize() == 4) {
				Component c = connect.oppositeComponent();
				if (c != null)
					c.refactorConnections();
			}
		} else if (p1.getX() < p0.getX() && p0.getX() - Settings.WIRE_LENGTH < p1.getX()) { // too
																		// short
			if (wire.getSize() <= 3 && connect.oppositeComponent() != null)
				return;
			p1=new Point2D(p0.getX() - Settings.WIRE_LENGTH, p0.getY());
			if (wire.getSize() > 2) {
				Point2D p2 = wire.getPoint(index0 + dir + dir);
				p2=new Point2D(p0.getX() - Settings.WIRE_LENGTH, p2.getY());
			}
		}
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		wire.align();
	}

	private void refactorFromWest(Connection connect, int index0, int dir) {
		Wire wire = connect.getWire();
		Boolean fromHead = connect.isHead();
		Point2D p0 = wire.getPoint(index0);
		Point2D p1 = wire.getPoint(index0 + dir);
		if (p0.getX() > p1.getX()) { // horizontal but wrong orientation
			wire.addPointFrom(1, new Point2D(p0.getX() + Settings.WIRE_LENGTH, p0.getY()), fromHead);
			wire.addPointFrom(2, new Point2D(p0.getX() + Settings.WIRE_LENGTH, p0.getY() - rect.getHeight()), fromHead);
			wire.addPointFrom(3, new Point2D(p1.getX(), p0.getY() - rect.getHeight()), fromHead);
			if (wire.getSize() == 5) {
				Component c = connect.oppositeComponent();
				if (c != null)
					c.refactorConnections();
			}
		} else if (p0.getX() == p1.getX()) { // vertical
			wire.addPointFrom(1, new Point2D(p0.getX() + Settings.WIRE_LENGTH, p0.getY()), fromHead);
			wire.addPointFrom(2, new Point2D(p0.getX() + Settings.WIRE_LENGTH, p1.getY()), fromHead);
			if (wire.getSize() == 4) {
				Component c = connect.oppositeComponent();
				if (c != null)
					c.refactorConnections();
			}
		} else if (p0.getX() < p1.getX() && p0.getX() + Settings.WIRE_LENGTH > p1.getX()) { // too
																		// short
			if (wire.getSize() <= 3 && connect.oppositeComponent() != null)
				return;
			p1=new Point2D(p0.getX() + Settings.WIRE_LENGTH, p0.getY());
			if (wire.getSize() > 2) {
				Point2D p2 = wire.getPoint(index0 + dir + dir);
				p2=new Point2D(p0.getX() + Settings.WIRE_LENGTH, p2.getY());
			}
		}
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		wire.align();
	}

	private void refactorFromNorth(Connection connect, int index0, int dir) {
		Wire wire = connect.getWire();
		Boolean fromHead = connect.isHead();
		Point2D p0 = wire.getPoint(index0);
		Point2D p1 = wire.getPoint(index0 + dir);
		if (p0.getY() < p1.getY()) { // vertical but wrong orientation
			wire.addPointFrom(1, new Point2D(p0.getX(), p0.getY() - Settings.WIRE_LENGTH), fromHead);
			wire.addPointFrom(2, new Point2D(p0.getX() + rect.getWidth(), p0.getY() - Settings.WIRE_LENGTH), fromHead);
			wire.addPointFrom(3, new Point2D(p0.getX() + rect.getWidth(), p1.getY()), fromHead);
			if (wire.getSize() == 5) {
				Component c = connect.oppositeComponent();
				if (c != null)
					c.refactorConnections();
			}
		} else if (p0.getY() == p1.getY()) { // horizontal
			wire.addPointFrom(1, new Point2D(p0.getX(), p0.getY() - Settings.WIRE_LENGTH), fromHead);
			wire.addPointFrom(2, new Point2D(p1.getX(), p0.getY() - Settings.WIRE_LENGTH), fromHead);
			if (wire.getSize() == 4) {
				Component c = connect.oppositeComponent();
				if (c != null)
					c.refactorConnections();
			}
		} else if (p1.getY() < p0.getY() && p0.getY() - Settings.WIRE_LENGTH < p1.getY()) { // too
																		// short
			if (wire.getSize() <= 3 && connect.oppositeComponent() != null)
				return;
			p1=new Point2D(p0.getX(), p0.getY() - Settings.WIRE_LENGTH);
			if (wire.getSize() > 2) {
				Point2D p2 = wire.getPoint(index0 + dir + dir);
				p2=new Point2D(p2.getX(), p0.getY() - Settings.WIRE_LENGTH);
			}
		}
		wire.align();
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refactorFromSouth(Connection connect, int index0, int dir) {
		Wire wire = connect.getWire();
		Boolean fromHead = connect.isHead();
		Point2D p0 = wire.getPoint(index0);
		Point2D p1 = wire.getPoint(index0 + dir);
		if (p0.getY() > p1.getY()) { // vertical but wrong orientation
			wire.addPointFrom(1, new Point2D(p1.getX(), p0.getY() + Settings.WIRE_LENGTH), fromHead);
			wire.addPointFrom(2, new Point2D(p0.getX() + rect.getWidth(), p0.getY() + Settings.WIRE_LENGTH), fromHead);
			wire.addPointFrom(3, new Point2D(p0.getX() + rect.getWidth(), p1.getY()), fromHead);
			if (wire.getSize() == 5) {
				Component c = connect.oppositeComponent();
				if (c != null)
					c.refactorConnections();
			}
		} else if (p0.getY() == p1.getY()) { // horizontal
			wire.addPointFrom(1, new Point2D(p0.getX(), p0.getY() + Settings.WIRE_LENGTH), fromHead);
			wire.addPointFrom(2, new Point2D(p1.getX(), p0.getY() + Settings.WIRE_LENGTH), fromHead);
			if (wire.getSize() == 4) {
				Component c = connect.oppositeComponent();
				if (c != null)
					c.refactorConnections();
			}
		} else if (p1.getY() < p0.getY() && p0.getY() - Settings.WIRE_LENGTH < p1.getY()) { // too
																		// short
			if (wire.getSize() <= 3 && connect.oppositeComponent() != null)
				return;
			p1=new Point2D(p0.getX(), p0.getY() + Settings.WIRE_LENGTH);
			if (wire.getSize() > 2) {
				Point2D p2 = wire.getPoint(index0 + dir + dir);
				p2=new Point2D(p2.getX(), p0.getY() + Settings.WIRE_LENGTH);
			}
		}
		wire.align();
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}