package fr.univmrs.lif.model.component;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.Orientation;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.NameGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

@XmlRootElement
@XmlType(name="module2")
public class Module extends Component{
	
	private List<Component> components= new ArrayList<Component>();
	private List<Wire> wires = new ArrayList<Wire>();
	
	public Module(){
		setType(ComponentType.MODULE);
		this.setName(NameGenerator.generate(getType()));
	}

	public Module(String name) {
		setType(ComponentType.MODULE);
		this.setName(name);
	}
	
//	@XmlTransient
	@XmlElementWrapper(name="components")
	@XmlElement(name="component")
	public List<Component> getComponents() {
		return components;
	}
	
	public void addComponent(Component component) {
		this.components.add(component);
	}
	
	public void removeComponent(Component component) {
		this.components.remove(component);
	}

	@Override
	public void processing() {
		for(int i = 0; i < this.getComponents().size(); i++) {
			if(this.getComponents().get(i).getType() == ComponentType.INPUTMODULE)
				this.getComponents().get(i).processing();
		}
	}

	@XmlElementWrapper(name="wires")
	@XmlElement(name="wire")
	public List<Wire> getWires() {
		return wires;
	}

	public void addWire(Wire wire) {
		this.wires.add(wire);
	}
	
	public void removeWire(Wire wire) {
		this.wires.remove(wire);
	}
	
	@Override
	public List<Wire> getAllWires() {
		ArrayList<Wire> wireList = new ArrayList<Wire>();
		for(Component c : getComponents())
			for (Plug plug : c.getPlugs()){
				if(plug.getWire() != null && !wireList.contains(plug.getWire()))
					wireList.add(plug.getWire());
			}
				
		return wireList;
	}

	@Override
	public int getSimulationState() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSimulationState(int simulation_state) {
		// TODO Auto-generated method stub
		
	}
	
	public Module copie(){
		Module copie = new Module(getName());
		copie.setComponents(getComponents());
		copie.setWires(getWires());
		return copie;
		
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public void setWires(List<Wire> wires) {
		this.wires = wires;
	}
}
