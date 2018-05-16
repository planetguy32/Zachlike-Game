//package me.planetguy.hex.math;
//
///**
// * 2D hexagon heading
// */
//public enum HexDirection {
//	NE,E,SE,SW,W,NW;
//
//	private static final HexDirection[] opposite={
//		SW,W,NW,NE,E,SE
//	};
//	public HexDirection opposite(){
//		return opposite[ordinal()];
//	}
//
//	private static final boolean[][] isCloseEnoughToAxis={
//			{true, true, false, false, false, true},
//			{true, true, true, false, false, false},
//			{false, true, true, true, false, false},
//			{false, false, true, true, true, false},
//			{false, false, false, true, true, true},
//			{true, false, false, false, true, true},
//	};
//	public HexDirection alignToAxisOrOpposite(HexDirection axis){
//		return isCloseEnoughToAxis[axis.ordinal()][ordinal()] ? axis : axis.opposite();
//	}
//
//	private static final int[] dxTable={
//		1,1,0,-1,-1,0
//	};
//	private static final int[] dyTable={
//		-1,0,1,1,0,-1
//	};
//
//	public int getXOffset(){
//		return dxTable[ordinal()];
//	}
//
//	public int nextInXFrom(int orig){
//		return orig+getXOffset();
//	}
//
//	public int getYOffset(){
//		return dyTable[ordinal()];
//	}
//
//	public int nextInYFrom(int orig){
//		return orig+getYOffset();
//	}
//
//	public int getDeltaDirectionTo(HexDirection h){
//		return (h.ordinal()+6-ordinal()) % 6;
//	}
//}