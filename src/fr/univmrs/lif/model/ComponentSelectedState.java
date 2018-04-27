package fr.univmrs.lif.model;

import fr.univmrs.lif.model.component.Component;
import javafx.geometry.Point2D;

public class ComponentSelectedState extends AbstractState {

	EditorController edControl;
	Component c = null;
	Point2D initialLocation = null;

	public ComponentSelectedState(EditorController edControl, Component comp, Point2D point) {
		this.edControl = edControl;
		c = comp;
		initialLocation = point;
//		System.out.println("Enter ComponentSelectedState");

	}

	@Override
	public void mousePressed(EditorContext context, Point2D point) {
		System.out.println("ComponentSelectedState: mouse pressed");
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(EditorContext context, Point2D point) {
		System.out.println("ComponentSelectedState: mouse released");
//		if (c != null) {
//			if (!edControl.isAlone(c))
//				c.move(initialLocation);
//			edControl.refactorConnections();
//			c.setSelected(false);
//		}
		context.getDraw().defaultCursor();
		context.setState(new SelectState(edControl));
//		context.getDraw().drawComponent();
	}

	@Override
	public void mouseDragged(EditorContext context, Point2D point) {
		System.out.println("ComponentSelectedState: mouse dragged");
		if (!(c == null)) {
			// edControl.module.setComponentLocation(c, point);
			//c.move(point);
			
//			context.getDraw().drawComponent();
		}
	}

}
