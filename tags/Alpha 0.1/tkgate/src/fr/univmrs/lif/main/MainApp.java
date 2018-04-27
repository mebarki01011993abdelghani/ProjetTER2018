package fr.univmrs.lif.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {

    private Parent rootNode;
    private static Scene scene;

    public static Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		MainApp.scene = scene;
	}

	@Override
    public void init(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/Main.fxml"));
        try {
			rootNode = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }

    @Override
    public void start(Stage stage) throws Exception {
    	Scene s = new Scene(rootNode);
    	setScene(s);
    	stage.setScene(getScene());
        stage.show();
        
        exitApplication(stage);
    }
   

    public static void main(String[] args) {
        launch(args);
    }
    
    private void exitApplication(Stage stage) {
   	 
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {	
             public void handle(WindowEvent we) {
                 System.exit(0);
             }
         }); 	
	  }
}