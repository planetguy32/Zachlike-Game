package me.planetguy.notspacechem.simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by planetguy on 5/1/18.
 */

public class SimStep {

    public final Board board;
    private final int tick;

    private List<WalkerSnapshot> walkers=new ArrayList<>();


    public SimStep(Board board, int tick){
        this.tick=tick;
        this.board=board;
    }

    private SimStep(Board board) {
        this.tick=0;
        this.board = board;
        for(int walkerIndex=0; walkerIndex<board.WALKER_COUNT; walkerIndex++){
            nextWalker:
            for(int x=0; x<board.symbols.length; x++) {
                for (int y = 0; y < board.symbols[0].length; y++) {
                    if (board.symbols[x][y][walkerIndex] == Symbol.START){
                        walkers.add(new WalkerSnapshot(x,y));
                        continue nextWalker;
                    }
                }
            }
        }
    }

    public SimStep advance(SimStep lastStep){
        SimStep result=new SimStep(this.board, this.tick+1);
        result.walkers.clear();
        for(WalkerSnapshot snapshot:walkers){
            //TODO move the walkers
            //TODO check collisions
        }
        return result;
    }

}
