package fr.univmrs.lif.view.library;
import java.io.IOException;

import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;

import fr.univmrs.lif.tools.ComponentType;
import fr.univmrs.lif.view.componentIcon.GateAND;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.SVGPath;

public class LibraryComponent extends VBox{

	@FXML private Label componentName;
    @FXML private LibraryDragIcon componentIcon;

	public LibraryDragIcon getComponentIcon() {
		return componentIcon;
	}

	private ComponentType type = null;

	public LibraryComponent() {

		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("libraryComponent.fxml")
				);

		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);

		try { 
			fxmlLoader.load();

		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	
	}

	@FXML
	private void initialize() {}

	public void relocateToPoint (Point2D p) {

		//relocates the object to a point that has been converted to
		//scene coordinates
		Point2D localCoords = getParent().sceneToLocal(p);

		relocate ( 
				(int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
				(int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
				);
	}

	public ComponentType getType () { return type; }

	public void setType (ComponentType type) {

		this.type = type;


		//added because the cubic curve will persist into other icons
		//if (this.getChildren().size() > 0)
		
		
		LibraryDragIcon dragIcon = new LibraryDragIcon();
		dragIcon.setType(type);

		switch (type) {



		case AND:
			componentName.setText("AND");
			componentIcon = dragIcon;
			getChildren().add(dragIcon);			
			break;


		default:
			break;
		}
	}
}

