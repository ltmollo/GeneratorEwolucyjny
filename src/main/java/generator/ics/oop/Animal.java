package generator.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class Animal extends WorldElement {
    private final int[] genotype;
    protected GeneDirections orientation;
    protected final int lengthOfGenotype;
    private int lastestGene;
    protected int energy;
    private final IWorldMap map;

    protected int seed = 0;
    protected int daysAlive = 0;
    private boolean isDead = false;

    private final IAnimalBehaviour behaviour;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();


    public Animal(Vector2d vector2d, GeneDirections orientation, int[] genotype, IWorldMap map, int energy, IAnimalBehaviour behaviour){
        super(vector2d);
        this.orientation = orientation;
        this.genotype = genotype;
        this.map = map;
        this.lastestGene = -1;
        this.lengthOfGenotype = genotype.length;
        this.energy = energy;
        this.behaviour = behaviour;
    }

    public void move(){
        this.lastestGene = this.behaviour.nextGen(this.lastestGene);
        int newOrientation = (this.orientation.ordinal() + this.genotype[this.lastestGene]) % 8;
        this.orientation = GeneDirections.values()[newOrientation];
        Vector2d newPosition = this.position.add(this.orientation.vector2d);
        Vector2d oldPosition = this.position;
        if(!map.checkIfInBoundaries(newPosition)){
            newPosition = map.encounterBoundary(newPosition, this);
        }
        this.position = newPosition;
        positionChanged(oldPosition, this.position);
    }

    public void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer : this.observers){
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }
    public int[] getGenotype(){
        return this.genotype;
    }
    public int getEnergy() {
        return this.energy;
    }

    public boolean getIsDead(){return this.isDead;}

    public String toString(){
        return "animal";
    }
}
