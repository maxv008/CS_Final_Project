package com.company;

import java.util.*;
import java.io.*;

/**
 * Created by Max Vigdorchik on 5/6/2016.
 * <p>
 * C:\Users\maxv0\OneDrive\Projects\IntelliJ\Javaplagiarism\RobotAnony\resultAbove10Percent
 */
public class SketchyLearning //IGNORE THIS FOR NOW, JUST A PLACEHOLDER.
{
    public List<Double> constants;
    private final List<Project> projects;
    private final List<ProjectPair> pairList;
    private List<Map.Entry<ProjectPair,Integer>> data;

    public SketchyLearning(List<Project> projects, List<ProjectPair> pairList)
    {
        constants = new ArrayList<>();
        this.projects = projects;
        this.pairList = pairList;
        try
        {
            data = gatherData();
        }catch(FileNotFoundException e)
        {
            //TODO: HANDLE IT.
        }catch(IOException e)
        {
            //TODO: Catch something here too.
        }
    }

    /**
     * Pulls data from the 41 test cases from APCS, and organizes them in a such a way that it can be used for regression.
     * The data is not formatted in a good way for this, so the method will be really ugly. Sorry. It is also specific to
     * the exact set of files Mr. Young gave and does not apply otherwise.
     */
    private List<Map.Entry<ProjectPair,Integer>> gatherData() throws FileNotFoundException,IOException
    {
        List<Map.Entry<ProjectPair,Integer>> result = new ArrayList<>();
        List<ProjectPair> unusedPairs = new LinkedList<>(); //Since the data only includes top 90%, this allows other 10% to be filled with 0s.
        unusedPairs.addAll(pairList); //Needed because its a linked list (will speed up the remove method by a good amount).

        for(int i = 0; i <= 343; i++)
        {
            BufferedReader in = new BufferedReader( //Sorry this is meant for my computer for now.
                    new FileReader("C:\\Users\\maxv0\\OneDrive\\Projects\\IntelliJ\\Javaplagiarism\\RobotAnony\\resultAbove10Percent\\" +
                    "match" + i + "-link.html"));
            for(int j = 0; j < 5; j++)
                in.readLine();
            String projectNameLine = in.readLine(); //6th line which contains the project names.
            String matchValueLine = in.readLine(); //7th line which contains the percent match.

            //unusedPairs.remove(The Project Pair generated goes here)
        }
        return null; //TODO: Fix this (gotta please that compiler).
    }
}
