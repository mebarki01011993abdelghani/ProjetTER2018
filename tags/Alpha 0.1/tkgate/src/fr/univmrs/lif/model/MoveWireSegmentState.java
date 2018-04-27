package fr.univmrs.lif.model;

import fr.univmrs.lif.model.wire.Wire;
import javafx.geometry.Point2D;

public class MoveWireSegmentState extends AbstractState {
	EditorControler edControl;
	Wire wire;
	int selSegment = -1;

	MoveWireSegmentState(EditorControler edControl, Wire wire, int selSegment) {
		this.edControl = edControl;
		this.wire = wire;
		this.selSegment = selSegment;
	}

	@Override
	public void mouseReleased(EditorContext context, Point2D point) {
		if (wire != null) {
			edControl.moveWireSegment(wire, point, selSegment);
			// TODO : il faut remttre le segment si edge est connecté à une
			// porte
			// TODO : retirer les cycles
			// TODO : éviter les portes
			// edControl.module.refactor(wire, edge);
			edControl.setSelectedWire(wire, false);
			wire.align();
			edControl.refactorConnections();
			context.getDraw().drawComponent();
			context.setState(new SelectState(edControl));
			context.getDraw().defaultCursor();
		}

	}

	@Override
	public void mouseDragged(EditorContext context, Point2D point) {
		if (!(wire == null)) {
			selSegment = edControl.module.moveWireSegment(wire, point, selSegment);
			context.getDraw().drawComponent();
		}
	}

}
