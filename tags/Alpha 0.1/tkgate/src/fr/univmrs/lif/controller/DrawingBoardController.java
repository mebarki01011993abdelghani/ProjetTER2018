package fr.univmrs.lif.controller;

import fr.univmrs.lif.model.EditorContext;
import fr.univmrs.lif.model.EditorControler;
import fr.univmrs.lif.model.PointSelectedState;
import fr.univmrs.lif.tools.ComponentType;
import fr.univmrs.lif.view.DrawingBoard;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class DrawingBoardController{

//	//private Group drawingBoard;
//	private MainController mainController;
	private DrawingBoard drawingBoard;
	private Pane componentSelected;
	private EditorContext edContext;
	
	public void addNewComponent(DrawingBoard currentBoard, DragContainer container) {
		
		Point2D cursorPoint = container.getValue("scene_coords");
		ComponentType type = ComponentType.valueOf(container.getValue("type"));
	
		//currentBoard.getOnMouseReleased();
		EditorContext context = currentBoard.getContext();
		EditorControler control = currentBoard.getEdControl();
		context.setState(new PointSelectedState(control, cursorPoint));
		context.addComponent(type); // graphic
		//context.addComponent(type); // model
		
		//	RectangleGate node = (RectangleGate) new ComponentFactory().buildComponent(type, NameGenerator.generate(type), new Double(cursorPoint.getX(), cursorPoint.getY()),Settings.GATE_DIMENSION, 2);

		//	node.injectMainController(mainController);
		//System.out.println("node : "+node.getName()+" dans "+currentBoard);
		//currentBoard.getChildren().add(node);
		//GraphicsContext gc = canvas.getGraphicsContext2D();
//		gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
	//	node.draw(gc);
//				node.relocateToPoint(
		//				new Point2D(cursorPoint.getX(), cursorPoint.getY() )
		//				);

//		setComponentSelected(node);

	}

	public void initDrawingBoard(DrawingBoard drawingBoard) {

	}

//	public void setEditorContext(EditorContext context) {
//		this.context=context;
//	}
//
//	/**
//	 * Is called by the main application.
//	 * @param module
//	 * @param scene
//	 */
//	public void setDrawing(Module module, Scene scene) {
//		this.module = module;
//		this.scene = scene;
//		module.getComponentList().addListener(new ListChangeListener<Component>() { 
//
//			@Override 
//			public void onChanged(ListChangeListener.Change<? extends Component> change) { 
//				clear();
//				drawComponent();
//			}            
//		});
//	}
//
//
//	@FXML  
//	private void initialize() {		
//		//drawingBoard.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
//		//		canvas.widthProperty().bind(drawingBoard.widthProperty());
//		//        canvas.heightProperty().bind(drawingBoard.heightProperty());
//	}
//
//	@FXML
//	public void handleMousePressed() {
//		canvas.setOnMousePressed(mouseEvent -> context.mousePressed(new Double(mouseEvent.getX(), mouseEvent.getY()))); 
//	}
//
//	@FXML
//	public void handleMouseRelease() {
//		canvas.setOnMouseReleased(mouseEvent -> context.mouseReleased(new Double(mouseEvent.getX(), mouseEvent.getY()))); 
//	}
//
//	@FXML
//	public void handleMouseDragged() {
//		canvas.setOnMouseDragged(mouseEvent -> context.mouseDragged(new Double(mouseEvent.getX(), mouseEvent.getY()))); 
//	}
//
//
//	public void drawComponent() {
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
//		for (Component c : module.getComponentList()) {
//			c.draw(gc);
//		}
//		if (cross != null)
//			cross.draw();
//	}
//
//	public void clear() {
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
//	}
//
//	// Change l'image du pointeur souris en fer à souder
//	public void solderCursor() {		
//		// TODO image mal placée
//		Image image = new Image("/solder.gif"); 
//		scene.setCursor(new ImageCursor(image));
//
//
//	}
//
//	public void handCursor() {
//		scene.setCursor(Cursor.CLOSED_HAND);
//	}
//
//	public void defaultCursor() {
//		scene.setCursor(Cursor.DEFAULT);
//	}
//
//	public void drawCross(double x, double y) {
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		cross = new Cross(x, y ,gc);
//		cross.draw();
//	}
//
//	class Cross {
//		double x;
//		double y;
//		GraphicsContext gc;
//
//		Cross(double x, double y, GraphicsContext gc) {
//			this.x = x;
//			this.y = y;
//			this.gc = gc;
//
//		}
//
//		public void draw() {
//			gc.setStroke(Color.BLACK);
//			gc.setLineWidth(1);
//			gc.strokeLine(x - 3, y - 3, x + 3, y + 3);
//			gc.strokeLine(x + 3, y - 3, x - 3, y + 3);
//		}
//	}
//
//	public void setCross(Cross cross) {
//		this.cross = cross;
//
//	}
//	// Scale of view
//	private double scale = 1;
//
////	public void injectMainController(MainController mainController){
////		this.mainController = mainController;
////	}
//
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		drawingBoard.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
//		//canvas = new Canvas();
//		canvas.widthProperty().bind(drawingBoard.widthProperty());
//		canvas.heightProperty().bind(drawingBoard.heightProperty());
//		System.out.println(canvas.getGraphicsContext2D());
//
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		System.out.println(gc);
//	}
//
//
//	public void addNewComponent(DrawingBoard currentBoard, DragContainer container) {
//		
//		Point2D cursorPoint = container.getValue("scene_coords");
//		ComponentType type = ComponentType.valueOf(container.getValue("type"));
//		RectangleGate node = (RectangleGate) new ComponentFactory().buildComponent(type, NameGenerator.generate(type), new Double(cursorPoint.getX(), cursorPoint.getY()),Settings.GATE_DIMENSION, 2);
//
//		//	node.injectMainController(mainController);
//		System.out.println("node : "+node.getName()+" dans "+currentBoard);
//		currentBoard.getChildren().add(node);
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
//		node.draw(gc);
//		//		node.relocateToPoint(
//		//				new Point2D(cursorPoint.getX(), cursorPoint.getY() )
//		//				);
//
//		setComponentSelected(node);
//
//	}
//
//	public void setComponentSelected(Pane componentPane) {
//		if(componentSelected != null)
//			componentSelected.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
//
//		componentSelected = componentPane;
//		componentSelected.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
//
//	}
//
//	public void setDrawingBoard(DrawingBoard drawingBoard) {
//		
//		this.drawingBoard = drawingBoard;
//		this.canvas = drawingBoard.getCanvas();
//	}
}
