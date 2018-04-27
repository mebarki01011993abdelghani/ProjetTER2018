package fr.univmrs.lif.controller.appBar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.controller.bottomBar.Console;
import fr.univmrs.lif.model.Project;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Joint;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.component.alu.Adder;
import fr.univmrs.lif.model.component.gate.AndGate;
import fr.univmrs.lif.model.component.gate.XnorGate;
import fr.univmrs.lif.model.component.gate.NandGate;
import fr.univmrs.lif.model.component.gate.NorGate;
import fr.univmrs.lif.model.component.gate.NotGate;
import fr.univmrs.lif.model.component.gate.OrGate;
import fr.univmrs.lif.model.component.gate.XorGate;
import fr.univmrs.lif.model.component.inputoutput.Clock;
import fr.univmrs.lif.model.component.inputoutput.Display;
import fr.univmrs.lif.model.component.inputoutput.GND;
import fr.univmrs.lif.model.component.inputoutput.InputModule;
import fr.univmrs.lif.model.component.inputoutput.Led;
import fr.univmrs.lif.model.component.inputoutput.OutputModule;
import fr.univmrs.lif.model.component.inputoutput.Switch;
import fr.univmrs.lif.model.component.inputoutput.VDD;
import fr.univmrs.lif.model.component.msi.Decoder;
import fr.univmrs.lif.model.component.msi.Multiplexer;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.Point2DProperty;
import fr.univmrs.lif.view.popup.ConfirmBox;
import fr.univmrs.lif.view.popup.PopupType;
import fr.univmrs.lif.view.popup.TextBox;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

public class MenuBarController implements Initializable {


	private MainController mainController;

	@FXML private MenuBar menu;

	@FXML private MenuItem menuBarFileNewProject;

	@FXML private MenuItem menuBarFileOpen;

	@FXML private MenuItem menuBarFileSave;

	@FXML private MenuItem menuBarFileSaveAs;

	@FXML private MenuItem menuBarFileQuit;

	@FXML private MenuItem menuBarEditUndo;

	@FXML private MenuItem menuBarEditRedo;

	@FXML private MenuItem menuBarEditDuplicate;

	@FXML private MenuItem menuBarEditDelete;

	@FXML private MenuItem menuBarEditSelectAll;

	@FXML private MenuItem menuBarProjectRename;

	@FXML private MenuItem menuBarProjectProperties;

	@FXML private MenuItem menuBarModuleNew;

	@FXML private MenuItem menuBarModuleRename;

	@FXML private MenuItem menuBarModuleDelete;

	@FXML private MenuItem menuBarModulePrint;

	@FXML private MenuItem menuBarHelpAbout;

	public void injectMainController(MainController mainController){
		this.mainController = mainController;

	}

	@FXML
	void handleMenuBarFileNewProject(ActionEvent event) {

		String nameProject = null;
		TextInputDialog dialog = new TextInputDialog("Untitled");
		dialog.setTitle("Project");
		dialog.setHeaderText("Project name");
		dialog.setContentText("Please enter name project:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			nameProject = result.get();
		}

		if(nameProject == null){
			return;
		}

		mainController.createProject(nameProject);

	}

	@FXML
	void handleMenuBarFileOpen(ActionEvent event) {
		final FileChooser dialog = new FileChooser(); 
		final File file = dialog.showOpenDialog(null); 
		if (file != null) { 
			try {

				JAXBContext context = JAXBContext.newInstance(
						Project.class,
						Module.class,
						Wire.class,
						Plug.class,
						Joint.class,
						Adder.class,
						AndGate.class,
						NandGate.class,
						NorGate.class,
						NotGate.class,
						OrGate.class,
						XorGate.class,
						XnorGate.class,
						Clock.class,
						Display.class,
						GND.class,
						InputModule.class,
						Led.class,
						OutputModule.class,
						Switch.class,
						VDD.class,
						Decoder.class,
						Multiplexer.class
						);
				javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();

				mainController.loadProject((Project) unmarshaller.unmarshal (file));

				menuBarFileSave.setDisable(false);
				//TODO Validation
				//Validator validator = jaxbContext.createValidator();
				Console.println("File load : " + file.getName());
			} catch (Exception e) {e.printStackTrace();}

		}
	}

	/**
	 * Quit application
	 * @param event
	 */
	// TODO check undo stack for changes and ask save
	@FXML
	void handleMenuBarFileQuit(ActionEvent event) {

		boolean haveChanges = true;

		if(haveChanges){

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Quit ?");
			alert.setHeaderText("There are some changes on project");
			alert.setContentText("Save changes ?");

			ButtonType save = new ButtonType("Save");
			ButtonType quit = new ButtonType("Quit");
			ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

			alert.getButtonTypes().setAll(save, quit, cancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == save){
				if(MainController.getCurrentSave() == null){
					handleMenuBarFileSaveAs(null);
				}else{
					handleMenuBarFileSave(null);
				}
			} else if (result.get() == quit) {
				Platform.exit();
				System.exit(0);

			}
		}
	}

	@FXML
	void handleMenuBarFileSave(ActionEvent event) {
		try {
			JAXBContext context = JAXBContext.newInstance(
					Project.class,
					Module.class,
					Wire.class,
					Plug.class,
					Joint.class,
					Adder.class,
					AndGate.class,
					NandGate.class,
					NorGate.class,
					NotGate.class,
					OrGate.class,
					XorGate.class,
					XnorGate.class,
					Clock.class,
					Display.class,
					GND.class,
					InputModule.class,
					Led.class,
					OutputModule.class,
					Switch.class,
					VDD.class,
					Decoder.class,
					Multiplexer.class
					);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(MainController.getCurrentProject(), MainController.getCurrentSave());
			Console.println("File Save : " + MainController.getCurrentSave());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void handleMenuBarFileSaveAs(ActionEvent event) {
		Project currentProject = MainController.getCurrentProject();
		final FileChooser dialog = new FileChooser();
		dialog.setInitialFileName(currentProject.getName()+".xml");
		final File file = dialog.showSaveDialog(null);
		if (file != null) { 
			try {

				JAXBContext context = JAXBContext.newInstance(
						Project.class,
						Module.class,
						Wire.class,
						Plug.class,
						Joint.class,
						Adder.class,
						AndGate.class,
						NandGate.class,
						NorGate.class,
						NotGate.class,
						OrGate.class,
						XorGate.class,
						XnorGate.class,
						Clock.class,
						Display.class,
						GND.class,
						InputModule.class,
						Led.class,
						OutputModule.class,
						Switch.class,
						VDD.class,
						Decoder.class,
						Multiplexer.class
						);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				m.marshal(currentProject, file);

				MainController.setCurrentSave(file);
				menuBarFileSave.setDisable(false);
				//Validator validator = jaxbContext.createValidator();
				Console.println("File Save : " + file.getName());
			} catch (Exception e) {e.printStackTrace();}

		}
	}

	@FXML
	void handleMenuBarEditUndo(ActionEvent event){
	}

	@FXML
	void handleMenuBarModuleNew(ActionEvent event) {
		String nameModule = TextBox.display(PopupType.MODULE);
		if(nameModule == null){
			return;
		}

		mainController.createModule(nameModule);

	}

	@FXML
	void handleMenuBarModulePrint(ActionEvent event) {


		WritableImage image = MainController.getCurrentBoard().snapshot(new SnapshotParameters(), null);

		final FileChooser dialog = new FileChooser();
		dialog.setInitialFileName(MainController.getCurrentProject().getName() + " - "+ MainController.getCurrentBoard().getModule().getName()+".png");
		final File file = dialog.showSaveDialog(null);
		if (file != null) { 
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			} catch (IOException e) {
				Console.errorln("Error can't print module.");
			}
		}
	}

	@FXML
	void handleMenuBarEditDelete(ActionEvent event) {

	}

	@FXML
	void handleMenuBarEditDuplicate(ActionEvent event) {

	}

	@FXML
	void handleMenuBarEditRedo(ActionEvent event) {

	}

	@FXML
	void handleMenuBarEditSelectAll(ActionEvent event) {

	}

	@FXML
	void handleMenuBarHelpAbout(ActionEvent event) {

	}

	@FXML
	void handleMenuBarModuleDelete(ActionEvent event) {

	}



	@FXML
	void handleMenuBarModuleRename(ActionEvent event) {
		if(MainController.getCurrentBoard().getModule() != 
				MainController.getCurrentProject().getMain()){
			String nameModule = TextBox.display(PopupType.MODULE);
			if(nameModule == null){
				return;
			}

			MainController.getCurrentBoard().getModule().setName(nameModule);
		}
		else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("Can't rename module...");
			alert.setContentText("Impossible to rename Main module.");

			alert.showAndWait();
		}
	}

	@FXML
	void handleMenuBarProjectProperties(ActionEvent event) {

	}

	@FXML
	void handleMenuBarProjectRename(ActionEvent event) {
		
			String nameProject = TextBox.display(PopupType.PROJECT);
			if(nameProject == null){
				return;
			}

			MainController.getCurrentProject().setName(nameProject);
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	public void setDisable(boolean disable) {
		menu.setDisable(disable);
	}

}