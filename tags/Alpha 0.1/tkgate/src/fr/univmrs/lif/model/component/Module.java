package fr.univmrs.lif.model.component;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.model.Notification;
import fr.univmrs.lif.model.Observable;
import fr.univmrs.lif.model.Observer;
import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.tools.ComponentType;
import fr.univmrs.lif.tools.NameGenerator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

/**
 * @author severine
 *
 */

public class Module implements Observable {

	
	private StringProperty name = new SimpleStringProperty();
	
	private int freeWire = 1; // number free to numbering wires TODO depend on
	// the project!
	private int freeGate = 1; // number free to numbering gates TODO depend on
							// the project!
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	
	private ObservableList<Component> componentList;
	
	public Module(){
		this.setName("N/A");
	}

	public Module(String moduleName) {
		this.setName(moduleName);
		componentList = FXCollections.observableArrayList();
	}

	
	public final String getName() {
		return name.get();
	}

	public final void setName(String name) {
		this.name.set(name);
	}
	
	public final StringProperty nameProperty() {return name;}
	
	public ObservableList<Component> getComponentList() {
		return componentList;
	}

	ArrayList<Wire> getWires() {
		ArrayList<Wire> list = new ArrayList<Wire>();
		for (Component c : componentList) {
			list.addAll(c.allWires());
		}
		return list;
	}


	public void addComponent(ComponentType type, Point2D location) {
		location = new Point2D(location.getX()-Settings.GATE_DIMENSION.getWidth()/2, location.getY()-Settings.GATE_DIMENSION.getHeight()/2);
		Component c = ComponentFactory.buildComponent(type, NameGenerator.generate(type), location, Settings.GATE_DIMENSION,
				freeWire);
	
		freeGate++;
		freeWire = freeWire + c.getDefaultInputsSize() + c.getDefaultOutputsSize();
		componentList.add(c);
		//notifyObserver(new Notification(Notification.REFRESH_PANEL));
	}

	public void addComponent(Component c) {
		componentList.add(c);
	}

	@Override
	public String toString() {
		String result = "module " + getName() + ";\n";

		ArrayList<Wire> wires = getWires();
		int size = wires.size();
		for (int i = 0; i < size; i++)
			result += wires.get(i).toString();

		result += "//: enddecls\n\n";

		size = componentList.size();
		for (int i = 0; i < size; i++)
			result += componentList.get(i).toString();

		result += "\nendmodule\n";

		return result;
	}

	/////////////////////////////////////////////////////////////////////////////////
	// Observers //
	/////////////////////////////////////////////////////////////////////////////////

	public void addObserver(Observer obs) {
		this.listObserver.add(obs);
	}

	@Override
	public void notifyObserver(Notification notif) {
		for (Observer obs : listObserver) {
			obs.update(notif);
		}
	}

	///////////////////////   naming of module and wires
	
	
	/*
	 * get a fresh name for a new wire
	 */
	public String getFreshWireName() {
		String name = "w" + freeWire;
		freeWire++;
		return name;
	}

	/*
	 * get a fresh name for a new wire
	 */
	public String getFreshGateName() {
		String name = "g" + freeGate;
		freeGate++;
		return name;
	}

	// ***************************** WIRE ACCESSES
	// ******************************************

	public void setSelected(Wire wire, boolean b) {
		wire.setSelected(b);
		notifyObserver(new Notification(Notification.REFRESH_PANEL));
	}



	public void moveWireHead(Wire wire, Point2D point) {
		wire.moveHead(point);
		notifyObserver(new Notification(Notification.REFRESH_PANEL));

	}

	public void moveWireTail(Wire wire, Point2D point) {
		wire.moveTail(point);
		notifyObserver(new Notification(Notification.REFRESH_PANEL));

	}

	public int moveWireSegment(Wire wire, Point2D point, int selSegment) {
		return wire.moveSegment(point, selSegment);

	}

	public int getSelSegment(Wire wire, Point2D point) {
		return wire.selectSegment(point);
	}

	public int moveWirePoint(Wire wire, Point2D point, int selPoint) {
		//TODO movePoint n'est pas implémenté 
		return wire.movePoint(selPoint, point);
	}

	// TODO commenté provisoirement il faut reprendre les methodes de wire
	// // moves the selected segment of wire to point
	// public void moveSelectedSegment(Wire wire, Point point) {
	// wire.setSelected(point);
	// wire.moveSelected(point);
	// notifyObserver(new Notification(Notification.REFRESH_PANEL));
	// // TODO refresh component rather than the whole panel?
	//
	// }

	// public void addWire(Wire wire) {
	// wireList.add(wire);
	// }

}
