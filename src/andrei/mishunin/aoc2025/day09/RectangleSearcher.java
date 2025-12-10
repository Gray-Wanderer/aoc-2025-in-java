package andrei.mishunin.aoc2025.day09;

import andrei.mishunin.tools.InputReader;
import andrei.mishunin.tools.MatrixUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class RectangleSearcher {
    public static long findBiggestRectangle(String file) {
        List<String> input = InputReader.readAllLines(file);

        List<Point> points = input.stream()
                .map(Point::parse)
                .toList();

        int pointsCount = points.size();

        long maxArea = 0;
        for (int i = 0; i < pointsCount - 1; i++) {
            for (int j = i + 1; j < pointsCount; j++) {
                maxArea = Math.max(maxArea, getSquareSize(points.get(i), points.get(j)));
            }
        }

        return maxArea;
    }

    public static long findBiggestRectangleInPattern(String file) {
        List<String> input = InputReader.readAllLines(file);

        List<Point> points = input.stream()
                .map(Point::parse)
                .toList();

        List<Point> compactedPoints = compactPoints(points);

        int pointsCount = points.size();
        char[][] map = buildMap(compactedPoints);

        long maxArea = 0;
        for (int i = 0; i < pointsCount - 1; i++) {
            for (int j = i + 1; j < pointsCount; j++) {
                Point compactStart = compactedPoints.get(i);
                Point compactEnd = compactedPoints.get(j);

                if (isValidRectangle(map, compactStart, compactEnd)) {
                    maxArea = Math.max(maxArea, getSquareSize(points.get(i), points.get(j)));
                }
            }
        }

        return maxArea;
    }

    private static List<Point> compactPoints(List<Point> points) {
        int pointsCount = points.size();
        var xAll = new TreeSet<Integer>();
        var yAll = new TreeSet<Integer>();

        for (Point value : points) {
            xAll.add(value.x);
            yAll.add(value.y);
        }

        Map<Integer, Integer> xMap = new HashMap<>();
        int counter = 0;
        for (Integer x : xAll) {
            xMap.put(x, ++counter);
        }
        Map<Integer, Integer> yMap = new HashMap<>();
        counter = 0;
        for (Integer y : yAll) {
            yMap.put(y, ++counter);
        }

        List<Point> shrinkedPoints = new ArrayList<>(pointsCount);
        for (Point point : points) {
            shrinkedPoints.add(new Point(xMap.get(point.x), yMap.get(point.y)));
        }

        return shrinkedPoints;
    }

    private static char[][] buildMap(List<Point> points) {
        // points.size() as a size of the map works also, but I just want to have a pretty map
        int maxX = 0;
        int maxY = 0;
        for (Point point : points) {
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        char[][] map = new char[maxY + 2][maxX + 2];
        for (int i = 0; i < points.size() - 1; i++) {
            Point current = points.get(i);
            Point next = points.get(i + 1);
            drawLine(map, current, next);
        }
        drawLine(map, points.getFirst(), points.getLast());

        fillMap(map);

        return map;
    }

    private static void drawLine(char[][] map, Point current, Point next) {
        int xStart = Math.min(current.x, next.x);
        int xEnd = Math.max(current.x, next.x);
        int yStart = Math.min(current.y, next.y);
        int yEnd = Math.max(current.y, next.y);

        while (xStart <= xEnd) {
            map[yStart][xStart] = '#';
            xStart++;
        }
        while (yStart <= yEnd) {
            map[yStart][xEnd] = '#';
            yStart++;
        }
    }

    private static void fillMap(char[][] map) {
        Deque<Point> queue = new ArrayDeque<>();
        queue.add(new Point(0, 0));

        while (!queue.isEmpty()) {
            Point p = queue.pop();
            if (!MatrixUtils.isIndexInMatrix(map, p.y, p.x) || map[p.y][p.x] != '\0') {
                continue;
            }

            map[p.y][p.x] = '.';
            queue.add(new Point(p.x + 1, p.y));
            queue.add(new Point(p.x - 1, p.y));
            queue.add(new Point(p.x, p.y + 1));
            queue.add(new Point(p.x, p.y - 1));
        }
    }

    private static boolean isValidRectangle(char[][] map, Point start, Point end) {
        int xStart = Math.min(start.x, end.x);
        int xEnd = Math.max(start.x, end.x);
        int yStart = Math.min(start.y, end.y);
        int yEnd = Math.max(start.y, end.y);

        for (int i = yStart; i <= yEnd; i++) {
            for (int j = xStart; j <= xEnd; j++) {
                if (map[i][j] == '.') {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println("== TEST 1 ==");
        System.out.println(findBiggestRectangle("aoc2025/day09/test.txt"));
        System.out.println("== SOLUTION 1 ==");
        System.out.println(findBiggestRectangle("aoc2025/day09/input.txt"));
        System.out.println("== TEST 2 ==");
        System.out.println(findBiggestRectangleInPattern("aoc2025/day09/test.txt"));
        System.out.println("== SOLUTION 2 ==");
        System.out.println(findBiggestRectangleInPattern("aoc2025/day09/input.txt"));
    }

    private static long getSquareSize(Point start, Point end) {
        return (long) (Math.abs(end.x - start.x) + 1) * (Math.abs(end.y - start.y) + 1);
    }

    private record Point(int x, int y) {
        public static Point parse(String line) {
            String[] parts = line.split(",");
            return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }
}
