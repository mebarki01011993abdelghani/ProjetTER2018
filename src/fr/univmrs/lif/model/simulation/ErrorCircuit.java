package fr.univmrs.lif.model.simulation;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.component.Component;

public class ErrorCircuit {
	private ComponentType type;
	private String name;
	private String module;
	private String error;
	
	public ErrorCircuit(Component component) {
		this.setType(component.getType());
		this.setName(component.getName());
	}

	public ComponentType getType() {
		return type;
	}

	public void setType(ComponentType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	public String toString() {
		return this.getType().toString() + " " + this.getName() + " in module " + this.getModule() + ": " + this.getError();
		
	}
}
