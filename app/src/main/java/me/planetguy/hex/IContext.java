package me.planetguy.hex;

public interface IContext {
	
	public int getXLimit();
	public int getYLimit(); //dimensions
	
	public void dispatchSymbol(Symbol s, int sendingWalker);
	public boolean notifyRotation(Walker walker, int deltaDirectionTo);
	public boolean notifyTranslation(Walker walker, int x, int y);
	
	//Provide walker IDs
	int getNextID();

}
