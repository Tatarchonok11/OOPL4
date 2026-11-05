import java.util.List;

public class Tetragon extends GeometricShape {
    public Tetragon(List<Point> points) {
        super(points);
    }

    public Tetragon(Point p1, Point p2, Point p3, Point p4) {
        this(List.of(p1, p2, p3, p4));
    }

    @Override
    public double getArea() {
        requireExisting();
        // Сумма знаковых площадей работает и для вогнутого четырёхугольника.
        return Math.abs(Point.crossProduct(point(0), point(1), point(2))
                + Point.crossProduct(point(0), point(2), point(3))) / 2.0;
    }

    @Override
    public boolean checkExistence() {
        return vertexCount() == 4 && super.checkExistence();
    }
}
