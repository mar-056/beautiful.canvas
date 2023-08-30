/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Spring 2023
 * Instructor: Prof. Brian King
 *
 * Name: Joshua Lee
 * Section: Section 1 9 am
 * Date: 4/5/23
 * Time: 9:36 AM
 *
 * Project: csci205_final_project
 * Package: org.mental_health
 * Class: MentalHealthModel
 *
 * Description:
 * Model class for the mental health app for the MVC design
 * ****************************************
 */

package org.mental_health;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.mental_health.model.ChatGPT;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class AppModel {
    /**
     * simple boolean to check if box is checked
     */
    private SimpleBooleanProperty isChecked;
    /**
     * simple boolean to check if box is unchecked
     */
    private SimpleBooleanProperty isUnchecked;
    /**
     * HashMap that stores the date and Mood of the Day
     */
    public static HashMap<LocalDate, String> map = new HashMap<>();
    /**
     * get the key in the hashmap to access the local dates
     */
    private LocalDate key;

    /**
     * Constructor to create a new app model
     */
    public AppModel(){
        this.isChecked = new SimpleBooleanProperty(true);
        this.isUnchecked = new SimpleBooleanProperty(false);
    }

    /**
     * When each Day Mood Button is Pressed, This method is called
     * to store the Mood of the Day into the HashMap, it can also be used
     * to update the existing mood
     * @param Mood
     */
    public void Update_Day_Mood(String Mood){
        // Get the current date
        LocalDate now = LocalDate.now();

        // Use the current time as the key
        map.put(now, Mood);

        // Print the value associated with the current time
        System.out.println(map.get(now));

    }

    /**
     * This Method allows you to retrieve the mood of the Day Based on the
     * Date Provided
     * @param date
     * @return String representing the mood
     */
    public String getMoodForDate(LocalDate date) {
        // Check if the date is in the map
        if (map.containsKey(date)) {
            // Return the mood value associated with the date
            return map.get(date);
        } else {
            // Return a message indicating that no mood was found for the date
            return "No mood found for " + date;
        }
    }

    /**
     * Method to get the LocalDate
     * used to get the date at the moment the button is pressed
     *
     * @return LocalDate representing the current date
     */
    public LocalDate getDate(){
        for (LocalDate keys : map.keySet()){
            key = keys;
        }
        return key;
    }

    /**
     * getter method for isChecked boolean
     * @return boolean
     */
    public boolean isIsChecked() {
        return isChecked.get();
    }

    /**
     * getter method that returns the isChecked object
     * @return {@link #isChecked}
     */
    public SimpleBooleanProperty isCheckedProperty() {
        return isChecked;
    }

    /**
     * getter method for isUnchecked boolean
     * @return boolean
     */
    public boolean isIsUnchecked() {
        return isUnchecked.get();
    }

    /**
     * getter method that returns the isUnchecked object
     * @return {@link #isUnchecked}
     */
    public SimpleBooleanProperty isUncheckedProperty() {
        return isUnchecked;
    }

    /**
     * Get a chatGPTBot to answer some text
     *
     * @param text input
     * @return a chatbot with the text given to it so that it can use it
     * @throws Exception
     */
    public String callChatGPT(String text) throws Exception {
        ChatGPT chatBot = new ChatGPT();
        return chatBot.chatGPT(text);
    }

}
