import java.io.File;
import java.util.*;

public class day2 {
    public static void main(String[] args) {
        File day2_input = new File("day2.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day2_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        int safeLvls = 0;
        int safeWithoutLevel = 0;
        int flag;
        while (scan.hasNextLine()) {
            String levelStr = scan.nextLine();
            flag = 0;
            if (isSafeLevel(levelStr)) {
                safeLvls++;
                flag = 1;
            }
            for (int i = 0; i < day2.countLvl(levelStr); i++) {
                if (flag == 0) {
                    String newTest = removeNum(levelStr, i);
                    if (isSafeLevel(newTest)) {
                        flag = 1;
                    }
                }
            }
            if (flag == 1) {
                safeWithoutLevel++;
            }
        }

        System.out.println("Number of safe levels: " + safeLvls);
        System.out.println("Number of 2 safe levels: " + safeWithoutLevel);
    }

    public static boolean isSafeLevel(String levelStr) {
        Scanner scanLvl = new Scanner(levelStr);
        int num1 = scanLvl.nextInt();
        int num2 = scanLvl.nextInt();
        int incOrDec = 0;
        if (num2 < num1) {
            incOrDec = 1;
        }
        else if (num2 == num1) {
            incOrDec = 2;
        }
        int diff = Math.abs(num1 - num2);
        if (diff > 3) {
            incOrDec = 2;
        }
        while (scanLvl.hasNext()) {
            num1 = num2;
            num2 = scanLvl.nextInt();
            if (incOrDec == 0) {
                // Increasing
                diff = num2 - num1;
                if (diff <= 0 || diff > 3) {
                    incOrDec = 2;
                }
            }
            else if (incOrDec == 1) {
                // Decreasing
                diff = num1 - num2;
                if (diff <= 0 || diff > 3) {
                    incOrDec = 2;
                }
            }
        }
        return incOrDec != 2;
    }

    public static String removeNum(String levelStr, int index2Remove) {
        Scanner scanLvl = new Scanner(levelStr);
        String newStr = "";
        int index = 0;
        while (scanLvl.hasNext()) {
            if (index != index2Remove) {
                newStr += " ";
                newStr += scanLvl.nextInt();
            }
            else {
                scanLvl.nextInt();
            }
            index++;
        }
        return newStr.substring(1);
    }

    public static int countLvl(String levelStr) {
        Scanner scanLvl = new Scanner(levelStr);
        int num = 0;
        int placeholder;
        while (scanLvl.hasNext()) {
            num++;
            placeholder = scanLvl.nextInt();
        }
        return num;
    }
}