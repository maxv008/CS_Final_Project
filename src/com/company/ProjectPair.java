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
    private int sketchyScore; //Regression testing for how to actually seed this sketchy score may be needed, or we can juts guess.
    private List<String> comments;

    public ProjectPair(Project p1, Project p2)
    {
        this.p1 = p1;
        this.p2 = p2;
        sketchyScore = 0;
    }

    /**
     * Compares import statements in the two projects and adjusts the sketchy score.
     * @return How much the sketchy score increased.
     */
    public int compareImports()
    {
        int sketchyInitial = sketchyScore; //just gets the initials score to return the correct value
        List<String> p1Import = p1.parseImport();
        List<String> p2Import = p2.parseImport();

        for(int i = 0; i < p1Import.size(); i++)
        {
            for(int j = 0; j < p2Import.size(); j++)
            {
                if(p1Import.get(i).equalsIgnoreCase(p2Import.get(j)))
                    sketchyScore++; // Again idk about how these values should be seeded, this is a placeholder.
            }
        }
        return sketchyScore - sketchyInitial;
    }
}
