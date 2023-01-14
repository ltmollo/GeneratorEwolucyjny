package generator.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Animal extends WorldElement {
    private final int[] genotype;
    protected GeneDirections orientation;
    protected final int lengthOfGenotype;
    private int latestGene;
    protected int energy;
    private final AbstractWorldMap map;

    private boolean isDead = false;
    protected int seed = 0; // co to?
    protected int daysAlive = 0;

    protected int platsEaten = 0;

    private final IAnimalBehaviour behaviour;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();


    public Animal(Vector2d vector2d, GeneDirections orientation, int[] genotype, AbstractWorldMap map, int energy, IAnimalBehaviour behaviour, int latestGene) {
        super(vector2d);
        this.orientation = orientation;
        this.genotype = genotype;
        this.map = map;
        this.latestGene = latestGene;
        this.lengthOfGenotype = genotype.length;
        this.energy = energy;
        this.behaviour = behaviour;
    }

    public void move() {
        this.latestGene = this.behaviour.nextGen(this.latestGene);
        int newOrientation = (this.orientation.ordinal() + this.genotype[this.latestGene]) % 8;
        this.orientation = GeneDirections.values()[newOrientation];
        Vector2d newPosition = this.position.add(this.orientation.vector2d);
        Vector2d oldPosition = this.position;
        if (!map.checkIfInBoundaries(newPosition)) {
            newPosition = map.encounterBoundary(newPosition, this);
        }
        this.position = newPosition;
        positionChanged(oldPosition, this.position);
    }

    public void animalDied() {
        this.isDead = true;
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observers) {
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }

    public int[] getGenotype() {
        return this.genotype.clone();                   // immutable copy
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getDaysAlive() {
        return this.daysAlive;
    }

    public String toString() {
        return "animal";
    }

    public String getInfo() {
        String result = "";
        result += "Genotype: " + Arrays.toString(this.genotype) + "\n";
        result += "Activated gene: " + this.genotype[latestGene] + "\n";
        result += "Energy: " + Math.max(this.energy, 0) + "\n";
        result += "Eaten plants: " + this.platsEaten + "\n";
        result += "Childern: " + this.seed + "\n";
        if (this.isDead) {
            result += "Died on: " + this.daysAlive + "\n";
        } else {
            result += "Lives: " + this.daysAlive + " days" + "\n";
        }
        return result;
    }
}
