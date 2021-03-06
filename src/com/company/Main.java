package com.company;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

//Test This Directory: C:\Users\maxv0\OneDrive\Projects\Test or C:\Users\Max\SkyDrive\Projects\Test
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

        if (!folder.isDirectory()) {
            System.err.format("%s is not a directory!", folder.getAbsolutePath());
            System.exit(1);
        }
        
        for (File file : folder.listFiles()) {
            String allTextInFile = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            projectList.add(new Project(allTextInFile, file.getName()));
        }

        for (int i = 0; i < projectList.size(); i++) //This can be used to compare every pair of projects when needed.
        {
            for (int j = 0; j < i; j++)
            {
                pairList.add(new ProjectPair(projectList.get(i), projectList.get(j)));
            }
        }

        SketchyLearning.setConstants(projectList, pairList); //With everything in place constants can be set.

        for (ProjectPair p : pairList) //For testing purposes. Currently printing odd values as weights are set too high.
        {
            //System.out.println(p.getSketchyScore());
        }

        for(double d : SketchyLearning.constants.values())
        {
            System.out.println(d);
        }
    }

}
