package fr.univmrs.lif.view;



import javafx.geometry.Dimension2D;
import javafx.scene.paint.Color;

public final class Settings {
	public static int GATE_WIDTH = 35; 
	public static int GATE_HEIGHT = 20;
	public static Dimension2D GATE_DIMENSION= new Dimension2D(GATE_WIDTH,GATE_HEIGHT);
	public static int JOINT_SIZE = 4;
	public static Dimension2D JOINT_DIMENSION = new Dimension2D(JOINT_SIZE,JOINT_SIZE);
	public static int WIRE_LENGTH = 10; 
	public static int MOUSE_PRECISION = 2;
	public static Color WIRE_COLOR = Color.GREEN.darker();
	public static Color SELECTED_WIRE_COLOR = WIRE_COLOR.darker();
	public static Color GATE_COLOR = Color.BLUE.darker();
	public static Color SELECTED_GATE_COLOR = GATE_COLOR.darker();
	
}
