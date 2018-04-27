package fr.univmrs.lif.model;


import java.awt.Dimension;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.scene.paint.Color;

public final class Settings {
	
	public static final IntegerProperty GATE_MAX_INPUT = new SimpleIntegerProperty(4);
	public static final IntegerProperty INPUT_MAX_OUTPUT = new SimpleIntegerProperty(4);
	public static final IntegerProperty OUTPUT_MAX_INPUT = new SimpleIntegerProperty(1);
	
	
	public static int GATE_WIDTH = 35; 
	public static int GATE_HEIGHT = 20;
	public static int MODULE_WIDTH = 50; 
	public static int MODULE_HEIGHT = 50;
	public static Dimension GATE_DIMENSION= new Dimension(GATE_WIDTH,GATE_HEIGHT);
	public static int JOINT_SIZE = 4;
	public static Dimension JOINT_DIMENSION = new Dimension(JOINT_SIZE,JOINT_SIZE);
	public static IntegerProperty WIRE_LENGTH = new SimpleIntegerProperty(10); 
	public static int MOUSE_PRECISION = 2;
	public static Color WIRE_COLOR = Color.GREEN.darker();
	public static Color SELECTED_WIRE_COLOR = WIRE_COLOR.darker();
	public static Color GATE_COLOR = Color.BLUE.darker();
	public static Color SELECTED_GATE_COLOR = GATE_COLOR.darker();
		
	
}
