package generator.ics.oop;


public class Jungle {
    public final Vector2d toLowerLeft; // czemu "to"?
    public final Vector2d toUpperRight;

    public Jungle(Settings settings) {
        Vector2d center = new Vector2d((int) Math.ceil((double) (settings.mapWidth - 1) / 2), (int) Math.ceil((double) (settings.mapHeight - 1) / 2));
        int minY = center.y - settings.jungleHeight / 2;
        int maxY = minY + settings.jungleHeight - 1;
        int minX = center.x - settings.jungleWidth / 2;
        int maxX = minX + settings.jungleWidth - 1;
        this.toLowerLeft = new Vector2d(minX, minY);
        this.toUpperRight = new Vector2d(maxX, maxY);
    }

    public boolean grassInJungle(Vector2d position) {            // To get different look in gui
        return position.follows(this.toLowerLeft) && position.precedes((this.toUpperRight));
    }
}
