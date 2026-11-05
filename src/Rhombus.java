import java.util.List;

public class Rhombus extends Tetragon {
    private final double horizontalDiagonal;
    private final double verticalDiagonal;

    public Rhombus(List<Point> points) {
        super(points);
        horizontalDiagonal = points.size() == 4 ? point(0).distanceTo(point(2)) : 0;
        verticalDiagonal = points.size() == 4 ? point(1).distanceTo(point(3)) : 0;
    }

    // Диагонали проходят через центр и параллельны осям координат.
    public Rhombus(Point center, double horizontalDiagonal, double verticalDiagonal) {
        super(new Point(center.getX() - horizontalDiagonal / 2, center.getY()),
                new Point(center.getX(), center.getY() - verticalDiagonal / 2),
                new Point(center.getX() + horizontalDiagonal / 2, center.getY()),
                new Point(center.getX(), center.getY() + verticalDiagonal / 2));
        this.horizontalDiagonal = horizontalDiagonal;
        this.verticalDiagonal = verticalDiagonal;
    }

    @Override
    public boolean checkExistence() {
        if (horizontalDiagonal <= EPSILON || verticalDiagonal <= EPSILON || !super.checkExistence()) {
            return false;
        }
        double side = point(0).distanceTo(point(1));
        for (int i = 1; i < 4; i++) {
            if (Math.abs(point(i).distanceTo(point((i + 1) % 4)) - side) > EPSILON) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double getArea() {
        requireExisting();
        return horizontalDiagonal * verticalDiagonal / 2;
    }
}
