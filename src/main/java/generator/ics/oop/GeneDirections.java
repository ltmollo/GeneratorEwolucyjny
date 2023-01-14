package generator.ics.oop;

public enum GeneDirections {
    NORTH(new Vector2d(0, 1)),
    NORTH_EAST(new Vector2d(1, 1)),
    EAST(new Vector2d(1, 0)),
    SOUTH_EAST(new Vector2d(1, -1)),
    SOUTH(new Vector2d(0, -1)),
    SOUTH_WEST(new Vector2d(-1, -1)),
    WEST(new Vector2d(-1, 0)),
    NORTH_WEST(new Vector2d(-1, 1));

    public final Vector2d vector2d;

    GeneDirections(Vector2d vector2d) {
        this.vector2d = vector2d;
    }

}
