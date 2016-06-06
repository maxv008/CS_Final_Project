package com.company;

import java.util.*;

public class ProjectPair
{
    private final Project p1, p2;
    //Equation to use: 2/(1 + Math.exp(-Math.pow(x,2))) -1 because it grows most in middle instead of start
    //private List<String> comments; TODO: Implement comments for what is contributing to the sketchy score.

    ProjectPair(Project p1, Project p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    public double getSketchyScore() //Every time you add a new metric, add another line here (Might need to make new constants and then seed them).
    {
        double result = 0;
        //The Constants are set in the sketchyLearning function, currently set manually.
        result += statFunction(compareImports(), "iMax", "iWeight");
        result += statFunction(compareComments(), "cMax" , "cWeight");

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
        return SketchyLearning.constants.get(maxKey) * (2.0 / (1.0 + Math.exp(-SketchyLearning.constants.get(weightKey)
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
                double similarity = StringUtilities.stringSimilarity(s1, s2);
                greatest = Math.max(similarity, greatest);
            }
            matchScore += greatest;
        }
        //TODO: See if there is a more robust way to use stringSimilarity.

        return matchScore;
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
