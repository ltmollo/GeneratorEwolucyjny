package generator.ics.oop;

import java.util.Objects;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public Vector2d add(Vector2d other) {
        int x = other.x + this.x;
        int y = other.y + this.y;
        return new Vector2d(x, y);

    }

    boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d compared = (Vector2d) other;
        return this.x == compared.x && this.y == compared.y;
    }

    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
