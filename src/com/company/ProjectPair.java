package com.company;

import java.util.*;
import java.io.*;

/**
 * Created by Max Vigdorchik on 5/3/2016.
 */
public class ProjectPair
{
    private final Project p1, p2;
    //private double sketchyScore; //When we are done this should cap out at 100 (i.e. all of our MAXIMUM constants sum to 100).
    //TODO: Find a way to seed all of the following constant values (more to come).
    //private final double IMPORT_MAXIMUM = 5; //Might want to hardcode this as 0 since our test cases have no imports.
    //private final double IMPORT_WEIGHT = 1; //Smaller number means each matching import counts more, but never more than maximum.
    //private final double COMMENT_MAXIMUM = 20; //Maximum percentage contribution of comments to sketchyscore.
    //private final double COMMENT_WEIGHT = 1;
    private SketchyLearning cList;
    //Equation to use: 2/(1 + Math.exp(-Math.pow(x,2))) -1 because it grows most in middle instead of start
    //private List<String> comments; TODO: Implement comments for what is contributing to the sketchy score.

    ProjectPair(Project p1, Project p2, SketchyLearning cList)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.cList = cList;
        //sketchyScore = 0;
    }

    ProjectPair(Project p1, Project p2) //This constructor is juts here for the sketchy learning class to use.
    {
        this.p1 = p1;
        this.p2 = p2;
        //sketchyScore = 0;
    }

    public double getSketchyScore()
    {
        double result = 0;
        double importScore = compareImports();
        double commentScore = compareComments();
        //The Constants are set in the sketchyLearning function, currently set manually.
        result += statFunction(importScore, "iMax", "iWeight");
        result += statFunction(commentScore, "cMax" , "cWeight");

        return result;
    }

    /**
     * Encapsulates the function that converts match amounts into a percent sketchy score.
     * @param in The Match Amount or whatever other equivalent variable.
     * @param maxKey Map key to get out one of the constants, given by "[a-z]" + "Max"
     * @param weightKey Key for the weight constant, given by "[a-z]" + "Weight"
     * @return The contribution to the sketchy score from 0-maxAmount.
     */
    public double statFunction(double in, String maxKey, String weightKey)
    {
        return cList.getConstants().get(maxKey) * (2.0 / (1.0 + Math.exp(-cList.getConstants().get(weightKey)
            * Math.pow(in,2))) - 1.0);
    }

    /**
     * Compares import statements in the two projects and adjusts the sketchy score.
     *
     * @return How much the sketchy score increased.
     */
    public double compareImports() //TODO: Consider order of import statements as well, maybe worth more than them matching.
    {
        double matchAmount = 0; //number of matches found
        List<String> p1Import = p1.parseImport();
        List<String> p2Import = p2.parseImport();

        for (String s1 : p1Import)
        {
            for (String s2 : p2Import)
            {
                if (s1.equalsIgnoreCase(s2)) //TODO: Might need something more robust than equals.
                    matchAmount++;
            }
        }
        //sketchyScore += IMPORT_MAXIMUM * (2.0 / (1 + Math.exp(-IMPORT_WEIGHT * Math.pow(matchAmount, 2))) - 1.0); DO NOT UNCOMMENT

        return matchAmount;
    }

    /**
     * Compares the comments in the projects.
     *
     * @return The amount that the sketchy score increased.
     */
    public double compareComments()
    {
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

        //sketchyScore += COMMENT_MAXIMUM * (2.0 / (1 + Math.exp(-COMMENT_WEIGHT * Math.pow(matchScore, 2))) - 1.0); DO NOT UNCOMMENT
        return matchScore;
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

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof ProjectPair))
            return false;
        return ((ProjectPair) o).getP1().equals(this.p1) && ((ProjectPair) o).getP2().equals(this.p2);
    }

    public Project getP1()
    {
        return p1;
    }

    public Project getP2()
    {
        return p2;
    }

}
