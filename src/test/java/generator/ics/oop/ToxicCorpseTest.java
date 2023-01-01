package generator.ics.oop;

import org.junit.jupiter.api.Test;

public class ToxicCorpseTest {

    ToxicCorpse plants = new ToxicCorpse(new Vector2d(0, 0), new Vector2d(20, 20));

    @Test
    public void placeGrassTest(){
        Vector2d result = plants.placeGrass(true);
        for(int k = 0; k <= 3; k++) {
            for (int i = 0; i <= 20; i++) {
                for (int j = 0; j <= 20; j++) {
                    if(i == 0 && j == 0){
                        continue;
                    }
                    plants.addDeadAnimal(new Vector2d(i, j));
                }
            }
        }
        plants.addDeadAnimal(new Vector2d(0,0));
    }
}
