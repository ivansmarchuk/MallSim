package com.consultsim.mallsim.Model.Persons;


import com.consultsim.mallsim.Model.Configuration;
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
    public static ArrayList<Store> arrayOfStores;
    private static int nextID=0;
    boolean isGoalMallDoor=false;
    private int timeInShop;
    private boolean inShop;
    private int timeSearching;
    private Position currentPosition;
    private Position nextPosition;
    private Store[] goalStores;
    private Store currentGoalStore;
    private int nrGoalStores;
    private double speed;
    private Random random;
    private SimulationHandler simulationHandler;
    private int id;
    //TO DO chrashmapObjects!!!
    private String interestedIn;    // hier könnte auch je nachdem ein StringArray hin
    private int movedSince;
    private double radius;
    private Person collidedWithPerson;
    private int waitedSince=0;
    private ArrayList<Store> goalStoresList;
    //private int goalID;

    private int goalX;
    private int goalY;

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
        this.radius=Configuration.PERSON_RADIUS;
        this.currentPosition=pos;
        this.nextPosition=pos;
        this.speed=speed;
        this.random=new Random();
        this.simulationHandler=simulationHandler;
        this.id=Person.nextID++;
        this.movedSince=0;
        //this.interestedIn = generateInterest();
        //this.interestedIn = "no";
        this.waitedSince=0;
        this.goalStoresList=goalStoresList;
        //this.goalX = goalStore.getDoorPosition()[2];
        //this.goalY = goalStore.getDoorPosition()[3];

        //inShop = false;
        //timeInShop = 0;
        //timeSearching = 0;


//        if (!(interestedIn.equals("nothing"))) {
//         goalID = getGoalCoordinates();
//         if (goalID == -1) {
//         goalX = -1;
//         goalY = -1;
//         }
//         } else {
//         goalX = goalStore.getDoorPosition()[2];
//         goalY = goalStore.getDoorPosition()[3];
//         }

    }

    /**
     * Gets value of how large a person is
     *
     * @return
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Changes radius of person
     *
     * @param radius
     */
    public void setRadius(double radius) {
        this.radius=radius;
    }

    /**
     * Computes next position of person (simple and randomized)
     */
    public void computeNext() {

        int direction;
        int currentX=this.currentPosition.getX();
        int currentY=this.currentPosition.getY();
        int nextX=currentX;
        int nextY=currentY;

//        if (interestedIn.equals("nothing")) {
//            //System.out.println("No shop found or needed");
//            // Was passiert dann?
//            //compute next position (simple and randomized)
//            direction = (int) random.nextInt(5);
//        } else {
//            direction = findDirectionWithHeatMap(currentX, currentY);
//            /** if ((currentX == goalX) && (currentY == goalY)) {
//             System.out.println("Goal reached");
//             direction = 4;
//             goalX = -1;
//             goalY = -1;
//             interestedIn = "nothing";
//             }*/
//        }

        //TODO Check if person is at entrance door and delete
        if (currentGoalStore.getHeatMapValue(currentX, currentY) == 1) {
            //goal reached
            if (currentGoalStore == simulationHandler.getEntranceDoor()) {
                /**
                 * person leaves the mall
                 * delete person
                 */
                isGoalMallDoor=true;
                direction=4;
                //simulationHandler.getArrayOfPersons().remove(this);
            } else {
                direction=4;
                System.out.println("Goal reached");
                nrGoalStores++;
                currentGoalStore=goalStoresList.get(nrGoalStores);
            }

            /*
            if (goalStores[nrGoalStores] == null) {
                currentGoalStore = simulationHandler.getEntranceDoor();
            }
            else currentGoalStore = goalStores[nrGoalStores];
            */
        } else {
            direction=findDirectionWithHeatMap(currentX, currentY);
        }
        //System.out.println(direction);

           /* if(inShop && (timeInShop < 20) ){
                //TO DO: dont leave shop yet -> check if move is still in the shop
            }*/

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
            waitedSince++;
        }

        if (simulationHandler.crashMap[currentY][currentX] == 4) {
            if (typeOfCollision == 2) {
                nextPosition=handleCollision(nextX, nextY, currentX, currentY, 3);
            } else if (typeOfCollision == 3) {
                nextPosition=handleCollision(nextX, nextY, currentX, currentY, 3);
            }
            simulationHandler.crashMap[nextPosition.getY()][nextPosition.getX()]=1;
            simulationHandler.crashMap[currentY][currentX]=0;
            movedSince=0;
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
     * @return
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
     * @param nextX          Next X Position
     * @param nextY          Next Y Position
     * @param currentX       Current X Position
     * @param currentY       Current Y Position
     * @param viewableRadius Area to cover when searching for a way to avoid being stuck
     * @return
     */
    private Position handleCollision(int nextX, int nextY, int currentX, int currentY, int viewableRadius) {
        int tempx;
        int tempy;

        Position tempPos=new Position(currentX, currentY);

        //int chooseDirection = (int) random.nextInt(4);
        //int found = 0;

        //chooses the direction the person should "escape"
        //tempx = currentX + ThreadLocalRandom.current().nextInt(-viewableRadius, viewableRadius);
        int chooseDirection = ThreadLocalRandom.current().nextInt(0, 4);
        switch (chooseDirection) {
            case 0: {
                if (currentY < ThreadLocalRandom.current().nextInt(50, 300)){
                    tempy=currentY + ThreadLocalRandom.current().nextInt(0, viewableRadius);
                    tempx=currentX + ThreadLocalRandom.current().nextInt( 0, viewableRadius);
                }else{
                    tempy=currentY + ThreadLocalRandom.current().nextInt(-viewableRadius, 0);
                    tempx=currentX + ThreadLocalRandom.current().nextInt(-viewableRadius, 0);
                }

            }break;
            case 1: {
                tempx=currentX + ThreadLocalRandom.current().nextInt(-viewableRadius, 0);
                tempy=currentY + ThreadLocalRandom.current().nextInt(0, viewableRadius);
            }break;
            case 2: {
                tempx=currentX + ThreadLocalRandom.current().nextInt(0, viewableRadius);
                if (currentY < ThreadLocalRandom.current().nextInt(50, 300)){
                    tempy=currentY + ThreadLocalRandom.current().nextInt(0, viewableRadius);
                }else{
                    tempy=currentY + ThreadLocalRandom.current().nextInt(-viewableRadius, 0);
                }
            }break;
            case 3: {
                tempx=currentX + ThreadLocalRandom.current().nextInt(0, viewableRadius);
                tempy=currentY + ThreadLocalRandom.current().nextInt(0, viewableRadius);
            }break;
            default:
                tempx = currentX;
                tempy = currentY;
        }



        if (tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000) {
            if ((simulationHandler.crashMap[tempy][tempx] == 0)) {
                tempPos.setY(tempy);
                tempPos.setX(tempx);
            } else if ((simulationHandler.crashMap[tempy][tempx] < 5) && (simulationHandler.crashMap[tempy][tempx] != 0)) {
                simulationHandler.crashMap[tempy][tempx]=1;
            }
        }

        /*
        switch (chooseDirection) {
            case 0: {
                for (int y = -viewableRadius; y < viewableRadius; y++) {
                    for (int x = -viewableRadius; x < viewableRadius; x++) {
                        tempy = currentY + y;
                        tempx = currentX + x;
                        if (tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000) {
                            if ((simulationHandler.crashMap[tempy][tempx] == 0)) {
                                tempPos.setY(tempy);
                                tempPos.setX(tempx);
                            } else if ((simulationHandler.crashMap[tempy][tempx] < 5) && (simulationHandler.crashMap[tempy][tempx] != 0)) {
                                simulationHandler.crashMap[tempy][tempx] = 1;
                            }
                        }


                    }
                }
                break;
            }
            case 1:
                for (int y = viewableRadius; y < -viewableRadius; y--) {
                    for (int x = viewableRadius; x < -viewableRadius; x--) {
                        tempy = currentY + y;
                        tempx = currentX + x;
                        if (tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000) {
                            if ((simulationHandler.crashMap[tempy][tempx] == 0)) {
                                tempPos.setY(tempy);
                                tempPos.setX(tempx);

                            } else if ((simulationHandler.crashMap[tempy][tempx] < 5) && (simulationHandler.crashMap[tempy][tempx] != 0)) {
                                simulationHandler.crashMap[tempy][tempx] = 1;
                            }
                        }


                    }
                }
                break;

            case 2:
                for (int y = -viewableRadius; y < viewableRadius; y++) {
                    for (int x = viewableRadius; x < -viewableRadius; x--) {
                        tempy = currentY + y;
                        tempx = currentX + x;
                        if (tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000) {
                            if ((simulationHandler.crashMap[tempy][tempx] == 0)) {
                                tempPos.setY(tempy);
                                tempPos.setX(tempx);

                            } else if ((simulationHandler.crashMap[tempy][tempx] < 5) && (simulationHandler.crashMap[tempy][tempx] != 0)) {
                                simulationHandler.crashMap[tempy][tempx] = 1;
                            }
                        }
                    }
                }
                break;

            case 3:
                for (int y = viewableRadius; y < -viewableRadius; y--) {
                    for (int x = -viewableRadius; x < viewableRadius; x++) {
                        tempy = currentY + y;
                        tempx = currentX + x;
                        if (tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000) {
                            if ((simulationHandler.crashMap[tempy][tempx] == 0)) {
                                tempPos.setY(tempy);
                                tempPos.setX(tempx);

                            } else if ((simulationHandler.crashMap[tempy][tempx] < 5) && (simulationHandler.crashMap[tempy][tempx] != 0)) {
                                simulationHandler.crashMap[tempy][tempx] = 1;
                            }
                        }


                    }
                }
                break;

        }*/

        //checks, if chosen position is a valid one (if no walls are in between)
        if (!isWallInBetween(currentY, currentX, tempPos)) {
            waitedSince=0;
            return tempPos;
        } else {

            tempPos.setX(currentX);
            tempPos.setY(currentY);
            waitedSince++;
        }

        if (waitedSince > 15) {

            //simulationHandler.getArrayOfPersons().remove(this);
        }
        return tempPos;

    }

    /**
     * check, if the next position is a valid one -> if there already is another person, abort
     *
     * @param nextX next X Position
     * @param nextY next Y Positon
     * @return
     */
    public int isValidMove(int nextX, int nextY) {
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

//    public String generateInterest() {
//        //choose what the person wants to buy
//        //hier fehlen noch die richtigen Kategorien, die es dann auch für die Geschäfte geben muss
//        int temp = (int) random.nextInt(8);
//        switch (temp) {
//            case 0:
//                //random movement
//                return "nothing";
//            case 1:
//                return "clothing";
//            case 2:
//                return "food";
//            case 3:
//                return "tech";
//            case 4:
//                return "sport";
//            case 5:
//                return "toys";
//            case 6:
//                return "general";
//            case 7:
//                return "other";
//            default:
//                return "error in generate interest()";
//        }
//
//    }

    /**
     * public int getGoalCoordinates() {
     * //int goalX = -1;
     * //int goalY = -1;
     * int interestingShops[] = new int[20];  //Ich weiß noch nicht wie viele Shops es gibt
     * int nrInterestingShops = 0;
     * int temp;
     * //int divisorForDoor;
     * arrayOfStores = simulationHandler.getArrayOfStores();
     * <p>
     * for (Store s : arrayOfStores) {
     * //condition: only 1 info tag on each store
     * if (interestedIn.equals(s.getInterestingFor()[0])) {
     * interestingShops[nrInterestingShops] = s.getId();
     * nrInterestingShops++;
     * }
     * }
     * <p>
     * if (nrInterestingShops != 0) {
     * <p>
     * temp = (int) random.nextInt(nrInterestingShops);
     * //get shop by id
     * <p>
     * //            for (Store s : arrayOfStores) {
     * //                if (s.getId() == interestingShops[temp]) {
     * //                    divisorForDoor = (int) random.nextInt(20);
     * //                    //@param: divisorForDoor is used, so the customers will have different goals in the door,
     * //                    // and will not all head for the middle, which may cause collisions
     * //
     * //                    //condition: 0 is lower than 2
     * //                    //condition: 1 is lower than 3
     * //                    // by definition of the xml-File
     * //
     * //                    goalX = s.getDoorPosition()[0] + (int) Math.ceil(Math.abs(s.getDoorPosition()[2] - s.getDoorPosition()[0]) / 20 * divisorForDoor);
     * //                    goalY = s.getDoorPosition()[1] + (int) Math.ceil(Math.abs(s.getDoorPosition()[3] - s.getDoorPosition()[1]) / 20 * divisorForDoor);
     * //                    break;
     * //                }
     * //            }
     * <p>
     * <p>
     * return interestingShops[temp];
     * }
     * <p>
     * //int[] goal = {goalX, goalY};
     * return -1;
     * }
     */

    public int[] movePerson(int direction, int currentX, int currentY) {

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

    /**
     * public int findDirectionWithGoal(int goalX, int goalY, int currentX, int currentY) {
     * <p>
     * <p>
     * int temp = (int) random.nextInt(2);
     * System.out.println("temp" + temp + "goal" + goalX + " " + goalY + "current" + currentX + " " + currentY);
     * int nextDirection = 4;
     * <p>
     * if (detourNeeded != 4) {
     * if ((detourNeeded == 1 || detourNeeded == 3) && (simulationHandler.crashMap[Math.abs(currentY) - 1][Math.abs(currentX)] != 10) && (simulationHandler.crashMap[Math.abs(currentY) + 1][Math.abs(currentX)] != 10)) {
     * detourNeeded = 4;
     * }
     * if ((detourNeeded == 0 || detourNeeded == 2) && (simulationHandler.crashMap[Math.abs(currentY)][Math.abs(currentX) - 1] != 10) && (simulationHandler.crashMap[Math.abs(currentY)][Math.abs(currentX) + 1] != 10)) {
     * detourNeeded = 4;
     * }
     * return detourNeeded;
     * }
     * <p>
     * System.out.println(detourX);
     * System.out.println(detourY);
     * <p>
     * if (detourX) {
     * if (currentY == goalY) {
     * if (goalX > currentX) {
     * detourNeeded = 0;
     * } else {
     * detourNeeded = 2;
     * }
     * } else {
     * if ((simulationHandler.crashMap[Math.abs(currentY) + 1][Math.abs(currentX)] != 10) && (simulationHandler.crashMap[Math.abs(currentY) - 1][Math.abs(currentX)] != 10)) {
     * detourX = false;
     * }
     * }
     * } else if (detourY) {
     * if (currentX == goalX) {
     * if (goalY > currentY) {
     * detourNeeded = 1;
     * } else {
     * detourNeeded = 3;
     * }
     * } else {
     * temp = 0;
     * }
     * if ((simulationHandler.crashMap[Math.abs(currentY)][Math.abs(currentX) + 1] != 10) && (simulationHandler.crashMap[Math.abs(currentY)][Math.abs(currentX) - 1] != 10)) {
     * detourY = false;
     * }
     * }
     * <p>
     * if (temp == 0) {
     * if (goalX < currentX) {
     * //move left
     * nextDirection = 2;
     * <p>
     * if (simulationHandler.crashMap[Math.abs(currentY)][movePerson(2, Math.abs(currentX), Math.abs(currentY))[0]] == 10) {
     * detourX = true;
     * }
     * <p>
     * } else if (goalX > currentX) {
     * //move right
     * if (simulationHandler.crashMap[Math.abs(currentY)][movePerson(0, Math.abs(currentX), Math.abs(currentY))[0]] == 10) {
     * detourX = true;
     * }
     * nextDirection = 0;
     * } else if ((goalX == currentX) && detourY) {
     * }
     * } else {
     * if (goalY > currentY) {
     * //move down
     * if (simulationHandler.crashMap[movePerson(1, Math.abs(currentX), Math.abs(currentY))[1]][Math.abs(currentX)] == 10) {
     * detourY = true;
     * }
     * nextDirection = 1;
     * } else if (goalY < currentY) {
     * //move up
     * nextDirection = 3;
     * if (simulationHandler.crashMap[movePerson(3, Math.abs(currentX), Math.abs(currentY))[1]][Math.abs(currentX)] == 10) {
     * detourY = true;
     * }
     * }
     * }
     * <p>
     * // simulationHandler.crashMap[movePerson(nextDirection[temp], currentX, currentY)[0]][movePerson(nextDirection[temp], currentX, currentY)[1]]
     * <p>
     * return nextDirection;
     * }
     */
    public int findDirectionWithHeatMap(int currentX, int currentY) {
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
            System.out.println("Strange");
            return ThreadLocalRandom.current().nextInt(0, 3);
        } else {
            return direction[(int) random.nextInt(temp)];
        }
    }
    /*
    public int findDirectionWithHeatMap(int currentX, int currentY) {
        TreeSet directionTreeSet = new TreeSet();

        int direction[] = new int[4];
        int temp = 0;
        int nextValueHeatMap;

        nextValueHeatMap = currentGoalStore.getHeatMapValue(currentX, currentY) - 1;

        if ( currentGoalStore.getHeatMapValue(currentX+1, currentY) == nextValueHeatMap) {
            //move right
            //direction[temp] = 0;
            //temp++;
            directionTreeSet.add(0);


        }
        if (currentGoalStore.getHeatMapValue(currentX-1, currentY) == nextValueHeatMap) {
            //move left
            directionTreeSet.add(2);
            //direction[temp] = 2;
            //temp++;
        }
        if (currentGoalStore.getHeatMapValue(currentX, currentY+1) == nextValueHeatMap) {
            //move down
            directionTreeSet.add(1);
            //direction[temp] = 1;
            //temp++;
        }
        if (currentGoalStore.getHeatMapValue(currentX, currentY-1) == nextValueHeatMap) {
            //move up
            directionTreeSet.add(3);
            //direction[temp] = 3;
            //temp++;
        }
        if (directionTreeSet.size() == 0) {
            System.out.println("Strange");
            return ThreadLocalRandom.current().nextInt(0, 3);
        } else {
            return  random.nextInt(directionTreeSet.size());

        }
    }
    */

    /**
     * Gets speed of Person
     *
     * @return
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets speed of person
     *
     * @param speed
     */
    public void setSpeed(double speed) {
        this.speed=speed;
    }

    /**
     * Gets position of person
     *
     * @return
     */
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Sets current position
     *
     * @param currentPosition
     */
    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition=currentPosition;

    }

    public boolean isGoalMallDoor() {
        return isGoalMallDoor;
    }

    public Store getCurrentGoalStore() {
        return currentGoalStore;
    }

    public void setCurrentGoalStore(Store currentGoalStore) {
        this.currentGoalStore=currentGoalStore;
    }
}