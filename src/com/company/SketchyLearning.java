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
    public List<Project> projects;
    public List<ProjectPair> pairList;
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
     * The data is not formatted in a good way for this, so the method will be really ugly. Sorry.
     */
    public static List<Map.Entry<ProjectPair,Integer>> gatherData() throws FileNotFoundException,IOException
    {
        List<Map.Entry<ProjectPair,Integer>> result = new ArrayList<>();
        for(int i = 0; i <= 343; i++)
        {
            BufferedReader in = new BufferedReader( //Sorry this is meant for my computer for now.
                    new FileReader("C:\\Users\\maxv0\\OneDrive\\Projects\\IntelliJ\\Javaplagiarism\\RobotAnony\\resultAbove10Percent\\" +
                    "match" + i + "-link.html"));
            for(int j = 0; j < 5; j++)
                in.readLine();
            String projectNameLine = in.readLine();
            String matchValueLine = in.readLine();
        }
        return null; //TODO: Fix this (gotta please that compiler).
    }
}
