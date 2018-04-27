package fr.univmrs.lif.main;

import java.io.IOException;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.controller.library.LibraryController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {
    private Parent rootNode;
    private static Scene scene;
    private static Stage stage;

    public static Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		MainApp.scene = scene;
	}
	
	public static Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		MainApp.stage = stage;
	}
	
	public static void upDateTitle(String title){
//		stage.setTitle("Logic Gates Simulator - "+title);
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
    	
    	setStage(stage);
    	Scene s = new Scene(rootNode,1200,800);
    	setScene(s);
    	stage.setTitle("FXGate");
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