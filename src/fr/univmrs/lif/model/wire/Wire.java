package fr.univmrs.lif.model.wire;

import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.tools.NameGenerator;
import fr.univmrs.lif.tools.Point2DProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.Point;


// TODO: Auto-generated Javadoc
/**
 * <b> A wire is used to connect components. </b>
 * <p>
 * A wire is given by a name and list of points describing its drawing in the
 * editor and by
 * <ul>
 * <li>A head if its head is connected to a component</li>
 * <li>A tail if its tail is connected to a component</li>
 * <li>A boolean indicating if the wire is currently selected in the editor
 * </ul>
 * It is always connected to at least one component, by one of its edges: the
 * head or the tail.
 *
 * @author severine
 * @see Component
 * @see Point
 */
@XmlRootElement
@XmlType( propOrder={ "name", 
		"head", 
		"tail",
		"state",
		"points",
		} )
public class Wire {

	/**
	 * The name of the wire.
	 */
	private StringProperty  name = new SimpleStringProperty();

	/**
	 * The component linked to the head of the wire. The head of the wire is its
	 * first Point in the list of points .
	 */

	private Component head = null;

	/**
	 * The component linked to the tail of the wire. The tail of the wire is its
	 * last point in the list of points
	 */
	
	private Component tail = null;

	/**
	 * List of points
	 */
	private ArrayList<Point2DProperty> points = new ArrayList<>();
	
	/**
	 * State of wire
	 */
	private int state = 0;

	
	
	/**
	 * 
	 * Instantiates a new wire by setting his name. The list of points is
	 * created empty
	 * @param name The name of the wire (it has to be unique in a Module)
	 */
	public Wire(String name) {
		this.setName(name);
	}
	
	/**
	 * Instantiates a new wire with auto-generated name. The list of points is
	 * created empty
	 */
	public Wire() {
		this.setName(NameGenerator.generate(ComponentType.WIRE));
	}
	
	/**
	 * Gets the name property of the wire.
	 *
	 * @return the name of the wire
	 */
	public final StringProperty getNameProperty() {
		return name;
	}

	/**
	 * Gets the name of the wire.
	 *
	 * @return the name of the wire
	 */
	@XmlID
	public final String getName() {
		return name.get();
	}

	/**
	 * Sets the name of the wire.
	 *
	 * @param name
	 *            the new name of the wire
	 */
	public final void setName(String name) {
		this.name.set(name);
	}

	
	/**
	 * Gets the component linked to the head of the wire.
	 *
	 * @return the component linked to the head of the wire.
	 */
	@XmlIDREF
	public final Component getHead() {
		return head;
	}

	/**
	 * Sets the component linked to the head of the wire.
	 *
	 * @param head
	 *            the new component linked to the head of the wire
	 */
	public final void setHead(Component head) {
		this.head = head;
	}

	/**
	 * Gets the component linked to the tail of the wire.
	 *
	 * @return the component linked to the tail of the wire.
	 */
	@XmlIDREF
	public final Component getTail() {
		return tail;
	}

	/**
	 * Sets the component linked to the tail of the wire.
	 *
	 * @param tail
	 *            the new component linked to the tail of the wire
	 */
	public final void setTail(Component tail) {
		this.tail = tail;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public void propagate() {
		this.tail.processing();
	}

	@XmlElementWrapper(name="points")
	@XmlElement(name="point")
	public ArrayList<Point2DProperty> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point2DProperty> points) {
		this.points = points;
	}
	
	@Override
	public String toString() {
		if(head == null && tail == null)
			return getName() + ", head = null" + ", tail = null" + ", "+points;
		else if(head == null)
			return getName() + ", head = null" + ", tail = " + tail.getName() + ", "+points;
		else if(tail == null)
			return getName() + ", head = " + head.getName() + ", tail = null" + ", "+points;
		else
		return getName() + ", head = " + head.getName() + ", tail = " + tail.getName() + ", "+points;
	}
	
}
