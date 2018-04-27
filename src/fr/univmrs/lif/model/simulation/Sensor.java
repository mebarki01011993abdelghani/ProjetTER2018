package fr.univmrs.lif.model.simulation;


import fr.univmrs.lif.controller.bottomBar.ChronogramController;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.NameGenerator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Sensor {
	

//	private ComponentType type = ComponentType.SENSOR;
	
	
	private Wire wire;
	
	
	public Sensor(Wire wire) {
	
		this.wire = wire;
	}

	public int getSensor_state() {
		return wire.getState();
	}
	
	public Wire getWire() {
		return wire;
	}

	public String getName() {
		return wire.getName();
	}	
}
