package generator.ics.oop;

public class AbstractPlantGrow implements IPlantGrow {

    protected final Vector2d lowerLeftBorder;
    protected final Vector2d upperRightBoarder;

    public AbstractPlantGrow(Vector2d lowerLeftBorder, Vector2d upperRightBoarder) {
        this.lowerLeftBorder = lowerLeftBorder;
        this.upperRightBoarder = upperRightBoarder;
    }

    protected boolean placeOnThePrefer() {
        int placeOnPreferPlace = (int) (Math.random() * 101);
        return placeOnPreferPlace > 20;
    }

    public Vector2d placeGrass(boolean canPlaceInJungle, boolean canPlaceInSteppe) {
        int x = generateNewPosition(this.lowerLeftBorder.x, this.upperRightBoarder.x);
        int y = generateNewPosition(this.lowerLeftBorder.y, this.upperRightBoarder.y);
        return new Vector2d(x, y);
    }

    protected int generateNewPosition(int min, int max) {
        return (int) ((Math.random() * (max + 1 - min)) + min);
    }
}
