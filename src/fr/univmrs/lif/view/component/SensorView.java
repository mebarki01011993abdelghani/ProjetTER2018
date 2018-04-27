package fr.univmrs.lif.view.component;

import java.io.IOException;

import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.inputoutput.Led;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class SensorView extends Pane{
	
	 @FXML private Rectangle handle;
	 @FXML
	    private Pane sensor;
	 
	 public SensorView() {

		
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("Sensor.fxml")
					);

			fxmlLoader.setController(this);

			try { 
				fxmlLoader.load();
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
		}
	 
	 @FXML
		private void initialize() {

		 	this.getChildren().add(sensor);
			
		}

}
