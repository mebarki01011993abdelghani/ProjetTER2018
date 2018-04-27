package fr.univmrs.lif.view.bottomBar;


import fr.univmrs.lif.model.simulation.Sensor;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;

public class ChronogramView extends Pane{
	
	final private Polyline line;
	private Line underline;
	StringProperty name = new SimpleStringProperty();
	
	
	double oldState = -1;
	
	double high = 0;
	double low = 20;
	
	Sensor sensor;
	
	public ChronogramView(Sensor sensor){
		setPadding(new Insets(5));
		this.sensor = sensor;
		
		line = new Polyline();
		line.setStroke(Color.RED);
		underline = new Line(0, low+1, 0, low+1);
		
		getChildren().add(line);
		getChildren().add(underline);
	
		
	}

	public void update(double time){
		int state = sensor.getSensor_state();
	    if(oldState != state || line.getPoints().isEmpty()){
	    
	    	if(state == 0){
	    		
	    		line.getPoints().add(time);
	    		line.getPoints().add(high);
	    		
	    		line.getPoints().add(time);
	    		line.getPoints().add(low);
	    		line.getPoints().add(time);
	    		line.getPoints().add(low);
	    	}
	    	else{
	    		line.getPoints().add(time);
	    		line.getPoints().add(low);
	    		
	    		line.getPoints().add(time);
	    		line.getPoints().add(high);
	    		line.getPoints().add(time);
	    		line.getPoints().add(high);
	    	}
	    }else{
	    	int size = line.getPoints().size();

	    	if(state == 0){
	    		line.getPoints().remove(size-2, size);
	    		line.getPoints().add(time);
	    		line.getPoints().add(low);
	    	}else{
	    		line.getPoints().remove(size-2, size);
	    		line.getPoints().add(time);
	    		line.getPoints().add(high);
	    	}
	    }
	    underline.setEndX(time);
		oldState = state;		
	}

	public void clear() {
		line.getPoints().clear();
		underline.setEndX(0);
		
	}

	public Sensor getSensor() {
		
		return sensor;
	}
	
	
	
}
