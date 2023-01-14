package generator.ics.oop;

public class BlisteringPortal extends AbstractWorldMap {

    public BlisteringPortal(Settings settings, Jungle jungle) {
        super(settings, jungle);
    }

    public int generateNewPosition(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Override
    public Vector2d encounterBoundary(Vector2d position, Animal animal) {
        int x;
        int y;
        do {
            x = generateNewPosition(0, this.settings.mapWidth);
            y = generateNewPosition(0, this.settings.mapHeight);
        }
        while (x == position.x && y == position.y);
        animal.energy -= this.settings.energyForProcreation; // bezpośredni dostęp do pola
        return new Vector2d(x, y);
    }
}
