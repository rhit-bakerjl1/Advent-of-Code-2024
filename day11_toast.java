import java.io.File;
import java.util.*;

// We're toast!
public class day11_toast {
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
            countStones += blink25(stones.get(j), numBlinks, 0);
            System.out.println("Finished blinking #" + j + ": " + stones.get(j) + ", countStones = " + countStones);
        }

        System.out.println("Number of stones: " + countStones);

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("I just took " + (double)totalTime*Math.pow(10,-9) + " s to run!");
    }   

    // Returns two integers, being the first and second number. -1 means no second number.
    public static long blink25(long stoneNum, int goalBlink, int currBlink) {
        if (currBlink >= goalBlink) {
            return 1;
        }
        int blinksAtATime = 25;

        ArrayList<Long> myStones = new ArrayList<Long>();
        myStones.add(stoneNum);

        for (int i = 0; i < blinksAtATime; i++) {
            for (int j = myStones.size() - 1; j >= 0; j--) {
                if (myStones.get(j) == 0) {
                    myStones.set(j, (long)1);
                } else if (numDigits(myStones.get(j)) % 2 == 0) {
                    // even digits
                    long[] returnInts = new long[2];
                    int halfDigits = numDigits(myStones.get(j))/2;
                    // left
                    returnInts[0] = (myStones.get(j) / (long)Math.pow(10,halfDigits));
                    // right
                    returnInts[1] = myStones.get(j) - (long)(returnInts[0]*Math.pow(10,halfDigits));
                    myStones.set(j, returnInts[0]);
                    myStones.add(returnInts[1]);
                }
                else {
                    myStones.set(j, myStones.get(j)*2024);
                }
            }
        }

        currBlink+= blinksAtATime;
        if (currBlink >= goalBlink)  {
            return myStones.size();
        } 

        long mySum = 0;
        for (int i = 0; i < myStones.size(); i++) {
            mySum += blink25(myStones.get(i), goalBlink, currBlink);
        }
        return mySum;
    }

    public static int numDigits(long stoneNum) {
        int numDigit = 1;
        long numTens = 10;
        while (numTens <= stoneNum) {
            numTens *= 10;
            numDigit++;
        }
        return numDigit;
    }
}