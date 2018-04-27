package fr.univmrs.lif.model;

import fr.univmrs.lif.enumeration.ComponentType;
import javafx.geometry.Point2D;

public class PointSelectedState extends AbstractState {

	EditorController edControl;
	Point2D point;

	public PointSelectedState(EditorController edControl, Point2D p) {
		this.edControl = edControl;
		point = p;
//		System.out.println("Enter PointSelectedState");
	}

	@Override
	public void mousePressed(EditorContext context, Point2D point) {
		context.setState(new SelectState(edControl, point));
		context.mousePressed(point);
		// System.out.println("PointSelectedState: mousePressed");
	}

	@Override
	public void mouseReleased(EditorContext context, Point2D point) {
		this.point = point;
	}

	@Override
	public void mouseDragged(EditorContext context, Point2D point) {
	}

	@Override
	public void addComponent(EditorContext context, ComponentType type) {
		
//		(edControl.getModule()).addComponent(type, point);		
		context.setState(new SelectState(edControl));
	}
	
//	public void addComponent(EditorContext context, ComponentType type, Point2D point) {
//		context.getDraw().setCross(null);
//		(edControl.getModule()).addComponent(type, point);		
//		context.setState(new SelectState(edControl));
//	}
	
	
}
