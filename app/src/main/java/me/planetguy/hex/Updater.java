package me.planetguy.hex;

import java.util.concurrent.TimeUnit;

import static me.planetguy.hex.HexTest.*;

public class Updater implements Runnable {

	private VM vm;
	
	public Updater(){
		vm=new VM();
		vm.scrolls.add(HexTest.walkerC);
	}
	
	public void scheduleSelf(){
		HexTest.timer.schedule(this, 100, TimeUnit.MILLISECONDS);
	}

	@Override
	public void run() {
		boolean keepGoing;
		synchronized(renderWorld){
			keepGoing=vm.update();
		}
		HexTest.theRenderer.repaint();
		if(keepGoing)
			scheduleSelf();
	}

}
