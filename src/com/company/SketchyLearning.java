package com.company;

import java.util.*;
import java.io.*;

/**
 * Created by Max Vigdorchik on 5/6/2016.
 * <p>
 * C:\Users\maxv0\OneDrive\Projects\IntelliJ\Javaplagiarism\RobotAnony\resultAbove10Percent
 */
public class SketchyLearning
{
    public Map<String ,Double> constants;
    private final List<Project> projects;
    private final List<ProjectPair> pairList;
    private List<Map.Entry<ProjectPair, Double>> data;

    public SketchyLearning(List<Project> projects, List<ProjectPair> pairList)
    {
        constants = new TreeMap<>();
            constants.put("iMax", 5.0);
            constants.put("iWeight", 2.0);
            constants.put("cMax", 20.0);
            constants.put("cWeight", 2.0);
        this.projects = projects;
        this.pairList = pairList;
    }

    /**
     * Calculated the residual r_i for a certain data point
     *
     * @param dataPoint Known data point with Project pair and the similarity.
     * @return The difference between "actual" value and predicted value.
     */
    private double residual(Map.Entry<ProjectPair, Double> dataPoint)
    {
        return dataPoint.getValue() - dataPoint.getKey().getSketchyScore();
    }

    /**
     * Pulls data from the 41 test cases from APCS, and organizes them in a such a way that it can be used for regression.
     * The data is not formatted in a good way for this, so the method will be really ugly. Sorry. It is also specific to
     * the exact set of files Mr. Young gave and does not apply otherwise.
     */
    public List<Map.Entry<ProjectPair, Double>> gatherData() throws IOException
    {
        List<Map.Entry<ProjectPair, Double>> result = new ArrayList<>();
        List<ProjectPair> unusedPairs = new LinkedList<>(); //Since the data only includes top 90%, this allows other 10% to be filled arbitrarily.
        unusedPairs.addAll(pairList); //Needed because its a linked list (will speed up the remove method by a good amount).

        try
        {
            for (int i = 0; i <= 343; i++)
            {
                BufferedReader in = new BufferedReader( //Sorry this is meant for my computer for now.
                        new FileReader("C:\\Users\\maxv0\\OneDrive\\Projects\\IntelliJ\\Javaplagiarism\\RobotAnony\\resultAbove10Percent\\" +
                                "match" + i + "-link.html"));
                for (int j = 0; j < 5; j++)
                    in.readLine();

                String projectNameLine = in.readLine(); //6th line which contains the project names.
                String p1Name = projectNameLine.substring(32, 64);//This is abusing the structure of those html files.
                String p2Name = projectNameLine.substring(67, 99);
                Project p1 = new Project("", ""), p2 = new Project("", "");
                for (Project p : projects)
                {
                    if (p.getFileName().equalsIgnoreCase(p1Name))
                        p1 = p;
                    if (p.getFileName().equalsIgnoreCase(p2Name))
                        p2 = p;
                }

                String matchValueLine = in.readLine(); //7th line which contains the percent match.
                Double value = Double.valueOf(matchValueLine.substring(20, matchValueLine.indexOf("%"))) / 100;

                result.add(new AbstractMap.SimpleEntry<ProjectPair, Double>(new ProjectPair(p1, p2), value));
                unusedPairs.remove(new ProjectPair(p1, p2));
            }
        }catch(IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        for (ProjectPair p : unusedPairs)//Young's data only includes values >= 10%, so the rest are being seeded as 5% for now.
            result.add(new AbstractMap.SimpleEntry<ProjectPair, Double>(p, 0.05)); //the value 0.05 is arbitrary

        data = result;
        return result;
    }

    public Map<String,Double> getConstants()
    {
        return constants;
    }
}

