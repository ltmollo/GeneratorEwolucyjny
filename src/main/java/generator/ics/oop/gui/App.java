package generator.ics.oop.gui;

import generator.ics.oop.*;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;

public class App extends Application{
    private GridPane grid = new GridPane();
    private IWorldMap map;
    private Stage primaryStage;
    private Settings settings;
    private SimulationEngine engine;
    private Jungle jungle;

    private Animal animalToFollow;

    private VBox drawObject(Vector2d currentPosition){
        VBox result = null;
        if (this.map.isAnimal(currentPosition)) {
            WorldElement object = this.map.getStrongestAnimal(currentPosition);
            GuiElementBox newElem = new GuiElementBox(object, settings);
            result = newElem.getBox();
            Animal animal = (Animal) object;
            if(this.engine.getPauseSimulation()){
                result.setOnMouseClicked(event -> {this.animalToFollow = animal;});
            }
        }

        else if(this.map.isGrass(currentPosition)) {
            WorldElement object = this.map.GrassAt(currentPosition);
            GuiElementBox newElem = new GuiElementBox(object, settings);
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

        AbstractWorldMap myMap = (AbstractWorldMap) map;
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

        label = new Label("World Stats:" + "\n" + this.engine.statisticsToDisplay());
        VBox box = new VBox();
        box.getChildren().addAll(label);
        grid.getRowConstraints().add(new RowConstraints(10));
        grid.add(box, haveToiterateX+2, 1 , 20, 6);

        if(this.engine.getPauseSimulation()) {
            label = new Label("Continue Simulation");
        }
        else{
            label = new Label("Pause Simulation");
        }
        VBox pauseBox = new VBox();
        pauseBox.getChildren().addAll(label);
        pauseBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));;
        pauseBox.setOnMouseClicked(event -> {this.engine.pauseSimulation();});
        grid.add(pauseBox, 0, haveToiterateX+2, 5, 2);

        VBox endBox = new VBox();
        label = new Label("End Simulation");
        endBox.getChildren().addAll(label);
        endBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        endBox.setOnMouseClicked(event -> {this.engine.endSimulation();});
        grid.add(endBox, 6, haveToiterateX+2, 5, 2);
        if(this.animalToFollow != null){
            VBox infoBox = new VBox();
            label = new Label("Animal stats: " + "\n" + this.animalToFollow.getInfo());
            infoBox.getChildren().addAll(label);
            grid.add(infoBox, haveToiterateX+2, 8, 20, 5);

            VBox stopFollowBox = new VBox();
            label = new Label("Stop following");
            stopFollowBox.getChildren().addAll(label);
            stopFollowBox.setOnMouseClicked(event -> {this.animalToFollow = null;});
            stopFollowBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
            grid.add(stopFollowBox, 12, haveToiterateX+2, 5, 2);
        }
        Scene scene = new Scene(grid, (haveToiterateX + 2) * 25.5 + + 400, (haveToiterateY + 2) * 25.5 + 40);
        this.primaryStage.setScene(scene);


    }

    public void updateMap(){
        grid.getChildren().clear();
        this.grid = new GridPane();
        drawMap();
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
                plantGrow = new ToxicCorpse(jungle, new Vector2d(0, 0), new Vector2d(settings.mapWidth-1, settings.mapHeight-1));
            }
            AbstractWorldMap map;
                if(settings.globe) {
                    map = new Globe(settings, plantGrow, jungle);
                }else{
                    map = new BlisteringPortal(settings, plantGrow, jungle);
                }
                this.map = map;
                this.primaryStage = primaryStage;
                this.engine = new SimulationEngine(animalBehaviour, genotype, plantGrow, (IPositionChangeObserver) map, map,
                this,settings);

                Label label = new Label("Zwierzak");
                Scene scene = new Scene(label, 400, 400);
                Thread threadEngine = new Thread(engine);
                threadEngine.start();
                primaryStage.setScene(scene);
                drawMap();
                primaryStage.show();
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

    }
}
