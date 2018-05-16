//package me.planetguy.hex;
//
//import java.util.ArrayList;
//import java.util.EnumMap;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import me.planetguy.hex.math.HexDirection;
//import me.planetguy.hex.math.HexPoint;
//import me.planetguy.notspacechem.simulation.Molecule;
//
//public class Scroll implements IContext, IPatternProvider {
//
//	private int index;
//
//	public final List<Walker> walkers=new ArrayList<Walker>();
//
//	HashMap<HexPoint, Molecule> activePatterns=new HashMap<HexPoint, Molecule>();
//
//	private final int x;
//	private final int y;
//
//	private EnumMap<SharedSide, IPatternProvider> neighbours=new EnumMap<SharedSide, IPatternProvider>(SharedSide.class);
//
//	/**
//	 * Max dimensions of this scroll
//	 */
//	public Scroll(int x, int y){
//		this.x=x;
//		this.y=y;
//	}
//
//	//test case
//	public void init(){
//		Walker w1=new Walker(this);
//		walkers.add(w1);
//		w1.goTo(5, 9);
//		w1.setSymbol(7,9,Symbol.LOCK_DOWN);
//
//		w1.setSymbol(9,9,Symbol.TURN_SE_NW);
//		w1.setSymbol(9,10,Symbol.TURN_NE_SW);
//		w1.setSymbol(8,11,Symbol.TURN_E_W);
//
//		w1.setSymbol(7,11,Symbol.UNLOCK_DOWN);
//
//		w1.setSymbol(5,9,Symbol.TURN_E_W);
//		w1.setSymbol(4,10,Symbol.TURN_NE_SW);
//		w1.setSymbol(4,11,Symbol.TURN_SE_NW);
//
//
//		Walker w2=new Walker(w1);
//
//		w2.setSymbol(7,9,Symbol.LOCK_UP);
//
//		w2.setSymbol(12,9,Symbol.TURN_SE_NW);
//		w2.setSymbol(12,10,Symbol.TURN_NE_SW);
//		w2.setSymbol(11,11,Symbol.TURN_E_W);
//
//		w2.setSymbol(7,11,Symbol.UNLOCK_UP);
//
//		w2.setSymbol(5,9,Symbol.TURN_E_W);
//		w2.setSymbol(4,10,Symbol.TURN_NE_SW);
//		w2.setSymbol(4,11,Symbol.TURN_SE_NW);
//		walkers.add(w2);
//
//		w2.goTo(12, 7);
//		w2.turnTo(HexDirection.SE);
//
//		Molecule p=new Molecule();
//		p.attach(0, 0, PatternPart.ORIGIN);
//		p.attach(0, 1, PatternPart.ORB);
//		p.attach(1, 1, PatternPart.FIRE);
//		addPattern(10, 9, p);
//
//		w2.setSymbol(10, 9, Symbol.SWAP);
//	}
//
//	int id=0;
//	@Override
//	public int getNextID(){
//		return id++;
//	}
//
//	public void preStep(){
//		for(Walker w:walkers){
//			w.preUpdate();
//		}
//	}
//
//	public void step(){
//		for(Walker w:walkers){
//			w.update();
//		}
//	}
//
//	public void postStep(){
//		for(Walker w:walkers){
//			w.postUpdate();
//		}
//	}
//
//
//	public Molecule getPatternAtPoint(int x, int y){
//		return activePatterns.get(new HexPoint(x,y));
//	}
//
//	@Override
//	public int getXLimit() {
//		return x;
//	}
//
//	@Override
//	public int getYLimit() {
//		return y;
//	}
//
//	@Override
//	public void dispatchSymbol(Symbol symbol, int sendingWalker) {
//		System.out.println(symbol.mnemonic);
//		Walker walker;
//		switch(symbol){
//		case LOCK_UP:
//			getWalker(sendingWalker-1).setLocked(true);
//			break;
//		case LOCK_DOWN:
//			getWalker(sendingWalker+1).setLocked(true);
//			break;
//		case UNLOCK_UP:
//			getWalker(sendingWalker-1).setLocked(false);
//			break;
//		case UNLOCK_DOWN:
//			getWalker(sendingWalker+1).setLocked(false);
//			break;
//		case GRAB:
//			walker=getWalker(sendingWalker);
//			walker.setHand(getPatternAtPoint(walker.x(), walker.y()));
//			break;
//		case DROP:
//			walker=getWalker(sendingWalker);
//			walker.setHand(null);
//			break;
//		case SWAP:
//			walker=getWalker(sendingWalker);
//			if(walker.getHand()==null)
//				walker.setHand(getPatternAtPoint(walker.x(), walker.y()));
//			else
//				walker.setHand(null);
//			break;
//		case CTL1:
//			break;
//		case CTL2:
//			break;
//		case CTL3:
//			break;
//		case CTL4:
//			break;
//		case PULL_DOWN:
//			break;
//		case PULL_UP:
//			break;
//		case PUSH_DOWN:
//			break;
//		case PUSH_UP:
//			break;
//		default:
//			break;
//		}
//	}
//
//	public Walker getWalker(int walker){
//		//TODO pull from global contextees
//		return walkers.get(walker);
//	}
//
//	public void addPattern(int x, int y, Molecule p){
//		Set<HexPoint> points=p.getOccupiedPoints(new HexPoint(x,y));
//		for(HexPoint pt:points){
//			activePatterns.put(pt, p);
//		}
//	}
//
//	public boolean notifyRotation(Walker walker, int deltaDirection) {
//		HexPoint center=walker.getPosition();
//		return resolveCollision(walker,
//				(HexPoint p) -> { return p.rotate(center, deltaDirection); }
//				);
//	}
//
//	public boolean notifyTranslation(Walker walker, int x2, int y2) {
//		return resolveCollision(walker,
//				(HexPoint p) -> { return p.addX(x2-walker.x()).addY(y2-walker.y());
//				});
//	}
//
//	public boolean resolveCollision(Walker walker, Function<HexPoint, HexPoint> transformation){
//		Molecule pattern=walker.getHand();
//		if(pattern==null)
//			return true;
//		HexPoint center=walker.getPosition();
//		Set<HexPoint> patternPoints=pattern.getOccupiedPoints(center);
//		Set<HexPoint> newPatternPoints=patternPoints.stream().map(transformation)
//				.collect(Collectors.toCollection(HashSet::new));
//
//		if(!motionCausesCollision(patternPoints, newPatternPoints)){
//			//not colliding with anything else
//			for(HexPoint pt:patternPoints){
//				activePatterns.remove(pt);
//			}
//			for(HexPoint pt:newPatternPoints){
//				activePatterns.put(pt, pattern);
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public boolean motionCausesCollision(Set<HexPoint> oldPattern, Set<HexPoint> newPattern){
//		//defensive copy
//		HashSet<HexPoint> patternsNotBeingRotated=new HashSet<HexPoint>(activePatterns.keySet());
//		//patterns shouldn't collide with themselves
//		patternsNotBeingRotated.removeAll(oldPattern);
//
//		//Keep any colliding points (destructively to patternsNotBeingRotated)
//		patternsNotBeingRotated.retainAll(newPattern);
//		return patternsNotBeingRotated.size()!=0;
//	}
//
//	@Override
//	public Molecule providePattern(SharedSide sides) {
//		//TODO
//		return null;
//	}
//
//	@Override
//	public boolean hasPattern(SharedSide sides) {
//		//TODO
//		return false;
//	}
//
//}
