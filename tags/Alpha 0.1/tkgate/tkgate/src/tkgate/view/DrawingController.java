package tkgate.view;




import tkgate.EditorContext;
import tkgate.modele.Component;
import tkgate.modele.Module;
import java.awt.geom.Point2D;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.collections.ListChangeListener;

 
public class DrawingController {
	@FXML
	private Canvas canvas;
	private Cross cross;
	private Module module;
	private Scene scene; // utilisé pour changer le curseur
	private EditorContext context;

	@FXML
    private StackPane drawPane;
	public DrawingController() {		
	}
	
	
	public void setEditorContext(EditorContext context) {
		this.context=context;
	}
	
	 /**
     * Is called by the main application.
     * 
     * @param mainApp
     */
    public void setDrawing(Module module, Scene scene) {
        this.module = module;
        this.scene = scene;
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
		canvas.widthProperty().bind(drawPane.widthProperty());
        canvas.heightProperty().bind(drawPane.heightProperty());
	}

	@FXML
	public void handleMousePressed() {
		canvas.setOnMousePressed(mouseEvent -> context.mousePressed(new Point2D.Double(mouseEvent.getX(), mouseEvent.getY()))); 
	}

	@FXML
	public void handleMouseRelease() {
		canvas.setOnMouseReleased(mouseEvent -> context.mouseReleased(new Point2D.Double(mouseEvent.getX(), mouseEvent.getY()))); 
	}
	
	@FXML
	public void handleMouseDragged() {
		canvas.setOnMouseDragged(mouseEvent -> context.mouseDragged(new Point2D.Double(mouseEvent.getX(), mouseEvent.getY()))); 
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
		scene.setCursor(new ImageCursor(image));
		
		
	}

	public void handCursor() {
		scene.setCursor(Cursor.CLOSED_HAND);
	}
	
	public void defaultCursor() {
		scene.setCursor(Cursor.DEFAULT);
	}
	
	public void drawCross(double x, double y) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		cross = new Cross(x, y ,gc);
		cross.draw();
	}
	
	class Cross {
		Double x;
		Double y;
		GraphicsContext gc;

		Cross(Double x, Double y, GraphicsContext gc) {
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
	

}
