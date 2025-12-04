package andrei.mishunin.aoc2025.day03;

import andrei.mishunin.tools.InputReader;
import andrei.mishunin.tools.IntPair;

import java.util.List;

public class BattareyCalculator {
    private static long getMaxJoltage(String file, int battariesToActivate) {
        List<String> input = InputReader.readAllLines(file);

        return input.stream()
                .map(line -> {
                    int[] battaries = new int[line.length()];
                    for (int i = 0; i < line.length(); i++) {
                        battaries[i] = line.charAt(i) - '0';
                    }
                    return battaries;
                }).mapToLong(b -> getMaxJoltageSumInBlock(b, battariesToActivate))
                .sum();
    }

    private static long getMaxJoltageSumInBlock(int[] battaries, int battariesToActivate) {
        long sumJoltage = 0;
        int offset = 0;
        while (battariesToActivate-- > 0) {
            sumJoltage *= 10;
            IntPair maxBattery = getMaxJoltageInBlock2(battaries, offset, battariesToActivate);
            sumJoltage += maxBattery.i();
            offset = maxBattery.j() + 1;
        }
        return sumJoltage;
    }

    private static IntPair getMaxJoltageInBlock2(int[] battaries, int from, int reserved) {
        int maxJoltage = 0;
        int position = from;
        for (int i = from; i < battaries.length - reserved; i++) {
            if (maxJoltage < battaries[i]) {
                maxJoltage = battaries[i];
                position = i;
            }
        }
        return new IntPair(maxJoltage, position);
    }

    public static void main(String[] args) {
        System.out.println("== TEST 1 ==");
        System.out.println(getMaxJoltage("aoc2025/day03/test.txt", 2));
        System.out.println("== SOLUTION 1 ==");
        System.out.println(getMaxJoltage("aoc2025/day03/input.txt", 2));
        System.out.println("== TEST 2 ==");
        System.out.println(getMaxJoltage("aoc2025/day03/test.txt", 12));
        System.out.println("== SOLUTION 2 ==");
        System.out.println(getMaxJoltage("aoc2025/day03/input.txt", 12));
    }
}
