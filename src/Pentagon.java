import java.util.List;

public class Pentagon extends GeometricShape {
    public Pentagon(List<Point> points) {
        super(points);
    }

    public Pentagon(Point p1, Point p2, Point p3, Point p4, Point p5) {
        this(List.of(p1, p2, p3, p4, p5));
    }

    @Override
    public boolean checkExistence() {
        return vertexCount() == 5 && super.checkExistence();
    }

    @Override
    public double getArea() {
        requireExisting();
        // Формула площади многоугольника по координатам соседних вершин.
        double sum = 0;
        for (int i = 0; i < 5; i++) {
            Point current = point(i);
            Point next = point((i + 1) % 5);
            sum += current.getX() * next.getY() - next.getX() * current.getY();
        }
        return Math.abs(sum) / 2;
    }
}
