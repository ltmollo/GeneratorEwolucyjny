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

public class App extends Application{
    private GridPane grid = new GridPane();
    private IWorldMap map;
    private Stage primaryStage;
    private Settings settings;
    private SimulationEngine engine;
    private Jungle jungle;

    private VBox drawObject(Vector2d currentPosition){
        VBox result = null;
        if (this.map.isAnimal(currentPosition)) {
            WorldElement object = this.map.getStrongestAnimal(currentPosition);
            GuiElementBox newElem = new GuiElementBox(object, settings);
            result = newElem.getBox();
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

        label = new Label(this.engine.statisticsToDisplay());
        VBox box = new VBox();
        box.getChildren().addAll(label);
        grid.getRowConstraints().add(new RowConstraints(10));
        grid.add(box, 0, haveToiterateX+1, 20, 1);
        Scene scene = new Scene(grid, (haveToiterateX + 2) * 25.5, (haveToiterateY + 2) * 25.5 + 150);
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
