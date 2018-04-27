package fr.univmrs.lif.view;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.enumeration.ComponentFamily;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.view.component.ComponentView;
import fr.univmrs.lif.view.component.PlugView;
import fr.univmrs.lif.view.component.View;
import fr.univmrs.lif.view.component.WireView;
import fr.univmrs.lif.view.popup.PopupType;
import fr.univmrs.lif.view.popup.TextBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ContextMenuViewFactory {


	public static ContextMenu buildContextMenu(Node view){
		//		ContextMenu contextMenu = null;
		if(view instanceof ComponentView){
			ContextMenu contextMenu = makeContextMenuComponentView((ComponentView) view);
			view.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent mouseEvent) {
					if(mouseEvent.getButton() == MouseButton.SECONDARY){
						contextMenu.show(((ComponentView) view).getComponentDraw(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
					}
					mouseEvent.consume();
				}
			});
			return contextMenu;

		}else if(view instanceof PlugView){
			ContextMenu contextMenu = makeContextMenuPlugView((PlugView) view);
			view.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent mouseEvent) {
					if(mouseEvent.getButton() == MouseButton.SECONDARY){
						contextMenu.show(view, mouseEvent.getScreenX(), mouseEvent.getScreenY());
					}
					mouseEvent.consume();
				}
			});
			return contextMenu;
		}
		else if(view instanceof WireView){
			ContextMenu contextMenu = makeContextMenuWireView((WireView) view);
			view.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent mouseEvent) {
					if(mouseEvent.getButton() == MouseButton.SECONDARY){
						contextMenu.show(view, mouseEvent.getScreenX(), mouseEvent.getScreenY());
					}
					mouseEvent.consume();
				}
			});
			return contextMenu;
		}

		return null;



	}


	private static ContextMenu makeContextMenuWireView(WireView view) {
		// TODO ICI pour type de cable 
		if(true /*Cable simple*/){
			return simpleWireContext(view);
		}

		return null;
	}


	private static ContextMenu simpleWireContext(WireView view) {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem renameItem   = new MenuItem("Rename...");
		MenuItem addSensorItem   = new MenuItem("Add Sensor");
		MenuItem clearSensorItem   = new MenuItem("Clear Sensor");
		MenuItem deleteItem   = new MenuItem("Delete");


		renameItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String newName = TextBox.display(PopupType.WIRE);
				if(newName != null){
					view.getModel().setName(newName);
				}
				event.consume();
			}
		});

		addSensorItem.setOnAction(a -> view.addSensor());
		clearSensorItem.setOnAction(a -> view.clearSensor());
		deleteItem.setOnAction(a -> view.delete());


		contextMenu.getItems().add(renameItem);
		contextMenu.getItems().add(addSensorItem);
		contextMenu.getItems().add(clearSensorItem);
		contextMenu.getItems().add(deleteItem);

		addSensorItem.disableProperty().bind(view.haveSensorProperty());
		clearSensorItem.disableProperty().bind(view.haveSensorProperty().not());

		return contextMenu;
	}


	/**
	 * Subfactory for building plug context menu 
	 * @param view
	 * @return
	 */
	private static ContextMenu makeContextMenuPlugView(PlugView view) {
		Plug model = view.getModel();

		if(model.getType() == PlugType.IN){
			return plugInContext(view);
		}
		else if(model.getType() == PlugType.OUT){
			return plugOutContext(view);
		}
		return null;
	}



	private static ContextMenu plugOutContext(PlugView view) {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem renameItem   = new MenuItem("Rename...");
		MenuItem deleteItem   = new MenuItem("Delete");


		renameItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String newName = TextBox.display(PopupType.PLUG);
				if(newName != null){
					view.getModel().setName(newName);
				}
				event.consume();
			}
		});

		deleteItem.setOnAction(a -> view.delete());


		contextMenu.getItems().add(renameItem);
		contextMenu.getItems().add(deleteItem);

		Component component = view.getModel().getComponent();

		if(component.getFamily() == ComponentFamily.GATE ||
				component.getType() == ComponentType.MODULE){

			deleteItem.setDisable(true);
		}
		else if(component.getFamily() == ComponentFamily.INPUT){

			deleteItem.disableProperty().bind(component.getOutputNumberProperty().lessThanOrEqualTo(1));

		}


		return contextMenu;
	}


	private static ContextMenu plugInContext(PlugView view) {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem renameItem   = new MenuItem("Rename...");
		MenuItem deleteItem   = new MenuItem("Delete");


		renameItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String newName = TextBox.display(PopupType.PLUG);
				if(newName != null){
					view.getModel().setName(newName);
				}
				event.consume();
			}
		});

		deleteItem.setOnAction(a -> view.delete());


		contextMenu.getItems().add(renameItem);
		contextMenu.getItems().add(deleteItem);

		Component component = view.getModel().getComponent();

		if(component.getType() == ComponentType.NOT || 
				component.getType() == ComponentType.MODULE ||
				(component.getFamily() == ComponentFamily.OUTPUT)){
			deleteItem.setDisable(true);
		}
		else if(component.getFamily() == ComponentFamily.GATE){

			deleteItem.disableProperty().bind(component.getInputNumberProperty().lessThanOrEqualTo(2));
		}
		


		return contextMenu;

	}



	private static ContextMenu makeContextMenuComponentView(ComponentView view) {
		Component model = view.getModel();

		if(model.getFamily() == ComponentFamily.GATE){
			return gateContext(view);
		}
		else if(model.getFamily() == ComponentFamily.INPUT){
			return inputContext(view);
		}
		else if(model.getFamily() == ComponentFamily.OUTPUT){
			return outputContext(view);
		}
		else if(model.getFamily() == ComponentFamily.MSI){
			return msiContext(view);
		}
		else if(model.getFamily() == ComponentFamily.ALU){
			return aluContext(view);
		}
		else if(model.getFamily() == ComponentFamily.MODULE){
			return moduleContext(view);
		}

		return null;
	}








	private static ContextMenu moduleContext(ComponentView view) {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem renameItem   = new MenuItem("Rename...");
		MenuItem addInputItem = new MenuItem("Add input");
		MenuItem addOutputItem   = new MenuItem("Add output");
		MenuItem deleteItem   = new MenuItem("Delete");


		renameItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String newName = TextBox.display(PopupType.GATE);
				if(newName != null){
					view.getModel().setName(newName);
				}
				event.consume();
			}
		});

		addInputItem.setOnAction(a -> view.addComponentInput());
		addOutputItem.setOnAction(a -> view.addComponentOutput());

		deleteItem.setOnAction(a -> view.delete());


		contextMenu.getItems().add(renameItem);
		contextMenu.getItems().add(addInputItem);
		contextMenu.getItems().add(addOutputItem);
		contextMenu.getItems().add(deleteItem);

		Component model = view.getModel();


		addInputItem.setDisable(true);
		addOutputItem.setDisable(true);
		return contextMenu;


	}


	/**
	 * Create a context menu for gates components
	 * @return
	 */
	private static ContextMenu gateContext(ComponentView view) {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem renameItem   = new MenuItem("Rename...");
		MenuItem addInputItem = new MenuItem("Add input");
		MenuItem addOutputItem   = new MenuItem("Add output");
		MenuItem deleteItem   = new MenuItem("Delete");


		renameItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String newName = TextBox.display(PopupType.GATE);
				if(newName != null){
					view.getModel().setName(newName);
				}
				event.consume();
			}
		});

		addInputItem.setOnAction(a -> view.addComponentInput());
		addOutputItem.setOnAction(a -> view.addComponentOutput());

		deleteItem.setOnAction(a -> view.delete());


		contextMenu.getItems().add(renameItem);
		contextMenu.getItems().add(addInputItem);
		contextMenu.getItems().add(addOutputItem);
		contextMenu.getItems().add(deleteItem);

		Component model = view.getModel();

		if(model.getType() == ComponentType.NOT){
			addInputItem.setDisable(true);
			addOutputItem.setDisable(true);
			return contextMenu;
		}

		addOutputItem.setDisable(true);

		addInputItem.disableProperty().bind(model.getInputNumberProperty().greaterThanOrEqualTo(Settings.GATE_MAX_INPUT));

		return contextMenu;

	}

	/**
	 * Create a context menu for inputs components
	 * @return
	 */
	private static ContextMenu inputContext(ComponentView view) {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem renameItem   = new MenuItem("Rename...");
		MenuItem addInputItem = new MenuItem("Add input");
		MenuItem addOutputItem   = new MenuItem("Add output");
		MenuItem deleteItem   = new MenuItem("Delete");


		renameItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String newName = TextBox.display(PopupType.INPUT);
				if(newName != null){
					view.getModel().setName(newName);
				}
				event.consume();
			}
		});

		addInputItem.setOnAction(a -> view.addComponentInput());
		addOutputItem.setOnAction(a -> view.addComponentOutput());
		deleteItem.setOnAction(a -> view.delete());

		contextMenu.getItems().add(renameItem);
		contextMenu.getItems().add(addInputItem);
		contextMenu.getItems().add(addOutputItem);
		contextMenu.getItems().add(deleteItem);

		Component model = view.getModel();

		if(view.getModel().getType() == ComponentType.VDD ||
				view.getModel().getType() == ComponentType.GND){

		}



		addInputItem.setDisable(true);

		if(view.getModel().getType() == ComponentType.VDD ||
				view.getModel().getType() == ComponentType.GND){
			addOutputItem.setDisable(true);
		}else{
			addOutputItem.disableProperty().bind(model.getOutputNumberProperty().
					greaterThanOrEqualTo(Settings.INPUT_MAX_OUTPUT));
		}

		return contextMenu;
	}

	/**
	 * Create a context menu for outputs components
	 * @return
	 */
	private static ContextMenu outputContext(ComponentView view) {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem renameItem   = new MenuItem("Rename...");
		MenuItem addInputItem = new MenuItem("Add input");
		MenuItem addOutputItem   = new MenuItem("Add output");
		MenuItem deleteItem   = new MenuItem("Delete");


		renameItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String newName = TextBox.display(PopupType.OUTPUT);
				if(newName != null){
					view.getModel().setName(newName);
				}
				event.consume();
			}
		});

		addInputItem.setOnAction(a -> view.addComponentInput());
		addOutputItem.setOnAction(a -> view.addComponentOutput());
		deleteItem.setOnAction(a -> view.delete());

		contextMenu.getItems().add(renameItem);
		contextMenu.getItems().add(addInputItem);
		contextMenu.getItems().add(addOutputItem);
		contextMenu.getItems().add(deleteItem);

		addOutputItem.setDisable(true);
		addInputItem.setDisable(true);


		return contextMenu;
	}

	private static ContextMenu msiContext(ComponentView view) {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem renameItem   = new MenuItem("Rename...");
		MenuItem addInputItem = new MenuItem("Add input");
		MenuItem addOutputItem   = new MenuItem("Add output");
		MenuItem deleteItem   = new MenuItem("Delete");


		renameItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String newName = TextBox.display(PopupType.OUTPUT);
				if(newName != null){
					view.getModel().setName(newName);
				}
				event.consume();
			}
		});

		addInputItem.setOnAction(a -> view.addComponentInput());
		addOutputItem.setOnAction(a -> view.addComponentOutput());
		deleteItem.setOnAction(a -> view.delete());

		contextMenu.getItems().add(renameItem);
		contextMenu.getItems().add(addInputItem);
		contextMenu.getItems().add(addOutputItem);
		contextMenu.getItems().add(deleteItem);

		addOutputItem.setDisable(true);
		addInputItem.setDisable(true);


		return contextMenu;
	}

	private static ContextMenu aluContext(ComponentView view) {
		ContextMenu contextMenu = new ContextMenu();

		MenuItem renameItem   = new MenuItem("Rename...");
		MenuItem addInputItem = new MenuItem("Add input");
		MenuItem addOutputItem   = new MenuItem("Add output");
		MenuItem deleteItem   = new MenuItem("Delete");


		renameItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String newName = TextBox.display(PopupType.OUTPUT);
				if(newName != null){
					view.getModel().setName(newName);
				}
				event.consume();
			}
		});

		addInputItem.setOnAction(a -> view.addComponentInput());
		addOutputItem.setOnAction(a -> view.addComponentOutput());
		deleteItem.setOnAction(a -> view.delete());

		contextMenu.getItems().add(renameItem);
		contextMenu.getItems().add(addInputItem);
		contextMenu.getItems().add(addOutputItem);
		contextMenu.getItems().add(deleteItem);

		addOutputItem.setDisable(true);
		addInputItem.setDisable(true);


		return contextMenu;
	}


}
