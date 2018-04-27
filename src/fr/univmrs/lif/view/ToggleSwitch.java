package fr.univmrs.lif.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ToggleSwitch extends VBox {
	
	private final Label label = new Label();
	private final Button button = new Button();
	
	private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);
	
	public SimpleBooleanProperty switchOnProperty() { return switchedOn; }
	
	private void init(double height) {
		
		button.setFocusTraversable(false);
		button.setText("Mode");
		label.setText("Editor");
		getChildren().addAll(label, button);	
		button.setOnAction((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		label.setOnMouseClicked((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		setStyle(height);
		bindProperties();
	}
	
	private void setStyle(double height) {
		//Default Width
		setWidth(80);
		setHeight(height);
		label.setAlignment(Pos.CENTER);
		setStyle("-fx-background-color: grey; -fx-text-fill:black;  -fx-font-weight:bold; -fx-background-radius: 4;");
		setAlignment(Pos.CENTER_LEFT);
	}
	
	private void bindProperties() {
		label.prefWidthProperty().bind(widthProperty());
		label.prefHeightProperty().bind(heightProperty().divide(2));
		button.prefWidthProperty().bind(widthProperty());
		button.prefHeightProperty().bind(heightProperty().divide(2));
	}
	
	public ToggleSwitch(double height) {
		init(height);
		switchedOn.addListener((a,b,c) -> {
			if (c) {
						
                		label.setText("Simulator");
                		setStyle("-fx-background-color: green; -fx-font-weight:bold;");
                		label.toFront();
            		}
            		else {
            		
            			label.setText("Editor");
            			setStyle("-fx-background-color: grey; -fx-font-weight:bold;");
                		button.toFront();
            		}
		});
	}
	
	public void fire(){
		button.fire();
	}
}