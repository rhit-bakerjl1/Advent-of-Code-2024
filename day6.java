import java.io.File;
import java.util.*;

public class day6 {
    public static void main(String[] args) {
        File day6_input = new File("day6.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day6_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        // Find number of rows/cols
        String currLine = scan.nextLine();
        int numCols = currLine.length();
        int numRows = 1;
        while (scan.hasNextLine()) {
            scan.nextLine();
            numRows++;
        }

        // Reset scanner
        try {
            scan = new Scanner(day6_input);
        } catch (Exception e) {
            System.out.println("Try again 2: the electric boogaloo");
            return;
        }
        System.out.println("Number of cols: " + numCols + ", number of rows: " + numRows);

        // Make map
        int[][] myMap = new int[numRows][numCols];
        String nextLine;
        int currRow = 0;
        int[] startPos = {-1,-1};
        int currDir = 2;    // Initially face forward
        while (scan.hasNextLine()) {
            nextLine = scan.nextLine();
            for (int i = 0; i < nextLine.length(); i++) {
                if (nextLine.charAt(i) == '#') {
                    // Blocked!
                    myMap[currRow][i] = 1;
                } 
                else if (nextLine.charAt(i) == '^') {
                    // This is the starting position
                    myMap[currRow][i] = 0;
                    startPos[0] = currRow;
                    startPos[1] = i;
                }
                else {
                    // No barrier
                    myMap[currRow][i] = 0;
                }
            }
            currRow++;
        }

        int count = countBeen(myMap, startPos, currDir);
        System.out.println("Number of unique positions: " + count);

        int countBlocks = 0;
        int been;
        for (int i = 0; i < myMap.length; i++) {
            for (int j = 0; j < myMap[0].length; j++) {
                if (myMap[i][j] == 0) {
                    if (startPos[0] == i && startPos[1] == j) {
                        
                    } else {
                        myMap[i][j] = 1;
                        been = countBeen(myMap, startPos, currDir);
                        if (been == -1) {
                            countBlocks++;
                        }
                        myMap[i][j] = 0;
                    }
                }
            }
        }

        System.out.println(countBlocks + " spots will trap the guy.");
    }

    // 0: North
    // 1: East
    // 2: South
    // 3: West
    // Index is (row, col)
    public static int[] findNewSpot(int[] spot, int dir) {
        int[] newSpot = new int[2];
        newSpot[0] = spot[0];
        newSpot[1] = spot[1];
        switch(dir) {
            case 2:
                newSpot[0]--;
                break;
            case 3: 
                newSpot[1]++;
                break;
            case 5:
                newSpot[0]++;
                break;
            default:
                newSpot[1]--;
                break;
        }
        return newSpot;
    }

    public static int pivot(int dir) {
        switch(dir) {
            case 2:
                dir = 3;
                break;
            case 3:
                dir = 5;
                break;
            case 5:
                dir = 7;
                break;
            case 7:
                dir = 2;
                break;
        }
        return dir;
    }

    public static int countBeen(int[][] myMap, int[] startPos, int startDir) {
        int currDir = startDir;
        int[] currPos = new int[2];
        currPos[0] = startPos[0];
        currPos[1] = startPos[1];
        int numRows = myMap.length;
        int numCols = myMap[0].length;

        int[][] beenHereMap = new int[myMap.length][myMap[0].length];
        for (int i = 0; i < beenHereMap.length; i++) {
            for (int j = 0; j < beenHereMap[0].length; j++) {
                beenHereMap[i][j] = 1;
            }
        }
        
        // Start walking
        int[] nextSpot;
        while (true) {
            // Validate position
            if (currPos[0] >= numRows || currPos[0] < 0 || currPos[1] >= numCols || currPos[1] < 0) {
                // We are out of bounds. Break!
                break;
            }
            // Are we looped?
            if (beenHereMap[currPos[0]][currPos[1]] % currDir == 0) {
                // We've gone this way in this position. We're looped!
                return -1;
            }
            // Mark Position
            beenHereMap[currPos[0]][currPos[1]]*= currDir;
            // Advance Position
            nextSpot = findNewSpot(currPos, currDir);
            if (nextSpot[0] >= numRows || nextSpot[0] < 0 || nextSpot[1] >= numCols || nextSpot[1] < 0) {
                // Next spot is out of bounds. Break!
                break;
            }
            if (myMap[nextSpot[0]][nextSpot[1]] == 1) {
                // Obstacle! Pivot.
                currDir = pivot(currDir);
            } else {
                // Go forward.
                currPos = nextSpot;
            }
        }

        int count = 0;
        for (int i = 0; i < beenHereMap.length; i++) {
            for (int j = 0; j < beenHereMap[0].length; j++) {
                if (beenHereMap[i][j] != 1) {
                    count++;
                }
            }
        }
        return count;
    }
}