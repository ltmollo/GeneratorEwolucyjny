package generator.ics.oop;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import java.util.*;

public abstract class AbstractWorldMap implements IPositionChangeObserver{

    private final Vector2d lowerLeftBorder;
    private final Vector2d upperRightBorder;
    private final Jungle jungle;
    private final IPlantGrow plantGrow;

    protected final Settings settings;

    protected final Map<Vector2d, Grass> grassField = new HashMap<>();
    protected final Multimap<Vector2d, Animal> animals = ArrayListMultimap.create();

    private final List<Animal> deadAnimals = new ArrayList<>();

    public AbstractWorldMap(Settings settings, IPlantGrow plantGrow, Jungle jungle){
        this.lowerLeftBorder = new Vector2d(0, 0);
        this.upperRightBorder = new Vector2d(settings.mapWidth-1, settings.mapHeight-1);
        this.jungle = jungle;
        this.plantGrow = plantGrow;
        this.settings = settings;
    }

    public boolean checkIfInBoundaries(Vector2d position) {
        return position.precedes(this.upperRightBorder) && position.follows(this.lowerLeftBorder);
    }

    public boolean place(Animal animal) {
        this.animals.put(animal.getPosition(), animal);
        animal.addObserver(this);
        return true;
    }

    public boolean isGrass(Vector2d position) {
        return this.grassField.get(position) != null ;
    }

    public boolean isAnimal(Vector2d position){
        return this.animals.get(position).size() > 0;
    }

    public Animal[] AnimalsAt(Vector2d position) {
        Animal[] animals = new Animal[this.animals.get(position).size()];
        this.animals.get(position).toArray(animals);
        return animals;
    }


    public Grass GrassAt(Vector2d position){
        return this.grassField.get(position);
    }

    public void positionChanged (Vector2d oldPosition, Vector2d newPosition, Animal animal){
        this.animals.remove(oldPosition, animal);
        this.animals.put(newPosition, animal);
    }

    public void deleteAnimal(Animal animal){
        this.deadAnimals.add(animal);
        this.animals.remove(animal.getPosition(), animal);
    }

    public abstract Vector2d encounterBoundary(Vector2d position, Animal animal);

    public boolean findFreeSpot(int beginningX, int endingX,int beginningY, int endingY){
        for(int x = beginningX; x <= endingX; x++){
            for(int y = beginningY; y <= endingY; y++){
                if(!isGrass(new Vector2d(x, y))){
                    return true;
                }
            }
        }
        return false;
    }


    public boolean canPlaceOnJungle(){
        return findFreeSpot(this.jungle.toLowerLeft.x, this.jungle.toUpperRight.x, this.jungle.toLowerLeft.y, this.jungle.toUpperRight.y);
    }
    public boolean canPlaceOnSteppe(){
        if(findFreeSpot(this.lowerLeftBorder.x, this.jungle.toLowerLeft.x-1, this.lowerLeftBorder.y, this.upperRightBorder.y-1)){
            return true;
        }
        if(findFreeSpot(this.jungle.toLowerLeft.x, this.jungle.toUpperRight.x, this.lowerLeftBorder.y, this.jungle.toLowerLeft.y-1)){
            return true;
        }
        if(findFreeSpot(this.jungle.toLowerLeft.x, this.jungle.toUpperRight.x, this.jungle.toUpperRight.y+1, this.upperRightBorder.y)){
            return true;
        }
        return findFreeSpot(this.jungle.toUpperRight.x+1, this.upperRightBorder.x, this.lowerLeftBorder.y, this.upperRightBorder.y);
    }

    public void initializeTufts(){
        for(int i = 0; i < this.settings.nbOfGrassAtTheBeginning; i++){
            Vector2d possiblePosition = plantGrow.placeGrass(canPlaceOnJungle(), canPlaceOnSteppe());
            if (possiblePosition == null) {
                return;
            }
            if(!isGrass(possiblePosition)){
                this.grassField.put(possiblePosition, new Grass(possiblePosition, this.settings.plantEnergy));
            }
            else{
                if(!settings.woodenEquator){
                    ToxicCorpse currentPlantGrow = (ToxicCorpse) this.plantGrow;
                    currentPlantGrow.increaseIndex();
                }
                i--;
            }
        }
        if(!settings.woodenEquator){
            ToxicCorpse currentPlantGrow = (ToxicCorpse) this.plantGrow;
            currentPlantGrow.restartIndex();
        }
    }

    public Vector2d getLowerLeftBorder(){
        return this.lowerLeftBorder;
    }

    public Vector2d getUpperRightBorder(){
        return this.upperRightBorder;
    }

    public List<Vector2d> getGrassPositions(){
        return this.grassField.keySet().stream().toList();
    }

    public List<Vector2d> getAnimalsPositions(){
        return this.animals.keySet().stream().toList();
    }

    public void grassEaten(Vector2d position){
        this.grassField.remove(position);
    }

    public Animal getStrongestAnimal(Vector2d position){
        int index = 0;
        List<Animal> animalsOnPostion = this.animals.get(position).stream().toList();
        for(int i = 0; i<animalsOnPostion.size(); i++){
            if(compareAnimals(animalsOnPostion.get(index), animalsOnPostion.get(i))){
                index = i;
            }
        }
        return animalsOnPostion.get(index);
    }

    private boolean compareAnimals(Animal animal1, Animal animal2){
        if(animal1.energy != animal2.energy){
            return animal1.energy > animal2.energy;
        }

        if(animal1.daysAlive != animal2.daysAlive){
            return animal1.daysAlive > animal2.daysAlive;
        }

        if(animal1.seed != animal2.seed){
            return animal1.seed > animal2.seed;
        }
        return true;
    }

    public List<Animal> getAnimalsForProcreation(Vector2d position){
        int index = 0;
        List<Animal> animalsOnPosition = this.animals.get(position).stream().toList();
        if(animalsOnPosition.size() < 2){
            return null;
        }
        for(int i = 1; i < animalsOnPosition.size(); i++){
            if(!compareAnimals(animalsOnPosition.get(index), animalsOnPosition.get(i))){
                index = i;
            }
        }
        int secondIndex = 0;
        if(index == 0){
            secondIndex = 1;
        }
        for(int i = 0; i<animalsOnPosition.size(); i++){
            if(i == index){
                continue;
            }

            if(!compareAnimals(animalsOnPosition.get(secondIndex), animalsOnPosition.get(i))){
                secondIndex = i;
            }
        }
        List<Animal> animals = new ArrayList<>();
        animals.add(animalsOnPosition.get(index));
        animals.add(animalsOnPosition.get(secondIndex));
        return animals;
    }

    public List<Animal> getDeadAnimals(){
        return ImmutableList.copyOf(this.deadAnimals);
    }
}
