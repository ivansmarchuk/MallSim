package com.consultsim.mallsim.Model.Persons;


import com.consultsim.mallsim.Model.Configuration;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.Model.Store;
import com.consultsim.mallsim.View.SimulationHandler;
import javafx.scene.shape.Circle;

import java.util.Random;

/**
 * A presentation of a person in the simulation
 */
public class Person extends Circle{

    private Position currentPosition;
    private Position nextPosition;

    private double speed;
    private Random random;
    private SimulationHandler simulationHandler;
    private static int nextID = 0;
    private int id;
    private int movedSince;
    public static ArrayList<Store> arrayOfStores;
    private String interestedIn;
    // hier könnte auch je nachdem ein StringArray hin

    public Person (Position pos, double speed, SimulationHandler simulationHandler){
        //radius
        int temp;

        super(Configuration.PERSON_RADIUS);
        this.currentPosition = pos;
        this.nextPosition = pos;
        this.speed = speed;
        this.random = new Random();
        this.simulationHandler = simulationHandler;
        this.id = Person.nextID++;
        this.movedSince = 0;

        //choose what the person wants to buy
        //hier fehlen noch die richtigen Kategorien, die es dann auch für die Geschäfte geben muss
        temp = (int) random.nextInt(5);
        switch (temp) {
            case 0:
                //random movement
                this.interestedIn = "nothing";
                break;
            case 1:
                this.interestedIn = "clothing";
                break;
            case 2:
                this.interestedIn = "food";
                break;
            case 3:
                this.interestedIn = "book";
                break;
            case 4:
                this.interestedIn = "other";
                break;
        }


    }


    public void computeNext(){

        //Ich berechne die Schritte mit linksoben als 0/0

        int direction;
        int nextStep;
        int currentX = this.currentPosition.getX();
        int currentY = this.currentPosition.getY();
        int nextX = currentX;
        int nextY = currentY;
        int goalX = -1;
        int goalY = -1;
        int divisorForDoor;
        int timeInShop = 0;
        boolean inShop;
        String interestedIn = this.interestedIn.getInterestedIn();
            nextStep = 4;

            if (interestedIn == "nothing") {
                //compute next position (simple and randomized)
                direction = (int) random.nextInt(5);
            }
            else {
                for (Store s: arrayOfStores) {
                    for(int i = 0; i <2;i++){
                        if(interestedIn == s.getInterestingFor()[i]){
                            //im moment gehen die Leute auch immer zum ersten Geschäft, was hat, was sie suchen

                            divisorForDoor = (int) random.nextInt(20);
                            //@param: divisorForDoor is used, so the customers will have different goals in the door,
                            // and will not all head for the middle, which may cause collisions

                            //condition: 0 is lower than 2
                            //condition: 1 is lower than 3
                            // by definition of the xml-File

                            goalX = s.getDoorPosition()[0] + (int)Math.ceil(Math.abs(s.getDoorPosition()[2] - s.getDoorPosition()[0]) / 20 * divisorForDoor);
                            goalY = s.getDoorPosition()[1] + (int)Math.ceil(Math.abs(s.getDoorPosition()[3] - s.getDoorPosition()[1]) / 20 * divisorForDoor);

                            break;
                        }
                    }

                }

                //find the necessary way to move
                // one or two directions will be found
                if (goalX = -1){
                    System.out.println("No shop found");
                    // Was passiert dann?
                }

                if (goalX == currentX){
                    //goal reached X
                }
                else if(goalX< currentX) {
                    //goal left
                }
                else {
                    //goal right
                }
                if (goalY == currentY){
                    //goal reached Y
                }
                else if (goalY < currentY) {
                    //goal up
                }
                else {
                    //goal down
                }



            }
        //determine which way to go

           //System.out.println("Rand: " + direction);
        //move the person
            if(!((currentX >= 1000) || (currentX <= 0 ) || (currentY >= 1000) || (currentY <= 0))) {

                switch (direction) {
                    case 0:
					//right
                        nextX = currentX + nextStep;
                        break;

                    case 1:
					//up
                        nextY = currentY + nextStep;
                        break;

                    case 2:
					//left
                        nextX = currentX - nextStep;
                        break;

                    case 3:
					//down
                       nextY = currentY - nextStep;
                        break;

                    case 4:
					//wait

                        break;
                }

                //check if valid move
                if(isValidMove(nextX, nextY)){
                    //System.out.println("true");
                    this.currentPosition.setX(nextX);
                    this.currentPosition.setY(nextY);
                    System.out.println("nexty: " + nextY + " nextx: " + nextX);
                    simulationHandler.crashMap[nextY][nextX] = 1;
                    simulationHandler.crashMap[currentY][currentX] = 0;
                }else{
                    simulationHandler.crashMap[currentY][currentX]++;
                }

                if(simulationHandler.crashMap[currentY][currentX] > 4){
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
                    }else{
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

    public String getInterestedIn(){return interestedIn;}

}
