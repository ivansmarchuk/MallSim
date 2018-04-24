package com.consultsim.mallsim.View;

import com.consultsim.mallsim.MainApp;
import com.consultsim.mallsim.Model.Configuration;
import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.StaticObjects.EntranceDoor;
import com.consultsim.mallsim.Model.StaticObjects.Spot;
import com.consultsim.mallsim.Model.StaticObjects.StoreHeatMap;
import com.consultsim.mallsim.Model.Store;
import com.consultsim.mallsim.View.CanvasFeatures.DrawFeatures;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UIHandler implements Initializable {

    @FXML
    public StackPane stackPane;
    @FXML
    public Button btnResetSim;
    @FXML
    public Button showStatistics;
    @FXML
    public Label lblCountPerson;
    @FXML
    public Slider sliderSpeedDayOfSim;
    @FXML
    public Button btnNextStep;
    @FXML
    public Label lblSpeedDayValue;
    @FXML
    public Button btnStartPause;
    @FXML
    public Button layoutLoadFromFIle;
    ProgressIndicator progressIndicator=new ProgressIndicator();
    private int speedDayOfSim;
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
    @FXML
    private AnchorPane basePane;
    private Stage primaryStage;
    private Timeline simulationLoop;
    private ArrayList<Person> arrayOfPerson;
    private ArrayList<Spot> arrayOfSpots;
    private SimulationHandler simulationHandler;
    private StatisticHandler statisticHandler;
    private ArrayList<Store> arrayOfStores;
    private ArrayList<Objects> arrayOfObjects;
    private EntranceDoor entranceDoor;
    private double dayMinutes=540;
    private double daySeconds;
    private DrawFeatures drawFeatures=DrawFeatures.getDrawInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCanvas();
        speedDayOfSim=Configuration.INITIAL_DAY_SPEED;
        lblSpeedDayValue.setText(String.format("%d", this.speedDayOfSim));
        btnStartPause.setText("Start");

        initializeSliderDayTime();
        initializeSliderNumberOfPersons();
        initializeSliderSpeedDayOfSim();
        initializeSimHandler();


        basePane.setPrefHeight(Configuration.STATISTIC_WINDOW_HEIGHT_SIZE);
        basePane.setPrefWidth(Configuration.STATISTIC_WINDOW_WIDTH_SIZE);
        showStatistics.setOnAction(event -> showSimStatistic());
        buildSimulationStart(this.speedDayOfSim);

    }

    private void initializeSimHandler() {
        //simulationHandler = new SimulationHandler();
        simulationHandler=SimulationHandler.getSimulationInstance();
        statisticHandler=StatisticHandler.getStatisticInstance();
        arrayOfPerson=new ArrayList<>();
        arrayOfSpots=new ArrayList<>();
        // Here are initialized persons
        simulationHandler.initializePersons();
        //drawFeature.drawCrashMap(graphicsContext, SimulationHandler.crashMap);
        arrayOfPerson=simulationHandler.getArrayOfPersons();
        lblCountPerson.setText(Integer.toString(arrayOfPerson.size()));
        arrayOfSpots=simulationHandler.statisticHandler.getHotColdSpots();


    }


    /**
     * If Button LoadFromFile was pressed
     *
     * @param actionEvent Load from File was pressed
     */
    public void loadLayoutFromFile(MouseEvent actionEvent) throws InterruptedException {

        //load from file in root directory
        try {
            graphicsContext=canvas.getGraphicsContext2D();
            final FileChooser fileChooser=new FileChooser();
            FileChooser.ExtensionFilter extentionFilter=new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extentionFilter);

            String userDirectoryString=System.getProperty("user.dir");
            File userDirectory=new File(userDirectoryString);
            if (!userDirectory.canRead()) {
                userDirectory=new File("c:/");
            }
            fileChooser.setInitialDirectory(userDirectory);
            //Choose the file
            File chosenFile=fileChooser.showOpenDialog(null);
            //Make sure a file was selected, if not return default
            String path;
            if (chosenFile != null) {

                path=chosenFile.getPath();
                FileHandler fileHandler=new FileHandler();
                fileHandler.readFile(path);

                arrayOfStores=fileHandler.getArrayOfStores();
                arrayOfObjects=fileHandler.getArrarOfObjects();
                entranceDoor=fileHandler.getEntranceDoor();
                simulationHandler.setArrayOfObjects(arrayOfObjects);
                simulationHandler.setArrayOfStores(arrayOfStores);
                simulationHandler.setEntranceDoor(entranceDoor);
                simulationHandler.fillCrashMapWithStoresAndObjects();

                StoreHeatMap nrTasks;

                ExecutorService pool=Executors.newFixedThreadPool(3);
                progressIndicator.setStyle("-fx-progress-color: #FFF;");
                stackPane.getChildren().add(progressIndicator);
                for (Store s : arrayOfStores) {
                    nrTasks=new StoreHeatMap();
                    nrTasks.setCrashMap(simulationHandler.crashMap);
                    nrTasks.setStore(s);
                    pool.execute(nrTasks);

                }
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        entranceDoor.generateHeatMap(simulationHandler.crashMap);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stackPane.getChildren().remove(progressIndicator);
                                btnStartPause.setDisable(false);
                                btnNextStep.setDisable(false);
                                btnResetSim.setDisable(false);

                                drawLayoutFromXMLFile();
                            }
                        });
                    }
                });

            } else {
                //default return value
                path = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Initializes the simulation loop
     *
     * @param speedDayOfSim fps
     */
    private void buildSimulationStart(int speedDayOfSim) {
        Duration duration=Duration.millis(1000 / (float) (speedDayOfSim * 10));
        KeyFrame frame=getToNextFrame(duration);
        simulationLoop=new Timeline();
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
            drawFeatures.drawHotColdSpots(graphicsContext, arrayOfSpots, 0.2);
            drawFeatures.drawPersons(graphicsContext, arrayOfPerson);
            double newDayTime=sliderDayTime.getValue() + duration.toSeconds() * sliderSpeedDayOfSim.getValue() * Configuration.SPEED_TIME_FACTOR;
            sliderDayTime.setValue(newDayTime);
            if (sliderDayTime.getValue() != sliderDayTime.getMax()) {
                lblCountPerson.setText(Integer.toString(arrayOfPerson.size()));
                //generation new persons
                simulationHandler.generatePerson(sliderNumberOfPersons.getValue(), sliderDayTime.getValue());
                simulationHandler.computeNextPositionOfPersons(sliderDayTime.getValue());
                //drawFeatures.drawCrashMap(graphicsContext, SimulationHandler.crashMap);
            } else {
                //TODO anders implementieren
                btnStartPause.setText("Start");
                btnStartPause.setDisable(true);
                sliderDayTime.setValue(sliderDayTime.getMin());
                showSimStatistic();
                simulationLoop.stop();
                clearCanvas(graphicsContext);
                simulationHandler.arrayOfPersons=new ArrayList<>();
            }
        });
    }


    private void showSimStatistic() {
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("StatisticTemplate.fxml"));
            AnchorPane page=loader.load();
            Stage dialogStage=new Stage();
            dialogStage.initOwner(this.basePane.getScene().getWindow());
            dialogStage.setTitle("Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene=new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    /**
     * called when the button 'Next Step' is pressed
     *
     * @param event press on nex next step button
     */
    @FXML
    public void getNextStep(ActionEvent event) {
        Duration duration=Duration.millis(1000 / (float) (speedDayOfSim * 10));
        KeyFrame frame=getToNextFrame(duration);
        simulationLoop=new Timeline();
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
        StringConverter<Double> stringConverter=new StringConverter<Double>() {
            //convert int to hours format hh:mm
            @Override
            public String toString(Double object) {
                long seconds=object.longValue();
                long minutes=TimeUnit.SECONDS.toMinutes(seconds);
                long hour=TimeUnit.MINUTES.toHours(minutes);
                long remainingMinutes=minutes - TimeUnit.HOURS.toMinutes(hour);
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
        daySeconds=sliderDayTime.getMin();

    }

    /**
     * Draws stores and other objects on canvas
     */
    private void drawLayoutFromXMLFile() {
        drawFeatures.drawStores(graphicsContext, arrayOfStores);
        drawFeatures.drawObjects(graphicsContext, arrayOfObjects);
    }

    private void initializeCanvas() {
        canvas.setHeight(Configuration.CANVAS_HEIGHT_SIZE);
        canvas.setWidth(Configuration.CANVAS_WIDTH_SIZE);
        graphicsContext=canvas.getGraphicsContext2D();
        graphicsContext.setFont(new Font(graphicsContext.getFont().getName(), Configuration.DEFAULT_FONT_SIZE));
        canvas.setLayoutY(1);
        canvas.setLayoutX(1);
        canvas.setScaleX(1);
        canvas.setScaleY(1);

    }


    /**
     * Event handling for slider  'Geschwindigkeit'
     */

    private void initializeSliderSpeedDayOfSim() {
        sliderSpeedDayOfSim.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderSpeedDayOfSim.setValue(newValue.intValue());
            lblSpeedDayValue.setText(String.format("%d", newValue.intValue()));
            speedDayOfSim=(int) sliderSpeedDayOfSim.getValue();
            changeFrame(speedDayOfSim);
        });
    }

    private void changeFrame(int speedDayOfSim) {
        Animation.Status status=simulationLoop.getStatus();
        simulationLoop.stop();
        simulationLoop.getKeyFrames().clear();
        buildSimulationStart(speedDayOfSim);
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
        resetSimulation();
    }

    private void resetSimulation() {
        Parent root=null;
        try {
            root=FXMLLoader.load(MainApp.class.getResource("View/MainTemplate.fxml"));
            simulationHandler.arrayOfPersons=new ArrayList<>();
            simulationHandler.arrayOfObjects=new ArrayList<>();
            statisticHandler.hotColdSpots=new ArrayList<>();
            simulationHandler.arrayOfStores=new ArrayList<>();
            statisticHandler.setCountOfPersons(0);
            simulationLoop.stop();

        } catch (IOException e) {
            e.printStackTrace();
        }
        btnStartPause.getScene().setRoot(root);
        btnResetSim.setDisable(false);


    }


}