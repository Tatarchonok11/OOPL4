import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<GeometricShape> shapes = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Лабораторная работа №4. Геометрические фигуры");
        try {
            boolean running = true;
            while (running) {
                System.out.println("\n1. Добавить фигуру");
                System.out.println("2. Показать все фигуры");
                System.out.println("3. Площадь и периметр фигуры");
                System.out.println("4. Сравнить по площади");
                System.out.println("5. Сравнить по периметру");
                System.out.println("6. Проверить пересечение");
                System.out.println("7. Удалить фигуру");
                System.out.println("0. Выход");
                int choice = readInt("Выберите действие: ", 0, 7);
                switch (choice) {
                    case 1:
                        addShape();
                        break;
                    case 2:
                        showShapes();
                        break;
                    case 3:
                        if (hasShapes()) {
                            GeometricShape shape = selectShape("Номер фигуры: ");
                            System.out.printf("Площадь: %.4f; периметр: %.4f%n",
                                    shape.getArea(), shape.getPerimeter());
                        }
                        break;
                    case 4:
                    case 5:
                    case 6:
                        compareShapes(choice);
                        break;
                    case 7:
                        if (hasShapes()) {
                            showShapes();
                            int index = readInt("Номер удаляемой фигуры: ", 1, shapes.size());
                            shapes.remove(index - 1);
                            System.out.println("Фигура удалена.");
                        }
                        break;
                    case 0:
                        running = false;
                        break;
                }
            }
        } catch (NoSuchElementException e) {
            // Завершение ввода в консоли или во входном файле.
            System.out.println("\nВвод завершён.");
        }
        System.out.println("Программа завершена.");
    }

    private static void addShape() {
        System.out.println("\n1. Треугольник\n2. Четырёхугольник\n3. Прямоугольник");
        System.out.println("4. Квадрат\n5. Ромб\n6. Пятиугольник\n7. Круг\n0. Назад");
        int type = readInt("Выберите фигуру: ", 0, 7);
        if (type == 0) {
            return;
        }

        GeometricShape shape;
        if (type == 7) {
            System.out.println("Введите центр круга:");
            Point center = readPoint();
            double radius = readDouble("Радиус: ");
            shape = new Circle(center, radius);
        } else {
            int count = type == 1 ? 3 : 4;
            if (type == 6) {
                count = 5;
            }
            List<Point> points = new ArrayList<>();
            System.out.println("Введите вершины последовательно по границе фигуры.");
            for (int i = 0; i < count; i++) {
                System.out.println("Вершина " + (i + 1) + ":");
                points.add(readPoint());
            }
            // Каждый конструктор передаёт список вершин родителю через super(points).
            switch (type) {
                case 1:
                    shape = new Triangle(points);
                    break;
                case 2:
                    shape = new Tetragon(points);
                    break;
                case 3:
                    shape = new Rectangle(points);
                    break;
                case 4:
                    shape = new Square(points);
                    break;
                case 5:
                    shape = new Rhombus(points);
                    break;
                default:
                    shape = new Pentagon(points);
                    break;
            }
        }
        if (shape.checkExistence()) {
            shapes.add(shape);
            System.out.println("Фигура добавлена под номером " + shapes.size() + ".");
        } else {
            System.out.println("Фигура с такими параметрами не существует. Она не добавлена.");
        }
    }

    private static boolean hasShapes() {
        if (shapes.isEmpty()) {
            System.out.println("Список пуст. Сначала добавьте фигуру.");
            return false;
        }
        return true;
    }

    private static void showShapes() {
        if (!hasShapes()) {
            return;
        }
        for (int i = 0; i < shapes.size(); i++) {
            GeometricShape shape = shapes.get(i);
            System.out.print((i + 1) + ". " + shapeName(shape));
            if (shape instanceof Circle) {
                Circle circle = (Circle) shape;
                System.out.println(": центр " + circle.getCenter() + ", радиус " + circle.getRadius());
            } else {
                System.out.println(": " + shape.getPoints());
            }
        }
    }

    private static String shapeName(GeometricShape shape) {
        // Квадрат проверяется раньше прямоугольника, так как наследуется от него.
        if (shape instanceof Square) return "Квадрат";
        if (shape instanceof Rectangle) return "Прямоугольник";
        if (shape instanceof Rhombus) return "Ромб";
        if (shape instanceof Pentagon) return "Пятиугольник";
        if (shape instanceof Circle) return "Круг";
        if (shape instanceof Triangle) return "Треугольник";
        return "Четырёхугольник";
    }

    private static GeometricShape selectShape(String prompt) {
        showShapes();
        int number = readInt(prompt, 1, shapes.size());
        return shapes.get(number - 1);
    }

    private static void compareShapes(int action) {
        if (!hasShapes()) {
            return;
        }
        GeometricShape first = selectShape("Номер первой фигуры: ");
        GeometricShape second = selectShape("Номер второй фигуры: ");
        if (action == 6) {
            if (first.intersects(second)) {
                System.out.println("Фигуры пересекаются (касание и вложение учитываются).");
            } else {
                System.out.println("Фигуры не пересекаются.");
            }
            return;
        }
        int result;
        if (action == 4) {
            result = first.compareByArea(second);
            System.out.println("Сравнение площадей:");
        } else {
            result = first.compareByPerimeter(second);
            System.out.println("Сравнение периметров:");
        }
        if (result < 0) {
            System.out.println("Первая фигура меньше второй.");
        } else if (result > 0) {
            System.out.println("Первая фигура больше второй.");
        } else {
            System.out.println("Значения равны.");
        }
    }

    private static Point readPoint() {
        double x = readDouble("x: ");
        double y = readDouble("y: ");
        return new Point(x, y);
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // После ошибки повторяем запрос.
            }
            System.out.println("Введите целое число от " + min + " до " + max + ".");
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim().replace(',', '.');
                double value = Double.parseDouble(input);
                if (Double.isFinite(value)) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // После ошибки повторяем запрос.
            }
            System.out.println("Введите конечное число, например 2 или 1,5.");
        }
    }
}
