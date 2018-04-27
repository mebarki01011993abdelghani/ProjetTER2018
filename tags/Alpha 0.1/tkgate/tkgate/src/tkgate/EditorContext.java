package tkgate;




import java.awt.geom.Point2D;

import javafx.scene.canvas.Canvas;
import tkgate.view.DrawingController;



public class EditorContext {

	private AbstractState currentState;
	private  DrawingController draw;
	



	
	public EditorContext(DrawingController draw, EditorControler edControl) {
		currentState = new SelectState(edControl);
	//	this.edControl = edControl;
		this.draw = draw;
	}

	public void setState(AbstractState state) {
		currentState = state;
		//TODO getModulePanel().repaint();
	}

	public AbstractState getState() {
		return currentState;
	}

	public DrawingController getDraw() {
		return draw;
	}

	public void mousePressed(Point2D.Double point) {
		currentState.mousePressed(this, point);
	}

	public void mouseReleased(Point2D.Double point) {
		currentState.mouseReleased(this, point);
	}

	
	
	public void mouseDragged(Point2D.Double point) {
		currentState.mouseDragged(this, point);
	}

	public void addComponent(String gtype) {
		currentState.addComponent(this, gtype);
		
	}

	

	
	
}
