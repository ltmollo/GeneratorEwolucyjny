package generator.ics.oop;

import org.junit.jupiter.api.Test;
import static org.testng.AssertJUnit.*;

public class WoodenEquatorTest {
    Jungle jungle = new Jungle(20, 20, 6, 6);
    WoodenEquator grow = new WoodenEquator(jungle, new Vector2d(0, 0), new Vector2d(19, 19));
    boolean canPlace = true;
    boolean cantPlace = false;
    @Test
    public void placeGrass(){
        Vector2d vector1 = grow.placeGrass(canPlace);
        assertTrue(vector1.follows(jungle.toLowerLeft) && vector1.precedes(jungle.toUpperRight));
        assertFalse(vector1.precedes(jungle.toLowerLeft));
        assertFalse(vector1.follows(jungle.toUpperRight));
        Vector2d vector2 = grow.placeGrass(cantPlace);
        assertFalse(vector2.follows(jungle.toLowerLeft) && vector2.precedes(jungle.toUpperRight));
    }
}
