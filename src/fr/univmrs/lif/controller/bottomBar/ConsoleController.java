package fr.univmrs.lif.controller.bottomBar;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ConsoleController implements Initializable{

	@FXML
	private TextFlow console;
	
	@FXML
    private ScrollPane logScrollPane;

	private IntegerProperty fontSize = new SimpleIntegerProperty(14);

	public void log(String value) {
		Text text = new Text(value);
		text.setStyle("-fx-font-size: "+fontSize+";");
		console.getChildren().add(text);
		
	}
	public void log(String value,String style) {

		Text text = new Text(value);
		text.setStyle(style + "-fx-font-size: "+fontSize+";");
		console.getChildren().add(text);
		
	}

	public void logln(String value) {
		Text text = new Text(value+"\n");
		text.setStyle("; -fx-font-size: "+fontSize+";");
		console.getChildren().add(text);
		autoScroll();
	}
	public void logln(String value,String style) {
		Text text = new Text(value+ "\n");
		text.setStyle(style + "; -fx-font-size: "+fontSize+";");
		
		console.getChildren().add(text);
		autoScroll();
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		console.setPadding(new Insets(5));
		console.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		Console staticConsole = new Console(this);
		
	}
	
	/**
	 * Keep VBar log console on bottom
	 */
	private void autoScroll(){
		logScrollPane.setVvalue(1);
		logScrollPane.layout();
	}
}