package generator.ics.oop;

public class Grass extends WorldElement{

    public final int energy;

    public Grass(Vector2d vector2d, int energy){
        super(vector2d);
        this.energy = energy;
    }

    public String toString(){
        return "grass";
    }
}
