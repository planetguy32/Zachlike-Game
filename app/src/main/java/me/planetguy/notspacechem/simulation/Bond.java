package me.planetguy.notspacechem.simulation;

/**
 * Created by planetguy on 6/6/18.
 */
public class Bond {
    public final Point p1;
    public final Point p2;

    public Bond(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Bond(int i, int i1, int i2, int i3) {
        this(new Point(i, i1), new Point(i2, i3));
    }
}
