import java.util.List;

public class Rectangle extends Tetragon {
    private final double width;
    private final double height;

    public Rectangle(List<Point> points) {
        super(points);
        width = points.size() == 4 ? point(0).distanceTo(point(1)) : 0;
        height = points.size() == 4 ? point(1).distanceTo(point(2)) : 0;
    }

    // Стороны параллельны осям координат.
    public Rectangle(Point bottomLeft, double width, double height) {
        super(bottomLeft, new Point(bottomLeft.getX() + width, bottomLeft.getY()),
                new Point(bottomLeft.getX() + width, bottomLeft.getY() + height),
                new Point(bottomLeft.getX(), bottomLeft.getY() + height));
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    @Override
    public boolean checkExistence() {
        if (width <= EPSILON || height <= EPSILON || !super.checkExistence()) {
            return false;
        }
        // У прямоугольника каждый угол прямой: скалярное произведение равно нулю.
        for (int i = 0; i < 4; i++) {
            Point a = point(i);
            Point b = point((i + 1) % 4);
            Point c = point((i + 2) % 4);
            double dot = (a.getX() - b.getX()) * (c.getX() - b.getX())
                    + (a.getY() - b.getY()) * (c.getY() - b.getY());
            if (Math.abs(dot) > EPSILON) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double getArea() {
        requireExisting();
        return width * height;
    }
}
