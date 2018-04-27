package fr.univmrs.lif.model;

import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.wire.Wire;
import javafx.geometry.Point2D;

public class SelectState extends AbstractState {

	private EditorControler edControl;
	private Point2D point;

	public SelectState(EditorControler edControl) {
		this.edControl = edControl;
		System.out.println("Enter SelectState");	
	}

	public SelectState(EditorControler edControl, Point2D p) {
		this.edControl = edControl;
		point = p;
	}

	@Override
	public void mousePressed(EditorContext context, Point2D point) {
		System.out.println("SelectState: Mouse pressed");
		Component comp = edControl.ComponentContaining(point);
		if (comp != null) {
			context.getDraw().setCross(null);
			comp.setSelected(true);
			context.getDraw().handCursor();
			context.setState(new ComponentSelectedState(edControl, comp, point));
			return;
		}
		Wire wire = edControl.wireContaining(point);
		if (wire == null && edControl.isFreePlace(point)) {
			context.getDraw().clear();
			context.getDraw().drawCross(point.getX(), point.getY());
			context.getDraw().drawComponent();
			context.setState(new PointSelectedState(edControl, point));
		}
		if (wire != null) {
			context.getDraw().setCross(null);
			int selIndex = edControl.indexOfSelectedPoint(wire, point);
			if (edControl.isFreeEdge(selIndex, wire)) {
				context.getDraw().solderCursor();
				edControl.setSelectedWire(wire, true);
				context.setState(new MoveWireEdgeState(edControl, wire, selIndex));
			} else if (selIndex != -1) {
				context.getDraw().handCursor();
				edControl.setSelectedWire(wire, true);
				context.setState(new MoveWirePointState(edControl, wire, selIndex));
			} else {
				context.getDraw().handCursor();
				edControl.setSelectedWire(wire, true);
				int selSegment = edControl.getModule().getSelSegment(wire, point);
				context.setState(new MoveWireSegmentState(edControl, wire, selSegment));
			}
		}
	}
	@Override
	public void mouseMoved(EditorContext context, Point2D point) {
		Wire wire = edControl.wireContaining(point);
		Component comp = edControl.ComponentContaining(point);
		if (comp != null) {

			context.getDraw().handCursor();

		}else if (wire != null) {
			int selIndex = edControl.indexOfSelectedPoint(wire, point);
			if (edControl.isFreeEdge(selIndex, wire)) {

				context.getDraw().solderCursor();
			} else if (selIndex != -1) {
				context.getDraw().handCursor();

			} else {
				context.getDraw().handCursor();

			}
		}else{
			context.getDraw().defaultCursor();
		}

		


	}

}
