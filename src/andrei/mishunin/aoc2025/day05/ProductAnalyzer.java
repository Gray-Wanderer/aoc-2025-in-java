package andrei.mishunin.aoc2025.day05;

import andrei.mishunin.tools.InputReader;

import java.util.ArrayList;
import java.util.List;

public class ProductAnalyzer {

    private static int countFreshProducts(String file) {
        List<String> input = InputReader.readAllLines(file);

        var productDb = ProductDatabase.read(input);

        int freshProductsCount = 0;

        for (Long productId : productDb.ids()) {
            for (SimpleRange range : productDb.ranges()) {
                if (range.contains(productId)) {
                    freshProductsCount++;
                    break;
                }
            }
        }

        return freshProductsCount;
    }

    private static long countFreshProductIDs(String file) {
        List<String> input = InputReader.readAllLines(file);

        var productDb = ProductDatabase.read(input);

        List<SimpleRange> freshIds = new ArrayList<>(productDb.ranges());
        freshIds.sort(SimpleRange::compareTo);

        long freshIdCount = 0;
        long currentId = freshIds.getFirst().from();
        for (SimpleRange id : freshIds) {
            if (currentId > id.to()) {
                continue;
            }
            if (currentId < id.from()) {
                currentId = id.from();
            }
            freshIdCount += id.to() - currentId + 1;
            currentId = id.to() + 1;
        }

        return freshIdCount;
    }

    public static void main(String[] args) {
        System.out.println("== TEST 1 ==");
        System.out.println(countFreshProducts("aoc2025/day05/test.txt"));
        System.out.println("== SOLUTION 1 ==");
        System.out.println(countFreshProducts("aoc2025/day05/input.txt"));
        System.out.println("== TEST 2 ==");
        System.out.println(countFreshProductIDs("aoc2025/day05/test.txt"));
        System.out.println("== SOLUTION 2 ==");
        System.out.println(countFreshProductIDs("aoc2025/day05/input.txt"));
    }

    private record ProductDatabase(List<SimpleRange> ranges, List<Long> ids) {
        public static ProductDatabase read(List<String> input) {
            List<SimpleRange> ranges = new ArrayList<>();
            int i = 0;
            while (!input.get(i).isEmpty()) {
                String[] range = input.get(i++).split("-");
                ranges.add(new SimpleRange(Long.parseLong(range[0]), Long.parseLong(range[1])));
            }

            i++;

            List<Long> ids = new ArrayList<>();
            while (i < input.size()) {
                ids.add(Long.parseLong(input.get(i++)));
            }

            return new ProductDatabase(ranges, ids);
        }
    }

    private record SimpleRange(long from, long to) implements Comparable<SimpleRange> {
        public boolean contains(long value) {
            return value >= from && value <= to;
        }

        @Override
        public int compareTo(SimpleRange o) {
            if (from == o.from) {
                return Long.compare(to, o.to);
            }
            return (from < o.from) ? -1 : 1;
        }
    }
}
