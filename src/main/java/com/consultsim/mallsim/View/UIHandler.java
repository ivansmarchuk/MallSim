package com.consultsim.mallsim.View;

import com.consultsim.mallsim.MainApp;
import com.consultsim.mallsim.Model.Configuration;
import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.Position;
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
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.StringConverter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class UIHandler implements Initializable {


    private int speedOfSim;
    private int speedDayOfSim;
    @FXML
    public Label lblCountPerson;
    @FXML
    public Slider sliderSpeedDayOfSim;
    @FXML
    public Button btnNextStep;
    @FXML
    public Slider sliderSpeedOfSim;
    @FXML
    public Label lblSpeedDayValue;
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

    private Timeline simulationLoop;
    private ArrayList<Person> arrayOfPerson;
    private ArrayList<Spot> arrayOfSpots;
    private SimulationHandler simulationHandler;
    private ArrayList<Store> arrayOfStores;
    private ArrayList<Objects> arrayOfObjects;
    private double dayHours = 540;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeCanvas();
        speedOfSim = Configuration.INITIAL_SPEED;
        speedDayOfSim = Configuration.INITIAL_DAY_SPEED;
        lblSpeedDayValue.setText(String.format("%d", this.speedDayOfSim));
        sliderSpeedOfSim.setValue(this.speedDayOfSim);
        btnStartPause.setText("Start");
        lblSpeedValue.setText(String.format("%d", this.speedOfSim));
        sliderSpeedOfSim.setValue(this.speedOfSim);

        initializeSliderDayTime();
        initializeSliderNumberOfPersons();
        initializeSliderSpeedOfSim();
        initializeSliderSpeedDayOfSim();
        //initializeSimHandler();

    }

    private void initializeSimHandler() {
        //simulationHandler = new SimulationHandler();
        arrayOfPerson = new ArrayList<>();
        arrayOfSpots = new ArrayList<>();
        // Here are initialized persons
        simulationHandler.initializePersons();
        arrayOfPerson = simulationHandler.getArrayOfPersons();
        lblCountPerson.setText(Integer.toString(arrayOfPerson.size()));
        arrayOfSpots = simulationHandler.stat.getHotColdSpots();

    }

    /**
     * If Button LoadFromFile was pressed
     *
     * @param actionEvent Load from File was pressed
     */
    public void loadLayoutFromFile(ActionEvent actionEvent) {
        simulationHandler = new SimulationHandler();


        //for load from File
        //final FileChooser fileChooser = new FileChooser();
        // File file = fileChooser.showOpenDialog(primaryStage);
        //fileHandler.readFile(file.getAbsolutePath());

        //load from file in root directory
        try {
            btnStartPause.setDisable(false);
            btnNextStep.setDisable(false);
            graphicsContext = canvas.getGraphicsContext2D();

            FileHandler fileHandler = new FileHandler();

            fileHandler.readFile("InputMallSim.xml");
            arrayOfStores = fileHandler.getArrayOfStores();
            arrayOfObjects = fileHandler.getArrarOfObjects();
            simulationHandler.setArrayOfObjects(arrayOfObjects);
            simulationHandler.setArrayOfStores(arrayOfStores);
            initializeSimHandler();

            drawLayoutFromXMLFile();
            buildSimulationStart(this.speedOfSim);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void computeNextPositionOfPersons() {

        //generatePerson(sliderNumberOfPersons.getValue(), sliderDayTime.getValue());
        simulationHandler.computeNextPositionOfPersons();
        arrayOfPerson = simulationHandler.getArrayOfPersons();
        arrayOfSpots = simulationHandler.stat.getHotColdSpots();
    }

    /**
     * Initializes the simulation loop
     *
     * @param speedOfSim fps
     */
    private void buildSimulationStart(int speedOfSim) {
        Duration duration = Duration.millis(1000 / (float) speedOfSim);
        KeyFrame frame = getToNextFrame(duration);
        simulationLoop = new Timeline();
        simulationLoop.setCycleCount(Timeline.INDEFINITE);
        simulationLoop.getKeyFrames().add(frame);
    }

    /**
     * additional function that initializes all the changes that are necessary for changing a single frame
     *
     * @param duration duration of time
     * @return new {@link KeyFrame}
     */
    private KeyFrame getToNextFrame(Duration duration) {
        return new KeyFrame(duration, event -> {
            simulationHandler.clearEverything();
            clearCanvas(graphicsContext);
            drawLayoutFromXMLFile();
            drawPersons(graphicsContext, arrayOfPerson);
            drawHotColdSpots(graphicsContext, arrayOfSpots);

            double dayTime = sliderDayTime.getValue();
            double newDayTime = dayTime + duration.toSeconds() * sliderSpeedDayOfSim.getValue();
            sliderDayTime.setValue(newDayTime);
            if (sliderDayTime.getValue() != sliderDayTime.getMax()) {
                lblCountPerson.setText(Integer.toString(arrayOfPerson.size()));
                generatePerson(sliderNumberOfPersons.getValue(), sliderDayTime.getValue());
                computeNextPositionOfPersons();
            } else {
                simulationLoop.stop();
            }
        });
    }

    /**
     * called when the button 'Start' is pressed
     *
     * @param event press on start button
     */
    @FXML
    public void pauseStartSim(ActionEvent event) {
        switch (simulationLoop.getStatus()) {
            case RUNNING:
                simulationLoop.pause();
                btnStartPause.setText("Start");
                btnNextStep.setDisable(false);
                break;
            case PAUSED:
            case STOPPED:
                simulationLoop.play();
                btnStartPause.setText("Pause");
                btnNextStep.setDisable(true);
                break;
        }
    }

    private void generatePerson(double numberOfPerson, double dayTime) {
        int maxX = 500;
        int minX = 480;
        int maxY = 70;
        int minY = 40;
        Random rand = new Random();
        //System.out.println("DayTime: " + Math.round(dayTime)/60);
        if (Math.round(dayTime) / 60 - dayHours > 10) {
            for (int i = 0; i < (int) numberOfPerson; i++) {
                int x = rand.nextInt((maxX - minX) + 1) + minX;
                int y = rand.nextInt((maxY - minY) + 1) + minY;

                arrayOfPerson.add(new Person(new Position(x, y), 10, simulationHandler));

            }
            dayHours = Math.round(dayTime) / 60;
        }

    }

    /**
     * called when the button 'Next Step' is pressed
     *
     * @param event press on nex next step button
     */
    @FXML
    public void getNextStep(ActionEvent event) {
        Duration duration = Duration.millis(1000 / (float) speedOfSim);
        KeyFrame frame = getToNextFrame(duration);
        simulationLoop = new Timeline();
        simulationLoop.setCycleCount(1);
        simulationLoop.getKeyFrames().add(frame);
        simulationLoop.play();
        simulationLoop.setCycleCount(Timeline.INDEFINITE);
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
            //convert int to hours format hh:mm
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
        gc.setStroke(Color.BLACK);
        for (Store store : arrayOfStores) {
            gc.setFill(store.getColor());
            gc.fillRect(store.getPosition()[0], store.getPosition()[1],
                    store.getPosition()[2] - store.getPosition()[0],
                    store.getPosition()[3] - store.getPosition()[1]);
            gc.save();
            gc.setFill(Color.BLACK);
            gc.fillText(store.getLabel(), store.getPosition()[0] + 5,
                    store.getPosition()[1] + (store.getPosition()[3] - store.getPosition()[1])/2);
            gc.restore();
            gc.strokeLine(store.getDoorPosition()[0], store.getDoorPosition()[1], store.getDoorPosition()[2], store.getDoorPosition()[3]);
            //System.out.println(store.getId());
        }
    }

    /**
     * @param gc             GraphicsContext
     * @param arrayOfObjects array of objects from XML temmplate
     */
    private void drawObjects(GraphicsContext gc, ArrayList<Objects> arrayOfObjects) {
        for (Objects obj : arrayOfObjects) {
            if (obj.getLabel().contains("plant")) {
                gc.setFill(Color.GREEN);
            } else if (obj.getLabel().contains("trash bin")) {
                gc.setFill(Color.BROWN);
            }
            gc.fillRect(obj.getPosition()[0], obj.getPosition()[1],
                    obj.getPosition()[2] - obj.getPosition()[0],
                    obj.getPosition()[3] - obj.getPosition()[1]);
            //System.out.println("Object " + obj.getId());
        }

    }

    private void drawHotColdSpots(GraphicsContext gc, ArrayList<Spot> arrayOfSpots) {

        for (Spot spot : arrayOfSpots) {
            if (spot.getSemaphor() == 1) {
                gc.setFill(Color.rgb(255, 64, 64, 0.2));
            } else if (spot.getSemaphor() == 2) {
                gc.setFill(Color.rgb(0, 0, 139, 0.2));
            }
            //gc.fillRect(0,0, 50,50);
            gc.getCanvas().setLayoutX(10);
            gc.fillRect(spot.getX(), spot.getY(), spot.getWidth(), spot.getHeigth());

        }
        //System.out.println("H/C " + s.getX() + " " + s.getY() + " " + s.getWidth() + " " + s.getHeigth());
    }

    /**
     * @param gc            GraphicsContext
     * @param arrayOfPerson array of Person
     */
    private void drawPersons(GraphicsContext gc, ArrayList<Person> arrayOfPerson) {
        for (Person p : arrayOfPerson) {
            gc.setFill(Color.BLACK);
            gc.fillOval(p.getCurrentPosition().getX(), p.getCurrentPosition().getY(), 5, 5);
        }
    }


    private void initializeCanvas() {
        canvas.setHeight(1000);
        canvas.setWidth(1000);
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFont(new Font(graphicsContext.getFont().getName(), 13.0));
        canvas.setLayoutY(1);
        canvas.setLayoutX(1);
        canvas.setScaleX(1);
        canvas.setScaleY(1);

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

    private void initializeSliderSpeedDayOfSim() {
        sliderSpeedDayOfSim.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderSpeedDayOfSim.setValue(newValue.intValue());
            lblSpeedDayValue.setText(String.format("%d", newValue.intValue()));
            speedDayOfSim = (int) sliderSpeedDayOfSim.getValue();
        });
    }

    private void changeFrame(int speedOfSim) {
        Animation.Status status = simulationLoop.getStatus();
        simulationLoop.stop();
        simulationLoop.getKeyFrames().clear();
        buildSimulationStart(speedOfSim);
        if (status == Animation.Status.RUNNING) {
            simulationLoop.play();
        }
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
     * @param event press on reset button
     */
    public void resetSim(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(MainApp.class.getResource("View/MainTemplate.fxml"));
            simulationLoop.stop();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnStartPause.getScene().setRoot(root);
    }

}