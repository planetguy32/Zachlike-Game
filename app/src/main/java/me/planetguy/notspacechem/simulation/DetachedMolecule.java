package me.planetguy.notspacechem.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by planetguy on 6/6/18.
 */

public class DetachedMolecule {

    public HashMap<Point, Atom> atoms=new HashMap<>();
    public List<Bond> bonds=new ArrayList<>();

    public DetachedMolecule(HashMap<Point, Atom> pointsToMove, List<Bond> bondsToMove) {
        this.atoms=pointsToMove;
        this.bonds=bondsToMove;
    }

    public DetachedMolecule() {
        this.atoms=new HashMap<>();
        this.bonds=new ArrayList<>();
    }

    public boolean equals(DetachedMolecule m){
        //TODO actually check equality
        //This will be tricky - graph equality is worse than polynomial time
        //Maybe some optimizations could be done - eg don't consider cases that pair different
        //   atoms with each other?
        return true;
    }
}
