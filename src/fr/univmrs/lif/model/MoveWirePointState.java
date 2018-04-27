package fr.univmrs.lif.model;

import fr.univmrs.lif.model.wire.Wire;
import javafx.geometry.Point2D;

public class MoveWirePointState extends AbstractState {
	EditorController edControl;
	Wire wire;
	int selPoint = -1;

	MoveWirePointState(EditorController edControl, Wire wire, int selIndex) {
		this.edControl = edControl;
		this.wire = wire;
		this.selPoint = selIndex;
	}

	@Override
	public void mouseReleased(EditorContext context, Point2D point) {
//		if (wire != null) {
//			edControl.moveWireSegment(wire, point, selPoint);
//			// TODO : il faut remttre le segment si edge est connecté à une
//			// porte
//			// TODO : retirer les cycles
//			// TODO : éviter les portes
//			// edControl.module.refactor(wire, edge);
//			edControl.setSelectedWire(wire, false);
//			wire.align();
//			edControl.refactorConnections();
////			context.getDraw().drawComponent();
//			context.setState(new SelectState(edControl));
//			context.getDraw().defaultCursor();
//		}

	}

	@Override
	public void mouseDragged(EditorContext context, Point2D point) {
//		if (!(wire == null)) {
//			selPoint = edControl.module.moveWirePoint(wire, point, selPoint);
//			context.getDraw().drawComponent();
//		}
	}

}
