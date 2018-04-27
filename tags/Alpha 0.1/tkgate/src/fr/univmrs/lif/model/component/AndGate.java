package fr.univmrs.lif.model.component;


import java.awt.Dimension;

import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.tools.ComponentType;
import fr.univmrs.lif.tools.Constant;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

public class AndGate extends Gate {

	
	private static int defaultInputsSize = 2; 
	private static int defaultOutputsSize = 1;
	private static ComponentType gateType = ComponentType.AND;
	
	public AndGate(String name, Point2D pos, Dimension dim, int freeWire) {
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
		gc.strokeLine(rect.getMinX(), rect.getMinY(), rect.getMinX()+ rect.getWidth()/2, rect.getMinY());
		gc.strokeLine(rect.getMinX(), rect.getMinY(), rect.getMinX(),  rect.getHeight() + rect.getMinY());
		gc.strokeLine(rect.getMinX(), rect.getMinY() + rect.getHeight(), rect.getMinX()+ rect.getWidth()/2, rect.getHeight() + rect.getMinY());
		gc.strokeArc(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight(), 270, 180, ArcType.OPEN);		
		
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

	
	@Override
	public void processing() {
		try {
			Thread.sleep(Constant.TIME_PROCESSING);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(this.inputs.get(0).getWire().getState() == 1 && this.inputs.get(0).getWire().getState() == 1) this.outputs.get(0).getWire().setState(1);
		else this.outputs.get(0).getWire().setState(0);
		
		this.outputs.get(0).getWire().propagate();
	}
}
