import java.io.File;
import java.util.*;

public class day13 {
    public static void main(String[] args) {
        File day13_input = new File("day13.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day13_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        String codeString;
        int nextInt;
        int numOn;
        ArrayList<int[]> myDatas = new ArrayList<int[]>();
        while (scan.hasNextLine()) {
            codeString = scan.nextLine() + scan.nextLine() + scan.nextLine();
            try {
                scan.nextLine();
            } catch (Exception e) {
            }

            // Get datas ready
            myDatas.add(new int[6]);

            numOn = 0;
            nextInt = 0;
            for (int i = 0; i < codeString.length(); i++) {
                if (Character.isDigit(codeString.charAt(i))) {
                    nextInt *= 10;
                    nextInt += codeString.charAt(i) - '0';
                } else {
                    if (nextInt != 0 && numOn < 6) {
                        myDatas.get(myDatas.size()-1)[numOn] = nextInt;
                        numOn++;
                        nextInt = 0;
                    }
                }
            }
            myDatas.get(myDatas.size()-1)[numOn] = nextInt;
        }

        long totCoins = 0;
        for (int i = 0; i < myDatas.size(); i++) {
            totCoins += numCoins(myDatas.get(i));
        }

        System.out.println("Coins spent: " + totCoins);
    }   

    public static long numCoins(int[] clawData) {
        long longlong = (long)10000000000000.0;
        long xA = clawData[0];
        long yA = clawData[1];
        long xB = clawData[2];
        long yB = clawData[3];
        long xG = clawData[4] + longlong;
        long yG = clawData[5] + longlong;

        // long later
        long denom = xA*yB - yA*xB;
        long num_A = yB*xG - xB*yG;
        long num_B = -yA*xG + xA*yG;
        // double num_A = (yB*xG - xB*yG)*1.0/denom;
        // double num_B = (-yA*xG + xA*yG)*1.0/denom;

        // If they are integers, positive, less than 100
        // if (num_A == Math.round(num_A) && num_B == Math.round(num_B) && num_A >= 0 && num_B >= 0 && num_A <= 100 && num_B <= 100) {
        // if (num_A == Math.round(num_A) && num_B == Math.round(num_B) && num_A >= 0 && num_B >= 0) {
        if (num_A % denom == 0 && num_B % denom == 0 && num_A/denom >= 0 && num_B/denom >= 0) {
            return (long)(num_A*3/denom + num_B/denom);
        } else {
            return 0;
        }
    }
}

