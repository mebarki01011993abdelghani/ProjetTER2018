package tkgate;


import java.awt.Point;

import tkgate.view.DrawingController;


public interface EditorState {
    public void mousePressed(DrawingController context, Point point);
    public void mouseReleased(DrawingController context, Point point);
    public void mouseDragged(DrawingController context, Point point);
	public void addComponent(String gtype);
}
