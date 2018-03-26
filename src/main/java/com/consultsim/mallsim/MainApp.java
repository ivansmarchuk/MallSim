package com.consultsim.mallsim;

import com.consultsim.mallsim.View.SimulationHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

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

    public static void main(String[] args){

        launch(args);
    }


}
