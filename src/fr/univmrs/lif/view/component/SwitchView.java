package fr.univmrs.lif.view.component;

import java.io.IOException;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.inputoutput.Switch;
import fr.univmrs.lif.model.simulation.Simulation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class SwitchView extends Pane {

	@FXML
	private AnchorPane switchView;

	@FXML
	private ToggleButton switchButton;

	Switch model;

	public SwitchView(Component component) {
		model = (Switch)component;
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("Switch.fxml")
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
		
		this.getChildren().add(switchView);
	
		// activer le bouton si le canvas est disable
//		switchButton.disabledProperty().addListener(new ChangeListener<Boolean>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//				
//					switchButton.setDisable(false);
//					System.out.println("setDisable "+!newValue);
//								
//			}
//		});
	}	

	@FXML
	void handleSwitchClicked(ActionEvent event) {
		int state = model.getSimulationState();

		if(model.getOn()){
			switchButton.setText("Off");
			if(state == Simulation.PROCESSING){
				model.switchOff();
			}else{
				model.setOn(false);
			}
		}else{
			switchButton.setText("On");
			if(state == Simulation.PROCESSING){
				model.switchOn();
			}else{
				model.setOn(true);
			}
		}

	}
	
	

}
