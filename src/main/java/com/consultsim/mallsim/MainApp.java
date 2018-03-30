package com.consultsim.mallsim;

import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.View.SimulationHandler;
import com.consultsim.mallsim.View.StatisticHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane mainLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SimulationApp");
        initMainLayout();

    }


    private void initMainLayout() {
        try{
            //Load the main layout from the fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("View/MainTemplate.fxml"));
            mainLayout = (BorderPane)loader.load();

            //Display the scene containing the main layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(600);
            primaryStage.setMinWidth(850);
            primaryStage.setOnCloseRequest(we -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("");
                alert.setTitle("Exit");
                alert.setContentText("Are you sure you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    primaryStage.close();
                } else {
                    we.consume();
                }
            });
            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void testHotColdSpots(){
        ArrayList<Person> arrayOfPerson;
        arrayOfPerson = new ArrayList<Person>();
        Random random = new Random();
        int x;
        int y;
        for (int i = 0; i < 3000; i++) {
            x = random.nextInt(1000) + 1;
            y = random.nextInt(1000) + 1;

            arrayOfPerson.add(new Person(new Position(x,y), 0.0));
        }

       new StatisticHandler().recognizeHCSpots(1000,1000, 100, 100, arrayOfPerson);
    }



    public static void main(String[] args){
        testHotColdSpots();
        launch(args);
    }


}
