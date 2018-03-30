package com.consultsim.mallsim.Model.Persons;


import com.consultsim.mallsim.Model.Configuration;
import com.consultsim.mallsim.Model.Position;
import javafx.scene.shape.Circle;

import java.util.Random;

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

    public void computeNext(){
        Random random = new Random();
        int temp;
            temp = random.nextInt(3) + 1;

            switch (temp){
                case 0: {
                    this.currentPosition.setX(this.currentPosition.getX()+1);
                }
                case 1: {
                    this.currentPosition.setY(this.currentPosition.getY()+1);
                }
                case 2: {
                    this.currentPosition.setX(this.currentPosition.getX()-1);
                }
                case 3: {
                    this.currentPosition.setY(this.currentPosition.getY()-1);
                }

            }

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
