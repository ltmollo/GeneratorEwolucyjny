package generator.ics.oop;

import org.junit.jupiter.api.Test;

public class GenotypesCreationTest {
    EffortlessAdjustment mutations = new EffortlessAdjustment(8, 5, 8);

    FullRandomness mutations2 = new FullRandomness ();
    Jungle jungle = new Jungle(20, 20, 10, 10);
    WoodenEquator plant = new WoodenEquator(jungle, new Vector2d(0, 0), new Vector2d(19, 19));

    IAnimalBehaviour behaviour = new FullPredestination(8);
    int[] genotype1 = new int[] {0, 1, 2, 3, 4, 5, 7, 1};
    int[] genotype2 = new int[] {5, 6, 7, 0, 1, 0, 3, 3};
    BlisteringPortal map = new BlisteringPortal(20, 20, 10, 10, 5, plant);
    Animal animal1 = new Animal(new Vector2d(1, 2), GeneDirections.NORTH, genotype1, map, 10, behaviour);
    Animal animal2 = new Animal(new Vector2d(1, 2), GeneDirections.NORTH, genotype2, map, 5, behaviour);

    @Test
    public void EffortlessAdjustment(){
        int[] gens = mutations.createGenotype(animal1, animal2);
        for(int gene : gens){
            System.out.print(gene + ", ");
        }
        System.out.println("NEW TEST");
        int[] gens2 = mutations2.createGenotype(animal1, animal2);
        for(int gene : gens2){
            System.out.print(gene + ", ");
        }
    }
}
