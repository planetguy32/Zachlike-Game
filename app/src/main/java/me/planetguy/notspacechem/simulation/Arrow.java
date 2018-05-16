package me.planetguy.notspacechem.simulation;

/**
 * Created by planetguy on 5/1/18.
 */

public enum Arrow {
    UP(0, 1),
    RIGHT(1, 0),
    DOWN(0, -1),
    LEFT(-1, 0);


    public final int DX;
    public final int DY;

    Arrow(int dx, int dy) {
        DX = dx;
        DY = dy;
    }
}
