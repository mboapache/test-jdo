package mydomain.model;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Convert;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class PCRect {

    @Convert(value = PointToStringConverter.class)
    @Column(name="UPPER_LEFT")
    private Point upperLeft;
    @Convert(value = PointToStringConverter.class)
    @Column(name="LOWER_RIGHT")
    private Point lowerRight;

    public PCRect() {}

    public Point getUpperLeft() {
        return upperLeft;
    }
    public void setUpperLeft(Point upperLeft) {
        this.upperLeft = upperLeft;
    }
    public Point getLowerRight() {
        return lowerRight;
    }
    public void setLowerRight(Point lowerRight) {
        this.lowerRight = lowerRight;
    }
}
