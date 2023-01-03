package generator.ics.oop;

public class Globe extends AbstractWorldMap{

    public Globe(Settings settings, Jungle jungle){
        super(settings, jungle);
    }

    @Override
    public Vector2d encounterBoundary(Vector2d position, Animal animal) {
        if(position.y == -1 || position.y == this.settings.mapHeight){
            int newOrientation = (animal.orientation.ordinal() + 4)%8;
            animal.orientation = GeneDirections.values()[newOrientation];
            position = new Vector2d(position.x, animal.position.y); //animal y coordinate does not change
        }

        if (position.x == this.settings.mapWidth){
                return new Vector2d(0, position.y);
            }
        if (position.x == -1){
                return new Vector2d(this.settings.mapWidth-1, position.y);
            }

        return position;
    }
}
