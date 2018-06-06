package me.planetguy.notspacechem.simulation;


public enum Atom {
    H(1, 1),
    HE(2, 0),
    C(6, 4),
    N(7, 5),
    O(8, 2);


    public final int number;
    public final int bonds;
    Atom(int number, int bonds) {
        this.number=number;
        this.bonds=bonds;
    }
}
