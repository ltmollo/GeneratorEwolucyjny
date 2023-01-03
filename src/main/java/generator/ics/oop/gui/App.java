package generator.ics.oop.gui;

import generator.ics.oop.*;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Arrays;

public class App extends Application{
    private GridPane grid = new GridPane();
    private AbstractWorldMap map;
    private Stage primaryStage;
    private Settings settings;
    private SimulationEngine engine;
    private Jungle jungle;

    private Animal animalToFollow;

    private VBox drawObject(Vector2d currentPosition){
        VBox result;
        if (this.map.isAnimal(currentPosition)) {
            WorldElement object = this.map.getStrongestAnimal(currentPosition);
            Animal animal = (Animal) object;
            GuiElementBox newElem;
            if(this.engine.getPauseSimulation()){
                newElem = new GuiElementBox(object, settings, Arrays.equals(animal.getGenotype(), this.engine.getDominantGenotype()));  //show one with the dominant genotype
            }else{
                newElem = new GuiElementBox(object, settings, false);   // don't want dominant genotype;
            }
            result = newElem.getBox();

            if(this.engine.getPauseSimulation()){
                result.setOnMouseClicked(event -> {this.animalToFollow = animal;});
            }
        }

        else if(this.map.isGrass(currentPosition)) {
            WorldElement object = this.map.GrassAt(currentPosition);
            GuiElementBox newElem = new GuiElementBox(object, settings, false);
            result = newElem.getBox();
        }
        else{
            result = new VBox(new Label(""));
            if(this.jungle.grassInJungle(currentPosition)) {
                result.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            }else{
                result.setBackground(new Background(new BackgroundFill(Color.rgb(144, 238, 144), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
        return result;
    }

    private void drawMap(){

        AbstractWorldMap myMap = map;
        int haveToiterateY = myMap.getUpperRightBorder().y - myMap.getLowerLeftBorder().y;
        int haveToiterateX = myMap.getUpperRightBorder().x - myMap.getLowerLeftBorder().x;

        Label label;
        for (int i = 0; i <= haveToiterateY; i++) {
            Integer newInt = myMap.getUpperRightBorder().y-i;
            label = new Label(newInt.toString());

            grid.getRowConstraints().add(new RowConstraints(25));
            grid.add(label, 0, i+1);

            GridPane.setHalignment(label, HPos.CENTER);
            for (int k = 0; k < haveToiterateX+1; k++) {

                if (i == 0) {
                    newInt = myMap.getLowerLeftBorder().x + k;
                    label = new Label(newInt.toString());
                    grid.add(label, k+1, 0);
                    grid.getColumnConstraints().add(new ColumnConstraints(25));
                    GridPane.setHalignment(label, HPos.CENTER);
                }
                VBox result = drawObject(new Vector2d(k+myMap.getLowerLeftBorder().x , i+myMap.getLowerLeftBorder().y));
                grid.add(result, k+1, haveToiterateY-i+1);
                GridPane.setHalignment(result, HPos.CENTER);
            }
        }

        label = new Label("x/y");
        grid.getColumnConstraints().add(new ColumnConstraints(25));
        grid.getRowConstraints().add(new RowConstraints(25));
        grid.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.CENTER);

        label = new Label("World Stats:" + "\n" + this.engine.statisticsToDisplay());   // show current statistics
        VBox statsBox = new VBox();
        statsBox.getChildren().addAll(label);
        grid.getRowConstraints().add(new RowConstraints(10));
        grid.add(statsBox, settings.mapWidth+5, 1 , 20, 6);

        if(this.engine.getPauseSimulation()) {
            label = new Label("Continue Simulation");
        }
        else{
            label = new Label("Pause Simulation");
        }

        VBox pauseBox = new VBox();                                                         // pause simulation
        pauseBox.getChildren().addAll(label);
        pauseBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));;
        pauseBox.setOnMouseClicked(event -> {this.engine.pauseSimulation();});
        grid.add(pauseBox, 0, settings.mapHeight+2, 5, 1);

        if(this.animalToFollow != null){                                                // if we follow an animal we display his parameters

            VBox stopFollowBox = new VBox();                                            // stop following an animal
            label = new Label("Stop following");
            stopFollowBox.getChildren().addAll(label);
            stopFollowBox.setOnMouseClicked(event -> {this.animalToFollow = null;});
            stopFollowBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

            VBox infoBox = new VBox();
            label = new Label("Animal stats: " + "\n" + this.animalToFollow.getInfo());
            infoBox.getChildren().addAll(label, stopFollowBox);
            grid.add(infoBox, haveToiterateX+2, 8, 20, 5);


        }
        double width = Math.max((haveToiterateX + 2) * 25.5 + 400, 810);
        double height = Math.max((haveToiterateY + 2) * 25.5 + 40, 450);
        Scene scene = new Scene(grid, width, height);
        this.primaryStage.setScene(scene);


    }

    public void updateMap(){
        grid.getChildren().clear();
        this.grid = new GridPane();
        drawMap();
    }
    @Override                               // You can close app like usually
    public void stop(){
        this.engine.endSimulation();
    }

    public void start(Stage primaryStage) {
        try {
            this.settings = new Settings();
            IGenotype genotype;
            if(settings.fullRandomness) {
                genotype = new FullRandomness(settings);
            } else{
                genotype = new EffortlessAdjustment(settings);
            }
            this.jungle = new Jungle(settings);
            IAnimalBehaviour animalBehaviour;
            if(settings.fullPredestination){
                animalBehaviour = new FullPredestination(settings.lengthOfGenotype);
            }else{
                animalBehaviour = new SomeMadness(settings.lengthOfGenotype);
            }
            IPlantGrow plantGrow;
            if(settings.woodenEquator){
                plantGrow = new WoodenEquator(jungle, new Vector2d(0, 0), new Vector2d(settings.mapWidth-1, settings.mapHeight-1));
            }else {
                plantGrow = new ToxicCorpse(new Vector2d(0, 0), new Vector2d(settings.mapWidth-1, settings.mapHeight-1));
            }
            AbstractWorldMap map;
                if(settings.globe) {
                    map = new Globe(settings, plantGrow, jungle);
                }else{
                    map = new BlisteringPortal(settings, plantGrow, jungle);
                }
                this.map = map;
                this.primaryStage = primaryStage;
                this.engine = new SimulationEngine(animalBehaviour, genotype, plantGrow, map, map,
                this,settings);

                Label label = new Label("Simulation Begin");
                Scene scene = new Scene(label, 400, 400);
                Thread threadEngine = new Thread(engine);
                threadEngine.start();
                primaryStage.setScene(scene);
                drawMap();
                primaryStage.show();
        } catch (IllegalArgumentException exception) {
            System.out.println("ERROR: " + exception.getMessage());
            System.exit(0);
        }
        catch (RuntimeException e){
            System.out.println("ERROR: " + e.getMessage());
            System.exit(0);
        }

    }
}
