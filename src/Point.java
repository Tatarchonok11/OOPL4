public class Point {
    private double x;
    private double y;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double distanceTo(Point other) {
        return Math.hypot(other.x - x, other.y - y);
    }

    public static double crossProduct(Point origin, Point a, Point b) {
        return (a.x - origin.x) * (b.y - origin.y)
                - (a.y - origin.y) * (b.x - origin.x);
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }
}
