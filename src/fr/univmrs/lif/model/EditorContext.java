package fr.univmrs.lif.model;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.view.DrawingBoard;
import javafx.geometry.Point2D;

public class EditorContext {

	private AbstractState currentState;
	private DrawingBoard draw;
	
	public EditorContext(DrawingBoard draw, EditorController edControl) {
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

	public DrawingBoard getDraw() {
		return draw;
	}

	public void mousePressed(Point2D point) {
		currentState.mousePressed(this, point);
	}

	public void mouseReleased(Point2D point) {
		currentState.mouseReleased(this, point);
	}
	
	public void mouseDragged(Point2D point) {
		currentState.mouseDragged(this, point);
	}

	public void addComponent(ComponentType type) {
		currentState.addComponent(this, type);
		
	}
//	public void addComponent(ComponentType gtype,Point2D point) {
//		currentState.addComponent(this, gtype, point);
//		
//	}

	public void mouseMoved(Point2D point) {
		currentState.mouseMoved(this,point);
	}

}
