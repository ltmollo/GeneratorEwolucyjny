package generator.ics.oop;

public class WorldElement {
    protected Vector2d position;

    public WorldElement(Vector2d vector2d) {
        this.position = vector2d;
    }

    public Vector2d getPosition() {
        return this.position;
    }

}
