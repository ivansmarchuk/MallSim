package com.consultsim.mallsim.View;

import com.consultsim.mallsim.Model.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class StController implements Initializable{

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


    public void initialize(URL location, ResourceBundle resources) {

        StatisticHandler stat = StatisticHandler.getStatisticInstance();
        SimulationHandler simulationHandler = SimulationHandler.getSimulationInstance();



        lblTitle.setFont(new Font(Configuration.MAX_FONT_SIZE));

        lblCountHoldSpots.setText(Integer.toString(stat.getCounterColdSpots()));
        lblCountHotSpots.setText(Integer.toString(stat.getCounterHotSpots()));
        lblCountPersons.setText(Integer.toString(simulationHandler.getArrayOfPersons().size()));
        lblCountAllPeople.setText(Integer.toString(stat.getCountOfPersons()));

    }
}
