import java.io.File;
import java.util.*;


public class day23 {
    public static void main(String[] args) {
        File day23_input = new File("day23.txt");
        // Scan the data
        Scanner scan;
        try {
            scan = new Scanner(day23_input);
        } catch (Exception e) {
            System.out.println("Try again");
            return;
        }

        ArrayList<String[]> connections = new ArrayList<String[]>();
        ArrayList<String> computers = new ArrayList<String>();
        while (scan.hasNextLine()) {
            String newLine = scan.nextLine();
            String[] players = {newLine.substring(0,2), newLine.substring(3,5)};
            connections.add(players);
            if (!computers.contains(players[0])) {
                computers.add(players[0]);
            }
            if (!computers.contains(players[1])) {
                computers.add(players[1]);
            }
        }

        System.out.println("There are " + computers.size() + " players.");
        ArrayList<ArrayList<String>> storedThrees = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < computers.size(); i++) {
            String word1 = computers.get(i);
            for (int j = i+1; j < computers.size(); j++) {
                String word2 = computers.get(j);
                if (isConnected(word1, word2, connections)) {
                    for (int k = j+1; k < computers.size(); k++) {
                        String word3 = computers.get(k);
                        // if (word1.charAt(0) == 't' || word2.charAt(0) == 't' || word3.charAt(0) == 't') {
                            if (isConnected(word1, word3, connections) && isConnected(word2, word3, connections)) {
                                ArrayList<String> myThree = new ArrayList<String>();
                                myThree.add(word1);
                                myThree.add(word2);
                                myThree.add(word3);
                                // String[] myThree = {word1, word2, word3};
                                storedThrees.add(myThree);
                                // System.out.println(word1 + ", " + word2 + ", " + word3);
                            }
                        // }
                    }
                }
            }
        }

        System.out.println("Number of threes connected: " + storedThrees.size());

        // ArrayList<ArrayList<String>> storedGroups;

        // See if we can add to the squad!
        int biggestGroup = 0;
        int bigGroupInd = 0;
        for (int i = 0; i < storedThrees.size(); i++) {
            for (int j = i+1; j < storedThrees.size(); j++) {
                ArrayList<String> newToAdd = new ArrayList<String>();
                for (int k = 0; k < storedThrees.get(i).size(); k++) {
                    if (isInvolved(storedThrees.get(i).get(k), storedThrees.get(j))) {
                        for (int l = 0; l < storedThrees.get(j).size(); l++) {
                            String strl = storedThrees.get(j).get(l);
                            // System.out.println("Trying to add " + strl + " to " + storedThrees.get(i).get(0) + storedThrees.get(i).get(1) + storedThrees.get(i).get(2));
                            if (!isInvolved(strl, newToAdd) && !isInvolved(strl, storedThrees.get(i))) {
                                // System.out.println("Successful!");
                                newToAdd.add(strl);
                            } else {
                                // System.out.println("Not successful");
                            }
                        }
                        break;
                    }
                }
                ArrayList<String> willAdd = new ArrayList<String>();
                for (int k = 0; k < newToAdd.size(); k++) {
                    if (canCombine(storedThrees.get(i), newToAdd.get(k), storedThrees)) {
                        willAdd.add(newToAdd.get(k));
                    }
                }
                for (int k = 0; k < willAdd.size(); k++) {
                    storedThrees.get(i).add(willAdd.get(k));
                }
            }

            if (storedThrees.get(i).size() > biggestGroup) {
                biggestGroup = storedThrees.get(i).size();
                bigGroupInd = i;
                System.out.println("New biggest group of size: " + biggestGroup + ", index: " + bigGroupInd);
            }
        }
        System.out.println("Found the biggest group: " + biggestGroup + " at " + bigGroupInd);

        ArrayList<String> bigGroup = storedThrees.get(bigGroupInd);
        while (!storedThrees.isEmpty()) {
            storedThrees.remove(0);
        }
        System.out.println(alphabetAndPrint(bigGroup));

    }   

    public static boolean isConnected(String player1, String player2, ArrayList<String[]> connections) {
        for (int i = 0; i < connections.size(); i++) {
            if ((player1.equals(connections.get(i)[0]) && player2.equals(connections.get(i)[1])) || (player1.equals(connections.get(i)[1]) && player2.equals(connections.get(i)[0]))) {
                return true;
            }
        }
        return false;
    }

    public static boolean canCombine(ArrayList<String> players, String newPlayer, ArrayList<ArrayList<String>> connections) {
        boolean willPlay;
        for (int i = 0; i < players.size(); i++) {
            willPlay = false;
            String newStr = players.get(i);
            for (int j = 0; j < connections.size(); j++) {
                if (isInvolved(newPlayer, connections.get(j)) && isInvolved(newStr, connections.get(j))) {
                    willPlay = true;
                    break;
                }
            }
            if (!willPlay) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInvolved(String newPlayer, ArrayList<String> connections) {
        for (int i = 0; i < connections.size(); i++) {
            if (newPlayer.equals(connections.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static String alphabetAndPrint(ArrayList<String> toSort) {
        ArrayList<String> sortedList = new ArrayList<String>();
        ArrayList<String> notSortedYet = new ArrayList<String>();

        for (int i = 0; i < toSort.size(); i++) {
            notSortedYet.add(toSort.get(i));
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

