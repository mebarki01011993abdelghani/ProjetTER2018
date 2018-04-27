package fr.univmrs.lif.tools;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Point2DProperty{

	DoubleProperty x = new SimpleDoubleProperty();
	DoubleProperty y = new SimpleDoubleProperty();
	
	public Point2DProperty(DoubleProperty x,DoubleProperty y) {
		this.x = x;
		this.y = y;
	}
	
	public Point2DProperty() {}

	
	public Point2DProperty(double x, double y) {
		this.x.set(x);
		this.y.set(y);
	}

	public double getX() {
		return x.get();
	}

	public void setX(double x) {
		this.x.set(x);
	}

	public double getY() {
		return y.get();
	}

	public void setY(double y) {
		this.y.set(y);
	}
	
	public DoubleProperty xProperty(){
		return x;
	}
	
	public DoubleProperty yProperty(){
		return y;
	}
	
	@Override
	public String toString() {
	
		return "("+getX()+","+getY()+")";
	}
	
	public void bind(DoubleProperty x, DoubleProperty y){
		this.x.bind(x);
		this.y.bind(y);
	}
	public void bind(Point2DProperty point){
		this.x.bind(point.xProperty());
		this.y.bind(point.yProperty());
	}
	
	public void unbind(){
		this.x.unbind();
		this.y.unbind();
	}
}
