package me.planetguy.notspacechem.simulation;

/**
 * Created by planetguy on 5/1/18.
 */

public class WalkerSnapshot {

    public final int x;
    public final int y;

    private Molecule heldThings;

    public WalkerSnapshot(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
