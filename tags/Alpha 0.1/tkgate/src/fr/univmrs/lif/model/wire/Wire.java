package fr.univmrs.lif.model.wire;


import java.util.ArrayList;

import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Connection;
import fr.univmrs.lif.model.Point;
import fr.univmrs.lif.model.Segment;
import fr.univmrs.lif.model.Settings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;


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
	 * The flag stating if the wire is selected. (In general, selected by the
	 * user in the editor)
	 */
	private BooleanProperty selected = new SimpleBooleanProperty(false);

	/**
	 * The list of points corresponding to the drawing of the wire in the editor
	 */
	private ObservableList<Point2D> points;
	
	private int state = 0;

	// ********************** Constructors
	// ******************************************

	/**
	 * 
	 * <p>
	 * Instantiates a new wire by setting his name. The list of points is
	 * created empty
	 * </p>
	 * 
	 * @param name
	 *            The name of the wire (it has to be unique in a Module)
	 * 
	 * 
	 *
	 * 
	 */
	public Wire(String name) {
		this.setName(name);
		points = FXCollections.observableArrayList();;
	}

	// *********************** Getters and Setters
	// *********************************

	/**
	 * Gets the name of the wire.
	 *
	 * @return the name of the wire
	 */
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
	 * Gets the name property of the wire.
	 *
	 * 
	 *     @return the name property of the wire
	 */
	public final StringProperty nameProperty() {
		return name;
	}
	
	
	
	
	/**
	 * Gets the component linked to the head of the wire.
	 *
	 * @return the component linked to the head of the wire.
	 */
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

	/**
	 * Gets the list of points corresponding to the drawing of the wire in the
	 * editor.
	 *
	 * @return the list of points corresponding to the drawing of the wire in
	 *         the editor
	 */
	private final ObservableList<Point2D> getPoints() {
		return points;
	}

//	public final void setPoints(ObservableList<Point2D> list) {
//		points = list;
//	}

	/**
	 * Sets the flag stating if the wire is selected.
	 * 
	 * @param b
	 *            the new flag stating if the wire is selected. the
	 */
	public void setSelected(boolean b) {
		selected.set(b);

	}

	/**
	 * Gets the flag stating if the wire is selected.
	 * 
	 * @return the flag stating if the wire is selected.
	 * 
	 */
	public boolean getSelected() {
		return selected.get();
	}

	/**
	 * Gets the flag property stating if the wire is selected.
	 * 
	 * @return the flag property stating if the wire is selected.
	 * 
	 */
	public BooleanProperty selectedProperty() {
		return selected;
	}
	
	
	
	// ************************** Accesses to points
	// ***************************************

	/**
	 * Replaces in the list point, the point at the specified position with the
	 * specified point.
	 *
	 * @param index
	 *            index of the point to replace
	 * @param point
	 *            point to be stored at the specified position
	 */
	public void setPoint(int index, Point2D point) {
		if (index < points.size())
			points.set(index, point);

	}

	/**
	 * Gets the last point of the point list.
	 *
	 * @return the last point of the point list
	 */
	public Point2D getLastPoint() {
		if (points.size() > 0) {
			return points.get(points.size() - 1);
		} else
			return null;
	}

	/**
	 * Gets the size of the list of points.
	 *
	 * @return the size of the list of points.
	 */
	public int getSize() {
		return points.size();
	}

	/**
	 * Returns the point at the specified position the points list.
	 *
	 * @param index
	 *            index of the point to return
	 * @return the point at the specified position in the points list.
	 */
	public Point2D getPoint(int index) {
		if (index < points.size())
			return points.get(index);
		return null;
	}

	/**
	 * Appends the specified point to the end of the point list.
	 *
	 * @param point
	 *            the point to append
	 */
	public void addPoint(Point2D point) {
		points.add(point);
	}

	/**
	 * Adds the specified point to the end of the specified index of the list.
	 *
	 * @param point
	 *            the point to add
	 * @param index
	 *            the index in the list to add the point
	 */
	public void addPoint(int index, Point2D point) {
		points.add(index, point);
	}

	public void addPointFrom(int index, Point2D point, boolean fromHead) {
		if (fromHead)
			addPoint(index, point);
		else
			addPoint(getSize() - index, point);
	}

	public boolean isEdge(int index) {
		return (index == 0 || index == getSize() - 1);
	}
	
	public boolean isEmpty() {
		return points.isEmpty();
	}

	// ********************************* Directions
	// ***************************************************

	/**
	 * Gives a point indicating the direction of the vector (p1,p2) where p1 and
	 * p2 are the two last points of the list of points.
	 *
	 * @return the direction of the wire tail
	 */
	public Point2D tailDirection() {
		return direction(points.get(getSize() - 2), points.get(getSize() - 1));
	}

	/**
	 * Gives a point indicating the direction of the vector (p2,p1) where p1 and
	 * p2 are the first and the second points of the list of points.
	 *
	 * @return the direction of the wire head
	 */
	public Point2D headDirection() {
		return direction(points.get(1), points.get(0));
	}

	/**
	 * Gives a point indicating the direction of the vector (p1,p2), where p1
	 * and p2 are two specified points
	 *
	 * @return the direction of the vector
	 */
	private Point2D direction(Point2D p1, Point2D p2) {
		double x = p2.getX() - p1.getX();
		double y = p2.getY() - p1.getY();
		double x0, y0;
		if (x == 0)
			x0 = 0;
		else if (x > 0)
			x0 = 1;
		else
			x0 = -1;
		if (y == 0)
			y0 = 0;
		else if (y > 0)
			y0 = 1;
		else
			y0 = -1;
		return new Point2D(x0, y0);
	}

	/**
	 * States if the vector (point0, point1) is horizontal, where point0 and
	 * point1 are two specified points
	 * 
	 * @param point0
	 * @param point1
	 * @return
	 */
	private boolean isHorizontal(Point2D point0, Point2D point1) {
		return (point0.getY() == point1.getY());
	}

	// //**************************** Move edge
	// **************************************************
	// Methods used to move a wire by an edge. When the mouse is dragged: use
	// move methods;
	// when the mouse is released: use the glue methods.

	/**
	 * Moves the head of this wire to the specified point.
	 *
	 * @param point
	 *            the new position of the head of this wire
	 */

	public void moveHead(Point2D point) {
		Point2D point0 = points.get(0);
		Point2D point1 = points.get(1);
		if (points.size() == 2) {
			if (isHorizontal(point0, point1))
				point0 = new Point2D(point.getX(), point0.getY());
			else
				point0 = new Point2D(point0.getX(), point.getY());
			points.add(0, point);
		} else if (points.size() > 2) {
			if (point0.getY() == point1.getY())
			point1 = new Point2D(point1.getX(), point.getY());
			else if (point0.getX() == point1.getX())
				point1 = new Point2D(point.getX(), point1.getY());
			points.set(0, point);
		}
		align();
	}

	/**
	 * Moves the tail of this wire to the specified point.
	 *
	 * @param point
	 *            the new position of the tail of this wire
	 */
	public void moveTail(Point2D point) {
		if (points.size() == 2) {
			Point2D point0 = points.get(1);
			Point2D point1 = points.get(0);
			if (point0.getY() == point1.getY()) // segment is horizontal
				point0 = new Point2D(point.getX(), point0.getY());
			else
				point0 = new Point2D(point0.getX(), point.getY());
			points.add(2, point);
		} else if (points.size() > 2) {
			Point2D point0 = points.get(points.size() - 1);
			Point2D point1 = points.get(points.size() - 2);
			if (point0.getY() == point1.getY())
				point1 = new Point2D(point1.getX(), point.getY());
			else if (point0.getX() == point1.getX())
				point1= new Point2D(point.getX(), point1.getY());
			points.set(points.size() - 1, point);
		}
		align();
	}

	/**
	 * Glue the head of this wire to the head of the specified wire.
	 * 
	 * This wire becomes the merge of it with the specified wire which remains
	 * unused (the tail of the specified wire becomes the head of this wire) The
	 * heads of the two wires have to be null
	 *
	 * @param wire
	 *            the wire to glue with this wire
	 */

	public void glueHeadToHead(Wire wire) {
		moveHead(wire.getPoint(0));
		for (int i = 1; i < wire.getSize(); i++)
			points.add(0, wire.getPoint(i));
		System.out.println(this.toString());
		Component c = wire.tail;
		this.setHead(c);
		if (c != null)
			c.changeConnection(new Connection(wire, false), new Connection(this, true));
		System.out.println(c.toString());
	}

	/**
	 * Glue the tail of this wire to the head of the specified wire.
	 * 
	 * This wire becomes the merge of it with the specified wire which remains
	 * unused.
	 * <p>
	 * The tail of the specified wire becomes the tail of this wire.
	 * <p>
	 * The tail of this wire, and the head the specified wire have to be null
	 *
	 * @param wire
	 *            the wire to glue with this wire
	 */
	public void glueTailToHead(Wire wire) {
		moveTail(wire.getPoint(0));
		wire.getPoints().remove(0);
		points.addAll(wire.getPoints());
		Component c = wire.getTail();
		this.setTail(c);
		if (c != null)
			c.changeConnection(new Connection(wire, false), new Connection(this, false));
	}

	/**
	 * Glue the head of this wire to the tail of the specified wire.
	 * 
	 * This wire becomes the merge of it with the specified wire which remains
	 * unused (the head of the specified wire becomes the head of this wire) The
	 * head of this wire, and the tail the specified wire have to be null
	 *
	 * @param wire
	 *            the wire to glue with this wire
	 */
	public void glueHeadToTail(Wire wire) {
		moveHead(wire.getLastPoint());
		for (int i = wire.getSize() - 2; i >= 0; i--)
			points.add(0, wire.getPoint(i));
		Component c = wire.getHead();
		this.setHead(c);
		if (c != null)
			c.changeConnection(new Connection(wire, true), new Connection(this, true));
	}

	/**
	 * Glue the tail of this wire to the tail of the specified wire.
	 * 
	 * This wire becomes the merge of it with the specified wire which remains
	 * unused (the head of the specified wire becomes the tail of this wire) The
	 * head of this wire, and the tail the specified wire have to be null
	 *
	 * If the head of the two components are not CMOS components, this generate
	 * a warning and the simulation cannot be done.
	 * 
	 * @param wire
	 *            the wire to glue with this wire
	 */
	public void glueTailToTail(Wire wire) {
		// TODO generer un warning si ce ne sont pas des cmos
		moveTail(wire.getLastPoint());
		for (int i = wire.getSize() - 2; i >= 0; i--)
			this.addPoint(wire.getPoint(i));
		Component c = wire.getHead();
		this.setTail(c);
		if (c != null)
			c.changeConnection(new Connection(wire, true), new Connection(this, false));
	}

	// *********************************** Adjustments
	// ****************************************

	/**
	 * Merges aligned segments (removes unused points of this wire). A segment
	 * connect two successive points in the list of points.
	 */

	public void align() {
		// TODO ajouter de la precision pour un aller-retour très serré.
		int i = 0;
		while (i <= points.size() - 3) {
			Point2D point0 = points.get(i);
			Point2D point1 = points.get(i + 1);
			Point2D point2 = points.get(i + 2);
			if (point0 == point1)
				points.remove(i + 1);
			else if (point0.getX() == point1.getX() && point1.getX() == point2.getX())
				points.remove(i + 1);
			else if (point0.getY() == point1.getY() && point1.getY() == point2.getY())
				points.remove(i + 1);
			else
				i++;
		}
	}

	public void removeU() {
		int i = 0;
		while (i < points.size() - 3) {
			removeU(i);
			i++;
		}
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeU(int index) {
		if (index + 3 < getSize()) {
			Point2D p0 = points.get(index);
			Point2D p1 = points.get(index + 1);
			Point2D p2 = points.get(index + 2);
			Point2D p3 = points.get(index + 3);
			Boolean p3isTail = (index + 3 == getSize() - 1 && tail != null);
			Boolean p0isHead = (index + 3 == getSize() - 1 && head != null);
			if (p0isHead && p3isTail)
				return;
			if (isHorizontal(p1, p2)) {
				if (-3 * Settings.MOUSE_PRECISION < p1.getX() - p2.getX() && 3 * Settings.MOUSE_PRECISION > p1.getX() - p2.getX()) {
					if (p0.getY() < p1.getY() && p3.getY() < p1.getY()) {
						points.remove(index + 1);
						points.remove(index + 1);
						if (p0.getY() <= p3.getY()) {
							if (p3isTail)
								points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
							else {
								points.remove(index + 1);
								points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
							}
						} else {
							if (p0isHead)
								points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
							else {
								points.remove(index + 1);
								points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
							}
						}
					}
					if (p0.getY() > p1.getY() && p3.getY() > p1.getY()) {
						points.remove(index + 1);
						points.remove(index + 1);
						if (p0.getY() >= p3.getY()) {

							if (p3isTail)
								points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
							else {
								points.remove(index + 1);
								points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
							}
						} else {

							if (p0isHead)
								points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
							else {
								points.remove(index + 1);
								points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
							}
						}
					}
				}
			} else if (-3 * Settings.MOUSE_PRECISION < p1.getY() - p2.getY() && 3 * Settings.MOUSE_PRECISION > p1.getY() - p2.getY()) {
				if (p0.getX() < p1.getX() && p3.getX() < p1.getX()) {
					points.remove(index + 1);
					points.remove(index + 1);
					if (p0.getX() <= p3.getX()) {

						if (p3isTail)
							points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
						else {
							points.remove(index + 1);
							points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
						}
					} else {

						if (p0isHead)
							points.add(index + 1, new Point2D(p3.getX(), p0.getY()));
						else {
							points.remove(index + 1);
							points.add(index + 1, new Point2D(p3.getX(), p0.getY()));
						}
					}
				}
				if (p0.getX() > p1.getX() && p3.getX() > p1.getX()) {
					points.remove(index + 1);
					points.remove(index + 1);
					if (p0.getX() >= p3.getX()) {

						if (p3isTail)
							points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
						else {
							points.remove(index + 1);
							points.add(index + 1, new Point2D(p0.getX(), p3.getY()));
						}
					} else {

						if (p0isHead)
							points.add(index + 1, new Point2D(p3.getX(), p0.getY()));
						else {
							points.remove(index + 1);
							points.add(index + 1, new Point2D(p3.getX(), p0.getY()));
						}
					}
				}
			}
		}
	}

	// ********************** Move a segment
	// ***************************************
	// Moves the wire by moving of a segment of index (s, s+1)

	/**
	 * If this wire is near the specified point, gives the smaller index s of a
	 * segment of this wire near the point. The segment is then (p1, p2) where
	 * p1 is the point of index s, and p2 the point of index s+1 in the list of
	 * points.
	 *
	 * @param point
	 *            the point that try to select a segment
	 * @return the smaller index of a segment near the point.
	 */
	public int selectSegment(Point2D point) {
		for (int i = 0; i < points.size() - 1; i++)
			if ((new Segment(points.get(i), points.get(i + 1))).contains(point, Settings.MOUSE_PRECISION))
				return i;
		return -1;
	}

	/**
	 * Move to the specified point the segment (p1, p2) where p1 is the point of
	 * specified index s, and p2 the point of index s+1 in the list of points.
	 *
	 * @param point
	 *            the new position of the segment
	 * @param s
	 *            the index of the segment to move
	 * @return the new index of the selected segment
	 */
	public int moveSegment(Point2D point, int s) {
		if (s == -1)
			return -1;
		if (s == 0 && s + 1 == this.getSize() - 1)
			return moveSegmentHeadTail(point);
		if (s == 0 && s + 1 != this.getSize() - 1)
			return moveSegmentHead(point);
		if (s + 1 == this.getSize() - 1)
			return moveSegmentTail(point);
		return moveSegmentNotEdge(s, point);
	}

	/**
	 * Move to the specified point the segment (p1, p2) where p1 is the point of
	 * specified index s, and p2 the point of index s+1 in the list of points.
	 * Special case where the two points of the segment are not edges of this
	 * wire
	 *
	 * @param point
	 *            the new position of the segment
	 * @param s
	 *            the index of the segment to move
	 * @return the new index of the selected segment
	 */
	private int moveSegmentNotEdge(int s, Point2D point) {
		Point2D p0 = getPoint(s);
		Point2D p1 = getPoint(s + 1);
		if (p0.getY() == p1.getY()) {
			p0 = new Point2D(p0.getX(), point.getY());
			p1 = new Point2D(p1.getX(), point.getY());
		} else {
			p0 = new Point2D(point.getX(), p0.getY());
			p1 = new Point2D(point.getX(), p1.getY());
		}
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;

	}

	/**
	 * Move to the specified point the segment (p1, p2) where p1 is the point of
	 * specified index s, and p2 the point of index s+1 in the list of points.
	 * Special case where the the two points of the segment are the head and the
	 * tail of this wire
	 *
	 * @param point
	 *            the new position of the segment
	 * @param s
	 *            the index of the segment to move
	 * @return the new index of the selected segment
	 */
	private int moveSegmentHeadTail(Point2D point) {
		if (tail == null)
			return moveSegmentHead(point);
		if (head == null)
			return moveSegmentTail(point);
		Point2D p0 = points.get(0);
		Point2D p5 = points.get(1);
		Point2D p1 = null;
		Point2D p2 = null;
		Point2D p3 = null;
		Point2D p4 = null;
		if (p0.getY() == p5.getY())
			if (p0.distance(point) > p5.distance(point)) {
				double length = (point.getX() - p0.getX()) / 2;
				p1 = new Point2D(p0.getX() + length, p0.getY());
				p2 = new Point2D(p0.getX() + length, point.getY());
				p4 = new Point2D(point.getX(), p0.getY());
				points.add(1, p1);
				points.add(2, p2);
				points.add(3, point);
				points.add(4, p4);
			} else {
				double length = (point.getX() - p0.getX()) / 2;
				p1 = new Point2D(point.getX(), p5.getY());
				p3 = new Point2D(p5.getX() + length, point.getY());
				p4 = new Point2D(p5.getX() + length, p5.getY());
				points.add(1, p1);
				points.add(2, point);
				points.add(3, p3);
				points.add(4, p4);
			}
		else {
			if (p0.distance(point) > p5.distance(point)) {
				double length = (point.getY() - p0.getY()) / 2;
				p1 = new Point2D(p0.getX(), length + p0.getY());
				p2 = new Point2D(point.getX(), length + p0.getY());
				p4 = new Point2D(p0.getX(), point.getY());
				points.add(1, p1);
				points.add(2, p2);
				points.add(3, point);
				points.add(4, p4);
			} else {
				double length = (point.getY() - p5.getY()) / 2;
				p1 = new Point2D(p5.getX(), point.getY());
				p3 = new Point2D(point.getX(), length + p5.getY());
				p4 = new Point2D(p5.getX(), length + p5.getY());
				points.add(1, p1);
				points.add(2, point);
				points.add(3, p3);
				points.add(4, p4);
			}

		}
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 2;
	}

	/**
	 * Moves the tail segment to the specified point
	 * 
	 * @param point
	 *            the point to move
	 * @return the new index of the selected segment
	 */

	private int moveSegmentTail(Point2D point) {
		// System.out.println("moveSegmentTail");
		int end = getSize() - 1;
		
		Point2D p0 = points.get(end);
		Point2D p1 = points.get(end - 1);
		if (tail != null) {
			if (p0.getY() == p1.getY()) {
				points.add(end, new Point2D(point.getX(), p0.getY()));
				points.add(end, point);
				p1 = new Point2D(p1.getX(), point.getY());
			} else {
				points.add(end, new Point2D(p0.getX(), point.getY()));
				points.add(end, point);
				p1 = new Point2D(point.getX(), p1.getY());
			}
			return end - 1;
		} else if (p0.getY() == p1.getY()) {
			p0 = new Point2D(p0.getX(), point.getY());
			p1 = new Point2D(p1.getX(), point.getY());
		} else {
			p0= new Point2D(point.getX(), p0.getY());
			p1= new Point2D(point.getX(), p1.getY());
		}
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return end - 1;
	}

	/**
	 * Moves the head segment to the specified point
	 * 
	 * @param point
	 *            the point to move
	 * @return the new index of the selected segment
	 */
	private int moveSegmentHead(Point2D point) {
		// System.out.println("moveSegmentHead");
		Point2D p0 = points.get(0);
		Point2D p1 = points.get(1);
		if (head != null) {
			if (p0.getY() == p1.getY()) {
				points.add(1, new Point2D(point.getX(), p0.getY()));
				points.add(2, point);
				p1= new Point2D(p1.getX(), point.getY());
			} else {
				points.add(1, new Point2D(p0.getX(), point.getY()));
				points.add(2, point);
				p1= new Point2D(point.getX(), p1.getY());
			}
			return 2;
		} else if (p0.getY() == p1.getY()) {
			p0= new Point2D(p0.getX(), point.getY());
			p1= new Point2D(p1.getX(), point.getY());
		} else {
			p0= new Point2D(point.getX(), p0.getY());
			p1= new Point2D(point.getX(), p1.getY());
		}
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// ********************************* Translations
	// *********************************
	// used to translate all the wire (when the connected component moves)

	/**
	 * Translate all points of the wire except the tail if it is connected. 1
	 * point at location (x,y) is translated by delta.getX() along the x axis and
	 * delta.getY() along the y axis so that it now represents the point (x+dx,y+dy).
	 * This method is used when move a component connected to the head of this
	 * wire.
	 *
	 * @param delta
	 *            the vector that represent the translation: delta.getX() is the
	 *            distance to move points along the X axis, and delta.getY() is the
	 *            distance to move points along the Y axis
	 */

	public void translateFromHead(Point2D delta) {
		if (tail == null) {
//			for (Point2D p : points)
//				p = new Point2D(p.getX() + delta.getX(), p.getY() + delta.getY());
			for (int i = 0; i < points.size(); i++)
				points.set(i, new Point2D(points.get(i).getX() + delta.getX(), points.get(i).getY() + delta.getY()));

		} else {
			Point2D pn = points.get(getSize() - 1);
			Point2D pn1 = points.get(getSize() - 2);
			for (int i = 0; i < getSize() - 1; i++) {
				Point2D p = points.get(i);
				setPoint(i, new Point2D(p.getX() + delta.getX(), p.getY() + delta.getY()));
			}
			Point2D p;
			if (pn.getY() == pn1.getY())
				p = new Point2D(points.get(getSize() - 2).getX(), pn.getY());
			else
				p = new Point2D(pn.getX(), points.get(getSize() - 2).getY());
			points.add(getSize() - 1, p);
		}
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Translate all points of the wire except the head if it is connected. 1
	 * point at location (x,y) is translated by delta.getX() along the x axis and
	 * delta.getY() along the y axis so that it now represents the point (x+dx,y+dy).
	 * This method is used when move a component connected to the tail of this
	 * wire.
	 *
	 * @param delta
	 *            the vector that represent the translation: delta.getX() is the
	 *            distance to move points along the X axis, and delta.getY() is the
	 *            distance to move points along the Y axis
	 */

	public void translateFromTail(Point2D delta) {
		if (head == null) {
//			for (Point2D p : points)
//				p = new Point2D(p.getX() + delta.getX(), p.getY() + delta.getY());
			for (int i = 0; i < points.size(); i++)
				points.set(i, new Point2D(points.get(i).getX() + delta.getX(), points.get(i).getY() + delta.getY()));

		} else {
			Point2D p0 = points.get(0);
			Point2D p1 = points.get(1);
			for (int i = 1; i < getSize(); i++) {
				Point2D p = points.get(i);
				setPoint(i, new Point2D(p.getX() + delta.getX(), p.getY() + delta.getY()));
			}
			Point2D p;
			if (p0.getY() == p1.getY())
				p = new Point2D(points.get(1).getX(), p0.getY());
			else
				p = new Point2D(p0.getX(), points.get(1).getY());
			points.add(1, p);
		}
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ************************************* Others
	// **************************************

	// dit si le wire contient (à precision près) le point

	/**
	 * Determines whether the specified point is near (according to the mouse
	 * precision Settings.MOUSE_PRECISION) this wire.
	 *
	 * @param p
	 *            the point
	 * @return true, if the specified point is near this wire.
	 */
	public boolean intersects(Point2D p) {
		int prec = Settings.MOUSE_PRECISION;
		for (int i = 0; i < points.size() - 1; i++)
			if ((new Segment(points.get(i), points.get(i + 1))).contains(p, prec))
				return true;
		return false;
	}

	/**
	 * Proximal point
	 *
	 * @param point
	 *            the point
	 * @return the index of a point of this wire near the point, otherwise
	 *         return -1;
	 */
	public int proximalPoint(Point2D point) {
		int prec = Settings.MOUSE_PRECISION * 2;
		Rectangle2D rect = new Rectangle2D(point.getX() - prec, point.getY() - prec, prec * 2, prec * 2);
		for (int i = 0; i < getSize(); i++)
			if (rect.contains(this.getPoint(i)))
				return i;

		return -1;

	}

	/**
	 * Draw.
	 *
	 * @param gc the graphics context
	 */
//	public void draw(GraphicsContext gc) {
//		gc.setStroke(Settings.WIRE_COLOR);
//		if (!getSelected())
//			gc.setLineWidth(2);
//		else
//			gc.setLineWidth(3);
//		for (int i = 0; i < points.size() - 1; i++)
//			gc.strokeLine(points.get(i).getX(), points.get(i).getY(), points.get(i + 1).getX(), points.get(i + 1).getY());
//	}
	//TODO
	public void draw(GraphicsContext gc) {
		gc.setStroke(Settings.WIRE_COLOR);
		if (!getSelected())
			gc.setLineWidth(2);
		else
			gc.setLineWidth(3);
		
			gc.strokeLine(points.get(0).getX(), points.get(0).getY(), points.get(points.size()-1).getX(), points.get(points.size()-1).getY());
		
	}

	@Override
	public String toString() {
		String out = "[ ";
		for (Point2D p : points) {
			out = out + "( " + p.getX() + " , " + p.getY() + " )";
		}
		return out + " ]";
	}

	// ************************** Joint two wires
	// *****************************************

	// cut this in two parts, and change their name
	// return the new wire created.

	/**
	 * Cut.
	 *
	 * @param name
	 *            the name
	 * @param pos
	 *            the pos
	 * @return the wire
	 */
	public Wire cut(String name, Point2D pos) {
		int pp = proximalPoint(pos);
		if (pp != -1)
			return cutToPoint(name, pp);// Ici normalement le controleur a deja
										// vérifié
		// que pp ne peux pas être un edge
		else {
			int index = selectSegment(pos);
			if (index >= 0)
				return cutSegment(name, index, pos);
		}
		try {
			debugg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Wire cutSegment(String name, int index, Point2D pos) {
		Point2D p0 = this.getPoint(index);
		Point2D p1 = this.getPoint(index + 1);
		Point2D p;
		if (isHorizontal(p0, p1))
			p = new Point2D(pos.getX(), p0.getY());
		else
			p = new Point2D(p0.getX(), pos.getY());
		this.points.add(index + 1, p);
		this.points.add(index + 1, p);
		this.setName(name);
		Wire wire = new Wire(name);
		while (index + 2 < points.size()) {
			wire.getPoints().add(this.getPoint(index + 2));
			points.remove(index + 2);
		}
		wire.setTail(this.tail);
		Component c = this.tail;
		if (c != null) {
			c.changeConnection(new Connection(this, false), new Connection(wire, false));
			this.tail = null;
		}

		return wire;
	}

	private Wire cutToPoint(String name, int pp) {
		if (pp == 0 || pp == this.getSize() - 1)
			return this;
		this.points.add(pp + 1, getPoint(pp));
		this.setName(name);
		Wire wire = new Wire(name);
		while (pp + 1 < points.size()) {
			wire.getPoints().add(this.getPoint(pp + 1));
			points.remove(pp + 1);
		}
		wire.setTail(this.tail);
		Component c = this.tail;
		if (c != null) {
			c.changeConnection(new Connection(this, false), new Connection(wire, false));
			this.tail = null;
		}
		return wire;
	}

	public void debugg() throws Exception {
		for (int i = 0; i < getSize() - 1; i++) {
			Point2D p0 = getPoint(i);
			Point2D p1 = getPoint(i + 1);
			if (!(p0.getX() == p1.getX() || p0.getY() == p1.getY())) {
				throw new Exception("bad wire");
			}
		}
	}

	public int movePoint(int selPoint, Point2D point) {
		// TODO Auto-generated method stub
		return 0;
	}

	// // public Point cut(String name, Point pos){
	// // int pp = proximalPoint(pos);
	// // if( pp != -1)
	// // return cutPoint(name, pp);
	// // else{
	// // int i = selectSegment(pos);
	// // if (i>=0)
	// // return cutSegment(name, i, pos);
	// // }
	// // return null;
	// // }
	//
	// // private Point cutSegment(String name2, int index, Point pos) {
	// // Wire wire = new Wire(name2);
	// // for (int j=index ; j < getSize(); j++ )
	// // // wire.getPoints().add(this.getPoint(j)); // il ne peut y avoir qu'un
	// // seul point!
	// // while ( index + 1 < points.size() ){
	// // wire.getPoints().add(this.getPoint(index+1);
	// // points.remove(index + 1);
	// // }
	// // if (getPoint(index).getX() == wire.getPoint(0).getX()){
	// // int x = getPoint(index).getX();
	// // getPoint(index).getY() = pos.getY();
	// // wire.getPoint(0).getY() = pos.getY();
	// // wire.setHead(joint);
	// // wire.setTail(getTail());
	// // setTail(joint);
	// // setName(name);
	// // return new Point(x, pos.getY());
	// // }
	// // else {
	// // int y = getPoint(index).getY();
	// // getPoint(index).getX() = pos.getX();
	// // wire.getPoint(0).getX() = pos.getX();
	// // wire.setHead(joint);
	// // wire.setTail(getTail());
	// // setTail(joint);
	// // setName(name);
	// // return new Point(pos.getX(),y);
	// // }
	// // return null;
	// // }
	//
	//
	//
	// // cut this in two parts, connect them to joint, and change their name
	// /**
	// * Cut.
	// *
	// * @param name
	// * the name
	// * @param pos
	// * the pos
	// * @param joint
	// * the joint
	// * @return the point
	// */
	// // return the position of joint
	// public Point cut(String name, Point pos, Joint joint) {
	// int pp = proximalPoint(pos);
	// if (pp != -1)
	// return cutPoint(name, pp, joint);
	// else {
	// int i = selectSegment(pos);
	// if (i >= 0)
	// return cutSegment(name, i, pos, joint);
	// }
	// return null;
	// }
	//
	// private Point cutPoint(String name, int pp, Joint joint) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// /**
	// * Cut segment.
	// *
	// * @param name
	// * the name
	// * @param index
	// * the index
	// * @param pos
	// * the pos
	// * @param joint
	// * the joint
	// * @return the point
	// */
	// public Point cutSegment(String name, int index, Point pos, Joint joint) {
	// Wire wire = new Wire(name);
	// // for (int j=index + 1 ; j < getSize(); j++ )
	// // wire.getPoints().add(this.getPoint(j)); // il ne peut y avoir qu'un
	// // seul point!
	// while (index + 1 < points.size()) {
	// wire.getPoints().add(this.getPoint(index + 1));
	// points.remove(index + 1);
	// }
	// if (getPoint(index).getX() == wire.getPoint(0).getX()) {
	// int x = getPoint(index).getX();
	// getPoint(index).getY() = pos.getY();
	// wire.getPoint(0).getY() = pos.getY();
	// wire.setHead(joint);
	// wire.setTail(getTail());
	// setTail(joint);
	// setName(name);
	// return new Point(x, pos.getY());
	// } else {
	// int y = getPoint(index).getY();
	// getPoint(index).getX() = pos.getX();
	// wire.getPoint(0).getX() = pos.getX();
	// wire.setHead(joint);
	// wire.setTail(getTail());
	// setTail(joint);
	// setName(name);
	// return new Point(pos.getX(), y);
	// }
	//
	// }
	//
	//
	// // public void moveSelected(Point point) {
	// // if (selectedSegment == -1) return;
	// // if (selectedSegment == 0){
	// // if (pointIsSelected(points.get(0), point))
	// // if (head != null) return;
	// // else movesTailPoint(point);
	// // //TODO finir
	// // }
	// // else if (selectedSegment == points.size()-2){
	// // Point p = getLastPoint();
	// // int prec = Settings.MOUSE_PRECISION*2;
	// // Rectangle rect = new Rectangle(p.getX()-prec, p.getY() - prec, prec*2, prec*2);
	// // if (rect.contains(point)){ //TODO Passer en mode fusion de connections
	// // setTail(point);
	// //// else
	// // }

	public void propagate() {
		this.tail.processing();
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
