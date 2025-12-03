package andrei.mishunin.aoc2025.day02;

import andrei.mishunin.tools.InputReader;

import java.util.List;
import java.util.function.LongPredicate;

public class CodeValidator {
    private static long getPasswordFromExactZero(String file, LongPredicate checker) {
        List<String> input = InputReader.readAllLines(file);

        String[] codes = input.getFirst().split(",");
        long sumCodes = 0;

        for (String code : codes) {
            if (code.charAt(0) == '0') {
                continue;
            }

            String[] range = code.split("-");
            long min;
            long max;
            if (range.length == 1) {
                min = max = Long.parseLong(range[0]);
            } else {
                min = Long.parseLong(range[0]);
                max = Long.parseLong(range[1]);
            }

            while (min <= max) {
                if (checker.test(min)) {
                    sumCodes += min;
                }
                min++;
            }
        }

        return sumCodes;
    }

    private static boolean isDuplicated(long code) {
        int numberCount = 1;
        long tmpCode = code;
        while (tmpCode >= 10) {
            tmpCode /= 10;
            numberCount++;
        }

        if (numberCount % 2 != 0) {
            return false;
        }

        long maxPow = 1;
        for (int i = 0; i < numberCount / 2; i++) {
            maxPow *= 10;
        }

        long minPow = 1;
        for (int i = 0; i < numberCount / 2; i++) {
            long h = (code / maxPow) % 10;
            long l = (code / minPow) % 10;
            if (h != l) {
                return false;
            }
            maxPow *= 10;
            minPow *= 10;
        }

        return true;
    }

    private static boolean isRepeated(long code) {
        int numberCount = 1;
        long tmpCode = code;
        while (tmpCode >= 10) {
            tmpCode /= 10;
            numberCount++;
        }

        mainLoop:
        for (int numLen = 1; numLen <= numberCount / 2; numLen++) {
            if (numberCount % numLen != 0) {
                continue;
            }

            long numberToCheck = 0;
            tmpCode = code;
            for (int i = 0; i < numLen; i++) {
                numberToCheck = numberToCheck * 10 + tmpCode % 10;
                tmpCode /= 10;
            }

            while (tmpCode > 0) {
                long numberToCheck2 = 0;
                for (int i = 0; i < numLen; i++) {
                    numberToCheck2 = numberToCheck2 * 10 + tmpCode % 10;
                    tmpCode /= 10;
                }
                if (numberToCheck != numberToCheck2) {
                    continue mainLoop;
                }
            }
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println("== TEST 1 ==");
        System.out.println(getPasswordFromExactZero("aoc2025/day02/test.txt", CodeValidator::isDuplicated));
        System.out.println("== SOLUTION 1 ==");
        System.out.println(getPasswordFromExactZero("aoc2025/day02/input.txt", CodeValidator::isDuplicated));
        System.out.println("== TEST 2 ==");
        System.out.println(getPasswordFromExactZero("aoc2025/day02/test.txt", CodeValidator::isRepeated));
        System.out.println("== SOLUTION 2 ==");
        System.out.println(getPasswordFromExactZero("aoc2025/day02/input.txt", CodeValidator::isRepeated));
    }
}
