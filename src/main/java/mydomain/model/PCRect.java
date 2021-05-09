package mydomain.model;

public class PCRect {

    private Point upperLeft;
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
