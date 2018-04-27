package fr.univmrs.lif.view.popup;


import java.util.Stack;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Popup with Yes / No buttons
 * @author Aurelien
 *
 */
public class ConfirmBox {
	static boolean answer;
	
	public static boolean display(String title, String message){
		Stage windows = new Stage();
		windows.initModality(Modality.APPLICATION_MODAL);
		windows.setTitle(title);
		windows.setMinWidth(250);
		Label label = new Label();
		label.setText(message);
		Button yesButton = new Button("Yes");
		Button noButton = new Button("No");
	
		yesButton.setOnAction(e -> {
			answer = true;
			windows.close();
		});
		
		noButton.setOnAction(e -> {
			answer = false;
			windows.close();
		});
	
		VBox layout = new VBox(25);
		layout.setPadding(new Insets(25));
		HBox layoutButton = new HBox(25);
		layoutButton.getChildren().addAll(yesButton,noButton);
		layout.getChildren().addAll(label,layoutButton);
		layout.setAlignment(Pos.CENTER);
		layoutButton.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		windows.setScene(scene);
		windows.showAndWait();
		
		return answer;
	}

}
