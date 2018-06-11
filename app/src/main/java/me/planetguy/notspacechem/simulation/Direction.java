package me.planetguy.notspacechem.simulation;

/**
 * Created by planetguy on 6/6/18.
 */
public enum Direction {
    //Directions are given from an origin at the top left corner
    //X is towards the right
    //Y is towards the bottom

    RIGHT(new Point(1, 0)),
    DOWN(new Point(0, 1)),
    LEFT(new Point(-1, 0)),
    UP(new Point(0, -1));

    public final Point vector;

    Direction(Point point) {
        this.vector = point;
    }
}
