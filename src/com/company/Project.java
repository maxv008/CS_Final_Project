package com.company;
import java.util.*;
import java.io.*;

/**
 * Created by maxv0 on 5/3/2016.
 */
public class Project
{
    private String content;

    public Project(String content)
    {
        this.content = content;
    }

    @Override public String toString()
    {
        return content;
    }

    /**
     * Seperates out all of the import statements at the start.
     * @return A string list containing each import statement and package statement.
     */
    public List<String> parseImport()
    {
        ArrayList<String> result = new ArrayList<String>();
        String[] firstRun = content.split(";"); //TODO: Fix this to find where import statement ends.

        for(String s : firstRun)
        {
            if(s.contains("import") || s.contains("package"))
                result.add(s);
        }

        return result;
    }
}
