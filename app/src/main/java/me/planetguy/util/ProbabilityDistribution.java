package me.planetguy.util;

import java.util.ArrayList;
import java.util.List;

public class ProbabilityDistribution<T> {

	private int totalWeight=0;
	
	private List<Entry<T>> possibleResults=new ArrayList<Entry<T>>();
	
	public ProbabilityDistribution(){
		
	}
	
	public ProbabilityDistribution(int defaultWeight, T[] things){
		for(T t:things){
			addElement(t, defaultWeight);
		}
	}
	
	public ProbabilityDistribution(int[] weights, T[] things){
		for(int i=0; i<weights.length && i<things.length; i++){
			addElement(things[i], weights[i]);
		}
	}
	
	public void addElement(T t, int weight) {
		Entry<T> entry=new Entry<T>(t, weight);
		possibleResults.add(entry);
		totalWeight+=weight;
	}
	
	public T get(double fraction){
		int pos=(int)(fraction*totalWeight); //from zero to totalWeight, not inclusive
		int idx=0;
		T result = null;
		do{
			Entry<T> entry=possibleResults.get(idx);
			pos-=entry.getWeight();
			result=entry.getResult();
			idx++;
		}while(pos>0);
		return result;
	}
	
	public T get(){
		return get(Math.random());
	}
	
	private class Entry<T> {
		
		private final int weight;
		private final T t;
		
		public Entry(T t, int weight){
			this.weight=weight;
			this.t=t;
		}
		
		public T getResult(){
			return t;
		}
		public int getWeight() {
			return weight;
		}
		public String toString(){
			return "("+weight+"): "+t;
		}
	}
}
