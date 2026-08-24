/*
@author Benjamin Le
Write a program that prompts the user to enter a string and displays the maximum consecutive increasingly ordered substring.
 Analyze the time complexity of your program.
 */

import java.util.Scanner;

public class MCIS {
    public static String longestSubString(String s){
        String longest = "";
        int currentStart = 0;
        int bestStart =0;
        int bestLength = 0;
        for(int i = 0; i < s.length()-1; i++){
            if(s.charAt(i) >= s.charAt(i+1)){
                int currentLength = i - currentStart + 1;
                if (currentLength > bestLength) {
                    bestLength = currentLength;
                    bestStart = currentStart;
                }
                currentStart = i+1;
            }
        }
        // Check the final sequence
        int currentLength = s.length() - currentStart;

        if (currentLength > bestLength) {
            bestLength = currentLength;
            bestStart = currentStart;
        }

        return s.substring(bestStart, bestStart + bestLength);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter massive string: ");
        String str = input.nextLine();
        System.out.print("The longest consecutive increasingly ordered substring contains the given string: ");
        System.out.println(longestSubString(str));
    }
}
