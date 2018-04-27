package fr.univmrs.lif.controller.drawingBoard;

import java.net.URL;
import java.util.ResourceBundle;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.controller.bottomBar.Console;
import fr.univmrs.lif.model.Project;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.view.Canvas;
import fr.univmrs.lif.view.DrawingBoard;
import fr.univmrs.lif.view.DrawingBoardTab;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class DrawingBoardController implements Initializable{
	private MainController mainController;
	
    @FXML
    private TabPane drawingBoard;
	
    private DrawingBoardTab mainTab;
    
    private SingleSelectionModel<Tab> selectionModel;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		selectionModel = drawingBoard.getSelectionModel();

	}
	
	public void injectMainController(MainController mainController) {
		this.mainController = mainController;
		
	}
	
	/**
	 * Create the special module main
	 */
	public void createMainModuleBoard(){
		// recuperation du module pricipale
		Module main = MainController.getCurrentProject().getMain();
		
		// creation de l'ongle pour le module principale
		mainTab = new DrawingBoardTab(main);
		mainTab.setClosable(false);
		mainTab.handelSelection();
		drawingBoard.getTabs().add(mainTab);
		selectionModel.selectLast();
	}
	
	public void createMainModuleBoard(Module main){
		// recuperation du module pricipale
//		main = MainController.getCurrentProject().getMain();
		
		// creation de l'ongle pour le module principale
		mainTab = new DrawingBoardTab(main);
		mainTab.setClosable(false);
		mainTab.handelSelection();
		drawingBoard.getTabs().add(mainTab);
		selectionModel.selectLast();
	}
	
	public void createModuleBoard(Module module) {
		
		// creation de l'ongle pour le module principale
		DrawingBoardTab tab = new DrawingBoardTab(module);
		tab.setClosable(false);
		tab.handelSelection();
		drawingBoard.getTabs().add(tab);
		selectionModel.selectLast();
		Console.println("Module " + module.getName() + " created.");
		
	}
	
	public void loadMainModule(Module mainModule) {
		createMainModuleBoard(mainModule);
		
		for (Component component : mainModule.getComponents()) {
			MainController.getCurrentBoard().loadComponent(component);
		}

		for (Wire wire : mainModule.getWires()) {
			MainController.getCurrentBoard().loadWire(wire);

		}
		Console.println("Module " + mainModule.getName() + " loaded.");
	}
	
	public void loadModule(Module module){
		createModuleBoard(module);

		for (Component component : module.getComponents()) {
			MainController.getCurrentBoard().loadComponent(component);
		}

		for (Wire wire : module.getWires()) {
			MainController.getCurrentBoard().loadWire(wire);

		}
		Console.println("Module " + module.getName() + " loaded.");
	}

	public void closeProject() {
		drawingBoard.getTabs().clear();
		Console.println("Project " + MainController.getCurrentProject().getName() + " closed.");
	}

	public void focusOnMain() {
		
		selectionModel.select(mainTab);
	}

	public void zoom(double zoomValue) {
		for(Tab tab : drawingBoard.getTabs())
			((DrawingBoardTab) tab).zoom(zoomValue);
		
	}

	
	
	
		

}