package me.planetguy.util;

import java.util.Arrays;

public class PDTest {
	
	public static void main(String[] args){
		ProbabilityDistribution<Integer> dist=new ProbabilityDistribution<Integer>(100, new Integer[]
				{0,1,2});
		dist.addElement(3, 50);
		int[] a=new int[4];
		for(int i=0; i<7000; i++) {
			Integer result=dist.get();
			a[result]++;
		}
		System.out.println(Arrays.toString(a));
		//ratio of numbers should be roughly 2000:2000:2000:1000
	}

}
