package fr.univmrs.lif.model.component;

import java.io.IOException;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.NameGenerator;
import fr.univmrs.lif.tools.Point2DProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

@XmlRootElement
@XmlType( propOrder={ "name", 
		"type", 
		"head", 
		"tail",
		"component",
		"wire",
		"connected",
		"indice"
		} )
public class Plug {

	private BooleanProperty connected = new SimpleBooleanProperty(false);
	private StringProperty name = new SimpleStringProperty();
	Wire wire;
	Component component;
	PlugType type;
	private Point2DProperty head = new Point2DProperty();
	private Point2DProperty tail = new Point2DProperty();
	private IntegerProperty indice = new SimpleIntegerProperty();

	public Plug(){}

	public Plug(Component component,PlugType type){
		setName(NameGenerator.generate(ComponentType.PLUG));
		this.component = component;
		this.type = type;
		if(type == PlugType.IN)
			indice.set(component.getInputNumber());
		else if(type == PlugType.OUT)
			indice.set(component.getOutputNumber());
	}


	public void setType(PlugType type) {
		this.type = type;
	}

	public PlugType getType() {
		return type;
	}

	// TODO
	public Component oppositeComponent() {
		if (type == PlugType.IN)
			return wire.getTail();
		else
			return wire.getHead();
	}

	public void setComponent(Component component){
		this.component = component;
	}

	@XmlIDREF
	public Component getComponent() {
		return component;
	}


	@XmlIDREF
	public Wire getWire() {
		return wire;
	}

	public void setWire(Wire wire){
		if(wire == null){
			setConnected(false);
		}else{
			setConnected(true);
		}
		this.wire = wire;
	}

	public Point2DProperty getHead() {
		return head;
	}

	public void setHead(Point2DProperty head) {
		this.head = head;
	}

	public Point2DProperty getTail() {
		return tail;
	}

	public void setTail(Point2DProperty tail) {
		this.tail = tail;
	}
	
	public StringProperty getNameProperty() {
		return name;
	}

	public String getName(){
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}
	
	@Override
	public String toString() {
		if(wire != null)
			return getName()+" type = "+ getType().name() +", " + head + ":" + tail + ", wire : " + wire.getName();
		else
			return getName()+" type = "+ getType().name() +", " + head + ":" + tail;
	}

	public final IntegerProperty indiceProperty() {
		return this.indice;
	}
	

	public final int getIndice() {
		return this.indiceProperty().get();
	}
	

	public final void setIndice(final int indice) {
		this.indiceProperty().set(indice);
	}

	public final BooleanProperty connectedProperty() {
		return this.connected;
	}
	

	public final boolean isConnected() {
		return this.connectedProperty().get();
	}
	

	public final void setConnected(final boolean connected) {
		this.connectedProperty().set(connected);
	}
	
	




}
