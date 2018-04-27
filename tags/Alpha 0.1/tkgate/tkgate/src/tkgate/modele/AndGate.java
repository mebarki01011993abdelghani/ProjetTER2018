package tkgate.modele;


import java.awt.Dimension;
import java.awt.geom.Point2D;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import tkgate.Settings;
public class AndGate extends Gate{

	private static int defaultInputsSize = 2; 
	private static int defaultOutputsSize = 1;
	private static String gateType = "And";
	
	public AndGate(String name, Point2D.Double pos, Dimension dim, int freeWire) {
		super(name, pos, dim, freeWire, defaultInputsSize, defaultOutputsSize);
	}

	@Override
	public void draw(GraphicsContext gc) {
		super.draw(gc);
		gc.setStroke(Settings.GATE_COLOR);
		if (selected){
			gc.setLineWidth(3);
		}else 
			gc.setLineWidth(2);
		gc.strokeLine(rect.x, rect.y, rect.x+ rect.width/2, rect.y);
		gc.strokeLine(rect.x, rect.y, rect.x,  rect.height + rect.y);
		gc.strokeLine(rect.x, rect.y + rect.height, rect.x+ rect.width/2, rect.height + rect.y);
		gc.strokeArc(rect.x, rect.y, rect.width, rect.height, 270, 180, ArcType.OPEN);		
	}

	@Override
	public int getDefaultInputsSize() {
		return defaultInputsSize;
	}

	@Override
	public int getDefaultOutputsSize() {
		return defaultOutputsSize;
	}
	
	public String toString(){
		return "[" + inputs.toString() + "," + outputs.toString() + "]"; 
	}

	


	

	

}
