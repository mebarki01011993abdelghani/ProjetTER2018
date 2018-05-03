package fr.univmrs.lif.controller;

import java.awt.MouseInfo;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import fr.univmrs.lif.controller.library.LibraryController;
import fr.univmrs.lif.main.MainApp;
import fr.univmrs.lif.model.EditorContext;
import fr.univmrs.lif.model.EditorControler;
import fr.univmrs.lif.model.Point;
import fr.univmrs.lif.model.Project;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.view.DrawingBoard;
import fr.univmrs.lif.view.ZoomableScrollPane;
import fr.univmrs.lif.view.component.ComponentView;
import fr.univmrs.lif.view.library.LibraryComponent;
import fr.univmrs.lif.view.library.LibraryDragIcon;
import fr.univmrs.lif.view.popup.ConfirmBox;
import fr.univmrs.lif.view.popup.TextBox;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class MainController {

	private DrawingBoardController drawingBoardController;
	@FXML private LibraryController libraryController;
	private ModuleTreeViewController moduleTreeViewController;

	@FXML private AnchorPane root;
	
	@FXML private MenuItem menuBarFileNewProject;
	@FXML private MenuItem menuBarFileOpen;
	@FXML private MenuItem menuBarFileSave;
	@FXML private MenuItem menuBarFileQuit;
	@FXML private MenuItem menuBarModuleNewModule;
	
	@FXML private Button toolBarRunButton;
	@FXML private Slider toolBarZoomSlider;
	@FXML private TabPane tabPane;
	@FXML private Tab tab;
	@FXML private BorderPane libraryPane;
	@FXML private Accordion library;
	
	@FXML private TreeView<String> moduleTreeView;

	
	private Project currentProject;
	/**
	 * Factor of zoom currently active 
	 */
	private double zoom = 1;
	
	private DrawingBoard currentBoard;
	private TabPaneController tabPaneController;
	//	private ZoomableScrollPane scrollDrawingBoard;

	//private EditorControler edControl;
	//private EditorContext edContext;
	
	private LibraryDragIcon componentIcon = null;

	private EventHandler<DragEvent> iconDragOverLibrary = null;
	private EventHandler<DragEvent> iconDragDropped = null;
	private EventHandler<DragEvent> iconDragOverDrawingBoard = null;

	private EventHandler<DragEvent> iconDragOverScrollDrawingBoard = null;
	private EventHandler<DragEvent> iconDragScrollDrawingBoardDropped = null;

	@FXML
	private void initialize() {

		drawingBoardController = new DrawingBoardController();
		libraryController.injectMainController(this);
		moduleTreeViewController = new ModuleTreeViewController();
		tabPaneController = new TabPaneController();
		tabPaneController.injectMainController(this);
		//drawingBoardController.injectMainController(this);

		//Add one icon that will be used for the drag-drop process
		//This is added as a child to the root anchorpane so it can be visible
		//on both sides of the split pane.
		componentIcon = new LibraryDragIcon();

		componentIcon.setVisible(false);
		componentIcon.setOpacity(0.65);
		root.getChildren().add(componentIcon);

		libraryController.initLibrary();
		moduleTreeViewController.initModuleTreeView(moduleTreeView);
		tabPaneController.initTabPane(tabPane);
	

		buildDragHandlers();
		menuBarFileNewProject.fire();
		
	}

	@FXML
	void handleMenuBarFileNewProject(ActionEvent event) {

		currentProject = new Project();
		//moduleTreeViewController.addModule(currentProject.getMain().getName());
		DrawingBoard drawingBoard = new DrawingBoard(currentProject.getMain());
		currentBoard = drawingBoard;
		//ZoomableScrollPane scrollPane = new ZoomableScrollPane(currentBoard);
		//		scrollDrawingBoard = scrollPane;
		Tab newTab = null;
		
		newTab = new Tab("main", currentBoard);
		newTab.setClosable(true);
		newTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				currentBoard = drawingBoard;
				//scrollDrawingBoard = scrollPane;
				//edContext = drawingBoard.getContext();
			}
		});
		tabPane.getTabs().add(newTab);
	}
	@FXML
	private void handleMenuBarFileSave(ActionEvent event) {
		menuBarFileSave.setOnAction(new EventHandler<ActionEvent>() { 
			@Override 
			public void handle(ActionEvent actionEvent) {
				
				final FileChooser dialog = new FileChooser(); 
				final File file = dialog.showSaveDialog(null); 
				if (file != null) { 
					try {

						JAXBContext context = JAXBContext.newInstance(Project.class);
						Marshaller m = context.createMarshaller();
						m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
						m.marshal(currentProject, file);
						//m.marshal(p,System.out);
						System.out.println("File saved ");
						//Validator validator = jaxbContext.createValidator();
					} catch (Exception e) {e.printStackTrace();}
					
				}
			}
		});
	}
	@FXML
	void handleMenuBarFileOpen(ActionEvent event){
		
	}
	
	
	@FXML
	void handleMenuBarFileQuit(ActionEvent event){
		boolean answer = ConfirmBox.display("Quit ?", "Are you sure you want to quit ?");
		
	}
	
	@FXML
	void handleMenuBarModuleNewModule(ActionEvent event){
		
		String nameModule = TextBox.display("Module", "Please, Enter name of module");
		if(nameModule == null){
			return;
		}
		DrawingBoard drawingBoard = new DrawingBoard(nameModule);
		currentBoard = drawingBoard;
		moduleTreeViewController.addModule(nameModule);
		//tabPaneController.addTab(nameModule, currentBoard);
		//ZoomableScrollPane scrollPane = new ZoomableScrollPane(currentBoard);
		//		scrollDrawingBoard = scrollPane;
		Tab newTab = null;
		
		newTab = new Tab(nameModule, currentBoard);
		newTab.setClosable(true);
		newTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				currentBoard = drawingBoard;
				//scrollDrawingBoard = scrollPane;
				//edContext = drawingBoard.getContext();
			}
		});
		tabPane.getTabs().add(newTab);
	}

	@FXML
	void handleToolBarRunButton(ActionEvent event) {
		System.out.println("toolBarRunButton was clicked.");
		toolBarRunButton.setText("Running");
	}

	
	@FXML
	private EventHandler<ZoomEvent> onZoomStarted;

    public final EventHandler<? super ZoomEvent> getOnZoomStarted() {
        if (onZoomStarted == null)
			return null;
		else
			return (EventHandler<? super ZoomEvent>) ((WritableObjectValue<Bounds>) onZoomStarted).get();
    }

	@FXML
	void handleToolBarZoomSlider(MouseEvent event) {

		double zoomFactor = toolBarZoomSlider.getValue();
		//scrollDrawingBoard.zoom(zoomFactor);
		currentBoard.zoom(zoomFactor);
		//zoom = zoomFactor;
		System.out.println("Zoom At : "+toolBarZoomSlider.getValue());
	}


	
	

	public void addDragDetection(LibraryDragIcon libraryIcon) {

		// Cursor Display for Drag&Drop
		libraryIcon.setOnMouseEntered(e -> libraryIcon.setCursor(Cursor.OPEN_HAND));
		libraryIcon.setOnMousePressed(e -> libraryIcon.setCursor(Cursor.CLOSED_HAND));
		libraryIcon.setOnMouseReleased(e -> libraryIcon.setCursor(Cursor.DEFAULT));


		// Manage drag
		libraryIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event){
				// set drag event handlers on their respective objects
				libraryPane.setOnDragOver(iconDragOverLibrary);
				currentBoard.setOnDragOver(iconDragOverDrawingBoard);
				//				scrollDrawingBoard.setOnDragOver(iconDragOverScrollDrawingBoard);
				currentBoard.setOnDragDropped(iconDragScrollDrawingBoardDropped);
				//currentBoard.setOnDragDropped(iconDragDropped);

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
				event.consume();
			}
		});
	}	

	private void buildDragHandlers() {

		//drag over transition to move widget form left pane to right pane
		iconDragOverLibrary = new EventHandler <DragEvent>() {

			@Override
			public void handle(DragEvent event) {

				Point2D p = currentBoard.sceneToLocal(event.getSceneX(), event.getSceneY());

				//turn on transfer mode and track in the right-pane's context 
				//if (and only if) the mouse cursor falls within the right pane's bounds.
				if (!currentBoard.boundsInLocalProperty().get().contains(p)) {

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

		iconDragOverScrollDrawingBoard = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {

				event.acceptTransferModes(TransferMode.ANY);
				System.out.println("event at -> "+event.getSceneX()+"/"+event.getSceneY());
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

		iconDragScrollDrawingBoardDropped = new EventHandler <DragEvent> () {
			@Override
			public void handle(DragEvent event) {

				DragContainer container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				container.addData("scene_coords", new Point2D((event.getSceneX()-tabPane.getLayoutX())/zoom, (event.getSceneY()-tabPane.getLayoutY()-32)/zoom));

				ClipboardContent content = new ClipboardContent();
				content.put(DragContainer.AddNode, container);

				event.getDragboard().setContent(content);
				event.setDropCompleted(true);

			}
		};
		/*
		iconDragDropped = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {

				DragContainer container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				container.addData("scene_coords", 
						new Point2D(event.getSceneX(), event.getSceneY()));

				ClipboardContent content = new ClipboardContent();
				content.put(DragContainer.AddNode, container);

				event.getDragboard().setContent(content);
				event.setDropCompleted(true);

			}
		};*/

		root.setOnDragDone (new EventHandler <DragEvent> (){

			@Override
			public void handle (DragEvent event) {
				currentBoard.removeEventHandler(DragEvent.DRAG_OVER, iconDragOverDrawingBoard);
				//				scrollDrawingBoard.removeEventHandler(DragEvent.DRAG_OVER, iconDragOverScrollDrawingBoard);


				libraryPane.removeEventHandler(DragEvent.DRAG_OVER, iconDragOverLibrary);

				currentBoard.removeEventHandler(DragEvent.DRAG_DROPPED, iconDragScrollDrawingBoardDropped);
				//currentBoard.removeEventHandler(DragEvent.DRAG_DROPPED, iconDragDropped);

				componentIcon.setVisible(false);

				//Create node drag operation
				DragContainer container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				if (container != null) {

					if (container.getValue("scene_coords") != null) {
						//creation of new component on the board.
						drawingBoardController.addNewComponent(currentBoard, container);
					}
				}
				/*
				//Move node drag operation
				container = 
						(DragContainer) event.getDragboard().getContent(DragContainer.DragNode);

				if (container != null) {
					if (container.getValue("type") != null)
						System.out.println ("Moved node " + container.getValue("type"));
				}*/


				//AddLink drag operation
				/*
				container =
						(DragContainer) event.getDragboard().getContent(DragContainer.AddLink);

				if (container != null) {

					//bind the ends of our link to the nodes whose id's are stored in the drag container
					String sourceId = container.getValue("source");
					String targetId = container.getValue("target");

					if (sourceId != null && targetId != null) {

						//	System.out.println(container.getData());
						NodeLink link = new NodeLink();

						//add our link at the top of the rendering order so it's rendered first
						drawingBoard.getChildren().add(0,link);

						DraggableNode source = null;
						DraggableNode target = null;

						for (Node n: drawingBoard.getChildren()) {

							if (n.getId() == null)
								continue;

							if (n.getId().equals(sourceId))
								source = (DraggableNode) n;

							if (n.getId().equals(targetId))
								target = (DraggableNode) n;

						}

						if (source != null && target != null)
							link.bindEnds(source, target);
					}

				}*/

				event.consume();
			}
		});		
	}

	//	private static void ensureVisible(ScrollPane pane, Node node) {
	//        double width = pane.getContent().getBoundsInLocal().getWidth();
	//        double height = pane.getContent().getBoundsInLocal().getHeight();
	//
	//        double x = node.getBoundsInParent().getMaxX();
	//        double y = node.getBoundsInParent().getMaxY();
	//
	//        // scrolling values range from 0 to 1
	//        pane.setVvalue(y/height);
	//        pane.setHvalue(x/width);
	//
	//        // just for usability
	//        node.requestFocus();
	//    }

	//	public void componentSelected(Pane componentPane) {
	//		drawingBoardController.setComponentSelected(componentPane);
	//		
	//	}
	//


	private Parent createZoomPane(final DrawingBoard zoomTarget) {

		Group group = new Group(zoomTarget);

		// stackpane for centering the content, in case the ScrollPane viewport
		// is larget than zoomTarget
		StackPane content = new StackPane(group);
		group.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
			// keep it at least as large as the content
			content.setMinWidth(newBounds.getWidth());
			content.setMinHeight(newBounds.getHeight());
		});

		ScrollPane scrollPane = new ScrollPane(content);
		// scrollPane.setPannable(true);
		//zoomTarget.setPrefSize(scrollPane.getWidth(), scrollPane.getHeight());

		scrollPane.viewportBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
			// use vieport size, if not too small for zoomTarget
			content.setPrefSize(newBounds.getWidth(), newBounds.getHeight());
		});

		//        content.setOnScroll(evt -> {
		//            
		//                evt.consume();
		//
		//               // final double zoomFactor = evt.getDeltaY() > 0 ? 1.05: 1 / 1.05;
		//
		//                Bounds groupBounds = group.getLayoutBounds();
		//                final Bounds viewportBounds = scrollPane.getViewportBounds();
		//
		//                // calculate pixel offsets from [0, 1] range
		//                double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
		//                double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());
		//
		//                // convert content coordinates to zoomTarget coordinates
		//                //Point2D posInZoomTarget = zoomTarget.parentToLocal(group.parentToLocal(new Point2D(evt.getX(), evt.getY())));
		//
		//                // calculate adjustment of scroll position (pixels)
		//                //Point2D adjustment = zoomTarget.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));
		//
		//                // do the resizing
		//               // zoomTarget.setScaleX(zoomFactor * zoomTarget.getScaleX());
		//                //zoomTarget.setScaleY(zoomFactor * zoomTarget.getScaleY());
		//
		//                // refresh ScrollPane scroll positions & content bounds
		//                scrollPane.layout();
		//
		//                // convert back to [0, 1] range
		//                // (too large/small values are automatically corrected by ScrollPane)
		//                groupBounds = group.getLayoutBounds();
		//                scrollPane.setHvalue((valX /*+ adjustment.getX()*/) / (groupBounds.getWidth() - viewportBounds.getWidth()));
		//                scrollPane.setVvalue((valY /*+ adjustment.getY()*/) / (groupBounds.getHeight() - viewportBounds.getHeight()));
		//            
		//        });
		return scrollPane;
	}

	private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
		double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
		double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
		double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
		double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
		double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
		double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
		return new Point2D(scrollXOffset, scrollYOffset);
	}

	private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
		double scrollXOffset = scrollOffset.getX();
		double scrollYOffset = scrollOffset.getY();
		double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
		if (extraWidth > 0) {
			double halfWidth = scroller.getViewportBounds().getWidth() / 2 ;
			double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset;
			scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
		} else {
			scroller.setHvalue(scroller.getHmin());
		}
		double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
		if (extraHeight > 0) {
			double halfHeight = scroller.getViewportBounds().getHeight() / 2 ;
			double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
			scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
		} else {
			scroller.setVvalue(scroller.getVmin());
		}
	}
	//	
	//	private SVGPath createStar() {
	//	    SVGPath star = new SVGPath();
	//	    star.setContent("M100,10 L100,10 40,180 190,60 10,60 160,180 z");
	//	    star.setStrokeLineJoin(StrokeLineJoin.ROUND);
	//	    star.setStroke(Color.BLUE);
	//	    star.setFill(Color.DARKBLUE);
	//	    star.setStrokeWidth(4);
	//	    return star;
	//	  }
	//
	public void setCurrentBoard(DrawingBoard currentBoard) {
		this.currentBoard = currentBoard;
		
	}
}
