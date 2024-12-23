import java.io.File;
import java.util.*;

public class day19 {
    public static HashMap<String, Long> storedCombos;

    public static void main(String[] args) {
        File day19_input = new File("day19.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day19_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<String> littleTowels = findTowels(scan.nextLine());
        ArrayList<String> bigTowels = new ArrayList<String>();
        scan.nextLine();
        while (scan.hasNextLine()) {
            bigTowels.add(scan.nextLine());
        }

        long count = 0;
        ArrayList<ArrayList<String>> sortedTowels;
        for (int i = 0; i < bigTowels.size(); i++) {
            storedCombos = new HashMap<String, Long>();
            sortedTowels = findMySortedTowels(bigTowels.get(i), littleTowels);
            long added = isPossible(bigTowels.get(i), sortedTowels); 
            count += added;
            System.out.println(bigTowels.get(i) + " has " + added + " combos");
            // if (isPossible(bigTowels.get(i), sortedTowels)) {
            //     count++;
            //     System.out.println(bigTowels.get(i) + " works!");
            // } else {
            //     System.out.println(bigTowels.get(i) + " doesn't work");
            // }
        }     
        
        System.out.println(count + "/" + bigTowels.size() + " work");
    }   

    public static ArrayList<String> findTowels(String sepTowels) {
        ArrayList<String> towels = new ArrayList<String>();
        int myTowel = 0;
        towels.add("");
        for (int i = 0; i < sepTowels.length(); i++) {
            if (sepTowels.charAt(i) == ',') {
                myTowel++;
                towels.add("");
                i++;
            } else {
                towels.set(myTowel, towels.get(myTowel) + sepTowels.charAt(i));
            }
        }

        return towels;
    }

    public static long isPossible(String bigTowel, ArrayList<ArrayList<String>> littleTowels) {
        int storeEvery = 2;
        if (bigTowel.length() == 0) {
            return 1;
        }
        if (bigTowel.length() % storeEvery == 0) {
            try {
                long cheating = storedCombos.get(bigTowel);
                return cheating;
            } catch (Exception e) {
            }
        }
        
        ArrayList<String> myLittleTowels = new ArrayList<String>();
        int box = findBox(bigTowel);
        myLittleTowels = littleTowels.get(box);

        long numCombs = 0;
        for (int i = 0; i < myLittleTowels.size(); i++) {
            String littleTowel = myLittleTowels.get(i);
            if (littleTowel.length() <= bigTowel.length() && littleTowel.equals(bigTowel.substring(0,littleTowel.length()))) {
                // starts with it!
                // boolean continues = isPossible(bigTowel.substring(littleTowel.length()), littleTowels);
                long added = isPossible(bigTowel.substring(littleTowel.length()), littleTowels);
                if (added > 0) {
                    numCombs += added;
                }
                // if (bigTowel.length() < 10) {
                    // System.out.println("So close! " + bigTowel.length());
                // }
                // if (continues) {
                    // System.out.println("Poggies! It's return time!");
                    // return true;
                // }
            }
        }
        // return false;
        if (bigTowel.length() % storeEvery == 0) {
            storedCombos.put(bigTowel, numCombs);
            
        }
        return numCombs;
    }

    // Order: w, u, b, r, g
    public static ArrayList<ArrayList<String>> sortTowels(ArrayList<String> littleTowels) {
        ArrayList<ArrayList<String>> sortedTowels = new ArrayList<ArrayList<String>>();
        char[] firstChars = {'w', 'u', 'b', 'r', 'g'};
        String[] firstSecondChars = new String[firstChars.length*firstChars.length];
        for (int i = 0; i < firstSecondChars.length; i++) {
            int firstChars1 = i / firstChars.length;
            int firstChars2 = i % firstChars.length;
            firstSecondChars[i] = "" + firstChars[firstChars1] + firstChars[firstChars2];
        }

        for (int i = 0; i < firstSecondChars.length; i++) {
            sortedTowels.add(new ArrayList<String>());
        }

        for (int i = 0; i < littleTowels.size(); i++) {
            int box = findBox(littleTowels.get(i));
            sortedTowels.get(box).add(littleTowels.get(i));
            if (littleTowels.get(i).length() == 1) {
                for (int j = 1; j < firstChars.length; j++) {
                    sortedTowels.get(box+j).add(littleTowels.get(i));
                }
            }
        }
        return sortedTowels;
    }

    public static ArrayList<ArrayList<String>> findMySortedTowels(String bigTowel, ArrayList<String> littleTowels) {
        ArrayList<String> myLittleTowels = new ArrayList<String>();
        for (int i = 0; i < littleTowels.size(); i++) {
            if (bigTowel.contains(littleTowels.get(i))) {
                myLittleTowels.add(littleTowels.get(i));
            }
        }
        // System.out.println("Narrowed " + littleTowels.size() + " to " + myLittleTowels.size());

        return sortTowels(myLittleTowels);
    }

    public static int findBox(String str) {
        int firstChar = 0;
        int secondChar = 0;
        char[] firstChars = {'w', 'u', 'b', 'r', 'g'};
        for (int i = 0; i < firstChars.length; i++) {
            if (str.charAt(0) == firstChars[i]) {
                firstChar = i;
                break;
            }
        }
        if (str.length() != 1) {
            for (int i = 0; i < firstChars.length; i++) {
                if (str.charAt(0) == firstChars[i]) {
                    secondChar = i;
                    break;
                }
            }
        }
        return firstChar*firstChars.length + secondChar;
    }

}

