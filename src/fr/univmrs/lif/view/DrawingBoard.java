package fr.univmrs.lif.view;

import java.io.IOException;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.EditorContext;
import fr.univmrs.lif.model.EditorController;
import fr.univmrs.lif.model.SelectState;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.ComponentFactory;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.view.component.ComponentView;
import fr.univmrs.lif.view.component.WireView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import jfxtras.labs.util.event.MouseControlUtil;


public class DrawingBoard extends ScrollPane {
	
	private StringProperty name = new SimpleStringProperty();
	
	private Pane canvas;

	private Scale scaleTransform;

	private double oldZoom = 1;
	
	private Module module;
	private EditorContext edContext;
	private EditorController edControl;
	
	public DrawingBoard(Module module){
		canvas = new Pane();
		setPannable(true);
        setContent(canvas);
        setStyle("-fx-background:white;");
		this.module = module;
		name.bind(module.getNameProperty());
		edControl = new EditorController(module);
        edContext = new EditorContext(this, edControl);
        edContext.setState(new SelectState(edControl));
        
	}
	
	public EditorController getEdControl() {
		return edControl;
	}

	public Module getModule() {
		return module;
	}

//	public DrawingBoard(Module module){
//		
//		
//		FXMLLoader fxmlLoader = new FXMLLoader(
//				getClass().getResource("DrawingBoard.fxml")
//				);
//		
//		fxmlLoader.setRoot(this); 
//		fxmlLoader.setController(this);
//			
//		try { 
//			fxmlLoader.load();
//			this.module = module;
//			name.bind(module.getNameProperty());
//            
//            edControl = new EditorController(module);
//            edContext = new EditorContext(this, edControl);
//            edContext.setState(new SelectState(edControl));
//            
//          
//		} catch (IOException exception) {
//		    throw new RuntimeException(exception);
//		}
//	
//	}
	
//	public DrawingBoard(ModuleView module) {
//		
//		FXMLLoader fxmlLoader = new FXMLLoader(
//				getClass().getResource("DrawingBoard.fxml")
//				);
//		
//		fxmlLoader.setRoot(this); 
//		fxmlLoader.setController(this);
//			
//		try { 
//			fxmlLoader.load();
//            this.module = module;
//            setDrawing(module);
//            name.bindBidirectional(module.nameProperty());
//            edControl = new EditorControler(module);
//            edContext = new EditorContext(this, edControl);
//            edContext.setState(new SelectState(edControl));
//            
//          
//		} catch (IOException exception) {
//		    throw new RuntimeException(exception);
//		}
//	
//	}
	
	
	
	
//	public DrawingBoard(String nameModule) {
//		FXMLLoader fxmlLoader = new FXMLLoader(
//				getClass().getResource("DrawingBoard.fxml")
//				);
//		
//		fxmlLoader.setRoot(this); 
//		fxmlLoader.setController(this);
//			
//		try { 
//			fxmlLoader.load();
//			module = new ModuleView(nameModule);
//			name.bindBidirectional(module.nameProperty());
//            setDrawing(module);
//            
//            edControl = new EditorController(module);
//            edContext = new EditorContext(this, edControl);
//            edContext.setState(new SelectState(edControl));
//            
//          
//		} catch (IOException exception) {
//		    throw new RuntimeException(exception);
//		}
//	}

	public void setEditorContext(EditorContext context) {
		this.edContext=context;
	}

	/**
	 * Is called by the main application.
	 * @param module
	 * @param scene
	 */
//	public void setDrawing(Module module) {
//		this.module = module;
//		module.getComponentList().addListener(new ListChangeListener<ComponentView>() { 
//
//			@Override 
//			public void onChanged(ListChangeListener.Change<? extends ComponentView> change) { 
//				clear();
//				drawComponent();
//			}            
//		});
//	}


//	@FXML  
//	private void initialize() {		
//		drawingBoard.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
//		//canvas = new Canvas();
//		canvas.widthProperty().bind(drawingBoard.widthProperty());
//		canvas.heightProperty().bind(drawingBoard.heightProperty());
//		
//		
//	}

	@FXML
	public void handleMousePressed() {
		canvas.setOnMousePressed(mouseEvent -> edContext.mousePressed(new Point2D(mouseEvent.getX(), mouseEvent.getY()))); 
	}

	@FXML
	public void handleMouseRelease() {
		canvas.setOnMouseReleased(mouseEvent -> edContext.mouseReleased(new Point2D(mouseEvent.getX(), mouseEvent.getY()))); 
	}

	@FXML
	public void handleMouseDragged() {
		canvas.setOnMouseDragged(mouseEvent -> edContext.mouseDragged(new Point2D(mouseEvent.getX(), mouseEvent.getY()))); 
	}

	@FXML
    void handleMouseMoved(MouseEvent event) {
		canvas.setOnMouseMoved(mouseEvent -> edContext.mouseMoved(new Point2D(mouseEvent.getX(), mouseEvent.getY()))); 
		
    }

//	public void drawComponent() {
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
//		for (ComponentView c : module.getComponentList()) {
//			c.draw(gc);
//		}
//	}

//	public void clear() {
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
//	}

	// Change l'image du pointeur souris en fer à souder
	public void solderCursor() {		
		// TODO image mal placée
		Image image = new Image("/solder.gif"); 
		ImageCursor ic = new ImageCursor(image,0,26);
		canvas.setCursor(ic);
	}

	public void handCursor() {
		canvas.setCursor(Cursor.CLOSED_HAND);
	}

	public void defaultCursor() {
		canvas.setCursor(Cursor.DEFAULT);
	}

	


//	public void injectMainController(MainController mainController){
//		this.mainController = mainController;
//	}


//	public void setComponentSelected(Pane componentPane) {
//		if(componentSelected != null)
//			componentSelected.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
//
//		componentSelected = componentPane;
//		componentSelected.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
//
//	}

//	public void setDrawingBoard(DrawingBoard drawingBoard) {
//		
//	
//		this.canvas = drawingBoard.getCanvas();
//	}

	public EditorContext getContext() {
	
		return edContext;
	}

	public void zoom(double zoomFactor) {
		  canvas.getTransforms().add(new Scale(1/oldZoom, 1/oldZoom, 0, 0));
		  scaleTransform = new Scale(zoomFactor, zoomFactor, 0, 0);
		  canvas.getTransforms().add(scaleTransform);
		  oldZoom = zoomFactor;
		  	}

//	public ModuleView getModule() {
//		
//		return module;
//	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void addComponent(ComponentType type, Point2D position) {

		position = canvas.sceneToLocal(position);

		Component newComponent = ComponentFactory.buildComponent(type);
		
		// mise a jour du modele
		module.getComponents().add(newComponent);
		
		// creation de la vue
//		ComponentView newComponentView = new ComponentView(newComponent);
//		canvas.getChildren().add(newComponentView);
//		newComponentView.initialize(position);
		System.out.println(newComponent);
	}
	
	public void loadComponent(Component component) {

		Point2D position = new Point2D(component.getPosition().getX(),component.getPosition().getY());
		
		// creation de la vue
//		ComponentView newComponentView = new ComponentView(component);
//		canvas.getChildren().add(newComponentView);
//		newComponentView.initialize(position);

	}
	
	public void loadWire(Wire wire) {

		WireView wireView = new WireView(canvas, wire);
		
		// creation de la vue
		canvas.getChildren().add(wireView);

	}

}
