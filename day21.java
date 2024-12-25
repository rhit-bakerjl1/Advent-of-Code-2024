import java.io.File;
import java.util.*;

public class day21 {
    public static ArrayList<HashMap<String,String>> nextStrArr;
    public static HashMap<String, Long> next5Num;

    public static void main(String[] args) {
        File day21_input = new File("day21.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day21_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        nextStrArr = new ArrayList<HashMap<String,String>>();
        next5Num = new HashMap<String,Long>();
        for (int i = 0; i < 4; i++) {
            nextStrArr.add(new HashMap<String,String>());
        }
        ArrayList<String> codes = new ArrayList<String>();
        while (scan.hasNextLine()) {
            codes.add(scan.nextLine());
        }
        // Set up robot keypads
        int numChainBots = 25;
        char[][] rob1Keys = {{'7','8','9'},{'4','5','6'},{'1','2','3'},{'D','0','A'}};
        char[][] rob2Keys = {{'D','^','A'},{'<','v','>'}};

        long complexity = 0;
        for (int i = 0; i < codes.size(); i++) {
            String rob1Commands = findMyCommands(codes.get(i), rob1Keys);
            String nextRobCommands = rob1Commands;
            // System.out.println(codes.get(i) + "\n" + nextRobCommands);
            long robSum25 = findNCommands(nextRobCommands, rob2Keys, numChainBots);

            // System.out.println();

            int numericPortion = 0;
            for (int j = 0; j < codes.get(i).length(); j++) {
                if (codes.get(i).charAt(j) != 'A') {
                    numericPortion *= 10;
                    numericPortion += codes.get(i).charAt(j) - '0';
                }
            }
            complexity += numericPortion*robSum25;
            System.out.println("We finished " + codes.get(i));
        }

        System.out.println("Total complexity: " + complexity);
    } 
    
    // Finds commands for the robot in order to press the desired string, but allows the next robot to choose.
    public static String findMyCommands(String myChars, char[][] hisKeys) {
        String myCommands = "";
        char lastChar;
        char currChar = 'A';

        for (int i = 0; i < myChars.length(); i++) {
            lastChar = currChar;
            currChar = myChars.charAt(i);

            int[] startNDist = findDist2(lastChar, currChar, hisKeys);
            myCommands += chooseDir(startNDist, hisKeys);
            myCommands += 'A';
            
        }
        return myCommands;
    }

    public static long findNCommands(String myChars, char[][] hisKeys, int numChainBots) {
        String nextCmd = findMyCommands(myChars, hisKeys);
        if (numChainBots == 1) {
            return nextCmd.length();
        }
        int numCheat = 15;
        if (numChainBots == numCheat) {
            try {
                long cheatSum = next5Num.get(myChars);
                return cheatSum;
            } catch (Exception e) {
            }
        }

        ArrayList<String> mySepChars = sepStrs(nextCmd);
        long mySum = 0;
        for (int i = 0; i < mySepChars.size(); i++) {
            mySum += findNCommands(mySepChars.get(i), hisKeys, numChainBots-1);
        }
        if (numChainBots == numCheat) {
            next5Num.put(myChars, mySum);
        }
        return mySum;
    } 

    public static ArrayList<String> sepStrs(String str) {
        String myStr = "";
        char newChar;
        ArrayList<String> myStrs = new ArrayList<String>();
        for (int i = 0; i < str.length(); i++) {
            newChar = str.charAt(i);
            myStr += newChar;
            if (newChar == 'A') {
                myStrs.add(myStr);
                myStr = "";
            }
        }
        if (myStr.length() > 0) {
            myStrs.add(myStr);
        }
        return myStrs;
    }

    // Chooses the path from current position to the end position
    public static String chooseDir(int[] startNDist, char[][] hisKeys) {
        // Find if we can't go row or col first.
        int[] pos1 = {startNDist[0], startNDist[1]};
        int[] hyppo = {startNDist[2], startNDist[3]};
        boolean canRowFirst = true;
        boolean canColFirst = true;
        int[] pos2 = {pos1[0] + hyppo[0], pos1[1] + hyppo[1]};
        int[] blankPos = new int[2];
        for (int i = 0; i < hisKeys.length; i++) {
            for (int j = 0; j < hisKeys[0].length; j++) {
                if (hisKeys[i][j] == 'D') {
                    blankPos[0] = i;
                    blankPos[1] = j;
                }
            }
        }
        if (blankPos[0] == pos1[0] && blankPos[1] == pos2[1]) {
            canColFirst = false;
        }
        if (blankPos[1] == pos1[1] && blankPos[0] == pos2[0]) {
            canRowFirst = false;
        }

        boolean goingRight = hyppo[1] >= 0;
        boolean goingDown = hyppo[0] >= 0;
        if (canColFirst && canRowFirst) {
            if (goingRight) {
                canColFirst = false;
            } else {
                canRowFirst = false;
            }
        }

        String returnString = "";
        if (canRowFirst) {
            // Do rows first!
            for (int i = 0; i < (int)Math.abs(hyppo[0]); i++) {
                if (goingDown) {
                    returnString += 'v';
                } else {
                    returnString += '^';
                }
            }
            for (int i = 0; i < (int)Math.abs(hyppo[1]); i++) {
                if (goingRight) {
                    returnString += '>';
                } else {
                    returnString += '<';
                }
            }
        } else {
            // Do columns first!
            for (int i = 0; i < (int)Math.abs(hyppo[1]); i++) {
                if (goingRight) {
                    returnString += '>';
                } else {
                    returnString += '<';
                }
            }
            for (int i = 0; i < (int)Math.abs(hyppo[0]); i++) {
                if (goingDown) {
                    returnString += 'v';
                } else {
                    returnString += '^';
                }
            }
        }
        return returnString;
    }

    public static int[] findDist2(char startChar, char endChar, char[][] hisKeys) {
        int[] startPos = new int[2];
        int[] endPos = new int[2];

        for (int i = 0; i < hisKeys.length; i++) {
            for (int j = 0; j < hisKeys[0].length; j++) {
                if (hisKeys[i][j] == startChar) {
                    startPos[0] = i;
                    startPos[1] = j;
                }
                if (hisKeys[i][j] == endChar) {
                    endPos[0] = i;
                    endPos[1] = j;
                }
            }
        }

        int[] startNDist = {startPos[0], startPos[1], endPos[0] - startPos[0], endPos[1] - startPos[1]};
        return startNDist;
    }

    

}

