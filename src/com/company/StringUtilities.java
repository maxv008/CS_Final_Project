package com.company;

/**
 * Created by daniel on 6/6/16.
 */
public class StringUtilities {

    /**
     * Compares the two input strings and returns a double from 0 to 1.0. Goes with the Ldistance I found online.
     *
     * @param s1 String 1
     * @param s2 String 2
     * @return double from 0.0 to 1.0 where 1.0 means the strings are identical.
     */
    public static double stringSimilarity(String s1, String s2)
    {
        String longer = s1;
        String shorter = s2;
        if (longer.length() < shorter.length())
        {
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) //Since they are both empty
            return 1.0;

        return ((double) longerLength - getLevenshteinDistance(longer, shorter)) / (double) longerLength;
    }

    /**
     * Standard implementation of edit distance between two strings (i.e. not originally written by me and found online).
     * Time complexity is O(n*k) where n and k are the length of the strings.
     *
     * @param s1 String 1
     * @param s2 String 2
     * @return The Levenshtein edit distance as a double (although it is just an integer really)
     */
    public static double getLevenshteinDistance(String s1, String s2) //Because I don't wanna bother setting up dependencies
    {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++)
        {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++)
            {
                if (i == 0)
                    costs[j] = j;
                else if (j > 0)
                {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1))
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return (double) costs[s2.length()];
    }

    /**
     * Sourced from https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Longest_common_substring#Java
     * @param string1 The first string
     * @param string2 The second string
     * @return The longest common substring of the two provided strings
     */
    private static String longestCommonSubstring(String string1, String string2) {
        int Start = 0;
        int Max = 0;
        for (int i = 0; i < string1.length(); i++) {
            for (int j = 0; j < string2.length(); j++) {
                int x = 0;
                while (string1.charAt(i + x) == string2.charAt(j + x)) {
                    x++;
                    if (((i + x) >= string1.length()) || ((j + x) >= string2.length())) break;
                }
                if (x > Max) {
                    Max = x;
                    Start = i;
                }
            }
        }
        return string1.substring(Start, (Start + Max));
    }
}
