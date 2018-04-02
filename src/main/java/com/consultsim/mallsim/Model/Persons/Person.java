package com.consultsim.mallsim.Model.Persons;


import com.consultsim.mallsim.Model.Configuration;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.View.SimulationHandler;
import javafx.scene.shape.Circle;

import java.util.Random;

/**
 * A presentation of a person in the simulation
 */
public class Person {

    private Position currentPosition;
    private Position nextPosition;

    private double speed;
    private Random random;
    private SimulationHandler simulationHandler;
    private static int nextID = 0;
    private int id;
    private int movedSince;
    private double radius;

    public Person (Position pos, double speed, SimulationHandler simulationHandler){
        //radius
        this.radius = Configuration.PERSON_RADIUS;
        this.currentPosition = pos;
        this.nextPosition = pos;
        this.speed = speed;
        this.random = new Random();
        this.simulationHandler = simulationHandler;
        this.id = Person.nextID++;
        this.movedSince = 0;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    //compute next position (simple and randomized)
    public void computeNext(){

        int temp;
        int nextStep;
        int currentX = this.currentPosition.getX();
        int currentY = this.currentPosition.getY();
        int nextX = currentX;
        int nextY = currentY;
        temp = (int) random.nextInt(5);
        nextStep = 4;
        //System.out.println("Rand: " + temp);

        //REBECCA: INSERT YOUR CODE HERE
        //TODO: Create Function to compute the position of the people
        if(!((currentX >= 1000) || (currentX <= 0 ) || (currentY >= 1000) || (currentY <= 0))) {

            switch (temp) {
                case 0:
                    nextX = currentX + nextStep;
                    break;

                case 1:
                    nextY = currentY + nextStep;
                    break;

                case 2:
                    nextX = currentX - nextStep;
                    break;

                case 3:
                    nextY = currentY - nextStep;
                    break;

                case 4:

                        break;
                }
                //check if valid move
                //if condition here 
                if(isValidMove(nextX, nextY)){
                    //System.out.println("true");
                    this.currentPosition.setX(nextX);
                    this.currentPosition.setY(nextY);
                    //System.out.println("nexty: " + nextY + " nextx: " + nextX);
                    simulationHandler.crashMap[nextY][nextX] = 1;
                    simulationHandler.crashMap[currentY][currentX] = 0;
                }else{
                    simulationHandler.crashMap[currentY][currentX]++;
                }

                if(simulationHandler.crashMap[currentY][currentX] == 4){
                    nextPosition = handleCollision(nextX, nextY, currentX, currentY);
                    simulationHandler.crashMap[nextPosition.getY()][nextPosition.getX()] = 1;
                    simulationHandler.crashMap[currentY][currentX] = 0;
                    movedSince = 0;
                    this.currentPosition.setX(nextPosition.getX());
                    this.currentPosition.setY(nextPosition.getY());
                }


            //System.out.println("X: " + this.getCurrentPosition().getX()  + " Y: " + this.getCurrentPosition().getY());

        }

    }

    private Position handleCollision(int nextX, int nextY, int currentX, int currentY){
        int tempx;
        int tempy;
        Position tempPos = new Position(currentX, currentY);
        for(int y = 0; y < 20; y++){
            for(int x = 0; x < 20; x++){
                tempy = currentY - y;
                tempx = currentX -x;
                if(tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000){
                    if((simulationHandler.crashMap[tempy][tempx] == 0)){
                        tempPos = new Position(tempx, tempy);
                    }else if(simulationHandler.crashMap[tempy][tempx] != 10){
                        simulationHandler.crashMap[tempy][tempx] = 1;
                    }
                }


            }
        }
        return tempPos;
    }

    //check, if the next position is a valid one
    public boolean isValidMove(int nextX, int nextY){
        if(nextX < 0 || nextY < 0 || nextX > 999 || nextY > 999){
            return false;
        }
            for(Person p: simulationHandler.getArrayOfPersons()){
                int x = p.getCurrentPosition().getX() - nextX;
                int y = p.getCurrentPosition().getY() - nextY;
                double distance = Math.sqrt(x*x + y*y);
                //System.out.println("Dist: " + distance);

                if((distance < 11) && (p.id != this.id)){
                    return false;
                }

                if(simulationHandler.crashMap[nextY][nextX] == 10){
                    return false;
                }
            }
            return true;
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