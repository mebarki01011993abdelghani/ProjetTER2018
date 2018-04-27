package fr.univmrs.lif.view;


import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.model.component.Module;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.transform.Scale;

public class DrawingBoardTab extends Tab{

	private final Group group = new Group();
    static BooleanProperty pannable = new SimpleBooleanProperty();
   
	private ScrollPane scrollPane = new ScrollPane();
	

	Canvas canvas;
	private double oldZoom = 1;
	private Scale scaleTransform;

	public DrawingBoardTab(Module module){

		textProperty().bind(module.getNameProperty());

		canvas = new Canvas(module);
		
		group.getChildren().add(canvas);
		scrollPane.setPannable(true);
		scrollPane.pannableProperty().bind(pannable);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setContent(group);
		scrollPane.setStyle("-fx-background:white;");
		this.setContent(scrollPane);
		

	}

	public Canvas getBoard(){
		return canvas;
	}

	public void handelSelection() {
		setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if(isSelected()){
					MainController.setCurrentBoard(canvas);
				}
			}
		});

	}
	
	 public static BooleanProperty getPannable() {
			return pannable;
		}

		public static void setPannable(boolean pannable) {
			DrawingBoardTab.pannable.set(pannable);
		}

		public void zoom(double zoomValue) {

//			group.getTransforms().add(new Scale(1/oldZoom, 1/oldZoom, 0, 0));
//			scaleTransform = new Scale(zoomValue, zoomValue, 0, 0);
//			canvas.getTransforms().add(scaleTransform);
//			oldZoom = zoomValue;
//			group.layout();
//			scrollPane.layout();
		
		}
	
	

}
