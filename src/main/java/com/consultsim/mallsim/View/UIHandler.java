package com.consultsim.mallsim.View;

import com.consultsim.mallsim.MainApp;
import com.consultsim.mallsim.Model.Configuration;
import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.StaticObjects.Spot;
import com.consultsim.mallsim.Model.Store;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class UIHandler implements Initializable {

    private int speedOfSim;

    @FXML
    public Button btnNextStep;
    @FXML
    public Slider sliderSpeedOfSim;
    @FXML
    public Label lblSpeedValue;
    @FXML
    public Button btnStartPause;
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

    private Timeline SimulationLoop;
    public ArrayList<Person> arrayOfPerson;
    public ArrayList<Spot> arrayOfSpots;
    public static StatisticHandler stat;
    public SimulationHandler simulationHandler;
    private ArrayList<Store> arrayOfStores;
    private ArrayList<Objects> arrayOfObjects;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeCanvas();
        speedOfSim = Configuration.INITIAL_SPEED;
        btnStartPause.setText("Start");
        lblSpeedValue.setText(String.format("%d", this.speedOfSim));
        sliderSpeedOfSim.setValue(this.speedOfSim);

        initializeSliderDayTime();
        initializeSliderNumberOfPersons();
        initializeSliderSpeedOfSim();
        initializeSimHandler();

    }

    private void initializeSimHandler() {
        simulationHandler = new SimulationHandler();
        arrayOfPerson = new ArrayList<Person>();
        arrayOfSpots = new ArrayList<Spot>();

    }

    /**
     * If Button LoadFromFile was pressed
     *
     * @param actionEvent Load from File was pressed
     */
    public void loadLayoutFromFile(ActionEvent actionEvent) {
        btnNextStep.setDisable(false);
        btnStartPause.setDisable(false);
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

        //load from file in root directory
        fileHandler.readFile("InputMallSim.xml");
        arrayOfStores = fileHandler.getArrayOfStores();
        arrayOfObjects = fileHandler.getArrarOfObjects();

        drawLayoutFromXMLFile();
        drawCanvasSim();

    }

    private void drawCanvasSim() {
        buildSimulationStart(this.speedOfSim);
        initializePerson();
    }

    /**
     * Initializes the simulation loop
     *
     * @param speedOfSim fps
     */
    private void buildSimulationStart(int speedOfSim) {
        Duration duration = Duration.millis(1000 / (float) speedOfSim);
        KeyFrame frame = changeToNextFrame(duration);
        SimulationLoop = new Timeline();
        SimulationLoop.setCycleCount(Timeline.INDEFINITE);
        SimulationLoop.getKeyFrames().add(frame);
    }

    /**
     * Here are initialized persons
     */
    private void initializePerson() {
        simulationHandler.initializePersons();
        arrayOfPerson = simulationHandler.getArrayOfPersons();
        arrayOfSpots = simulationHandler.stat.getHotColdSpots();
    }


    @FXML
    public void pauseStartSim(ActionEvent event) {
        switch (SimulationLoop.getStatus()) {
            case RUNNING:
                SimulationLoop.pause();
                btnStartPause.setText("Start");
                btnNextStep.setDisable(false);
                break;
            case PAUSED:
            case STOPPED:
                SimulationLoop.play();
                btnStartPause.setText("Pause");
                btnNextStep.setDisable(true);
                break;
        }
    }

    /**
     * Event handling for slider  'Geschwindigkeit'
     */
    private void initializeSliderSpeedOfSim() {
        sliderSpeedOfSim.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderSpeedOfSim.setValue(newValue.intValue());
            lblSpeedValue.setText(String.format("%d", newValue.intValue()));
            speedOfSim = (int) sliderSpeedOfSim.getValue();
            changeFrame(speedOfSim);
        });
    }

    private void changeFrame(int speedOfSim) {
        Animation.Status status = SimulationLoop.getStatus();
        SimulationLoop.stop();
        SimulationLoop.getKeyFrames().clear();
        buildSimulationStart(speedOfSim);
        if (status == Animation.Status.RUNNING) {
            SimulationLoop.play();
        }
        //TODO: to implement the function for automatically changing a speed of simulation if the slider was used
    }


    /**
     * Event handling for slider  'Person Anzahl'
     */
    private void initializeSliderNumberOfPersons() {
        sliderNumberOfPersons.setMajorTickUnit(10);
        sliderNumberOfPersons.setShowTickLabels(true);
        labelNumberOfPersons.textProperty()
                .bindBidirectional(sliderNumberOfPersons.valueProperty(), NumberFormat.getNumberInstance());
        sliderNumberOfPersons.valueProperty()
                .addListener((observable, oldValue, newValue) ->
                        sliderNumberOfPersons.setValue(newValue.intValue()));

    }

    /**
     * Event handling for slider  'Tageszeit'
     */
    private void initializeSliderDayTime() {

        sliderDayTime.setMajorTickUnit(10000);
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


    /**
     * Draws stores and other objects on canvas
     */
    private void drawLayoutFromXMLFile() {
        drawStores(graphicsContext, arrayOfStores);
        drawObjects(graphicsContext, arrayOfObjects);
    }

    /**
     * @param gc            GraphicsContext
     * @param arrayOfStores array of stores from XML temmplate
     */
    private void drawStores(GraphicsContext gc, ArrayList<Store> arrayOfStores) {
        for (Store store : arrayOfStores) {
            gc.setFill(Color.DARKMAGENTA);
            gc.fillRect(store.getPosition()[0], store.getPosition()[1],
                    store.getPosition()[2] - store.getPosition()[0],
                    store.getPosition()[3] - store.getPosition()[1]);
            //System.out.println(store.getId());
        }
    }

    /**
     * @param gc             GraphicsContext
     * @param arrayOfObjects array of objects from XML temmplate
     */
    private void drawObjects(GraphicsContext gc, ArrayList<Objects> arrayOfObjects) {
        for (Objects obj : arrayOfObjects) {
            gc.setFill(Color.RED);
            gc.fillRect(obj.getPosition()[0], obj.getPosition()[1],
                    obj.getPosition()[2] - obj.getPosition()[0],
                    obj.getPosition()[3] - obj.getPosition()[1]);
            //System.out.println("Object " + obj.getId());
        }

    }

    private void drawHotColdSpots(GraphicsContext gc, Spot spot) {

        if (spot.getSemaphor() == 1) {
            gc.setFill(Color.rgb(255, 64, 64, 0.5));
        } else if (spot.getSemaphor() == 2) {
            gc.setFill(Color.rgb(0, 0, 139, 0.5));
        }
        //gc.fillRect(0,0, 50,50);
        gc.fillRect(spot.getX(), spot.getY(), spot.getWidth(), spot.getHeigth());
    }

    private void drawPersons(GraphicsContext gc, Person p) {
        gc.setFill(Color.BLACK);
        gc.fillOval(p.getCurrentPosition().getX(), 1000 - p.getCurrentPosition().getY(), 5, 5);

    }

    private void drawStuff(GraphicsContext gc) {

        gc.setFill(Color.YELLOW);
        gc.fillOval(0, 0, 40, 40);

    }


    private void initializeCanvas() {
        canvas.setHeight(1000);
        canvas.setWidth(1000);
        graphicsContext = canvas.getGraphicsContext2D();
        canvas.setLayoutY(-20);
        canvas.setLayoutX(-10);
        canvas.setScaleX(1);
        canvas.setScaleY(-1);

    }

    /**
     * called when the button 'Next Step' is pressed
     *
     * @param event
     */

    public void startSimulation(ActionEvent event) {

        simulationHandler.initializePersons();
        Duration duration = Duration.millis(80);
        KeyFrame frame = changeToNextFrame(duration);
        Timeline loopOfSim = new Timeline();
        loopOfSim.setCycleCount(Timeline.INDEFINITE);
        loopOfSim.getKeyFrames().add(frame);
        loopOfSim.play();
    }


    public void getNextStep(ActionEvent event) {

        Duration duration = Duration.millis(1000 / (float) speedOfSim);
        KeyFrame frame = changeToNextFrame(duration);
        SimulationLoop = new Timeline();
        SimulationLoop.setCycleCount(1);
        SimulationLoop.getKeyFrames().add(frame);
        SimulationLoop.play();
    }

    /**
     * additional function that initializes all the changes that are necessary for changing a single frame
     *
     * @param duration duration of time
     * @return new {@link KeyFrame}
     */
    private KeyFrame changeToNextFrame(Duration duration) {
        return new KeyFrame(duration, event -> {
            clearCanvas(graphicsContext);
            simulationHandler.clearEverything();
            drawLayoutFromXMLFile();
            //drawStores(graphicsContext, arrayOfStores);
            //drawObjects(graphicsContext, arrayOfObjects);

            simulationHandler.computeNextPositionOfPersons();
            arrayOfPerson = simulationHandler.getArrayOfPersons();
            arrayOfSpots = simulationHandler.stat.getHotColdSpots();

            for (Person p : arrayOfPerson) {
                drawPersons(graphicsContext, p);
            }

            for (Spot s : arrayOfSpots) {
                drawHotColdSpots(graphicsContext, s);
                //System.out.println("H/C " + s.getX() + " " + s.getY() + " " + s.getWidth() + " " + s.getHeigth());
            }

            //TODO add all events that change with each frame

        });
    }

    /**
     * Clears the content
     *
     * @param gc is the {@link GraphicsContext} that needs to be cleared
     */
    private void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }


    /**
     * to reset and new start of simulation
     *
     * @param event
     */
    public void resetSim(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(MainApp.class.getResource("View/MainTemplate.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnStartPause.getScene().setRoot(root);
    }

}
