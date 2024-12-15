import java.io.File;
import java.util.*;

public class day11 {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        File day11_input = new File("day11.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day11_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<Long> stones = new ArrayList<Long>();
        while (scan.hasNextLong()) {
            stones.add(scan.nextLong());
        }

        int numBlinks = 75;
        double countStones = 0;
        for (int j = 0; j < stones.size(); j++) {
            countStones += blink(stones.get(j), numBlinks, 0);
            System.out.println("Finished blinking #" + j + ": " + stones.get(j) + ", countStones = " + countStones);
        }
        // for (int i = 0; i < numBlinks; i++) {
        //     // Blink once
        //     for (int j = stones.size()-1; j >= 0; j--) {
        //         long[] newStones = blink(stones.get(j));
        //         stones.set(j, newStones[0]);
        //         if (newStones[1] != -1) {
        //             stones.add(j+1, newStones[1]);
        //         }
        //     }
        // }

        // long maxStone = Long.MIN_VALUE;
        // long minStone = Long.MAX_VALUE;
        // for (int i = 0; i < stones.size(); i++) {
        //     if (stones.get(i) > maxStone) {
        //         maxStone = stones.get(i);
        //     }
        //     if (stones.get(i) < minStone) {
        //         minStone = stones.get(i);
        //     }
        // }

        // Count 'em up!
        // long stoneCount = stones.size();
        System.out.println("Number of stones: " + countStones);
        // System.out.println("Min Stone: " + minStone);
        // System.out.println("Max Stone: " + maxStone);
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("I just took " + (double)totalTime*Math.pow(10,-9) + " s to run!");
    }   

    // Returns two integers, being the first and second number. -1 means no second number.
    public static long blink(long stoneNum, int goalBlink, int currBlink) {
        if (currBlink == goalBlink) {
            return 1;
        }
        // if (stoneNum < 0) {
            // System.out.println("NEGATIVE OH NO: " + stoneNum);
        // }
        if (stoneNum == 0) {
            // long[] returnInts = {1,-1};
            return blink(1, goalBlink, currBlink + 1);
            // return returnInts;
        } else if (numDigits(stoneNum) % 2 == 0) {
            // even digits
            long[] returnInts = new long[2];
            int halfDigits = numDigits(stoneNum)/2;
            // left
            returnInts[0] = (stoneNum / (long)Math.pow(10,halfDigits));
            // right
            returnInts[1] = stoneNum - (long)(returnInts[0]*Math.pow(10,halfDigits));
            return blink(returnInts[0], goalBlink, currBlink + 1) + blink(returnInts[1], goalBlink, currBlink + 1);
            // return returnInts;   
        }
        else {
            // long[] returnInts = {stoneNum*2024, -1};
            // if (returnInts[0] >= 405855977472.0) {
            //     System.out.println("So big! " + returnInts[0]);
            // }
            // if (returnInts[0] < 0) {
            //     System.out.println("NEGATIVE OH NO");
            // }
            return blink(stoneNum*2024, goalBlink, currBlink + 1);
            // return returnInts;
        }
    }

    public static int numDigits(long stoneNum) {
        // if (stoneNum < 0) {
            // System.out.println("NEGATIVE OH NO: " + stoneNum);
        // }
        int numDigit = 1;
        long numTens = 10;
        while (numTens <= stoneNum) {
            numTens *= 10;
            numDigit++;
        }
        return numDigit;
    }
}