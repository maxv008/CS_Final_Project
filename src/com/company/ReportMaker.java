package com.company;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * Makes reports.
 * Created by daniel on 6/5/16.
 */
public class ReportMaker extends SwingWorker<Void, String> {
    private File projectFolder;
    private Consumer<String> println;

    public ReportMaker(File projectFolder, Consumer<String> println) {
        this.projectFolder = projectFolder;
        this.println = println;
    }

    @Override
    public Void doInBackground() throws IOException {
        writeReport(makeReport(getProjectList()));
        return null;
    }

    @Override
    public void process(List<String> strings) {
        strings.stream().forEach(println);
    }

    @Override
    protected void done() {
        try {
            get();
            setProgress(100);
        } catch(ExecutionException ex) {
            ex.getCause().printStackTrace();
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private List<Project> getProjectList() throws IOException {
        List<Project> projects = new ArrayList<>();
        for (File file : projectFolder.listFiles()) {
            if(file.isDirectory()) continue;
            if(!file.getName().endsWith(".java")) continue;
            String allTextInFile = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            projects.add(new Project(allTextInFile, file.getName()));
        }
        return projects;
    }

    private String makeReport(List<Project> projects) {
        StringBuilder sb = new StringBuilder();
        addHeader(sb);
        sb.append("<p>").append(projects.size()).append(" Java files to parse.</p>");
        publish("Initializing machine learning...");
        List<ProjectPair> projectPairs = getPairs(projects);
        SketchyLearning.setConstants(projects, projectPairs);
        sb.append("<table><tr><th>Project 1</th><th>Project 2</th><th>Score</th></tr>");
        publish("Getting scores...");
        Map<ProjectPair, Double> scores = new HashMap<>();
        int numProjectPairs = projectPairs.size();
        setProgress(0);
        for(int i = 0; i < numProjectPairs; i++) {
            scores.put(projectPairs.get(i), projectPairs.get(i).getSketchyScore());
            setProgress((int)(100 * ((float)i / numProjectPairs)));
        }
        publish("Sorting project pairs...");
        double expectedCompares = projectPairs.size()*Math.log(projectPairs.size());
        setProgress(0);
        int[] compareCounter = new int[] {0};
        projectPairs.sort((a, b) -> {
            setProgress(Math.min(99, compareCounter[0] > expectedCompares
                    ? 100
                    : (int)(100 * compareCounter[0] / expectedCompares)));
            compareCounter[0]++;
            return scores.get(b).compareTo(scores.get(a));
        });
        publish("Project pairs sorted.");
        setProgress(0);
        for(int i = 0; i < numProjectPairs; i++) {
            ProjectPair pair = projectPairs.get(i);
            sb.append("<tr><td>").append(pair.getP1().getFileName()).append("</td><td>")
                    .append(pair.getP2().getFileName()).append("</td><td>").append(scores.get(pair))
                    .append("</td></tr>");
            setProgress((int)(100 * ((float)i / numProjectPairs)));
        }
        sb.append("</table></body></html>");
        return sb.toString();
    }

    private void writeReport(String report) throws FileNotFoundException, UnsupportedEncodingException {
        String reportPath = projectFolder.getAbsolutePath() + File.separator + "report.html";
        PrintWriter writer = new PrintWriter(reportPath, "UTF-8");
        writer.println(report);
        writer.close();
        publish("Report written to " + reportPath);
    }

    private List<ProjectPair> getPairs(List<Project> projects) {
        List<ProjectPair> result = new ArrayList<>();
        for (int i = 0; i < projects.size(); i++) {
            for (int j = 0; j < i; j++) {
                result.add(new ProjectPair(projects.get(i), projects.get(j)));
            }
        }
        return result;
    }

    private void addHeader(StringBuilder sb) {
        sb.append("<!doctype html><html><head><meta charset=\"utf-8\" /><title>Plagiarism Detector Results</title>" +
                "<style type='text/css'>body{margin:40px auto;max-width:650px;line-height:1.6;font-size:18px;\n" +
                "color:#444;padding:0 10px}h1,h2,h3{line-height:1.2}</style></head><body><h1>Plagiarism Detector Results</h1>");
    }
}
