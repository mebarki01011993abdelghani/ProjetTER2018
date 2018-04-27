package fr.univmrs.lif.model;


import fr.univmrs.lif.tools.ComponentType;
import javafx.geometry.Point2D;

public abstract class AbstractState {
		public void mousePressed(EditorContext context, Point2D p){}
	    public void mouseReleased(EditorContext context, Point2D p){}
	    public void mouseDragged(EditorContext context, Point2D p){}
		public void addComponent(EditorContext editorContext, ComponentType gtype){}
		public void addComponent(EditorContext context, ComponentType type, Point2D point) {}
		public void mouseMoved(EditorContext editorContext, Point2D point){}
		
}
