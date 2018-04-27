package fr.univmrs.lif.view.library;
import java.io.IOException;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.component.Module;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * Create Label + icon in library
 * @author Aurelien
 *
 */
public class LibraryComponent extends HBox{

	@FXML private Label componentName;
    private LibraryDragIcon componentIcon;

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

	public ComponentType getType () { return type; }

	public void setType (ComponentType type) {

		this.type = type;
		
		LibraryDragIcon dragIcon = new LibraryDragIcon();
		dragIcon.setType(type);
		componentIcon = dragIcon;
	
		componentName.setText(type.toString());	
	
		
		getChildren().add(dragIcon);

	}
	
	/**
	 * For Modules
	 * @param type
	 * @param module
	 */
	public void setType (Module module) {

		LibraryDragIcon dragIcon = new LibraryDragIcon();
		dragIcon.setType(module.getType());
		componentIcon = dragIcon;
		componentName.textProperty().bind(module.getNameProperty());
		getChildren().add(dragIcon);

	}
}

