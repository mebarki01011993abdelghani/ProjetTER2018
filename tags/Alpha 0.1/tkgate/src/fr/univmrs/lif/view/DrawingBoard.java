package fr.univmrs.lif.view;

import java.io.IOException;


import fr.univmrs.lif.controller.DragContainer;
import fr.univmrs.lif.main.MainApp;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.EditorContext;
import fr.univmrs.lif.model.EditorControler;
import fr.univmrs.lif.model.SelectState;
import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.model.component.ComponentFactory;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.model.component.RectangleGate;
import fr.univmrs.lif.tools.ComponentType;
import fr.univmrs.lif.tools.NameGenerator;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;


public class DrawingBoard extends StackPane{
	
	@FXML
	private StackPane drawingBoard;

	@FXML
	private Canvas canvas;

	private Scale scaleTransform;

	private double oldZoom = 1;
	
	private Cross cross;
	private Module module;
	private Scene scene; // utilisé pour changer le curseur
	private EditorContext edContext;
	private EditorControler edControl;
	private Point2D cursorPoint;
	
	public EditorControler getEdControl() {
		return edControl;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public DrawingBoard() {
		
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("DrawingBoard.fxml")
				);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
			
		try { 
			fxmlLoader.load();
            module = new Module("main");
            scene = MainApp.getScene();
            setDrawing(module);
            
            edControl = new EditorControler(module);
            edContext = new EditorContext(this, edControl);
            edContext.setState(new SelectState(edControl));
            
          
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}
	
	}
	
	public DrawingBoard(Module module) {
		
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("DrawingBoard.fxml")
				);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
			
		try { 
			fxmlLoader.load();
            this.module = module;
            scene = MainApp.getScene();
            setDrawing(module);
            
            edControl = new EditorControler(module);
            edContext = new EditorContext(this, edControl);
            edContext.setState(new SelectState(edControl));
            
          
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}
	
	}
	
	
	
	public DrawingBoard(String nameModule) {
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("DrawingBoard.fxml")
				);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
			
		try { 
			fxmlLoader.load();
			module = new Module(nameModule);
            scene = MainApp.getScene();
            setDrawing(module);
            
            edControl = new EditorControler(module);
            edContext = new EditorContext(this, edControl);
            edContext.setState(new SelectState(edControl));
            
          
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}
	}

	public void setEditorContext(EditorContext context) {
		this.edContext=context;
	}

	/**
	 * Is called by the main application.
	 * @param module
	 * @param scene
	 */
	public void setDrawing(Module module) {
		this.module = module;
		module.getComponentList().addListener(new ListChangeListener<Component>() { 

			@Override 
			public void onChanged(ListChangeListener.Change<? extends Component> change) { 
				clear();
				drawComponent();
			}            
		});
	}


	@FXML  
	private void initialize() {		
		drawingBoard.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		//canvas = new Canvas();
		canvas.widthProperty().bind(drawingBoard.widthProperty());
		canvas.heightProperty().bind(drawingBoard.heightProperty());
		
		
	}

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

	public void drawComponent() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
		for (Component c : module.getComponentList()) {
			c.draw(gc);
		}
		if (cross != null)
			cross.draw();
	}

	public void clear() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
	}

	// Change l'image du pointeur souris en fer à souder
	public void solderCursor() {		
		// TODO image mal placée
		Image image = new Image("/solder.gif"); 
		ImageCursor ic = new ImageCursor(image,0,26);
		this.setCursor(ic);
	}

	public void handCursor() {
		this.setCursor(Cursor.CLOSED_HAND);
	}

	public void defaultCursor() {
		this.setCursor(Cursor.DEFAULT);
	}

	public void drawCross(double x, double y) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		cross = new Cross(x, y ,gc);
		cross.draw();
		System.out.println("cursor -> "+x+","+y);
	}

	class Cross {
		double x;
		double y;
		GraphicsContext gc;

		Cross(double x, double y, GraphicsContext gc) {
			this.x = x;
			this.y = y;
			this.gc = gc;

		}

		public void draw() {
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1);
			gc.strokeLine(x - 3, y - 3, x + 3, y + 3);
			gc.strokeLine(x + 3, y - 3, x - 3, y + 3);
		}
	}

	public void setCross(Cross cross) {
		this.cross = cross;

	}

//	public void injectMainController(MainController mainController){
//		this.mainController = mainController;
//	}



	public void addNewComponent(DrawingBoard currentBoard, DragContainer container) {
		
		Point2D cursorPoint = container.getValue("scene_coords");
		ComponentType type = ComponentType.valueOf(container.getValue("type"));
		RectangleGate node = (RectangleGate) new ComponentFactory().buildComponent(type, NameGenerator.generate(type),cursorPoint,Settings.GATE_DIMENSION, 2);

		//	node.injectMainController(mainController);
		System.out.println("node : "+node.getName()+" dans "+currentBoard);
		currentBoard.getChildren().add(node);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
		node.draw(gc);
		//		node.relocateToPoint(
		//				new Point2D(cursorPoint.getX(), cursorPoint.getY() )
		//				);

//		setComponentSelected(node);

	}

//	public void setComponentSelected(Pane componentPane) {
//		if(componentSelected != null)
//			componentSelected.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
//
//		componentSelected = componentPane;
//		componentSelected.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
//
//	}

	public void setDrawingBoard(DrawingBoard drawingBoard) {
		
		this.drawingBoard = drawingBoard;
		this.canvas = drawingBoard.getCanvas();
	}

	public EditorContext getContext() {
	
		return edContext;
	}

	public void zoom(double zoomFactor) {
		  canvas.getTransforms().add(new Scale(1/oldZoom, 1/oldZoom, 0, 0));
		  scaleTransform = new Scale(zoomFactor, zoomFactor, 0, 0);
		  canvas.getTransforms().add(scaleTransform);
		  oldZoom = zoomFactor;
		  setDrawing(module);
		
	}

	public Module getModule() {
		
		return module;
	}

}
