package fr.univmrs.lif.controller;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ModuleTreeViewController {
	
	private Image iconModule = new Image("/ModuleIcon.gif");
	private TreeView<String> moduleTreeView;
	private TreeItem<String> treeViewRoot;
	
	public ModuleTreeViewController(){
		
	}
	
	public void initModuleTreeView(TreeView<String> moduleTreeView){
		this.moduleTreeView = moduleTreeView;
		treeViewRoot = new TreeItem<String>("Main");
		treeViewRoot.setExpanded(true);
		
		this.moduleTreeView.setRoot(treeViewRoot);
		this.moduleTreeView.setShowRoot(true);
	}
	
	public void addModule(String name){
		TreeItem<String> item = new TreeItem<String>(name,new ImageView(iconModule));
		item.setExpanded(true);
		treeViewRoot.getChildren().add(item);
	}
	
	

}
