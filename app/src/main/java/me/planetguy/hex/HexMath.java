package me.planetguy.hex;

public class HexMath {
	/*
	 * Hexagonal math for axial coordinate systems
	 * See also http://www.redblobgames.com/grids/hexagons/
	 */
	
	public static class Point2{
		public final int x;
		public final int y;
		public Point2(int x, int y){
			this.x=x;
			this.y=y;
		}
		public Point2 add(Point2 p){
			return new Point2(x+p.x, y+p.y);
		}
	}
	
	/**
	 * 2D hexagon heading
	 */
	public enum HexDirection {
		NE,E,SE,SW,W,NW;
		
		private static final HexDirection[] opposite={
			SW,W,NW,NE,E,SE
		};
		public HexDirection opposite(){
			return opposite[ordinal()];
		}
		
		private static final boolean[][] isCloseEnoughToAxis={
				{true, true, false, false, false, true},
				{true, true, true, false, false, false},
				{false, true, true, true, false, false},
				{false, false, true, true, true, false},
				{false, false, false, true, true, true},
				{true, false, false, false, true, true},
		};
		public HexDirection alignToAxisOrOpposite(HexDirection axis){
			return isCloseEnoughToAxis[axis.ordinal()][ordinal()] ? axis : axis.opposite();
		}
		
		private static final int[] dxTable={
			1,1,0,-1,-1,0
		};
		private static final int[] dyTable={
			-1,0,1,1,0,-1
		};
		
		public int getXOffset(){
			return dxTable[ordinal()];
		}
		
		public int nextInXFrom(int orig){
			return orig+getXOffset();
		}
		
		public int getYOffset(){
			return dyTable[ordinal()];
		}
		
		public int nextInYFrom(int orig){
			return orig+getYOffset();
		}
	}
	
	/**
	 * Bounds checking for whether the hexagon at the point is within a stretched big hexagon of the given dimensions.
	 */
	public static boolean permitSpace(Point2 pt, int bigHexL, int bigHexW){
		return permitSpace(pt.x, pt.y, bigHexL, bigHexW);
	}
	
	public static boolean permitSpace(int x, int y, int bigHexL, int bigHexW){
		int sum=x+y;
		return sum>=bigHexL-1              //cuts off top left corner
			&& sum < 2*(bigHexL-1)+bigHexW //cuts off bottom right corner
			&& x>=0 && y>=0 && x<bigHexW+bigHexL-1 && y<=bigHexL*2-2;//cuts off out of array
	}
	
	/**
	 * Tells you what dimensions you need to make an array with for a hexagon of sloped sides of length l and width w
	 */
	public static Point2 getArraySizedFor(int l, int w){
		return new Point2( l+w-1, l*2-1);
	}
	
	/**
	 * "Knocks over" a square array for rendering it as hex tiles
	 * 
	 * x and y are coords of the tile, widthPx is what you multiply by
	 */
	public static int calculateAngle(int x, int y, int widthPx){
		return x*widthPx + y*widthPx/2;
	}
	
}
