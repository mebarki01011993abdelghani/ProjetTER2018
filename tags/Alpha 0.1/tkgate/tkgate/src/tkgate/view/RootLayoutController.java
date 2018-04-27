package tkgate.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import tkgate.EditorContext;

public class RootLayoutController {
	
	@FXML  
	private MenuItem addAnd;
	
	private EditorContext edContext;

	
	@FXML
	private void initialize() {}
	
    public RootLayoutController() {}
	
	@FXML
	private void handleAdd() {
		addAnd.setOnAction(new EventHandler<ActionEvent>() { 
		    @Override 
		    public void handle(ActionEvent actionEvent) { 
		        String gtype = ((MenuItem) actionEvent.getSource()).getText();
		        edContext.addComponent(gtype);
		    } 
		});
	}

	public void setEdContext(EditorContext edContext) {
		this.edContext = edContext;
		
	}

}
