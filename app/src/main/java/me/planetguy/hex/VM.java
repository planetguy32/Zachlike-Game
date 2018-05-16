//package me.planetguy.hex;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import me.planetguy.notspacechem.simulation.VMTermination;
//
//public class VM {
//
//	public List<Walker> walkers=new ArrayList<Walker>();
//
//	public List<Scroll> scrolls=new ArrayList<Scroll>();
//
//	public boolean update(){
//		try{
//			for(Scroll scroll:scrolls){
//				scroll.preStep();
//			}
//			for(Scroll scroll:scrolls){
//				scroll.step();
//			}
//			for(Scroll scroll:scrolls){
//				scroll.postStep();
//			}
//			return true;
//		}catch(VMTermination crash){
//			return false;
//		}
//	}
//
//}
