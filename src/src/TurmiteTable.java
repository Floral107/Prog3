package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static src.TurmiteFrame.tables;


/**
 * A szimuláció táblája amelyet a JFrameből származtattam.
 * Itt jelenik meg a 2D rács amely a sejteket és a hangyát tartalmazza.
 * Ebben az osztályban van definiálva néhány olyan button amely a GUI-ban kap helyet azonban a funkciójukból adódóan itt használom őket.
 */
public class TurmiteTable extends JFrame {
    static final int screen_w = 800;
    static final int screen_h = 800;
    static final int cell_size = 10;
    public static boolean running = true;

    public static int delay = 100;

    public static JButton save = new JButton("save");
    public static JButton pause = new JButton("pause");
    public static JButton continueButton = new JButton("continue");

    static int rows = screen_w / cell_size;
    static  int cols = screen_h / cell_size;
    static final int cells_db = rows * cols;
    /**
     * A hangya pozicióit tároló két lista.
     * X a vízszintes tengelyen, y a függőlegesen.
     */
    public static LinkedList<Integer> x = new LinkedList<>();
    public static LinkedList<Integer> y = new LinkedList<>();

    public static Cell[] cells = new Cell[cells_db];
    public boolean rule_0 = false;
    public boolean rule_1 = false;
    public boolean rule_2 = false;
    public boolean rule_3 = false;

    public static Turmite turmite;
    public static BufferedImage image;


    /**
     * A tábla konstruktora amely egy CellTable-t is létrehoz.
     * @throws InterruptedException
     */
    public TurmiteTable() throws InterruptedException {
        super("Table");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        //x.add(-1);
        //y.add(-1);
        x.add(rows / 2);
        y.add(cols / 2);
        this.setLocation(700,0);
        CellTable cellTable = new CellTable();
        add(cellTable);

        PauseButtonActionListener p = new PauseButtonActionListener();
        pause.addActionListener(p);

        ContinueButtonActionListener c = new ContinueButtonActionListener();
        continueButton.addActionListener(c);

        setVisible(true);
        this.pack();
    }

    /**
     *A JPanel-ből származtatott és az ActionListenert implementáló osztály, amely a tényleges szimulációt végzi.
     */
    public class CellTable extends JPanel implements ActionListener {
        static Timer timer;
        /**
         * Az osztály konstruktora amely feltölti sejtekkel a táblát, továbbá elindítja a timert, és ezzel a szimulációt. A sejteket egy tömben tárolom.
         */
        CellTable() {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setPreferredSize(new Dimension(screen_w, screen_h));
            setLayout(new GridLayout(rows, cols,1 ,1));

            setBackground(Color.PINK);
            SaveButtonActionListener s = new SaveButtonActionListener();
            save.addActionListener(s);

            for (int i = 0; i < cells_db; i++) {
                cells[i] = new Cell();
                this.add(cells[i]);
            }
            running = true;
            if(running) {
                timer = new Timer(delay, this);
                timer.start();
            }

        }

        /**
         * A szimulációt vezérlő függvény, amíg fut a timer addig hívja meg a step-et
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(running) {
                step();
            }
        }
    }

    /**
     * Visszatér a megadott koordináták helyén szereplő sejttel.
     * A "pos" vaygis a sejteket tartalmazó tömbben a keresett sejt indexe, ezt abból adódóan kell a "y * cols + x" képlettel kiszámolni,
     * hoyg a sejtek egy egydimenziós tömbben vannak eltárolva.
     * @param x, a táblán az x azaz vízszintes koordináta
     * @param y,  a táblán az y azaz függőleges koordináta
     * @return Cell, a keresett sejt
     */
    public Cell getCell(int x, int y) {
        int pos = y * cols + x;

        if(pos >= cells_db ){
            return cells[pos - cells_db];
        }
        if(pos < 0){
            return cells[cells_db  + pos];
        }
        return cells[pos];
    }

    /**
     * Eldönti, hogy melyik szabályt használja a program a hangya 4 lehetséges szabályai közül majd meghívja az ahhoz a konkrét szabályhoz tartozó függvényt.
     * Ezt a sejt állapota és a hanyga állapota alapján teszi, mivel ez egy sejtautomata.
     */
    public void step() {
        korrigal();

        Cell current_cell = getCell(x.getLast(), y.getLast());
        rule_0 = false;
        rule_1 = false;
        rule_2 = false;
        rule_3 = false;

        switch (current_cell.state) { 
            case 0:
                switch (turmite.state) {
                    case 0:
                        rule_0 = true;
                        break;
                    case 1:
                        rule_2 = true;
                        break;
                }
            case 1:
                switch (turmite.state) {
                    case 0:
                        rule_1 = true;
                        break;
                    case 1:
                        rule_3 = true;
                        break;
                }
        }
        if (rule_0) {
            rule_0(current_cell);
        } else if (rule_1) {
            rule_1(current_cell);
        } else if (rule_2) {
            rule_2(current_cell);
        } else if (rule_3) {
            rule_3(current_cell);
        }
    }//állapotgépek a különböző szabályokra

    /**
     * A 4 szabályhoz tartozó 4, működési elvében megeggyező, azonban viselkedésben nyilvánvalóan különböző függvény.
     * A lépéseket tertmészetesen a hangyában tárolt szabályok tömb alapján végzik az állapotgépek.
     * A lépések sorrendje a következő:
     *       <li>sejt állapotának megváltoztatása</li>
     *      <li>hanyga saját állapotának megváltoztatása</li>
     *       <li>fordulás</li>
     *       <li>lépés</li>
     *
     * @param c, az aktuáli sejt amelyen a hangya áll
     */
    public void rule_0(Cell c) {
        stateChanger(c,0);
        //fordul
        switch (turmite.dir) {
            //fel
            case 0:
                switch (turmite.rules[1][0]) {
                    //L
                    case 0:
                        turmite.dir = 3;
                        break;
                    //R
                    case 1:
                        turmite.dir = 2;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 1;
                        break;
                }
                break;
            //le
            case 1:
                switch (turmite.rules[1][0]) {
                    //L
                    case 0:
                        turmite.dir = 2;
                        break;
                    //R
                    case 1:
                        turmite.dir = 3;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 0;
                        break;
                }
                break;
            //jobb
            case 2:
                switch (turmite.rules[1][0]) {
                    //L
                    case 0:
                        turmite.dir = 0;
                        break;
                    //R
                    case 1:
                        turmite.dir = 1;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 3;
                        break;
                }
                break;
            //bal
            case 3:
                switch (turmite.rules[1][0]) {
                    //L
                    case 0:
                        turmite.dir = 1;
                        break;
                    //R
                    case 1:
                        turmite.dir = 0;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 2;
                        break;
                }
                break;
        }

        //lép
        switch (turmite.dir) {
            //fel
            case 0:
                x.add(x.getLast());
                y.add(y.getLast() - 1);
                break;
            //le
            case 1:
                x.add(x.getLast());
                y.add(y.getLast() + 1);
                break;
            //jobb
            case 2:
                x.add(x.getLast() + 1);
                y.add(y.getLast());
                break;
            //bal
            case 3:
                x.add(x.getLast() - 1);
                y.add(y.getLast());
                break;
        }
        korrigal();

    }
    public void rule_1(Cell c) {
        stateChanger(c,1);

        //fordul
        switch (turmite.dir) {
            //fel
            case 0:
                switch (turmite.rules[1][1]) {
                    //L
                    case 0:
                        turmite.dir = 3;
                        break;
                    //R
                    case 1:
                        turmite.dir = 2;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 1;
                        break;
                }
                break;
            //le
            case 1:
                switch (turmite.rules[1][1]) {
                    //L
                    case 0:
                        turmite.dir = 2;
                        break;
                    //R
                    case 1:
                        turmite.dir = 3;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 0;
                        break;
                }
                break;
            //jobb
            case 2:
                switch (turmite.rules[1][1]) {
                    //L
                    case 0:
                        turmite.dir = 0;
                        break;
                    //R
                    case 1:
                        turmite.dir = 1;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 3;
                        break;

                }

                break;
            //bal
            case 3:
                switch (turmite.rules[1][1]) {
                    //L
                    case 0:
                        turmite.dir = 1;
                        break;
                    //R
                    case 1:
                        turmite.dir = 0;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 2;
                        break;

                }

                break;
        }

        //lép
        switch (turmite.dir) {
            //fel
            case 0:
                x.add(x.getLast());
                y.add(y.getLast() - 1);
                break;
            //le
            case 1:
                x.add(x.getLast());
                y.add(y.getLast() + 1);
                break;
            //jobb
            case 2:
                x.add(x.getLast() + 1);
                y.add(y.getLast());
                break;
            //bal
            case 3:
                x.add(x.getLast() - 1);
                y.add(y.getLast());
                break;
        }
        korrigal();
    }
    public void rule_2(Cell c) {
        stateChanger(c,2);

        //fordul
        switch (turmite.dir) {
            //fel
            case 0:
                switch (turmite.rules[1][2]) {
                    //L
                    case 0:
                        turmite.dir = 3;
                        break;
                    //R
                    case 1:
                        turmite.dir = 2;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 1;
                        break;
                }
                break;
            //le
            case 1:
                switch (turmite.rules[1][2]) {
                    //L
                    case 0:
                        turmite.dir = 2;
                        break;
                    //R
                    case 1:
                        turmite.dir = 3;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 0;
                        break;

                }
                break;
            //jobb
            case 2:
                switch (turmite.rules[1][2]) {
                    //L
                    case 0:
                        turmite.dir = 0;
                        break;
                    //R
                    case 1:
                        turmite.dir = 1;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 3;
                        break;
                }

                break;
            //bal
            case 3:
                switch (turmite.rules[1][2]) {
                    //L
                    case 0:
                        turmite.dir = 1;
                        break;
                    //R
                    case 1:
                        turmite.dir = 0;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 2;
                        break;
                }
                break;
        }

        //lép
        switch (turmite.dir) {
            //fel
            case 0:
                x.add(x.getLast());
                y.add(y.getLast() - 1);
                break;
            //le
            case 1:
                x.add(x.getLast());
                y.add(y.getLast() + 1);
                break;
            //jobb
            case 2:
                x.add(x.getLast() + 1);
                y.add(y.getLast());
                break;
            //bal
            case 3:
                x.add(x.getLast() - 1);
                y.add(y.getLast());
                break;
        }
        korrigal();
    }
    public void rule_3(Cell c) {
        stateChanger(c,3);

        //fordul
        switch (turmite.dir) {
            //fel
            case 0:
                switch (turmite.rules[1][3]) {
                    //L
                    case 0:
                        turmite.dir = 3;
                        break;
                    //R
                    case 1:
                        turmite.dir = 2;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 1;
                        break;
                }
                break;
            //le
            case 1:
                switch (turmite.rules[1][3]) {
                    //L
                    case 0:
                        turmite.dir = 2;
                        break;
                    //R
                    case 1:
                        turmite.dir = 3;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 0;
                        break;
                }
                break;
            //jobb
            case 2:
                switch (turmite.rules[1][3]) {
                    //L
                    case 0:
                        turmite.dir = 0;
                        break;
                    //R
                    case 1:
                        turmite.dir = 1;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 3;
                        break;
                }
                break;
            //bal
            case 3:
                switch (turmite.rules[1][3]) {
                    //L
                    case 0:
                        turmite.dir = 1;
                        break;
                    //R
                    case 1:
                        turmite.dir = 0;
                        break;
                    //N
                    case 2:
                        break;
                    //U
                    case 3:
                        turmite.dir = 2;
                        break;
                }
                break;
        }

        //lép
        switch (turmite.dir) {
            //fel
            case 0:
                x.add(x.getLast());
                y.add(y.getLast() - 1);
                break;
            //le
            case 1:
                x.add(x.getLast());
                y.add(y.getLast() + 1);
                break;
            //jobb
            case 2:
                x.add(x.getLast() + 1);
                y.add(y.getLast());
                break;
            //bal
            case 3:
                x.add(x.getLast() - 1);
                y.add(y.getLast());
                break;
        }
        korrigal();
    }

    /**
     * Minden szabály elején végrehajtandó függvény amely a sejtek és a hangya állapotait változtatja meg.
     * @param c ,a sejt amin el kell végezni a változtatásokat
     * @param col , a turmite szabáylainak kellő oszlopa
     */
    public void stateChanger(Cell c, int col){
        //cellát átír
        if (turmite.rules[2][col] == 0) {
            c.setBackground(Color.WHITE);
            c.state = 0;
        } else if (turmite.rules[2][col] == 1) {
            c.setBackground(Color.BLACK);
            c.state = 1;
        }

        //saját állapotot átír
        if (turmite.rules[3][col] == 0) {
            turmite.state = 0;
        } else if (turmite.rules[3][col] == 1) {
            turmite.state = 1;
        }
    }
    public void korrigal(){
        if(x.getLast() < 0){
            x.add(rows + x.getLast());
        }
        if(x.getLast() > rows){
            x.add(x.getLast() - rows);
        }
        if(y.getLast() < 0){
            y.add(cols + y.getLast());
        }
        if(y.getLast() > cols){
            y.add(x.getLast() - cols);
        }
    }

    static class PauseButtonActionListener implements ActionListener{
        /**
         * Elkészíti a késöbb lementendő image objektumot, és megállítja szimulációt.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            image = new BufferedImage(screen_w,screen_h,BufferedImage.TYPE_INT_RGB);
            running = false;
        }
    }
    static class ContinueButtonActionListener implements ActionListener{
        /**
         * Folytatja a megállított szimulációt.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) { running = true;}
    }


    class SaveButtonActionListener implements ActionListener{
        /**
         * Lementi az adott TurmiteTable-t egy png fájlba a turmiteToPng függvény meghívásával. Majd bezárja a table-t, és leállítja a szimulációt.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                turmiteToPng(TurmiteFrame.png_name.getText(), image );
                } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if(!tables.isEmpty()){
                tables.getLast().setVisible(false);
                tables.getLast().dispatchEvent(new WindowEvent(tables.getLast(), WindowEvent.WINDOW_CLOSING));
                tables.getLast().dispose();
                TurmiteTable.CellTable.timer.stop();
            }
        }
    }
    /**
     * A szimulációt egy png fájlként elmentő függvény.
     * A GUI-ban meg kell adni a kép nevét is.
     * A képet a "pictures" mappába menti amely a házit is tartalmazó könyvtárban található.
     * @param name, a felhasználó által választott képnév
     * @throws IOException
     */
    public void turmiteToPng(String name, BufferedImage image) throws IOException {
        if(image != null) {
            paintAll(image.getGraphics());
            String file = "pictures/" + name + ".png";
            ImageIO.write(image, "png", new File(file));
        }
    }

}
