package me.planetguy.notspacechem.simulation;

import java.util.List;

/**
 * Created by planetguy on 5/1/18.
 */

public class Board {

    public final int X=10;
    public final int Y=8;
    public final int WALKER_COUNT=2;

    public Direction[][][] arrows=new Direction[X][Y][WALKER_COUNT];
    public Symbol[][][] symbols=new Symbol[X][Y][WALKER_COUNT];
    public int[][][] details=new int[X][Y][WALKER_COUNT];

    public final Puzzle puzzle;
    public Point[] bonders;

    public Board(Puzzle puzzle) {
        this.puzzle=puzzle;
        bonders=new Point[puzzle.bonderCount];
        for(int i=0; i<bonders.length; i++){
            bonders[i]=new Point(i, i%2);
        }
        for(int i=0; i<WALKER_COUNT; i++){
            symbols[0][0][i]=Symbol.START;
        }
    }

}
