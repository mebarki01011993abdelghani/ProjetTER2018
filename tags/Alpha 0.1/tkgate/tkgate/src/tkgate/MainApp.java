package tkgate;

import java.io.IOException;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tkgate.modele.Module;
import tkgate.view.DrawingController;
import tkgate.view.RootLayoutController;

public class MainApp extends Application {
    private Scene scene;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private StackPane drawing;
    private Module module;

    
  
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JGate");
        initRootLayout();      
    }
    
    
 
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
    	try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            RootLayoutController rootController = loader.getController();  
            showDrawing(rootController);
           
            
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }
    
    /**
     * Shows drawing panel inside the root layout.
     */
    public void showDrawing(RootLayoutController rootController) {
       
    	try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Drawing.fxml"));
            drawing = (StackPane) loader.load();


            // Set drawing into the center of root layout.
            rootLayout.setCenter(drawing);
            
            // Give the controller access to module and scene.
            DrawingController drawController = loader.getController();
            module = new Module("main");
            drawController.setDrawing(module, scene);
                      
            EditorControler edControl = new EditorControler(module);
            EditorContext edContext = new EditorContext(drawController, edControl);
    	    drawController.setEditorContext(edContext);
    	    rootController.setEdContext(edContext);
       
            
    	 } catch (IOException e) {
             e.printStackTrace();
         }    
    }

    
    
    
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    
    /**
     * Returns the scene.
     * @return
     */
    public Scene getScene() {
        return scene;
    }


	public static void main(String[] args) {
		launch(args);
	}
}
