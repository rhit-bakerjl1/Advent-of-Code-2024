import java.io.File;
import java.util.*;

public class day24 {
    public static void main(String[] args) {
        File day24_input = new File("day24.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day24_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        HashMap<String, Boolean> code2Bool = new HashMap<String, Boolean>();
        ArrayList<String[]> daPendants = new ArrayList<String[]>();
        boolean isDoingGates = false;
        while (scan.hasNextLine()) {
            String newLine = scan.nextLine();
            if (newLine.length() == 0) {
                isDoingGates = true;
            } else if (!isDoingGates) {
                code2Bool.put(newLine.substring(0,3), newLine.charAt(5) == '1');
            } else {
                daPendants.add(new String[4]);
                daPendants.get(daPendants.size()-1)[0] = newLine.substring(0,3);
                daPendants.get(daPendants.size()-1)[1] = newLine.substring(4,7);
                String next;
                String third;
                if (daPendants.get(daPendants.size()-1)[1].equals("OR ")) {
                    daPendants.get(daPendants.size()-1)[1] = "OR";
                    next = newLine.substring(7,10);
                    third = newLine.substring(14,17);
                } else {
                    next = newLine.substring(8,11);
                    third = newLine.substring(15,18);
                }
                daPendants.get(daPendants.size()-1)[2] = next;
                daPendants.get(daPendants.size()-1)[3] = third;
            }
        }

        long xNum = findNumber('x', code2Bool);
        long yNum = findNumber('y', code2Bool);
        long zNum = xNum + yNum;
        System.out.println("xNum: " + xNum + "\nyNum: " + yNum + "\nzNum: " + zNum);

        HashMap<String, Boolean> code2BoolFinished = (HashMap<String, Boolean>)code2Bool.clone();
        long myNum = getZScore(daPendants, code2BoolFinished);
        System.out.println("Solved all the codes!");

        ArrayList<String[]> swapList = new ArrayList<String[]>();
        String[] newAdd1 = {"gdd", "z27"};
        String[] newAdd2 = {"hhp", "dmq"};
        String[] newAdd3 = {"z14", "kqj"};
        String[] newAdd4 = {"gpm", "z13"};
        swapList.add(newAdd1);
        swapList.add(newAdd2);
        swapList.add(newAdd3);
        swapList.add(newAdd4);
        long mySecondNum = getZScore(daPendants, code2Bool, swapList);
        System.out.println("mySecondNum: " + mySecondNum + " which should be " + zNum);
        System.out.println(alphabetAndPrint(swapList));

        int numOff = compareNum(zNum, myNum);
        System.out.println("Output number: " + myNum + ", which is " + numOff + " off of " + zNum);

        System.out.println(getFourPairs(daPendants));

        // long newZScore = getZScore(daPendants, code2Bool, swapCodes);
        // System.out.println("Output number: " + newZScore + ", which should be " + zNum);
    }  

    public static String getFourPairs(ArrayList<String[]> daPendants) {
        ArrayList<String> myDaPendants = new ArrayList<String>();
        for (int i = 0; i < daPendants.size(); i++) {
            myDaPendants.add(daPendants.get(i)[3]);
        }
        
        ArrayList<String[]> possiblePairs = new ArrayList<String[]>();
        for (int i = 0; i < myDaPendants.size(); i++) {
            String dap1 = myDaPendants.get(i);
            for (int j = i+1; j < myDaPendants.size(); j++) {
                String[] newPair = {dap1, myDaPendants.get(j)};
                possiblePairs.add(newPair);
            }
        }

        // ArrayList<Integer> timesDeleted = new ArrayList<Integer>();
        // for (int i = 0; i < possiblePairs.size(); i++) {
        //     timesDeleted.add(0);
        // }
        // // while (possiblePairs.size() > 4) {
        // for (int i = 0; i < 20; i++) {
        //     HashMap<String, Boolean> code2Bool = newCodesToBools();
        //     long zNum = findNumber('x', code2Bool) + findNumber('y', code2Bool);
        //     long newZ = getZScore(daPendants, code2Bool);
        //     int numRemoved = removePairs(daPendants, possiblePairs, code2Bool, zNum, newZ, timesDeleted);
        //     // System.out.println(possiblePairs.size() + " (-" + numRemoved + ")");
        // }

        // ArrayList<String[]> sortedList = new ArrayList<String[]>();
        // ArrayList<Integer> sortedNums = new ArrayList<Integer>();
        // for (int i = 0; i < possiblePairs.size(); i++) {
        //     boolean placed = false;
        //     String[] myPair = possiblePairs.get(i);
        //     int myDeleted = timesDeleted.get(i);
        //     for (int j = 0; j < sortedNums.size(); j++) {
        //         if (timesDeleted.get(i) < sortedNums.get(j)) {
        //             sortedList.add(j, myPair);
        //             sortedNums.add(j, myDeleted);
        //             placed = true;
        //             break;
        //         }
        //     }
        //     if (!placed) {
        //         sortedList.add(myPair);
        //         sortedNums.add(myDeleted);
        //     }
        // }

        // ArrayList<String[]> myBestSwaps = new ArrayList<String[]>();
        // for (int i = 0; i < possiblePairs.size(); i++) {
        //     System.out.println(possiblePairs.get(i)[0] + "," + possiblePairs.get(i)[1] + " was deleted " + possiblePairs.get(i) + " times.");
        //     // if (myBestSwaps.size() < 4 && !myBestSwaps.contains(sortedList.get(i)[0]) && !myBestSwaps.contains(sortedList.get(i)[1])) {
        //         myBestSwaps.add(possiblePairs.get(i));
        //         // myGuess.add(sortedList.get(i));
        //     // }
        // }

        // ArrayList<String[][]> mySwapOps = new ArrayList<String[][]>();
        for (int i = 0; i < possiblePairs.size(); i++) {
            String[] swap0 = possiblePairs.get(i);
            for (int j = i+1; j < possiblePairs.size(); j++) {
                String[] swap1 = possiblePairs.get(j);
                if (!anyInCommon(swap0, swap1)) {
                    // for (int k = j+1; k < myBestSwaps.size(); k++) {
                        String[] swap2 = {"vhm","z14"};
                        if (!anyInCommon(swap0, swap1, swap2)) {
                            // for (int l = k+1; l < myBestSwaps.size(); l++) {
                                String[] swap3 = {"cnk","qwf"};
                                if (!anyInCommon(swap0, swap1, swap2, swap3)) {
                                    ArrayList<String[]> swapThese = new ArrayList<String[]>();
                                    swapThese.add(swap0);
                                    swapThese.add(swap1);
                                    swapThese.add(swap2);
                                    swapThese.add(swap3);
                                    int numCorrect = testSwaps(daPendants, swapThese);
                                    if (numCorrect > 10) {
                                        printSwapThese(swapThese, numCorrect);
                                    }
                                    if (numCorrect == 100) {
                                        System.out.println("We've found it! It's the truth!");
                                        return "";
                                    }
                                }
                            // }
                        }
                    // }
                }
            }
            if (i % 10 == 0) {
                System.out.println("Finished i = " + i);
            }
        }

        // return alphabetAndPrint(myBestSwaps);
        return "";
    }

    public static int testSwaps(ArrayList<String[]> daPendants, ArrayList<String[]> swapThese) {
        int numCorrect = 0;
        for (int i = 0; i < 100; i++) {
            HashMap<String, Boolean> code2Bool = newCodesToBools();
            long correctZ = findNumber('x', code2Bool) + findNumber('y', code2Bool);
            long newZScore = getZScore(daPendants, code2Bool, swapThese);
            if (correctZ == newZScore) {
                numCorrect++;
            } else {
                break;
            }
        }
        return numCorrect;
    }

    public static int removePairs(ArrayList<String[]> daPendants, ArrayList<String[]> possiblePairs, HashMap<String, Boolean> code2Bool, long zNum1, long zNum2, ArrayList<Integer> timesDeleted) {
        // Find which values could have changed the incorrect outputs.
        ArrayList<String> noChange = new ArrayList<String>();
        ArrayList<String> doChange = new ArrayList<String>();
        ArrayList<String> changesBoth = new ArrayList<String>();
        getGoodBadPendants(daPendants, code2Bool, zNum1, zNum2, noChange, doChange, changesBoth);

        // System.out.println("No Change: " + noChange.size() + "; Do Change: " + doChange.size() + "; Changes Both: " + changesBoth.size() + "; Total: " + (noChange.size() + doChange.size() + changesBoth.size()));
        // System.out.println("Max Number: " + daPendants.size());

        // for (int i = 0; i < doChange.size(); i++) {
        //     try {
        //         timesInvolved.put(doChange.get(i), timesInvolved.get(doChange.get(i)) + 1);
        //     } catch (Exception e) {
        //         timesInvolved.put(doChange.get(i), 1);
        //     }
        // }

        // Removal strategy: 
        //      - IF it only contributes to bad positions, value should probably change (doChange). If swapping wouldn't change, invalid swap.
        //      - If it only contributes to good positions, value should probably not change (noChange). If swapping would change, invalid swap.
        //      - If it could have affected both good and bad, either swap is likely valid.
        //      - Apply logic to both sides of the pair.
        int numRemoved = 0;
        for (int i = 0; i < possiblePairs.size(); i++) {
            String pair1 = possiblePairs.get(i)[0];
            String pair2 = possiblePairs.get(i)[1];
            boolean areDifferent = code2Bool.get(pair1) ^ code2Bool.get(pair2);
            boolean doRemove = false;
            if (areDifferent && noChange.contains(pair1)) {
                // Don't change a noChange
                doRemove = true;
                timesDeleted.set(i, timesDeleted.get(i)+1);
            } else if (!areDifferent && doChange.contains(pair1)) {
                // Do change a doChange
                doRemove = true;
                timesDeleted.set(i, timesDeleted.get(i)+1);
            } else if (areDifferent && noChange.contains(pair2)) {
                doRemove = true;
                timesDeleted.set(i, timesDeleted.get(i)+1);
            } else if (areDifferent && doChange.contains(pair2)) {
                doRemove = true;
                timesDeleted.set(i, timesDeleted.get(i)+1);
            }

            if (doRemove) {
            //     possiblePairs.remove(i);
            //     i--;
                numRemoved++;
            }
        }

        return numRemoved;
    }
    
    public static long getZScore(ArrayList<String[]> daPendants, HashMap<String, Boolean> code2Bool) {
        return getZScore(daPendants, code2Bool, new ArrayList<String[]>());
    }

    public static long getZScore(ArrayList<String[]> daPendants, HashMap<String, Boolean> code2Bool, ArrayList<String[]> swapCodes) {
        ArrayList<String[]> daPendantsClone = (ArrayList<String[]>)daPendants.clone();
        for (int i = 0; i < daPendantsClone.size(); i++) {
            daPendantsClone.set(i,daPendantsClone.get(i).clone());
        }

        for (int i = 0; i < swapCodes.size(); i++) {
            swapThese(swapCodes.get(i), daPendantsClone);
        }

        while (!daPendantsClone.isEmpty()) {
            int numDeleted = 0;
            for (int i = 0; i < daPendantsClone.size(); i++) {
                String operation = daPendantsClone.get(i)[1];
                try {
                    boolean bool1 = code2Bool.get(daPendantsClone.get(i)[0]);
                    boolean bool2 = code2Bool.get(daPendantsClone.get(i)[2]);
                    boolean newBool;
                    if (operation.equals("AND")) {
                        newBool = bool1 && bool2;
                    } else if (operation.equals("OR")) {
                        newBool = bool1 || bool2;
                    } else {
                        newBool = bool1 ^ bool2;
                    }
                    String swapStr = daPendantsClone.get(i)[3];
                    code2Bool.put(swapStr, newBool);
                    daPendantsClone.remove(i);
                    numDeleted++;
                    i--;
                } catch (Exception e) {
                }
            }
            // System.out.println("We solved " + numDeleted + " new codes!");
            if (numDeleted == 0) {
                break;
            }
        }
        return findNumber('z', code2Bool);
    }

    public static long findNumber(char firstChar, HashMap<String, Boolean> code2Bool) {
        // Go through the Z's!
        boolean stopFlag = false;
        int zNum = 0;
        long myNum = 0;
        while (!stopFlag) {
            String myZs = "" + firstChar;
            myZs += (zNum / 10);
            myZs += (zNum % 10);
            try {
                if (code2Bool.get(myZs)) {
                    myNum += (long)Math.pow(2,zNum);
                }
            } catch (Exception e) {
                stopFlag = true;
            }
            zNum++;
        }
        return myNum;
    }

    public static int compareNum(long num1, long num2) {
        return compareNum(num1, num2, new ArrayList<String>(), new ArrayList<String>());
    }

    public static int compareNum(long num1, long num2, ArrayList<String> noChange, ArrayList<String> doChange) {
        int maxPow = 0;
        while ((long)Math.pow(2,maxPow) <= num1 && (long)Math.pow(2,maxPow) <= num2) {
            maxPow++;
        }
        int numWrong = 0;
        for (int i = maxPow; i >= 0; i--) {
            long bigNum = (long)Math.pow(2,i);
            boolean changedFirst = false;
            boolean changedSecond = false;
            if (num1 >= bigNum) {
                num1 -= bigNum;
                changedFirst = true;
            }
            if (num2 >= bigNum) {
                num2 -= bigNum;
                changedSecond = true;
            }
            String doDontChange = "z" + i/10 + "" + i%10;
            if (changedFirst ^ changedSecond) {
                numWrong++;
                doChange.add(doDontChange);
            } else {
                noChange.add(doDontChange);
            }
        }
        // System.out.println(numWrong + "/" + maxPow);

        return numWrong;
    }

    public static HashMap<String, Boolean> newCodesToBools() {
        HashMap<String, Boolean> code2Bool = new HashMap<String, Boolean>();
        int max2Pow = 44;
        long maxNum = (long)(Math.pow(2,max2Pow+1)-1);
        long xNum = (long)(Math.random()*maxNum);
        long yNum = (long)(Math.random()*maxNum);

        for (int i = max2Pow; i >= 0; i--) {
            long bigNum = (long)(Math.pow(2,i));
            String strX = "x" + i/10 + "" + i%10;
            String strY = "y" + i/10 + "" + i%10;
            if (xNum >= bigNum) {
                xNum -= bigNum;
                code2Bool.put(strX, true);
            } else {
                code2Bool.put(strX, false);
            }
            if (yNum >= bigNum) {
                yNum -= bigNum;
                code2Bool.put(strY, true);
            } else {
                code2Bool.put(strY, false);
            }
        }

        return code2Bool;
    }

    public static void getGoodBadPendants(ArrayList<String[]> daPendants, HashMap<String, Boolean> code2Bool, long zNum1, long zNum2, ArrayList<String> noChange, ArrayList<String> doChange, ArrayList<String> changesBoth) {
        compareNum(zNum1, zNum2, noChange, doChange);
        traceEffect(daPendants, code2Bool, noChange);
        traceEffect(daPendants, code2Bool, doChange);

        for (int i = 0; i < noChange.size(); i++) {
            for (int j = 0; j < doChange.size(); j++) {
                if (noChange.get(i).equals(doChange.get(j))) {
                    changesBoth.add(noChange.get(i));
                    noChange.remove(i);
                    i--;
                    doChange.remove(j);
                    j--;
                }
            }
        }
    }

    public static void traceEffect(ArrayList<String[]> daPendants, HashMap<String, Boolean> code2Bool, ArrayList<String> wouldChange) {
        for (int i = 0; i < wouldChange.size(); i++) {
            for (int j = 0; j < daPendants.size(); j++) {
                if (daPendants.get(j)[3].equals(wouldChange.get(i))) {
                    String code1 = daPendants.get(j)[0];
                    String code2 = daPendants.get(j)[2];
                    boolean addC1 = false;
                    boolean addC2 = false;
                    boolean canAddC1 = !wouldChange.contains(code1) && code1.charAt(0) != 'x' && code1.charAt(0) != 'y';
                    boolean canAddC2 = !wouldChange.contains(code2) && code2.charAt(0) != 'x' && code2.charAt(0) != 'y';
                    if (daPendants.get(j)[1].equals("XOR")) {
                        addC1 = canAddC1;
                        addC2 = canAddC2;
                    } else if (daPendants.get(j)[1].equals("OR")) {
                        // If the other is true, changing would have no effect. If false, it would have effect.
                        if (!code2Bool.get(code2)) {
                            addC1 = canAddC1;
                        }
                        if (!code2Bool.get(code1)) {
                            addC2 = canAddC2;
                        }
                    } else {
                        // And. If the other is false, changing would have no effect. If true, it would have effect.
                        if (code2Bool.get(code2)) {
                            addC1 = canAddC1;
                        }
                        if (code2Bool.get(code1)) {
                            addC2 = canAddC2;
                        }
                    }
                    // Add codes
                    if (addC1) {
                        wouldChange.add(code1);
                    }
                    if (addC2) {
                        wouldChange.add(code2);
                    }
                }
            }
        }
    }

    public static void swapThese(String[] codes, ArrayList<String[]> daPendants) {
        int ind1 = -1;
        int ind2 = -1;
        for (int i = 0; i < daPendants.size(); i++) {
            if (daPendants.get(i)[3].equals(codes[0])) {
                ind1 = i;
                if (ind2 != -1) {
                    break;
                }
            }
            if (daPendants.get(i)[3].equals(codes[1])) {
                ind2 = i;
                if (ind1 != -1) {
                    break;
                }
            }
        }

        if (ind1 != -1 && ind2 != -1) {
            String temp = daPendants.get(ind1)[3];
            daPendants.get(ind1)[3] = daPendants.get(ind2)[3];
            daPendants.get(ind2)[3] = temp;
        }
    }

    public static boolean anyInCommon(String[] grp1, String[] grp2) {
        return grp1[0].equals(grp2[0]) || grp1[0].equals(grp2[1]) || grp1[1].equals(grp2[0]) || grp1[1].equals(grp2[1]);
    }
    
    public static boolean anyInCommon(String[] grp1, String[] grp2, String[] grp3) {
        return anyInCommon(grp1, grp3) || anyInCommon(grp2, grp3);
    }

    public static boolean anyInCommon(String[] grp1, String[] grp2, String[] grp3, String[] grp4) {
        return anyInCommon(grp1, grp4) || anyInCommon(grp2, grp4) || anyInCommon(grp3, grp4);
    }

    public static void printSwapThese(ArrayList<String[]> swapThese, int numCorrect) {
        System.out.println("These got: " + numCorrect + " correct!");
        System.out.print("    ");
        alphabetAndPrint(swapThese);
    }

    public static String alphabetAndPrint(ArrayList<String[]> toSort) {
        ArrayList<String> sortedList = new ArrayList<String>();
        ArrayList<String> notSortedYet = new ArrayList<String>();

        for (int i = 0; i < toSort.size(); i++) {
            for (int j = 0; j < toSort.get(0).length; j++) {
                notSortedYet.add(toSort.get(i)[j]);
            }
        }

        while (!notSortedYet.isEmpty()) {
            String nextToSort = notSortedYet.get(0);
            boolean sorted = false;
            for (int i = 0; i < sortedList.size(); i++) {
                if (nextToSort.compareTo(sortedList.get(i)) < 0) {
                    sortedList.add(i, nextToSort);
                    sorted = true;
                    break;
                }
            }
            if (!sorted) {
                sortedList.add(nextToSort);
            }
            notSortedYet.remove(0);
        }

        String returnStr = "";
        for (int i = 0; i < sortedList.size(); i++) {
            returnStr += sortedList.get(i) + ',';
        }
        returnStr = returnStr.substring(0,returnStr.length()-1);
        System.out.println(returnStr);
        return returnStr;
    }
}

