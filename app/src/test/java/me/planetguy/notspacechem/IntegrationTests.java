package me.planetguy.notspacechem;

import org.junit.Test;

import me.planetguy.notspacechem.simulation.Atom;
import me.planetguy.notspacechem.simulation.Board;
import me.planetguy.notspacechem.simulation.BoardSnapshot;
import me.planetguy.notspacechem.simulation.Bond;
import me.planetguy.notspacechem.simulation.DetachedMolecule;
import me.planetguy.notspacechem.simulation.Direction;
import me.planetguy.notspacechem.simulation.Point;
import me.planetguy.notspacechem.simulation.Puzzle;
import me.planetguy.notspacechem.simulation.Symbol;
import me.planetguy.notspacechem.simulation.stopreason.Completion;
import me.planetguy.notspacechem.simulation.stopreason.VMTermination;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class IntegrationTests {

    @Test
    public void test_sc_1(){
        Puzzle puzzle=new Puzzle();
        DetachedMolecule o2=new DetachedMolecule();

        o2.atoms.put(new Point(0, 0), Atom.O);
        o2.atoms.put(new Point(1, 0), Atom.O);
        o2.bonds.add(new Bond(new Point(0, 0), new Point(1, 0)));

        puzzle.inputs[0]=o2;
        puzzle.outputs[0]=o2;

        puzzle.outputsRequired[1]=0;


        Board board=new Board(puzzle);
        board.symbols[3][0][0]= Symbol.START;
        board.details[3][0][0]= Direction.LEFT.ordinal();

        board.symbols[2][0][0]= Symbol.IN;
        board.details[2][0][0]= 0;

        board.symbols[1][0][0]= Symbol.GRAB;
        board.arrows[1][0][0]= Direction.DOWN;

        board.arrows[1][1][0]= Direction.RIGHT;

        board.arrows[7][1][0]= Direction.UP;

        board.symbols[7][0][0]= Symbol.DROP;
        board.arrows[7][0][0]= Direction.LEFT;

        board.symbols[6][0][0]= Symbol.OUT;

        board.symbols[3][0][1]= Symbol.START;
        board.details[3][0][1]=2;

        BoardSnapshot state=new BoardSnapshot(board);
        try{
            while(true)
                state.step();
        }catch(Completion t){
            return;
        }
    }
}