import java.io.File;
import java.util.*;

public class day12 {
    public static void main(String[] args) {
        File day12_input = new File("day12.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day12_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<String> data = new ArrayList<String>();
        while (scan.hasNextLine()) {
            data.add(scan.nextLine());
        }

        int numRows = data.size();
        int numCols = data.get(0).length();
        int[][] isCounted = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                isCounted[i][j] = 0;
            }
        }

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (isCounted[row][col] == 0) {
                    isCounted = assignRegionScores(data, isCounted, row, col);
                }
            }
        }

        int areas = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                areas += isCounted[row][col];
            }
        }

        for (int i = 0; i < isCounted.length; i++) {
            for (int j = 0; j < isCounted[0].length; j++) {
                System.out.print(isCounted[i][j]);
            }
            System.out.println("");
        }

        System.out.println("Total count: " + areas);
    }   

    public static int[][] assignRegionScores(ArrayList<String> data, int[][] isCounted, int row, int col) {
        // Strategy: assign perimeter to each box. If it hasn't been counted, see which others around it are in 
        // the same box and count them.
        isCounted[row][col] = -1;

        int numRows = data.size();
        int numCols = data.get(0).length();
        ArrayList<int[]> positionsInRegion = new ArrayList<int[]>();
        int[] rowcol = {row, col};
        positionsInRegion.add(rowcol);
        char myChar = data.get(row).charAt(col);

        // Part 2: Pass on 2 v 3 > 5 ^ 7 <. 
        int[] infoDrop = {2,3,5,7};
        ArrayList<Integer> myInfo = new ArrayList<Integer>();
        ArrayList<int[]> pos2Add;
        ArrayList<int[]> pos2Update;
        myInfo.add(1);

        int[] rowAdd = {-1,0,1,0};
        int[] colAdd = {0,1,0,-1};
        int perimeter = 0;
        // Finding the perimeter
        for (int i = 0; i < positionsInRegion.size(); i++) {
            pos2Add = new ArrayList<int[]>();
            pos2Update = new ArrayList<int[]>();
            for (int j = 0; j < rowAdd.length; j++) {
                int newRow = positionsInRegion.get(i)[0] + rowAdd[j];
                int newCol = positionsInRegion.get(i)[1] + colAdd[j];
                if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols && myChar == data.get(newRow).charAt(newCol)) {
                    int[] newRowcol = {newRow, newCol};
                    if (isCounted[newRow][newCol] == 0) {
                        // Part 1
                        // positionsInRegion.add(newRowcol);
                        // isCounted[newRow][newCol] = -1;
                        // Part 2
                        isCounted[newRow][newCol] = -1;
                        pos2Add.add(newRowcol);
                    } else {
                        // We've seen this one before!
                        // Part 2 (comms part 1)
                        pos2Update.add(newRowcol);
                    }
                } else {
                    // perimeter++;
                    // Part 2
                    // if (-isCounted[positionsInRegion.get(i)[0]][positionsInRegion.get(i)[1]] % infoDrop[j] != 0 && myInfo.get(i) % infoDrop[j] != 0) {
                    if (-isCounted[positionsInRegion.get(i)[0]][positionsInRegion.get(i)[1]] % infoDrop[j] != 0) {
                        // Part 1
                        // Increase perimeter if no fence from that direction and 
                        perimeter++;
                    }
                    myInfo.set(i, myInfo.get(i)*infoDrop[j]);
                }
            }

            // Part 2: Add to ArrayList!
            for (int j = 0; j < pos2Add.size(); j++) {
                positionsInRegion.add(pos2Add.get(j));
                for (int k = 0; k < infoDrop.length; k++) {
                    if (-isCounted[pos2Add.get(j)[0]][pos2Add.get(j)[1]] % infoDrop[k] != 0 && myInfo.get(i) % infoDrop[k] == 0) {
                        isCounted[pos2Add.get(j)[0]][pos2Add.get(j)[1]] *= infoDrop[k];
                    }
                }
                myInfo.add(1);
            }
            // Update the isCounted values
            for (int j = 0; j < pos2Update.size(); j++) {
                for (int k = 0; k < infoDrop.length; k++) {
                    if (-isCounted[pos2Update.get(j)[0]][pos2Update.get(j)[1]] % infoDrop[k] != 0 && myInfo.get(i) % infoDrop[k] == 0) {
                        isCounted[pos2Update.get(j)[0]][pos2Update.get(j)[1]] *= infoDrop[k];        
                    }
                }
            }
        }

        // System.out.println("Region of " + myChar);
        // for (int i = 0; i < isCounted.length; i++) {
        //     for (int j = 0; j < isCounted[0].length; j++) {
        //         System.out.print(isCounted[i][j]);
        //     }
        //     System.out.println("");
        // }
        // System.out.println("");

        for (int i = 0; i < positionsInRegion.size(); i++) {
            isCounted[positionsInRegion.get(i)[0]][positionsInRegion.get(i)[1]] = perimeter;
        }

        return isCounted;
    }
}

