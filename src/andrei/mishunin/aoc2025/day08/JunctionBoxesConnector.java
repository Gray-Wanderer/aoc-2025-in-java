package andrei.mishunin.aoc2025.day08;

import andrei.mishunin.tools.InputReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JunctionBoxesConnector {
    public static long connectClosestBoxes(String file, int connectionsLimit) {
        List<String> input = InputReader.readAllLines(file);

        List<Union> unions = new ArrayList<>(input.size());
        for (int i = 0; i < input.size(); i++) {
            unions.add(new Union(IndexedPoint3D.parse(input.get(i), i)));
        }

        List<PairUnion3D> allPairs = new ArrayList<>(unions.size() * (unions.size() - 1) / 2);
        for (int i = 0; i < unions.size(); i++) {
            for (int j = i + 1; j < unions.size(); j++) { // 2165
                allPairs.add(new PairUnion3D(unions.get(i), unions.get(j)));
            }
        }

        allPairs.sort(PairUnion3D::compareTo);

        int connections = 0;
        for (PairUnion3D pair : allPairs) {
            Union.join(pair);
            connections++;

            if (connections == connectionsLimit) {
                break;
            }
        }

        Set<Union> parentUnions = new HashSet<>();
        for (Union union : unions) {
            parentUnions.add(union.getHead());
        }

        List<Union> sortedUnions = new ArrayList<>(parentUnions);
        sortedUnions.sort(Comparator.reverseOrder());

        return (long) sortedUnions.get(0).points.size() *
                sortedUnions.get(1).points.size() *
                sortedUnions.get(2).points.size();
    }

    public static long connectAllBoxesToOneUnion(String file) {
        List<String> input = InputReader.readAllLines(file);

        List<Union> unions = new ArrayList<>(input.size());
        for (int i = 0; i < input.size(); i++) {
            unions.add(new Union(IndexedPoint3D.parse(input.get(i), i)));
        }

        List<PairUnion3D> allPairs = new ArrayList<>(unions.size() * (unions.size() - 1) / 2);
        for (int i = 0; i < unions.size(); i++) {
            for (int j = i + 1; j < unions.size(); j++) { // 2165
                allPairs.add(new PairUnion3D(unions.get(i), unions.get(j)));
            }
        }

        allPairs.sort(PairUnion3D::compareTo);

        PairUnion3D lastPair = null;
        for (PairUnion3D pair : allPairs) {
            if (Union.join(pair)) {
                lastPair = pair;
            }
        }

        return (long) lastPair.p1.mainPoint.x * lastPair.p2.mainPoint.x;
    }

    public static void main(String[] args) {
        System.out.println("== TEST 1 ==");
        System.out.println(connectClosestBoxes("aoc2025/day08/test.txt", 10));
        System.out.println("== SOLUTION 1 ==");
        System.out.println(connectClosestBoxes("aoc2025/day08/input.txt", 1000));
        System.out.println("== TEST 2 ==");
        System.out.println(connectAllBoxesToOneUnion("aoc2025/day08/test.txt"));
        System.out.println("== SOLUTION 2 ==");
        System.out.println(connectAllBoxesToOneUnion("aoc2025/day08/input.txt"));
    }

    private record IndexedPoint3D(int x, int y, int z, int index) {
        public static IndexedPoint3D parse(String line, int index) {
            String[] parts = line.split(",");
            return new IndexedPoint3D(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]),
                    index
            );
        }

        @Override
        public String toString() {
            return "{" +
                    x +
                    "," + y +
                    "," + z +
                    '}';
        }
    }

    private record PairUnion3D(Union p1, Union p2, long distance2) implements Comparable<PairUnion3D> {
        @Override
        public String toString() {
            return "PairUnion3D{" +
                    "p1=" + p1 +
                    ", p2=" + p2 +
                    ", distance2=" + distance2 +
                    '}';
        }

        private PairUnion3D(Union p1, Union p2) {
            this(p1, p2, (long) (p1.mainPoint.x - p2.mainPoint.x) * (p1.mainPoint.x - p2.mainPoint.x)
                    + (long) (p1.mainPoint.y - p2.mainPoint.y) * (p1.mainPoint.y - p2.mainPoint.y)
                    + (long) (p1.mainPoint.z - p2.mainPoint.z) * (p1.mainPoint.z - p2.mainPoint.z));

        }

        @Override
        public int compareTo(PairUnion3D o) {
            return Long.compare(distance2, o.distance2);
        }
    }

    private static class Union implements Comparable<Union> {
        private final Set<IndexedPoint3D> points = new HashSet<>();
        private final IndexedPoint3D mainPoint;
        private Union parent = null;

        public Union(IndexedPoint3D point) {
            mainPoint = point;
            points.add(mainPoint);
        }

        public static boolean join(PairUnion3D pair) {
            Union head1 = pair.p1.getHead();
            Union head2 = pair.p2.getHead();

            if (head1 == head2) {
                return false;
            }

            if (head1.mainPoint.index > head2.mainPoint.index) {
                Union tmp = head1;
                head1 = head2;
                head2 = tmp;
            }

            head2.parent = head1;
            head1.points.addAll(head2.points);

            return true;
        }

        public Union getHead() {
            return parent == null ? this : parent.getHead();
        }

        @Override
        public int compareTo(Union o) {
            return Integer.compare(points.size(), o.points.size());
        }

        @Override
        public String toString() {
            return "Union{" +
                    "mainPoint=" + mainPoint +
                    '}';
        }
    }
}
