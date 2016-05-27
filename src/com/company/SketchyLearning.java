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
    private List<Map.Entry<ProjectPair, Double>> data;

    public SketchyLearning(List<Project> projects, List<ProjectPair> pairList)
    {
        constants = new ArrayList<>();
        this.projects = projects;
        this.pairList = pairList;
        try
        {
            data = gatherData();
        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
            //TODO: Catch something here too.
        }
    }

    /**
     * Pulls data from the 41 test cases from APCS, and organizes them in a such a way that it can be used for regression.
     * The data is not formatted in a good way for this, so the method will be really ugly. Sorry. It is also specific to
     * the exact set of files Mr. Young gave and does not apply otherwise.
     */
    private List<Map.Entry<ProjectPair, Double>> gatherData() throws IOException
    {
        List<Map.Entry<ProjectPair, Double>> result = new ArrayList<>();
        List<ProjectPair> unusedPairs = new LinkedList<>(); //Since the data only includes top 90%, this allows other 10% to be filled with 0s.
        unusedPairs.addAll(pairList); //Needed because its a linked list (will speed up the remove method by a good amount).

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
            Project p1 = new Project("",""), p2 = new Project("","");
            for (Project p : projects)
            {
                if (p.getFileName().equalsIgnoreCase(p1Name))
                    p1 = p;
                if (p.getFileName().equalsIgnoreCase(p2Name))
                    p2 = p;
            }

            String matchValueLine = in.readLine(); //7th line which contains the percent match.
            Double value = Double.valueOf(matchValueLine.substring(20, matchValueLine.indexOf("%")));

            result.add(new AbstractMap.SimpleEntry<ProjectPair, Double>(new ProjectPair(p1, p2), value));
            unusedPairs.remove(new ProjectPair(p1,p2));
        }
        for(ProjectPair p : unusedPairs)//Young's data only includes values >= 10%, so the rest are being seeded as 5% for now.
            result.add(new AbstractMap.SimpleEntry<ProjectPair,Double>(p, 5.0)); //the value 5.0 is arbitrary
        return result;
    }
}

