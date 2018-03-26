package com.consultsim.mallsim.View;

import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Store;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UIHandler  implements Initializable{

    private GraphicsContext graphicsContext;

    @FXML
    public Canvas canvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCanvas();

    }

    public static ArrayList<Rectangle> rectangles;
    private Stage primaryStage;

    public void loadLayoutFromFile(ActionEvent actionEvent) {

        graphicsContext = canvas.getGraphicsContext2D();

        FileHandler fileHandler = new FileHandler();

        /**
         * for load from File
         * */
        /*
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        fileHandler.readFile(file.getAbsolutePath());
        */

        /*
        * load from file in root directory
        * */
        fileHandler.readFile("InputMallSim.xml");



        for(Store s: fileHandler.getArrayOfStores()){
           drawStores(graphicsContext, s);
            System.out.println(s.getId());
        }

        for(Objects o: fileHandler.getArrarOfObjects()){
            drawOblects(graphicsContext, o);
            System.out.println("Object " + o.getId());
        }
        fileHandler.getArrayOfStores();
    }

    private void drawStores(GraphicsContext gc, Store store) {
        gc.setFill(Color.BLUE);
        gc.fillRect(store.getPosition()[0], store.getPosition()[1],
                store.getPosition()[2]-store.getPosition()[0],
                store.getPosition()[3]-store.getPosition()[1]);
    }

    private void drawOblects(GraphicsContext gc, Objects store) {
        gc.setFill(Color.RED);
        gc.fillRect(store.getPosition()[0], store.getPosition()[1],
                store.getPosition()[2]-store.getPosition()[0],
                store.getPosition()[3]-store.getPosition()[1]);
    }

    private void initializeCanvas() {
        canvas.setHeight(500);
        canvas.setWidth(500);
        graphicsContext = canvas.getGraphicsContext2D();
        canvas.setLayoutY(-50);
        canvas.setLayoutX(-10);
        canvas.setScaleX(1);
        canvas.setScaleY(-1);

    }
}
