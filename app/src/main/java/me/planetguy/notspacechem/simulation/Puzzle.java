package me.planetguy.notspacechem.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by planetguy on 6/6/18.
 */

public class Puzzle {

    public DetachedMolecule[] inputs=new DetachedMolecule[2];
    public DetachedMolecule[] outputs=new DetachedMolecule[2];
    public int[] outputsRequired=new int[2];

    public int bonderCount=8;

    public Puzzle(){
        for(int i=0; i<2; i++){
            outputsRequired[i]=10;
        }
    }

}
