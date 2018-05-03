package fr.univmrs.lif.view;

//import javafx.scene.Group;
//import javafx.scene.Node;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.transform.Scale;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;

public class ZoomableScrollPane extends ScrollPane{
	  Group zoomGroup;
	  Scale scaleTransform;
	  Node content;
	  double oldZoom;
	  
	  public ZoomableScrollPane(Node content)
	  {
		super(content);
	    this.content = content;
//	    Group contentGroup = new Group();
//	    contentGroup.getChildren().add(content);
//	    zoomGroup.getChildren().add(content);
//	    setContent(contentGroup);
//	    scaleTransform = new Scale(1, 1, 0, 0);
//	    content.getTransforms().add(scaleTransform);
//	    oldZoom = 1;
//	    setPrefSize(300, 300);
	    setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	    setFitToWidth(true);
        setFitToHeight(true);
        
	  }
	  
	  public void zoom(double zoomFactor){
		  
		  content.getTransforms().add(new Scale(1/oldZoom, 1/oldZoom, 0, 0));
		  scaleTransform = new Scale(zoomFactor, zoomFactor, 0, 0);
		  content.getTransforms().add(scaleTransform);
		  oldZoom = zoomFactor;
	  }
	  
	}