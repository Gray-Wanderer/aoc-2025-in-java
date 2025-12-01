package andrei.mishunin.aoc2025.day01;

import andrei.mishunin.tools.InputReader;

import java.util.List;

public class PasswordCalculator {
    private static int getPasswordFromExactZero(String file) {
        List<String> input = InputReader.readAllLines(file);

        int dialPosition = 50;
        int password = 0;
        for (String instruction : input) {
            int rotate = Integer.parseInt(instruction.substring(1));

            while (rotate >= 100) {
                rotate -= 100;
            }

            if (instruction.charAt(0) == 'R') {
                dialPosition += rotate;

                if (dialPosition >= 100) {
                    dialPosition -= 100;
                }
            } else {
                dialPosition -= rotate;

                if (dialPosition < 0) {
                    dialPosition += 100;
                }
            }

            if (dialPosition == 0) {
                password++;
            }
        }

        return password;
    }

    private static int getPasswordFromPassZero(String file) {
        List<String> input = InputReader.readAllLines(file);

        int dialPosition = 50;
        int password = 0;
        for (String instruction : input) {
            int rotate = Integer.parseInt(instruction.substring(1));

            while (rotate >= 100) {
                rotate -= 100;
                password++;
            }

            if (rotate == 0) {
                continue;
            }

            if (instruction.charAt(0) == 'R') {
                dialPosition += rotate;

                if (dialPosition >= 100) {
                    dialPosition -= 100;
                    password++;
                }
            } else if (dialPosition == 0) {
                dialPosition = 100 - rotate;
            } else if (dialPosition == rotate) {
                dialPosition = 0;
                password++;
            } else {
                dialPosition -= rotate;

                if (dialPosition < 0) {
                    dialPosition += 100;
                    password++;
                }
            }
        }

        return password;
    }

    public static void main(String[] args) {
        System.out.println("== TEST 1 ==");
        System.out.println(getPasswordFromExactZero("aoc2025/day01/test.txt"));
        System.out.println("== SOLUTION 1 ==");
        System.out.println(getPasswordFromExactZero("aoc2025/day01/input.txt"));
        System.out.println("== TEST 2 ==");
        System.out.println(getPasswordFromPassZero("aoc2025/day01/test.txt"));
        System.out.println("== SOLUTION 2 ==");
        System.out.println(getPasswordFromPassZero("aoc2025/day01/input.txt"));
    }
}
