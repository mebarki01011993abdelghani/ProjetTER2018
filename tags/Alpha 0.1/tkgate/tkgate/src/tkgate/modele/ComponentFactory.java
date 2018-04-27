package tkgate.modele;

import java.awt.Dimension;
import java.awt.geom.Point2D;


public class ComponentFactory {
	public static Component buildComponent(String ctype,String name, Point2D.Double pos, Dimension dim, int freeNumber){
		if (ctype.compareTo("AND") == 0)
			return new AndGate(name, pos, dim, freeNumber);
		if (ctype.compareTo("And") == 0)
			return new AndGate(name, pos, dim, freeNumber);
		else
			System.err.println("Factory : bad component identifyer");
			return null;
		
	}

}
