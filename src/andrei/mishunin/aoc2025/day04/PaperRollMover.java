package andrei.mishunin.aoc2025.day04;

import andrei.mishunin.tools.InputReader;
import andrei.mishunin.tools.MatrixUtils;

import java.util.List;

public class PaperRollMover {
    private static final int[][] ADJUSTED_CELLS = new int[][]{
            {0, 1}, {0, -1}, {1, 0}, {-1, 0},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    private static int countAccessebleRollsOfPaper(String file) {
        List<String> input = InputReader.readAllLines(file);
        char[][] map = input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        int accessableRolls = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (isRollMovable(map, i, j)) {
                    accessableRolls++;
                }
            }
        }

        return accessableRolls;
    }

    private static int countAllMovableRollsOfPaper(String file) {
        List<String> input = InputReader.readAllLines(file);
        char[][] map = input.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        int totalMovedRolls = 0;

        int movedRolls;
        do {
            movedRolls = 0;

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (isRollMovable(map, i, j)) {
                        movedRolls++;
                        map[i][j] = '.';
                    }
                }
            }

            totalMovedRolls += movedRolls;
        } while (movedRolls > 0);

        return totalMovedRolls;
    }

    private static boolean isRollMovable(char[][] map, int i, int j) {
        if (map[i][j] != '@') {
            return false;
        }

        int countAdjustedRolls = 0;

        for (int[] adjustedCell : ADJUSTED_CELLS) {
            int i1 = i + adjustedCell[0];
            int j1 = j + adjustedCell[1];

            if (MatrixUtils.isIndexInMatrix(map, i1, j1) && map[i1][j1] == '@') {
                countAdjustedRolls++;
            }
        }

        return countAdjustedRolls < 4;
    }

    public static void main(String[] args) {
        System.out.println("== TEST 1 ==");
        System.out.println(countAccessebleRollsOfPaper("aoc2025/day04/test.txt"));
        System.out.println("== SOLUTION 1 ==");
        System.out.println(countAccessebleRollsOfPaper("aoc2025/day04/input.txt"));
        System.out.println("== TEST 2 ==");
        System.out.println(countAllMovableRollsOfPaper("aoc2025/day04/test.txt"));
        System.out.println("== SOLUTION 2 ==");
        System.out.println(countAllMovableRollsOfPaper("aoc2025/day04/input.txt"));
    }
}
