package fr.univmrs.lif.view.component;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.enumeration.ComponentFamily;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Joint;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.Point2DProperty;
import fr.univmrs.lif.view.Canvas;
import fr.univmrs.lif.view.ContextMenuViewFactory;
import fr.univmrs.lif.view.component.ComponentView.Delta;
import fr.univmrs.lif.view.popup.PopupType;
import fr.univmrs.lif.view.popup.TextBox;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import jfxtras.labs.util.NodeUtil;

public class PlugView extends Line{
	class Delta { double x, y; }

	private ContextMenu contextMenu;

	Line wire;

	Point2DProperty anchor;
	//	Point2DProperty tail;

	// The Model
	private Plug model;

	WireView wireView;

	// Save initiale point 
	//	private Point2DProperty anchorOrigine;


	/**
	 * Build a Plug with type attached to a component
	 * @param component
	 * @param type
	 */
	public PlugView(Plug plug,Point2DProperty head,Point2DProperty tail) {
		this.model = plug;
		//		this.anchorOrigine = head;
		this.anchor = head;
		//		this.tail = tail;
		plug.setHead(head);
		plug.setTail(tail);
		setStrokeWidth(3);
		buildMouseHandlers();
		//binding
		startXProperty().bind(head.xProperty());
		startYProperty().bind(head.yProperty());
		endXProperty().bind(tail.xProperty());
		endYProperty().bind(tail.yProperty());

		//		buildContextMenu();

		contextMenu = ContextMenuViewFactory.buildContextMenu(this);

		Tooltip toolTip =  new Tooltip();

		toolTip.textProperty().bind(plug.getNameProperty());
		Tooltip.install(
				this,
				toolTip
				);
	}

	public void makeRotation(){
		setRotate(getRotate() + 90);
	}

	private void buildMouseHandlers(){
		final Delta dragDelta = new Delta();
		final PlugView self = this;
		setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {

				Point2D coordsScene = new Point2D(getStartX(), getStartY());
				Point2D localCoords = getParent().localToParent(coordsScene);

				wire = new Line();
				wire.setStrokeWidth(3);
				wire.setStartX((int)localCoords.getX());
				wire.setStartY((int)localCoords.getY());
				wire.setEndX((int)localCoords.getX());
				wire.setEndY((int)localCoords.getY());

				NodeUtil.addToParent(getParent(), wire);

				mouseEvent.consume();
			}
		});

		setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {

				Image image = new Image("/solder.gif"); 
				ImageCursor ic = new ImageCursor(image,0,26);
				setCursor(ic);
				mouseEvent.consume();
			}
		});

		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {


				mouseEvent.consume();
			}
		});

		setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override public void handle(MouseEvent mouseEvent) {
				if(mouseEvent.getButton() == MouseButton.PRIMARY){
					PlugView plugView = (PlugView) NodeUtil.getNode(getParent(), mouseEvent.getSceneX(), mouseEvent.getSceneY(), PlugView.class);
					WireView wireView = (WireView) NodeUtil.getNode(getParent(), mouseEvent.getSceneX(), mouseEvent.getSceneY(), WireView.class);

					setCursor(Cursor.CROSSHAIR);
					if(plugView != null && plugView.isBindable(self)){
						Image image = new Image("/solder.gif"); 
						ImageCursor ic = new ImageCursor(image,0,26);
						setCursor(ic);
					}
					if(wireView != null && model.getType() == PlugType.IN){
						Image image = new Image("/solder.gif"); 
						ImageCursor ic = new ImageCursor(image,0,26);
						setCursor(ic);
					}
					Point2D coordsScene = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
					Point2D localCoords = getParent().sceneToLocal(coordsScene);

					wire.setEndX((int)localCoords.getX());
					wire.setEndY((int)localCoords.getY());

				}
			}
		});

		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				// drop on other plug or on wire
				PlugView plugView = (PlugView) NodeUtil.getNode(getParent(), mouseEvent.getSceneX(), mouseEvent.getSceneY(), PlugView.class);
				WireView wireView = (WireView) NodeUtil.getNode(getParent(), mouseEvent.getSceneX(), mouseEvent.getSceneY(), WireView.class);

				NodeUtil.removeFromParent(wire);


				if(plugView != null && plugView.isBindable(self)){
					buildNewWire(plugView);
				}else if(wireView != null && model.getType() == PlugType.IN){
					buildJoint(wireView,new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
				}
				setCursor(Cursor.DEFAULT);
				mouseEvent.consume();
			}


		});
	}

	protected boolean isBindable(PlugView plug) {
		if(model.getType() == plug.getModel().getType() || 
				(model.getWire() != null || model.getWire() != null ))
			return false;
		else
			return true;
	}

	private void buildNewWire(PlugView plugView) {
		Wire wire = new Wire();
		model.setWire(wire);
		plugView.getModel().setWire(wire);

		Component opositeComponent = plugView.getModel().getComponent();
		if(model.getType() == PlugType.IN){
			wire.setHead(opositeComponent);
			wire.setTail(model.getComponent());
			wireView = new WireView(getParent() ,wire, plugView ,this );


		}
		else if (model.getType() == PlugType.OUT) {
			wire.setHead(model.getComponent());
			wire.setTail(opositeComponent);
			wireView = new WireView(getParent() ,wire, this , plugView);
		}

		plugView.bindWire(wireView);

		MainController.getCurrentBoard().getModule().addWire(wire);

	}

	private void buildJoint(WireView wireView,Point2D position) {
		// creation de du composant Joint
		Joint joint = new Joint();

		position = ((Canvas) getParent()).sceneToLocal(position);

		((Canvas) getParent()).getModule().getComponents().add(joint);
		ComponentView jointView = new ComponentView(joint);
		((Canvas) getParent()).getChildren().add(jointView);
		jointView.initialize();
		jointView.setDropPosition(position);

		// recuperation des PlugView
		PlugView plugViewInput   = wireView.getHeadPlug();
		PlugView plugViewOutput1 = wireView.getTailPlug();
		PlugView plugViewOutput2 = this;

		PlugView jointInput   = null;
		PlugView jointOutput1 = null;
		PlugView jointOutput2 = null;

		// input
		for(Node n : ((Canvas)getParent()).getChildrenUnmodifiable()){
			if(n instanceof PlugView){
				if(((PlugView)n).getModel() == joint.getInputs().get(0)){
					jointInput = (PlugView)n;
				}
				else if(((PlugView)n).getModel() == joint.getOutputs().get(0)){
					jointOutput1 = (PlugView)n;
				}
				else if(((PlugView)n).getModel() == joint.getOutputs().get(1)){
					jointOutput2 = (PlugView)n;
				}
			}
		}

		wireView.delete();

		jointInput.buildNewWire(plugViewInput);
		jointOutput1.buildNewWire(plugViewOutput1);
		jointOutput2.buildNewWire(plugViewOutput2);

	}


	public Line getWire() {
		return wire;
	}


	public void setWire(Line wire) {
		this.wire = wire;
	}


	public Point2DProperty anchorProperty() {
		return anchor;
	}


	public Plug getModel() {
		return model;
	}


	public void setPlug(Plug plug) {
		this.model = plug;
	}

	public void bindWire(WireView wire){
		//		System.out.println(this + " bind with "+ wire);
		wireView = wire;
	}

	public void unBindWire(){
		model.setWire(null);
		wireView = null;
	}

	public void delete(){

		if(wireView != null)
			wireView.delete();


		Component component = model.getComponent();

		if(component.getType() != ComponentType.MODULE){
			if(model.getType() == PlugType.IN){
				component.removeInput(model);

			}
			else if(model.getType() == PlugType.OUT){
				component.removeOutput(model);
			}
		
	
		anchor.unbind();
		model.getHead().unbind();
		model.getTail().unbind();
		}
		((Canvas)getParent()).getChildren().remove(this);

	}

	@Override
	public String toString() {

		return model.toString();
	}

}
