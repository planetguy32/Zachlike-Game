package me.planetguy.hex;

//State of a pattern-read operation:
	//Which symbols have been read

//State of a spell once cast:
	//Current position
	//Current heading direction
	//Current selection (entity or block@pos)


//At each origin, a pattern reader starts, in order from top to bottom and left to right.
//Readers go to the nearest part (in order of HexDirections) that has not already been traversed.
//If there is no legal move for a reader, then reading stops and no further effects happen.
//Symbols cannot be re-read even by different readers.
//Readers update in a round-robin order from oldest to newest.


//Simple fireball spell: origin -> orb -> fire

//Instant bridge: origin -> warp_down -> ray -> earth
	//warp_down recommended so it's at foot level

//Ender pearl-like teleport: origin -> bind -> orb -> unbind



//Different effects can hit the same target. Hits are evaluated strongest-to-weakest, and each subsequent hit to the same target takes a 20% penalty relative to the previous hit.

public enum PatternPart {
	
	///Special symbols - these change the parameters of the global spell.
	AMPLIFY,		//you need 90% the number of copies of the pattern to cast the spell. (Normally 20 copies)
	/*?*/LEAK,		//Causes 1% damage to the spellcasting instrument when read. (100% damage -> the instrument needs to be repaired and the current spell fails.)
	
	//Modifiers - They modify the next symbol to be read where they are applicable, and are consumed then.
			//They're set up so that the best boost changes from A to B to C over time
	A_EMPHASIZER,	//Multiplies strength by 150% - un-dilutes things. *=number is effected by the effect power
	A_ELONGATOR,	//multiplies range by 150%, and dilutes the effect proportionally. ^=number can be elongated
	B_EMPHASIZER,	//Multiplies strength by (50+10(number of read symbols))%	Breaks even at 10 symbols wrt !! (150%)
	B_ELONGATOR,	//Multiplies range by (50+10(number of read symbols))%
	C_EMPHASIZER,	//Multiplies strength by (12.5(number of read symbols))%	Breaks even at 20 symbols wrt !! (250%)
	C_ELONGATOR,	//Multiplies range by (12.5(number of read symbols))%
	
	//Region selection options
	ORB,			//An orb is launched from the player at 4^ meters per second (applying gravity), and the sub-effect goes at the reflection angle from where the orb lands. Sub-effect has 100% strength.
	RAY,			//Targets everything in a straight line from the user, extending 20^ blocks. Each block gets the effect at 75% strength.
	CONE,			//Targets things within a 5^-block cone around the heading from the position, producing 8 sub-effects with 60% strength each.
	SPHERE,			//Launches sub-effects out faces of a 1^-block icosahedron of the current position, producing 20 sub-effects with 50% power each.
	WALL,			//Creates its effect in a circle normal to the heading and centered on the position. 10 sub-effects with 70% strength each start equally-spaced with headings away from the center of the circle.
	FLOOD,			//Starting where it lands, flood-fills (nearest, filling up volume) 30^ blocks with the effect. Sub-effects go random directions from where they are flooded to, power is 30% of original.
	JUMPER,			//Targets entities. One sub-effect has 50% power and starts with the entity's heading and position, and another starts with 70% power at the nearest target not already touched by this spell
	
	//Heading modifiers
	REVERSE,		//Turns the heading to be its opposite. 
	UP,				//Sets heading to point straight up
	DOWN,			//Sets heading to point straight down
	LEFT,			//Turns heading 90 degrees left of current, in the horizontal plane
	RIGHT,			//Turns heading 90 degrees right of current, in the horizontal plane
	FLAT,			//Sets heading to be horizontal
	DISPERSE,		//Adds 20^% random variation to heading in each direction.
	
	//Position modifiers
	WARP_UP,		//Goes up 1 block
	WARP_DOWN,		//Goes down 1 block
	WARP,			//Goes forward 1 block
	
	//Effects
	FIRE,			//Sets fire to entities for 5* seconds, and to 40* percent of blocks. 10* percent of blocks get flowing lava.
	ICE,			//Adds a random depth up to 4* on snow blocks, turning snow to ice if buried below 2 solid snow blocks, slows down entities for 8* seconds.
	EARTH,			//Summons magical stone at the target, with hardness and blast resistance 2*. Can reinforce existing summoned magical stone, setting it to:
					//max(my hardness, existing hardness + 50% of my hardness)
	AIR,			//Pushes target in the direction of the current heading, accelerating it by 2* meters per second.
	/*?*/BIND,		//Begins moving the target entity or blocks. Does nothing unless there is a matching unbind effect.
	/*?*/UNBIND,	//Puts down the held area. In case of splits, different sub-effects get each thing (block or entity) being carried; some may get 2
	FLASH,			//Blinds entities in range for 4* seconds
	DELAY,			//Waits for 3* seconds before continuing
	SLICKEN,		//Coats 20* percent of surfaces with frictionless material, and the rest with a low-friction material
	HEAL,			//Restores 3* health to entities, converts cobble->stone (80*%), uncracks cracked stone bricks(80*%), spreads moss(20*%), restores wool (20*%)
	GAS,			//Creates toxic (1 damage/sec) gas in the area, which dissipates after 1 minute or when built over
	
	//Reading controls
	ORIGIN, 		//The pattern readers start with each origin, but fail if ever reaching another origin.
	GATE,			//Molecule readers can move to a gate only from an origin.
	CROSS,			//Molecule reader can arrive here three times, not just once.
	DIE,			//Molecule reader stops immediately
	FORK,			//When pattern reading arrives, it starts two separate readers going out in different directions. 
	PULL,			//Molecule readers go to pull instructions even if there are non-pull instructions that would be otherwise read first because of their angle.
	NOP,			//No special effect
	
	//Targeting logic - TODO they introduce lots of complexity; worth it?
	SELECT,			//Selects the thing currently targeted, for future reference by other instructions
	REQUIRE,		//Limits the effect to only things like the currently selected target
	RESTRICT,		//Limits the effect to anything except the currently selected target
	BY_GROUP,		//Makes selections compare by entity type (player, hostile, neutral, passive, boss) or block material
	BY_TYPE,		//Makes selections compare by entity class (player, pig, zombie, etc) or block type
	BY_INSTANCE,	//Makes selections compare by entity instance (a==b) or block position
	
}
