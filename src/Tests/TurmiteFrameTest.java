package Tests;
import src.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static src.TurmiteFrame.*;

public class TurmiteFrameTest {
    TurmiteFrame frame;

    @BeforeEach
    void setUp()  {
        frame = new TurmiteFrame();
        TurmiteTable.turmite = new Turmite();

        frame.combo00.setSelectedItem("L");
        frame.combo01.setSelectedItem("R");
        frame.combo10.setSelectedItem("N");
        frame.combo11.setSelectedItem("U");

        frame.data00_nv.setText("0");
        frame.data01_nv.setText("0");
        frame.data10_nv.setText("0");
        frame.data11_nv.setText("0");

        frame.data00_ns.setText("1");
        frame.data01_ns.setText("1");
        frame.data10_ns.setText("1");
        frame.data11_ns.setText("1");
    }


    @Test
    void turmInitTest() throws IOException {
        start.doClick();

        int[][] testArray = new int[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++) {
                testArray[i][j] = frame.turmInit().rules[i][j];
            }
        }
        assertArrayEquals(new int[]{0, 1, 2, 3}, testArray[0]);
        assertArrayEquals(new int[]{0, 1, 2, 3}, testArray[1]);
        assertArrayEquals(new int[]{0, 0, 0, 0}, testArray[2]);
        assertArrayEquals(new int[]{1, 1, 1, 1}, testArray[3]);
    }

    @Test
    void regiTurmInitTest() throws IOException {
        Turmite tesTurmite = TurmiteFrame.regiTurmInit();

        Assertions.assertArrayEquals(new int[]{0, 1, 2, 3}, tesTurmite.rules[0]);
        Assertions.assertArrayEquals(new int[]{0, 1, 2, 3}, tesTurmite.rules[1]);
        Assertions.assertArrayEquals(new int[]{0, 0, 0, 0}, tesTurmite.rules[2]);
        Assertions.assertArrayEquals(new int[]{1, 1, 1, 1}, tesTurmite.rules[3]);

    }

    @Test
    void saveTurm_to_FileTest() throws IOException {
        boolean test = false;
        try {
            frame.saveTurmToFile(frame.turmInit());
        }catch (FileNotFoundException e){
            test = true;
        }
        assertFalse(test);
    }

    @Test
    void setDelay() {
        frame.setDelay(73);
        Assertions.assertEquals(73, TurmiteTable.delay);
    }
}