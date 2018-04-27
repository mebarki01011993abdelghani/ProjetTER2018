package fr.univmrs.lif.view.library;

import java.io.IOException;

import javax.xml.namespace.QName;

import fr.univmrs.lif.tools.ComponentType;
import fr.univmrs.lif.view.componentIcon.GateAND;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class LibraryDragIcon extends AnchorPane{
    @FXML private AnchorPane libraryIcon;

	private ComponentType type = null;

	public LibraryDragIcon() {

		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("libraryDragIcon.fxml")
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

		getChildren().clear();
		
		switch (type) {

		case AND:
			Pane and = new GateAND(2);
			getChildren().add(and);
			break;


		default:
			break;
		}
	}

	public String getText() {
		// TODO Auto-generated method stub
		return "and";
	}
}

