package me.planetguy.notspacechem.simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by planetguy on 5/1/18.
 */

public class SimStep {

    public final Board board;
    private final int tick;
    public BoardSnapshot snapshot;


    public SimStep(Board board, int tick){
        this.tick=tick;
        this.board=board;
    }

    private SimStep(Board board) {
        this.tick=0;
        this.board = board;
        this.snapshot=new BoardSnapshot(board);
        for(int x=0; x<board.symbols.length; x++){
            for(int y=0; y<board.symbols[x].length; y++){
                for(int color=0; color<board.symbols[x][y].length; color++){
                    if(board.symbols[x][y][color] == Symbol.START){
                        snapshot.walkerPos[color]=new Point(x, y);
                    }
                }
            }
        }
    }

}
