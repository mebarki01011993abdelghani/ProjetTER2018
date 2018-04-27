package fr.univmrs.lif.view.component;

import java.io.IOException;
import java.util.ArrayList;

import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.inputoutput.Display;
import fr.univmrs.lif.model.component.inputoutput.Led;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

public class DisplayView extends Pane{
	Display model;

	@FXML
	private AnchorPane display;

	@FXML private Path a;
	@FXML private Path b;
	@FXML private Path c;
	@FXML private Path d;
	@FXML private Path e;
	@FXML private Path f;
	@FXML private Path g;
	
	ArrayList<Path> segments;

	public DisplayView(Component component) {
		model = (Display)component;

		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("Display.fxml")
				);

		fxmlLoader.setController(this);

		try { 
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@FXML
	private void initialize() {

		this.getChildren().add(display);

		for(BooleanProperty segment : model.getSegments()){
			segment.addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue) {
					update();
				}
			});
			
		}
		segments = new ArrayList<>();
		segments.add(a);
		segments.add(b);
		segments.add(c);
		segments.add(d);
		segments.add(e);
		segments.add(f);
		segments.add(g);
	}


	protected void update() {
		for(int i = 0; i < 7; i++){
			if(model.getSegments().get(i).get()){
				turnOn(segments.get(i));
			}else{
				turnOff(segments.get(i));
			}
		}
		
	}

	private void turnOn(Path segment){
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				segment.setFill(Color.RED);
			}
		});
		
	
	}

	private void turnOff(Path segment){
		Platform.runLater(new Runnable() {                          
			@Override
			public void run() {
				segment.setFill(Color.web("red",0.3));
			}
		});
		
	}
}
