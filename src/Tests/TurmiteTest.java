package Tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import src.*;
class TurmiteTest {
    @Test
    void turmiteConstructorTest() {
        Turmite turmite = new Turmite();
        int[][] testR = new int[1][4];
        for (int i = 0; i < 4; i++){
            testR[0][i] = turmite.rules[0][i];
        }
        assertArrayEquals(new int[]{0, 1, 2, 3}, testR[0]);
    }

}