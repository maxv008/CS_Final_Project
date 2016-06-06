package com.company;

import java.util.*;
import java.io.*;

/**
 * Created by Max Vigdorchik on 5/6/2016.
 * <p> //C:\Users\maxv0\OneDrive\Projects\IntelliJ\Javaplagiarism\
 * C:\Users\maxv0\OneDrive\Projects\IntelliJ\Javaplagiarism\RobotAnony\resultAbove10Percent
 */
public class SketchyLearning
{
    public static Map<String, Double> constants;
    private static List<Project> projects = new ArrayList<>();
    private static List<ProjectPair> pairList = new ArrayList<>();
    private static List<Map.Entry<ProjectPair, Double>> data;

    public static void setConstants()
    {
        setConstants(null, null);
    }

    public static void setConstants(List<Project> p, List<ProjectPair> plist) //Just sets up static constants
    {
        constants = new TreeMap<>();
        constants.put("iMax", 0.05);
        constants.put("iWeight", 2.0);
        constants.put("cMax", 0.2);
        constants.put("cWeight", 2.0);
        projects = p;
        pairList = plist;
        try
        {
            BufferedReader r = new BufferedReader(new FileReader("Constants.ini")); //Careful as order of Treemap must coincide with order of results.
            r.readLine();
            for (Map.Entry<String, Double> c : constants.entrySet()) //Grabs constants from results file
            {
                String value = r.readLine();
                constants.put(c.getKey(), Double.parseDouble(value.substring(value.indexOf("=") + 2)));
            }
            r.close();
        } catch (IOException e)
        {
            System.out.println("Can't read pre-calculated values, using defaults \n" +
                    "This should have no effect on the program.");
        }
    }

    /**
     * Calculated the residual r_i for a certain data point
     *
     * @param dataPoint Known data point with Project pair and the similarity.
     * @return The difference between "actual" value and predicted value.
     */
    private static double residual(Map.Entry<ProjectPair, Double> dataPoint)
    {
        return dataPoint.getValue() - dataPoint.getKey().getSketchyScore();
    }

    /**
     * Pulls data from the 41 test cases from APCS, and organizes them in a such a way that it can be used for regression.
     * The data is not formatted in a good way for this, so the method will be really ugly. Sorry. It is also specific to
     * the exact set of files Mr. Young gave and does not apply otherwise.
     */
    public static List<Map.Entry<ProjectPair, Double>> gatherData() throws IOException
    {
        List<Map.Entry<ProjectPair, Double>> result = new ArrayList<>();
        List<ProjectPair> unusedPairs = new LinkedList<>(); //Since the data only includes top 90%, this allows other 10% to be filled arbitrarily.
        unusedPairs.addAll(pairList); //Needed because its a linked list (will speed up the remove method by a good amount).

        try
        {
            for (int i = 0; i <= 343; i++)
            {
                BufferedReader in = new BufferedReader( //Sorry this is meant for my computer for now.
                        new FileReader("RobotAnony\\resultAbove10Percent\\" +
                                "match" + i + "-link.html"));
                for (int j = 0; j < 5; j++)
                    in.readLine();

                String projectNameLine = in.readLine(); //6th line which contains the project names.
                String p1Name = projectNameLine.substring(32, 64);//This is using the structure of those html files.
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

                result.add(new AbstractMap.SimpleEntry<>(new ProjectPair(p1, p2), value));
                unusedPairs.remove(new ProjectPair(p1, p2));
                in.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        for (ProjectPair p : unusedPairs)//Young's data only includes values >= 10%, so the rest are being seeded as 5% for now.
            result.add(new AbstractMap.SimpleEntry<ProjectPair, Double>(p, 0.05)); //the value 0.05 is arbitrary

        return result;
    }

    /**
     * Writes all necessary data into a file so that it can be processed by a numeric solver.
     */
    public static void writeData() throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Compiled_Data.txt"), "utf-8"));
        writer.write(Integer.toString(constants.size()));
        writer.newLine();

        for (Double c : constants.values())
        {
            writer.write(c.toString());
            writer.newLine();
        }

        for (Map.Entry<ProjectPair, Double> d : data)
        {
            writer.write(Double.toString(d.getKey().compareComments()) + "," //Make sure this is done alphabetically!
                    + Double.toString(d.getKey().compareImports()) + ","
                    + Double.toString(d.getValue()));
            writer.newLine();
        }
        writer.close();
    }

}

