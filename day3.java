import java.io.File;
import java.util.*;

public class day3 {
    public static void main(String[] args) {
        File day3_input = new File("day3.txt");
        // Scan the data
        Scanner scan;
        System.out.println(isNumeric("5730jj32"));
        try {
            scan = new Scanner(day3_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        int count = 0;
        int countEnabled = 0;
        int isEnabled = 1;
        int[] temp;
        while (scan.hasNextLine()) {
            String newLine = scan.nextLine();
            count += countMuls(newLine);
            temp = countEnabledMuls(newLine, isEnabled);
            countEnabled += temp[0];
            isEnabled = temp[1];
        }

        System.out.println("Sum of the multiplies: " + count);
        System.out.println("Sum of the enabled multiplies: " + countEnabled);
        
    }
    
    public static int countMuls(String line) {  // Part 1
        int num1 = 0;
        int num2 = 0;
        int component = 0;
        // Component Code:
        // 0: mul(
        // 1: num1
        // 2: ,
        // 3: num2
        // 4: )

        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            switch (component) {
                case 1:
                    // getting first number
                    num1 = isNumeric(line.substring(i));
                    if (num1 == -1) {
                        // isNumeric failed. Reset.
                        component = 0;
                        i--;
                    } else if (num1 >= 10) {
                        i++;
                        if (num1 >= 100) {
                            i++;
                        }
                        component++;
                    }
                    else {
                        component++;
                    }
                    break;
                case 2:
                    // getting ,
                    if (line.charAt(i) == ',') {
                        component++;
                    } else {
                        component = 0;
                        i--;
                    }
                    break;
                case 3:
                    // getting second number
                    num2 = isNumeric(line.substring(i));
                    if (num2 == -1) {
                        // isNumeric failed. Reset.
                        component = 0;
                        i--;
                    } else if (num2 >= 10) {
                        i++;
                        if (num2 >= 100) {
                            i++;
                        }
                        component++;
                    } else {
                        component++;
                    }
                    break;
                case 4: 
                    // getting )
                    if (line.charAt(i) == ')') {
                        // We can multiply!
                        count += num1*num2;
                        num1 = 0;
                        num2 = 0;
                        component = 0;
                    } else {
                        component = 0;
                        i--;
                    }
                    break;
                default:
                    // getting mul(
                    if (i+3 < line.length() && line.substring(i, i+4).equals("mul(")) {
                        component++;
                        i+=3;
                    }
                    break;
            }
        }

        return count;
    }

    public static int isNumeric(String str) {
        int myNum = -1;
        int temp;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                temp = (int)str.charAt(i) - '0';
                if (myNum == -1) {
                    myNum = temp;
                } else {
                    myNum*= 10;
                    myNum+= temp;
                }
            } else {
                while (myNum > 1000) {
                    myNum /= 10;
                }
                return myNum;
            }
        }
        while (myNum > 1000) {
            myNum %= 10;
        }
        return myNum;
    }

    public static int[] countEnabledMuls(String line, int isEnabled) {  // Part 1
        int num1 = 0;
        int num2 = 0;
        int component = 0;
        // Component Code:
        // 0: mul(
        // 1: num1
        // 2: ,
        // 3: num2
        // 4: )
        int count = 0;
        int disableCount = 0;
        for (int i = 0; i < line.length(); i++) {
            if (i+6 < line.length() && line.substring(i,i+7).equals("don\'t()") && isEnabled == 1) {
                isEnabled = 0;
                i+=6;
                component = 0;
            } 
            if (i+3 < line.length() && line.substring(i,i+4).equals("do()") && isEnabled == 0) {
                isEnabled = 1;
                i+=3;
                component = 0;
            }
            switch (component) {
                case 1:
                    // getting first number
                    num1 = isNumeric(line.substring(i));
                    if (num1 == -1) {
                        // isNumeric failed. Reset.
                        component = 0;
                        i--;
                    } else if (num1 >= 10) {
                        i++;
                        if (num1 >= 100) {
                            i++;
                        }
                        component++;
                    }
                    else {
                        component++;
                    }
                    break;
                case 2:
                    // getting ,
                    if (line.charAt(i) == ',') {
                        component++;
                    } else {
                        component = 0;
                        i--;
                    }
                    break;
                case 3:
                    // getting second number
                    num2 = isNumeric(line.substring(i));
                    if (num2 == -1) {
                        // isNumeric failed. Reset.
                        component = 0;
                        i--;
                    } else if (num2 >= 10) {
                        i++;
                        if (num2 >= 100) {
                            i++;
                        }
                        component++;
                    } else {
                        component++;
                    }
                    break;
                case 4: 
                    // getting )
                    if (line.charAt(i) == ')') {
                        // We can multiply!
                        if (isEnabled == 1) {
                            count += num1*num2;
                        } else {
                            disableCount += num1*num2;
                        }
                        num1 = 0;
                        num2 = 0;
                        component = 0;
                    } else {
                        component = 0;
                        i--;
                    }
                    break;
                default:
                    // getting mul(
                    if (i+3 < line.length() && line.substring(i, i+4).equals("mul(")) {
                        component++;
                        i+=3;
                    }
                    break;
            }

        }
        System.out.println("DisableCount: " + disableCount);
        int[] temp = {count, isEnabled};
        return temp;
    }
}