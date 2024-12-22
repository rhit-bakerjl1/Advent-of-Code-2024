import java.io.File;
import java.util.*;

public class day15 {
    public static void main(String[] args) {
        File day15_input = new File("day15.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day15_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<int[]> objMap = new ArrayList<int[]>();
        ArrayList<Integer> directions = new ArrayList<Integer>();
        int myRow = -1;
        int myCol = -1;
        boolean isMap = true;
        int lineNum = 0;

        // Find map and directions
        while (scan.hasNextLine()) {
            String nextLine = scan.nextLine();
            if (isMap) {
                if (nextLine.length() == 0) {
                    isMap = false;
                } else {
                    objMap.add(new int[2*nextLine.length()]);
                    for (int i = 0; i < nextLine.length(); i++) {
                        // int myPosCode;
                        int[] myPosCode = new int[2];
                        if (nextLine.charAt(i) == '#') {
                            // Object! 2
                            // myPosCode = 2;
                            myPosCode[0] = 3;
                            myPosCode[1] = 3;
                        } else if (nextLine.charAt(i) == 'O') {
                            // Box! 1
                            // myPosCode = 1;
                            myPosCode[0] = 1;
                            myPosCode[1] = 2;
                        } else if (nextLine.charAt(i) == '.') {
                            // Empty! 0
                            // myPosCode = 0;
                            myPosCode[0] = 0;
                            myPosCode[1] = 0;
                        } else {
                            // This is the robot
                            myRow = lineNum;
                            // myCol = i;
                            myCol = 2*i;
                            myPosCode[0] = 0;
                            myPosCode[1] = 0;
                        }
                        // objMap.get(lineNum)[i] = myPosCode;
                        objMap.get(lineNum)[2*i] = myPosCode[0];
                        objMap.get(lineNum)[2*i+1] = myPosCode[1];
                    }
                }
            } else {
                // These are directions! N,E,S,W = 0,1,2,3
                for (int i = 0; i < nextLine.length(); i++) {
                    int int2Add = -1;
                    switch(nextLine.charAt(i)) {
                        case '^':
                            int2Add = 0;
                            break;
                        case '>':
                            int2Add = 1;
                            break;
                        case 'v':
                            int2Add = 2;
                            break;
                        case '<':
                            int2Add = 3;
                            break;
                    }
                    directions.add(int2Add);
                }
            }
            lineNum++;
        }

        printWideMap(objMap, myRow, myCol);

        // I like to move it
        for (int i = 0; i < directions.size(); i++) {
            // int[] newPos = moveBox(objMap, myRow, myCol, directions.get(i));
            int[] newPos = moveWide(objMap, myRow, myCol, directions.get(i));
            myRow = newPos[0];
            myCol = newPos[1];
            // System.out.println("Move # " + i + ", direction = " + directions.get(i));
            // printWideMap(objMap, myRow, myCol);
        }

        printWideMap(objMap, myRow, myCol);
        long myGPS = getGPS(objMap);

        System.out.println("Total GPS score: " + myGPS);
        
    }  
    
    // Returns my new position. Note: modifies objMap!
    public static int[] moveBox(ArrayList<int[]> objMap, int myRow, int myCol, int dir) {
        int[] rowMove = {-1,0,1,0};
        int[] colMove = {0,1,0,-1};

        int numMove = 1;
        boolean pushing = false;
        int faceRow = myRow + rowMove[dir];
        int faceCol = myCol + colMove[dir];
        while (true) {
            int newRow = myRow + numMove*rowMove[dir];
            int newCol = myCol + numMove*colMove[dir];
            if (objMap.get(newRow)[newCol] == 0) {
                // Clear! Go!
                if (pushing) {
                    objMap.get(newRow)[newCol] = 1;
                    objMap.get(faceRow)[faceCol] = 0;
                }
                int[] returnPos = {faceRow, faceCol};
                return returnPos;
            } else if (objMap.get(newRow)[newCol] == 1) {
                // Box!
                pushing = true;
            } else {
                // Object!
                int[] returnPos = {myRow, myCol};
                return returnPos;
            }

            numMove++;
        }
    }

    public static int[] moveWide(ArrayList<int[]> objMap, int myRow, int myCol, int dir) {
        int[] rowMove = {-1,0,1,0};
        int[] colMove = {0,1,0,-1};
        boolean isSide = dir == 1 || dir == 3;

        // int numMove = 1;
        // boolean pushing = false;
        // int faceRow = myRow + rowMove[dir];
        // int faceCol = myCol + colMove[dir];
        ArrayList<int[]> boxes2Move = new ArrayList<int[]>();
        ArrayList<int[]> frontPos = new ArrayList<int[]>();
        ArrayList<int[]> nextFront = new ArrayList<int[]>();

        int[] myPos = {myRow, myCol};
        boxes2Move.add(myPos);
        nextFront.add(myPos);
        while (true) {
            // Update front positions
            frontPos = nextFront;
            nextFront = new ArrayList<int[]>();
            // Analyze each front position
            boolean allClear = true;
            for (int i = 0; i < frontPos.size(); i++) {
                int[] myNextFront = {frontPos.get(i)[0] + rowMove[dir], frontPos.get(i)[1] + colMove[dir]};
                switch (objMap.get(myNextFront[0])[myNextFront[1]]) {
                    case 0:
                        // Clear!

                        break;
                    case 1:
                    case 2:
                        // Case Left or right
                        allClear = false;
                        if (!hasPos(boxes2Move, myNextFront)) {
                            boxes2Move.add(myNextFront);
                            nextFront.add(myNextFront);
                        }
                        if (!isSide) {
                            int sideShift = 1;
                            if (objMap.get(myNextFront[0])[myNextFront[1]] == 2) {
                                sideShift = -1;
                            }
                            int[] rightPos = {myNextFront[0], myNextFront[1] + sideShift};
                            if (!hasPos(boxes2Move, rightPos)) {
                                boxes2Move.add(rightPos);
                                nextFront.add(rightPos);
                            }
                        }
                        break;
                    case 3:
                        // blocked!
                        return myPos;
                }
            }

            if (allClear) {
                // Move all of boxes2Move up!
                for (int i = boxes2Move.size()-1; i >= 0; i--) {
                    objMap.get(boxes2Move.get(i)[0] + rowMove[dir])[boxes2Move.get(i)[1] + colMove[dir]] = objMap.get(boxes2Move.get(i)[0])[boxes2Move.get(i)[1]];
                    objMap.get(boxes2Move.get(i)[0])[boxes2Move.get(i)[1]] = 0;
                }
                int[] facePos = {myRow + rowMove[dir], myCol + colMove[dir]};
                return facePos;
            }
        }
    }

    public static boolean hasPos(ArrayList<int[]> myPoss, int[] myPos) {
        for (int i = 0; i < myPoss.size(); i++) {
            if (myPoss.get(i)[0] == myPos[0] && myPoss.get(i)[1] == myPos[1]) {
                return true;
            }
        }
        return false;
    }

    public static long getGPS(ArrayList<int[]> objMap) {
        long GPSScore = 0;
        for (int i = 0; i < objMap.size(); i++) {
            for (int j = 0; j < objMap.get(0).length; j++) {
                if (objMap.get(i)[j] == 1) {
                    GPSScore += 100*i + j;
                }
            }
        }
        return GPSScore;
    }

    public static void printObjMap(ArrayList<int[]> objMap) {
        for (int i = 0; i < objMap.size(); i++) {
            for (int j = 0; j < objMap.get(0).length; j++) {
                char printChar = 'X';
                switch (objMap.get(i)[j]) {
                    case 0:
                        printChar = '.';
                        break;
                    case 1:
                        printChar = 'O';
                        break;
                    case 2:
                        printChar = '#';
                        break;
                }
                System.out.print(printChar);
            }
            System.out.println();
        }
    }

    public static void printWideMap(ArrayList<int[]> objMap) {
       printWideMap(objMap, -1, -1);
    }

    public static void printWideMap(ArrayList<int[]> objMap, int rowPos, int colPos) {
        for (int i = 0; i < objMap.size(); i++) {
            for (int j = 0; j < objMap.get(0).length; j++) {
                char printChar = 'X';
                if (rowPos == i && colPos == j) {
                    printChar = '@';
                } else {
                    switch (objMap.get(i)[j]) {
                        case 0 -> printChar = '.';
                        case 1 -> printChar = '[';
                        case 2 -> printChar = ']';
                        case 3 -> printChar = '#';
                    }
                }
                System.out.print(printChar);
            }
            System.out.println();
        }
    }
}

