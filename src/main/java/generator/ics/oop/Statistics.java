package generator.ics.oop;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Statistics {

    private int day = 0;
    private int numberOfAnimals = 0;
    private int numberOfGrass = 0;
    private int freeSpaces = 0;
    private final HashMap<int[], Integer> genotypePopularity = new HashMap<>();
    private double averageAnimalEnergy = 0;
    private double averageAnimalLifeTime = 0;

    private File file;
    private FileWriter outputfile;

    private CSVWriter writer;

    public void updateDay(){
        this.day++;
    }
    public void updateNumberOfAnimals(int nbOfAnimals){
        this.numberOfAnimals = nbOfAnimals;
    }

    public void updateNumberOfTufts(int newTufts){
        this.numberOfGrass += newTufts;
    }

    public void updateGenotypePopularity(int[] genotype){
        if(this.genotypePopularity.containsKey(genotype)){
            Integer popularity = this.genotypePopularity.get(genotype)+1;
            this.genotypePopularity.put(genotype, popularity);
        }else{
            this.genotypePopularity.put(genotype, 1);
        }
    }

    public void updateAverageAnimalEnergy(List<Animal> animals){
        this.averageAnimalEnergy = 0;
        animals.forEach(animal ->{
            this.averageAnimalEnergy+= animal.getEnergy();
        });
        this.averageAnimalEnergy = this.averageAnimalEnergy/ animals.size();
        this.averageAnimalEnergy = Math.round(this.averageAnimalEnergy*100.0) / 100.0;
        if(averageAnimalEnergy < 0){
            this.averageAnimalEnergy = 0;
        }
    }

    public void updateAverageAnimalLifeTime(List<Animal> animals){
        this.averageAnimalLifeTime = 0;
        animals.forEach(animal -> {
            this.averageAnimalLifeTime += animal.getDaysAlive();
        });
        this.averageAnimalLifeTime = this.averageAnimalLifeTime/ animals.size();
        this.averageAnimalLifeTime = Math.round(this.averageAnimalLifeTime*100.0) / 100.0;
    }

    public void addFreeSpace(){
        this.freeSpaces++;
    }

    public void restartFreeSpaces(){
        this.freeSpaces = 0;
    }

    public int[] getDominantGenotype() {
        return Collections.max(this.genotypePopularity.entrySet(), Map.Entry.comparingByValue()).getKey().clone();
    }

    public String getFullStatistics() {
        String statistics = "Day: " + this.day + "\n";
        statistics += "Born Animals " + this.numberOfAnimals + "\n";
        statistics += "Nb of Grass " + this.numberOfGrass + "\n";
        statistics += "Free spots: " + this.freeSpaces + "\n";
        statistics += "Dominant genotype: " + Arrays.toString(getDominantGenotype()) + "\n";
        statistics += "Average Animal energy: " + this.averageAnimalEnergy + "\n";
        statistics += "Average Animal lifetime: " + this.averageAnimalLifeTime + "\n";;

        return statistics;
    }

    public void createNewFile(){
        this.file = new File("src/main/resources/" + System.currentTimeMillis() + ".csv");
        try {
            this.outputfile = new FileWriter(file);
            this.writer = new CSVWriter(outputfile);
            String[] header = this.getFullStatistics().split("\n");
            writer.writeNext(header);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addToFile(){

        String[] header = this.getFullStatistics().split("\n");
        writer.writeNext(header);
    }

    public void SaveFile() throws IOException {
        this.writer.close();
    }

}
