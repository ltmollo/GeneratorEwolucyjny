package generator.ics.oop;

import generator.ics.oop.gui.App;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SimulationEngine implements Runnable{
    private final IAnimalBehaviour animalBehaviour;
    private final IGenotype genotype;
    private final IPlantGrow plantGrow;
    private final IPositionChangeObserver positionChangeObserver;
    private final IWorldMap worldMap;

    private final App observer;

    private final Settings settings;
    private final List<Animal> animals = new ArrayList<>();

    private Vector2d generateNewPosition(){
        AbstractWorldMap map = (AbstractWorldMap) worldMap;
        int x = (int) (Math.random() * this.settings.mapWidth);
        int y = (int) (Math.random() * this.settings.mapHeight);
        return new Vector2d(x, y);
    }

    public SimulationEngine(IAnimalBehaviour animalBehaviour, IGenotype genotype, IPlantGrow plantGrow, IPositionChangeObserver positionChangeObserver,
                            IWorldMap worldMap, App observer, Settings settings){
        this.animalBehaviour = animalBehaviour;
        this.genotype = genotype;
        this.plantGrow = plantGrow;
        this.positionChangeObserver = positionChangeObserver;
        this.worldMap = worldMap;
        this.observer = observer;
        this.settings = settings;
    }

    private void addAnimalsOnTheMap(){
        for(int i=0; i < this.settings.animalsAtTheBeginning; i++){
            Animal animal = new Animal(generateNewPosition(), GeneDirections.NORTH_EAST, genotype.createRandomGenotype(), this.worldMap, this.settings.startEnergy, this.animalBehaviour);
            this.animals.add(animal);
            this.worldMap.place(animal);
        }
    }

    private void removeDeadAnimals(){
        this.animals.forEach(animal -> {
            if(animal.energy <= 0){
              animal.removeObserver(this.positionChangeObserver);
              this.worldMap.deleteAnimal(animal);
                if(!settings.woodenEquator){
                    ToxicCorpse currentPlantGrow = (ToxicCorpse) this.plantGrow;
                    currentPlantGrow.addDeadAnimal(animal.getPosition());
                }
            }
        });
        for(int i = this.animals.size()-1; i >= 0; i--){
            if(this.animals.get(i).energy <= 0){
                this.animals.remove(i);
            }
        }
        Platform.runLater(this.observer::updateMap);
    }

    private void procreateAnimal(List<Animal> parents){
        if(parents.get(1).energy >= this.settings.energyForFull){    // Second animal is the one with higer energy
            Animal animal = new Animal(parents.get(0).getPosition(), GeneDirections.NORTH_EAST, genotype.createGenotype(parents.get(0), parents.get(1)), this.worldMap, 2*this.settings.energyForProcreation, this.animalBehaviour);
            this.animals.add(animal);
            this.worldMap.place(animal);
            parents.get(0).seed++;
            parents.get(1).seed++;
            parents.get(0).energy -= settings.energyForProcreation;
            parents.get(1).energy -= settings.energyForProcreation;
            Platform.runLater(this.observer::updateMap);
        }
    }

    public void run(){

        addAnimalsOnTheMap();
        Platform.runLater(this.observer::updateMap);
        try {
            Thread.sleep(300);

            while (true){
                removeDeadAnimals();
                Thread.sleep(300);
                int nbOfAnimals = this.animals.size();
                for(int i = 0; i < nbOfAnimals; i++){
                    Animal animal = this.animals.get(i);
                    animal.move();
                    animal.energy -= this.settings.moveEnergy;
                    animal.daysAlive++;
                    Platform.runLater(this.observer::updateMap);
                    Thread.sleep(300);
                }
                List<Vector2d> grassList = this.worldMap.getGrassPositions();
                grassList.forEach(this.worldMap::eatGrass);

                List<Vector2d> animalsList = this.worldMap.getAnimalsPositions();
                animalsList.forEach(animalPositions -> {
                    List<Animal> animalsForProcreation= this.worldMap.getAnimalsForProcreation(animalPositions);
                    if(animalsForProcreation != null){
                        procreateAnimal(animalsForProcreation);
                    }
                });
                this.worldMap.initializeTufts();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e + "Przerwano symulację");
        }
    }



}
