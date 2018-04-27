package fr.univmrs.lif.view;

import java.util.ArrayList;
import java.util.List;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.ComponentFactory;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.component.inputoutput.InputModule;
import fr.univmrs.lif.model.component.inputoutput.OutputModule;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.Point2DProperty;
import fr.univmrs.lif.view.component.ComponentView;
import fr.univmrs.lif.view.component.SwitchView;
import fr.univmrs.lif.view.component.WireView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import jfxtras.labs.util.NodeUtil;

public class Canvas extends Pane{

	private Module module;
	private Node selected;

	List<ComponentView> modulesView = new ArrayList<>();

	public Canvas(Module module){

		this.module = module;
		//		disableCanvasPolicy();
		//		onMouseClicked();

	}

	private void disableCanvasPolicy() {
		MainController.onSimulationProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				for(Node n : getChildren()){

					n.setDisable(newValue);

				}

			}
		});

	}

	public void printAll(){
		System.out.println("Model");
		for(Component c : module.getComponents()){
			System.out.println(c);
		}
		for(Wire w : module.getAllWires()){
			System.out.println(w);
		}

		System.out.println("View");
		for(Node n : getChildren()){
			System.out.println(n);
		}
	}

	public void setSelectionItem(Node n){
		selected = n;
		//		System.out.println(n+" selected !");
		//		((ComponentView )n).setLoadPosition(new Point2D(0.0, 0.0));

	}

	//	private void onMouseClicked(){
	//		Canvas self = this;
	//		addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
	//
	//			@Override
	//			public void handle(MouseEvent event) {
	//				Node n = NodeUtil.getNode(self, event.getSceneX(),  event.getSceneY(), ComponentView.class);
	//				if(n != null){
	//					System.out.println(n+" selected !");
	//				}else
	//					System.out.println("no selection !");
	//			}
	//		});
	//	}

	public void addComponent(ComponentType type, Point2D position) {

		position = sceneToLocal(position);
		Component newComponent = ComponentFactory.buildComponent(type);

		// mise a jour du modele
		module.getComponents().add(newComponent);

		// creation de la vue
		ComponentView newComponentView = new ComponentView(newComponent);
		getChildren().add(newComponentView);
		newComponentView.initialize();
		newComponentView.setDropPosition(position);

		if(type == ComponentType.INPUTMODULE){
			Plug plugIn = new Plug(module, PlugType.IN);

			((InputModule)newComponent).setPlugIn(plugIn);
			module.addInput(plugIn);
		}
		else if(type == ComponentType.OUTPUTMODULE){
			Plug plugOut = new Plug(module, PlugType.OUT);

			((OutputModule)newComponent).setPlugOut(plugOut);
			module.addOutput(plugOut);
		}
			
	}

	public void addComponentModule(String value, Point2D position) {
		position = sceneToLocal(position);
		Component newComponent = null;

		for(Component module : MainController.getCurrentProject().getModules()){

			if(module.getName().equals(value))
				newComponent = module;
		}

		if(newComponent == null)
			return;
	

		

				// mise a jour du modele
				module.getComponents().add(newComponent);
		
				// creation de la vue
				ComponentView newComponentView = new ComponentView(newComponent);
				getChildren().add(newComponentView);
				newComponentView.initialize();
				newComponentView.setDropPosition(position);
				modulesView.add(newComponentView);

	}

	public void loadComponent(Component component) {

		Point2D position = new Point2D(component.getPosition().getX(),component.getPosition().getY());

		// creation de la vue
		ComponentView newComponentView = new ComponentView(component);
		getChildren().add(newComponentView);

		newComponentView.initialize();
		newComponentView.setLoadPosition(position);

		// Module ?
		if(component.getType() == ComponentType.MODULE)
			modulesView.add(newComponentView);
	}

	public void loadWire(Wire wire) {

		WireView wireView = new WireView(this, wire);

		// creation de la vue
		getChildren().add(wireView);

	}



	public Module getModule(){
		return module;
	}

	




}
