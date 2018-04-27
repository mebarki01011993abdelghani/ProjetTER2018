package fr.univmrs.lif.view.popup;

import fr.univmrs.lif.controller.MainController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorBox {
	
	public static void display(String message){
	
		Stage windows = new Stage();
		windows.initModality(Modality.APPLICATION_MODAL);
		windows.setTitle("Error");
		windows.setMinWidth(250);
		Label label = new Label();
		label.setText(message);
		Button okButton = new Button("Ok");
	
		okButton.setOnAction(e -> {
			windows.close();
		});
		
		okButton.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	            if (ke.getCode().equals(KeyCode.ENTER))
	            {
	                okButton.fire();
	            }
	        }
	    });
	
		VBox layout = new VBox(25);
		layout.setPadding(new Insets(25));
		HBox layoutButton = new HBox(25);
		layoutButton.getChildren().addAll(okButton);
		layoutButton.setAlignment(Pos.BOTTOM_RIGHT);
		layout.getChildren().addAll(label,layoutButton);
		layout.setAlignment(Pos.CENTER);
		layoutButton.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		windows.setScene(scene);
		windows.showAndWait();
		
	}

}
