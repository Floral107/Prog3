package src;

/**
 *A Hangyát/Termeszt reprezentáló osztály amely tárolja a hanyga irányát, állapotát és a rá vonatkozó szabályokat.
 * A konstruktorban a szabályok kétdimenziós tömb első sora is inicializálódik hisz ezeket a felhasználó nem változtathatja meg.
 * Az első sor tartalmazza ugyanis az összes lehetőséget amely előfordulhat abból a szempontból, hogy a hangya és a sejt amin áll milyen állapotban vannak.
 * (00 = 0, 01 = 1, 10 = 2, 11 = 3).
 * Így minden oszlop egy különböző szabályt jelent : (állapot-cellaérték)-irány-újérték-újállapot, sorrendben ahogy az a specifikációban is adott.
 * A hangya iránya a következő képpen van tárolva: 0 = fel, 1 = le, 2 = jobb, 3 = bal.
 */
public class Turmite {
    public int dir; //0 = fel, 1 = le, 2 = jobb, 3 = bal
    public int state; //0 v. 1 lehet
    public int[][] rules = new int[4][4];
    public Turmite(){
        state = 0;
        dir = 0;
        rules[0][0] = 0;
        rules[0][1] = 1;
        rules[0][2] = 2;
        rules[0][3] = 3;
    }
}
