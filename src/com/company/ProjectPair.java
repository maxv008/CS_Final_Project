package com.company;
import java.util.*;
import java.io.*;

/**
 * Created by maxv0 on 5/3/2016.
 */
public class ProjectPair
{
    public Project p1;
    public Project p2;
    private int sketchyScore;
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

        return 0; //fix this later
    }
}
