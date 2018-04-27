package fr.univmrs.lif.controller.library;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.view.library.LibraryComponent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ModuleListController {

	private MainController mainController;

	@FXML
	private ListView<Module> moduleList;

	//TODO
	public void initModuleList(Module module){

		moduleList.getItems().add(module);
		moduleList.setCellFactory(new Callback<ListView<Module>, 
				ListCell<Module>>() {
			@Override 
			public ListCell<Module> call(ListView<Module> list) {
				return new ModuleIcon();
			}

		}
				);

	}

	public void addModule(Module module){
		
		moduleList.getItems().add(module);
		moduleList.setCellFactory(new Callback<ListView<Module>, 
				ListCell<Module>>() {
			@Override 
			public ListCell<Module> call(ListView<Module> list) {
				return new ModuleIcon();
			}

		}
				);
		
//		mainController.getDragAndDropController().addDragDetectionModule(module);
		

	}

	public void injectMainController(MainController mainController){
		this.mainController = mainController;
		initModuleList(MainController.getCurrentProject().getMain());
	}


	static class ModuleIcon extends ListCell<Module> {
		private Image iconModule = new Image("/ModuleIcon.gif");
		@Override
		public void updateItem(Module module, boolean empty) {
			super.updateItem(module, empty);
			ImageView imageView = new ImageView(iconModule);
			if (empty) {
				setText(null);
				setGraphic(null);
			} else {               
				imageView.setImage(iconModule);
				setText(module.getName());
				setGraphic(imageView);
			}
		}

		{
			setOnDragDetected( ( MouseEvent event ) ->
			{
				
				System.out.println( "listcell setOnDragDetected" +  getItem() );
				Dragboard db = startDragAndDrop( TransferMode.ANY );
				ClipboardContent content = new ClipboardContent();
				content.putString( getItem().getName() );
				db.setContent( content );
				event.consume();

			} );
		}



	}
}
