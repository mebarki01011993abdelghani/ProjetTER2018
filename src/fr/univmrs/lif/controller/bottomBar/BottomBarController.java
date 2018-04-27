package fr.univmrs.lif.controller.bottomBar;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;

public class BottomBarController implements Initializable {

	@FXML private ConsoleController consoleController;
	@FXML private ChronogramController chronogramController;
	
	@FXML
    private TitledPane bottomBar;

    @FXML
    private Tab consoleTab;

    @FXML
    private Tab chronogramTab;
    
  
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	public ChronogramController getChronogramController() {
		return chronogramController;
	}
}