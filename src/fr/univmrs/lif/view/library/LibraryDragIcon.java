package fr.univmrs.lif.view.library;

import java.io.IOException;

import fr.univmrs.lif.enumeration.ComponentType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;


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

	public void setType (ComponentType type){

		this.type = type;

		getChildren().clear();

		FXMLLoader fxmlLoader = null;
		try { 
			switch (type) {

			// GATE
			case NOT :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("NotGateIcon.fxml"));

				break;
				
			case AND :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("AndGateIcon.fxml"));

				break;
			case NAND :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("NotAndGateIcon.fxml"));

				break;

			case OR :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("OrGateIcon.fxml"));

				break;
			case NOR :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("NotOrGateIcon.fxml"));

				break;
			case XOR :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("XOrGateIcon.fxml"));

				break;
			case XNOR :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("XNOrGateIcon.fxml"));

				break;
			

			// INPUT
			case SWITCH :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("SwitchIcon.fxml"));
			
				break;
			case CLOCK :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("ClockIcon.fxml"));
				break;
			case VDD :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("VddIcon.fxml"));
				break;
			case GND :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("GroundIcon.fxml"));
				break;
				
			// OUTPUT
			case LED :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("LEDIcon.fxml"));
				
				break;
			case DISPLAY :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("DisplayIcon.fxml"));
				
				break;
			case MUX :
				
				fxmlLoader = new FXMLLoader(
						getClass().getResource("MultiplexerIcon.fxml"));
				
				break;
			case DECODER :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("DecoderIcon.fxml"));
				
				break;
			case ADDER :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("AdderIcon.fxml"));
				
				break;
				
			case MODULE :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("ModuleIcon.fxml"));
				
				break;
				
			case INPUTMODULE :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("InputModuleIcon.fxml"));
				
				break;
				
			case OUTPUTMODULE :
				fxmlLoader = new FXMLLoader(
						getClass().getResource("OutputModuleIcon.fxml"));
				
				break;


			default:
				break;
			}
		
			fxmlLoader.load();
			getChildren().add(fxmlLoader.getRoot());
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

	}

	
}

