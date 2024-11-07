package Tests;
import  src.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static src.TurmiteTable.*;
import static src.TurmiteFrame.*;


class TurmiteTableTest {
    TurmiteFrame frame;
    TurmiteTable table = new TurmiteTable();

    TurmiteTableTest() throws InterruptedException {
    }

    @Test
    void get_CellTest() throws InterruptedException {

        Cell c = table.getCell(0,0);
        Assertions.assertEquals(0, c.state);
    }


    @Test
    void rule_0Test()  {


        turmite.rules[0][0] = 0;
        turmite.rules[1][0] = 1; //azaz jobbra
        turmite.rules[2][0] = 1;
        turmite.rules[3][0] = 1;

        turmite.dir = 0;

        Cell c = new Cell();
        table.rule_0(c);
        assertEquals(2, turmite.dir); //jobbra kell nézzen
        assertEquals(1, c.state);
        assertEquals(1, turmite.state);

    }
    @Test
    void stateChangerTest()  {

        turmite.rules[0][2] = 2;
        turmite.rules[1][2] = 0;
        turmite.rules[2][2] = 1;
        turmite.rules[3][2] = 1;

        Cell c = new Cell();
        table.stateChanger(c,2);
        assertEquals(1, c.state);
        assertEquals(1, turmite.state);

    }


    @Test
    void turmiteToPngTest()  {
        frame = new TurmiteFrame();
        table.step();

        turmite.state = 1;
        TurmiteTable.x.add(40);
        TurmiteTable.y.add(40);

        table.step();
        table.step();
        table.step();

        boolean test1 = false;
        boolean test2 = false;

        pause.doClick();

        try {
            table.turmiteToPng("test",image);
        }catch (FileNotFoundException e){
            test1 = true;
        } catch (IOException e) {
            test2 = true;
        }
        assertFalse(test1);
        assertFalse(test2);

    }

    @Test
    void PauseAndContinueButtonTest(){
        pause.doClick();
        assertFalse(running);
        continueButton.doClick();
        assertTrue(running);
    }

}