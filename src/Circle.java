import java.util.List;
import java.util.Objects;

public class Circle extends GeometricShape {
    private final Point center;
    private final double radius;

    public Circle(Point center, double radius) {
        super(List.of()); // У круга нет вершин.
        Objects.requireNonNull(center, "Центр не должен быть null");
        this.center = new Point(center.getX(), center.getY());
        this.radius = radius;
    }

    public Point getCenter() {
        return new Point(center.getX(), center.getY());
    }
    public double getRadius() {
        return radius;
    }

    @Override
    public boolean checkExistence() {
        return Double.isFinite(center.getX()) && Double.isFinite(center.getY())
                && Double.isFinite(radius) && radius > EPSILON;
    }

    @Override
    public double getArea() {
        requireExisting();
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        requireExisting();
        return 2 * Math.PI * radius;
    }

    @Override
    protected boolean contains(Point p) {
        return center.distanceTo(p) <= radius + EPSILON;
    }

    @Override
    public boolean intersects(GeometricShape other) {
        Objects.requireNonNull(other);
        requireExisting();
        other.requireExisting();
        if (other instanceof Circle) {
            Circle circle = (Circle) other;
            return center.distanceTo(circle.center) <= radius + circle.radius + EPSILON;
        }
        if (other.contains(center)) {
            return true;
        }
        for (int i = 0; i < other.vertexCount(); i++) {
            Point a = other.point(i), b = other.point((i + 1) % other.vertexCount());
            double dx = b.getX() - a.getX(), dy = b.getY() - a.getY();
            // Ближайшая к центру точка отрезка, включая его концы.
            double t = ((center.getX() - a.getX()) * dx + (center.getY() - a.getY()) * dy)
                    / (dx * dx + dy * dy);
            t = Math.max(0, Math.min(1, t));
            if (contains(new Point(a.getX() + t * dx, a.getY() + t * dy))) {
                return true;
            }
        }
        return false;
    }
}
