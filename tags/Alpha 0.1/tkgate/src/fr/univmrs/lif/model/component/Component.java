package fr.univmrs.lif.model.component;


import java.util.List;

import fr.univmrs.lif.model.wire.Wire;
import fr.univmrs.lif.tools.ComponentType;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public interface Component {

	public ComponentType getType();

	public void draw(GraphicsContext gc);

	void setPosition(Point2D p);

	Point2D getPosition();

	String getName();

	void setName(String name);
	
	public StringProperty nameProperty();
	 
	void setShowName(boolean b);

	public boolean getShowName();

	void setOrientation(int orientation);

	public int getOrientation();

	public List<Wire> findWires(Point2D point);

	public List<Wire> allWires();

	public int getDefaultInputsSize();

	public int getDefaultOutputsSize();

	// void setInputs(List<Wire> inputList);

	List<Connection> getInputs();

	// void setOutputs(List<Wire> inputList);

	List<Connection> getOutputs();

	public int getInputSize();

	public int getOutputSize();

	public boolean intersects(Rectangle2D rect);

	public boolean contains(Point2D p);

	public boolean intersects(Component component);

	void setSelected(boolean b);

	void move(Point2D p);

	void refactorConnections();

	// void resetWires();

	/**
	 * replace the specified old connection by the specified new connection in
	 * the lists of input and outputs
	 * 
	 * @param connectionOld
	 *            the old connection to replace by the new connection
	 * @param wireNew
	 *            the new connection that replace the old connection
	 */
	void changeConnection(Connection connectionOld, Connection connectionNew);

	// public void resetWireHead(Wire wire);
	
	public void processing();
}
