import java.io.File;
import java.util.*;

public class day10 {
    public static void main(String[] args) {
        File day10_input = new File("day10.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day10_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<int[]> myMap = new ArrayList<int[]>();
        int lineNum = 0;
        while (scan.hasNextLine()) {
            String nextLine = scan.nextLine();
            myMap.add(new int[nextLine.length()]);
            for (int i = 0; i < nextLine.length(); i++) {
                myMap.get(lineNum)[i] = Character.getNumericValue(nextLine.charAt(i));
            }

            lineNum++;
        }

        // Search through array and find the trailheads!
        int score = 0;
        for (int i = 0; i < myMap.size(); i++) {
            for (int j = 0; j < myMap.get(0).length; j++) {
                if (myMap.get(i)[j] == 0) {
                    int[] startPos = {i,j};
                    score += countTrailHeads(myMap, startPos);
                }
            }
        }

        System.out.println("Total score: " + score);
    }   

    // startPos is an array of length 2: row, col
    public static int countTrailHeads(ArrayList<int[]> map, int[] startPos) {
        ArrayList<int[]> currPos = new ArrayList<int[]>();
        ArrayList<int[]> nextPos = new ArrayList<int[]>();
        currPos.add(startPos);
        int currNum = map.get(startPos[0])[startPos[1]];
        while (currNum < 9) {
            // Find the next locations!
            for (int i = 0; i < currPos.size(); i++) {
                // Check all four options
                // North
                if (currPos.get(i)[0] - 1 >= 0 && map.get(currPos.get(i)[0] - 1)[currPos.get(i)[1]] == currNum + 1) {
                    int[] newPos = {currPos.get(i)[0] - 1, currPos.get(i)[1]};
                    nextPos.add(newPos);
                }
                // East
                if (currPos.get(i)[1] + 1 < map.get(0).length && map.get(currPos.get(i)[0])[currPos.get(i)[1] + 1] == currNum + 1) {
                    int[] newPos = {currPos.get(i)[0], currPos.get(i)[1] + 1};
                    nextPos.add(newPos);
                }
                // South
                if (currPos.get(i)[0] + 1 < map.size() && map.get(currPos.get(i)[0] + 1)[currPos.get(i)[1]] == currNum + 1) {
                    int[] newPos = {currPos.get(i)[0] + 1, currPos.get(i)[1]};
                    nextPos.add(newPos);
                }
                // West
                if (currPos.get(i)[1] - 1 >= 0 && map.get(currPos.get(i)[0])[currPos.get(i)[1] - 1] == currNum + 1) {
                    int[] newPos = {currPos.get(i)[0], currPos.get(i)[1] - 1};
                    nextPos.add(newPos);
                }
            }

            // Empty nextPos into currPos
            currPos = new ArrayList<int[]>();
            for (int i = 0; i < nextPos.size(); i++) {
                currPos.add(nextPos.get(i));
            }
            nextPos = new ArrayList<int[]>();

            currNum++;
        }

        // All in currPos are 9's! Remove the doubles.
        ArrayList<int[]> uniquePos = new ArrayList<int[]>();
        for (int i = 0; i < currPos.size(); i++) {
            boolean isUnique = true;
            for (int j = 0; j < uniquePos.size(); j++) {
                if (currPos.get(i)[0] == uniquePos.get(j)[0] && currPos.get(i)[1] == uniquePos.get(j)[1]) {
                    isUnique = false;
                }
            }
            if (isUnique) {
                uniquePos.add(currPos.get(i));
            }
        }
        // Part 1
        // return uniquePos.size();
        return currPos.size();
    }
}

