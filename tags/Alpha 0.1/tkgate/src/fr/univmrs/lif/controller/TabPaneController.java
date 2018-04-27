package fr.univmrs.lif.controller;

import fr.univmrs.lif.view.DrawingBoard;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabPaneController {
	
	private TabPane tabPane;
	private MainController mainController;
	
	public void initTabPane(TabPane tabPane){
		this.tabPane = tabPane;
		
		
	}
	
	public void addTab(String name, Node content){
		
		Tab newTab = new Tab(name, content);
		newTab.setClosable(true);
		newTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				mainController.setCurrentBoard((DrawingBoard) content);
				//scrollDrawingBoard = scrollPane;
				//edContext = drawingBoard.getContext();
			}
		});
		tabPane.getTabs().add(newTab);
		
	}

	public void injectMainController(MainController mainController) {
		this.mainController = mainController;
		
	}

}
