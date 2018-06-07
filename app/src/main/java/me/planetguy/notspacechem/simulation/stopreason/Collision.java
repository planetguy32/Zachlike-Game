package me.planetguy.notspacechem.simulation.stopreason;

import me.planetguy.notspacechem.simulation.Point;

/**
 * Created by planetguy on 5/1/18.
 */

public class Collision extends VMTermination {
    public final Point errorPoint;
    public Collision(Point newPoint) {
        super();
        this.errorPoint=newPoint;
    }
}
