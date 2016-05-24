package com.company;

import java.util.*;
import java.io.*;

/**
 * Created by Max Vigdorchik on 5/3/2016.
 */
public class ProjectPair
{
    private Project p1, p2;
    private double sketchyScore; //When we are done this should cap out at 100 (i.e. all of our MAXIMUM constants sum to 100).
    //TODO: Find a way to seed all of the following constant values (more to come).
    private final double IMPORT_MAXIMUM = 5; // Maximum percentage contribution of import statements to sketchyscore.
    private final double IMPORT_WEIGHT = 2; //Smaller number means each matching import counts more, but never more than maximum.
    private final double COMMENT_MAXIMUM = 20;
    private final double COMMENT_WEIGHT = 2;
    //Equation to use: 2/(1 + Math.exp(-Math.pow(x,2))) -1 because it grows most in middle instead of start
    //private List<String> comments; TODO: Implement comments for what is contributing to the sketchy score.

    public ProjectPair(Project p1, Project p2)
    {
        this.p1 = p1;
        this.p2 = p2;
        sketchyScore = 0;
    }

    public double getSketchyScore()
    {
        return sketchyScore;
    }

    /**
     * Compares import statements in the two projects and adjusts the sketchy score.
     *
     * @return How much the sketchy score increased.
     */
    public double compareImports() //TODO: Consider order of import statements.
    {
        double sketchyInitial = sketchyScore; //just gets the initials score to return the correct value
        double matchAmount = 0; //number of matches found
        List<String> p1Import = p1.parseImport();
        List<String> p2Import = p2.parseImport();

        for (int i = 0; i < p1Import.size(); i++)
        {
            for (int j = 0; j < p2Import.size(); j++)
            {
                if (p1Import.get(i).equalsIgnoreCase(p2Import.get(j))) //TODO: Might need something more robust than equals.
                    matchAmount++;
            }
        }
        sketchyScore += IMPORT_MAXIMUM * (2.0 / (1 + Math.exp(-IMPORT_WEIGHT*Math.pow(matchAmount, 2))) - 1.0);

        return sketchyScore - sketchyInitial;
    }

    /**
     * Compares the comments in the projects.
     *
     * @return The amount that the sketchy score increased.
     */
    public double compareComments()
    {
        double sketchyInitial = sketchyScore;
        double matchScore = 0;
        List<String> p1Comments = p1.parseComments();
        List<String> p2Comments = p2.parseComments();

        for (String s1 : p1Comments)
        {
            double greatest = 0;
            for (String s2 : p2Comments) //For each string it only matches it to the most similar comment.
            {
                double similarity = stringSimilarity(s1, s2);
                greatest = Math.max(similarity, greatest);
            }
            matchScore += greatest;
        }
        //TODO: See if there is a more robust way to use stringSimilarity.

        sketchyScore += COMMENT_MAXIMUM * (2.0 / (1 + Math.exp(-COMMENT_WEIGHT*Math.pow(matchScore, 2))) - 1.0);
        return sketchyScore - sketchyInitial;
    }

    /**
     * Compares the two input strings and returns a double from 0 to 1.0. Goes with the Ldistance I found online.
     *
     * @param s1 String 1
     * @param s2 String 2
     * @return double from 0.0 to 1.0 where 1.0 means the strings are identical.
     */
    private static double stringSimilarity(String s1, String s2)
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
    private static double getLevenshteinDistance(String s1, String s2) //Because I don't wanna bother setting up dependencies
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
}
