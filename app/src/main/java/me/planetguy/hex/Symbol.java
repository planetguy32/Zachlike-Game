package me.planetguy.hex;

public enum Symbol {
	
	NOP(' '),
	
	//Coerces the walker's heading to the axis.
	TURN_NE_SW('/'),
	TURN_SE_NW('\\'),
	TURN_E_W('-'),
	
	//Locks/unlocks the walker above/below. Locked walkers don't move or dispatch symbols until unlocked.
	LOCK_UP('('),
	LOCK_DOWN('['),
	UNLOCK_UP(')'),
	UNLOCK_DOWN(']'),
	
	//Molecule manipulation
	GRAB('^'),
	DROP('v'),
	SWAP('X'),
	
	//TODO from here on down - these depend on our multi-region system
	//Pushes the held pattern in the listed direction to wherever the current location maps to. Blocks until matching a pull.
	PUSH_UP('x'),
	PUSH_DOWN('y'),
	//Pulls and, if in a place with a handhold on it, picks up a pattern from its direction. Blocks until matching a push.
	PULL_UP('a'),
	PULL_DOWN('b'),
	CTL1('1'), //implementation-defined functionality
	CTL2('2'),
	CTL3('3'),
	CTL4('4'),
	
	;
	public final char mnemonic;
	Symbol(char mnemonic){
		this.mnemonic=mnemonic;
	}
}
