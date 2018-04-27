package fr.univmrs.lif.controller.library;

import java.net.URL;
import java.util.ResourceBundle;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.tools.ComponentType;
import fr.univmrs.lif.view.library.LibraryComponent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class LibraryController implements Initializable {

	@FXML
    private Accordion library;

    @FXML
    private VBox libraryGate;

    @FXML
    private VBox libraryIO;

    @FXML
    private VBox libraryMSI;

    @FXML
    private VBox libraryALU;

    @FXML
    private VBox libraryMemory;

	private MainController mainController;

	public void injectMainController(MainController mainController){
		this.mainController = mainController;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		
	}
	
	public void initLibrary(){
		initGateIcon();
	}


	private void initGateIcon(){
		//Add all simple gates
		for (int i = 0; i < 1; i++) {

			LibraryComponent lc = new LibraryComponent();
			lc.setType(ComponentType.values()[i]);
			mainController.addDragDetection(lc.getComponentIcon());

			libraryGate.getChildren().add(lc);
			
			
		}
	}








}
