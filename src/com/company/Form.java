package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * Created by daniel on 6/5/16.
 * A class with a form.
 */
public class Form {
    JFrame frame;
    JLabel folderLabel;
    JButton folderButton;
    JPanel statusPanel;
    JLabel statusLabel;
    JButton processButton;
    JProgressBar progressBar;
    JTextArea statusArea;
    File selectedFolder;

    public static void main(String[] args) {
        (new Form()).setVisible(true);
    }

    public void setVisible(boolean visible) {
        if(frame == null) {
            buildGui();
        }
        frame.setVisible(visible);
    }

    private void buildGui() {
        frame = new JFrame("Plagiarism Detector");
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        JPanel folderPanel = new JPanel(new BorderLayout());
        folderLabel = new JLabel();
        folderLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        folderPanel.add(folderLabel, BorderLayout.CENTER);
        JPanel folderButtonPanel = new JPanel(new BorderLayout());
        folderButtonPanel.add(Box.createHorizontalStrut(5));
        folderButton = new JButton("Select folder...");
        folderButton.addActionListener((e) -> selectFolder());
        folderButtonPanel.add(folderButton, BorderLayout.EAST);
        folderPanel.add(folderButtonPanel, BorderLayout.EAST);
        northPanel.add(folderPanel);

        statusPanel = new JPanel(new CardLayout());
        JPanel statusPanelWithButton = new JPanel(new BorderLayout());
        statusLabel = new JLabel("(no folder selected)");
        statusPanelWithButton.add(statusLabel, BorderLayout.WEST);
        statusPanelWithButton.add(Box.createHorizontalStrut(5), BorderLayout.CENTER);
        processButton = new JButton("Process >>");
        processButton.addActionListener((e) -> new Thread(this::process).start());
        processButton.setEnabled(false);
        statusPanelWithButton.add(processButton, BorderLayout.EAST);
        statusPanel.add(statusPanelWithButton, "button");
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        statusPanel.add(progressBar, "bar");

        northPanel.add(Box.createVerticalStrut(5));
        northPanel.add(statusPanel);
        northPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        statusArea = new JTextArea("Loading complete.");
        statusArea.setEditable(false);
        statusArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        frame.setBounds(Helper.getCenteredBounds(500, 300));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
    }

    public void println(Object newStatus) {
        statusArea.append("\n" + newStatus.toString());
    }

    private void selectFolder() {
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFolder = fc.getSelectedFile();
            folderLabel.setText(selectedFolder.getAbsolutePath());
            int numFiles = selectedFolder.listFiles().length;
            if(numFiles > 0) {
                int numJavaFiles = selectedFolder.listFiles((d, n) -> n.endsWith(".java")).length;
                processButton.setEnabled(numJavaFiles > 0);
                statusLabel.setText(numJavaFiles + " Java files found.");
                println(String.format("%d files found, of which %d were Java files.", numFiles, numJavaFiles));
            } else {
                println("There aren't any files in that folder!");
                processButton.setEnabled(false);
            }
        }
    }

    private void process() {
        SwingUtilities.invokeLater(() -> {
            ((CardLayout)statusPanel.getLayout()).next(statusPanel);
            folderButton.setEnabled(false);
        });
        String reportPath = selectedFolder.getAbsolutePath() + File.separator + "report.html";
        try {
            ReportMaker rm = new ReportMaker(selectedFolder, this::println);
            rm.addPropertyChangeListener((e) -> {
                if(e.getPropertyName().equals("progress")) {
                    progressBar.setValue((int)e.getNewValue());
                }
            });
            rm.execute();
            rm.get(); // Block until we have the report generated
            println("Opening report in a web browser...");
            if(Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("file://" + reportPath));
            }
        } catch(IOException ex) {
            JOptionPane.showConfirmDialog(frame, "Error reading or writing a file!", "Error!",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch(URISyntaxException ex) {
            JOptionPane.showConfirmDialog(frame, "Error opening the generated report in a web browser.", "Error!",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
