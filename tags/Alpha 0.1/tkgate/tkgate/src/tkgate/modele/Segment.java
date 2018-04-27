package tkgate.modele;


import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Segment {
	
	public Point2D.Double point1, point2;

	public Segment(Point2D.Double point1, Point2D.Double point2) {
		super();
		this.point1 = point1;
		this.point2 = point2;
	}
	
	public boolean isHorizontal(){
		return point1.y == point2.y;
	}
	
	public boolean isVertical(){
		return point1.x == point2.x;
	}
	
	public boolean contains(Point2D.Double p){
		boolean fromX = (point1.x <= p.x && point2.x >=p.x) ||
				(point2.x <= p.x && point1.x >=p.x);
		boolean fromY = (point1.y <= p.y && point2.y >=p.y) ||
				(point2.y <= p.y && point1.y >=p.y);
		return fromX && fromY;
	}
	
	public boolean contains(Point2D.Double p, int precision){
		boolean fromX = (point1.x <= p.x + precision && point2.x >=p.x - precision) ||
				(point2.x <= p.x + precision && point1.x >=p.x - precision);
		boolean fromY = (point1.y <= p.y + precision && point2.y >=p.y - precision) ||
				(point2.y <= p.y + precision && point1.y >=p.y - precision);
		return fromX && fromY;
	}
}
