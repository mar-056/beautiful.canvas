

Ahmad Rehman - Developer
junior computer engineering major

## Project Summary

The project is a mental health app. It is meant to allow users to track 
their overall mental well-being as well as taking quick note of healthy 
daily activities that they have done. Users can also talk to an AI 
chatbot to ask any question they want.

The main purpose of this app is to help people improve their mental health
and to provide an intuitive way to push themselves in the right direction.

There are currently two available tabs in the app. The home screen is the 
main tab, and the one the user starts on. It contains all the personal 
information that is currently tracked. Users can select their overall mood 
for the day so that it can later be displayed as a trend in the chart below. 
There are also five checkboxes that show different daily activities that 
are recommended to help maintain wellbeing. Users can check them off to see 
if there is anything more that they can do.

The second currently available tab is a chatbot that one can use to ask 
questions if one feels lost or needs ideas. All the other tabs are currently 
unavailbale as they have not been developed yet.

## Package Structure

The project is structured using the MVC design pattern.
MVC stands for "Model, View, Design"
The View represents what the user sees. This class builds the GUI 
and helps stylize it (ie alight things to the center or place 
things in a certain order)
The Model represents the logic of the program. This is where user input 
is processed and results are returned. Data is recorded through this class 
as well.
The Controller is what links the view to the model (ie the GUI to the logic). 
This class is what takes the input from what is happening in the GUI, such as
clicked buttons and typed in input, and sends it to the model to be processed.
It also binds different events such as button clicks to separate events in
the GUI, such as clicking a button in order to go to a different page/part of 
the app.
The Main method is small compared to the other classes but is what brings 
them all together. It is the JavaFX application class unlike the others 
which are basic Java classes. It contains instances of each of the 
previous three classes and is what binds them to each other and actually
initializes the GUI.
Other than these four most important classes, the resources folder contains 
images and text files that the program uses build the GUI.

## 3rd Party Libraries

JavaFX --- Version 17 --- https://openjfx.io/

## Video Presentation URL
https://youtu.be/NReBDelxqV8 