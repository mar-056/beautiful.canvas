/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Spring 2023
 * Instructor: Prof. Brian King
 *
 * Name: Joshua Lee
 * Section: Section 1 9 am
 * Date: 4/5/23
 * Time: 6:14 PM
 *
 * Project: csci205_final_project
 * Package: org.mental_health
 * Class: AppView
 *
 * Description:
 * View class for the mental health app MVC design
 * ****************************************
 */

package org.mental_health;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * create the view for the mental health app
 */
public class AppView {
    /**
     * store the model
     */
    private AppModel theModel;

    /**
     * create a new vBox homeScene
     */
    private VBox homeScene;
    /**
     * Create a new VBox for chatBotScene
     */
    private VBox chatBotScene;

    /**
     * HBox that will store the mood faces
     */
    private HBox hMoods;
    /**
     * HBox that will store the label to ask user how they are feeling
     */
    private HBox hMoodsQuestionBox;
    /**
     * label that will write how are you feeling
     */
    private Label moodQuestionLabel;
    /**
     * HBox to store all the mood labels
     */
    private HBox moodLabels;
    /**
     * label for the title of habit tracking
     */
    private Label habitTrackingLabel;

    /**
     * Create the container for the checkboxes and the graph next to it.
     */
    private VBox habitTrackerScene;
    /**
     * create a line chart to save the data stored in the file
     */
    private LineChart<String, Number> lineChart;
    /**
     * Hbox to store the habit trackers and chart
     */
    private HBox listAndPieChart;
    /**
     * store the habit lists in a VBox
     */
    private VBox habitList;
    /**
     * store the chart for habits in a VBox
     */
    private VBox habitPieChart;

    /**
     * create 5 radio buttons that will keep track of the habits
     */
    private RadioButton didExercise;
    private RadioButton slept8Hours;
    private RadioButton meditated;
    private RadioButton journaled;
    private RadioButton relaxed;

    /**
     * store the mood question scene in a VBox
     */
    private VBox moodQuestionScene;
    /**
     * array list will be used to get access to all the buttons
     */
    private ArrayList<Button> buttonsArray;
    /**
     * store the progress bar
     */
    private ProgressBar habitProgress;
    /**
     * array list to get access to the radio buttons
     */
    private ArrayList<RadioButton> habitButtons;
    /**
     * progress label
     */
    private Label progresslbl;

    private VBox navBarScene;
    private Button editButton;
    private VBox chatBackgroundScene;

    /**
     * Container for the different habits
     * i.e. the buttons with the radio buttons and habits
     */
    private HBox habitContainer2;
    private HBox habitContainer1;
    private HBox habitContainer3;
    private HBox habitContainer4;
    private HBox habitContainer5;

    /**
     * Buttons for menu at bottom of app
     */
    private Button chatBotButton;
    private Button communityButton;
    private Button homeButton;
    private Button libraryButton;
    private Button meButton;

    /**
     * VBoxes for the navigation bar
     */
    private VBox rootScene;
    private HBox navBarBox;
    private VBox communityScene;
    private VBox libraryScene;
    private VBox meScene;
    private ArrayList<Label> messages;
    private int index;
    private Button sendButton;

    /**
     * Text fields for habits and user inputs
     */
    private TextField userInput;
    private TextField habit1;
    private TextField habit2;
    private TextField habit3;
    private TextField habit4;
    private TextField habit5;

    /**
     * Constructor to create a new AppView that instantiates the model
     * @param theModel
     */
    public AppView(AppModel theModel){
        this.theModel = theModel;
        initRootSceneGraph();
        initStyling();
    }

    /**
     * Method that initializes the styling of the view
     */
    private void initStyling() {

    }

    /**
     * create the root scene. It will be a VBox and the parent root of everything
     */
    public void initRootSceneGraph() {
        // Initializing root scene
        this.rootScene = new VBox();

        // add the rootScene VBox to css
        rootScene.getStyleClass().add("root-scene");

        initHomeSceneGraph();

        // add the home scene to the rootScene
        rootScene.getChildren().add(homeScene);

        initChatBotSceneGraph();
        initCommunitySceneGraph();
        initLibrarySceneGraph();
        initMeSceneGraph();

        // Initializing navBarScene
        initNavBar();

        // Add the VBox for navBar to the homeScene
        rootScene.getChildren().add(navBarBox);
        navBarBox.setAlignment(Pos.BOTTOM_CENTER);
    }

    /**
     * scene graph that will create home scene to be displayed
     */
    public void initHomeSceneGraph(){
        // Initializing home scene
        this.homeScene = new VBox();

        // add the homeScene VBox to css
        homeScene.getStyleClass().add("home-scene");

        // Initializing moodQuestionScene
        initMoodQuestionScene();

        // Initializing habitTrackerScene
        initHabitTrackerScene();

        // Initializing Bar Chart Scene
        initBarChart();

        // add the mood question scene to the homeScene
        homeScene.getChildren().add(moodQuestionScene);

        // add the VBox to the homeScene
        homeScene.getChildren().add(habitTrackerScene);

        // Add Visual Chart to HomeScene
        homeScene.getChildren().add(lineChart);
    }

    /**
     * scene graph that will create chat bot scene to be displayed
     */
    public void initChatBotSceneGraph(){
        // Initializing chatBotScene
        this.chatBotScene = new VBox();
        // add the chatBotScene VBox to css
        chatBotScene.getStyleClass().add("chat-bot-scene");

        // HBox for the title
        HBox chatHBox = new HBox();
        chatHBox.getStyleClass().add("chat-hbox");

        // create the AI CHAT title label
        Label chatLabel = new Label("AI CHAT");
        chatLabel.getStyleClass().add("chat-label");

        // Add AI CHAT label to the chatHBox
        chatHBox.getChildren().add(chatLabel);
        chatBotScene.getChildren().add(chatHBox);

        VBox messageBox = new VBox();
        messageBox.setPadding(new Insets(10, 10, 10, 10));
        messageBox.prefWidthProperty().bind(chatBotScene.widthProperty().multiply(0.95));
        messageBox.getStyleClass().add("message-box");

        // Setting up the scroll pane to put message box as a child
        ScrollPane scroll = new ScrollPane();
        scroll.getStyleClass().add("scroll");
        scroll.setContent(messageBox);

        chatBotScene.getChildren().add(scroll);

        //HBox for the user input and send button
        HBox userInputHBox = new HBox();
        userInputHBox.getStyleClass().add("user-input-hbox");
        userInputHBox.setAlignment(Pos.BOTTOM_CENTER);

        // TextField
        userInput = new TextField();
        userInput.getStyleClass().add("user-input");
        // Send button
        sendButton = new Button("Send");
        sendButton.getStyleClass().add("send-button");

        // Add userInout TextField and send Button to the userInputHBox
        userInputHBox.getChildren().addAll(userInput, sendButton);

        chatBotScene.getChildren().add(userInputHBox);

        //TODO getter method for send button
        //TODO getter method for userMessageLabel
        sendButton.setOnAction(event -> {
            Label userMessageLabel = new Label(userInput.getText());
            userMessageLabel.getStyleClass().add("user-message-label");

            // Aligning the message
            HBox userMessage = new HBox();
            userMessage.getChildren().add(userMessageLabel);
            userMessage.setAlignment(Pos.BOTTOM_RIGHT);

            // Adding the HBox of message into messageBox container
            messageBox.getChildren().add(userMessage);
            messageBox.setSpacing(10);

            try {
                // Call ChatGPT and give userInput as its parameter
                String chatGPTOutput = theModel.callChatGPT(userInput.getText());

                // Encapsulate the output of ChatGPT into label and add CSS
                Label chatGPTMessageLabel = new Label(chatGPTOutput);
                chatGPTMessageLabel.getStyleClass().add("chatgpt-message-label");

                // Aligning the message
                HBox chatGPTMessage = new HBox();
                chatGPTMessage.getChildren().add(chatGPTMessageLabel);
                chatGPTMessage.setAlignment(Pos.BOTTOM_LEFT);

                // Adding the HBox of message into messageBox container
                messageBox.getChildren().add(chatGPTMessageLabel);
                messageBox.setSpacing(10);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * scene graph that will create community scene to be displayed
     */
    public void initCommunitySceneGraph() {
        // Initializing communityScene
        this.communityScene = new VBox();

        // add the chatBotScene VBox to css
        communityScene.getStyleClass().add("community-scene");

        Label communityLabel = new Label("Development in progress...");
        communityLabel.getStyleClass().add("community-label");

        communityScene.getChildren().add(communityLabel);
    }

    /**
     * scene graph that will create library scene to be displayed
     */
    public void initLibrarySceneGraph() {
        // Initializing communityScene
        this.libraryScene = new VBox();

        // add the chatBotScene VBox to css
        libraryScene.getStyleClass().add("library-scene");

        Label libraryLabel = new Label("Development in progress...");
        libraryLabel.getStyleClass().add("library-label");

        libraryScene.getChildren().add(libraryLabel);
    }

    /**
     * scene graph that will create me scene to be displayed
     */
    public void initMeSceneGraph() {
        // Initializing communityScene
        this.meScene = new VBox();

        // add the meScene VBox to css
        meScene.getStyleClass().add("me-scene");

        Label meLabel = new Label("Development in progress...");
        meLabel.getStyleClass().add("me-label");

        meScene.getChildren().add(meLabel);
    }

    /**
     * bar chart that will read data from the txt file and plot it
     */
    private void initBarChart(){
        // Create the x-axis and y-axis
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create the Line chart and set the axis
        lineChart = new LineChart<>(xAxis, yAxis);

        // add lineChart to css
        lineChart.getStyleClass().add("mood-graph");

        // Set the title of the bar chart
        lineChart.setTitle("Daily Mood");

    }

    /**
     * create the scene for the habit trackers and the progress bar
     * Has a main VBox container with Hboxes inside
     */
    private void initHabitTrackerScene() {
        // Parent scene for the Habit Tracker Scene
        this.habitTrackerScene = new VBox();

        // add the habitTrackerscene VBox to css
        habitTrackerScene.getStyleClass().add("habit-tracker-scene");

        // create the habit tracker title box
        HBox habitTrackerTitleBox = new HBox();
        habitTrackerTitleBox.getStyleClass().add("habit-tracker-title");

        // Create the label for habit tracking
        habitTrackingLabel = new Label("Today's Habit Tracker");
        habitTrackingLabel.getStyleClass().add("habit-tracker-label");


        // add the label to the Hbox for the label
        habitTrackerTitleBox.getChildren().add(habitTrackingLabel);

        // HBox as container for list and pie chart
        this.listAndPieChart = new HBox();
        this.listAndPieChart.getStyleClass().add("list-chart");

        // Two VBoxes for habit list and habit pie chart
        this.habitList = new VBox();
        this.habitList.getStyleClass().add("habit-list");
        this.habitPieChart = new VBox();
        this.habitPieChart.getStyleClass().add("habit-pie-chart");

        //add the title HBox with the label to the main habit tracker scene
        habitTrackerScene.getChildren().add(habitTrackerTitleBox);

        // add the list and pie chart hbox to the main scene
        habitTrackerScene.getChildren().add(listAndPieChart);

        // add the list and piechart hbox to the css file
        listAndPieChart.getStyleClass().add("list-progress-box");

        FlowPane textFields = new FlowPane();

        // add the habits and chart to the hbox
        listAndPieChart.getChildren().add(habitList);
        listAndPieChart.getChildren().add(habitPieChart);

        createHabitButtons();

        createProgressBar();

    }

    /**
     * Refactored code to create the Habit buttons and add them to the scene
     */
    private void createHabitButtons() {

        // create a hbox that has a text field and a radio button five times
        habitContainer1 = new HBox();
        habitContainer1.setSpacing(10);
        habitList.getChildren().add(habitContainer1);
        this.didExercise = new RadioButton();
        habitContainer1.getChildren().add(this.didExercise);
        habit1 = new TextField("Exercise");
        habit1.getStyleClass().add("habit1-css");
        habit1.setTranslateY(-3);
        habit1.setTranslateX(-8);

        habitContainer1.getChildren().add(habit1);

        habitContainer2 = new HBox();
        habitContainer2.setSpacing(10);
        habitList.getChildren().add(habitContainer2);
        this.slept8Hours = new RadioButton();
        habitContainer2.getChildren().add(this.slept8Hours);
        habit2 = new TextField("Slept 7 hours");
        habit2.getStyleClass().add("habit2-css");
        habit2.setTranslateY(-3);
        habit2.setTranslateX(-8);

        habitContainer2.getChildren().add(habit2);

        habitContainer3 = new HBox();
        habitContainer3.setSpacing(10);
        habitList.getChildren().add(habitContainer3);
        this.meditated = new RadioButton();
        habitContainer3.getChildren().add(this.meditated);
        habit3 = new TextField("Meditate");
        habit3.getStyleClass().add("habit3-css");
        habit3.setTranslateY(-3);
        habit3.setTranslateX(-8);

        habitContainer3.getChildren().add(habit3);


        habitContainer4 = new HBox();
        habitContainer4.setSpacing(10);
        habitList.getChildren().add(habitContainer4);
        this.journaled = new RadioButton();
        habitContainer4.getChildren().add(this.journaled);
        habit4 = new TextField("Journaled");
        habit4.getStyleClass().add("habit4-css");
        habit4.setTranslateY(-3);
        habit4.setTranslateX(-8);

        habitContainer4.getChildren().add(habit4);

        habitContainer5 = new HBox();
        habitContainer5.setSpacing(10);
        habitList.getChildren().add(habitContainer5);
        this.relaxed = new RadioButton();
        habitContainer5.getChildren().add(this.relaxed);
        habit5 = new TextField("Say 'Thank You' 5 times");
        habit5.getStyleClass().add("habit5-css");
        habit5.setTranslateY(-3);
        habit5.setTranslateX(-8);

        habitContainer5.getChildren().add(habit5);

        // create a arraylist for habit buttons and add them
        habitButtons = new ArrayList<>();
        habitButtons.add(this.didExercise);
        habitButtons.add(this.slept8Hours);
        habitButtons.add(this.meditated);
        habitButtons.add(this.journaled);
        habitButtons.add(this.relaxed);
    }

    /**
     * Refactored code to create a progress bar
     */
    private void createProgressBar() {
        // create a new progress bar
        habitProgress = new ProgressBar(0);

        // add the progress bar to css file
        habitProgress.getStyleClass().add("progress-bar");

        // create a label for the progress bar
        progresslbl = new Label();
        progresslbl.getStyleClass().add("progress-lbl");

        // add the labels and progress bar to the habitPieChart VBox
        habitPieChart.getChildren().add(habitProgress);
        habitPieChart.getChildren().add(progresslbl);
    }

    /**
     * Set the scene for the mood question. Add the images to the buttons
     * and also set the id for each button
     */
    private void initMoodQuestionScene() {
        moodQuestionScene = new VBox();
        moodQuestionScene.getStyleClass().add("mood-question-scene");

        // instantiate the HBoxes for the moods and the text
        hMoods = new HBox();
        hMoodsQuestionBox = new HBox();
        moodLabels = new HBox();

        // create the question label
        moodQuestionLabel = new Label("How are you feeling today?");

        // add the label and HBoxes to the CSS file
        moodQuestionLabel.getStyleClass().add("hmoods-question");
        hMoodsQuestionBox.getStyleClass().add("hmoods-question-box");
        moodLabels.getStyleClass().add("hmoods-labels-hbox");
        hMoods.getStyleClass().add("hmoods-box");

        // add the Hbox label and Hbox moods to the moodQuestionScene
        this.moodQuestionScene.getChildren().add(hMoodsQuestionBox);
        this.moodQuestionScene.getChildren().add(hMoods);
        this.moodQuestionScene.getChildren().add(moodLabels);

        // add the label to the Hbox for the label
        hMoodsQuestionBox.getChildren().add(moodQuestionLabel);

        // Calling method to create buttons for mood
        createButtonsForMood();

    }

    /**
     * Method that creates buttons with images of mood.
     * Due to limitations for styling, this had to all be done here
     */
    private void createButtonsForMood() {
        // Loading images as stream
        InputStream veryBadStream = AppMain.class.getClassLoader().getResourceAsStream("veryBad.png");
        InputStream poorStream = AppMain.class.getClassLoader().getResourceAsStream("poor.png");
        InputStream mediumStream = AppMain.class.getClassLoader().getResourceAsStream("medium.png");
        InputStream goodStream = AppMain.class.getClassLoader().getResourceAsStream("good.png");
        InputStream excellentStream = AppMain.class.getClassLoader().getResourceAsStream("excellent.png");

        // load images
        Image veryBadImg = new Image(veryBadStream);
        Image poorImg = new Image(poorStream);
        Image mediumImg = new Image(mediumStream);
        Image goodImg = new Image(goodStream);
        Image excellentImg = new Image(excellentStream);

        // Displaying images
        ImageView veryBadView = new ImageView(veryBadImg);
        ImageView poorView = new ImageView(poorImg);
        ImageView mediumView = new ImageView(mediumImg);
        ImageView goodView = new ImageView(goodImg);
        ImageView excellentView = new ImageView(excellentImg);

        // set the ratio for the images
        veryBadView.setPreserveRatio(true);
        poorView.setPreserveRatio(true);
        mediumView.setPreserveRatio(true);
        goodView.setPreserveRatio(true);
        excellentView.setPreserveRatio(true);

        // Setting up the buttons for the moods
        Button veryBad = new Button();
        Button poor = new Button();
        Button medium = new Button();
        Button good = new Button();
        Button excellent = new Button();

        // set the id for the buttons 0 is lowest 4 is highest
        veryBad.setId("0");
        poor.setId("1");
        medium.setId("2");
        good.setId("3");
        excellent.setId("4");

        // create an arraylist for the buttons and add them
        buttonsArray = new ArrayList<>();
        buttonsArray.add(veryBad);
        buttonsArray.add(poor);
        buttonsArray.add(medium);
        buttonsArray.add(good);
        buttonsArray.add(excellent);

        //Setting the size of the button
        veryBad.setPrefSize(56.8, 74);
        poor.setPrefSize(56.8, 74);
        medium.setPrefSize(56.8, 74);
        good.setPrefSize(56.8, 74);
        excellent.setPrefSize(56.8, 74);

        // Setting the graphic to the button
        veryBad.setGraphic(veryBadView);
        poor.setGraphic(poorView);
        medium.setGraphic(mediumView);
        good.setGraphic(goodView);
        excellent.setGraphic(excellentView);

        // add the buttons to the hbox of moods
        hMoods.getChildren().addAll(veryBad, poor, medium, good, excellent);
    }

    /**
     * navigation bar to switch between scenes
     */
    public void initNavBar() {

        // create the HBox for nav bar
        navBarBox = new HBox();
        navBarBox.getStyleClass().add("nav-bar-box");

        // create 5 buttons for the navigation bar
        chatBotButton = new Button();
        communityButton = new Button();
        homeButton = new Button();
        libraryButton = new Button();
        meButton = new Button();

        // Loading images as stream
        InputStream chatBotStream = AppMain.class.getClassLoader().getResourceAsStream("chatBot.png");
        InputStream communityStream = AppMain.class.getClassLoader().getResourceAsStream("community.png");
        InputStream homeStream = AppMain.class.getClassLoader().getResourceAsStream("home.png");
        InputStream libraryStream = AppMain.class.getClassLoader().getResourceAsStream("library.png");
        InputStream meStream = AppMain.class.getClassLoader().getResourceAsStream("me.png");

        // loading images
        Image chatBotImg = new Image(chatBotStream);
        Image communityImg = new Image(communityStream);
        Image homeImg = new Image(homeStream);
        Image libraryImg = new Image(libraryStream);
        Image meImg = new Image(meStream);

        // Displaying images
        ImageView chatBotView = new ImageView(chatBotImg);
        ImageView communityView = new ImageView(communityImg);
        ImageView homeView = new ImageView(homeImg);
        ImageView libraryView = new ImageView(libraryImg);
        ImageView meView = new ImageView(meImg);

        // set the ratio for the images
        chatBotView.setPreserveRatio(true);
        communityView.setPreserveRatio(true);
        homeView.setPreserveRatio(true);
        libraryView.setPreserveRatio(true);
        meView.setPreserveRatio(true);

        //Setting the size of the button
        chatBotButton.setPrefSize(56.8, 74);
        communityButton.setPrefSize(56.8, 74);
        homeButton.setPrefSize(56.8, 74);
        libraryButton.setPrefSize(56.8, 74);
        meButton.setPrefSize(56.8, 74);

        // Setting the graphic to the button
        chatBotButton.setGraphic(chatBotView);
        communityButton.setGraphic(communityView);
        homeButton.setGraphic(homeView);
        libraryButton.setGraphic(libraryView);
        meButton.setGraphic(meView);

        // add the buttons to the hbox of chatBotBox amd homeBox
        navBarBox.getChildren().add(chatBotButton);
        navBarBox.getChildren().add(communityButton);
        navBarBox.getChildren().add(homeButton);
        navBarBox.getChildren().add(libraryButton);
        navBarBox.getChildren().add(meButton);
    }

    /**
     * Getter method that will return the rootScene
     * @return {@Link #rootScene}
     */
    public VBox getRootScene() {
        return rootScene;
    }

    /**
     * Getter method that will return the homeScene
     * @return {@Link #homeScene}
     */
    public VBox getHomeScene() {
        return homeScene;
    }

    /**
     * Getter method for chatBotScene
     * @return {@Link #chatBotScene}
     */
    public VBox getChatBotScene() {
        return chatBotScene;
    }

    /**
     * Getter method for the NavBarScene
     * @return VBox of the NavBarScene
     */
    public VBox getNavBarScene() {
        return navBarScene;
    }

    /**
     * Getter method to return the Progresslbl
     * @return Label of Progresslbl
     */
    public Label getProgresslbl(){
        return progresslbl;
    }

    /**
     * Getter method for the ChatBotButton
     * @return Button of ChatBotButton
     */
    public Button getChatBotButton() {
        return chatBotButton;
    }

    /**
     * Getter method for the CommunityButton
     * @return Button for CommunityButton
     */
    public Button getCommunityButton() {
        return communityButton;
    }

    /**
     * Getter method for the HomeButton
     * @return Button for HomeButton
     */
    public Button getHomeButton() {
        return homeButton;
    }

    /**
     * Getter method for the LibraryButton
     * @return Button for the LibraryButton
     */
    public Button getLibraryButton() {
        return libraryButton;
    }

    /**
     * Getter Method for the meButton
     * @return Button for the MeButton
     */
    public Button getMeButton() {
        return meButton;
    }

    /**
     * getter method to get access to all the buttons
     * @return {@link #buttonsArray}
     */
    public ArrayList<Button> getButtonList(){
        return buttonsArray;
    }

    /**
     * getter method for the progress bar
     * @return {@link #habitProgress}
     */
    public ProgressBar getHabitProgress(){
        return habitProgress;
    }

    /**
     * getter method for radio buttons list
     * @return {@link ArrayList<RadioButton>}
     */
    public ArrayList<RadioButton> getHabitButtons(){
        return habitButtons;
    }

    /**
     * Getter method for the edit button
     * @return Button EditButton
     */
    public Button getEditButton(){
        return editButton;
    }

    /**
     * Getter method for bar chart
     * @return LineChart representing barChart
     */
    public LineChart<String, Number> getBarChart(){
        return lineChart;
    }

    /**
     * getter method that returns the community scene
     * @return VBoc of communityScene
     */
    public VBox getCommunityScene() {
        return communityScene;
    }

    /**
     * getter method that gets the library scene
     * @return VBox of the LibraryScene
     */
    public VBox getLibraryScene() {
        return libraryScene;
    }

    /**
     * getter method that returns the me scene
     * @return VBox for the MeScene
     */
    public VBox getMeScene() {
        return meScene;
    }
}

