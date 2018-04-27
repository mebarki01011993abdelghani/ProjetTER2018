package fr.univmrs.lif.model;

import fr.univmrs.lif.model.wire.Wire;
import javafx.geometry.Point2D;

public class MoveWireEdgeState extends AbstractState {

	EditorController edControl;
	Wire wire;
	int edge = -1;

	MoveWireEdgeState(EditorController edControl, Wire wire, int edge) {
		this.edControl = edControl;
		this.wire = wire;
		this.edge = edge;
		System.out.println("Enter MoveWireEdgeState");
	}

	@Override
	public void mouseReleased(EditorContext context, Point2D point) {
		edControl.releaseWireEdge(wire, edge, point);
		edControl.setSelectedWire(wire, false);
		edControl.refactorConnections();
		context.getDraw().defaultCursor();
		context.setState(new SelectState(edControl));
//		context.getDraw().drawComponent();
		context.getDraw().defaultCursor();

	}

	@Override
	public void mouseDragged(EditorContext context, Point2D point) {
		edControl.moveWireEdge(wire, edge, point);
//		context.getDraw().drawComponent();
	}

}
