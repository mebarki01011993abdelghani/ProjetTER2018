

package fr.univmrs.lif.controller.dragAndDrop;

import com.sun.javafx.scene.control.skin.FXVK.Type;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.view.library.LibraryDragIcon;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class DragAndDropController {

	MainController mainController;

	private LibraryDragIcon componentIcon = null;

	private EventHandler<DragEvent> iconDragOverLibrary= null;
	private EventHandler<DragEvent> iconDragOverDrawingBoard = null;
	private EventHandler<DragEvent> iconDroppedIntoDrawingBoard = null;
	
	boolean isModuleDrag = false;

	public DragAndDropController(MainController mainController){
		this.mainController = mainController;
		
		//Drag icon
		componentIcon = new LibraryDragIcon();
		componentIcon.setVisible(false);
		componentIcon.setOpacity(0.65);
		mainController.getRoot().getChildren().add(componentIcon);
		buildDragHandlers();
	}

	// Drag n drop
	public void addDragDetection(LibraryDragIcon libraryIcon) {

		// Cursor Display for Drag&Drop
		libraryIcon.setOnMouseEntered(e -> libraryIcon.setCursor(Cursor.OPEN_HAND));
		libraryIcon.setOnMousePressed(e -> libraryIcon.setCursor(Cursor.CLOSED_HAND));
		libraryIcon.setOnMouseReleased(e -> libraryIcon.setCursor(Cursor.DEFAULT));


		// Manage drag
		libraryIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event){
				if(MainController.getCurrentBoard() == null){
					event.consume();
					return;
				}
				// set drag event handlers on their respective objects
				mainController.getLibrarySide().setOnDragOver(iconDragOverLibrary);
				mainController.getDrawingBoard().setOnDragOver(iconDragOverDrawingBoard);
				mainController.getDrawingBoard().setOnDragDropped(iconDroppedIntoDrawingBoard);
				
				// get a reference to the clicked DragIcon object
				LibraryDragIcon icn = (LibraryDragIcon) event.getSource();

				//begin drag ops
				componentIcon.setType(icn.getType());
				componentIcon.relocateToPoint(new Point2D (event.getSceneX(), event.getSceneY()));

				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();

				container.addData ("type", componentIcon.getType().toString());
				content.put(DragContainer.AddNode, container);

				componentIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
				componentIcon.setVisible(true);
				componentIcon.setMouseTransparent(true);
				isModuleDrag = false;
				event.consume();
			}
		});
	}
	
	// Drag n drop Modules
	public void addDragDetectionModule(LibraryDragIcon libraryIcon,Module module) {

		// Cursor Display for Drag&Drop
		libraryIcon.setOnMouseEntered(e -> libraryIcon.setCursor(Cursor.OPEN_HAND));
		libraryIcon.setOnMousePressed(e -> libraryIcon.setCursor(Cursor.CLOSED_HAND));
		libraryIcon.setOnMouseReleased(e -> libraryIcon.setCursor(Cursor.DEFAULT));


		// Manage drag
		libraryIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event){
				if(MainController.getCurrentBoard() == null){
					event.consume();
					return;
				}
				// set drag event handlers on their respective objects
				mainController.getLibrarySide().setOnDragOver(iconDragOverLibrary);
				mainController.getDrawingBoard().setOnDragOver(iconDragOverDrawingBoard);
				mainController.getDrawingBoard().setOnDragDropped(iconDroppedIntoDrawingBoard);
				
				// get a reference to the clicked DragIcon object
				LibraryDragIcon icn = (LibraryDragIcon) event.getSource();

				//begin drag ops
				componentIcon.setType(icn.getType());
				componentIcon.relocateToPoint(new Point2D (event.getSceneX(), event.getSceneY()));

				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();

				container.addData ("type", module.getName());
				content.put(DragContainer.AddModule, container);

				componentIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
				componentIcon.setVisible(true);
				componentIcon.setMouseTransparent(true);
				isModuleDrag = true;
				event.consume();
			}
		});
	}

	private void buildDragHandlers() {

		//drag over transition to move widget form left pane to right pane
		iconDragOverLibrary = new EventHandler <DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				Point2D p = mainController.getDrawingBoard().sceneToLocal(event.getSceneX(), event.getSceneY());

				//turn on transfer mode and track in the right-pane's context 
				//if (and only if) the mouse cursor falls within the right pane's bounds.
				if (!mainController.getDrawingBoard().boundsInLocalProperty().get().contains(p)) {

					event.acceptTransferModes(TransferMode.ANY);

					componentIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
					return;
				}

				event.consume();
			}
		};

		iconDragOverDrawingBoard = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {
				event.acceptTransferModes(TransferMode.ANY);

				//convert the mouse coordinates to scene coordinates,
				//then convert back to coordinates that are relative to 
				//the parent of mDragIcon.  Since mDragIcon is a child of the root
				//pane, coodinates must be in the root pane's coordinate system to work
				//properly.
				componentIcon.relocateToPoint(
						new Point2D(event.getSceneX(), event.getSceneY())
						);
				event.consume();
			}
		};


		iconDroppedIntoDrawingBoard = new EventHandler <DragEvent> () {
			@Override
			public void handle(DragEvent event) {
				if(!isModuleDrag){
				DragContainer container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				container.addData("scene_coords", new Point2D((event.getSceneX()), (event.getSceneY())));

				ClipboardContent content = new ClipboardContent();
				content.put(DragContainer.AddNode, container);

				event.getDragboard().setContent(content);
				event.setDropCompleted(true);
				}else{
				DragContainer container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddModule);

				container.addData("scene_coords", new Point2D((event.getSceneX()), (event.getSceneY())));

				ClipboardContent content = new ClipboardContent();
				content.put(DragContainer.AddModule, container);

				event.getDragboard().setContent(content);
				event.setDropCompleted(true);
				}
			}
		};
	

		mainController.getRoot().setOnDragDone (new EventHandler <DragEvent> (){

			@Override
			public void handle (DragEvent event) {
				mainController.getDrawingBoard().removeEventHandler(DragEvent.DRAG_OVER, iconDragOverDrawingBoard);
			    mainController.getLibrarySide().removeEventHandler(DragEvent.DRAG_OVER, iconDragOverLibrary);

				mainController.getDrawingBoard().removeEventHandler(DragEvent.DRAG_DROPPED, iconDroppedIntoDrawingBoard);
				
				componentIcon.setVisible(false);

				//Create node drag operation
				DragContainer container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				if (container != null) {
					if (container.getValue("scene_coords") != null) {
			
						//creation of new component on the board.
						//drawingBoardController.addNewComponent(currentBoard, container);
						//TODO
						Point2D position = container.getValue("scene_coords");
						ComponentType type = ComponentType.valueOf(container.getValue("type"));
						MainController.getCurrentBoard().addComponent(type, position);
					}
				}

				container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddModule);
				if (container != null) {
					if (container.getValue("scene_coords") != null) {
			
						//creation of new component on the board.
						//drawingBoardController.addNewComponent(currentBoard, container);
						//TODO
						Point2D position = container.getValue("scene_coords");
						
						MainController.getCurrentBoard().addComponentModule(container.getValue("type"), position);
					}
				}

				event.consume();
			}
		});		
	}
}
