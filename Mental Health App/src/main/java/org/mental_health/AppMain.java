/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Spring 2023
 *
 * Author: Prof. King
 *
 * Name: Alexander Pisarchik Shketav
 * Date: 04/05/2023
 * Time: 9:30 AM

 * Project: csci205_final_project
 * Class: AppMain
 *
 * Description:
 * Main class for the mental health app for the MVC design
 * ****************************************
 */

package org.mental_health;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class that initializes all classes for our app
 */
public class AppMain extends Application {

    /**
     * private fields containing the required MVC components
     */
    private AppModel theModel;
    private AppView theView;
    private AppController theController;

    /**
     * Instance variable for stages
     */
    private Stage stage;

    /**
     * The applications initialization method. Method is called immediately
     * after the application class is loaded and constructed, but before the
     * start() method is invoked
     */
    @Override
    public void init() throws Exception {
        super.init();

        //instantiate the fields
        this.theModel = new AppModel();
        this.theView = new AppView(theModel);
        this.theController = new AppController(theModel, theView);
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     */
    @Override
    public void start(Stage primaryStage) {
        //set the main scene and stage
        stage = primaryStage;
        Scene rootScene = new Scene(this.theView.getRootScene());

        //import the CSS file resources
        rootScene.getStylesheets().add(getClass()
                .getResource("/final_project/final_project.css")
                .toExternalForm());

        //format, add the root scene, stylize, and show it
        stage.setTitle("Genuine - Mental Health App for Students");
        stage.setScene(rootScene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Standard main program for a JavaFX application
     *
     * @param args String[] args
     */
    public static void main(String[] args) {
        launch(args);
    }
}