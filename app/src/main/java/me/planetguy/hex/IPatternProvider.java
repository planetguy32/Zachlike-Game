//package me.planetguy.hex;
//
//import me.planetguy.notspacechem.simulation.Molecule;
//
////Patterns are pulled as needed, so only the provider is needed - consumer is implicit
//public interface IPatternProvider {
//
//	public static final IPatternProvider nullProvider=new IPatternProvider(){
//		public boolean hasPattern(SharedSide regions) { return false; }
//		public Molecule providePattern(SharedSide regions) { throw new IllegalStateException("Cannot provide pattern!"); }
//	};
//
//	public static enum SharedSide {
//		TOP_LEFT,
//		BOTTOM_LEFT,
//		TOP_RIGHT,
//		BOTTOM_RIGHT,
//		LEFT,
//		RIGHT;
//	}
//
//	boolean hasPattern(SharedSide sides);
//	Molecule providePattern(SharedSide sides);
//
//}
