package fr.univmrs.lif.view.popup;

import java.util.Optional;

import fr.univmrs.lif.tools.NameManager;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

	public static String display(String type){
		do{
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle(type);
			dialog.setHeaderText("Please, Enter name of "+type.toLowerCase());
			dialog.setContentText("Name:");
			dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {

				@Override
				public void handle(DialogEvent event) {

				}
			});
			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
				String name = result.get().toString();
				if(name == null) return null;
				
				if(name.isEmpty()){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Empty field");
					alert.setContentText("Please, Enter name");
					alert.showAndWait();
				}else if(NameManager.exist(name)){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Name already exist");
					alert.setContentText("Please, Enter an other name");
					alert.showAndWait();
				}else{
				
					return name;
				}
			}else{
				return null;
			}
		}while(true);

	}

}
