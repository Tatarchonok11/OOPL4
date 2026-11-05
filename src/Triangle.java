import java.util.List;

public class Triangle extends GeometricShape {
    public Triangle(List<Point> points) {
        super(points);
    }

    public Triangle(Point p1, Point p2, Point p3) {
        this(List.of(p1, p2, p3));
    }

    @Override
    public double getArea() {
        requireExisting();
        return Math.abs(Point.crossProduct(point(0), point(1), point(2))) / 2.0;
    }

    @Override
    public boolean checkExistence() {
        return vertexCount() == 3 && super.checkExistence();
    }
}
