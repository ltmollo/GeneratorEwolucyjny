package generator.ics.oop;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Settings {

    public final int mapWidth;
    public final int mapHeight;
    public final int jungleWidth;
    public final int jungleHeight;
    public final int nbOfGrassAtTheBeginning;
    public final int lengthOfGenotype;
    public final int minMutations;
    public final int maxMutations;
    public final int startEnergy;
    public final int moveEnergy;
    public final int plantEnergy;
    public final int energyForProcreation;
    public final int energyForFull;
    public final int animalsAtTheBeginning;
    public final boolean globe;
    public final boolean woodenEquator;
    public final boolean fullRandomness;
    public final boolean fullPredestination;
    public final boolean saveStatistics;

    public Settings() {
        JSONParser jsonParser = new JSONParser();
        JSONObject data = null;
        try {
            data = (JSONObject) jsonParser.parse(new FileReader("src/main/resources/parameters.json"));
        } catch (IOException e) {
            throw new RuntimeException(e + "Błąd wczytu parametrów wejściowych");
        } catch (ParseException e) {
            throw new RuntimeException(e + "Błąd wczytu parametrów wejśćiowych");
        }
        this.mapWidth = Integer.parseInt(data.get("mapWidth").toString());
        this.mapHeight = Integer.parseInt(data.get("mapHeight").toString());
        this.jungleWidth = Integer.parseInt(data.get("jungleWidth").toString());
        this.jungleHeight = Integer.parseInt(data.get("jungleHeight").toString());
        this.nbOfGrassAtTheBeginning = Integer.parseInt(data.get("nbOfGrassAtTheBeginning").toString());
        this.lengthOfGenotype = Integer.parseInt(data.get("lengthOfGenotype").toString());
        this.minMutations = Integer.parseInt(data.get("minMutations").toString());
        this.maxMutations = Integer.parseInt(data.get("maxMutations").toString());
        this.startEnergy = Integer.parseInt(data.get("startEnergy").toString());
        this.moveEnergy = Integer.parseInt(data.get("moveEnergy").toString());
        this.plantEnergy = Integer.parseInt(data.get("plantEnergy").toString());
        this.energyForProcreation = Integer.parseInt(data.get("energyForProcreation").toString());
        this.energyForFull = Integer.parseInt(data.get("energyForFull").toString());
        this.animalsAtTheBeginning = Integer.parseInt(data.get("animalsAtTheBeginning").toString());
        this.globe = Boolean.parseBoolean(data.get("globe").toString());
        this.woodenEquator = Boolean.parseBoolean(data.get("woodenEquator").toString());
        this.fullRandomness = Boolean.parseBoolean(data.get("fullRandomness").toString());
        this.fullPredestination = Boolean.parseBoolean(data.get("fullPredestination").toString());
        this.saveStatistics = Boolean.parseBoolean(data.get("saveStatistics").toString());

        if (mapWidth < 3 || mapHeight < 2) {
            throw new IllegalArgumentException("Mapa musi mieć większe rozmiary, width >= 3, height >= 2");
        }
        if (jungleWidth > mapWidth || jungleHeight > mapHeight) {
            throw new IllegalArgumentException("Jungla musi być mniejsza od mapy");
        }
        if (nbOfGrassAtTheBeginning < 1) {
            throw new IllegalArgumentException("Musi rosnąć trochę trawy");
        }
        if (lengthOfGenotype < 2) {
            throw new IllegalArgumentException("Genotyp musi być trochę dłuższy");
        }
        if (minMutations < 0 || minMutations > lengthOfGenotype) {
            throw new IllegalArgumentException("Uzgodnij wieklkość minimalnej mutacji");
        }
        if (maxMutations < 0 || maxMutations > lengthOfGenotype) {
            throw new IllegalArgumentException("Uzgodnij wielkość maksymalnej mutacji");
        }
        if (minMutations > maxMutations) {
            throw new IllegalArgumentException("Uzgodnij wieklkości mutacji");
        }
        if (startEnergy < 1) {
            throw new IllegalArgumentException("Podaj większą energię startową");
        }
        if (moveEnergy < 0) {
            throw new IllegalArgumentException("moveEnergy musi być liczbą dodatnią");
        }
        if (plantEnergy < 0) {
            throw new IllegalArgumentException("plantEnergy musi być liczbą dodatnią");
        }
        if (energyForProcreation < 0) {
            throw new IllegalArgumentException("energyForProcreation musi być liczbą dodatnią");
        }
        if (energyForFull < 0) {
            throw new IllegalArgumentException("energyForFull musi być liczbą dodatnią");
        }
        if (energyForFull < energyForProcreation) {
            throw new IllegalArgumentException("Energia rozmnażania musi być większa niż zwierzaka najedzonego");
        }
        if (animalsAtTheBeginning < 1) {
            throw new IllegalArgumentException("Musi być przynajmniej jeden zwierzak");
        }

    }
}
