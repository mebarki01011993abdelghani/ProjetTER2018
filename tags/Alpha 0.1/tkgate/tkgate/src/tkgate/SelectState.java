package tkgate;

import tkgate.modele.Component;
import tkgate.modele.Wire;

import java.awt.geom.Point2D;

import tkgate.EditorContext;
import tkgate.EditorControler;

public class SelectState extends AbstractState {

	private EditorControler edControl;
	private Point2D.Double point;

	public SelectState(EditorControler edControl) {
		this.edControl = edControl;
		System.out.println("Enter SelectState");	
	}

	SelectState(EditorControler edControl, Point2D.Double p) {
		this.edControl = edControl;
		point = p;
	}

	@Override
	public void mousePressed(EditorContext context, Point2D.Double point) {
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
			context.getDraw().drawCross(point.x, point.y);
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
}
