package tkgate;



import java.awt.geom.Point2D;

import tkgate.EditorContext;
import tkgate.modele.Point;

public class PointSelectedState extends AbstractState {

	EditorControler edControl;
	Point2D.Double point;

	public PointSelectedState(EditorControler edControl, Point2D.Double p) {
		this.edControl = edControl;
		point = p;
		System.out.println("Enter PointSelectedState");
	}

	@Override
	public void mousePressed(EditorContext context, Point2D.Double point) {
		context.setState(new SelectState(edControl, point));
		context.mousePressed(point);
		// System.out.println("PointSelectedState: mousePressed");
	}

	@Override
	public void mouseReleased(EditorContext context, Point2D.Double point) {
	}

	@Override
	public void mouseDragged(EditorContext context, Point2D.Double point) {
	}

	@Override
	public void addComponent(EditorContext context, String gtype) {
		context.getDraw().setCross(null);
		(edControl.getModule()).addComponent(gtype, point);		
		context.setState(new SelectState(edControl));
	}
}
