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
}
