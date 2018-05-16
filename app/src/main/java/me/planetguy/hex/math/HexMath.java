//package me.planetguy.hex.math;
//
//
//public class HexMath {
//
//	/**
//	 * Bounds checking for whether the hexagon at the point is within a stretched big hexagon of the given dimensions.
//	 */
//	public static boolean permitSpace(HexPoint pt, int bigHexL, int bigHexW){
//		return permitSpace(pt.x(), pt.y(), bigHexL, bigHexW);
//	}
//
//	public static boolean permitSpace(int x, int y, int bigHexL, int bigHexW){
//		int sum=x+y;
//		return sum>=bigHexL-1              //cuts off top left corner
//			&& sum < 2*(bigHexL-1)+bigHexW //cuts off bottom right corner
//			&& x>=0 && y>=0 && x<bigHexW+bigHexL-1 && y<=bigHexL*2-2;//cuts off out of array
//	}
//
//	/**
//	 * Tells you what dimensions you need to make an array with for a hexagon of sloped sides of length l and width w
//	 */
//	public static HexPoint getArraySizedFor(int l, int w){
//		return new HexPoint( l+w-1, l*2-1);
//	}
//
//	/**
//	 * "Knocks over" a square array for rendering it as hex tiles
//	 *
//	 * x and y are coords of the tile, widthPx is what you multiply by
//	 */
//	public static int calculateAngle(int x, int y, int widthPx){
//		return x*widthPx + y*widthPx/2;
//	}
//
//}
