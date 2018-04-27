package fr.univmrs.lif.controller.appBar;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.controller.simulation.SimulationController;
import fr.univmrs.lif.enumeration.Mode;
import fr.univmrs.lif.view.ToggleSwitch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AppBarController implements Initializable {
	
	private MainController mainController;
    private ToggleSwitch switchModeButton;
    
    @FXML private MenuBarController menuBarController;

    @FXML
    private VBox appBarContainer;

    @FXML
    private HBox toolBarContainer;

    @FXML
    private ToolBar toolBarEditor;
    
    @FXML
    private ToolBar toolBarSimulator;

    @FXML
    private Slider toolBarZoomSlider;

    @FXML
    private Button startSimulationButton;

    @FXML
    private Button pauseSimulationButton;

    @FXML
    private Button stopSimulationButton;

    public void injectMainController(MainController mainController){
		this.mainController = mainController;
		initSwitchMode();
		
		menuBarController.injectMainController(mainController);
	
	}
    
    private void initSwitchMode(){
    	switchModeButton = new ToggleSwitch(toolBarContainer.getHeight());
		toolBarContainer.getChildren().add(0, switchModeButton);		
		buildHandleModeChange();
		setEditionMode();
    }

	private void buildHandleModeChange() {
		switchModeButton.switchOnProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(switchModeButton.switchOnProperty().get()){
					
					setSimulationMode();
				}else{
					
					setEditionMode();
				}
			}
		});
	}

	public VBox getAppBar() {
		return appBarContainer;
	}

    @FXML
    void handleToolBarZoomSlider(MouseEvent event) {
    	mainController.zoom(toolBarZoomSlider.getValue());
    }
    
    @FXML
    void startSimulation(ActionEvent event) {
    	mainController.getSimulationController().startSimulation();
    }

    @FXML
    void pauseSimulation(ActionEvent event) {
    	mainController.getSimulationController().pauseSimulation();
    }

    @FXML
    void stopSimulation(ActionEvent event) {
    	mainController.getSimulationController().stopSimulation();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	public void fireSwitch(){
		switchModeButton.fire();
	}
	
	/**
	 * Test if a simulation can be done.
	 * Make available the simulation 
	 */
	private void setSimulationMode(){
		if(!mainController.canBeSimulate()){
			switchModeButton.fire();
			return;
		}
		mainController.setMode(Mode.SIMULATION);
		toolBarSimulator.setDisable(false);
		
		menuBarController.setDisable(true);
		mainController.getLibrarySide().setDisable(true);
		
		mainController.getDrawingBoardController().focusOnMain();
	}
	
	private void setEditionMode(){
		
		mainController.stopSimulationMode();
		
		mainController.setMode(Mode.EDITION);
		toolBarSimulator.setDisable(true);
		
		menuBarController.setDisable(false);
		mainController.getLibrarySide().setDisable(false);
	}

}
