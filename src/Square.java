import java.util.List;

public class Square extends Rectangle {
    public Square(List<Point> points) {
        super(points);
    }

    public Square(Point bottomLeft, double side) {
        super(bottomLeft, side, side);
    }

    public double getSide() {
        return getWidth();
    }

    @Override
    public boolean checkExistence() {
        return super.checkExistence() && Math.abs(getWidth() - getHeight()) <= EPSILON;
    }

    @Override
    public double getArea() {
        requireExisting();
        return getSide() * getSide();
    }
}
