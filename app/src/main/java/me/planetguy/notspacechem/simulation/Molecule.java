package me.planetguy.notspacechem.simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Molecule {

	private class Vertex {
		Point relativeLocation; //relative to the center of this Molecule, at (0,0,0)
		final Atom part;
		public Vertex(int x, int y, Atom part){
			this.part=part;
			this.relativeLocation=new Point(x,y);
		}
		public Vertex(Vertex o){
			this.part=o.part;
			this.relativeLocation=o.relativeLocation;
		}
		public int hashCode(){
			return relativeLocation.hashCode() ^ part.hashCode();
		}
	}

	private class Edge {
		private Vertex v1;
		private Vertex v2;
		public Edge(Vertex v1, Vertex v2){

		}
	}
	
	private WalkerSnapshot holder=null;

	private List<Vertex> components=new ArrayList<Vertex>();
	private List<Edge> edges=new ArrayList<Edge>();

	public Molecule(){}
	
	public Molecule(Molecule base){
		this.components=new ArrayList<Vertex>(base.components.size());
		for(Vertex part:base.components){
			components.add(new Vertex(part));
		}
	}
	
	public Set<Point> getOccupiedPoints(Point center){
		HashSet<Point> points=new HashSet<>();
		for(Vertex p:components){
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
