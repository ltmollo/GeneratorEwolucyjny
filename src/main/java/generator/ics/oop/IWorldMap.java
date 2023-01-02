package generator.ics.oop;

import java.util.List;
import java.util.Vector;

public interface IWorldMap {

    // The interface is never used. It is more for me just to know what methods I would like to have
    boolean checkIfInBoundaries(Vector2d position);
    /** Check if animal can move to given position
     * it cannot pass boundaries
     */
    boolean place(Animal animal);

    /** Return true if an animal was placed
     */
    boolean isGrass(Vector2d position);
    /** Check if there is grass at given position,
     */

    boolean isAnimal(Vector2d position);
    /** Check if there are any animals at given position
     */
    Animal[] AnimalsAt(Vector2d position);
    /** Return animals at given position
     */

    Grass GrassAt(Vector2d position);
    /** Return grass at fiven position
     */

    Vector2d encounterBoundary(Vector2d position, Animal animal);
    /** New postion of an Animal depends on an option of the map.
     */

    Animal getStrongestAnimal(Vector2d position);
    /** Return Strongest animal on a given position
     */

    void initializeTufts();

    List<Vector2d> getGrassPositions();

    void grassEaten(Vector2d position);

    public List<Vector2d> getAnimalsPositions();

    public List<Animal> getAnimalsForProcreation(Vector2d position);
    void deleteAnimal(Animal animal);
}
