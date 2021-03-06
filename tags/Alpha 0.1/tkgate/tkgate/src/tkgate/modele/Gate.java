package tkgate.modele;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import tkgate.Settings;



/**
 * @author severine A Gate is a RectangleGate such that wires are disposed in
 *         the following way: if the orientation is EAST, inputs are all at
 *         WEST, and outputs at EAST. Here are then written methods to set
 *         inputs and outputs.
 */
public abstract class Gate extends RectangleGate {

	// ********************************* Constructors
	// ***************************************************

	public Gate(String name, Point2D.Double pos, Dimension dim, int wireNb, int defaultInputsSize, int defaultOutputsSize) {
		this.rect = new Rectangle2D.Double(pos.x, pos.y, Settings.GATE_WIDTH,Settings.GATE_HEIGHT);
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
			setInputWire(wire, (i + 1) * rect.height / (inputSize + 1));
		}
	}

	private void setInputWire(Wire wire, double shift) {
		Point2D.Double p0 = new Point2D.Double(rect.x - Settings.WIRE_LENGTH, rect.y + shift);
		Point2D.Double p1 = new Point2D.Double(rect.x, rect.y + shift);
		p0 = rotation(p0, orientation);
		p1 = rotation(p1, orientation);
		wire.addPoint(p0);
		wire.addPoint(p1);
	}

	private void setOutputWires() {
		int outputSize = outputs.size();
		for (int i = 0; i < outputSize; i++) {
			Wire wire = outputs.get(i).getWire();
			setOutputWire(wire, (i + 1) * rect.height / (outputSize + 1));
		}
	}

	private void setOutputWire(Wire wire, double shift) {
		Point2D.Double p0 = new Point2D.Double(rect.x + rect.width, rect.y + shift);
		Point2D.Double p1 = new Point2D.Double(rect.x + rect.width + Settings.WIRE_LENGTH, rect.y + shift);
		p0 = rotation(p0, orientation);
		p1 = rotation(p1, orientation);
		wire.addPoint(p0);
		wire.addPoint(p1);
	}

	// new position of the delta between p0 and the position of the gate, after
	// a rotation
	// rot * 90°
	private Point2D.Double delta(Point2D.Double p0, int rot) {
		Point2D.Double delta = new Point2D.Double(p0.x - getPosition().x, p0.y - getPosition().y);
		if (rot % 4 == 0)
			return delta;
		if (rot % 4 == 1)
			return new Point2D.Double(-delta.y, delta.x);
		if (rot % 4 == 2)
			return new Point2D.Double(-delta.x, -delta.y);
		if (rot % 4 == 3)
			return new Point2D.Double(delta.y, -delta.x);
		return null;
	}

	// New position of p0 after a rotation from the position of this
	private Point2D.Double rotation(Point2D.Double p0, int rot) {
		Point2D.Double delta = delta(p0, rot);
		return new Point2D.Double(getPosition().x + delta.x, getPosition().y + delta.y);
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

// // ************************** Reset wires
// // ******************************************
// // ******************** TODO REECRIRE ET VERIFIER TAIL/HEAD
// // ****************************************
//
// @Override
// public void resetWires() {
// resetInputs();
// resetOutputs();
// }
//
// private void resetInputs() {
// int inputSize = inputs.size();
// for (int i = 0; i < inputSize; i++) {
// Wire wire = inputs.get(i).getWire();
// resetInputWire(wire, (i + 1) * rect.height / (inputSize + 1));
// }
// }
//
// private void resetInputWire(Wire wire, int shift) {
// int s = wire.getSize() - 1;
// Point p0 = wire.getPoint(s - 1);
// Point p1 = wire.getPoint(s);
//
// if (orientation == Gate.EAST_ORIENTATION) {
// if (p0.y == p1.y && p0.x <= p1.x && p1.x < p0.x + Settings.WIRE_LENGTH) {
// p0.x = p1.x - Settings.WIRE_LENGTH;
// if (wire.getSize() > 2) {
// Point p2 = wire.getPoint(2);
// p2.x = p1.x - Settings.WIRE_LENGTH;
// }
// } else if (p0.x == p1.x) {
// wire.getPoints().add(s - 1, new Point(p1.x - Settings.WIRE_LENGTH, p1.y));
// p0.x = p1.x - Settings.WIRE_LENGTH;
// }
//
// // TODO Others orientations
//
// } else if (orientation == Gate.SOUTH_ORIENTATION) {
// p0 = new Point(rect.x + shift, rect.y - Settings.WIRE_LENGTH);
// p1 = new Point(rect.x + shift, rect.y);
// } else if (orientation == Gate.WEST_ORIENTATION) {
// p0 = new Point(rect.x + rect.width + Settings.WIRE_LENGTH, rect.y + shift);
// p1 = new Point(rect.x + rect.width, rect.y + shift);
// } else if (orientation == Gate.NORTH_ORIENTATION) {
// p0 = new Point(rect.x + shift, rect.y + rect.height);
// p1 = new Point(rect.x + shift, rect.y + rect.height + Settings.WIRE_LENGTH);
// }
// }
//
// private void resetOutputs() {
// int outputSize = outputs.size();
// for (int i = 0; i < outputSize; i++) {
// Wire wire = outputs.get(i).getWire();
// resetOutputWire(wire, (i + 1) * rect.height / (outputSize + 1));
// }
// }
//
// // TODO rendre implementation proof (par rapport à wire)
//
// private void resetOutputWire(Wire wire, int shift) {
// Point p0 = wire.getPoint(0);
// Point p1 = wire.getPoint(1);
// // if (wire.getPoints().size() == 2){
// if (orientation == Gate.EAST_ORIENTATION) {
// if (p0.y == p1.y && p0.x <= p1.x && p1.x < p0.x + Settings.WIRE_LENGTH) {
// System.out.println("c1");
// p1.x = p0.x + Settings.WIRE_LENGTH;
// if (wire.getSize() > 2) {
// Point p2 = wire.getPoint(2);
// p2.x = p0.x + Settings.WIRE_LENGTH;
// }
// } else if (p0.x == p1.x) {
// System.out.println("c2");
// wire.getPoints().add(1, new Point(p0.x + Settings.WIRE_LENGTH, p0.y));
// p1.x = p0.x + Settings.WIRE_LENGTH;
// }
// }
// // TODO autres orientations
// else if (orientation == Gate.SOUTH_ORIENTATION) {
// p0 = new Point(rect.x + shift, rect.y + rect.height);
// p1 = new Point(rect.x + shift, rect.y + rect.height + Settings.WIRE_LENGTH);
// } else if (orientation == Gate.WEST_ORIENTATION) {
// p0 = new Point(rect.x, rect.y + shift);
// p1 = new Point(rect.x - Settings.WIRE_LENGTH, rect.y + shift);
// } else if (orientation == Gate.NORTH_ORIENTATION) {
// p0 = new Point(rect.x + shift, rect.y - Settings.WIRE_LENGTH);
// p1 = new Point(rect.x + shift, rect.y);
// }
// }
//
// }