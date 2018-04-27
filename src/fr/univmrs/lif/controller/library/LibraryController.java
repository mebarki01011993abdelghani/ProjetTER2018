package fr.univmrs.lif.controller.library;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.controller.appBar.AppBarController;
import fr.univmrs.lif.controller.dragAndDrop.DragContainer;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.view.library.LibraryComponent;
import fr.univmrs.lif.view.library.LibraryDragIcon;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

public class LibraryController {

//	@FXML private ModuleListController moduleListController;

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

	@FXML
	private VBox libraryModule;

	private MainController mainController;

	public void injectMainController(MainController mainController){
		this.mainController = mainController;

		
	}


	public void initLibrary(){
		initIconsGate();
		initIconsIO();
		initIconsMSI();
		initIconsALU();
		initIconsMEM();
	}


	private void initIconsGate(){

		ArrayList<ComponentType> items = new ArrayList<>();
		// Add new compoment to gate library section
		items.add(ComponentType.NOT);
		items.add(ComponentType.AND);
		items.add(ComponentType.NAND);
		items.add(ComponentType.OR);
		items.add(ComponentType.NOR);
		items.add(ComponentType.XOR);
		items.add(ComponentType.XNOR);
		// end
		
		for(ComponentType type : items){
			LibraryComponent icon = new LibraryComponent();
			icon.setType(type);
			mainController.getDragAndDropController().addDragDetection(icon.getComponentIcon());
			libraryGate.getChildren().add(icon);
			libraryGate.getChildren().add(new Separator(Orientation.HORIZONTAL));
		}

	}

	private void initIconsIO() {

		ArrayList<ComponentType> items = new ArrayList<>();
		// Add new compoment to input/output library section
		items.add(ComponentType.VDD);
		items.add(ComponentType.GND);
		items.add(ComponentType.SWITCH);
		items.add(ComponentType.LED);
		items.add(ComponentType.CLOCK);
		items.add(ComponentType.DISPLAY);
		items.add(ComponentType.INPUTMODULE);
		items.add(ComponentType.OUTPUTMODULE);
		
		// end
		
		for(ComponentType type : items){
			LibraryComponent icon = new LibraryComponent();
			icon.setType(type);
			mainController.getDragAndDropController().addDragDetection(icon.getComponentIcon());
			libraryIO.getChildren().add(icon);
			libraryIO.getChildren().add(new Separator(Orientation.HORIZONTAL));
		}
		
	}
	
	private void initIconsMSI() {

		ArrayList<ComponentType> items = new ArrayList<>();
		// Add new compoment to input/output library section
		items.add(ComponentType.MUX);
		items.add(ComponentType.DECODER);
		// end
		
		for(ComponentType type : items){
			LibraryComponent icon = new LibraryComponent();
			icon.setType(type);
			mainController.getDragAndDropController().addDragDetection(icon.getComponentIcon());
			libraryMSI.getChildren().add(icon);
			libraryMSI.getChildren().add(new Separator(Orientation.HORIZONTAL));
		}
		
	}
	
	private void initIconsALU() {

		ArrayList<ComponentType> items = new ArrayList<>();
		// Add new compoment to input/output library section
		items.add(ComponentType.ADDER);
		// end
		
		for(ComponentType type : items){
			LibraryComponent icon = new LibraryComponent();
			icon.setType(type);
			mainController.getDragAndDropController().addDragDetection(icon.getComponentIcon());
			libraryALU.getChildren().add(icon);
			libraryALU.getChildren().add(new Separator(Orientation.HORIZONTAL));
		}
		
	}
	
	private void initIconsMEM() {

		ArrayList<ComponentType> items = new ArrayList<>();
		// Add new compoment to input/output library section
	
		// end
		
		for(ComponentType type : items){
			LibraryComponent icon = new LibraryComponent();
			icon.setType(type);
			mainController.getDragAndDropController().addDragDetection(icon.getComponentIcon());
			libraryMemory.getChildren().add(icon);
			libraryMemory.getChildren().add(new Separator(Orientation.HORIZONTAL));
		}
		
	}


	public void addModule(Module module) {
		LibraryComponent icon = new LibraryComponent();
		icon.setType(module);
		mainController.getDragAndDropController().addDragDetectionModule(icon.getComponentIcon(),module);
		libraryModule.getChildren().add(icon);
		libraryModule.getChildren().add(new Separator(Orientation.HORIZONTAL));

	}


}
