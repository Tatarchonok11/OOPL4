import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class GeometricShape {
    protected static final double EPSILON = 1e-9;
    private final List<Point> points;

    // Вершины перечисляются в порядке обхода границы.
    public GeometricShape(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        List<Point> copy = new ArrayList<>();
        for (Point p : points) {
            copy.add(new Point(p.getX(), p.getY()));
        }
        return List.copyOf(copy);
    }

    protected Point point(int index) {
        return points.get(index);
    }
    protected int vertexCount() {
        return points.size();
    }
    public abstract double getArea();

    public boolean checkExistence() {
        int n = points.size();
        if (n < 3) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            Point current = points.get(i);
            if (!Double.isFinite(current.getX()) || !Double.isFinite(current.getY())) {
                return false;
            }
            for (int j = i + 1; j < n; j++) {
                if (current.distanceTo(points.get(j)) <= EPSILON) {
                    return false;
                }
            }
            double cross = Point.crossProduct(current, points.get((i + 1) % n), points.get((i + 2) % n));
            if (!Double.isFinite(cross) || Math.abs(cross) <= EPSILON) {
                return false;
            }
        }
        // Несмежные стороны простого многоугольника не пересекаются.
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (j == i + 1 || (i == 0 && j == n - 1)) {
                    continue;
                }
                if (segmentsIntersect(points.get(i), points.get((i + 1) % n),
                        points.get(j), points.get((j + 1) % n))) return false;
            }
        }
        return true;
    }

    protected void requireExisting() {
        if (!checkExistence()) throw new IllegalStateException("Фигура с заданными вершинами не существует");
    }

    public double getPerimeter() {
        requireExisting();
        double result = 0;
        for (int i = 0; i < points.size(); i++) {
            result += points.get(i).distanceTo(points.get((i + 1) % points.size()));
        }
        return result;
    }

    // -1: меньше, 0: равно, 1: больше (с абсолютной погрешностью EPSILON).
    public int compareByArea(GeometricShape other) {
        return compare(getArea(), Objects.requireNonNull(other).getArea());
    }

    public int compareByPerimeter(GeometricShape other) {
        return compare(getPerimeter(), Objects.requireNonNull(other).getPerimeter());
    }

    private static int compare(double a, double b) {
        return Math.abs(a - b) <= EPSILON ? 0 : Double.compare(a, b);
    }

    // Касание и полное вложение тоже считаются пересечением.
    public boolean intersects(GeometricShape other) {
        Objects.requireNonNull(other);
        requireExisting();
        other.requireExisting();
        if (other instanceof Circle) {
            return other.intersects(this);
        }
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < other.points.size(); j++) {
                if (segmentsIntersect(points.get(i), points.get((i + 1) % points.size()),
                        other.points.get(j), other.points.get((j + 1) % other.points.size()))) return true;
            }
        }
        return contains(other.points.get(0)) || other.contains(points.get(0));
    }

    protected boolean contains(Point p) {
        boolean inside = false;
        for (int i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            Point a = points.get(j), b = points.get(i);
            if (onSegment(a, b, p)) {
                return true;
            }
            if ((a.getY() > p.getY()) != (b.getY() > p.getY())) {
                double x = a.getX() + (p.getY() - a.getY()) * (b.getX() - a.getX()) / (b.getY() - a.getY());
                if (p.getX() < x) inside = !inside;
            }
        }
        return inside;
    }

    private static boolean onSegment(Point a, Point b, Point p) {
        return Math.abs(Point.crossProduct(a, b, p)) <= EPSILON
                && p.getX() >= Math.min(a.getX(), b.getX()) - EPSILON
                && p.getX() <= Math.max(a.getX(), b.getX()) + EPSILON
                && p.getY() >= Math.min(a.getY(), b.getY()) - EPSILON
                && p.getY() <= Math.max(a.getY(), b.getY()) + EPSILON;
    }

    private static boolean segmentsIntersect(Point a, Point b, Point c, Point d) {
        return (oppositeSigns(Point.crossProduct(a, b, c), Point.crossProduct(a, b, d))
                && oppositeSigns(Point.crossProduct(c, d, a), Point.crossProduct(c, d, b)))
                || onSegment(a, b, c) || onSegment(a, b, d) || onSegment(c, d, a) || onSegment(c, d, b);
    }

    private static boolean oppositeSigns(double a, double b) {
        return (a > EPSILON && b < -EPSILON) || (a < -EPSILON && b > EPSILON);
    }
}
