/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Spring 2023
 * Instructor: Prof. Brian King
 *
 * Name: Joshua Lee
 * Section: Section 1 9 am
 * Date: 4/5/23
 * Time: 9:57 AM
 *
 * Project: csci205_final_project
 * Package: org.mental_health
 * Class: AppController
 *
 * Description:
 * Controller class for the mental health app MVC design
 * ****************************************
 */

package org.mental_health;

import javafx.geometry.Pos;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

public class AppController {
    /**
     * create an object of the model to use in the controller
     */
    private AppModel theModel;
    /**
     * create an object of the view to use in the controller
     */
    private AppView theView;
    /**
     * keep track of progress for the progress bar
     */
    private double progress = 0;
    /**
     * keep track of the progress lbl to see if all habits are finished
     */
    private int progresslbl = 0;
    /**
     * create a arraylist to store all the moods tracked on the file
     */
    private ArrayList<String> moodHolder;
    /**
     * create a tree with a comparator to sort by local date
     */
    private Map<LocalDate, String> uniqueTree;

    /**
     * create a new AppController that will instantiate the model and the view
     * @param theModel
     * @param theView
     */
    public AppController(AppModel theModel, AppView theView) throws IOException{
        this.theModel = theModel;
        this.theView = theView;
        this.moodHolder = new ArrayList<>();

        // create a new tree with a comparator
        uniqueTree = new TreeMap<>(new Comparator<LocalDate>() {
            @Override
            public int compare(LocalDate date1, LocalDate date2) {
                return date1.compareTo(date2);
            }
        });

        // handle all event handlers
        initEventHandlers();

        // handle scene changing
        sceneChangeHandler();
    }

    /**
     * initialize the event handlers
     */
    private void initEventHandlers() throws IOException {

        // keep track of the buttons
        trackButton();

        // keep track of the progress
        habitTracker();

        // plot the data from the file to the graph
        plotMoodGraph();
    }

    /**
     * keep track of when a button is pressed and also change the color
     * of the button when it is pressed
     */
    private void trackButton() {
        // loop through all the buttons
        for (Button button : theView.getButtonList()) {
            // keep the original style of the button
            String originalStyle = button.getStyle();
            button.setOnAction(event -> {
                theModel.Update_Day_Mood(button.getId());// Add Day Mood to the HashMap

                this.uniqueTree.put(theModel.getDate(), button.getId()); // add the date and id to the tree

                for (Button removebutton : theView.getButtonList()) { // changes color of button
                    removebutton.setStyle(originalStyle);
                }
                // call the method to write to the file
                saveDataToFile();
                button.setStyle("-fx-background-color: green;");
            });
        }
    }

    /**
     * edit the progress bar in the habit tracker whenever a habit is pressed. It
     * will fill the progress bar each time a button is pressed
     */
    private void habitTracker() {
        // method will edit the habit progress bar
        for (RadioButton habitButtons : theView.getHabitButtons()) {
            habitButtons.setOnAction(event -> {
                if (habitButtons.isSelected()) {
                    // add 1/5 progress
                    progress += 0.2;

                    // keep track of progress
                    progresslbl += 1;
                } else if (!habitButtons.isSelected()) {
                    // lower 1/5 progress
                    progress -= 0.2;

                    // keep track
                    progresslbl -= 1;
                }
                // set the progress bar
                theView.getHabitProgress().setProgress(progress);
                // add a counter to the label
                theView.getProgresslbl().setText(progresslbl + "/5");
                // check if progress is finished
                if (progresslbl == 5){
                    theView.getProgresslbl().setText("FINISHED!");
                }
            });
        }
    }

    /**
     * create a new graph and read the data from the file to plot it on a line chart
     */
    private void plotMoodGraph() {
        // add the data from the file to the graph
        // Mood Scores are Assigned on a 0-4 Scale
        // 0-Very Bad, 1-Poor, 2-Medium, 3-Good, 4-Excellent
        XYChart.Series<String, Number> data = new XYChart.Series<>();

        File file = new File("moodFile.txt");
        try {
            Scanner scnr = new Scanner(file);
            // check every line in the file
            while (scnr.hasNextLine()){
                String line = scnr.nextLine();
                // add each line to the arraylist
                moodHolder.add(line);
                // split the file strings for values to be used in the graph
                String[] parts = line.replace("{", "").replace("}","")
                        .replaceAll("^\\d{4}-","").split("=");
                // make sure the length is not lower than 1
                if (parts.length >= 2) {
                    // add the values to the graph
                    data.getData().add(new XYChart.Data<>(parts[0], Integer.parseInt(parts[1])));
                }
            }
        }
        catch (FileNotFoundException e){
            e.getMessage();
        }
        // Set the data to the bar chart
        theView.getBarChart().getData().add(data);
    }

    /**
     * change the scenes whenever a different button is clicked in the navigation bar
     */
    private void sceneChangeHandler() {
        theView.getChatBotButton().setOnAction(event -> {
            // add the home scene to the rootScene
            theView.getRootScene().getChildren().set(0, theView.getChatBotScene());
        });

        theView.getHomeButton().setOnAction(event -> {
            theView.getRootScene().getChildren().set(0, theView.getHomeScene());
        });

        theView.getCommunityButton().setOnAction(event -> {
            theView.getRootScene().getChildren().set(0, theView.getCommunityScene());
        });

        theView.getLibraryButton().setOnAction(event -> {
            theView.getRootScene().getChildren().set(0, theView.getLibraryScene());
        });

        theView.getMeButton().setOnAction(event -> {
            theView.getRootScene().getChildren().set(0, theView.getMeScene());
        });
    }

    /**
     * this method will create a new file and override the current one to save all data between days
     */
    private void saveDataToFile() {
            // open a new file
            File file = new File("moodFile.txt");
            try{
                // create a new print writer
                PrintWriter writer = new PrintWriter(file);

                // check if it is the first data being tracked
                if (moodHolder.size() == 0)
                {
                    writer.println(uniqueTree);
                }

                // if there is multiple data saved already, get all the data
                else {
                    for (String uniqueDates : moodHolder) {
                        writer.println(uniqueDates);
                    }
                    writer.println(uniqueTree);
                }
                writer.close();
            }
            catch (FileNotFoundException e){
                e.getMessage();
            }
    }
}
