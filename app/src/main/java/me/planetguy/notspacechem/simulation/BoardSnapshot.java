package me.planetguy.notspacechem.simulation;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import me.planetguy.notspacechem.MainActivity;
import me.planetguy.notspacechem.simulation.stopreason.Collision;
import me.planetguy.notspacechem.simulation.stopreason.Completion;
import me.planetguy.notspacechem.simulation.stopreason.OutOfBounds;
import me.planetguy.notspacechem.simulation.stopreason.WrongOutput;

/**
 * Created by planetguy on 5/1/18.
 */

public class BoardSnapshot {

    public Point[] walkerPos;
    public boolean[] walkerIsCarrying;
    public Direction[] walkerHeadings;

    public int[] outputsCompleted=new int[2];


    public HashMap<Point,Atom> atoms=new HashMap<>();
    public List<Bond> bonds=new ArrayList<>();

    public int cycles=0;

    private final Board board;

    public BoardSnapshot(Board board){
        this.board=board;
        int walkerCount=board.WALKER_COUNT;
        walkerPos=new Point[walkerCount];
        walkerIsCarrying =new boolean[walkerCount];
        walkerHeadings=new Direction[walkerCount];
        for(int i=0; i<walkerHeadings.length; i++){
            initWalker(i);
        }
    }

    public void initWalker(int i){
        for(int x=0; x<board.symbols.length; x++){
            for(int y=0; y<board.symbols[x].length; y++) {
                if(board.symbols[x][y][i]==Symbol.START){
                    walkerHeadings[i]=Direction.values()[board.details[x][y][i]];
                    walkerPos[i]=new Point(x, y);
                }
            }
        }
    }

    public void createBond(Point p1, Point p2){
        Direction d=p1.getDirectionToAdjacent(p2);
        if(d != null){

            Atom a1=atoms.get(p1);
            if(a1==null)
                return;
            int a1FreeSlots=a1.maxBondCount;

            Atom a2=atoms.get(p2);
            if(a2==null)
                return;
            int a2FreeSlots=a2.maxBondCount;

            for(Bond b:bonds){
                //Bond is attached to a1
                if(b.p1.equals(p1) || b.p2.equals(p1)) {
                    a1FreeSlots--;
                }else if(b.p1.equals(p2) || b.p2.equals(p2)){
                    a2FreeSlots--;
                }
            }
            if(a1FreeSlots > 0 && a2FreeSlots > 0)
                bonds.add(new Bond(p1, p2));
        }
    }

    public void destroyBond(Point p1, Point p2) {
        Direction d = p1.getDirectionToAdjacent(p2);
        if (d != null) {
            Iterator<Bond> iter=bonds.iterator();
            while(iter.hasNext()){
                Bond b=iter.next();
                if((b.p1.equals(p1) && b.p2.equals(p2)) || (b.p1.equals(p2) && b.p2.equals(p1))){
                    iter.remove();
                    return;
                }
            }
        }
    }

    private void considerPoint(Point p, List<Point> toCheck, HashMap<Point, Atom> toMove){
        if(toCheck.contains(p))
            return;
        if(toMove.keySet().contains(p))
            return;
        toCheck.add(p);
    }

    public DetachedMolecule cutMolecule(Point origin){
        List<Point> pointsToCheck = new ArrayList<>();
        Set<Bond> bondsToMove=new HashSet<>();
        HashMap<Point, Atom> pointsToMove = new HashMap<>();

        //collect all attached
        pointsToCheck.add(new Point(0,0));
        while (!pointsToCheck.isEmpty()) {
            Point pointToCheck = pointsToCheck.remove(0);
            Atom atom=atoms.get(pointToCheck.add(origin));
            if(atom == null)
                continue;
            pointsToMove.put(pointToCheck, atom);
            for (Bond b : bonds) {
                boolean needToAddBond=false;
                if (b.p1.equals(pointToCheck.add(origin))) {
                    considerPoint(b.p2.subtract(origin), pointsToCheck, pointsToMove);
                    needToAddBond=true;
                }
                if (b.p2.equals(pointToCheck.add(origin))) {
                    considerPoint(b.p1.subtract(origin), pointsToCheck, pointsToMove);
                    needToAddBond=true;
                }
                if(needToAddBond)
                    bondsToMove.add(b);
            }
        }

        List<Bond> bondResults=new ArrayList<>();
        for(Bond bond:bondsToMove){
            bondResults.add(new Bond(bond.p1.subtract(origin), bond.p2.subtract(origin)));
        }

        //Nothing is here
        if(pointsToMove.size() == 0)
            return null;
        DetachedMolecule dm=new DetachedMolecule(pointsToMove, bondResults);

        //Remove from world
        for(Point pt:dm.atoms.keySet())
            atoms.remove(pt.add(origin));
        bonds.removeAll(bondsToMove);
        return dm;
    }

    public void addMolecule(Point base, DetachedMolecule molecule){
        HashMap<Point, Atom> points=molecule.atoms;
        List<Bond> bonds=molecule.bonds;
        for(Point pt:points.keySet()){
            Point newPoint=pt.add(base);
            if(!newPoint.isWithin(0, board.X, 0, board.Y))
                throw new OutOfBounds();
            Atom atom=points.get(pt);
            if(atoms.get(newPoint) != null)
                throw new Collision(newPoint);
            else
                atoms.put(newPoint, atom);
        }

        for(Bond b:bonds){
            this.bonds.add(new Bond(b.p1.add(base), b.p2.add(base)));
        }
    }

    public void step(int walker){
        cycles++;

        Point origin=walkerPos[walker];

        //Change direction
        Direction arrow=board.arrows[origin.x][origin.y][walker];
        if(arrow != null)
            walkerHeadings[walker]=arrow;

        Direction heading=walkerHeadings[walker];

        int detail=board.details[origin.x][origin.y][walker];
        Point base=detail==0 ? new Point(0, 0) : new Point(0, 4);
        Symbol sym=board.symbols[origin.x][origin.y][walker];
        //Java doesn't like nulls in switches
        if(sym!=null)
            switch(board.symbols[origin.x][origin.y][walker]){
                case IN:
                    if(board.puzzle.inputs[detail] != null)
                        addMolecule(base, board.puzzle.inputs[detail]);
                    break;
                case OUT:
                    //If we're carrying, don't do anything.
                    //TODO only output things we're not carrying
                    if(!walkerIsCarrying[walker])
                        for(int dx=0; dx<4; dx++){
                            for(int dy=0; dy<4; dy++){
                                DetachedMolecule mol=cutMolecule(base.add(new Point(dx+6, dy)));
                                //No output is specified, or no molecule is here
                                if(board.puzzle.outputs[detail]==null || mol==null)
                                    continue;
                                //The wrong molecule is provided
                                if(!mol.equals(board.puzzle.outputs[detail])){
                                    throw new WrongOutput();
                                    //The right molecule is provided
                                } else {
                                    outputsCompleted[detail]++;
                                    boolean foundInsufficientQuantity = false;
                                    for(int i=0; i<outputsCompleted.length; i++){
                                        if(outputsCompleted[i]<board.puzzle.outputsRequired[i])
                                            foundInsufficientQuantity=true;
                                    }
                                    if(!foundInsufficientQuantity)
                                        throw new Completion();
                                }
                            }
                        }
                case GRAB:
                    //Only pick up if something is there
                    Atom atom=atoms.get(origin);
                    walkerIsCarrying[walker]=atom != null;
                    break;
                case DROP:
                    walkerIsCarrying[walker]=false;
                    break;
                case BOND:
                    for(Point bonder1:board.bonders){
                        for(Point bonder2:board.bonders){
                            createBond(bonder1, bonder2);
                        }
                    }
                    break;
                case UNBOND:
                    for(Point bonder1:board.bonders){
                        for(Point bonder2:board.bonders){
                            destroyBond(bonder1, bonder2);
                        }
                    }
                    break;
            }

        //Go forwards
        Point proposedPoint=origin.add(heading.vector);

        //But only if we wouldn't go OOB
        if(proposedPoint.isWithin(0, board.X, 0, board.Y)){

            //Carrying anything?
            if(walkerIsCarrying[walker]) {

                DetachedMolecule oldMolecule=cutMolecule(origin);
                //Insert at new positions
                addMolecule(origin.add(heading.vector), oldMolecule);
            }
            walkerPos[walker]=proposedPoint;
        }
    }

    public void step(){
        for(int i=0; i<board.WALKER_COUNT; i++){
            step(i);
        }
    }

    @Override
    public String toString(){
        String result="";
        String[] colorCodes=new String[]{(char)27+"[31m", (char)27+"[34m"};
        for(int i=0; i<board.symbols.length; i++){
            String[] lineParts=new String[]{"", ""};
            String atomLine = "";
            String underline="";
            for(int j=0; j<board.symbols[i].length; j++){
                for(int k=0; k<board.symbols[i][j].length; k++) {
                    lineParts[k]+=colorCodes[k];
                    Symbol s = board.symbols[i][j][k];
                    if (s == null)
                        lineParts[k] += " ";
                    else if (s == Symbol.BOND)
                        lineParts[k] += "B";
                    else if (s == Symbol.GRAB)
                        lineParts[k] += "G";
                    else if (s == Symbol.DROP)
                        lineParts[k] += "D";
                    else if (s == Symbol.IN)
                        lineParts[k] += "I";
                    else if (s == Symbol.OUT)
                        lineParts[k] += "O";
                    else if (s == Symbol.UNBOND)
                        lineParts[k] += "U";
                    Direction arrow = board.arrows[i][j][k];
                    char[] arrows = new char[]{'v', '>', '^', '<'};
                    lineParts[k] += (arrow == null) ? ' ' : arrows[arrow.ordinal()];
                    lineParts[k] += (walkerPos[k].x == i && walkerPos[k].y == j)
                            ? (walkerIsCarrying[k] ? "*" : "0")
                            : " ";
                    lineParts[k] += (char)27+"[0m|";
                }
                Atom atom=atoms.get(new Point(i, j));
                String atomName=atom==null ? "  " : atom.toString();
                while(atomName.length() < 2)
                    atomName+=" ";
                atomLine += atomName +" |";
                underline += "---+";
            }
            for(String s:lineParts)
                result+=s+"\n";
            result+=atomLine+"\n";
            result+=underline+"\n";
        }
        return result;
    }
}
