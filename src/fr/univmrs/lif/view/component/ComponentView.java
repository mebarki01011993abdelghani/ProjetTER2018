package fr.univmrs.lif.view.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


import fr.univmrs.lif.UndoRedo.ComponentChange;
import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.controller.appBar.MenuBarController;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.main.MainApp;
import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.component.inputoutput.InputModule;
import fr.univmrs.lif.model.component.inputoutput.OutputModule;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.Point2DProperty;
import fr.univmrs.lif.view.Canvas;
import fr.univmrs.lif.view.ContextMenuViewFactory;
import fr.univmrs.lif.view.DrawingBoardTab;
import fr.univmrs.lif.view.popup.PopupType;
import fr.univmrs.lif.view.popup.TextBox;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import jfxtras.labs.util.NodeUtil;


public class ComponentView extends Pane{

	class Delta { double x, y; }

	// Model
	final private Component model;

	private Pane componentDraw;
	private Label componentName;

	private ContextMenu contextMenu;

	protected boolean showName = false;

	protected boolean selected = false;

	private List<PlugView> plugInView = new ArrayList<>();
	private List<PlugView> plugOutView = new ArrayList<>();
	private ChangeListener<Number> inputListener ;
	private ChangeListener<Number> outputListener ;

	ComponentView self;

	public ComponentView(Component component){

		this.model = component;
		componentName = new Label();
		componentName.setStyle("-fx-background-color:white;");
		componentDraw = new Pane();
		setPickOnBounds(false);
		requestLayout();
	}

	public void initialize() {
		// DEGUG
		//setStyle("-fx-border-color:red;");
		//componentDraw.setStyle("-fx-border-color:blue;");
		self = this;
		buildInputOutputListener();

		buildView();
		buildMouseHandlers();
		contextMenu = ContextMenuViewFactory.buildContextMenu(this);

		buildPlugs();

		Tooltip toolTip =  new Tooltip();
		toolTip.textProperty().bind(model.getNameProperty());
		Tooltip.install(
				componentDraw,
				toolTip
				);


	}

	private void buildPlugs() {
		//placement des plugs in
		// IN
		for(int i = 0; i < model.getInputs().size(); i++){

			PlugView plugView = PlugViewFactory.buildPlugInView(this,model.getInputs().get(i));
			NodeUtil.addToParent(getParent(), plugView);
			plugInView.add(plugView);
			plugView.toBack();
		}

		// OutPut
		for(int i = 0; i < model.getOutputs().size(); i++){

			PlugView plugView = PlugViewFactory.buildPlugOutView(this,model.getOutputs().get(i));
			NodeUtil.addToParent(getParent(), plugView);
			plugOutView.add(plugView);
			plugView.toBack();

		}
		addInputOutputListener();
	}

	/**
	 * Check if plug was added to model
	 */
	public void buildInputOutputListener(){
		inputListener = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(oldValue.intValue() < newValue.intValue()){
					// input was added in model
					PlugView plugView = PlugViewFactory.buildPlugInView(self,model.getInputs().get(oldValue.intValue()));
					plugInView.add(plugView);
					NodeUtil.addToParent(getParent(), plugView);
					plugView.toBack();

				}else if(oldValue.intValue() > newValue.intValue()){

					PlugView plugToRemove = null;
					//check existence in model
					for(PlugView plugView : plugInView){
						boolean exist = false;
		
						for(Plug plug : model.getInputs()){
							if(plug == plugView.getModel())
								exist = true;
						}
						if(!exist){
							plugToRemove = plugView;
						}
					}

					if(plugToRemove != null){
						plugInView.remove(plugToRemove);
						plugToRemove.delete();
					}

				}
			}
		};

		outputListener = new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(oldValue.intValue() < newValue.intValue()){
			
					// input was added in model
					PlugView plugView = PlugViewFactory.buildPlugOutView(self,model.getOutputs().get(oldValue.intValue()));
					plugOutView.add(plugView);
					NodeUtil.addToParent(getParent(), plugView);
					plugView.toBack();

				}else if(oldValue.intValue() > newValue.intValue()){
				
					PlugView plugToRemove = null;
					//check existence in model
					for(PlugView plugView : plugOutView){
						boolean exist = false;
			
						for(Plug plug : model.getOutputs()){
							if(plug == plugView.getModel())
								exist = true;
						}
						if(!exist){
							plugToRemove = plugView;
						}

					}

					if(plugToRemove != null){
						plugOutView.remove(plugToRemove);
						plugToRemove.delete();
					}

				}
			}


		};
	}

	public void addInputOutputListener(){
		if(model.getType() == ComponentType.MODULE){
		model.getInputNumberProperty().addListener(inputListener);
		model.getOutputNumberProperty().addListener(outputListener);
		}
	}

	public void removeInputOutputListener(){
		if(model.getType() == ComponentType.MODULE){
		model.getInputNumberProperty().removeListener(inputListener);
		model.getOutputNumberProperty().removeListener(outputListener);
		}
	}

	public void addComponentInput(){
		Plug newInput = new Plug(getModel(), PlugType.IN);
		model.addInput(newInput);

		PlugView plugView = PlugViewFactory.buildPlugInView(this,newInput);
		plugInView.add(plugView);
		NodeUtil.addToParent(getParent(), plugView);
		plugView.toBack();

	}
	public void addComponentOutput(){
		Plug newOutput = new Plug(getModel(), PlugType.OUT);
		model.addOutput(newOutput);

		PlugView plugView = PlugViewFactory.buildPlugOutView(this,newOutput);
		plugOutView.add(plugView);
		NodeUtil.addToParent(getParent(), plugView);
		plugView.toBack();

	}


	private void buildView(){

		FXMLLoader fxmlLoader = null;
		switch (model.getType()) {
		case JOINT:
			fxmlLoader = new FXMLLoader(getClass().getResource("Joint.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}

			break;
		case AND:
			fxmlLoader = new FXMLLoader(getClass().getResource("AndGate.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}

			break;
		case NAND:
			fxmlLoader = new FXMLLoader(getClass().getResource("NotAndGate.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}

			break;
		case OR:
			fxmlLoader = new FXMLLoader(getClass().getResource("OrGate.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
			break;
		case NOR:
			fxmlLoader = new FXMLLoader(getClass().getResource("NotOrGate.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
			break;
		case XOR:
			fxmlLoader = new FXMLLoader(getClass().getResource("XOrGate.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
			break;
		case XNOR:
			fxmlLoader = new FXMLLoader(getClass().getResource("XNOrGate.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
			break;
		case NOT:
			fxmlLoader = new FXMLLoader(getClass().getResource("NotGate.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
			break;
		case CLOCK:
			fxmlLoader = new FXMLLoader(getClass().getResource("Clock.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
			break;
		case VDD:
			fxmlLoader = new FXMLLoader(getClass().getResource("Vdd.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
			break;
		case GND:
			fxmlLoader = new FXMLLoader(getClass().getResource("Ground.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
			break;

		case SWITCH:

			SwitchView switchView = new SwitchView(model);
			componentDraw.getChildren().add(switchView);

			break;
		case LED:

			LedView ledView = new LedView(model);
			componentDraw.getChildren().add(ledView);

			break;
		case DISPLAY:

			DisplayView displayView = new DisplayView(model);
			componentDraw.getChildren().add(displayView);

			break;
		case MUX:

			fxmlLoader = new FXMLLoader(getClass().getResource("Multiplexer.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}

			break;
		case DECODER:

			fxmlLoader = new FXMLLoader(getClass().getResource("Decoder.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}

			break;
		case ADDER:

			fxmlLoader = new FXMLLoader(getClass().getResource("Adder.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}

			break;
		case INPUTMODULE:
			// Provisoire
			fxmlLoader = new FXMLLoader(getClass().getResource("InputModule.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}

			break;
		case OUTPUTMODULE:
			// Provisoire
			fxmlLoader = new FXMLLoader(getClass().getResource("OutputModule.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}

			break;
		case MODULE:
			// Provisoire
			fxmlLoader = new FXMLLoader(getClass().getResource("Module.fxml"));

			try { 
				fxmlLoader.load();
				componentDraw.getChildren().add(fxmlLoader.getRoot());
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
			break;
		default:
			break;
		}

		getChildren().add(componentDraw);
		layout();

		// set label name
		componentName.setVisible(showName);
		componentName.textProperty().bind(model.getNameProperty());
		//Placement du label du composant. En haut a Droite.
		componentName.layoutXProperty().bind(componentDraw.widthProperty().add(componentDraw.layoutXProperty()));
		componentName.layoutYProperty().bind(Settings.WIRE_LENGTH.subtract(componentName.heightProperty()));

		getChildren().add(componentName);
		layout();
	}

	private void buildMouseHandlers(){

		final Delta dragDelta = new Delta();
		componentDraw.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				// enregistre la position avant le deplacement
				if(mouseEvent.getButton() == MouseButton.PRIMARY){
					// record a delta distance for the drag and drop operation
					Point2D coordsScene = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
					Point2D localCoords = getParent().sceneToLocal(coordsScene);
					dragDelta.x = (int)(getLayoutX() - localCoords.getX());
					dragDelta.y = (int)(getLayoutY() - localCoords.getY());

					setCursor(Cursor.MOVE);
					((Canvas)getParent()).setSelectionItem(self);
				}
				mouseEvent.consume();
			}
		});

		componentDraw.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				showName(true);
				setCursor(Cursor.HAND);
				DrawingBoardTab.setPannable(false);
				mouseEvent.consume();
			}
		});

		componentDraw.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				showName(false);
				setCursor(Cursor.DEFAULT);
				DrawingBoardTab.setPannable(true);
				mouseEvent.consume();
			}
		});

		componentDraw.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				if(mouseEvent.getButton() == MouseButton.PRIMARY){
					Point2D coordsScene = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
					Point2D localCoords = getParent().sceneToLocal(coordsScene);

					// round the position
					int x = (int) (localCoords.getX() + dragDelta.x);
					int y = (int) (localCoords.getY() + dragDelta.y);

					//					x = x - (x%6);
					//					y = y - (y%6);

					setLayoutX(x);
					setLayoutY(y);

					toFront();
				}
			}
		});

		componentDraw.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override public void handle(MouseEvent mouseEvent) {
				// TODO verifier que place libre

			}
		});
	}



	public Pane getComponentDraw() {
		return componentDraw;
	}

	protected void showName(boolean b) {
		if(b)
			componentName.setVisible(true);
		else
			componentName.setVisible(false);
	}

	public void delete() {

		removeInputOutputListener();

//		List<PlugView> plugViews = new ArrayList<>();
//		List<Plug> plugs = getModel().getPlugs();
		// trouver les plugView des plugs
//		for(Node n : getParent().getChildrenUnmodifiable()){
//			if(n instanceof PlugView){
//				for(Plug plug : plugs){
//					if(((PlugView) n).getModel() == plug )
//						plugViews.add((PlugView) n);
//				}
//			}
//		}

		for(PlugView plugView : this.plugInView){
			if(plugView != null)
				plugView.delete();
		}
		for(PlugView plugView : this.plugOutView){
			if(plugView != null)
				plugView.delete();
		}
		
		// if an I/O Module is delete, we have to keep model update
		if(model.getType() == ComponentType.INPUTMODULE){
			MainController.getCurrentBoard().getModule().removeInput(((InputModule)model).getPlugIn());
		}else if(model.getType() == ComponentType.OUTPUTMODULE){
			MainController.getCurrentBoard().getModule().removeInput(((OutputModule)model).getPlugOut());
		}

		MainController.getCurrentBoard().getModule().removeComponent(model);

		((Canvas)getParent()).getChildren().remove(this);


	}

	public void setDropPosition(Point2D position) {

		setLayoutX((int)(position.getX() - componentDraw.getBoundsInParent().getWidth()/2));
		setLayoutY((int)(position.getY() - componentDraw.getBoundsInParent().getHeight()/2));

		// Binding
		model.getPosition().xProperty().bind(layoutXProperty());
		model.getPosition().yProperty().bind(layoutYProperty());


	}

	public void setLoadPosition(Point2D position) {
		setLayoutX((int)(position.getX()));
		setLayoutY((int)(position.getY()));

		// Binding
		model.getPosition().xProperty().bind(layoutXProperty());
		model.getPosition().yProperty().bind(layoutYProperty());

	}

	public void setShowName(boolean b) {
		showName = b;
	}

	public boolean getShowName() {
		return showName;
	}

	public Component getModel(){
		return model;
	}

	public void changeColor(Color color){
		//		Path s = (Path) ((Pane) componentDraw.getChildren().get(0)).getChildren().get(0);
		//		s.setFill(color);
	}



	@Override
	public String toString() {

		return model.toString();
	}

}
