package com.consultsim.mallsim.View;

import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Store;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class UIHandler implements Initializable {

    @FXML
    private Slider sliderDayTime;
    @FXML
    private Label labelDayTime;
    @FXML
    private Slider sliderNumberOfPersons;
    @FXML
    private Label labelNumberOfPersons;
    @FXML
    private GraphicsContext graphicsContext;
    @FXML
    private Canvas canvas;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCanvas();


        initializeSliderDayTime();
        initializeSliderNumberOfPersons();
    }

    private void initializeSliderNumberOfPersons() {
        sliderNumberOfPersons.setMajorTickUnit(10);
        sliderNumberOfPersons.setShowTickLabels(true);
        labelNumberOfPersons.textProperty()
                .bindBidirectional(sliderNumberOfPersons.valueProperty(),NumberFormat.getNumberInstance());
        sliderNumberOfPersons.valueProperty()
                .addListener((observable, oldValue, newValue) ->
                        sliderNumberOfPersons.setValue(newValue.intValue()));

    }


    private void initializeSliderDayTime() {

        sliderDayTime.setMajorTickUnit(4500);
        sliderDayTime.setShowTickLabels(true);
        StringConverter<Double> stringConverter = new StringConverter<Double>() {

            /*
            * convert int to hours format hh:mm
            * */
            @Override
            public String toString(Double object) {
                long seconds = object.longValue();
                long minutes = TimeUnit.SECONDS.toMinutes(seconds);
                long hour = TimeUnit.MINUTES.toHours(minutes);
                long remainingMinutes = minutes - TimeUnit.HOURS.toMinutes(hour);
                return String.format("%02d", hour) + ":" + String.format("%02d", remainingMinutes);
            }
            @Override
            public Double fromString(String string) {
                return null;
            }
        };
        labelDayTime.setText(stringConverter.toString(sliderDayTime.getMin()));
        sliderDayTime.setLabelFormatter(stringConverter);
        sliderDayTime.valueProperty().addListener((observable, oldValue, newValue) ->
                labelDayTime.setText(stringConverter.toString(newValue.doubleValue())));

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


        for (Store s : fileHandler.getArrayOfStores()) {
            drawStores(graphicsContext, s);
            System.out.println(s.getId());
        }

        for (Objects o : fileHandler.getArrarOfObjects()) {
            drawOblects(graphicsContext, o);
            System.out.println("Object " + o.getId());
        }
        fileHandler.getArrayOfStores();
    }

    private void drawStores(GraphicsContext gc, Store store) {
        gc.setFill(Color.BLUE);
        gc.fillRect(store.getPosition()[0], store.getPosition()[1],
                store.getPosition()[2] - store.getPosition()[0],
                store.getPosition()[3] - store.getPosition()[1]);
    }

    private void drawOblects(GraphicsContext gc, Objects store) {
        gc.setFill(Color.RED);
        gc.fillRect(store.getPosition()[0], store.getPosition()[1],
                store.getPosition()[2] - store.getPosition()[0],
                store.getPosition()[3] - store.getPosition()[1]);
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
