import java.io.File;
import java.util.*;

public class day22 {
    public static void main(String[] args) {
        File day22_input = new File("day22.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day22_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<Integer> monkeyNumbers = new ArrayList<Integer>();
        while (scan.hasNextLine()) {
            monkeyNumbers.add(scan.nextInt());  // HINT: Potentially nextLong
        }

        int numsPerDay = 2000;
        int[][] priceMap = new int[monkeyNumbers.size()][numsPerDay];
        int[][] stonksMap = new int[monkeyNumbers.size()][numsPerDay];
        long secretSum = 0;        
        for (int i = 0; i < monkeyNumbers.size(); i++) {
            long secretNumber = monkeyNumbers.get(i);
            int lastPrice;
            int price = 0;
            for (int j = 0; j < numsPerDay; j++) {
                // Update
                lastPrice = price;

                // Get new info
                secretNumber = nextSecretNumber(secretNumber);
                price = (int)(secretNumber % 10);
                int rise = price - lastPrice;
                priceMap[i][j] = price;
                stonksMap[i][j] = rise;
            }
            secretSum += secretNumber;
        }

        System.out.println("Final Monkey Number: " + secretSum);

        int maxBanas = getMaxBanas(priceMap, stonksMap);
        System.out.println("Max bananas: " + maxBanas);
    }  

    public static int getMaxBanas(int[][] priceMap, int[][] stonksMap) {
        ArrayList<int[]> possCombos = new ArrayList<int[]>();
        for (int i = -9; i < 10; i++) {
            for (int j = -9; j < 10; j++) {
                if (i + j >= -9 && i + j <= 9) {
                    for (int k = -9; k < 10; k++) {
                        if (i + j + k >= -9 && i + j + k <= 9) {
                            for (int l = 0; l < 10; l++) {
                                if (i + j + k + l >= -9 && i + j + k + l <= 9) {
                                    int[] newCombo = {i,j,k,l};
                                    possCombos.add(newCombo);
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Got all the combos! " + possCombos.size());
        ArrayList<Integer> banasEach = new ArrayList<Integer>();
        int maxBanas = 0;
        for (int i = 0; i < possCombos.size(); i++) {
            banasEach.add(0);
            int[] myCombo = possCombos.get(i);
            for (int j = 0; j < stonksMap.length; j++) {
                for (int k = 4; k < stonksMap[0].length; k++) {
                    boolean add = true;
                    for (int l = 0; l < 4; l++) {
                        if (myCombo[l] != stonksMap[j][k-3+l]) {
                            add = false;
                            break;
                        }
                    }
                    if (add) {
                        banasEach.set(i,banasEach.get(i) + priceMap[j][k]);
                        break;
                    }
                }
            }
            if (banasEach.get(i) > maxBanas) {
                maxBanas = banasEach.get(i);
                System.out.println("New record! " + myCombo[0] + myCombo[1] + myCombo[2] + myCombo[3] + " had " + maxBanas);
            }
        }

        return maxBanas;
    }

    public static long next2000(long secretNumber) {
        for (int i = 0; i < 2000; i++) {
            secretNumber = nextSecretNumber(secretNumber);
        }
        return secretNumber;
    }
    
    public static long nextSecretNumber(long secretNumber) {
        secretNumber = mixPrune(secretNumber*64, secretNumber);
        secretNumber = mixPrune(secretNumber/32, secretNumber);
        secretNumber = mixPrune(secretNumber*2048, secretNumber);
        return secretNumber;
    }

    public static long mixPrune(long secret1, long secret2) {
        long secretNumber = bitXOR(secret1, secret2);
        secretNumber %= 16777216;
        return secretNumber;
    }

    public static long bitXOR(long num1, long num2) {
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
    
    public static long binary2Int(int[] binaryNum) {
        long sum = 0;
        for (int i = 0; i < binaryNum.length; i++) {
            int index = binaryNum.length - 1 - i;
            sum += longPow(2,i)*binaryNum[index];
        }
        return sum;
    }
    
    public static int[] int2Binary(long intNum) {
        int arrLen = 3;
        long twoPow = (long)Math.pow(2, arrLen);
        while (twoPow <= intNum) {
            arrLen++;
            twoPow = longPow(2, arrLen);
        }
    
        int[] myBinary = new int[arrLen];
        for (int i = 0; i < myBinary.length; i++) {
            long pow = longPow(2, myBinary.length-1-i);
            if (pow <= intNum) {
                intNum -= pow;
                myBinary[i] = 1;
            } else {
                myBinary[i] = 0;
            }
        }
        return myBinary;
    }

    public static long longPow(long num1, long num2) {
        long returnNum = 1;
        for (long i = 0; i < num2; i++) {
            returnNum *= num1;
        }
        return returnNum;
    }
}