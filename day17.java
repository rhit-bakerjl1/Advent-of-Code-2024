import java.io.File;
import java.util.*;

public class day17 {
    public static void main(String[] args) {
        File day17_input = new File("day17.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day17_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        int regA = -1;
        int regB = -1;
        int regC = -1;
        boolean[] regsFilled = {false, false, false};
        ArrayList<Integer> commands = new ArrayList<Integer>();
        String nextLine = "";
        while (scan.hasNextLine()) {
            nextLine = scan.nextLine();
            int[] myNums = findNextInt(nextLine, 0);
            if (!regsFilled[0]) {
                regA = myNums[1];
                regsFilled[0] = true;
            } else if (!regsFilled[1]) {
                regB = myNums[1];
                regsFilled[1] = true;
            } else if (!regsFilled[2]) {
                regC = myNums[1];
                regsFilled[2] = true;
            } else if (nextLine.length() > 0) {
                // Start doing the thing.
                int index = 0;
                while (index < nextLine.length() && index >= 0) {
                    int[] nums1 = findNextInt(nextLine, index);
                    index = nums1[0];
                    commands.add(nums1[1]);
                }
            }
        }
        // nextLine was the last line!
        String goalCommand = nextLine.substring(9);
        System.out.println(goalCommand);
        int newRegA = getFirstRegA(commands, regB, regC, goalCommand);

        String output = getOutput(commands, regA, regB, regC);
        System.out.println("Output: " + output);
        System.out.println("First regA that wins: " + newRegA);
    } 
    
    public static String getOutput(ArrayList<Integer> commands, long regA, long regB, long regC) {
        long[] regsABC = {regA, regB, regC}; 
        int myInd = 0;
        String output = "";
        while (myInd < commands.size()) {
            int[] nextCommand = {commands.get(myInd), commands.get(myInd+1)};
            int[] indOut = handleCommand(nextCommand, regsABC, myInd);
            if (indOut[1] != -1) {
                output += indOut[1] + ",";
            }
            myInd = indOut[0];
        }
        output = output.substring(0,output.length()-1);
        return output;
    }

    public static String getOutputStop(ArrayList<Integer> commands, long regA, long regB, long regC, String goalString) {
        long[] regsABC = {regA, regB, regC}; 
        int myInd = 0;
        String output = "";
        while (myInd < commands.size()) {
            int[] nextCommand = {commands.get(myInd), commands.get(myInd+1)};
            int[] indOut = handleCommand(nextCommand, regsABC, myInd);
            if (indOut[1] != -1) {
                output += indOut[1];
                if (output.length() > goalString.length() || !output.equals(goalString.substring(0,output.length()))) {
                    return "";
                }
                output += ",";
            }
            myInd = indOut[0];
        }
        output = output.substring(0,output.length()-1);
        return output;
    }

    public static int getFirstRegA(ArrayList<Integer> commands, int regB, int regC, String goalString) {
        for (int i = 560000000; i < Integer.MAX_VALUE; i++) {
        // for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String myOutput = getOutputStop(commands, i, regB, regC, goalString);
            if (goalString.equals(myOutput)) {
                return i;
            }
            if (i % 20000000 == 0) {
                System.out.println("Not zero through " + i);
            }
        }
        return -1;
    }

    // returns the next index to check and the output value.
    public static int[] handleCommand(int[] command, long[] regsABC, int ind) {
        int opCode = command[0];
        int operand = command[1];
        long comboOp = operand;
        int outInd = -1;
        if (comboOp > 3 && comboOp != 7) {
            comboOp = regsABC[(int)comboOp-4];
        }
        int nextInd = ind + 2;
        switch (opCode) {
            case 0:
                // adv instruction
                regsABC[0] = regsABC[0]/(int)(Math.pow(2,comboOp));
                break;
            case 1:
                // bxl instruction
                regsABC[1] = bitXOR(regsABC[1], operand);
                break;
            case 2:
                // bst instruction
                regsABC[1] = comboOp % 8;
                break;
            case 3:
                // jnz instruction
                if (regsABC[0] != 0) {
                    nextInd = operand;
                }
                break;
            case 4:
                // bxc instruction
                regsABC[1] = bitXOR(regsABC[1], regsABC[2]);
                break;
            case 5:
                // out instruction
                outInd = (int)(comboOp % 8);
                break;
            case 6:
                // bdv instruction
                regsABC[1] = regsABC[0]/(int)(Math.pow(2,comboOp));
                break;
            case 7:
                // cdv instruction
                regsABC[2] = regsABC[0]/(int)(Math.pow(2,comboOp));
                break;
        }
        int[] hey = {nextInd, outInd};
        return hey;
    }

    // Returns index after the number and the actual number
    public static int[] findNextInt(String str, int startInd) {
        boolean intStarted = false;
        int returnNum = 0;
        for (int i = startInd; i < str.length(); i++) {
            char myChar = str.charAt(i);
            if (Character.isDigit(myChar)) {
                intStarted = true;
                returnNum *= 10;
                returnNum += myChar - '0';
            } else {
                if (intStarted) {
                    int[] returnArr = {i, returnNum};
                    return returnArr;
                }
            }
        }
        if (intStarted) {
            int[] returnArr = {str.length(), returnNum};
            return returnArr;
        }
        int[] returnArr = {-1, returnNum};
        return returnArr;
    }

    public static int bitXOR(long num1, long num2) {
        int[] bin1 = int2Binary(num1);
        int[] bin2 = int2Binary(num2);
        // Adjust lengths
        if (bin1.length > bin2.length) {
            // add to bin2
            int[] newBin2 = new int[bin1.length];
            for (int i = 0; i < newBin2.length; i++) {
                newBin2[i] = 0;
            }
            for (int i = 0; i < bin2.length; i++) {
                newBin2[newBin2.length-1-i] = bin2[bin2.length-1-i];
            }
            bin2 = newBin2;
        }
        if (bin2.length > bin1.length) {
            // add to bin1
            int[] newBin1 = new int[bin2.length];
            for (int i = 0; i < newBin1.length; i++) {
                newBin1[i] = 0;
            }
            for (int i = 0; i < bin1.length; i++) {
                newBin1[newBin1.length-1-i] = bin1[bin1.length-1-i];
            }
            bin1 = newBin1;
        }

        int[] returnBin = new int[bin1.length];
        for (int i = 0; i < returnBin.length; i++) {
            returnBin[i] = (bin1[i] + bin2[i]) % 2;
        }
        return binary2Int(returnBin);
    }

    public static int binary2Int(int[] binaryNum) {
        int sum = 0;
        for (int i = 0; i < binaryNum.length; i++) {
            int index = binaryNum.length - 1 - i;
            sum += (int)(Math.pow(2,i)*binaryNum[index]);
        }
        return sum;
    }

    public static int[] int2Binary(long intNum) {
        int arrLen = 3;
        long twoPow = (long)Math.pow(2, arrLen);
        while (twoPow < intNum) {
            arrLen++;
            twoPow = (long)Math.pow(2, arrLen);
        }

        int[] myBinary = new int[arrLen];
        for (int i = 0; i < myBinary.length; i++) {
            long pow = (long)Math.pow(2, myBinary.length-1-i);
            if (pow <= intNum) {
                intNum -= pow;
                myBinary[i] = 1;
            } else {
                myBinary[i] = 0;
            }
        }
        return myBinary;
    }
}

