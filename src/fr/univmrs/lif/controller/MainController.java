package fr.univmrs.lif.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import fr.univmrs.lif.controller.appBar.AppBarController;
import fr.univmrs.lif.controller.bottomBar.BottomBarController;
import fr.univmrs.lif.controller.bottomBar.Console;
import fr.univmrs.lif.controller.dragAndDrop.DragAndDropController;
import fr.univmrs.lif.controller.drawingBoard.DrawingBoardController;
import fr.univmrs.lif.controller.library.LibraryController;
import fr.univmrs.lif.controller.properties.PropertiesController;
import fr.univmrs.lif.controller.simulation.SimulationController;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.Mode;
import fr.univmrs.lif.main.MainApp;
import fr.univmrs.lif.model.Project;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.model.simulation.SimulationCircuit;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.view.Canvas;
import fr.univmrs.lif.view.DrawingBoard;
import fr.univmrs.lif.view.library.LibraryDragIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class MainController implements Initializable{

	private SimulationController simulationController;
	private DragAndDropController dragAndDropController;


	@FXML private AppBarController appBarController;             // Top
	@FXML private LibraryController libraryController;           // Left
	@FXML private DrawingBoardController drawingBoardController; // Center
	@FXML private PropertiesController propertiesController;     // Right
	@FXML private BottomBarController bottomBarController;       // Bottom


	@FXML private AnchorPane root;

	@FXML private VBox appBar;
	@FXML private BorderPane librarySide;
	@FXML private TabPane drawingBoard;
	@FXML private BorderPane propertiesSide; 
	@FXML private TitledPane bottomBar;


	private static Project currentProject;
	private static Canvas currentBoard;
	private static File currentSave = null;

	private Mode mode;

	private static BooleanProperty onSimulation = new SimpleBooleanProperty(false);


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		libraryController.injectMainController(this);
		appBarController.injectMainController(this);	

		createProject("Untitled");

		simulationController = new SimulationController(this);
		dragAndDropController = new DragAndDropController(this);

		libraryController.initLibrary();
	}




	/**
	 * Create a new project
	 */
	public void createProject(String nameProject){
		if(currentProject != null){
			// TODO Demander save
			//liberer memoire (bind)
			// fermer ongles
			drawingBoardController.closeProject();
		}
		MainApp.upDateTitle(nameProject);
		currentProject = new Project(nameProject);
		// Creer le board
		drawingBoardController.createMainModuleBoard();

	}

	/**
	 * Create a new module
	 */
	public void createModule(String nameModule){

		Module module = new Module(nameModule);

		drawingBoardController.createModuleBoard(module);
		libraryController.addModule(module);
		currentProject.addModule(module);

	}

	/**
	 * Load a project
	 */
	public void loadProject(Project project){

		if(currentProject != null){
			// TODO Demander save
			//liberer memoire (bind)
			// fermer ongles
			drawingBoardController.closeProject();
		}

		MainApp.upDateTitle(project.getName());
		currentProject = project;


		drawingBoardController.loadMainModule(project.getMain());

		for(Module module : project.getModules()){
			drawingBoardController.loadModule(module);
			libraryController.addModule(module);
		}

	}


	public static void setCurrentBoard(Canvas currentBoard) {
		MainController.currentBoard = currentBoard;

	}

	public static Canvas getCurrentBoard() {
		return currentBoard;

	}

	public void setMode(Mode mode) {
		this.mode = mode;
		if(mode == Mode.SIMULATION){
			Console.println("Mode Simulation");
			setOnSimulation(true);
		}else if(mode == Mode.EDITION){
			Console.println("Mode Edition");
			setOnSimulation(false);
		}
	}

	public boolean canBeSimulate(){
		return simulationController.verifyCircuit();
	}

	public void stopSimulationMode(){
		if(simulationController != null)
			simulationController.stopSimulation();
	}


	public static ReadOnlyBooleanProperty onSimulationProperty() {
		return onSimulation;
	}

	public Boolean getOnSimulation() {
		return onSimulation.get();
	}

	public void setOnSimulation(Boolean onSimulation) {
		this.onSimulation.set(onSimulation);
	}

	public Mode getMode(){
		return mode;
	}

	public static Project getCurrentProject() {
		return currentProject;
	}

	public static void setCurrentProject(Project currentProject) {
		MainController.currentProject = currentProject;
	}

	public static File getCurrentSave() {
		return currentSave;
	}


	public static void setCurrentSave(File currentSave) {
		MainController.currentSave = currentSave;
	}

	// Layouts Getters

	public AnchorPane getRoot() {
		return root;
	}

	public BorderPane getLibrarySide() {
		return librarySide;
	}

	public TabPane getDrawingBoard() {
		return drawingBoard;
	}




	// Controllers Getters 


	public SimulationController getSimulationController() {
		return simulationController;
	}

	public DrawingBoardController getDrawingBoardController() {
		return drawingBoardController;
	}

	public LibraryController getLibraryController() {
		return libraryController;
	}

	public AppBarController getAppBarController() {
		return appBarController;
	}

	public BottomBarController getBottomAppController() {
		return bottomBarController;
	}

	public DragAndDropController getDragAndDropController() {
		return dragAndDropController;
	}




	public void zoom(double zoomValue) {
		drawingBoardController.zoom(zoomValue);
		
	}

}
