package com.company;

import java.util.*;
import java.io.*;

/**
 * Created by maxv0 on 5/3/2016.
 */
public class Project
{
    private String content;
    private String fileName;

    public Project(String content, String fileName)
    {
        this.content = content;
        this.fileName = fileName;
    }

    @Override
    public String toString()
    {
        return content;
    }

    public String getFileName()
    {
        return fileName;
    }

    /**
     * Seperates out all of the import statements at the start.
     *
     * @return A string list containing each import statement and package statement.
     */
    public List<String> parseImport()
    {
        ArrayList<String> result = new ArrayList<String>();
        String[] firstRun = content.split(";");

        for (String s : firstRun)
        {
            if (s.contains("import") || s.contains("package"))
                result.add(s);
        }

        return result;
    }

    /**
     * Separates out all of the comments into an array. First come all the "//" comments and then the "/**" ones follow.
     * @return List of comments including the // or /**.
     */
    public List<String> parseComments()
    {
        List<String> result = new ArrayList<>();
        String[] firstRun = content.split("\n");
        for (String s : firstRun)
        {
            if (s.contains("//"))
            {
                //TODO: Make it cut the string to include only the part after the "//"
                result.add(s);
            }
        }

        for(int i = 0; i < firstRun.length; i++)
        {
            if(firstRun[i].contains("/**"))
            {
                result.add(firstRun[i]); //TODO: Make it only add the part after "/**"
                if(!firstRun[i].contains("*/")) //If statement is only here to handle the case where one line has both "/**" and "*/"
                {
                    while (!firstRun[++i].contains("*/"))
                    {
                        result.add(firstRun[i]);
                    }
                    result.add(firstRun[i]); //TODO: Fix this so it only adds the part before "*/"
                }
            }
        }

        return result;
    }
}
