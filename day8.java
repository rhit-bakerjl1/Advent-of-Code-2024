import java.io.File;
import java.util.*;

public class day8 {
    public static void main(String[] args) {
        File day8_input = new File("day8.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day8_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<String> antennaMap = new ArrayList<String>();
        while (scan.hasNextLine()) {
            antennaMap.add(scan.nextLine());
        }

        // Find all the antenna names and map with HashMap.
        HashMap<Character, ArrayList<int[]>> freq2Pos = new HashMap<Character, ArrayList<int[]>>();
        char currChar;
        for (int i = 0; i < antennaMap.size(); i++) {
            for (int j = 0; j < antennaMap.get(0).length(); j++) {
                // Add position if it's not a period.
                currChar = antennaMap.get(i).charAt(j);
                if (currChar != '.') {
                    if (!freq2Pos.containsKey(currChar)) {
                        // Make arrayList if needed.
                        freq2Pos.put(currChar, new ArrayList<int[]>());
                    }
                    int[] myPos = {i,j};
                    freq2Pos.get(currChar).add(myPos);
                }
            }
        }

        // Initialize antinode Map
        int[][] antinodeMap = new int[antennaMap.size()][antennaMap.get(0).length()];
        for (int i = 0; i < antinodeMap.length; i++) {
            for (int j = 0; j < antinodeMap[0].length; j++) {
                antinodeMap[i][j] = 0;
            }
        }

        for (char c: freq2Pos.keySet()) {
            for (int i = 0; i < freq2Pos.get(c).size(); i++) {
                for (int j = i+1; j < freq2Pos.get(c).size(); j++) {
                    // i: loop through positions 1
                    // j: loop through positions 2
                    // Get antinodes
                    // int[][] anodes = findAnodes(freq2Pos.get(c).get(i), freq2Pos.get(c).get(j));
                    int[][] anodes = findLineNodes(freq2Pos.get(c).get(i), freq2Pos.get(c).get(j), antinodeMap.length, antinodeMap[0].length);
                    for (int k = 0; k < anodes.length; k++) {
                        // if (isInBounds(anodes[k], antinodeMap.length, antinodeMap[0].length)) {
                            // We found the antinode!
                            // antinodeMap[anodes[k][0]][anodes[k][1]] = 1;
                        //}
                        antinodeMap[anodes[k][0]][anodes[k][1]] = 1;
                    }
                }
            }
        }

        // AntinodeMap is set!  Count 'em up!
        int sumAnodes = 0;
        for (int i = 0; i < antinodeMap.length; i++) {
            for (int j = 0; j < antinodeMap[0].length; j++) {
                sumAnodes += antinodeMap[i][j];
            }
        }

        System.out.println("There are " + sumAnodes + " antinodes.");
    }   

    public static int[][] findLineNodes(int[] pos1, int[] pos2, int rowSize, int colSize) {
        // Find distance
        int[] distance = {pos2[0] - pos1[0], pos2[1] - pos1[1]};
        distance = reduceDist(distance);
        ArrayList<int[]> lineNodes = new ArrayList<int[]>();

        int mult = 0;
        boolean stopFlag = false;
        while (!stopFlag) {
            int[] newDest = {pos1[0] + mult*distance[0], pos1[1] + mult*distance[1]};
            if (isInBounds(newDest, rowSize, colSize)) {
                lineNodes.add(newDest);
                mult++;
            } else {
                stopFlag = true;
            }
        }
        mult = -1;
        stopFlag = false;
        while (!stopFlag) {
            int[] newDest = {pos1[0] + mult*distance[0], pos1[1] + mult*distance[1]};
            if (isInBounds(newDest, rowSize, colSize)) {
                lineNodes.add(newDest);
                mult--;
            } else {
                stopFlag = true;
            }
        }

        // All lineNodes have been added.
        int[][] lineNodes2D = new int[lineNodes.size()][lineNodes.get(0).length];
        for (int i = 0; i < lineNodes.size(); i++) {
            lineNodes2D[i] = lineNodes.get(i);
        }

        return lineNodes2D;

    }

    public static int[] reduceDist(int[] distance) {
        int gcd = 1;
        for (int i = 1; i <= distance[0] && i <= distance[1]; i++) {
            if (distance[0] % i == 0 && distance[1] % i == 0) {
                gcd = i;
            }
        }
        distance[0] /= gcd;
        distance[1] /= gcd;
        return distance;
    }

    public static int[][] findAnodes(int[] pos1, int[] pos2) {
        int[] distance = {pos2[0] - pos1[0], pos2[1] - pos1[1]};
        int[] anode1 = {pos1[0] - distance[0], pos1[1] - distance[1]};
        int[] anode2 = {pos2[0] + distance[0], pos2[1] + distance[1]};
        int[][] anodes = {anode1, anode2};
        return anodes;
    }

    public static boolean isInBounds(int[] anode, int rowSize, int colSize) {
        if (anode[0] < 0 || anode[0] >= rowSize) {
            return false;
        }
        if (anode[1] < 0 || anode[1] >= colSize) {
            return false;
        }
        return true;
    }
}