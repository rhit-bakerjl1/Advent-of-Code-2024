import java.io.File;
import java.util.*;

public class day16 {
    public static void main(String[] args) {
        File day16_input = new File("day16.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day16_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        int[] startPos = new int[2];
        int[] endPos = new int[2];
        ArrayList<int[]> obsMap = new ArrayList<int[]>();
        int numRow = 0;
        while (scan.hasNextLine()) {
            String nextLine = scan.nextLine();
            int[] nextObs = new int[nextLine.length()];
            for (int i = 0; i < nextObs.length; i++) {
                char nextChar = nextLine.charAt(i);
                switch (nextChar) {
                case '#': 
                    nextObs[i] = -1;
                    break;
                case '.':
                    nextObs[i] = 0;
                    break;
                case 'E': 
                    nextObs[i] = 0;
                    endPos[0] = numRow;
                    endPos[1] = i;
                    break;
                case 'S':
                    nextObs[i] = 0;
                    startPos[0] = numRow;
                    startPos[1] = i;
                }
            }
            obsMap.add(nextObs);
            numRow++;
        }

        // printMap(obsMap);
        popMap(obsMap, startPos, endPos);
        printMap(obsMap);
        System.out.println();
        int numBest = countBest(obsMap, startPos, endPos);

        System.out.println("Lowest Score: " + obsMap.get(startPos[0])[startPos[1]]);
        System.out.println("Number of tiles on best routes: " + numBest);
    }   

    public static void popMap(ArrayList<int[]> obsMap, int[] startPos, int[] endPos) {
        // queue positions: {row, col, dir, score}, dir = 0,1,2,3 for N,E,S,W, 4 for any
        ArrayList<int[]> queuePos = new ArrayList<int[]>();
        int[] firstPos = {endPos[0], endPos[1], 4, 0};
        queuePos.add(firstPos);

        int[] rowInc = {-1,0,1,0};
        int[] colInc = {0,1,0,-1};

        while (!queuePos.isEmpty()) {
            ArrayList<int[]> queueQueue = new ArrayList<int[]>();
            // Variable setup for ease of reading
            int currRow = queuePos.get(0)[0];
            int currCol = queuePos.get(0)[1];
            int currDir = queuePos.get(0)[2];
            int currScore = queuePos.get(0)[3];
            obsMap.get(currRow)[currCol] = currScore;
            for (int i = 0; i < 4; i++) {
                // Variable setup for ease of reading
                int nextRow = currRow + rowInc[i];
                int nextCol = currCol + colInc[i];
                if (obsMap.get(nextRow)[nextCol] != -1) {
                    int nextDir = i;
                    int nextScore = currScore + 1;
                    if (nextDir != currDir && currDir != 4) {
                        nextScore += 1000;
                    }
                    if (nextRow == startPos[0] && nextCol == startPos[1] && nextDir != 3) {
                        nextScore += 1000;
                        if (nextDir == 1) {
                            nextScore += 1000;
                        }
                    }
                    if (obsMap.get(nextRow)[nextCol] == 0 || nextScore < obsMap.get(currRow)[currCol]) {
                        int[] myQueue = {nextRow, nextCol, nextDir, nextScore};
                        queueQueue.add(myQueue);
                    }
                }
            }

            queuePos.remove(0);
            for (int i = 0; i < queueQueue.size(); i++) {
                int addInd = queuePos.size();
                boolean isSet = false;
                for (int j = 0; j < queuePos.size(); j++) {
                    if (queuePos.get(j)[3] > queueQueue.get(i)[3] && !isSet) {
                        addInd = j;
                        isSet = true;
                    }
                    if (queuePos.get(j)[0] == queueQueue.get(i)[0] && queuePos.get(j)[1] == queueQueue.get(i)[1]) {
                        // If wanting to override a position
                        if (!isSet) {
                            addInd = -1;
                            break;
                        } else {
                            queuePos.remove(j);
                        }
                    }
                }
                if (addInd >= 0) {
                    if (addInd < queuePos.size()) {
                        queuePos.add(addInd, queueQueue.get(i));
                    } else {
                        queuePos.add(queueQueue.get(i));
                    }
                }
            }
        }
    }

    public static int countBest(ArrayList<int[]> obsMap, int[] startPos, int[] endPos) {
        // Map of best paths
        ArrayList<int[]> myPath = new ArrayList<int[]>();
        for (int i = 0; i < obsMap.size(); i++) {
            myPath.add(new int[obsMap.get(0).length]);
            for (int j = 0; j < obsMap.get(0).length; j++) {
                myPath.get(i)[j] = 0;
            }
        }

        ArrayList<int[]> checkRoute;
        ArrayList<int[]> nextRoute = new ArrayList<int[]>();
        ArrayList<int[]> lastRoute;
        ArrayList<int[]> nextLastRoute = new ArrayList<int[]>();
        int[] rowInc = {-1,0,1,0};
        int[] colInc = {0,1,0,-1};
        nextRoute.add(startPos);
        nextLastRoute.add(startPos);
        boolean stopFlag = false;
        boolean isFirst = true;

        // Use stopFlag when we've reached the very end.
        while (!stopFlag) {
            lastRoute = nextLastRoute;
            nextLastRoute = new ArrayList<int[]>();
            checkRoute = nextRoute;
            nextRoute = new ArrayList<int[]>();
            for (int i = 0; i < checkRoute.size(); i++) {
                int[] currTile = {checkRoute.get(i)[0], checkRoute.get(i)[1]};
                int[] lastTile = {lastRoute.get(i)[0], lastRoute.get(i)[1]};
                if (myPath.get(currTile[0])[currTile[1]] != 1) {
                    myPath.get(currTile[0])[currTile[1]] = 1;
                    for (int j = 0; j < rowInc.length; j++) {
                        int[] nextTile = {currTile[0] + rowInc[j], currTile[1] + colInc[j]};
                        int tileDiff = obsMap.get(currTile[0])[currTile[1]] - obsMap.get(nextTile[0])[nextTile[1]];
                        boolean isCross = Math.abs(nextTile[0] - lastTile[0]) == 1;
                        int crossDiff = obsMap.get(lastTile[0])[lastTile[1]] - obsMap.get(nextTile[0])[nextTile[1]];
                        // if (isCross && crossDiff < 1000) {
                        //     System.out.println("No no no!");
                        // }
                        if (obsMap.get(nextTile[0])[nextTile[1]] != -1 && (isFirst || ((tileDiff > 0 || tileDiff == -999) && !(crossDiff < 1000 && isCross)))) {
                            isFirst = false;
                            nextRoute.add(nextTile);
                            nextLastRoute.add(currTile);
                        }
                        if (nextTile[0] == endPos[0] && nextTile[1] == endPos[1]) {
                            stopFlag = true;
                        }
                    }
                }
            }
            if (nextRoute.isEmpty()) {
                stopFlag = true;
            }

        }

        printPath(myPath);

        int count = 0;
        for (int i = 0; i < myPath.size(); i++) {
            for (int j = 0; j < myPath.get(0).length; j++) {
                count += myPath.get(i)[j];
            }
        }
        count++;    // For the end spot!
        return count;
    }

    public static void printMap(ArrayList<int[]> obsMap) {
        for (int i = 0; i < obsMap.size(); i++) {
            for (int j = 0; j < obsMap.get(0).length; j++) {
                if (obsMap.get(i)[j] != -1) {
                    System.out.print(obsMap.get(i)[j] + " ");
                    if (obsMap.get(i)[j] < 1000) {
                        System.out.print("   ");
                    }
                    if (obsMap.get(i)[j] < 10000) {
                        System.out.print(" ");
                    }
                } else {
                    System.out.print("#     ");
                }
            }
            System.out.println();
        }
    }

    public static void printPath(ArrayList<int[]> myPath) {
        for (int i = 0; i < myPath.size(); i++) {
            for (int j = 0; j < myPath.get(0).length; j++) {
                if (myPath.get(i)[j] == 0) {
                    System.out.print('.');
                } else {
                    System.out.print("O");
                }
            }
            System.out.println();
        }
    }
}

