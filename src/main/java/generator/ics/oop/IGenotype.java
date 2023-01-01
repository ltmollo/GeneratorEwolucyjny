package generator.ics.oop;

public interface IGenotype {

    int[] mutation(int[] genotype);
    /** Create mutation before creating a new genotype
     */

    int[] createGenotype(Animal animal1, Animal animal2);
    /** Create new genotype
     *
     */

    int[] createRandomGenotype();
    /** Create genotype for the beginning
     *
     */
}
