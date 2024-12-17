import java.io.File;
import java.util.*;

public class day14 {
    public static void main(String[] args) {
        File day14_input = new File("day14.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day14_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        // Parsing Through Data
        ArrayList<int[]> robotPosVel = new ArrayList<int[]>();
        while (scan.hasNextLine()) {
            String nextLine = scan.nextLine();
            int numOn = 0;
            int nextInt = 0;
            boolean started = false;
            int neg = 1;
            robotPosVel.add(new int[4]);
            for (int i = 0; i < nextLine.length(); i++) {
                if (Character.isDigit(nextLine.charAt(i))) {
                    started = true;
                    nextInt *= 10;
                    nextInt += nextLine.charAt(i) - '0';
                } else if (nextLine.charAt(i) == '-') {
                    neg = -1;
                } else {
                    if (started && numOn < 4) {
                        robotPosVel.get(robotPosVel.size()-1)[numOn] = nextInt*neg;
                        numOn++;
                        nextInt = 0;
                        neg = 1;
                        started = false;
                    }
                }
            }
            robotPosVel.get(robotPosVel.size()-1)[numOn] = nextInt*neg;

        }

        int numRows = 103;
        int numCols = 101;
        // numRows = 7;
        // numCols = 11;
        int numSecs = 100;

        int[][] robotGrid = new int[numRows][numCols];
        for (int i = 0; i < robotGrid.length; i++) {
            for (int j = 0; j < robotGrid[1].length; j++) {
                robotGrid[i][j] = 0;
            }
        }

        for (int i = 0; i < robotPosVel.size(); i++) {
            int[] finalPos = getLastPos(robotPosVel.get(i), numRows, numCols, numSecs);
            robotGrid[finalPos[0]][finalPos[1]]++;
        }

        int[] qsData = {0,0,0,0};
        int midCol = (numCols-1)/2;
        int midRow = (numRows-1)/2;
        int numBots = 0;
        for (int i = 0; i < robotGrid.length; i++) {
            for (int j = 0; j < robotGrid[0].length; j++) {
                if (robotGrid[i][j] != 0) {
                    System.out.print(robotGrid[i][j]);
                    numBots += robotGrid[i][j];
                } else {
                    System.out.print(".");
                }
                if (i < midRow) {
                    // Q1 or Q2
                    if (j < midCol) {
                        qsData[0] += robotGrid[i][j];
                    } else if (j > midCol) {
                        qsData[1] += robotGrid[i][j];
                    }
                } else if (i > midRow) {
                    // Q3 or Q4
                    if (j < midCol) {
                        qsData[2] += robotGrid[i][j];
                    } else if (j > midCol) {
                        qsData[3] += robotGrid[i][j];
                    }
                }
            }
            System.out.println();
        }

        long fos = 1;
        for (int i = 0; i < qsData.length; i++) {
            fos*= qsData[i];
        }

        System.out.println("Num bots: " + numBots);
        System.out.println("Safety factor: " + fos);

    }   

    public static int[] getLastPos(int[] posVelData, int numRows, int numCols, int numSecs) {
        int posX = posVelData[0];
        int posY = posVelData[1];
        int velX = posVelData[2];
        int velY = posVelData[3];

        int[] lastPos = {(posY + numSecs*velY) % numRows,(posX + numSecs*velX) % numCols};
        while (lastPos[0] < 0) {
            lastPos[0] += numRows;
        }
        while (lastPos[1] < 0) {
            lastPos[1] += numCols;
        }
        return lastPos;
    }
}