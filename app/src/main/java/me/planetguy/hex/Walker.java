//package me.planetguy.hex;
//
//import me.planetguy.hex.math.HexDirection;
//import me.planetguy.hex.math.HexMath;
//import me.planetguy.hex.math.HexPoint;
//import me.planetguy.notspacechem.simulation.Molecule;
//import me.planetguy.notspacechem.simulation.VMTermination;
//
//
////not a test
//public class Walker {
//
//	private final IContext context;
//	private int x;
//	private int y;
//	private HexDirection hdg=HexDirection.E;
//	private int id; //should be unique per context
//	private int worldVerticalSides;
//	private int worldHorizontalSides;
//	public Symbol[][] code;
//	private boolean locked;
//	private boolean lockedNextTick; //makes locking take effect only once all walkers have resolved their moves, so walker turn order is irrelevant
//	private Molecule hand;
//
//	public Walker(IContext context){
//		this.context=context;
//		this.id=context.getNextID();
//		this.worldVerticalSides=context.getXLimit();
//		this.worldHorizontalSides=context.getYLimit();
//		this.x=worldVerticalSides/2;
//		this.y=worldHorizontalSides/2;
//		this.code=makeCode(worldHorizontalSides, worldVerticalSides);
//	}
//
//	public Walker(Walker walker){
//		this(walker.context);
//	}
//
//	private static Symbol[][] makeCode(int x, int y){
//		int xMax=HexMath.getArraySizedFor(x, y).x();
//		int yMax=HexMath.getArraySizedFor(x, y).y();
//		Symbol[][] code= new Symbol[xMax][yMax];
//		for(int i=0; i<code.length; i++){
//			for(int j=0; j<code[i].length; j++) {
//				code[i][j]=Symbol.NOP;
//			}
//		}
//		return code;
//	}
//
//	public void preUpdate(){
//
//	}
//
//	public void update(){
//		//dispatch symbols
//		if(locked)
//			return;
//		goTo(hdg.nextInXFrom(x), hdg.nextInYFrom(y));
//		Symbol current=code[x][y];
//		if(current==Symbol.TURN_NE_SW){
//			turnTo(hdg.alignToAxisOrOpposite(HexDirection.NE));
//		} else if(current==Symbol.TURN_SE_NW){
//			turnTo(hdg.alignToAxisOrOpposite(HexDirection.NW));
//		} else if(current==Symbol.TURN_E_W){
//			turnTo(hdg.alignToAxisOrOpposite(HexDirection.E));
//		} else if(current != Symbol.NOP){
//			context.dispatchSymbol(current, id);
//		}
//	}
//
//	public void postUpdate() {
//		//resolve anything still needing work
//		locked=lockedNextTick;
//	}
//
//	public void turnTo(HexDirection newHdg){
//		if(context.notifyRotation(this, hdg.getDeltaDirectionTo(newHdg)) ){
//			if(hand != null){
//				hand.rotate(hdg, newHdg);
//			}
//			hdg=newHdg;
//		}
//	}
//
//	public int x(){
//		return x;
//	}
//
//	public int y(){
//		return y;
//	}
//
//	public HexPoint getPosition(){
//		return new HexPoint(x(), y());
//	}
//
//	//Returns this walker's ID, which is unique within its set of walkers
//	public int id(){
//		return id;
//	}
//
//	public boolean canBeAt(int x, int y){
//		return HexMath.permitSpace(x, y, worldVerticalSides, worldHorizontalSides);
//	}
//
//	public void goTo(int x, int y){
//		if(context.notifyTranslation(this, x, y)){
//			this.x=x;
//			this.y=y;
//		} else {
//			throw new VMTermination();
//		}
//	}
//
//	public void turn(boolean left){
//		HexDirection[] dirs=HexDirection.values();
//		if(left){
//			turnTo(dirs[(hdg.ordinal()+1)%dirs.length]);
//		} else {
//			turnTo(dirs[(hdg.ordinal()+dirs.length-1)%dirs.length]);
//		}
//	}
//
//	public Symbol getSymbol(int x, int y){
//		return code[x][y];
//	}
//
//	public void setSymbol(int x, int y, Symbol s){
//		code[x][y]=s;
//	}
//
//	public void setLocked(boolean locked){
//		this.lockedNextTick=locked;
//	}
//
//	public Molecule getHand(){
//		return this.hand;
//	}
//
//	public void setHand(Molecule p){
//		if(p!=null) {
//			p.setOwner(this);
//		} else {
//			this.hand.setOwner(null);
//		}
//		this.hand=p;
//	}
//
//}
