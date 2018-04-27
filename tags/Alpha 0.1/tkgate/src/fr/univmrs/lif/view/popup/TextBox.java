package fr.univmrs.lif.view.popup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Popup with textfield.
 * @author Aurelien
 *
 */
public class TextBox {
	
static String answer;
	
	public static String display(String title, String message){
		Stage windows = new Stage();
		windows.initModality(Modality.APPLICATION_MODAL);
		windows.setTitle(title);
		windows.setMinWidth(250);
		Label label = new Label();
		label.setText(message);
		Button okButton = new Button("Ok");
		Button cancelButton = new Button("Cancel");
		
		TextField textField = new TextField();
	
		okButton.setOnAction(e -> {
			answer = textField.getText();
			answer.trim();
			//TODO rechercher dans les noms de module si il existe
			if(answer.isEmpty())
				answer = null;
			windows.close();
		});
		
		cancelButton.setOnAction(e -> {
			answer = null;
			windows.close();
		});
	
		VBox layout = new VBox(25);
		layout.setPadding(new Insets(25));
		HBox layoutButton = new HBox(25);
		layoutButton.getChildren().addAll(okButton,cancelButton);
		layout.getChildren().addAll(label,textField,layoutButton);
		layout.setAlignment(Pos.CENTER);
		layoutButton.setAlignment(Pos.CENTER_RIGHT);
		Scene scene = new Scene(layout);
		windows.setScene(scene);
		windows.showAndWait();
		
		return answer;
	}

}
