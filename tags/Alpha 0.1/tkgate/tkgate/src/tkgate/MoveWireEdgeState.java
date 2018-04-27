package tkgate;


import tkgate.modele.Wire;

import java.awt.geom.Point2D;

import tkgate.EditorContext;

public class MoveWireEdgeState extends AbstractState {

	EditorControler edControl;
	Wire wire;
	int edge = -1;

	MoveWireEdgeState(EditorControler edControl, Wire wire, int edge) {
		this.edControl = edControl;
		this.wire = wire;
		this.edge = edge;
		System.out.println("Enter MoveWireEdgeState");
	}

	@Override
	public void mouseReleased(EditorContext context, Point2D.Double point) {
		edControl.releaseWireEdge(wire, edge, point);
		edControl.setSelectedWire(wire, false);
		edControl.refactorConnections();
		context.getDraw().defaultCursor();
		context.setState(new SelectState(edControl));
		context.getDraw().drawComponent();
		context.getDraw().defaultCursor();

	}

	@Override
	public void mouseDragged(EditorContext context, Point2D.Double point) {
		edControl.moveWireEdge(wire, edge, point);
		context.getDraw().drawComponent();
	}

}
