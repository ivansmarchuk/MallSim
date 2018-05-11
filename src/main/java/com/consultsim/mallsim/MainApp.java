package com.consultsim.mallsim;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class MainApp extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MallSim");
        initMainLayout();
    }

   public void initMainLayout() {
        try {
            //Load the main layout from the fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("MainTemplate.fxml"));
            BorderPane mainLayout = loader.load();

            //Display the scene containing the main layout.
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(600);
            primaryStage.setMinWidth(850);
            primaryStage.getIcons().add(new Image(String.valueOf(getClass().getClassLoader().getResource("images/icon.png"))));
            primaryStage.setOnCloseRequest((WindowEvent we) -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("");
                alert.setTitle("Exit");
                alert.setContentText("Are you sure you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();
                //noinspection ConstantConditions
                if (result.get() == ButtonType.OK) {
                    primaryStage.close();
                } else {
                    we.consume();
                }
            });
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
