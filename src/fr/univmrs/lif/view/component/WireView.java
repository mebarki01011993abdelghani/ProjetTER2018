package fr.univmrs.lif.view.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.controller.bottomBar.ChronogramController;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.main.MainApp;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.simulation.Sensor;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.Point2DProperty;
import fr.univmrs.lif.view.Canvas;
import fr.univmrs.lif.view.ContextMenuViewFactory;
import fr.univmrs.lif.view.component.PlugView.Delta;
import fr.univmrs.lif.view.popup.PopupType;
import fr.univmrs.lif.view.popup.TextBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import jfxtras.labs.util.NodeUtil;

public class WireView extends Group{

	private ContextMenu contextMenu;
	
	// Model
	Wire model;

	private PlugView headPlug;
	private PlugView tailPlug;
	
	Point2DProperty headAnchor = new Point2DProperty();
	Point2DProperty tailAnchor = new Point2DProperty();
	ArrayList<Point2DProperty> points;

	Point2DProperty A = new Point2DProperty();
	Point2DProperty B = new Point2DProperty();

	private Sensor sensor;
	private SensorView sensorView;
	private BooleanProperty haveSensor = new SimpleBooleanProperty();

	public WireView(Parent canvas, Wire model, PlugView head, PlugView tail) {
	
		setPickOnBounds(false);
		points = new ArrayList<>();
		this.headPlug = head;
		this.tailPlug = tail;
		this.model = model;
		model.setPoints(points);
		NodeUtil.addToParent(canvas, this);
		toBack();

		headAnchor.bind(head.anchorProperty());
		tailAnchor.bind(tail.anchorProperty());
		points.add(headAnchor);
		points.add(A);
		points.add(B);
		points.add(tailAnchor);
		
		buildPath();
		buildMouseHandlers();
		contextMenu = ContextMenuViewFactory.buildContextMenu(this);

		
		Tooltip toolTip =  new Tooltip();

		toolTip.textProperty().bind(model.getNameProperty());
		Tooltip.install(
				this,
				toolTip
				);
	}
	
	public WireView(Parent canvas, Wire model) {
		setPickOnBounds(false);
	
		this.model = model;
		this.points = model.getPoints();
		
		loadPath(canvas,model);
		buildMouseHandlers();
		contextMenu = ContextMenuViewFactory.buildContextMenu(this);
		Tooltip toolTip =  new Tooltip();

		toolTip.textProperty().bind(model.getNameProperty());
		Tooltip.install(
				this,
				toolTip
				);
	}



	private void loadPath(Parent canvas, Wire model) {

		for(Node n : canvas.getChildrenUnmodifiable()){
			if(n instanceof PlugView){
				Plug plug = ((PlugView) n).getModel();
				
				
				if(plug.getWire() != null && plug.getWire().getName().equals(model.getName())){
					PlugView plugView = (PlugView) n;
					if(plug.getType()==PlugType.OUT){				
						headAnchor.bind(plug.getHead());
						headPlug = plugView;
						plugView.bindWire(this);
						
					}
					else{
						tailAnchor.bind(plug.getHead());
						tailPlug  = plugView;
						plugView.bindWire(this);
					}
				}
			}
		}
		
		A = points.get(2);
		B = points.get(1);
	
		A.xProperty().bind(headAnchor.xProperty());
		B.xProperty().bind(tailAnchor.xProperty());
		// TODO smart Path
				Line one = new Line();
				one.setStrokeWidth(3);
				one.setStroke(Color.GREEN);
				one.startXProperty().bind(headAnchor.xProperty());
				one.startYProperty().bind(headAnchor.yProperty());
				one.endXProperty().bind(A.xProperty());
				one.endYProperty().bind(A.yProperty());
				Line two = new Line();
				two.setStrokeWidth(3);
				two.setStroke(Color.GREEN);
				two.startXProperty().bind(A.xProperty());
				two.startYProperty().bind(A.yProperty());
				two.endXProperty().bind(B.xProperty());
				two.endYProperty().bind(B.yProperty());
				Line three = new Line();
				three.setStrokeWidth(3);
				three.setStroke(Color.GREEN);
				three.startXProperty().bind(B.xProperty());
				three.startYProperty().bind(B.yProperty());
				three.endXProperty().bind(tailAnchor.xProperty());
				three.endYProperty().bind(tailAnchor.yProperty());


				getChildren().addAll(one,two,three);
		
	}

	private void buildPath(){
		//HEAD
		if(model.getHead() != model.getTail()){
			A.xProperty().bind(headAnchor.xProperty());
			if(tailAnchor.getY() > headAnchor.getY())
				A.yProperty().bind(tailAnchor.yProperty().subtract(tailAnchor.yProperty().subtract(headAnchor.yProperty()).divide(2)));
			else 
				A.yProperty().bind(headAnchor.yProperty().subtract(headAnchor.yProperty().subtract(tailAnchor.yProperty()).divide(2)));


			B.xProperty().bind(tailAnchor.xProperty());
			if(tailAnchor.getY() > headAnchor.getY())
				B.yProperty().bind(tailAnchor.yProperty().subtract(tailAnchor.yProperty().subtract(headAnchor.yProperty()).divide(2)));
			else 
				B.yProperty().bind(headAnchor.yProperty().subtract(headAnchor.yProperty().subtract(tailAnchor.yProperty()).divide(2)));
		}else{
			// find componentView
			Point2D coordsScene = null;
			if(tailAnchor.getX() > headAnchor.getX())
				coordsScene = new Point2D(tailAnchor.getX()-(tailAnchor.getX()-headAnchor.getX())/2, tailAnchor.getY());
			else
				coordsScene = new Point2D(headAnchor.getX()-(headAnchor.getX()-tailAnchor.getX())/2, headAnchor.getY());

			ComponentView componentView = null;
			for(Node n : getParent().getChildrenUnmodifiable()){
				if(n instanceof ComponentView){
					if(((ComponentView) n).getModel() == model.getHead()){
						componentView = (ComponentView) n;
					}
				}
			}
			
			A.xProperty().bind(headAnchor.xProperty());
			B.xProperty().bind(tailAnchor.xProperty());
			if((tailAnchor.getY() <= headAnchor.getY() && tailAnchor.getX() < headAnchor.getX()) ||
					(tailAnchor.getY() >= headAnchor.getY() && tailAnchor.getX() > headAnchor.getX())){
				A.yProperty().bind(componentView.layoutYProperty().subtract(10));
				B.yProperty().bind(componentView.layoutYProperty().subtract(10));
			}else{
				A.yProperty().bind(componentView.layoutYProperty().add(componentView.heightProperty().add(10)));
				B.yProperty().bind(componentView.layoutYProperty().add(componentView.heightProperty().add(10)));
			}

		}
		
		// TODO smart Path
		Line one = new Line();
		one.setStrokeWidth(3);
		one.setStroke(Color.GREEN);
		one.startXProperty().bind(headAnchor.xProperty());
		one.startYProperty().bind(headAnchor.yProperty());
		one.endXProperty().bind(A.xProperty());
		one.endYProperty().bind(A.yProperty());
		Line two = new Line();
		two.setStrokeWidth(3);
		two.setStroke(Color.GREEN);
		two.startXProperty().bind(A.xProperty());
		two.startYProperty().bind(A.yProperty());
		two.endXProperty().bind(B.xProperty());
		two.endYProperty().bind(B.yProperty());
		Line three = new Line();
		three.setStrokeWidth(3);
		three.setStroke(Color.GREEN);
		three.startXProperty().bind(B.xProperty());
		three.startYProperty().bind(B.yProperty());
		three.endXProperty().bind(tailAnchor.xProperty());
		three.endYProperty().bind(tailAnchor.yProperty());


		getChildren().addAll(one,two,three);

	}

	private void buildMouseHandlers(){
		setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				setCursor(Cursor.CLOSED_HAND);

				mouseEvent.consume();
			}
		});

		setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				setCursor(Cursor.OPEN_HAND);
			}
		});

		setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {

				setCursor(Cursor.DEFAULT);
				mouseEvent.consume();
			}
		});

		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				if(mouseEvent.getButton() == MouseButton.PRIMARY){
				Point2D coordsScene = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
				Point2D localCoords = getParent().sceneToLocal(coordsScene);

				A.yProperty().unbind();
				A.setY(localCoords.getY());
				B.yProperty().unbind();
				B.setY(localCoords.getY());
				}
			}
		});

		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				// TODO drop on other plug

				setCursor(Cursor.DEFAULT);
				mouseEvent.consume();
			}


		});
	}
	
	public Wire getModel(){
		return model;
	}
	
	public void delete(){
		
		if(haveSensor.get())
			clearSensor();
		
		headAnchor.unbind();
		tailAnchor.unbind();
		A.unbind();
		B.unbind();
		model.setHead(null);
		model.setTail(null);
		
		headPlug.unBindWire();
		tailPlug.unBindWire();
		
		((Canvas)getParent()).getChildren().remove(this);
		MainController.getCurrentBoard().getModule().removeWire(model);
		model = null;
	}
	
	public PlugView getHeadPlug() {
		return headPlug;
	}

	public void setHeadPlug(PlugView headPlug) {
		this.headPlug = headPlug;
	}

	public PlugView getTailPlug() {
		return tailPlug;
	}

	public void setTailPlug(PlugView tailPlug) {
		this.tailPlug = tailPlug;
	}

	public void addSensor() {
		
		sensor = new Sensor(model);
		
		ChronogramController.addSensor(sensor);
		sensorView = new SensorView();
		sensorView.layoutXProperty().bind(A.xProperty().add(B.xProperty()).divide(2));
		sensorView.layoutYProperty().bind(A.yProperty().subtract(sensorView.heightProperty()));
		getChildren().add(sensorView);
		haveSensor.set(true);
		
	}

	public void clearSensor() {
		
		
		ChronogramController.removeSensor(sensor);
		sensorView.layoutXProperty().unbind();
		sensorView.layoutYProperty().unbind();
		getChildren().remove(sensorView);
		sensor = null;
		haveSensor.set(false);
	}

	public final BooleanProperty haveSensorProperty() {
		return this.haveSensor;
	}
	

	public final boolean isHaveSensor() {
		return this.haveSensorProperty().get();
	}
	

	public final void setHaveSensor(final boolean haveSensor) {
		this.haveSensorProperty().set(haveSensor);
	}
	
	@Override
	public String toString() {
		
		return model.toString();
	}

}
