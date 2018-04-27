package fr.univmrs.lif.controller.bottomBar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.model.simulation.Sensor;
import fr.univmrs.lif.view.bottomBar.ChronogramView;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

/**
 * Control chronogram functionalities
 * @author Aurelien
 *
 */
public class ChronogramController implements Initializable{

	private static VBox vBoxLabelSensor;
	private static VBox vBoxChronogram;
	@FXML private SplitPane root;
	@FXML private HBox rightPane;
	@FXML private VBox chronogramControlPanel;
	@FXML private Group chronogramGroup;
	@FXML private ScrollPane chronogramScrollPane;
	@FXML private ScrollPane labelScrollPane;
	@FXML private Slider speed;
	@FXML private Label speedValue;
	@FXML private Button playButton;
	@FXML private Button pauseButton;
	@FXML private Button clearButton;
	@FXML private Button print;

	static int time = 0;

	static ObservableList<ChronogramView> chronograms = FXCollections.observableArrayList();
	static DoubleProperty timeProperty = new SimpleDoubleProperty(0);

	NumberAxis xAxis = new NumberAxis();
	
	private BooleanProperty pause = new SimpleBooleanProperty(true);
	private Line lineCursor = new Line();
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		lineCursor.setStartY(0);
		lineCursor.endYProperty().bind(chronogramScrollPane.heightProperty().subtract(10));
		lineCursor.setVisible(false);

		vBoxChronogram = new VBox();
		vBoxChronogram.setFillWidth(true);
		vBoxChronogram.setPadding(new Insets(5));
		vBoxLabelSensor = new VBox(5);
		vBoxLabelSensor.setFillWidth(true);
		vBoxLabelSensor.setPadding(new Insets(0,0,32,0));

		

		IntegerProperty intSpeedValue = new SimpleIntegerProperty();
		intSpeedValue.bind(speed.valueProperty());
		speedValue.textProperty().bind(intSpeedValue.asString());

		playButton.disableProperty().bind(pause.not());
		pauseButton.disableProperty().bind(pause);
		
		// Build Axis
		xAxis.setAutoRanging(false);
		xAxis.upperBoundProperty().bind(timeProperty);
		xAxis.setMinorTickCount(1);
		xAxis.setTickUnit(50);
		xAxis.setAnimated(false);
		xAxis.visibleProperty().bind(new When(timeProperty.isEqualTo(0)).then(false).otherwise(true));
		xAxis.setPrefWidth(0);
		
		chronogramGroup.getChildren().add(lineCursor);
		chronogramGroup.getChildren().add(vBoxChronogram);
		vBoxChronogram.getChildren().add(xAxis);
		labelScrollPane.setContent(vBoxLabelSensor);

		// Binding
		chronogramControlPanel.disableProperty().addListener(c -> clearButton.fire());
		chronogramControlPanel.disableProperty().bind(MainController.onSimulationProperty().not());
		chronogramControlPanel.disableProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue == true){
					pause.set(true);
					pauseButton.fire();
				}
			}
		});
	

		labelScrollPane.vmaxProperty().bind(chronogramScrollPane.vmaxProperty());
		labelScrollPane.vvalueProperty().bind(chronogramScrollPane.vvalueProperty());

	}

	/**
	 * Update chronogram diagram
	 * @param sensor
	 */
	public void update() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				for(ChronogramView cv : chronograms){

					cv.update(timeProperty.get());
				}


				time++;
				timeProperty.set(time);
				root.layout();
				chronogramScrollPane.setHvalue(1);

				lineCursor.setStartX(lineCursor.getStartX()+1);
				lineCursor.setEndX(lineCursor.getEndX()+1);

				if(time == 5000) clearButton.fire();
			}
		});




	}


	/**
	 * add sensor to chronogram diagram
	 * @param sensor
	 */
	public static void addSensor(Sensor sensor){

		
		ChronogramView newChronogramSensor = new ChronogramView(sensor);
		chronograms.add(newChronogramSensor);
		vBoxChronogram.getChildren().add(0, newChronogramSensor);
		Label sensorLabel = new Label();
		sensorLabel.textProperty().bind(sensor.getWire().getNameProperty());
		sensorLabel.setPadding(new Insets(5));
		vBoxLabelSensor.getChildren().add(0, sensorLabel);


	}

	/**
	 * Remove sensor form chronogram diagram
	 * @param sensor
	 */
	public static void removeSensor(Sensor sensor) {
		ChronogramView chronogramToRemove = null;
		for(ChronogramView cv : chronograms){
			if(cv.getSensor() == sensor)
				chronogramToRemove = cv;
		}
		
		int indexChronogram = vBoxChronogram.getChildren().indexOf(chronogramToRemove);
		vBoxChronogram.getChildren().remove(indexChronogram);
		vBoxLabelSensor.getChildren().remove(indexChronogram);
		
			
	}


	  @FXML
	    void pauseChronogramKey(KeyEvent event) {
		  
		  if(event.getCode() == KeyCode.SPACE &&
	    			event.getEventType()== KeyEvent.KEY_RELEASED){
			  pauseButton.fire();
		  }
		  
	    }

	  

	    @FXML
	    void playChronogramKey(KeyEvent event) {
	  
	    	if(event.getCode() == KeyCode.SPACE &&
	    			event.getEventType()== KeyEvent.KEY_PRESSED){
				  playButton.fire();
			
			  }
	    }



	/**
	 * start current acquisition 
	 * @param event
	 */
	@FXML
	void playChronogram(ActionEvent event) {

		new playerFrame().start();
		pause.set(false);
	}

	/**
	 * Pause current acquisition 
	 * @param event
	 */
	@FXML
	void pauseChronogram(ActionEvent event) {
		pause.set(true);
	}
	
	/**
	 * Clear chronograms diagram, reset timeline.
	 * @param event
	 */
	@FXML
	void clearChronogram(ActionEvent event) {
		for(ChronogramView cv : chronograms)
			cv.clear();
		time = 0;
		timeProperty.set(time);
	}

	/**
	 * Show cursor line when mouse exit from chronograms diagram
	 * @param event
	 */
	@FXML
	void hideLineCursor(MouseEvent event) {
		lineCursor.setVisible(false);
	}

	/**
	 * Show cursor line when mouse enter into chronograms diagram
	 * @param event
	 */
	@FXML
	void showLineCursor(MouseEvent event) {
		lineCursor.setVisible(true);
	}

	/**
	 * Move the cursor line when mouse move
	 * @param mouseEvent
	 */
	@FXML
	void updateLineCursor(MouseEvent mouseEvent) {
		Point2D coordsScene = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
		Point2D localCoords = chronogramGroup.sceneToLocal(coordsScene);

		lineCursor.setStartX(localCoords.getX());
		lineCursor.setEndX(localCoords.getX());
	}

	/**
	 * Print chronogram as image
	 * @param event
	 */
	@FXML
    void printChronogram(ActionEvent event) {
		// TODO Print only viewPort, choose format between png jpg ..
		WritableImage image = rightPane.snapshot(new SnapshotParameters(), null);

		final FileChooser dialog = new FileChooser();
		dialog.setInitialFileName("Chronogram "+MainController.getCurrentProject().getName()+".png");
		final File file = dialog.showSaveDialog(null);
		if (file != null) { 
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			} catch (IOException e) {
				Console.errorln("Error can't print chronograms.");
			}
		}

    }


	/**
	 * Player loop for update chronogram diagram
	 * @author Aurelien
	 *
	 */
	class playerFrame extends Thread {

		@Override
		public void run() {
			while(!pause.get()){

				update();

				try {
					sleep((long) speed.getValue());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}


		}

	}








}