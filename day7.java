import java.io.File;
import java.util.*;

public class day7 {
    public static void main(String[] args) {
        File day7_input = new File("day7.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day7_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }
        test();

        long count = 0;
        int lines = 0;
        while (scan.hasNextLine()) {
            count += testNextLine(scan.nextLine());
            lines++;
        }
        System.out.println("Total count: " + count);
        System.out.println("We evaluated " + lines + " lines");
        
    }

    public static void test() {
        ArrayList<Long> nums = new ArrayList<Long>();
        nums.add((long)1);
        nums.add((long)7);
        nums.add((long)5);
        nums.add((long)9);
        nums.add((long)0);
        nums.add((long)4);
        nums.add((long)6);
        nums.add((long)3);
        nums.add((long)7);
        nums.add((long)9);
        nums.add((long)0);
        nums.add((long)6);
        nums.add((long)5);
        nums.add((long)4);
        nums.add((long)1);
        int[] pmCode = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] pmCode2 = {2,2,2,2,2,2,2,2,2,2,2,2,2,2};
        System.out.println(getResult(nums,pmCode2));
        System.out.println(testAllResults((long)175904637906541.0, nums, pmCode));
    }

    public static long testNextLine(String nextLine) {
        String[] parts = nextLine.split(":");
        Long goalNum = Long.parseLong(parts[0]);
        Scanner scan2 = new Scanner(parts[1]);
        ArrayList<Long> lineInts = new ArrayList<Long>();
        while (scan2.hasNextLong()) {
            lineInts.add(scan2.nextLong());
        }

        // pmCode = 0: +
        // pmCode = 1: *
        int[] pmCode = new int[lineInts.size()-1];
        for (int i = 0; i < pmCode.length; i++) {
            pmCode[i] = 0;
        }
        if (testAllResults(goalNum, lineInts, pmCode)) {
            return goalNum;
        }
        return 0;

    }

    public static boolean testAllResults(long goalNum, ArrayList<Long> nums, int[] pmCodeHelper) {
        long numLeft;
        // Part 1
        // for (int i = 0; i < (int)Math.pow(2,pmCodeHelper.length); i++) {
        //     // Based on counting in binary
        //     numLeft = i;
        //     for (int j = pmCodeHelper.length-1; j >= 0; j--) {
        //         if (numLeft >= (int)Math.pow(2,j)) {
        //             numLeft -= (int)Math.pow(2,j);
        //             pmCodeHelper[j] = 1;
        //         } else {
        //             pmCodeHelper[j] = 0;
        //         }
        //     }
        //     if (getResult(nums, pmCodeHelper) == goalNum) {
        //         return true;
        //     }
        // }
        for (long i = 0; i < (long)Math.pow(3,pmCodeHelper.length); i++) {
            // Based on counting in trinary
            numLeft = i;
            for (int j = pmCodeHelper.length-1; j >= 0; j--) {
                if (numLeft >= Math.pow(3,j)*2) {
                    numLeft -= Math.pow(3,j)*2;
                    pmCodeHelper[j] = 2;
                } else if (numLeft >= Math.pow(3,j)) {
                    numLeft -= Math.pow(3,j);
                    pmCodeHelper[j] = 1;
                }
                else {
                    pmCodeHelper[j] = 0;
                }
            }
            if (getResult(nums, pmCodeHelper) == goalNum) {
                return true;
            }
        }
        return false;
    }

    public static long getResult(ArrayList<Long> nums, int[] pmCode) {
        long sum = nums.get(0);
        for (int i = 0; i < pmCode.length; i++) {
            // Part 1
            // if (pmCode[i] == 0) {
            //     // Add
            //     sum += nums.get(i+1);
            // } else {
            //     // Mult
            //     sum *= nums.get(i+1);
            // }
            if (pmCode[i] == 0) {
                // Add
                sum += nums.get(i+1);
            } else if (pmCode[i] == 2) {
                // Concat
                long numTens = 1;
                while (numTens <= nums.get(i+1)) {
                    numTens*=10;
                }
                sum *= numTens;
                sum += nums.get(i+1);
            }else {
                // Mult
                sum *= nums.get(i+1);
            }
        }
        return sum;
    }
}