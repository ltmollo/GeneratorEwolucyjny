package generator.ics.oop;

import generator.ics.oop.gui.App;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationEngine implements Runnable{
    private final IAnimalBehaviour animalBehaviour;
    private final IGenotype genotype;
    private final IPlantGrow plantGrow;
    private final IPositionChangeObserver positionChangeObserver;
    private final AbstractWorldMap worldMap;
    private final App observer;
    private final Settings settings;
    private final List<Animal> animals = new ArrayList<>();
    private final Statistics statistics = new Statistics();
    private boolean pauseSimulation = false;

    private boolean Simulate = true;

    private Vector2d generateNewPosition(){         // generate positions for animals at the beginning
        int x = (int) (Math.random() * this.settings.mapWidth);
        int y = (int) (Math.random() * this.settings.mapHeight);
        return new Vector2d(x, y);
    }

    public SimulationEngine(IAnimalBehaviour animalBehaviour, IGenotype genotype, IPlantGrow plantGrow, IPositionChangeObserver positionChangeObserver,
                            AbstractWorldMap worldMap, App observer, Settings settings){
        this.animalBehaviour = animalBehaviour;
        this.genotype = genotype;
        this.plantGrow = plantGrow;
        this.positionChangeObserver = positionChangeObserver;
        this.worldMap = worldMap;
        this.observer = observer;
        this.settings = settings;

    }

    public void pauseSimulation(){
        this.pauseSimulation = !this.pauseSimulation;
        Platform.runLater(this.observer::updateMap);
        System.out.println(this.pauseSimulation);
    }

    public boolean getPauseSimulation(){
        return this.pauseSimulation;
    }

    public void endSimulation(){
        this.pauseSimulation = false;
        this.Simulate = false;
    }
    private void addAnimalsOnTheMap(){      // Just for initiation
        for(int i=0; i < this.settings.animalsAtTheBeginning; i++){
            Animal animal = new Animal(generateNewPosition(),  generateOrientation(), genotype.createRandomGenotype(), this.worldMap, this.settings.startEnergy, this.animalBehaviour, generateBeginOfGenotype());
            this.animals.add(animal);
            this.worldMap.place(animal);
            this.statistics.updateGenotypePopularity(animal.getGenotype());
        }
    }

    public void initializeTufts(){
        for(int i = 0; i < this.settings.nbOfGrassAtTheBeginning; i++){
            Vector2d possiblePosition = plantGrow.placeGrass(worldMap.canPlaceOnJungle(), worldMap.canPlaceOnSteppe());
            if (possiblePosition == null) {
                return;
            }
            if(!worldMap.isGrass(possiblePosition)){
                worldMap.addTuft(possiblePosition);
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

    private GeneDirections generateOrientation(){
        return GeneDirections.values()[(int) ((Math.random() * 8))];
    }

    private int generateBeginOfGenotype(){
        return (int) ((Math.random() * settings.lengthOfGenotype));
    }

    public String statisticsToDisplay(){
        return this.statistics.getFullStatistics();
    }

    private void updateStatistics(){
        this.statistics.updateDay();
        this.statistics.updateNumberOfAnimals(this.animals.size() + this.worldMap.getDeadAnimals().size());
        if(this.worldMap.grassField.size() < this.settings.mapWidth * this.settings.mapHeight){
            this.statistics.updateNumberOfTufts(this.settings.nbOfGrassAtTheBeginning);
        } else{
            this.statistics.updateNumberOfTufts(this.settings.mapWidth * this.settings.mapHeight - this.worldMap.grassField.size());
        }
        this.statistics.updateAverageAnimalEnergy(this.animals);
        this.statistics.updateAverageAnimalLifeTime(this.worldMap.getDeadAnimals());
        this.statistics.restartFreeSpaces();
        for(int i = 0; i < this.settings.mapWidth; i++){
            for(int j = 0; j < this.settings.mapHeight; j++){
                Vector2d toCheck = new Vector2d(i, j);
                if(!this.worldMap.isAnimal(toCheck) && !this.worldMap.isGrass(toCheck)){
                    this.statistics.addFreeSpace();
                }
            }
        }

    }

    private void removeDeadAnimals(){
        this.animals.forEach(animal -> {
            if(animal.energy <= 0){
                animal.animalDied();
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

    public int[] getDominantGenotype(){
        return this.statistics.getDominantGenotype();
    }

    private void procreateAnimal(List<Animal> parents){
        if(parents.get(1).energy >= this.settings.energyForFull){    // Second animal is the one with higer energy
            Animal animal = new Animal(parents.get(0).getPosition(), generateOrientation(), genotype.createGenotype(parents.get(0), parents.get(1)), this.worldMap, 2*this.settings.energyForProcreation, this.animalBehaviour, generateBeginOfGenotype());
            this.animals.add(animal);
            this.worldMap.place(animal);
            this.statistics.updateGenotypePopularity(animal.getGenotype());
            parents.get(0).seed++;
            parents.get(1).seed++;
            parents.get(0).energy -= settings.energyForProcreation;
            parents.get(1).energy -= settings.energyForProcreation;
            Platform.runLater(this.observer::updateMap);
        }
    }

    private void eatGrass(Vector2d position){
        if(!this.worldMap.isAnimal(position)){
            return;
        }
        Animal animal = this.worldMap.getStrongestAnimal(position);
        animal.energy += this.settings.plantEnergy;
        animal.platsEaten++;
        this.worldMap.grassEaten(position);
    }

    private void waitToContinune(){
        while(this.pauseSimulation){
            Thread.onSpinWait();
        }
    }

    public void run(){

        addAnimalsOnTheMap();
        Platform.runLater(this.observer::updateMap);
        if(settings.saveStatistics) {                       // Create file
            this.statistics.createNewFile();
        }
        try {
            Thread.sleep(300);
            while(animals.size() > 0 && this.Simulate) {
                removeDeadAnimals();
                Thread.sleep(300);
                waitToContinune();
                int nbOfAnimals = this.animals.size();
                for (int i = 0; i < nbOfAnimals; i++) {
                    Animal animal = this.animals.get(i);
                    animal.move();
                    animal.energy -= this.settings.moveEnergy;
                    animal.daysAlive++;
                    waitToContinune();
                }
                Platform.runLater(this.observer::updateMap);

                List<Vector2d> grassList = this.worldMap.getGrassPositions();
                grassList.forEach(this::eatGrass);
                Thread.sleep(300);

                List<Vector2d> animalsList = this.worldMap.getAnimalsPositions();
                animalsList.forEach(animalPositions -> {
                    List<Animal> animalsForProcreation = this.worldMap.getAnimalsForProcreation(animalPositions);
                    if (animalsForProcreation != null) {
                        procreateAnimal(animalsForProcreation);
                    }
                });
                waitToContinune();
                this.initializeTufts();
                updateStatistics();
                if(this.settings.saveStatistics) {
                    statistics.addToFile();                 // update file
                }

            }
            if(this.settings.saveStatistics) {
                statistics.SaveFile();                      // save file after simulation ends
            }
            Platform.exit();
        } catch (InterruptedException e) {
            throw new RuntimeException(e + "Przerwano symulację");
        } catch (IOException e) {
            throw new RuntimeException(e + "Błąd wczytu danych");
        }
    }



}
