package tkgate;



import java.awt.geom.Point2D;




public abstract class AbstractState {
		public void mousePressed(EditorContext context, Point2D.Double p){}
	    public void mouseReleased(EditorContext context, Point2D.Double p){}
	    public void mouseDragged(EditorContext context, Point2D.Double p){}
		public void addComponent(EditorContext editorContext, String gtype){}
}
