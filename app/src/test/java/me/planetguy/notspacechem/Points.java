package me.planetguy.notspacechem;

import org.junit.Test;

import java.util.HashMap;

import me.planetguy.notspacechem.simulation.Atom;
import me.planetguy.notspacechem.simulation.Board;
import me.planetguy.notspacechem.simulation.BoardSnapshot;
import me.planetguy.notspacechem.simulation.Bond;
import me.planetguy.notspacechem.simulation.DetachedMolecule;
import me.planetguy.notspacechem.simulation.Direction;
import me.planetguy.notspacechem.simulation.Point;
import me.planetguy.notspacechem.simulation.Puzzle;
import me.planetguy.notspacechem.simulation.Symbol;
import me.planetguy.notspacechem.simulation.stopreason.VMTermination;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Points {
    @Test
    public void test_points() throws Exception {
        Point p1=new Point(3,3);
        Point p2=new Point(3,4);
        assertEquals(p1.getDirectionToAdjacent(p2), Direction.UP);
        HashMap<Point, String> t1=new HashMap<>();
        t1.put(p1, "foo");
        assertEquals(t1.get(p1), "foo");
        assertEquals(t1.get(p2), null);
    }

}