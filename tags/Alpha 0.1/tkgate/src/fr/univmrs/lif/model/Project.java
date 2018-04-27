package fr.univmrs.lif.model;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.model.component.Module;

@XmlRootElement
public class Project {
	
	private String name;
	private Module main = null;
	
	public Project() {
		this.name = "Unnamed";
		main = new Module("main");
	}
	
	public Project(String name){
		this.name = name;
		main = new Module("main");
	}
	
	public Project(String name, Module main){
		this.name = name;
		this.main = main;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Module getMain() {
		return main;
	}

	public void setMain(Module main) {
		this.main = main;
	}

	

}
