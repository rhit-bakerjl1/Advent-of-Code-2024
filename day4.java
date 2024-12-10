import java.io.File;
import java.util.*;

public class day4 {
    public static void main(String[] args) {
        File day4_input = new File("day4.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day4_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<String> day4Data = new ArrayList<String>();
        while (scan.hasNextLine()) {
            day4Data.add(scan.nextLine());
        }

        int countXmas = 0;
        int countX_mas = 0;
        for (int row = 0; row < day4Data.size(); row++) {
            for (int col = 0; col < day4Data.get(0).length(); col++) {
                countXmas += isXmas(day4Data, row, col);
                countX_mas += isX_MAS(day4Data, row, col);
            }
        }

        System.out.println("We found " + countXmas + " Xmas's!");
        System.out.println("We found " + countX_mas + " X-MAS's!");

    }   

    // Direction: 
    // 0: North;  1: NorthEast;  2: East;  3: SouthEast
    // 4: South;  5: SouthWest;  6: West;  7: NorthWest
    public static int isXmas(ArrayList<String> data, int row, int col) {
        int[] endRow = {-3,-3,0,3,3,3,0,-3};
        int[] endCol = {0,3,3,3,0,-3,-3,-3};
        // Get direction strings
        int countXmas = 0;
        String isXmasStr;
        if (data.get(row).charAt(col) != 'X') {
            return 0;
        }
        for (int i = 0; i < endRow.length; i++) {
            // Test if it's in bounds
            if (row + endRow[i] < 0 || row + endRow[i] >= data.size()) {
                // Row out of bounds
            } else if (col + endCol[i] < 0 || col + endCol[i] >= data.get(0).length()) {
                // Col out of bounds
            } else {
                // Make String
                isXmasStr = "X";
                for (int j = 1; j <= 3; j++) {
                    isXmasStr += String.valueOf(data.get(row + endRow[i]*j/3).charAt(col + endCol[i]*j/3));
                }
                if (isXmasStr.equals("XMAS")) {
                    countXmas++;
                }
            }
        }
        return countXmas;
    }

    public static int isX_MAS(ArrayList<String> data, int row, int col) {
        // Get direction strings
        int countXmas = 0;
        String isXmasStr;
        if (data.get(row).charAt(col) != 'A') {
            return 0;
        }
        if (row - 1 < 0 || row + 1 >= data.size()) {
            // Row out of bounds
            return 0;
        } else if (col - 1 < 0 || col + 1 >= data.get(0).length()) {
            // Col out of bounds
            return 0;
        } else {
            isXmasStr = String.valueOf(data.get(row - 1).charAt(col - 1));
            isXmasStr += String.valueOf(data.get(row - 1).charAt(col + 1));
            isXmasStr += String.valueOf(data.get(row + 1).charAt(col + 1));
            isXmasStr += String.valueOf(data.get(row + 1).charAt(col - 1));
            if (isXmasStr.equals("MMSS") || isXmasStr.equals("SMMS") || isXmasStr.equals("SSMM") || isXmasStr.equals("MSSM")) {
                return 1;
            }
        }
        return 0;
    }
}