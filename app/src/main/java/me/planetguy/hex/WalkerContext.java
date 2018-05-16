package me.planetguy.hex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import me.planetguy.hex.math.HexDirection;
import me.planetguy.hex.math.HexPoint;
import me.planetguy.notspacechem.simulation.Molecule;

public class WalkerContext implements IContext {

	public final List<Walker> walkers=new ArrayList<Walker>();
	
	HashMap<HexPoint, Molecule> activePatterns=new HashMap<HexPoint, Molecule>();
	
	private final int x;
	private final int y;

	public WalkerContext(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	//test case
	public void init(){
		Walker w1=new Walker(this);
		walkers.add(w1);
		w1.goTo(5, 9);
		w1.setSymbol(7,9,Symbol.LOCK_DOWN);
		
		w1.setSymbol(9,9,Symbol.TURN_SE_NW);
		w1.setSymbol(9,10,Symbol.TURN_NE_SW);
		w1.setSymbol(8,11,Symbol.TURN_E_W);
		
		w1.setSymbol(7,11,Symbol.UNLOCK_DOWN);
		
		w1.setSymbol(5,9,Symbol.TURN_E_W);
		w1.setSymbol(4,10,Symbol.TURN_NE_SW);
		w1.setSymbol(4,11,Symbol.TURN_SE_NW);

		
		Walker w2=new Walker(w1);
		
		w2.setSymbol(7,9,Symbol.LOCK_UP);
		
		w2.setSymbol(12,9,Symbol.TURN_SE_NW);
		w2.setSymbol(12,10,Symbol.TURN_NE_SW);
		w2.setSymbol(11,11,Symbol.TURN_E_W);
		
		w2.setSymbol(7,11,Symbol.UNLOCK_UP);
		
		w2.setSymbol(5,9,Symbol.TURN_E_W);
		w2.setSymbol(4,10,Symbol.TURN_NE_SW);
		w2.setSymbol(4,11,Symbol.TURN_SE_NW);
		walkers.add(w2);
		
		w2.goTo(12, 7);
		w2.turnTo(HexDirection.SE);
		
		Molecule p=new Molecule();
		p.attach(0, 0, Part.TEST);
		p.attach(0, 1, Part.TEST);
		p.attach(1, 1, Part.TEST);
		addPattern(10, 9, p);
		
		w2.setSymbol(10, 9, Symbol.GRAB);
	}
	
	public void step(){
		for(Walker w:walkers){
			w.preUpdate();
		}
		for(Walker w:walkers){
			w.update();
		}
		for(Walker w:walkers){
			w.postUpdate();
		}
	}
	
	
	public Molecule getPatternAtPoint(int x, int y){
		return activePatterns.get(new HexPoint(x,y));
	}
	
	@Override
	public int getXLimit() {
		return x;
	}

	@Override
	public int getYLimit() {
		return y;
	}

	@Override
	public void dispatchSymbol(Symbol symbol, int sendingWalker) {
		System.out.println(symbol.mnemonic);
		if(symbol==Symbol.LOCK_UP) {
			walkers.get(0).setLocked(true);
		} else if(symbol==Symbol.LOCK_DOWN) {
			walkers.get(1).setLocked(true);
		} else if(symbol==Symbol.UNLOCK_UP) {
			walkers.get(0).setLocked(false);
		} else if(symbol==Symbol.UNLOCK_DOWN) {
			walkers.get(1).setLocked(false);
		} else if(symbol==Symbol.GRAB){
			Walker walker=walkers.get(sendingWalker);
			walker.setHand(getPatternAtPoint(walker.x(), walker.y()));
		}
	}

	public void addPattern(int x, int y, Molecule p){
		Set<HexPoint> points=p.getOccupiedPoints(new HexPoint(x,y));
		for(HexPoint pt:points){
			activePatterns.put(pt, p);
		}
	}
	
	//Don't permit overlapping patterns
	public boolean notifyRotation(Walker walker, int deltaDirection) {
		//TODO fix this to stop putting points out of the world
		HexPoint center=walker.getPosition();
		return resolveCollision(walker, 
				(HexPoint p) -> { return p.rotate(center, deltaDirection); }
				);
	}

	public boolean notifyTranslation(Walker walker, int x2, int y2) {
		return resolveCollision(walker, 
				(HexPoint p) -> { return p.addX(x2-walker.x()).addY(y2-walker.y());
				});
	}
	
	public boolean resolveCollision(Walker walker, Function<HexPoint, HexPoint> transformation){
		Molecule pattern=walker.getHand();
		if(pattern==null)
			return true;
		HexPoint center=walker.getPosition();
		Set<HexPoint> patternPoints=pattern.getOccupiedPoints(center);
		Set<HexPoint> newPatternPoints=patternPoints.stream().map(transformation)
				.collect(Collectors.toCollection(HashSet::new));
		
		if(!motionCausesCollision(patternPoints, newPatternPoints)){
			//not colliding with anything else
			for(HexPoint pt:patternPoints){
				activePatterns.remove(pt);
			}
			for(HexPoint pt:newPatternPoints){
				activePatterns.put(pt, pattern);
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean motionCausesCollision(Set<HexPoint> oldPattern, Set<HexPoint> newPattern){
		//defensive copy
		HashSet<HexPoint> patternsNotBeingRotated=new HashSet<HexPoint>(activePatterns.keySet());
		//patterns shouldn't collide with themselves
		patternsNotBeingRotated.removeAll(oldPattern);
		
		//Keep any colliding points (destructively to patternsNotBeingRotated)
		patternsNotBeingRotated.retainAll(newPattern);
		return patternsNotBeingRotated.size()!=0;
	}
	
}
