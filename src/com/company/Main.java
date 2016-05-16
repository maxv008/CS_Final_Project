package com.company;

import java.io.*;
import java.util.*;

//Test This Directory: C:\Users\maxv0\OneDrive\Projects\Test
public class Main
{

    public static void main(String[] args) throws IOException
    {
        System.out.println("Input Folder Directory");
        Scanner in = new Scanner(System.in);
        File folder = new File(in.nextLine());
        File[] tempFileList = folder.listFiles();
        List<Project> projectList = new ArrayList<Project>();
        List<ProjectPair> pairList = new ArrayList<>();

        for (int i = 0; i < tempFileList.length; i++) //This puts all of the files into a string array, each with their own string.
        {
            FileReader reader;
            char[] textBuffer = new char[1024 * 1024]; //This may be a problem for much larger projects, consider later.

            try
            {
                if (!tempFileList[i].isDirectory())
                    reader = new FileReader(tempFileList[i]);
                else
                    continue;
            } catch (FileNotFoundException e)
            {
                continue;
                //TODO: Handle Exception Further If Needed
            }

            reader.read(textBuffer, 0, 1024 * 1024);
            projectList.add(new Project(new String(textBuffer), tempFileList[i].getName()));
        }

        for (int i = 0; i < projectList.size(); i++) //This can be used to compare every pair of projects when needed.
        {
            for (int j = 0; j < i; j++)
            {
                pairList.add(new ProjectPair(projectList.get(i), projectList.get(j)));
            }
        }

        for (ProjectPair p : pairList)
        {
            p.compareComments();
            p.compareImports();
            System.out.println(p.getSketchyScore());
        }

        in.next(); //just so it doesn't quit, don't forget to press enter to end the process when done testing.
    }

}
