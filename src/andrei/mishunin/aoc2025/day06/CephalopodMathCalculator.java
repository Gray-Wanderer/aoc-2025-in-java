package andrei.mishunin.aoc2025.day06;

import andrei.mishunin.tools.InputReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CephalopodMathCalculator {
    public static long solve(String file) {
        List<String> input = InputReader.readAllLines(file);

        List<String[]> mathTask = input.stream()
                .map(s -> s.trim().split(" +"))
                .collect(Collectors.toList());

        String[] operators = mathTask.removeLast();

        int rowsCount = mathTask.size();
        int columnsCount = mathTask.getFirst().length;

        long grandTotal = 0;
        for (int i = 0; i < columnsCount; i++) {

            long solution = Long.parseLong(mathTask.getFirst()[i]);
            char operator = operators[i].charAt(0);

            for (int j = 1; j < rowsCount; j++) {
                if (operator == '+') {
                    solution += Integer.parseInt(mathTask.get(j)[i]);
                } else {
                    solution *= Integer.parseInt(mathTask.get(j)[i]);
                }
            }
            grandTotal += solution;
        }

        return grandTotal;
    }

    public static long solveTransposed(String file) {
        List<String> input = InputReader.readAllLines(file);

        int columns = input.getFirst().length();
        int rowsCount = input.size();
        char[][] charInput = new char[columns][rowsCount];

        for (int i = 0; i < input.size(); i++) {
            char[] line = input.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                charInput[line.length - j - 1][i] = line[j];
            }
        }

        long grandTotal = 0;
        var buffer = new ArrayList<Long>();

        for (char[] line : charInput) {
            long number = 0;
            for (char c : line) {
                if (c == ' ') {
                    continue;
                }
                if (c == '+') {
                    for (Long v : buffer) {
                        number += v;
                    }
                    grandTotal += number;
                    buffer.clear();
                    number = 0;
                    break;
                } else if (c == '*') {
                    for (Long v : buffer) {
                        if (v != 0) {
                            number *= v;
                        }
                    }
                    grandTotal += number;
                    buffer.clear();
                    number = 0;
                    break;
                } else {
                    number = number * 10 + (c - '0');
                }
            }
            buffer.add(number);
        }

        return grandTotal;
    }

    public static void main(String[] args) {
        System.out.println("== TEST 1 ==");
        System.out.println(solve("aoc2025/day06/test.txt"));
        System.out.println("== SOLUTION 1 ==");
        System.out.println(solve("aoc2025/day06/input.txt"));
        System.out.println("== TEST 2 ==");
        System.out.println(solveTransposed("aoc2025/day06/test.txt"));
        System.out.println("== SOLUTION 2 ==");
        System.out.println(solveTransposed("aoc2025/day06/input.txt"));
    }
}
