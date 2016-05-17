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
    public List<String> parseImport() //TODO: to speedup can stop checking after a non import statement (if actually needed).
    {
        ArrayList<String> result = new ArrayList<String>();
        String[] firstRun = content.split(";");

        for (String s : firstRun) //TODO: Might need to see if there is a comma syntax variation to consider.
        {
            if (s.contains("import") || s.contains("package"))
                result.add(s);
        }

        return result;
    }

    /**
     * Separates out all of the comments into an array. First come all the "//" comments and then the "/**" ones follow.
     *
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
                result.add(s.substring(s.indexOf("//"))); //TODO: Maybe change index to not include the "//" characters.
            }
        }

        for (int i = 0; i < firstRun.length; i++) //TODO: Make it handle multiple "/**" comments in a single line.
        {
            String longComment = ""; //Containing everything in one string so a longcomment is a single element in result.

            if (firstRun[i].contains("/*")) //TODO: Test for "/*" parsing which I just added.
            {
                if (firstRun[i].contains("*/")) //Meaning the entire thing is one line total.
                {
                    longComment += firstRun[i].substring(firstRun[i].indexOf("/*"), firstRun[i].indexOf("*/") + 2) + "\n";
                } else
                {
                    longComment += firstRun[i].substring(firstRun[i].indexOf("/*")) + "\n";
                    i++; //To prevent it repeating the first line in the result.
                    while (!firstRun[i].contains("*/"))
                    {
                        longComment += firstRun[i] + "\n";
                        i++; //Messing with the i variable is probably a bad idea but whatever.
                        if (i >= firstRun.length) break;//in case there never is a "*/"
                    }
                    longComment += firstRun[i].substring(0, firstRun[i].indexOf("*/") + 2);
                }
                result.add(longComment);
            }
        }

        return result;
    }
}