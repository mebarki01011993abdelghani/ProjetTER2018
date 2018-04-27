package fr.univmrs.lif.model.component;

import java.awt.Dimension;
import java.util.ArrayList;

import fr.univmrs.lif.model.Settings;
import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.ComponentType;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

// The three connected parts of the wire are in inputs and outputs is empty. 

public class Joint extends RectangleGate{

	private static int defaultInputsSize = 2;
	private static int defaultOutputsSize = 1;
	private static ComponentType gateType = ComponentType.JOINT; // TODO pb d'accès
	private static Dimension dim = Settings.JOINT_DIMENSION;

	public Joint(String name, Point2D pos, Wire wire1, int edge, Wire wire2) {
		this.rect = new Rectangle2D(0,0,dim.getWidth(),dim.getHeight());
		this.setName(name);
		inputs = new ArrayList<Connection>();
		outputs = new ArrayList<Connection>();
		Connection connect1;
		Wire wire3 = wire2.cut(wire1.getName(), pos);
		// System.out.println("wire 2:"+ wire2);
		Point2D p = wire3.getPoint(0);
		setPosition(p);
		if (edge == 0) {
			Point2D dir1 = wire1.headDirection();
			wire1.moveHead(new Point2D(p.getX() + dim.width / 2 * dir1.getX(), p.getY() + dim.width / 2 * dir1.getY()));
			wire1.setHead(this);
			connect1 = new Connection(wire1, true);
			System.out.println("wire 1: " + wire1);
			System.out.println("dir: " + dir1);
			System.out.println("Point: " + p);
		} else {
			Point2D dir1 = wire1.tailDirection();
			wire1.moveTail(new Point2D(p.getX() + dim.width / 2 * dir1.getX(), p.getY() + dim.width / 2 * dir1.getY()));
			wire1.setTail(this);
			connect1 = new Connection(wire1, false);
		}
		this.inputs.add(connect1);
		Point2D dir2 = wire2.tailDirection();
		wire2.moveTail(new Point2D(p.getX() + dim.width / 2 * dir2.getX(), p.getY() + dim.width / 2 * dir2.getY()));
		wire2.setTail(this);
		Connection connect2 = new Connection(wire2, false);
		this.inputs.add(connect2);
		Point2D dir3 = wire3.headDirection();
		wire3.moveHead(new Point2D(p.getX() + dim.width / 2 * dir3.getX(), p.getY() + dim.width / 2 * dir3.getY()));
		wire3.setHead(this);
		Connection connect3 = new Connection(wire3, true);
		this.inputs.add(connect3);
		System.out
				.println("joint: position " + getPosition() + "[" + inputs.toString() + "," + outputs.toString() + "]");
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		super.draw(gc);
		gc.setStroke(Settings.GATE_COLOR);
		gc.fillRect(rect.getWidth(), rect.getHeight(), dim.width, dim.height);
	
	}

	@Override
	public int getDefaultInputsSize() {
		return defaultInputsSize;
	}

	@Override
	public int getDefaultOutputsSize() {
		return defaultOutputsSize;
	}

	@Override
	public String toString() {
		return "joint: position " + getPosition() + "[" + inputs.toString() + "," + outputs.toString() + "]";
	}

	@Override
	public void refactorConnections() {
		for (Connection connect : inputs) {
			// refactorConnection(connect);
			connect.getWire().removeU();
		}
		for (Connection connect : outputs) {
			// refactorConnection(connect);
			connect.getWire().removeU();
		}
	}

	private void refactorConnection(Connection connect) {
		int index0 = 0;
		int dir = 1;
		Boolean isHead = true;
		Wire wire = connect.getWire();
		if (!connect.isHead()) {
			index0 = connect.getWire().getSize() - 1;
			dir = -1;
			isHead = false;
		}
		Point2D p0 = connect.getWire().getPoint(index0);
		Point2D p1 = connect.getWire().getPoint(index0 + dir);
		Point2D p = getPosition();
		if (p0.getX() > p1.getX()) {// EAST
			p0 = new Point2D(p.getX() - dim.width / 2, p.getY());
			if (wire.isEdge(index0 + dir)) {
				// ici add Point appelé avec index -1, size = 2 (avec head and
				// tail)
				wire.addPointFrom(index0 + 2, new Point2D(p.getX() - dim.width - Settings.WIRE_LENGTH, p.getY()), isHead);
				wire.addPointFrom(index0 + 2, new Point2D(p.getX() - dim.width - Settings.WIRE_LENGTH, p1.getY()), isHead);
			} else
				p1 = new Point2D(p1.getX(), p.getY());
		} else if (p0.getX() < p1.getX()) {// WEST
			p0= new Point2D(p.getX() + dim.width / 2, p.getY());
			if (wire.isEdge(index0 + dir)) {
				wire.addPointFrom(index0 + 2, new Point2D(p.getX() + dim.width + Settings.WIRE_LENGTH, p.getY()), isHead);
				wire.addPointFrom(index0 + 2, new Point2D(p.getX() + dim.width + Settings.WIRE_LENGTH, p1.getY()), isHead);
			} else
				p1= new Point2D(p1.getX(), p.getY());
		} else if (p0.getY() > p1.getY()) {// NORTH
			p0= new Point2D(p.getX(), p.getY() - dim.width / 2);
			if (wire.isEdge(index0 + dir)) {
				wire.addPointFrom(index0 + 2, new Point2D(p.getX(), p.getY() - dim.width - Settings.WIRE_LENGTH), isHead);
				wire.addPointFrom(index0 + 2, new Point2D(p.getX(), p1.getY() - dim.width - Settings.WIRE_LENGTH), isHead);
			} else
				p1= new Point2D(p.getX(), p1.getY());
		} else if (p0.getY() < p1.getY()) {// NORTH
			p0= new Point2D(p.getX(), p.getY() + dim.width / 2);
			if (wire.isEdge(index0 + dir)) {
				wire.addPointFrom(index0 + 2, new Point2D(p.getX(), p.getY() + dim.width + Settings.WIRE_LENGTH), isHead);
				wire.addPointFrom(index0 + 2, new Point2D(p.getX(), p1.getY() + dim.width + Settings.WIRE_LENGTH), isHead);
			} else
				p1= new Point2D(p.getX(), p1.getY());
		}
	}

	@Override
	public void processing() {
		// TODO Auto-generated method stub
		
	}
}
