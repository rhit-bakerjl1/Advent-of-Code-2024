import java.io.File;
import java.util.*;

public class day25 {
    public static void main(String[] args) {
        File day25_input = new File("day25.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day25_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<int[]> locks = new ArrayList<int[]>();
        ArrayList<int[]> keys = new ArrayList<int[]>();
        int linesPer = 6;
        while (scan.hasNextLine()) {
            String topLine = scan.nextLine();
            if (topLine.charAt(0) == '#') {
                // Lock
                locks.add(new int[topLine.length()]);
                for (int i = 0; i < locks.get(locks.size()-1).length; i++) {
                    locks.get(locks.size()-1)[i] = -1;
                }
                for (int i = 0; i < linesPer; i++) {
                    String nextLine = scan.nextLine();
                    for (int j = 0; j < nextLine.length(); j++) {
                        if (locks.get(locks.size()-1)[j] == -1 && nextLine.charAt(j) == '.') {
                            locks.get(locks.size()-1)[j] = i;
                        }
                    }
                }
            } else {
                // key
                keys.add(new int[topLine.length()]);
                for (int i = 0; i < keys.get(keys.size()-1).length; i++) {
                    keys.get(keys.size()-1)[i] = -1;
                }
                for (int i = 0; i < linesPer; i++) {
                    String nextLine = scan.nextLine();
                    for (int j = 0; j < nextLine.length(); j++) {
                        if (keys.get(keys.size()-1)[j] == -1 && nextLine.charAt(j) == '#') {
                            keys.get(keys.size()-1)[j] = linesPer-i-1;
                        }
                    }
                }
            }
            if (scan.hasNextLine()) {
                scan.nextLine();
            }
        }

        System.out.println("Check now!");

        int fitCount = 0;
        for (int i = 0; i < locks.size(); i++) {
            for (int j = 0; j < keys.size(); j++) {
                if (canFit(locks.get(i), keys.get(j))) {
                    fitCount++;
                }
            }
        }

        System.out.println("Unique lock/key pairs: " + fitCount);
    }   

    public static boolean canFit(int[] lock, int[] key) {
        int linesPer = 5;
        for (int i = 0; i < lock.length; i++) {
            if (lock[i] + key[i] > linesPer) {
                return false;
            }
        }
        return true;
    }
}

