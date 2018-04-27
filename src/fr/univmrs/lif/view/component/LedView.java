package fr.univmrs.lif.view.component;

import java.io.IOException;

import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.inputoutput.Led;
import fr.univmrs.lif.model.component.inputoutput.Switch;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

public class LedView extends Pane {

	Led model;

	@FXML
	private AnchorPane led;

	@FXML
	private Path diode;

	@FXML
	private Rectangle base;

	@FXML
	private Path cathode;

	@FXML
	private Path anode;

	public LedView(Component component) {
		model = (Led)component;

		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("LED.fxml")
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

		this.getChildren().add(led);
		model.activeProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if(model.getActive()){
					turnOn();
				}else{
					turnOff();
				}
			}
		});
	}


	private void turnOn(){
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				base.setFill(Color.RED);
				diode.setFill(Color.RED);
			}
		});

	}

	private void turnOff(){
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				base.setFill(Color.SILVER);
				diode.setFill(Color.SILVER);
			}
		});
	}




}
