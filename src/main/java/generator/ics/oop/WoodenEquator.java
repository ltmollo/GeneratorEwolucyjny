package generator.ics.oop;

public class WoodenEquator extends AbstractPlantGrow {
    private final Jungle jungle;

    public WoodenEquator(Jungle jungle, Vector2d lowerLeftBorder, Vector2d upperRightBoarder) {
        super(lowerLeftBorder, upperRightBoarder);
        this.jungle = jungle;

    }

    @Override
    public Vector2d placeGrass(boolean canPlaceInJungle, boolean canPlaceInSteppe) {
        int x;
        int y;
        boolean placeOnThePreferSpot = false;
        if (canPlaceInJungle && canPlaceInSteppe) {


            placeOnThePreferSpot = placeOnThePrefer();
        } else if (canPlaceInJungle) {
            placeOnThePreferSpot = true;
        } else if (!canPlaceInSteppe) {
            return null;
        }
        if (placeOnThePreferSpot) {
            x = generateNewPosition(jungle.toLowerLeft.x, jungle.toUpperRight.x);
            y = generateNewPosition(jungle.toLowerLeft.y, jungle.toUpperRight.y);

            return new Vector2d(x, y);
        } else {
            Vector2d vectorToPlace;
            do {
                vectorToPlace = super.placeGrass(canPlaceInJungle, canPlaceInSteppe);
            } while (vectorToPlace.follows(jungle.toLowerLeft) && vectorToPlace.precedes(jungle.toUpperRight));
            return vectorToPlace;
        }
    }
}