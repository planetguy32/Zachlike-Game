package me.planetguy.notspacechem.simulation;

/**
 * Created by planetguy on 5/1/18.
 */

public class Board {

    public final int X=10;
    public final int Y=8;
    public final int WALKER_COUNT=2;

    public Arrow[][][] arrows=new Arrow[X][Y][WALKER_COUNT];
    public Symbol[][][] symbols=new Symbol[10][8][WALKER_COUNT];

}
