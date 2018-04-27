package fr.univmrs.lif.view.component;


import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.event.MouseInputListener;

import fr.univmrs.lif.controller.DragContainer;
import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.controller.Point2dSerial;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Connection;
import fr.univmrs.lif.tools.ComponentType;
import fr.univmrs.lif.tools.NameGenerator;
import fr.univmrs.lif.view.Settings;
import fr.univmrs.lif.view.componentIcon.GateAND;
import fr.univmrs.lif.view.wire.Wire;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


public class ComponentView extends Pane{

	private MainController mainController;
	private VBox vbox;
	private Label nameLabel;
    
	protected boolean showName = false;
	//protected Rectangle2D.Double rect;
	protected StringProperty name = new SimpleStringProperty();
	protected int orientation = EAST_ORIENTATION;
	public static int EAST_ORIENTATION = 0;
	public static int SOUTH_ORIENTATION = 1;
	public static int WEST_ORIENTATION = 2;
	public static int NORTH_ORIENTATION = 3;
	protected ArrayList<Connection> inputs;
	protected ArrayList<Connection> outputs;
	protected ComponentType componentType;
	protected boolean selected = false;
	
	private Point2D dragOffset = new Point2D (0.0, 0.0);
	private Pane drawingComponent;
	private EventHandler <DragEvent> contextDragOver;
	private EventHandler <DragEvent> contextDragDropped;
	

	// ************** Methods from Component
	// ***************************************//
	
	public ComponentView(ComponentType componentType){
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("ComponentView.fxml")
				);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}

		setType(componentType);
		setName(NameGenerator.generate(getType()));
		
		drawingComponent = new GateAND(2);
		nameLabel = new Label(getName());
		nameLabel.setVisible(false);
		vbox = new VBox(5);
		vbox.setFillWidth(false);
		vbox.setAlignment(Pos.BASELINE_CENTER);
	
		vbox.getChildren().add(nameLabel);
		vbox.getChildren().add(drawingComponent);
		getChildren().add(vbox);
    	buildNodeDragHandlers();
    	buildMouseHandlers();
		
	}
	

    public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }
    
    @FXML
	private void initialize() {

		
    }
    
    private void buildMouseHandlers(){
    	ComponentView componentPane = this;
    	drawingComponent.setOnMousePressed(new EventHandler<Event>() {

		@Override
		public void handle(Event event) {
			//mainController.componentSelected(componentPane);
			nameLabel.setText(getName());
			event.consume();
		}
    	});
    	
    	drawingComponent.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				componentPane.showName(true);
				event.consume();
			}
		});
    	
    	drawingComponent.setOnMouseExited(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				componentPane.showName(false);	
				event.consume();
			}
		});
    }

	protected void showName(boolean b) {
		if(b)
			nameLabel.setVisible(true);
		else
			nameLabel.setVisible(false);
	}


	public ComponentType getType() {
		return componentType;
	}
	
	public void setType(ComponentType type) {
		componentType = type;
	}

//	public void draw(GraphicsContext gc) {
//		for (Iterator<Connection> iter = inputs.iterator(); iter.hasNext();) {
//			Wire wire = iter.next().getWire();
//			wire.draw(gc);
//		}
//		for (Iterator<Connection> iter = outputs.iterator(); iter.hasNext();) {
//			Wire wire = iter.next().getWire();
//			wire.draw(gc);
//		}
//	}

	
	public void relocateToPoint (Point2D p) {

		//relocates the object to a point that has been converted to
		//scene coordinates
//		Point2D localCoords = getParent().sceneToLocal(p);
		
//		relocate ( 
//				(int) (localCoords.getX() - dragOffset.getX()),
//				(int) (localCoords.getY() - dragOffset.getY())
//			);
//		relocate ( 
//				(int) (localCoords.getX() - getWidth()/2),
//				(int) (localCoords.getY() - getHeight()/2)
//				);
		Point2D localCoords = getParent().sceneToLocal(p);

		relocate ( 
				(int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
				(int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
				);
	}
	
	public void buildNodeDragHandlers() {
		
		// Cursor Display for Drag&Drop
//				this.setOnMouseEntered(e -> this.setCursor(Cursor.OPEN_HAND));
//				this.setOnMousePressed(e -> this.setCursor(Cursor.CLOSED_HAND));
//				this.setOnMouseReleased(e -> this.setCursor(Cursor.DEFAULT));
		
		contextDragOver = new EventHandler <DragEvent>() {

			//dragover to handle node dragging in the right pane view
			@Override
			public void handle(DragEvent event) {		
		
				event.acceptTransferModes(TransferMode.ANY);				
				relocateToPoint(new Point2dSerial( event.getSceneX(), event.getSceneY()));

				event.consume();
			}
		};
		
		//dragdrop for node dragging
		contextDragDropped = new EventHandler <DragEvent> () {
	
			@Override
			public void handle(DragEvent event) {
			
				getParent().setOnDragOver(null);
				getParent().setOnDragDropped(null);
				
				event.setDropCompleted(true);
				
				event.consume();
			}
		};
		
		//drag detection for node dragging
		drawingComponent.setOnDragDetected ( new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {

				getParent().setOnDragOver(null);
				getParent().setOnDragDropped(null);

				getParent().setOnDragOver (contextDragOver);
				getParent().setOnDragDropped (contextDragDropped);

                //begin drag ops
                dragOffset = new Point2D(event.getX(), event.getY());
                
                relocateToPoint(
                		new Point2D(event.getSceneX(), event.getSceneY())
                		);
                
                ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();
				
				container.addData ("type", componentType.toString());
				content.put(DragContainer.AddNode, container);
				
                startDragAndDrop (TransferMode.ANY).setContent(content);                
                
                event.consume();					
			}
			
		});
	}
	
	/**
	 * Set the rectangle at position p and set wires.
	 */
	public void setPosition(Point2D p) {
		setLayoutX(p.getX());
		setLayoutY(p.getY());
//		rect.x=(p.getX() - rect.getWidth() / 2);
//		rect.y = (p.getY() - rect.height / 2);
	}

	public Point2D getPosition() {
//		Point2D p = new Point2D(rect.x, rect.y);
//		p.x =(p.getX() + rect.width / 2);
//		p.y = (p.getY() + rect.height / 2);
//		return p;
		return new Point2D(getLayoutX(),getLayoutY());
	}

	public final String getName() {
		return name.get();
	}

	public final void setName(String name) {
		this.name.set(name);
	}
	
	public final StringProperty nameProperty() {return name;}
	 
	public void setShowName(boolean b) {
		showName = b;
	}

	public boolean getShowName() {
		return showName;
	}

	public void setOrientation(int or) {
		if (or == EAST_ORIENTATION || or == SOUTH_ORIENTATION || or == WEST_ORIENTATION || or == NORTH_ORIENTATION)
			orientation = or;
		else {
			System.err.println("Bad orientation identifyer");
		}
	}

	public int getOrientation() {
		return orientation;
	}	
	
//
//	public List<Wire> findWires(Point2D point) {
//		ArrayList<Wire> wireList = new ArrayList<Wire>();
//		for (Connection connection : outputs)
//			if (connection.getWire().intersects(point))
//				wireList.add(connection.getWire());
//		for (Connection connection : inputs)
//			if (connection.getWire().intersects(point))
//				wireList.add(connection.getWire());
//		return wireList;
//	}
//
//	public List<Wire> allWires() {
//		ArrayList<Wire> wireList = new ArrayList<Wire>();
//		for (Connection connection : outputs)
//			wireList.add(connection.getWire());
//		for (Connection connection : inputs)
//			wireList.add(connection.getWire());
//		return wireList;
//	}
//
//
//	public void setInputs(ArrayList<Connection> inputList) {
//		inputs = inputList;
//
//	}
//
//
//	public ArrayList<Connection> getInputs() {
//		return inputs;
//	}
//
//	// 
//	public void setOutputs(ArrayList<Connection> outputs) {
//		this.outputs = outputs;
//	}
//
//
//	public ArrayList<Connection> getOutputs() {
//		return outputs;
//	}
//
//	
//	public int getInputSize() {
//		return inputs.size();
//	}
//
//	
//	public int getOutputSize() {
//		return outputs.size();
//	}
//
//	
//	public boolean intersects(Rectangle2D.Double rect) {
//		return getRectangle().intersects(rect);
//	}
//
//	
//	public boolean contains(Point2D p) {
//		// System.out.println("RectGate " + p );
//		return getRectangle().contains(p);
//	}
//
//	
//	public boolean intersects(Component component) {
//		if (component instanceof ComponentView) {
//			Rectangle2D.Double r = ((ComponentView) component).getRectangle();
//			return (this.getRectangle().intersects(r));
//		} else
//			return false; // TODO
//	}
//
//	
//	public void setSelected(boolean b) {
//		selected = b;
//	}
//
//	
//	public void changeConnection(Connection connectionOld, Connection connectionNew) {
//		for (Connection c : inputs)
//			if (c.equals(connectionOld)) {
//				c.setHead(connectionNew.head);
//				c.setWire(connectionNew.wire);
//				return;
//			}
//		for (Connection c : outputs)
//			if (c.equals(connectionOld)) {
//				c.setHead(connectionNew.head);
//				c.setWire(connectionNew.wire);
//				return;
//			}
//	}
//
//	// *************************************************************************************
//
//	public Rectangle2D.Double getRectangle() {
//		return (Rectangle2D.Double) rect.clone();
//	}
//
//	
//	public void move(Point2D p) {
//		Point2D delta = new Point2D(p.getX() - getPosition().getX(), p.getY() - getPosition().getY());
//		for (Connection co : inputs) {
//			if (co.isHead())
//				co.getWire().translateFromHead(delta);
//			else
//				co.getWire().translateFromTail(delta);
//			co.getWire().align();
//		}
//		for (Connection co : outputs) {
//			if (co.isHead())
//				co.getWire().translateFromHead(delta);
//			else
//				co.getWire().translateFromTail(delta);
//			co.getWire().align();
//		}
//		setPosition(p);
//	}
//
//	
//	public String toString() {
//		String s = this.getType() + " inputs: ";
//		for (Connection c : inputs)
//			s = s + c.toString() + ", ";
//		for (Connection c : outputs)
//			s = s + c.toString() + ", ";
//		return s;
//	}
//
//	// *************************************** Refactor connections
//
//	// Appelé après déplacement d'une porte, pour replacer les fils correctement
//	// (évite par exemple qu'un fil traverse une porte)
//
//	
//	public void refactorConnections() {
//		for (Connection connect : inputs) {
//			refactorConnection(connect);
//			connect.getWire().removeU();
//		}
//		for (Connection connect : outputs) {
//			refactorConnection(connect);
//			connect.getWire().removeU();
//		}
//	}
//
//	public void debugg() throws Exception {
//		for (Connection connect : inputs)
//			connect.getWire().debugg();
//		for (Connection connect : outputs)
//			connect.getWire().debugg();
//	}
//
//	private void refactorConnection(Connection connect) {
//		int index0, dir;
//		if (connect.isHead()) {
//			index0 = 0;
//			dir = 1;
//		} else {
//			index0 = connect.getWire().getSize() - 1;
//			dir = -1;
//		}
//		Point2D p0 = connect.getWire().getPoint(index0);
//		if (rect.x == p0.x)
//			refactorFromEast(connect, index0, dir);
//		else if (rect.x + rect.getWidth() == p0.x)
//			refactorFromWest(connect, index0, dir);
//		else if (rect.y == p0.y)
//			refactorFromNorth(connect, index0, dir);
//		else if (rect.y + rect.getHeight() == p0.y)
//			refactorFromSouth(connect, index0, dir);
//	}
//
//	private void refactorFromEast(Connection connect, int index0, int dir) {
//		Wire wire = connect.getWire();
//		Boolean fromHead = connect.isHead();
//		Point2D p0 = wire.getPoint(index0);
//		Point2D p1 = wire.getPoint(index0 + dir);
//		if (p0.x < p1.x) { // horizontal but wrong orientation
//			wire.addPointFrom(1, new Point2D(p0.x - Settings.WIRE_LENGTH, p0.y), fromHead);
//			wire.addPointFrom(2, new Point2D(p0.x - Settings.WIRE_LENGTH, p0.y - rect.height), fromHead);
//			wire.addPointFrom(3, new Point2D(p1.x, p0.y - rect.height), fromHead);
//			if (wire.getSize() == 5) {
//				Component c = connect.oppositeComponent();
//				if (c != null)
//					c.refactorConnections();
//			}
//		} else if (p0.x == p1.x) { // vertical
//			wire.addPointFrom(1, new Point2D(p0.x - Settings.WIRE_LENGTH, p0.y), fromHead);
//			wire.addPointFrom(2, new Point2D(p0.x - Settings.WIRE_LENGTH, p1.y), fromHead);
//			if (wire.getSize() == 4) {
//				Component c = connect.oppositeComponent();
//				if (c != null)
//					c.refactorConnections();
//			}
//		} else if (p1.x < p0.x && p0.x - Settings.WIRE_LENGTH < p1.x) { // too
//																		// short
//			if (wire.getSize() <= 3 && connect.oppositeComponent() != null)
//				return;
//			p1.setLocation(p0.x - Settings.WIRE_LENGTH, p0.y);
//			if (wire.getSize() > 2) {
//				Point2D p2 = wire.getPoint(index0 + dir + dir);
//				p2.setLocation(p0.x - Settings.WIRE_LENGTH, p2.y);
//			}
//		}
//		try {
//			debugg();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		wire.align();
//	}
//
//	private void refactorFromWest(Connection connect, int index0, int dir) {
//		Wire wire = connect.getWire();
//		Boolean fromHead = connect.isHead();
//		Point2D p0 = wire.getPoint(index0);
//		Point2D p1 = wire.getPoint(index0 + dir);
//		if (p0.x > p1.x) { // horizontal but wrong orientation
//			wire.addPointFrom(1, new Point2D(p0.x + Settings.WIRE_LENGTH, p0.y), fromHead);
//			wire.addPointFrom(2, new Point2D(p0.x + Settings.WIRE_LENGTH, p0.y - rect.height), fromHead);
//			wire.addPointFrom(3, new Point2D(p1.x, p0.y - rect.height), fromHead);
//			if (wire.getSize() == 5) {
//				Component c = connect.oppositeComponent();
//				if (c != null)
//					c.refactorConnections();
//			}
//		} else if (p0.x == p1.x) { // vertical
//			wire.addPointFrom(1, new Point2D(p0.x + Settings.WIRE_LENGTH, p0.y), fromHead);
//			wire.addPointFrom(2, new Point2D(p0.x + Settings.WIRE_LENGTH, p1.y), fromHead);
//			if (wire.getSize() == 4) {
//				Component c = connect.oppositeComponent();
//				if (c != null)
//					c.refactorConnections();
//			}
//		} else if (p0.x < p1.x && p0.x + Settings.WIRE_LENGTH > p1.x) { // too
//																		// short
//			if (wire.getSize() <= 3 && connect.oppositeComponent() != null)
//				return;
//			p1.setLocation(p0.x + Settings.WIRE_LENGTH, p0.y);
//			if (wire.getSize() > 2) {
//				Point2D p2 = wire.getPoint(index0 + dir + dir);
//				p2.setLocation(p0.x + Settings.WIRE_LENGTH, p2.y);
//			}
//		}
//		try {
//			debugg();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		wire.align();
//	}
//
//	private void refactorFromNorth(Connection connect, int index0, int dir) {
//		Wire wire = connect.getWire();
//		Boolean fromHead = connect.isHead();
//		Point2D p0 = wire.getPoint(index0);
//		Point2D p1 = wire.getPoint(index0 + dir);
//		if (p0.y < p1.y) { // vertical but wrong orientation
//			wire.addPointFrom(1, new Point2D(p0.x, p0.y - Settings.WIRE_LENGTH), fromHead);
//			wire.addPointFrom(2, new Point2D(p0.x + rect.width, p0.y - Settings.WIRE_LENGTH), fromHead);
//			wire.addPointFrom(3, new Point2D(p0.x + rect.width, p1.y), fromHead);
//			if (wire.getSize() == 5) {
//				Component c = connect.oppositeComponent();
//				if (c != null)
//					c.refactorConnections();
//			}
//		} else if (p0.y == p1.y) { // horizontal
//			wire.addPointFrom(1, new Point2D(p0.x, p0.y - Settings.WIRE_LENGTH), fromHead);
//			wire.addPointFrom(2, new Point2D(p1.x, p0.y - Settings.WIRE_LENGTH), fromHead);
//			if (wire.getSize() == 4) {
//				Component c = connect.oppositeComponent();
//				if (c != null)
//					c.refactorConnections();
//			}
//		} else if (p1.y < p0.y && p0.y - Settings.WIRE_LENGTH < p1.y) { // too
//																		// short
//			if (wire.getSize() <= 3 && connect.oppositeComponent() != null)
//				return;
//			p1.setLocation(p0.x, p0.y - Settings.WIRE_LENGTH);
//			if (wire.getSize() > 2) {
//				Point2D p2 = wire.getPoint(index0 + dir + dir);
//				p2.setLocation(p2.x, p0.y - Settings.WIRE_LENGTH);
//			}
//		}
//		wire.align();
//		try {
//			debugg();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void refactorFromSouth(Connection connect, int index0, int dir) {
//		Wire wire = connect.getWire();
//		Boolean fromHead = connect.isHead();
//		Point2D p0 = wire.getPoint(index0);
//		Point2D p1 = wire.getPoint(index0 + dir);
//		if (p0.y > p1.y) { // vertical but wrong orientation
//			wire.addPointFrom(1, new Point2D(p1.x, p0.y + Settings.WIRE_LENGTH), fromHead);
//			wire.addPointFrom(2, new Point2D(p0.x + rect.width, p0.y + Settings.WIRE_LENGTH), fromHead);
//			wire.addPointFrom(3, new Point2D(p0.x + rect.width, p1.y), fromHead);
//			if (wire.getSize() == 5) {
//				Component c = connect.oppositeComponent();
//				if (c != null)
//					c.refactorConnections();
//			}
//		} else if (p0.y == p1.y) { // horizontal
//			wire.addPointFrom(1, new Point2D(p0.x, p0.y + Settings.WIRE_LENGTH), fromHead);
//			wire.addPointFrom(2, new Point2D(p1.x, p0.y + Settings.WIRE_LENGTH), fromHead);
//			if (wire.getSize() == 4) {
//				Component c = connect.oppositeComponent();
//				if (c != null)
//					c.refactorConnections();
//			}
//		} else if (p1.y < p0.y && p0.y - Settings.WIRE_LENGTH < p1.y) { // too
//																		// short
//			if (wire.getSize() <= 3 && connect.oppositeComponent() != null)
//				return;
//			p1.setLocation(p0.x, p0.y + Settings.WIRE_LENGTH);
//			if (wire.getSize() > 2) {
//				Point2D p2 = wire.getPoint(index0 + dir + dir);
//				p2.setLocation(p2.x, p0.y + Settings.WIRE_LENGTH);
//			}
//		}
//		wire.align();
//		try {
//			debugg();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	

}