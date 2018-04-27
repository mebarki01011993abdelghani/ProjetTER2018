package tkgate.modele;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import tkgate.Settings;

// The three connected parts of the wire are in inputs and outputs is empty. 

public class Joint extends RectangleGate {

	private static int defaultInputsSize = 2;
	private static int defaultOutputsSize = 1;
	private static String gateType = "joint"; // TODO pb d'accès
	private static Dimension dim = Settings.JOINT_DIMENSION;

	public Joint(String name, Point2D.Double pos, Wire wire1, int edge, Wire wire2) {
		this.rect = new Rectangle2D.Double(0,0,dim.getWidth(),dim.getHeight());
		this.setName(name);
		inputs = new ArrayList<Connection>();
		outputs = new ArrayList<Connection>();
		Connection connect1;
		Wire wire3 = wire2.cut(wire1.getName(), pos);
		// System.out.println("wire 2:"+ wire2);
		Point2D.Double p = wire3.getPoint(0);
		setPosition(p);
		if (edge == 0) {
			Point2D.Double dir1 = wire1.headDirection();
			wire1.moveHead(new Point2D.Double(p.x + dim.width / 2 * dir1.x, p.y + dim.width / 2 * dir1.y));
			wire1.setHead(this);
			connect1 = new Connection(wire1, true);
			System.out.println("wire 1: " + wire1);
			System.out.println("dir: " + dir1);
			System.out.println("Point: " + p);
		} else {
			Point2D.Double dir1 = wire1.tailDirection();
			wire1.moveTail(new Point2D.Double(p.x + dim.width / 2 * dir1.x, p.y + dim.width / 2 * dir1.y));
			wire1.setTail(this);
			connect1 = new Connection(wire1, false);
		}
		this.inputs.add(connect1);
		Point2D.Double dir2 = wire2.tailDirection();
		wire2.moveTail(new Point2D.Double(p.x + dim.width / 2 * dir2.x, p.y + dim.width / 2 * dir2.y));
		wire2.setTail(this);
		Connection connect2 = new Connection(wire2, false);
		this.inputs.add(connect2);
		Point2D.Double dir3 = wire3.headDirection();
		wire3.moveHead(new Point2D.Double(p.x + dim.width / 2 * dir3.x, p.y + dim.width / 2 * dir3.y));
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
		gc.fillRect(rect.x, rect.y, dim.width, dim.height);
	
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
		Point2D.Double p0 = connect.getWire().getPoint(index0);
		Point2D.Double p1 = connect.getWire().getPoint(index0 + dir);
		Point2D.Double p = getPosition();
		if (p0.x > p1.x) {// EAST
			p0.setLocation(p.x - dim.width / 2, p.y);
			if (wire.isEdge(index0 + dir)) {
				// ici add Point appelé avec index -1, size = 2 (avec head and
				// tail)
				wire.addPointFrom(index0 + 2, new Point2D.Double(p.x - dim.width - Settings.WIRE_LENGTH, p.y), isHead);
				wire.addPointFrom(index0 + 2, new Point2D.Double(p.x - dim.width - Settings.WIRE_LENGTH, p1.y), isHead);
			} else
				p1.setLocation(p1.x, p.y);
		} else if (p0.x < p1.x) {// WEST
			p0.setLocation(p.x + dim.width / 2, p.y);
			if (wire.isEdge(index0 + dir)) {
				wire.addPointFrom(index0 + 2, new Point2D.Double(p.x + dim.width + Settings.WIRE_LENGTH, p.y), isHead);
				wire.addPointFrom(index0 + 2, new Point2D.Double(p.x + dim.width + Settings.WIRE_LENGTH, p1.y), isHead);
			} else
				p1.setLocation(p1.x, p.y);
		} else if (p0.y > p1.y) {// NORTH
			p0.setLocation(p.x, p.y - dim.width / 2);
			if (wire.isEdge(index0 + dir)) {
				wire.addPointFrom(index0 + 2, new Point2D.Double(p.x, p.y - dim.width - Settings.WIRE_LENGTH), isHead);
				wire.addPointFrom(index0 + 2, new Point2D.Double(p.x, p1.y - dim.width - Settings.WIRE_LENGTH), isHead);
			} else
				p1.setLocation(p.x, p1.y);
		} else if (p0.y < p1.y) {// NORTH
			p0.setLocation(p.x, p.y + dim.width / 2);
			if (wire.isEdge(index0 + dir)) {
				wire.addPointFrom(index0 + 2, new Point2D.Double(p.x, p.y + dim.width + Settings.WIRE_LENGTH), isHead);
				wire.addPointFrom(index0 + 2, new Point2D.Double(p.x, p1.y + dim.width + Settings.WIRE_LENGTH), isHead);
			} else
				p1.setLocation(p.x, p1.y);
		}
	}
}
