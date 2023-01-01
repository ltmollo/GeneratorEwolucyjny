package generator.ics.oop;

public class WorldElement {
    protected Vector2d position;

    public WorldElement(Vector2d vector2d){
        this.position = vector2d;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public String getLinkToImage(){
        String result;
        switch (this.toString()){
            case("animal") -> result = "src/main/resources/animal.jpg";
            case("grass") -> result = "src/main/resources/grass.png";
            default -> result = null;
        }
        return result;
    }

}
