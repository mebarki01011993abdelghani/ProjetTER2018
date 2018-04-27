package fr.univmrs.lif.model.component;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import fr.univmrs.lif.enumeration.ComponentFamily;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.Orientation;
import fr.univmrs.lif.model.simulation.Simulation;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.Point2DProperty;
import fr.univmrs.lif.tools.AnyTypeAdapter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlJavaTypeAdapter(AnyTypeAdapter.class)
@XmlRootElement
@XmlType( propOrder={ "name", 
		"type", 
		"family",
		"position", 
		"orientation",
		"timeProcessing",
		"inputNumber",
		"outputNumber",
		"inputs",
		"outputs"
} )
public abstract class Component implements Simulation {

	private StringProperty name = new SimpleStringProperty();
	protected ArrayList<Plug> inputs = new ArrayList<Plug>();
	protected ArrayList<Plug> outputs = new ArrayList<Plug>();

	protected IntegerProperty inputNumber = new SimpleIntegerProperty();
	protected IntegerProperty outputNumber = new SimpleIntegerProperty();

	protected ComponentType type;
	protected ComponentFamily family;

	protected Orientation orientation;
	protected Point2DProperty position = new Point2DProperty();

	int timeProcessing = Simulation.DEFAULT_TIME_PROCESSING;
	protected int simulation_state = NOT_SIMULATED;


	public Component(){	}

	/**
	 * Return type of component.
	 */

	public ComponentType getType() {
		return type;
	}

	/**
	 * Set type of component.
	 * @param type
	 */
	public void setType(ComponentType type) {
		this.type = type;
		if(type == ComponentType.NOT       || 
				type == ComponentType.AND  || 
				type == ComponentType.NAND || 
				type == ComponentType.OR   ||
				type == ComponentType.NOR  ||
				type == ComponentType.JOINT||
				type == ComponentType.XNOR ||
				type == ComponentType.XOR)
			setFamily(ComponentFamily.GATE);
		else if(type == ComponentType.SWITCH || 
				type == ComponentType.VDD || 
				type == ComponentType.GND || 
				type == ComponentType.INPUTMODULE ||  
				type == ComponentType.CLOCK )
			setFamily(ComponentFamily.INPUT);
		else if(type == ComponentType.LED ||
				type == ComponentType.OUTPUTMODULE ||
				type == ComponentType.DISPLAY )
			setFamily(ComponentFamily.OUTPUT);
		else if(type == ComponentType.MUX || 
				type == ComponentType.DECODER )
			setFamily(ComponentFamily.MSI);
		else if(type == ComponentType.ADDER)
			setFamily(ComponentFamily.ALU);
		else if(type == ComponentType.MODULE)
			setFamily(ComponentFamily.MODULE);
		else
			setFamily(ComponentFamily.DEFAULT);
	}

	public ComponentFamily getFamily() {
		return family;
	}

	public void setFamily(ComponentFamily family) {
		this.family = family;
	}

	@XmlElement
	@XmlID
	public final String getName() {
		return name.get();
	}

	public final void setName(String name) {
		this.name.set(name);
	}

	public final StringProperty getNameProperty() {
		return name;
	}


	public List<Plug> getPlugs() {
		ArrayList<Plug> plugs = new ArrayList<>(inputs);
		plugs.addAll(outputs);
		return plugs;
	}

	@XmlElementWrapper(name="plugIn")
	@XmlElement(name="plug")
	public List<Plug> getInputs() {

		return inputs;
	}

	@XmlElementWrapper(name="plugOut")
	@XmlElement(name="plug")
	public List<Plug> getOutputs() {

		return outputs;
	}


	public void addInput(Plug plug){
		inputs.add(plug);
		incrementInputNumber();
		computePlugInIndice();
	}

	public void removeInput(Plug plug) {
		inputs.remove(plug);
		decrementInputNumber();
		computePlugInIndice();
	}

	public void addOutput(Plug plug){
		outputs.add(plug);
		incrementOutputNumber();
		computePlugOutIndice();
	}

	public void removeOutput(Plug plug) {
		outputs.remove(plug);
		decrementOutputNumber();
		computePlugOutIndice();
	}

	private void computePlugInIndice(){
		for(Plug plugToShift : inputs){
			plugToShift.setIndice(inputs.indexOf(plugToShift));
		}
	}

	private void computePlugOutIndice(){
		for(Plug plugToShift : outputs){
			plugToShift.setIndice(outputs.indexOf(plugToShift));
		}
	}

	public List<Wire> getAllWires() {
		ArrayList<Wire> wireList = new ArrayList<Wire>();
		for (Plug plug : getPlugs())
			wireList.add(plug.getWire());
		return wireList;
	}

	public void setPosition(Point2DProperty position) {
		this.position = position;
	}


	public Point2DProperty getPosition() {
		return position;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;

	}

	public Orientation getOrientation() {
		return orientation;
	}

	public IntegerProperty getInputNumberProperty() {
		return inputNumber;
	}

	public int getInputNumber(){
		return inputNumber.get();
	}

	public void setInputNumber(int value){
		inputNumber.set(value);
	}

	public IntegerProperty getOutputNumberProperty() {
		return outputNumber;
	}

	public int getOutputNumber(){
		return outputNumber.get();
	}

	public void setOutputNumber(int value){
		outputNumber.set(value);
	}

	public void incrementInputNumber() {
		inputNumber.set(getInputNumber()+1);

	}

	public void incrementOutputNumber() {
		outputNumber.set(getOutputNumber()+1);	
	}

	public void decrementInputNumber() {
		if(inputNumber.get() > 0)
			inputNumber.set(getInputNumber()-1);

	}

	public void decrementOutputNumber() {
		if(outputNumber.get() > 0)
			outputNumber.set(getOutputNumber()-1);	
	}


	public int getTimeProcessing() {
		return timeProcessing;
	}

	public void setTimeProcessing(int timeProcessing) {
		this.timeProcessing = timeProcessing;
	}

	@XmlTransient
	@Override
	public int getSimulationState() {
		return this.simulation_state;
	}

	@Override
	public void setSimulationState(int simulation_state) {
		this.simulation_state = simulation_state;
	}

	@Override
	public String toString() {
		String s = getName() + " : "+ getType().name()+"\n";
		for(Plug plug : getPlugs()){
			s += "   > " + plug.toString()+"\n";
		}
		return s;
	}


}
