package fr.univmrs.lif.model.component;

import java.awt.Dimension;

import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.tools.ComponentType;
import javafx.geometry.Point2D;


public class ComponentFactory {
	public static Component buildComponent(ComponentType type,String name, Point2D pos, Dimension dim, int freeNumber){
		if (type == ComponentType.AND)
			return new AndGate(name, pos, dim, freeNumber);
		else
			System.err.println("Factory : bad component identifyer");
			return null;
		
	}

}
