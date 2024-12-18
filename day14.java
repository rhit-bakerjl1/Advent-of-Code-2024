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
        // int numSecs = 100;

        for (int i = 1; i < 30000; i++) {
            long myLine = safetyFactor(robotPosVel, numRows, numCols, i, false);
            if (myLine > 300) {
                System.out.println("Time = " + i + " s, Line Score = " + myLine);
            }
        }

        long myLine = safetyFactor(robotPosVel, numRows, numCols, 17440, true);

        // long fos = safetyFactor(robotPosVel, numRows, numCols, numSecs, true);

        // System.out.println("Safety factor: " + fos);

        // int halfway = (numCols-1)/2;
        // int botCount = 0;
        // for (int i = 0; i < numRows; i++) {
        //     for (int j = 0; j < numCols; j++) {
        //         if (j == halfway + i || j == halfway - i || i == halfway || j == halfway) {
        //             System.out.print('1');
        //             botCount++;
        //             robotGrid[i][j] = 1;
        //         } else {
        //             System.out.print('.');
        //             robotGrid[i][j] = 0;
        //         }
        //     }
        //     System.out.println();
        // }

        // System.out.println("There are " + botCount + " bots in the tree");

        // int myLine = lineScore(robotGrid);
        // System.out.println("RobotGrid lineScore: " + myLine);

    }   

    public static long safetyFactor(ArrayList<int[]> robotPosVel, int numRows, int numCols, int numSecs, boolean display) {
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
        for (int i = 0; i < robotGrid.length; i++) {
            for (int j = 0; j < robotGrid[0].length; j++) {
                if (robotGrid[i][j] != 0) {
                    if (display) {
                        System.out.print(robotGrid[i][j]);
                    }
                } else {
                    if (display) {
                        System.out.print(".");
                    }
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
            if (display) {
                System.out.println();
            }
        }

        long fos = 1;
        for (int i = 0; i < qsData.length; i++) {
            fos*= qsData[i];
        }

        // return fos;
        return lineScore(robotGrid);
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

    public static int lineScore(int[][] robotGrid) {
        int numRows = robotGrid.length;
        int numCols = robotGrid[0].length;

        // Horizontal Count
        int horizCount = 0;
        for (int i = 0; i < numRows; i++) {
            horizCount += consecScore(robotGrid[i]);
        }

        // Vertical Count
        int vertCount = 0;
        int[] myCol = new int[numRows];
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                myCol[j] = robotGrid[j][i];
            }
            vertCount += consecScore(myCol);
        }

        // Diagonal Count
        int upLeftCount = 0;
        for (int i = numCols-1; i >= 0; i--) {
            int[] myDiag = new int[numCols - i];
            for (int j = 0; j < myDiag.length; j++) {
                myDiag[j] = robotGrid[j][i+j];
            }
            upLeftCount += consecScore(myDiag);
        }
        for (int i = 1; i < numRows - numCols; i++) {
            int[] myDiag = new int[numCols];
            for (int j = 0; j < myDiag.length; j++) {
                myDiag[j] = robotGrid[i+j][j];
            }
            upLeftCount += consecScore(myDiag);
        }
        for (int i = numRows - numCols; i < numRows; i++) {
            int[] myDiag = new int[numRows - i];
            for (int j = 0; j < myDiag.length; j++) {
                myDiag[j] = robotGrid[i+j][j];
            }
            upLeftCount += consecScore(myDiag);
        }

        // Diagonal other count
        int upRightCount = 0;
        for (int i = 0; i < numCols; i++) {
            int[] myDiag = new int[i];
            for (int j = 0; j < myDiag.length; j++) {
                myDiag[j] = robotGrid[j][i-j];
            }
            upRightCount += consecScore(myDiag);
        }
        for (int i = 1; i < numRows - numCols; i++) {
            int[] myDiag = new int[numCols];
            for (int j = myDiag.length-1; j >= 0; j--) {
                myDiag[j] = robotGrid[numCols - j + i][j];
            }
            upRightCount += consecScore(myDiag);
        }
        for (int i = numRows - numCols; i < numRows; i++) {
            int[] myDiag = new int[numRows - i];
            for (int j = 0; j < myDiag.length; j++) {
                myDiag[j] = robotGrid[i+j][numCols-j-1];
            }
            upRightCount += consecScore(myDiag);
        }

        return horizCount + vertCount + upLeftCount + upRightCount;
    }

    public static int consecScore(int[] nums) {
        int score = 0;
        int consec = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                consec += nums[i];
            } else {
                if (consec > 3) {
                    score += consec;
                }
                consec = 0;
            }
        }
        score += consec;

        return score;
    }
}