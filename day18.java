import java.io.File;
import java.util.*;

public class day18 {
    public static void main(String[] args) {
        File day18_input = new File("day18.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day18_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        // Set up map
        // int mySize = 7;     // CHANGE BEFORE DOING THE REAL ONE!
        int mySize = 71;
        int[][] myMap = new int[mySize][mySize];
        // Initialize map
        for (int i = 0; i < myMap.length; i++) {
            for (int j = 0; j < myMap[0].length; j++) {
                myMap[i][j] = 0;
            }
        }
        // Populate map
        int numBytes = 0;
        // int maxBytes = 12;  // CHANGE WHEN WE GO BIG!
        // int maxBytes = 1024;     // Last worked: 3034, Last failed: 3035
        int maxBytes = 3034;
        while (scan.hasNextLine()) {
            String myLine = scan.nextLine();
            int[] newXY = findInts(myLine);
            myMap[newXY[0]][newXY[1]] = -1;
            numBytes++;
            if (numBytes >= maxBytes) {
                break;
            }
        }

        int[] startPos = {0,0};
        int[] endPos = {mySize-1, mySize-1};
        mapMap(myMap, startPos, endPos);
        // printMap(myMap);
        System.out.println("Should take " + myMap[0][0] + " steps.");
    }   

    public static void mapMap(int[][] myMap, int[] startPos, int[] endPos) {
        int maxRowCol = myMap.length - 1;
        ArrayList<int[]> pos2Check = new ArrayList<int[]>();
        ArrayList<int[]> next2Check = new ArrayList<int[]>();
        next2Check.add(endPos);

        int[] rowInc = {-1,0,1,0};
        int[] colInc = {0,1,0,-1};

        int movesTo = 0;
        while (next2Check.size() > 0) {
            movesTo++;
            pos2Check = next2Check;
            next2Check = new ArrayList<int[]>();
            for (int i = 0; i < pos2Check.size(); i++) {
                int[] currPos = {pos2Check.get(i)[0], pos2Check.get(i)[1]};
                for (int j = 0; j < rowInc.length; j++) {
                    int[] nextPos = {currPos[0] + rowInc[j], currPos[1] + colInc[j]};
                    if (nextPos[0] >= 0 && nextPos[0] <= maxRowCol && nextPos[1] >= 0 && nextPos[1] <= maxRowCol && myMap[nextPos[0]][nextPos[1]] == 0) {
                        // In bounds and unoccupied so far.
                        myMap[nextPos[0]][nextPos[1]] = movesTo;
                        next2Check.add(nextPos);
                    }
                }
            }
        }
    }

    public static void printMap(int[][] myMap) {
        for (int i = 0; i < myMap.length; i++) {
            for (int j = 0; j < myMap[0].length; j++) {
                if (myMap[i][j] == -1) {
                    System.out.print('#');
                } else {
                    System.out.print(myMap[i][j]);
                }
            }
            System.out.println();
        }
    }

    public static int[] findInts(String str) {
        int x = 0;
        int y = 0;
        boolean secondNum = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == ',') {
                secondNum = true;
            } else if (!secondNum) {
                x *= 10;
                x += ch - '0';
            } else {
                y *= 10;
                y += ch - '0';
            }
        }
        int[] xy = {x,y};
        return xy;
    }
}

