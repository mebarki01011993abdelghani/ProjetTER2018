package fr.univmrs.lif.view.componentIcon;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

public class GateAND extends Pane{
	
	public GateAND(int numberOfPin){
		
		if(numberOfPin< 2) numberOfPin=2;
		
		int baseSize = (10+10*numberOfPin);
		SVGPath pathBody = new SVGPath();
		Shape finalShape = pathBody;
		
		pathBody.setContent("M 0 10, l 20 0,M 0 30, l 20 0, M 20 0, l 0 40 , M 20 40, A 30 21 0 1 0 20 0 ,M 60 20 , l 20 0 ,Z ");
		finalShape.setStrokeWidth(1);
		finalShape.setFill(Color.TRANSPARENT);
		finalShape.setStroke(Color.BLACK);
//		finalShape.setScaleX(1.2);
//		finalShape.setScaleY(1.2);
		getChildren().add(finalShape);
		
//		this.setScaleX(1.2);
//		this.setScaleY(1.2);
		
	}
		

}
