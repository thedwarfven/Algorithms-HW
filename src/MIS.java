/*
@author Benjamin Le
Write a program that prompts the user to enter a string and displays the maximum increasingly ordered subsequence of characters.
 Analyze the time complexity of your program.
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class MIS {
    public static String longestIncreaseSubString(String s) {
        if (s.isEmpty()) {
            return "";
        }
        ArrayList<Character> mis = new ArrayList<>();
        // tailsIndex[k] stores the original string index
        // of the character currently representing mis[k]
        int[] tailsIndex = new int[s.length()];
        // previous[i] stores the original index of the character
        // that comes before s.charAt(i) in its increasing subsequence
        int[] previous = new int[s.length()];

        for (int i = 0; i < s.length(); i++) {//O(n) = n
            char c = s.charAt(i);
            int position;
            // c extends the longest sequence
            if (mis.isEmpty() || mis.getLast() < c) {
                mis.add(c);
                position = mis.size() - 1;
            } else {
                // find where c belongs in mis, O(n) = log n
                position = Collections.binarySearch(mis, c);
                if (position < 0) {
                    position = -position - 1;
                }
                mis.set(position, c);
            }
            // if c is at position 0, it starts a sequence.
            // otherwise, connect it to the character
            if (position == 0) {
                previous[i] = -1;
            } else {
                previous[i] = tailsIndex[position - 1];
            }
            // same job as mis, only
            tailsIndex[position] = i;
        }

        // Start from the original index that ends
        // the longest increasing subsequence.
        int current = tailsIndex[mis.size() - 1];

        StringBuilder result = new StringBuilder();
        while (current != -1) {
            result.append(s.charAt(current));
            current = previous[current];
        }

        return result.reverse().toString();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter massive string: ");
        String str = input.nextLine();
        System.out.print("The longest increasingly ordered substring contains the given string: ");
        System.out.println(longestIncreaseSubString(str));
//        System.out.println(longestIncreaseSubString("abcdefghijklmnopqrstuvwxyz"));
//        System.out.println(longestIncreaseSubString("zyxwvutsrqponmlkjihgfedcba"));
//        System.out.println(longestIncreaseSubString("jihgfedcbaabcdefghijklmnopqrstuvwxyz"));
//        System.out.println(longestIncreaseSubString("zabcdefghijklmnopqrstuvwxy"));
//        System.out.println(longestIncreaseSubString("abababababababababab"));
//        System.out.println(longestIncreaseSubString("mississippi"));
//        System.out.println(longestIncreaseSubString("qazwsxedcrfvtgbyhnujmikolp"));
//        System.out.println(longestIncreaseSubString("thequickbrownfoxjumpsoverthelazydog"));
//        System.out.println(longestIncreaseSubString("programmingalgorithms"));
//        System.out.println(longestIncreaseSubString("welcome"));

    }
}
