//package me.planetguy.hex.math;
//
///*
// * Immutable
// */
//public class HexPoint{
//
//	public static final HexPoint ORIGIN=new HexPoint(0,0);
//
//	final int x;
//	final int y;
//
//	public HexPoint(int x, int y){
//		this.x=x;
//		this.y=y;
//	}
//	public HexPoint add(HexPoint p){
//		return new HexPoint(x+p.x, y+p.y);
//	}
//	public HexPoint negate(){
//		return new HexPoint(-x, -y);
//	}
//	public HexPoint subtract(HexPoint p){
//		return new HexPoint(x-p.x, y-p.y);
//	}
//	public int hashCode(){
//		return x*y ^ (x*x) ^ (y*13);
//	}
//	public boolean equals(Object o){
//		return (o instanceof HexPoint) && ((HexPoint)o).equals(this);
//	}
//	public boolean equals(HexPoint a){
//		return x==a.x && y==a.y;
//	}
//
//	public int x(){return x;}
//	public int y(){return y;}
//	public int z(){return -x-y;}
//
//	public HexPoint addX(int x){
//		return new HexPoint(this.x+x,y);
//	}
//	public HexPoint addY(int y){
//		return new HexPoint(x,this.y+y);
//	}
//	public HexPoint addZ(int z){
//		return new HexPoint(x-z,y+z);
//	}
//
//	//Could probably be done with less memory pressure, unless Java optimizes this away
//	public HexPoint rotate(HexPoint axis, int deltaDirection){
//		if(deltaDirection < 0)
//			deltaDirection += 6;
//		return subtract(axis).rotate_do(deltaDirection).add(axis);
//	}
//
//	private HexPoint rotate_do(int deltaDirection) {
//		if(deltaDirection==0){
//			return this;
//		} else {
//			return new HexPoint(-y(), -z()).rotate_do(deltaDirection-1);
//		}
//	}
//
//	public String toString(){
//		return "("+x()+","+y()+") -> "+z();
//	}
//}