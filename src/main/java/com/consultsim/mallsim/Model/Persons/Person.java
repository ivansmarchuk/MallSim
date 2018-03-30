package com.consultsim.mallsim.Model.Persons;


import com.consultsim.mallsim.Model.Configuration;
import com.consultsim.mallsim.Model.Position;
import javafx.scene.shape.Circle;

/**
 * A presentation of a person in the simulation
 */
public class Person extends Circle{

    private Position currentPosition;

    private double speed;


    public Person (Position pos, double speed){
        //radius
        super(Configuration.PERSON_RADIUS);
        this.currentPosition = pos;
        this.speed = speed;


    }


    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;

    }

}
