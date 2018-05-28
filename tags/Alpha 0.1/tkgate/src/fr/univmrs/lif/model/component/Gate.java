package fr.univmrs.lif.model.component;

import java.awt.Dimension;
import java.util.ArrayList;

import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.model.wire.Wire;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

/**
 * 		   A Gate is a RectangleGate such that wires are disposed in
 *         the following way: if the orientation is EAST, inputs are all at
 *         WEST, and outputs at EAST. Here are then written methods to set
 *         inputs and outputs.
 * @author severine 
 */
public abstract class Gate extends RectangleGate {

	// ********************************* Constructors
	// ***************************************************

	public Gate(String name, Point2D pos, Dimension dim, int wireNb, int defaultInputsSize, int defaultOutputsSize) {
		this.rect = new Rectangle2D(pos.getX(), pos.getY(), Settings.GATE_WIDTH,Settings.GATE_HEIGHT);
		this.setName(name);

		inputs = new ArrayList<Connection>();
		for (int i = 0; i < defaultInputsSize; i++) {
			Wire wire = new Wire("w" + wireNb);
			wire.setTail(this);
			wireNb++;
			inputs.add(new Connection(wire, false));
		}
		this.setInputWires();

		outputs = new ArrayList<Connection>();
		for (int i = 0; i < defaultOutputsSize; i++) {
			Wire wire = new Wire("w" + wireNb);
			wire.setHead(this);
			wireNb++;
			outputs.add(new Connection(wire, true));
		}
		this.setOutputWires();
	}

	// ***************************** Initialization of wires
	// *********************************************

	private void setInputWires() {
		int inputSize = inputs.size();
		for (int i = 0; i < inputSize; i++) {
			Wire wire = inputs.get(i).getWire();
			setInputWire(wire, (i + 1) * rect.getHeight() / (inputSize + 1));
		}
	}

	private void setInputWire(Wire wire, double shift) {
		Point2D p0 = new Point2D(rect.getMinX() - Settings.WIRE_LENGTH, rect.getMinY() + shift);
		Point2D p1 = new Point2D(rect.getMinX(), rect.getMinY() + shift);
		p0 = rotation(p0, orientation);
		p1 = rotation(p1, orientation);
		wire.addPoint(p0);
		wire.addPoint(p1);
	}

	private void setOutputWires() {
		int outputSize = outputs.size();
		for (int i = 0; i < outputSize; i++) {
			Wire wire = outputs.get(i).getWire();
			setOutputWire(wire, (i + 1) * rect.getHeight() / (outputSize + 1));
		}
	}

	private void setOutputWire(Wire wire, double shift) {
		Point2D p0 = new Point2D(rect.getMinX() + rect.getWidth(), rect.getMinY() + shift);
		Point2D p1 = new Point2D(rect.getMinX() + rect.getWidth() + Settings.WIRE_LENGTH, rect.getMinY() + shift);
		p0 = rotation(p0, orientation);
		p1 = rotation(p1, orientation);
		wire.addPoint(p0);
		wire.addPoint(p1);
	}

	// new position of the delta between p0 and the position of the gate, after
	// a rotation
	// rot * 90°
	private Point2D delta(Point2D p0, int rot) {
		Point2D delta = new Point2D(p0.getX() - getPosition().getX(), p0.getY() - getPosition().getY());
		if (rot % 4 == 0)
			return delta;
		if (rot % 4 == 1)
			return new Point2D(-delta.getY(), delta.getX());
		if (rot % 4 == 2)
			return new Point2D(-delta.getX(), -delta.getY());
		if (rot % 4 == 3)
			return new Point2D(delta.getY(), -delta.getX());
		return null;
	}

	// New position of p0 after a rotation from the position of this
	private Point2D rotation(Point2D p0, int rot) {
		Point2D delta = delta(p0, rot);
		return new Point2D(getPosition().getX() + delta.getX(), getPosition().getY() + delta.getY());
	}

	// ***************************** Add additional wires
	// *********************************

	void addInputWire(String name) {
		// TODO il faut tester dans le controleur qu'on a le droit de l'ajouter
		Wire wire = new Wire(name);
		wire.setTail(this);
		inputs.add(new Connection(wire, false));
		this.setInputWires();
	}

	void addOutputWire(String name) {
		// TODO il faut tester dans le controleur qu'on a le droit de l'ajouter
		Wire wire = new Wire(name);
		wire.setHead(this);
		outputs.add(new Connection(wire, true));
		this.setOutputWires();
	}


}