package fr.univmrs.lif.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import fr.univmrs.lif.model.component.Module;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlRootElement
@XmlType(propOrder = {"name", "main", "modules"})
public class Project {
	
	private StringProperty name = new SimpleStringProperty();
	private Module main = null;
	private List<Module> modules = new ArrayList<>();
	
	public Project() {
		this.name.set("Untitled");
		main = new Module("Main");
	}
	
	public Project(String name){
		this.name.set(name);
		main = new Module("Main");
//		main.getNameProperty().bind(this.name);
	}
	
	public StringProperty getNameProperty() {
		return name;
	}
	
	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public Module getMain() {
		return main;
	}

	public void setMain(Module main) {
		this.main = main;
	}

	@XmlElementWrapper(name="modules")
	@XmlElement(name="module")
	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
	public void addModule(Module module){
		modules.add(module);
	}
	
	public void removeModule(Module module){
		modules.remove(module);
	}
	
}
