import java.io.File;
import java.util.*;

public class day24_fresh {
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

        ArrayList<boolean[][]> traceMap = printDependents(daPendants);

        ArrayList<String[]> possSwaps = findSwaps(traceMap, daPendants);
        // System.out.println(possSwaps.size());

        // for (int i = 0; i < daPendants.get(0).length; i++) {
        //     for (int j = 0; j < daPendants.size(); j++) {
        //         System.out.println(daPendants.get(j)[i]);
        //     }
        //     System.out.println();
        // }

        HashMap<String, String> translationMap = new HashMap<String, String>();
        for (int i = 0; i < daPendants.size(); i++) {
            if (daPendants.get(i)[0].charAt(0) == 'x' || daPendants.get(i)[0].charAt(0) == 'y') {
                int myNum = getTheNumber(daPendants.get(i)[0]);
                String buildStr = "";
                if (daPendants.get(i)[1].equals("AND")) {
                    buildStr += "A";
                } else {
                    buildStr += "X";
                }
                buildStr += myNum / 10 + "" + myNum % 10;
                translationMap.put(daPendants.get(i)[3], buildStr);
            }
        }
        // All the X and As are set. Set all H, T, Z
        for (int i = 0; i < daPendants.size(); i++) {
            try {
                String newString = translationMap.get(daPendants.get(i)[3]);
                if (newString == null) {
                    throw new Exception();
                }
                String yumString = newString + " yum";
            } catch (Exception e) {
                // All these are H, T, or Z. Find number and assign value.
                String buildStr = "";
                String code1 = daPendants.get(i)[0];
                String oper = daPendants.get(i)[1];
                String code2 = daPendants.get(i)[2];
                String myKey = daPendants.get(i)[3];
                
                if (oper.equals("AND")) {
                    buildStr += "H";
                } else if (oper.equals("OR")) {
                    buildStr += "T";
                } else {
                    buildStr += "Z";
                }
                try {
                    String trans1 = translationMap.get(code1);
                    if (trans1 == null) {
                        throw new Exception();
                    }
                    int myNum = getTheNumber(trans1);
                    buildStr += myNum/10 + "" + myNum%10;
                } catch (Exception f) {
                    try {
                        String trans2 = translationMap.get(code2);
                        if (trans2 == null) {
                            throw new Exception();
                        }
                        int myNum = getTheNumber(trans2);
                        buildStr += myNum/10 + "" + myNum%10;
                    } catch (Exception g) {
                        printEQs("Doesn't Work!", daPendants.get(i), code1, oper, code2, myKey);
                    }
                }
                translationMap.put(myKey, buildStr);
            }
        }

        int numInc = 0;
        for (int i = 0; i < daPendants.size(); i++) {
            if (!lineIsCorrect(translationMap, daPendants.get(i))) {
                numInc++;
            }
        }

        System.out.println(numInc + " are incorrect");

    }

    public static boolean lineIsCorrect(HashMap<String,String> translationMap, String[] equation) {
        if (equation[0].charAt(0) == 'x' || equation[0].charAt(0) == 'y') {
            return true;
        }
        String code1 = translationMap.get(equation[0]);
        String oper = equation[1];
        String code2 = translationMap.get(equation[2]);
        String result = translationMap.get(equation[3]);

        int code1Num = getTheNumber(code1);
        int code2Num = getTheNumber(code2);
        int resultNum = getTheNumber(result);

        switch (result.charAt(0)) {
            case 'H':
                int xNum;
                int tNum;
                if (code1.charAt(0) == 'X' && code2.charAt(0) == 'T') {
                    xNum = code1Num;
                    tNum = code2Num;
                } else if (code1.charAt(0) == 'T' && code2.charAt(0) == 'X') {
                    xNum = code2Num;
                    tNum = code1Num;
                } else {
                    printEQs("Letters: ", equation, code1, oper, code2, result);
                    return false;
                }
                if (resultNum != xNum || resultNum != tNum+1) {
                    // printEQs("Numbers: ", equation, code1, oper, code2, result);
                    return false;
                }
                break;
            case 'T':
                int aNum;
                int hNum;
                if (code1.charAt(0) == 'A' && code2.charAt(0) == 'H') {
                    aNum = code1Num;
                    hNum = code2Num;
                } else if (code1.charAt(0) == 'H' && code2.charAt(0) == 'A') {
                    aNum = code2Num;
                    hNum = code1Num;
                } else {
                    printEQs("Letters: ", equation, code1, oper, code2, result);
                    return false;
                }
                if (resultNum != aNum || resultNum != hNum) {
                    // printEQs("Numbers: ", equation, code1, oper, code2, result);
                    return false;
                }
                break;
            case 'Z':
                int xNumZ;
                int tNumZ;
                if (code1.charAt(0) == 'X' && code2.charAt(0) == 'T') {
                    xNumZ = code1Num;
                    tNumZ = code2Num;
                } else if (code1.charAt(0) == 'T' && code2.charAt(0) == 'X') {
                    xNumZ = code2Num;
                    tNumZ = code1Num;
                } else {
                    printEQs("Letters: ", equation, code1, oper, code2, result);
                    return false;
                }
                if (resultNum != xNumZ || resultNum != tNumZ+1) {
                    // printEQs("Numbers: ", equation, code1, oper, code2, result);
                    return false;
                }
                break;
            
        }
        return true;
    }

    public static void printEQs(String problem, String[] equation, String code1, String oper, String code2, String result) {
        System.out.println(problem + result + " = " + code1 + " " + oper + " " + code2);
        System.out.println("\t" + equation[3] + " = " + equation[0] + " " + equation[1] + " " + equation[2]);
    }

    public static int testSwaps(ArrayList<String[]> daPendants, ArrayList<String[]> swapThese) {
        int numCorrect = 0;
        for (int i = 0; i < 100; i++) {
            HashMap<String, Boolean> code2Bool = day24.newCodesToBools();
            long correctZ = day24.findNumber('x', code2Bool) + day24.findNumber('y', code2Bool);
            long newZScore = day24.getZScore(daPendants, code2Bool, swapThese);
            if (correctZ == newZScore) {
                numCorrect++;
            } else {
                break;
            }
        }
        return numCorrect;
    }

    public static ArrayList<boolean[][]> printDependents(ArrayList<String[]> daPendants) {
        int maxX = 45;
        int maxY = maxX;
        ArrayList<boolean[][]> traceMap = new ArrayList<boolean[][]>();
        for (int i = 0; i < daPendants.size(); i++) {
            traceMap.add(new boolean[maxX][2]);
            traceEffect(daPendants, daPendants.get(i)[3], traceMap.get(i));
        }

        printDependentsInOrder(daPendants, traceMap);
        return traceMap;
    }

    public static void printDependentsInOrder(ArrayList<String[]> daPendants, ArrayList<boolean[][]> traceMap) {
        int zeroToHero = 0;
        int singleNum = 0;
        int other = 0;
        int maxZ = 46;
        for (int i = 0; i < maxZ; i++) {
            String myZ = "z" + i/10 + "" + i%10;
            System.out.print(myZ + ": ");
            boolean isZTH = true;
            boolean isSingle = true;
            boolean singleFound = false;
            for (int j = 0; j < daPendants.size(); j++) {
                if (daPendants.get(j)[3].equals(myZ)) {
                    boolean thisTrue = false;
                    boolean lastTrue = true;
                    for (int k = 0; k < traceMap.get(j).length; k++) {
                        lastTrue = thisTrue;
                        thisTrue = false;
                        if (traceMap.get(j)[k][0]) {
                            System.out.print("x" + k/10 + k%10 + ",");
                            thisTrue = true;
                        }
                        if (traceMap.get(j)[k][1]) {
                            System.out.print("y" + k/10 + k%10 + ",");
                            thisTrue = true;
                        }
                        if (thisTrue && singleFound) {
                            isSingle = false;
                        } else if (thisTrue) {
                            singleFound = true;
                        }
                        if (thisTrue && lastTrue) {
                            isSingle = false;
                        }
                        if (k != 0 && !lastTrue && thisTrue) {
                            isZTH = false;
                        }
                    }
                }
            }
            System.out.println();
            if (isSingle) {
                singleNum++;
                // System.out.println("Single!");
            }
            else if (isZTH) {
                zeroToHero++;
                // System.out.println("Zero to Hero!");
            } else {
                other++;
                // System.out.println("Other!");
            }
        }

        for (int i = 0; i < daPendants.size(); i++) {
            boolean isZTH = true;
            boolean isSingle = true;
            boolean singleFound = false;
            if (daPendants.get(i)[3].charAt(0) != 'z') {
                // System.out.print(daPendants.get(i)[3] + ": ");
                boolean thisTrue = false;
                boolean lastTrue = true;
                for (int k = 0; k < traceMap.get(i).length; k++) {
                    lastTrue = thisTrue;
                    thisTrue = false;
                    if (traceMap.get(i)[k][0]) {
                        // System.out.print("x" + k/10 + k%10 + ",");
                        thisTrue = true;
                    }
                    if (traceMap.get(i)[k][1]) {
                        // System.out.print("y" + k/10 + k%10 + ",");
                        thisTrue = true;
                    }
                    if (thisTrue && singleFound) {
                        isSingle = false;
                    } else if (thisTrue) {
                        singleFound = true;
                    }
                    if (thisTrue && lastTrue) {
                        isSingle = false;
                    }
                    if (k != 0 && !lastTrue && thisTrue) {
                        isZTH = false;
                    }
                }
                // System.out.println();
                if (isSingle) {
                    singleNum++;
                    // System.out.println("Single!");
                } else if (isZTH) {
                    zeroToHero++;
                    // System.out.println("Zero to Hero!");
                } else {
                    other++;
                    // System.out.println("Other!");
                }
            }
        }

        System.out.println("Summary: ZeroToHero: " + zeroToHero + ", Single number: " + singleNum + ", other: " + other);
    }

    public static void traceEffect(ArrayList<String[]> daPendants, String startEffect, boolean[][] xyTracker) {
        ArrayList<String> wouldChange = new ArrayList<String>();
        wouldChange.add(startEffect);
        for (int i = 0; i < wouldChange.size(); i++) {
            for (int j = 0; j < daPendants.size(); j++) {
                if (daPendants.get(j)[3].equals(wouldChange.get(i))) {
                    String[] code12 = {daPendants.get(j)[0], daPendants.get(j)[2]};

                    for (int k = 0; k < code12.length; k++) {
                        if (code12[k].charAt(0) == 'x' || code12[k].charAt(0) == 'y') {
                            int xyNum = 10*(code12[k].charAt(1) - '0') + (code12[k].charAt(2) - '0');
                            int ind2 = 0;
                            if (code12[k].charAt(0) == 'y') {
                                ind2 = 1;
                            }
                            xyTracker[xyNum][ind2] = true;
                        } else {
                            wouldChange.add(code12[k]);
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<String[]> findSwaps(ArrayList<boolean[][]> traceMap, ArrayList<String[]> daPendants) {
        ArrayList<String[]> possSwaps = new ArrayList<String[]>();
        for (int i = 0; i < daPendants.size(); i++) {
            String swap1 = daPendants.get(i)[3];
            boolean[][] xyTracker1 = traceMap.get(i);
            for (int j = i+1; j < daPendants.size(); j++) {
                String swap2 = daPendants.get(j)[3];
                boolean[][] xyTracker2 = traceMap.get(j);
                boolean shouldAdd = true;
                for (int k = 0; k < xyTracker1.length; k++) {
                    if (xyTracker1[k][0] != xyTracker2[k][0]) {
                        shouldAdd = false;
                        break;
                    }
                }
                if (shouldAdd) {
                    String[] newSwap = {swap1, swap2};
                    possSwaps.add(newSwap);
                }
            }
        }
        return possSwaps;
    }

    public static int getTheNumber(String str) {
        return 10*(str.charAt(1)-'0') + (str.charAt(2)-'0');
    }
}