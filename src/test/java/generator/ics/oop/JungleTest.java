package generator.ics.oop;

import org.junit.jupiter.api.Test;
import static org.testng.AssertJUnit.*;

public class JungleTest {

    int width = 13;
    int height = 7;

    @Test
    public void JungleTest(){
        Jungle jungle1 = new Jungle(width, height, 3, 3);
        assertEquals(jungle1.toLowerLeft, new Vector2d(5, 2));
        assertEquals(jungle1.toUpperRight, new Vector2d(7, 4));

        Jungle jungle2 = new Jungle(width, height, 3, 5);
        assertEquals(jungle2.toLowerLeft, new Vector2d(5, 1));
        assertEquals(jungle2.toUpperRight, new Vector2d(7, 5));

        Jungle jungle3 = new Jungle(6, 5, 2, 1);
        assertEquals(jungle3.toLowerLeft, new Vector2d(2, 2));
        assertEquals(jungle3.toUpperRight, new Vector2d(3, 2));

        Jungle jungle4 = new Jungle(6, 6, 2, 2);
        assertEquals(jungle4.toLowerLeft, new Vector2d(2, 2));
        assertEquals(jungle4.toUpperRight, new Vector2d(3, 3));

    }
}
