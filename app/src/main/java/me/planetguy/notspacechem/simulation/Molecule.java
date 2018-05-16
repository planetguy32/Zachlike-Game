package me.planetguy.notspacechem.simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Molecule {
	
	private class AtomGraphNode {
		Point relativeLocation; //relative to the center of this Molecule, at (0,0,0)
		final Atom part;
		List<AtomGraphNode> connections=new ArrayList<>();
		public AtomGraphNode(int x, int y, Atom part){
			this.part=part;
			this.relativeLocation=new Point(x,y);
		}
		public AtomGraphNode(AtomGraphNode o){
			this.part=o.part;
			this.relativeLocation=o.relativeLocation;
		}
		public int hashCode(){
			return relativeLocation.hashCode() ^ part.hashCode();
		}
	}
	
	private WalkerSnapshot holder=null;

	private List<AtomGraphNode> components=new ArrayList<AtomGraphNode>();
	
	public Molecule(){}
	
	public Molecule(Molecule base){
		this.components=new ArrayList<AtomGraphNode>(base.components.size());
		for(AtomGraphNode part:base.components){
			components.add(new AtomGraphNode(part));
		}
	}
	
	public void attach(int newX, int newY, Atom part){
		AtomGraphNode a=new AtomGraphNode(newX, newY, part);
		components.add(a);
	}
	
	public Set<Point> getOccupiedPoints(Point center){
		HashSet<Point> points=new HashSet<>();
		for(AtomGraphNode p:components){
			points.add(center.add(p.relativeLocation));
		}
		return points;
	}
	
	public void setOwner(WalkerSnapshot w){
		//fail in case of trying to grab something already held
		if(holder != null && w != null){
			throw new VMTermination();
		} else {
			holder=w;
		}
	}
	
}
