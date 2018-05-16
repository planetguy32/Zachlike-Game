package me.planetguy.notspacechem.simulation;

/*
 * Immutable
 */
public class Point {

	public static final Point ORIGIN=new Point(0,0);

	final int x;
	final int y;

	public Point(int x, int y){
		this.x=x;
		this.y=y;
	}
	public Point add(Point p){
		return new Point(x+p.x, y+p.y);
	}
	public Point negate(){
		return new Point(-x, -y);
	}
	public Point subtract(Point p){
		return new Point(x-p.x, y-p.y);
	}
	public int hashCode(){
		return x*y ^ (x*x) ^ (y*13);
	}
	public boolean equals(Object o){
		return (o instanceof Point) && ((Point)o).equals(this);
	}
	public boolean equals(Point a){
		return x==a.x && y==a.y;
	}
	
	public int x(){return x;}
	public int y(){return y;}

	public Point addX(int x){
		return new Point(this.x+x,y);
	}
	public Point addY(int y){
		return new Point(x,this.y+y);
	}

	public String toString(){
		return "("+x()+","+y()+")";
	}
}