import java.io.File;
import java.util.*;

public class day9 {
    public static void main(String[] args) {
        File day9_input = new File("day9.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day9_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        String data = scan.nextLine();
        // System.out.println(data);
        // System.out.println(data.length() + " characters.");

        // Interpret data
        int dataID = 0;
        boolean isEmpty = false;
        ArrayList<Integer> mySpace = new ArrayList<Integer>();
        for (int i = 0; i < data.length(); i++ ) {
            int dataNum = Integer.parseInt(data.substring(i,i+1));
            if (isEmpty) {
                for (int j = 0; j < dataNum; j++) {
                    // Use -1 as a dot
                    mySpace.add(-1);
                }
            }
            else {
                for (int j = 0; j < dataNum; j++) {
                    mySpace.add(dataID);
                }
                dataID++;
            }
            isEmpty = !isEmpty;
        }

        // for (int i = 0; i < mySpace.size(); i++) {
        //     if (mySpace.get(i) == -1) {
        //         System.out.print(".");
        //     } else {
        //         System.out.print(mySpace.get(i));
        //     }
        // }
        // System.out.println("");

        // Now sort the data!
        int firstSpace = 0;
        int lastSpace = mySpace.size() - 1;
        boolean stopFlag = false;
        while (!stopFlag) {
            // Increase firstSpace until it's at a space.
            // Part 1
            // while (firstSpace < mySpace.size() && mySpace.get(firstSpace) != -1) {
            //     firstSpace++;
            // }
            while (firstSpace < lastSpace && mySpace.get(firstSpace) != -1) {
                firstSpace++;
            }

            // Delete the ends until there is a digit at the end
            // Part 1
            // while (firstSpace < mySpace.size() && mySpace.get(mySpace.size() - 1) == -1) {
            //     mySpace.remove(mySpace.size() - 1);
            // }
            while (firstSpace < lastSpace && mySpace.get(lastSpace) == -1) {
                lastSpace--;
            }

            // CraigSwap
            // Part 1
            // if (firstSpace < mySpace.size()) {
            //     // Do the swap
            //     mySpace.set(firstSpace, mySpace.get(mySpace.size() - 1));
            //     mySpace.set(mySpace.size() - 1, -1);
            // } else {
            //     // We need to stop
            //     stopFlag = true;
            // }
            if (firstSpace < lastSpace) {
                // See if you can do the swap.
                int firstLen = findFile(mySpace, firstSpace, 1);
                int lastLen = findFile(mySpace, lastSpace, -1);
                if (lastLen > firstLen) {
                    // Go back and find us more space
                    firstSpace += firstLen;
                } else {
                    // Replace
                    for (int i = 0; i < lastLen; i++ ) {
                        mySpace.set(firstSpace + i, mySpace.get(lastSpace - i));
                        mySpace.set(lastSpace - i, -1);
                    }

                    // Increment
                    // firstSpace += lastLen;
                    firstSpace = 0;
                    lastSpace -= lastLen;
                }
            } else {
                // There's no space left!
                int lastLen = findFile(mySpace, lastSpace, -1);
                lastSpace -= lastLen;
                firstSpace = 0;
            }
            if (lastSpace <= 0) {
                stopFlag = true;
            }

        }

        // Count the score
        long checksum = 0;
        int loc = 0;
        for (int i = 0; i < mySpace.size(); i++) {
            if (mySpace.get(i) != -1) {
                // System.out.print(mySpace.get(i));
                checksum += i*mySpace.get(i);
                loc++;
            } else {
                // System.out.print(".");
            }
        }

        // System.out.println("");
        System.out.println("The checksum is: " + checksum);
    }   

    // Returns number of blocks in the same file
    // dir = -1: backward
    // dir = 1: forward
    public static int findFile(ArrayList<Integer> data, int outInd, int dir) {
        int grpNum = data.get(outInd);
        int inARow = 1;
        while (outInd + inARow*dir >= 0 && data.get(outInd + inARow*dir) == grpNum) {
            inARow++;
        }
        if (outInd + inARow*dir < 0) {
            return 5;
        }

        return inARow;
    }
}

