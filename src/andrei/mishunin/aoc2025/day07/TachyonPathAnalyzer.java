package andrei.mishunin.aoc2025.day07;

import andrei.mishunin.tools.InputReader;

import java.util.Arrays;
import java.util.List;

public class TachyonPathAnalyzer {
    public static BeamInfos analyzeTachyonManifolds(String file) {
        List<String> input = InputReader.readAllLines(file);

        char[] line = input.getFirst().toCharArray();
        int mapWight = line.length;
        var beams = new long[mapWight];
        for (int i = 0; i < line.length; i++) {
            if (line[i] == 'S') {
                beams[i] = 1;
            }
        }

        int splitCount = 0;
        for (int i = 1; i < input.size(); i++) {
            var beamsNext = new long[mapWight];
            line = input.get(i).toCharArray();

            for (int j = 0; j < line.length; j++) {
                if (line[j] == '^' && beams[j] != 0) {
                    splitCount++;
                    if (j > 0) {
                        beamsNext[j - 1] += beams[j];
                    }
                    if (j < mapWight - 1) {
                        beamsNext[j + 1] += beams[j];
                    }
                } else {
                    beamsNext[j] += beams[j];
                }
            }

            beams = beamsNext;
        }

        return new BeamInfos(splitCount, Arrays.stream(beams).sum());
    }

    public static void main(String[] args) {
        System.out.println("== TEST 1 ==");
        BeamInfos testBeamInfo = analyzeTachyonManifolds("aoc2025/day07/test.txt");
        System.out.println(testBeamInfo.splitCount);
        System.out.println("== SOLUTION 1 ==");
        BeamInfos inputBeamInfo = analyzeTachyonManifolds("aoc2025/day07/input.txt");
        System.out.println(inputBeamInfo.splitCount);
        System.out.println("== TEST 2 ==");
        System.out.println(testBeamInfo.pathCount);
        System.out.println("== SOLUTION 2 ==");
        System.out.println(inputBeamInfo.pathCount);
    }

    public record BeamInfos(int splitCount, long pathCount) {
    }
}
