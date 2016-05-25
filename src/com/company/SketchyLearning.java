package com.company;

import java.util.*;

/**
 * Created by Max Vigdorchik on 5/6/2016.
 * <p>
 * I have no idea how this will end up working.
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
        data = gatherData();
    }

    /**
     * Pulls data from the 41 test cases from APCS, and organizes them in a such a way that it can be used for regression.
     * The data is not formatted in a good way for this, so the method will be really ugly. Sorry.
     */
    public static List<Map.Entry<ProjectPair,Integer>> gatherData()
    {

        return null; //TODO: Fix this (gotta please that compiler).
    }
}
