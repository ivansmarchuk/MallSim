package com.consultsim.mallsim.Model.Persons;


import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.Model.Store;
import com.consultsim.mallsim.View.SimulationHandler;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A presentation of a person in the simulation
 */
public class Person {
    private static int nextID=0;
    private boolean isGoalMallDoor=false;
    private Position currentPosition;
    private Position nextPosition;
    private Store currentGoalStore;
    private int nrGoalStores;
    private double speed;
    private Random random;
    private SimulationHandler simulationHandler;
    private int id;
    private ArrayList<Store> goalStoresList;

    /**
     * Konstruktor
     *
     * @param pos               Position of Person
     * @param speed             Speed of person
     * @param simulationHandler Simulationhandler for access to maps
     * @param goalStoresList    Store which the person should walk to
     */
    public Person(Position pos, double speed, SimulationHandler simulationHandler, ArrayList<Store> goalStoresList) {
        //radius
        nrGoalStores=0;
        //this.goalStores = goalStores;
        this.currentGoalStore=goalStoresList.get(0);
        this.currentPosition=pos;
        this.nextPosition=pos;
        this.speed=speed;
        this.random=new Random();
        this.simulationHandler=simulationHandler;
        this.id=Person.nextID++;
        this.goalStoresList=goalStoresList;
    }

    public void computeNext() {

        int direction;
        int currentX=this.currentPosition.getX();
        int currentY=this.currentPosition.getY();
        int nextX;
        int nextY;
        if (currentGoalStore.getHeatMapValue(currentX, currentY) == 1) {
            //goal reached
            if (currentGoalStore == simulationHandler.getEntranceDoor()) {
                isGoalMallDoor=true;
                direction=4;

            } else {
                direction=4;
                //System.out.println("Goal reached");
                nrGoalStores++;
                currentGoalStore=goalStoresList.get(nrGoalStores);
            }
        } else {
            direction=findDirectionWithHeatMap(currentX, currentY);
        }

        nextX=movePerson(direction, currentX, currentY)[0];
        nextY=movePerson(direction, currentX, currentY)[1];

        //check if valid move
        //if condition here
        int typeOfCollision=isValidMove(nextX, nextY);
        if (typeOfCollision == 1) {
            //System.out.println("true");
            this.currentPosition.setX(nextX);
            this.currentPosition.setY(nextY);
            //System.out.println("nexty: " + nextY + " nextx: " + nextX);
            simulationHandler.crashMap[nextY][nextX]=1;
            simulationHandler.crashMap[currentY][currentX]=0;
        } else if (typeOfCollision == 2 || typeOfCollision == 3 || typeOfCollision == 0) {
            simulationHandler.crashMap[currentY][currentX]++;
        }
        if (simulationHandler.crashMap[currentY][currentX] == 4) {
            if (typeOfCollision == 2) {
                nextPosition=handleCollision(currentX, currentY);
            } else if (typeOfCollision == 3) {
                nextPosition=handleCollision(currentX, currentY);
            }
            simulationHandler.crashMap[nextPosition.getY()][nextPosition.getX()]=1;
            simulationHandler.crashMap[currentY][currentX]=0;
            this.currentPosition.setX(nextPosition.getX());
            this.currentPosition.setY(nextPosition.getY());
        }
        //System.out.println("X: " + this.getCurrentPosition().getX()  + " Y: " + this.getCurrentPosition().getY());
    }

    /**
     * Checks, if wall is in between new position or not, only called if person "jumps" to avoid being stuck
     *
     * @param currentY Current Y Position
     * @param currentX Current X Position
     * @param p        next Position to go to
     * @return true or false
     */
    private boolean isWallInBetween(int currentY, int currentX, Position p) {
        //System.out.println("Arrived");
        int lowerY=(currentY < p.getY() ? currentY : p.getY());
        int higherY=(currentY > p.getY() ? currentY : p.getY());
        int lowerX=(currentX < p.getX() ? currentX : p.getX());
        int higherX=(currentX > p.getX() ? currentX : p.getX());

        for (int y=lowerY; y < higherY; y++) {
            for (int x=lowerX; x < higherX; x++) {
                if (simulationHandler.crashMap[y][x] == 10) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * only called, if collision between two people is detected and they don't move
     *
     * @param currentX Current X Position
     * @param currentY Current Y Position
     * @return new position where can be move next step
     */
    private Position handleCollision(int currentX, int currentY) {
        int tempx;
        int tempy;

        Position tempPos=new Position(currentX, currentY);
        int chooseDirection=ThreadLocalRandom.current().nextInt(0, 4);
        switch (chooseDirection) {
            case 0: {
                if (currentY < ThreadLocalRandom.current().nextInt(50, 300)) {
                    tempy=currentY + ThreadLocalRandom.current().nextInt(0, 3);
                    tempx=currentX + ThreadLocalRandom.current().nextInt(0, 3);
                } else {
                    tempy=currentY + ThreadLocalRandom.current().nextInt(-3, 0);
                    tempx=currentX + ThreadLocalRandom.current().nextInt(-3, 0);
                }

            }
            break;
            case 1: {
                tempx=currentX + ThreadLocalRandom.current().nextInt(-3, 0);
                tempy=currentY + ThreadLocalRandom.current().nextInt(0, 3);
            }
            break;
            case 2: {
                tempx=currentX + ThreadLocalRandom.current().nextInt(0, 3);
                if (currentY < ThreadLocalRandom.current().nextInt(50, 300)) {
                    tempy=currentY + ThreadLocalRandom.current().nextInt(0, 3);
                } else {
                    tempy=currentY + ThreadLocalRandom.current().nextInt(-3, 0);
                }
            }
            break;
            case 3: {
                tempx=currentX + ThreadLocalRandom.current().nextInt(0, 3);
                tempy=currentY + ThreadLocalRandom.current().nextInt(0, 3);
            }
            break;
            default:
                tempx=currentX;
                tempy=currentY;
        }


        if (tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000) {
            if ((simulationHandler.crashMap[tempy][tempx] == 0)) {
                tempPos.setY(tempy);
                tempPos.setX(tempx);
            } else if ((simulationHandler.crashMap[tempy][tempx] < 5) && (simulationHandler.crashMap[tempy][tempx] != 0)) {
                simulationHandler.crashMap[tempy][tempx]=1;
            }
        }
        //checks, if chosen position is a valid one (if no walls are in between)
        if (!isWallInBetween(currentY, currentX, tempPos)) {
            return tempPos;
        } else {
            tempPos.setX(currentX);
            tempPos.setY(currentY);
        }

        return tempPos;

    }

    /**
     * check, if the next position is a valid one -> if there already is another person, abort
     *
     * @param nextX next X Position
     * @param nextY next Y Positon
     * @return boolean value when move is valid
     */
    private int isValidMove(int nextX, int nextY) {
        if (nextX < 0 || nextY < 0 || nextX > 999 || nextY > 999) {
            return 0;
        }
        for (Person p : simulationHandler.getArrayOfPersons()) {
            int x=p.getCurrentPosition().getX() - nextX;
            int y=p.getCurrentPosition().getY() - nextY;
            double distance=Math.sqrt(x * x + y * y);
            //System.out.println("Dist: " + distance);

            if ((distance < 11) && (p.id != this.id)) {
                return 2;
            }

            if (simulationHandler.crashMap[nextY][nextX] == 10) {
                return 3;
            }
        }
        return 1;
    }

    private int[] movePerson(int direction, int currentX, int currentY) {

        int nextStep=1;
        int[] newPosition={currentX, currentY};

        switch (direction) {
            case 0:
                //right
                if ((currentX + nextStep) <= 1000) {
                    newPosition[0]=currentX + nextStep;
                }
                break;

            case 1:
                //down
                if ((currentY + nextStep) <= 1000) {
                    newPosition[1]=currentY + nextStep;
                }
                break;

            case 2:
                //left
                if ((currentX - nextStep) >= 0) {
                    newPosition[0]=currentX - nextStep;
                }
                break;

            case 3:
                //up
                if ((currentY - nextStep) >= 0) {
                    newPosition[1]=currentY - nextStep;
                }
                break;

            case 4:
                //wait
                break;
        }

        return newPosition;
    }

    private int findDirectionWithHeatMap(int currentX, int currentY) {
        int direction[]=new int[4];
        int temp=0;
        int nextValueHeatMap;

        nextValueHeatMap=currentGoalStore.getHeatMapValue(currentX, currentY) - 1;

        if (currentGoalStore.getHeatMapValue(currentX + 1, currentY) == nextValueHeatMap) {
            //move right
            direction[temp]=0;
            temp++;
        }
        if (currentGoalStore.getHeatMapValue(currentX - 1, currentY) == nextValueHeatMap) {
            //move left
            direction[temp]=2;
            temp++;
        }
        if (currentGoalStore.getHeatMapValue(currentX, currentY + 1) == nextValueHeatMap) {
            //move down
            direction[temp]=1;
            temp++;
        }
        if (currentGoalStore.getHeatMapValue(currentX, currentY - 1) == nextValueHeatMap) {
            //move up
            direction[temp]=3;
            temp++;
        }
        if (temp == 0) {
            //System.out.println("Strange");
            return ThreadLocalRandom.current().nextInt(0, 3);
        } else {
            return direction[random.nextInt(temp)];
        }
    }

    /**
     * Gets position of person
     *
     * @return current position
     */
    public Position getCurrentPosition() {
        return currentPosition;
    }

    public boolean isGoalMallDoor() {
        return isGoalMallDoor;
    }

    public void setCurrentGoalStore(Store currentGoalStore) {
        this.currentGoalStore=currentGoalStore;
    }
}