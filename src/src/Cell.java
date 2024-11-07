package src;

import javax.swing.*;
import java.awt.*;

/**
 *A sejteket reprezentáló osztály amely segítségével létrehozom a szimuláció alapját képező táblát.
 *Egy sejt állapotát tárolja továbbá a konstruktora az alapértelmezett színt fehérre állítja.
 */
public class Cell extends JPanel {
    public int state;
    public Cell(){
        state = 0;
        setBackground(Color.WHITE);
    }

}
