package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *A felhasználói felülethez tartozó osztály amelyet a JFrame-ből származtattam.
 */

public class TurmiteFrame extends JFrame {
    private final JTextField delay_setter = new JTextField(6);
    static JTextField png_name = new JTextField(6);

    //new value bemenetek
    public final JTextField data00_nv = new JTextField(5);
    public final JTextField data01_nv = new JTextField(5);
    public final JTextField data10_nv = new JTextField(5);
    public final JTextField data11_nv = new JTextField(5);

    //new state bemenetek
    public final JTextField data00_ns = new JTextField(5);
    public final JTextField data01_ns = new JTextField(5);
    public final JTextField data10_ns = new JTextField(5);
    public final JTextField data11_ns = new JTextField(5);

    //combo box-ok
    //0123
    //LRNU - Left, Rigth, No turn, U turn (180°)
    private final Object[] directions = {"L", "R", "N", "U"};
    public final JComboBox<Object> combo00 = new JComboBox<>(directions);
    public final JComboBox<Object> combo01 = new JComboBox<>(directions);
    public final JComboBox<Object> combo10 = new JComboBox<>(directions);
    public final JComboBox<Object> combo11 = new JComboBox<>(directions);

    final JLabel nv = new JLabel("new value");
    final JLabel ns = new JLabel("new state");
    final JLabel turn = new JLabel("    turn");
    final JLabel delay = new JLabel("               delay:");
    final JLabel name = new JLabel("                name:");
    public static JButton start = new JButton("start");
    public static JButton old = new JButton("previous");
    static LinkedList<TurmiteTable> tables = new LinkedList<>() ;

    /**
     * A GUI konstruktora
     */
    public TurmiteFrame(){
        super("NagyHF_UF_Turmites");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel left = new JPanel();
        JPanel right = new JPanel();
        JPanel startStateInit = new JPanel();

        setLayout(new FlowLayout(FlowLayout.CENTER));

        //bal oldali kezdőállapot beállító
        startStateInit.setLayout(new GridLayout(5,3,20,0));

            startStateInit.add(turn);
            startStateInit.add(nv);
            startStateInit.add(ns);

            startStateInit.add(combo00);
            startStateInit.add(data00_nv);
            startStateInit.add(data00_ns);

            startStateInit.add(combo01);
            startStateInit.add(data01_nv);
            startStateInit.add(data01_ns);

            startStateInit.add(combo10);
            startStateInit.add(data10_nv);
            startStateInit.add(data10_ns);

            startStateInit.add(combo11);
            startStateInit.add(data11_nv);
            startStateInit.add(data11_ns);

        left.add(startStateInit);

        //jobb oldali gombok
        right.setLayout(new GridLayout(3,3,20,10));
            right.add(old);
            right.add(delay);
            right.add(delay_setter);
            right.add(start);
            right.add(TurmiteTable.pause);
            right.add(TurmiteTable.continueButton);
            right.add(TurmiteTable.save);
            right.add(name);
            right.add(png_name);


        //actionlistenerek
        HangyaButtonActionListener hb = new HangyaButtonActionListener();
        start.addActionListener(hb);

        RegiButtonActionListener rb = new RegiButtonActionListener();
        old.addActionListener(rb);

        DelayTextFieldListener d = new DelayTextFieldListener();
        delay_setter.addActionListener(d);

        //make it pretty!
            getContentPane().setBackground(Color.PINK);
            left.setBackground(Color.PINK);
            right.setBackground(Color.PINK);
            startStateInit.setBackground(Color.PINK);
            TurmiteTable.save.setBackground(Color.WHITE);
            TurmiteTable.pause.setBackground(Color.WHITE);
            TurmiteTable.continueButton.setBackground(Color.WHITE);
            start.setBackground(Color.WHITE);
            old.setBackground(Color.WHITE);
            combo00.setBackground(Color.WHITE);
            combo01.setBackground(Color.WHITE);
            combo10.setBackground(Color.WHITE);
            combo11.setBackground(Color.WHITE);
        //it is pretty

        add(left);
        add(right);
        this.pack();

    }
    class HangyaButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                TurmiteTable.turmite = turmInit();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            tableCloser();
        }
    }
    static class RegiButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                TurmiteTable.turmite = regiTurmInit();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
           tableCloser();
        }
    }
    class DelayTextFieldListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            setDelay(Integer.parseInt(delay_setter.getText()));
        }
    }

    /**
     * Inicializálja a hangyát a felhasználó által megadott paraméterek szerint, amelyeket a comboboxok és a textfieldek segítségével állít be.
     * A hangya szabályit tartalmazó mátrixot tölti fel, állapotgépek segítségével. A felhasználói felületen a 4 sor jelenti a "rules" tömb 4 oszlopát, természetesen a megfelelő sorrendben.
     * Így először az irányra/fordulásra vonatkozó sort tölti fel.
     * Majd az újn állapotra vonatkozót, végül pedig a sejt új állapotára vonatkozót.
     * Továbbá elmenti a kapott szabályokat a saveTurmToFile függvény segítségével, amely ahhoz kell majd, hogy képes legyen a program régebbi hanyga visszatöltésére.
     * @return src.Turmite turmite, a kapott hangya
     * @throws IOException
     */
    public Turmite turmInit() throws IOException {
        Turmite turmite = new Turmite();
        switch ((String) combo00.getSelectedItem()){
            case "L":
                turmite.rules[1][0] = 0;
                break;
            case "R":
                turmite.rules[1][0] = 1;
                break;
            case "N":
                turmite.rules[1][0] = 2;
                break;
            case "U":
                turmite.rules[1][0] = 3;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + combo00.getSelectedItem());
        }
        switch ((String) combo01.getSelectedItem()){
            case "L":
                turmite.rules[1][1] = 0;
                break;
            case "R":
                turmite.rules[1][1] = 1;
                break;
            case "N":
                turmite.rules[1][1] = 2;
                break;
            case "U":
                turmite.rules[1][1] = 3;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + combo01.getSelectedItem());
        }
        switch ((String) combo10.getSelectedItem()){
            case "L":
                turmite.rules[1][2] = 0;
                break;
            case "R":
                turmite.rules[1][2] = 1;
                break;
            case "N":
                turmite.rules[1][2] = 2;
                break;
            case "U":
                turmite.rules[1][2] = 3;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + combo10.getSelectedItem());
        }
        switch ((String) combo11.getSelectedItem()){
            case "L":
                turmite.rules[1][3] = 0;
                break;
            case "R":
                turmite.rules[1][3] = 1;
                break;
            case "N":
                turmite.rules[1][3] = 2;
                break;
            case "U":
                turmite.rules[1][3] = 3;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + combo11.getSelectedItem());
        }
        try {
            turmite.rules[2][0] = Integer.parseInt(data00_nv.getText());
            turmite.rules[2][1] = Integer.parseInt(data01_nv.getText());
            turmite.rules[2][2] = Integer.parseInt(data10_nv.getText());
            turmite.rules[2][3] = Integer.parseInt(data11_nv.getText());

            turmite.rules[3][0] = Integer.parseInt(data00_ns.getText());
            turmite.rules[3][1] = Integer.parseInt(data01_ns.getText());
            turmite.rules[3][2] = Integer.parseInt(data10_ns.getText());
            turmite.rules[3][3] = Integer.parseInt(data11_ns.getText());

        }catch (NumberFormatException ignored){}

       saveTurmToFile(turmite);

       return turmite;

    }

    /**
     * Ha a felhasználó az egyel korábbi hanygát szeretné újra szimulálni akkor hívódik meg ez a függvény.
     * Egy txt fájlból olvassa ki az előző hanyga adatait, azonban a kapott mátrixot transzponálni kell.
     * @return src.Turmite turmite, a kapott hanyga
     * @throws FileNotFoundException
     */
    public static Turmite regiTurmInit() throws FileNotFoundException {
        Turmite turmite = new Turmite();
        File f = new File("turmites.txt");
        Scanner sc = new Scanner(f);
        int col_counter = 0;
        while (col_counter < 4 && sc.hasNextLine()){
            String line = sc.nextLine();
            String[] dat = line.split(" ");
            switch (dat[0]) {
                case "L":
                    turmite.rules[1][col_counter] = 0;
                    break;
                case "R":
                    turmite.rules[1][col_counter] = 1;
                    break;
                case "N":
                    turmite.rules[1][col_counter] = 2;
                    break;
                case "U":
                    turmite.rules[1][col_counter] = 3;
                    break;
            }
            int j = 1;
            for(int i = 2; i < 4; i++ ) {
                turmite.rules[i][col_counter] =  Integer.parseInt(dat[j]);
                j++;
            }
            col_counter++;
        }
        return turmite;
    }

    /**
     * Ahhoz, hogy lehessen régebbi hanygát használni el kell menteni az adott hanyga szabályait.
     * A parméterként kapott hanyga szabályait írja ki egy txt fájlba, az első sort kivéve hisz azok minden hangyában ugyanazok, inkább jelölésre szolgálnak.
     * @param turmite
     * @throws IOException
     */
    public void saveTurmToFile(Turmite turmite) throws IOException {
        File f = new File("turmites.txt");
        FileWriter fw = new FileWriter(f);
        PrintWriter pw = new PrintWriter(fw);

        //itt kb transzponálom a mátrixot
        String[][] result = new String[4][3];
        int col_counter = 0; //a turmite rulesban
        int row_counter = 0; //a resultban
        while(col_counter < 4 && row_counter < 4 ) {
            switch (turmite.rules[1][col_counter]) {
                case 0:
                    result[row_counter][0] = "L";
                    break;
                case 1:
                    result[row_counter][0] = "R";
                    break;
                case 2:
                    result[row_counter][0] = "N";
                    break;
                case 3:
                    result[row_counter][0] = "U";
                    break;
            }

            result[row_counter][1] = String.valueOf(turmite.rules[2][col_counter]);
            result[row_counter][2] = String.valueOf(turmite.rules[3][col_counter]);

            row_counter++;
            col_counter++;

        }

       for(int i = 0; i < 4; i++) {
           for (int j = 0; j < 2; j++) {
               pw.print(result[i][j] + " ");
           }
           pw.print(result[i][2]);
           pw.println();
       }
        pw.close();
        fw.close();
    }

    /**
     * Átállítja a szimuláció sebességét.
     * @param delay
     */
    public void setDelay(int delay){
        TurmiteTable.delay = delay;
    }

    /**
     * A TurmiteTable frameket bezáró függvény, mivel azokat a felhasználónak nem kell bezárnia. Hisz a gombok (start, old) megynyomásával kell ennek megtörténnie.
     */
    public static void tableCloser(){
        try {
       /* if(!tables.isEmpty()){
            tables.getLast().setVisible(false);
            tables.getLast().dispatchEvent(new WindowEvent(tables.getLast(), WindowEvent.WINDOW_CLOSING));
            TurmiteTable.CellTable.timer.stop();
            tables.getLast().dispose();
        }*/
        tables.add(new TurmiteTable());
        } catch (InterruptedException ex) {
        throw new RuntimeException(ex);
        }
    }
}
