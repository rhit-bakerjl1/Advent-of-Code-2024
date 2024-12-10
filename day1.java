import java.io.*;
import java.util.*;

public class day1 {
    
    public static void main(String[] args) {
        File day1_input = new File("day1.txt");
        ArrayList<Integer> nums1 = new ArrayList<Integer>();
        ArrayList<Integer> nums2 = new ArrayList<Integer>();
        Scanner scan;
        try {
            // Get the information
            scan = new Scanner(day1_input);           

        } catch (Exception e) {
            System.out.println("Coulnd't find your text file.\n");
            return;
        }  
        
        // Getting all the information
        while (scan.hasNextLine()) {
            nums1.add(scan.nextInt());
            nums2.add(scan.nextInt());
        }

        // Sorting the numbers
        nums1.sort(null);
        nums2.sort(null);
        int sum = 0;
        int prod = 0;

        for (int i = 0; i < nums1.size(); i++) {
            sum += Math.abs(nums1.get(i) - nums2.get(i));
            prod += nums1.get(i)*day1.findInArrList(nums1.get(i),nums2);
        }

        System.out.println("Total sum of differences: " + sum);
        System.out.println("Total Score: " + prod);
    }

    public static int findInArrList(int num, ArrayList<Integer> nums) {
        int numFound = 0;
        for (int i = 0; i < nums.size(); i++) {
            if (nums.get(i) == num) {
                numFound++;
            }
        }
        return numFound;
    }
}