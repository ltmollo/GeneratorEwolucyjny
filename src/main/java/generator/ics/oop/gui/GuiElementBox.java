package generator.ics.oop.gui;
import generator.ics.oop.Animal;
import generator.ics.oop.Settings;
import generator.ics.oop.WorldElement;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private Image image;
    private ImageView imageView;
    private VBox box = new VBox();
    private final Settings settings;

    public VBox getBox(){
        return this.box;
    }

    public GuiElementBox(WorldElement element, Settings settings, boolean isDominant) {
        this.settings = settings;
        if(element.toString().equals("grass")){
            try {
                this.image = new Image(new FileInputStream("src/main/resources/grass.png"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e + "Nie znaleziono pliku ze zdjęciem");
            }
        }
        else{
            Animal animal = (Animal) element;
            if(animal.getEnergy() >= this.settings.energyForFull){
                try {
                    this.image = new Image(new FileInputStream("src/main/resources/animalFull.jpg"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e + "Nie znaleziono pliku ze zdjęciem");
                }
            } else if (animal.getEnergy() >= this.settings.energyForFull * 0.5) {
                try {
                    this.image = new Image(new FileInputStream("src/main/resources/animalHalf.jpg"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e + "Nie znaleziono pliku ze zdjęciem");
                }
            }else{
                try {
                    this.image = new Image(new FileInputStream("src/main/resources/animalLow.jpg"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e + "Nie znaleziono pliku ze zdjęciem");
                }
            }
            if(isDominant){
                try {
                    this.image = new Image(new FileInputStream("src/main/resources/redCow.png"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e + "Nie znaleziono pliku ze zdjęciem");
                }
            }
        }
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(26.2);
        this.imageView.setFitHeight(26.2);
        box.getChildren().addAll((Node) this.imageView);
    }
}
