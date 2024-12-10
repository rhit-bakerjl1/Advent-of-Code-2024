import java.io.File;
import java.util.*;

public class day5 {
    public static void main(String[] args) {
        File day5_input = new File("day5.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day5_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        // Parse through data
        ArrayList<int[]> myRules = new ArrayList<int[]>();
        ArrayList<ArrayList<Integer>> myUpdates = new ArrayList<ArrayList<Integer>>();
        boolean parseRules = true;
        String currLine;
        Scanner scanLine;
        int numUpdates = 0;
        while (scan.hasNextLine()) {
            if (parseRules) {
                currLine = scan.nextLine();
                // Test if we need to swap
                if (currLine.length() == 0) {
                    parseRules = false;
                } else {
                    // Find the rule
                    scanLine = new Scanner(currLine);
                    String[] parts = currLine.split("[|]");
                    int[] newRule = {Integer.parseInt(parts[0]),Integer.parseInt(parts[1])};
                    myRules.add(newRule);
                }
            } else {
                // Find the guys
                currLine = scan.nextLine();
                scanLine = new Scanner(currLine);
                myUpdates.add(new ArrayList<Integer>());
                String[] parts = currLine.split("[,]");
                for (int i = 0; i < parts.length; i++) {
                    myUpdates.get(numUpdates).add(Integer.parseInt(parts[i]));
                }
                numUpdates++;
            }
        }

        int count = 0;
        int wrongCount = 0;
        int result;
        for (int i = 0; i < myUpdates.size(); i++) {
            result = testAllRules(myRules, myUpdates.get(i));
            if (result < 0) {
                wrongCount -= result;
            } else {
                count += result;
            }
        }

        System.out.println("Middle numbers: " + count);
        System.out.println("Incorrect middle numbers: " + wrongCount);
    }

    public static void test() {

    }

    public static int testAllRules(ArrayList<int[]> myRules, ArrayList<Integer> myPages) {
        // Test if it passes every rule
        int midIndex = (myPages.size())/2;
        for (int i = 0; i < myRules.size(); i++) {
            if (!testRule(myRules.get(i), myPages)) {
                // List is out of order! Sort list.
                myPages = sortList(myRules, myPages);
                return -myPages.get(midIndex);
            }
        }

        // Return the middle number
        return myPages.get(midIndex);
    }
    
    public static boolean testRule(int[] myRule, ArrayList<Integer> myPages) {
        int index1  = -1;
        int index2  = -1;
        // Get indices for both numbers
        for (int i = 0; i < myPages.size(); i++) {
            if (myPages.get(i) == myRule[0]) {
                index1 = i;
            }
            if (myPages.get(i) == myRule[1]) {
                index2 = i;
            }
        }
        if (index1 == -1 || index2 == -1) {
            // One of two numbers were not found.  Passes rule.
            return true;
        }
        // True if first number comes before second number.
        return index1 < index2;
    }

    public static ArrayList<Integer> sortList(ArrayList<int[]> myRules, ArrayList<Integer> myPages) {
        ArrayList<Integer> sortedList = new ArrayList<Integer>();
        // Sorting strategy: find minimum, put at start
        int currBegin;
        int minInd;
        while (!myPages.isEmpty()){
            if (myPages.size() == 1) {
                sortedList.add(myPages.get(0));
                myPages.remove(0);
            } else {
                // More than 2 elements!
                currBegin = myPages.get(0);
                minInd = 0;
                for (int i = 1; i < myPages.size(); i++) {
                    if (!compare(myRules, currBegin, myPages.get(i))) {
                        currBegin = myPages.get(i);
                        minInd = i;
                    }
                }
                // Switch
                sortedList.add(currBegin);
                myPages.remove(minInd);
            }
        }

        return sortedList;
    }

    public static boolean compare(ArrayList<int[]> myRules, int num1, int num2) {
        int rule1;
        int rule2;
        for (int i = 1; i < myRules.size(); i++) {
            rule1 = myRules.get(i)[0];
            if (rule1 == num1) {
                rule2 = myRules.get(i)[1];
                if (rule2 == num2) {
                    // In order!
                    return true;
                }
            } else if (rule1 == num2) {
                rule2 = myRules.get(i)[1];
                if (rule2 == num1) {
                    // Out of order!
                    return false;
                }
            }
        }
        return true;
    }
}