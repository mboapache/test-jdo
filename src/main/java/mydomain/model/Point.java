package mydomain.model;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType= IdentityType.APPLICATION, detachable="true")
public class Point {

    @PrimaryKey
    private long id;
    private int x;
    private int y;

    public Point() { }

    public Point(long id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
