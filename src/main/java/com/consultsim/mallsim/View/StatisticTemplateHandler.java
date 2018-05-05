package com.consultsim.mallsim.View;

import com.consultsim.mallsim.Model.Configuration;
import com.consultsim.mallsim.View.CanvasFeatures.DrawFeatures;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.ResourceBundle;

public class StatisticTemplateHandler implements Initializable{

    @FXML
    public Label lblTitle;
    @FXML
    public Label lblCountHoldSpots;
    @FXML
    public Label lblCountHotSpots;
    @FXML
    public Label lblCountPersons;
    @FXML
    public Label lblTest;

    @FXML
    public Label lblCountAllPeople;
    @FXML
    public Canvas canvas;
    @FXML
    private GraphicsContext graphicsContext;
    private DrawFeatures drawFeatures = DrawFeatures.getDrawInstance();

    SimulationHandler simulationHandler = SimulationHandler.getSimulationInstance();
    StatisticHandler statisticHandler = StatisticHandler.getStatisticInstance();

    public void initialize(URL location, ResourceBundle resources) {


        initializeCanvas();

        lblTitle.setFont(new Font(Configuration.MAX_FONT_SIZE));

        lblCountHoldSpots.setText(Integer.toString(statisticHandler.getCounterColdSpots()));
        lblCountHotSpots.setText(Integer.toString(statisticHandler.getCounterHotSpots()));
        lblCountPersons.setText(Integer.toString(simulationHandler.getArrayOfPersons().size()));
        lblCountAllPeople.setText(Integer.toString(statisticHandler.getCountOfPersons()));
        drawStatistic();

    }



    private void drawStatistic() {
        drawFeatures.drawStores(graphicsContext, simulationHandler.arrayOfStores);
        drawFeatures.drawObjects(graphicsContext, simulationHandler.arrayOfObjects);

        System.out.println();
        //String key=statisticHandler.getHm().last().getKey();
        NavigableSet<Map.Entry<String, Integer>> hmMap = statisticHandler.getHm();

        int counter;
        //System.out.println(statisticHandler.getHm());
        counter=Configuration.STATISTIC_COUNT_HOT_COLD_SPOTS > 50 ? 50 : Configuration.STATISTIC_COUNT_HOT_COLD_SPOTS;

        for (int i=0; i < counter; i++) {
            String key =hmMap.pollLast().getKey();
            System.out.println(key);
            drawFeatures.drawHotSpot(graphicsContext, key, Configuration.OPACITY_SPOTS_STATISTIC_WINDOW);
        }
        for (int i=0; i <counter; i++) {
            String key =hmMap.pollFirst().getKey();
            System.out.println(key);
            drawFeatures.drawColdSpot(graphicsContext, key, Configuration.OPACITY_SPOTS_STATISTIC_WINDOW);
        }

        //drawFeatures.drawHotColdSpots(graphicsContext, statisticHandler.hotColdSpots, 0.5);



    }

    private void initializeCanvas() {
        canvas.setHeight(Configuration.CANVAS_HEIGHT_SIZE);
        canvas.setWidth(Configuration.CANVAS_WIDTH_SIZE);
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFont(new Font(graphicsContext.getFont().getName(), Configuration.DEFAULT_FONT_SIZE));


    }


}
