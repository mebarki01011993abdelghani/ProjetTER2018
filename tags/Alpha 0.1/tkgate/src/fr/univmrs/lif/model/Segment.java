package fr.univmrs.lif.model;


import java.awt.geom.Rectangle2D;

import javafx.geometry.Point2D;

public class Segment {
	
	public Point2D point1, point2;

	public Segment(Point2D point1, Point2D point2) {
		super();
		this.point1 = point1;
		this.point2 = point2;
	}
	
	public boolean isHorizontal(){
		return point1.getY() == point2.getY();
	}
	
	public boolean isVertical(){
		return point1.getX() == point2.getX();
	}
	
	public boolean contains(Point2D p){
		boolean fromX = (point1.getX() <= p.getX() && point2.getX() >=p.getX()) ||
				(point2.getX() <= p.getX() && point1.getX() >=p.getX());
		boolean fromY = (point1.getY() <= p.getY() && point2.getY() >=p.getY()) ||
				(point2.getY() <= p.getY() && point1.getY() >=p.getY());
		return fromX && fromY;
	}
	
	public boolean contains(Point2D p, int precision){
		boolean fromX = (point1.getX() <= p.getX() + precision && point2.getX() >=p.getX() - precision) ||
				(point2.getX() <= p.getX() + precision && point1.getX() >=p.getX() - precision);
		boolean fromY = (point1.getY() <= p.getY() + precision && point2.getY() >=p.getY() - precision) ||
				(point2.getY() <= p.getY() + precision && point1.getY() >=p.getY() - precision);
		return fromX && fromY;
	}
}
