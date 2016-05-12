package com.company;

import java.util.*;
import java.io.*;

/**
 * Created by maxv0 on 5/3/2016.
 */
public class ProjectPair
{
    private Project p1;
    private Project p2;
    private int sketchyScore; //Regression testing for how to actually seed this sketchy score may be needed, or we can just guess.
    //TODO: Find a way to seed all of the following constant values (more to come).
    private final int IMPORT_MAXIMUM = 5; // Maximum percentage contribution of import statements to sketchyscore.
    private final int IMPORT_WEIGHT = 3; //Smaller number means each matching import counts more.
    //private List<String> comments; TODO: Implement comments for what is contributing to the sketchy score.

    public ProjectPair(Project p1, Project p2)
    {
        this.p1 = p1;
        this.p2 = p2;
        sketchyScore = 0;
    }

    /**
     * Compares import statements in the two projects and adjusts the sketchy score.
     *
     * @return How much the sketchy score increased.
     */
    public int compareImports()
    {
        int sketchyInitial = sketchyScore; //just gets the initials score to return the correct value
        int matchAmount = 0; //number of matches found
        List<String> p1Import = p1.parseImport();
        List<String> p2Import = p2.parseImport();

        for (int i = 0; i < p1Import.size(); i++)
        {
            for (int j = 0; j < p2Import.size(); j++)
            {
                if (p1Import.get(i).equalsIgnoreCase(p2Import.get(j))) //Might need something more robust than equals.
                    matchAmount++;
            }
        }
        sketchyScore += IMPORT_MAXIMUM * (1 - Math.exp(matchAmount / (IMPORT_WEIGHT))); //Like the equation for charging capacitors.

        return sketchyScore - sketchyInitial;
    }
}
