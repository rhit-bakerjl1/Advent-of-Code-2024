import java.io.File;
import java.util.*;

public class day20 {
    public static void main(String[] args) {
        File day20_input = new File("day20.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day20_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<int[]> raceTrack = new ArrayList<int[]>();
        int numRows = 0;
        int[] startPos = new int[2];
        int[] endPos = new int[2];
        while (scan.hasNextLine()) {
            String nextRow = scan.nextLine();
            raceTrack.add(new int[nextRow.length()]);
            for (int i = 0; i < nextRow.length(); i++) {
                char nextChar = nextRow.charAt(i);
                // -2: empty, -1: occupied
                switch (nextChar) {
                    case '#':
                        raceTrack.get(numRows)[i] = -1;
                        break;
                    case 'S':
                        startPos[0] = numRows;
                        startPos[1] = i;
                        raceTrack.get(numRows)[i] = -2;
                        break;
                    case 'E':
                        endPos[0] = numRows;
                        endPos[1] = i;
                        raceTrack.get(numRows)[i] = -2;
                        break;
                    default:
                        raceTrack.get(numRows)[i] = -2;
                        break;
                }
            }
            numRows++;
        }

        mapMap(raceTrack, endPos);
        printMap(raceTrack);
        int timeSave = 100;
        int myShortcuts = numShortcuts(raceTrack, timeSave);
        long myIllShortcuts = numIllShortcuts(raceTrack, timeSave);
        System.out.println("There are " + myShortcuts + " legal shortcuts that save at least " + timeSave + " picoseconds");
        System.out.println("There are " + myIllShortcuts + " illegal shortcuts that save at least " + timeSave + " picoseconds");
    }   

    public static void mapMap(ArrayList<int[]> raceTrack, int[] endPos) {
        ArrayList<int[]> checkPos = new ArrayList<int[]>();
        ArrayList<int[]> nextPos = new ArrayList<int[]>();
        nextPos.add(endPos);
        int num2Assign = 0;
        raceTrack.get(endPos[0])[endPos[1]] = num2Assign;

        int[] rowInc = {-1,0,1,0};
        int[] colInc = {0,1,0,-1};
        while (nextPos.size() > 0) {
            checkPos = nextPos;
            nextPos = new ArrayList<int[]>();
            num2Assign++;
            for (int i = 0; i < checkPos.size(); i++) {
                int[] currPos = {checkPos.get(i)[0], checkPos.get(i)[1]};
                for (int j = 0; j < rowInc.length; j++) {
                    int[] newPos = {currPos[0] + rowInc[j], currPos[1] + colInc[j]};
                    if (raceTrack.get(newPos[0])[newPos[1]] == -2) {
                        raceTrack.get(newPos[0])[newPos[1]] = num2Assign;
                        nextPos.add(newPos);
                    }
                }
            }
        }
    }

    public static void printMap(ArrayList<int[]> raceTrack) {
        for (int i = 0; i < raceTrack.size(); i++) {
            for (int j = 0; j < raceTrack.get(0).length; j++) {
                if (raceTrack.get(i)[j] == -1) {
                    System.out.print("#  ");
                } else {
                    System.out.print(raceTrack.get(i)[j]);
                    if (raceTrack.get(i)[j] < 10) {
                        System.out.print(" ");
                    }
                    if (raceTrack.get(i)[j] < 100) {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }

    public static int numShortcuts(ArrayList<int[]> raceTrack, int timeSave) {
        int[] rowInc = {-1,0,1,0};
        int[] colInc = {0,1,0,-1};

        int cutCount = 0;
        for (int i = 0; i < raceTrack.size(); i++) {
            for (int j = 0; j < raceTrack.get(0).length; j++) {
                if (raceTrack.get(i)[j] == -1) {
                    int[] surrScores = new int[rowInc.length];
                    int maxScore = Integer.MIN_VALUE;
                    int minScore = Integer.MAX_VALUE;
                    for (int k = 0; k < rowInc.length; k++) {
                        int[] nextPos = {i + rowInc[k], j + colInc[k]};
                        if (nextPos[0] >= 0 && nextPos[1] >= 0 && nextPos[0] < raceTrack.size() && nextPos[1] < raceTrack.get(0).length) {
                            surrScores[k] = raceTrack.get(i + rowInc[k])[j + colInc[k]];
                            if (surrScores[k] > maxScore) {
                                maxScore = surrScores[k];
                            }
                            if (surrScores[k] != -1 && surrScores[k] < minScore) {
                                minScore = surrScores[k];
                            }
                        }
                    }
                    int myCut = maxScore - minScore - 2;
                    if (minScore != Integer.MAX_VALUE && myCut >= timeSave) {
                        // System.out.println("We saved " + myCut + " picoseconds!");
                        cutCount++;
                    }
                }
            }
        }
        return cutCount;
    }

    public static long numIllShortcuts(ArrayList<int[]> raceTrack, int timeSave) {
        int maxCut = 20;
        long cutCount = 0;
        for (int i = 0; i < raceTrack.size(); i++) {
            for (int j = 0; j < raceTrack.get(0).length; j++) {
                int score1 = raceTrack.get(i)[j];
                if (score1 != -1) {
                    int rowStart = i - maxCut;
                    // int rowStart = i;
                    int rowEnd = i + maxCut;
                    if (rowStart < 0) {
                        rowStart = 0;
                    }
                    if (rowEnd >= raceTrack.size()) {
                        rowEnd = raceTrack.size() - 1;
                    }
                    for (int row = rowStart; row <= rowEnd; row++) {
                        int colStart = (int)Math.abs(row - i) - maxCut + j;
                        int colEnd = maxCut + j - (int)Math.abs(row - i);
                        if (colStart < 0) {
                            colStart = 0;
                        }
                        if (colEnd >= raceTrack.get(0).length) {
                            colEnd = raceTrack.get(0).length - 1;
                        }
                        for (int col = colStart; col <= colEnd; col++) {
                            int dist = (int)(Math.abs(row - i) + Math.abs(col - j));
                            // if (dist > maxCut) {
                            //     System.out.println("Yikers! Oh no! Dist = " + dist);
                            // }
                            int score2 = raceTrack.get(row)[col];
                            if (score2 != -1) {
                                int myCut = (int)Math.abs(score2 - score1) - dist;
                                // if (myCut >= timeSave) {
                                if (myCut >= timeSave) {
                                    cutCount++;
                                    // System.out.println("Saved " + myCut + " picoseconds!");
                                }
                            }
                        }
                    }
                }
            }
        }
        return cutCount/2;
    }
}

