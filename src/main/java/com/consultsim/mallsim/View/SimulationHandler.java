package com.consultsim.mallsim.View;


import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.Model.StaticObjects.Mall;
import com.consultsim.mallsim.Model.Store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class SimulationHandler {

    public int crashMap[][];
    private static SimulationHandler simulationInstance;
    public StatisticHandler statisticHandler;
    private double dayTimeInMinutes = 540;
    double randomNum = 1.0;
    public ArrayList<Person> arrayOfPersons;
    public ArrayList<Store> arrayOfStores;
    private Store goalStore;

    public ArrayList<Objects> arrayOfObjects;
    private int countPersons = 0;
    private Mall mall;
    private int countOfPersons = 0;

    //Initialize values
    public SimulationHandler() {

        statisticHandler = StatisticHandler.getStatisticInstance();
        mall = Mall.getMallInstance();

        arrayOfPersons = new ArrayList<Person>();
        arrayOfStores = new ArrayList<Store>();
        arrayOfObjects = new ArrayList<Objects>();
        crashMap = new int[1000][1000];

        //fillCrashMapWithStoresAndObjects();
    }

    /**
     * Singleton pattern
     *
     * @return only one instance
     */
    public static SimulationHandler getSimulationInstance() {
        if (simulationInstance == null) {
            simulationInstance = new SimulationHandler();
        }
        return simulationInstance;
    }

    /**
     * Getter of setter
     * @return
     */
    public int getCountOfPersons() {
        return countOfPersons;
    }

    /**
     * Getter of setter
     * @param countOfPersons
     */
    public void setCountOfPersons(int countOfPersons) {
        this.countOfPersons = countOfPersons;
    }

    /**
     * inserts the stores and objects people cannot walk through into the crashmap (sets values to)
     */
    public void fillCrashMapWithStoresAndObjects() {
        int xPosLeftUpper;
        int yPosLeftUpper;
        int xPosDownRight;
        int yPosDownRight;


        /**
         * example:
         * 0 0 0 0 0 0
         * 0 x x x x 0
         * 0 x 0 0 x 0
         * 0 x 0 0 x 0
         * 0 x 0 x x 0
         * 0 0 0 0 0 0
         * where x - wall, 0- free place
         *
         */
        for (Store s : arrayOfStores) {
            xPosLeftUpper = s.getPosition()[0];
            yPosLeftUpper = s.getPosition()[1];
            xPosDownRight = s.getPosition()[2];
            yPosDownRight = s.getPosition()[3];

            //fill crashmap only an border with 10
            for (int y = yPosLeftUpper; y < yPosDownRight; y++) {
                for (int x = xPosLeftUpper; x < xPosDownRight; x++) {

                    if (y == yPosLeftUpper || y == yPosDownRight - 1) {
                        crashMap[y][x] = 10;
                    } else if (x == xPosLeftUpper || x == xPosDownRight - 1) {
                        crashMap[y][x] = 10;
                    }

                    //crashMap[y][x] = 10;

                }
            }
            // clear a border where is a door

            if (s.getDoorPosition()[1] == s.getDoorPosition()[3]) {
                if (s.getDoorPosition()[1] == s.getPosition()[1]) {
                    for (int x = s.getDoorPosition()[0]; x < s.getDoorPosition()[2]; x++) {
                        crashMap[s.getDoorPosition()[1]][x] = 1;
                    }
                } else {
                    for (int x = s.getDoorPosition()[0]; x < s.getDoorPosition()[2]; x++) {
                        crashMap[s.getDoorPosition()[1] - 1][x] = 1;
                    }
                }
            } else {
                if (s.getDoorPosition()[0] == s.getPosition()[0]) {
                    for (int y = s.getDoorPosition()[1]; y < s.getDoorPosition()[3]; y++) {
                        crashMap[y][s.getDoorPosition()[0]] = 1;
                    }
                } else {
                    for (int y = s.getDoorPosition()[1]; y < s.getDoorPosition()[3]; y++) {
                        crashMap[y][s.getDoorPosition()[0] - 1] = 1;
                    }
                }
            }


        }


        for (Objects s : arrayOfObjects) {
            xPosLeftUpper = s.getPosition()[0];
            yPosLeftUpper = s.getPosition()[1];
            xPosDownRight = s.getPosition()[2];
            yPosDownRight = s.getPosition()[3];

            for (int y = yPosLeftUpper; y < yPosDownRight; y++) {
                for (int x = xPosLeftUpper; x < xPosDownRight; x++) {
                    crashMap[y][x] = 10;
                }
            }
        }
    }

    /**
     * Test function, adds specific amount of persons
     */
    public void initializePersons() {

        //fillCrashMapWithStoresAndObjects();

        Random random = new Random();

        /*
        for (int i = 0; i < 1; i++) {
            x = random.nextInt(1000);
            y = random.nextInt(1000);
            if(crashMap[y][x] != 10){
                arrayOfPersons.add(new Person(new Position(x,y), 0.0, this));
                //System.out.println("x: " + x + " y: " + y);
                crashMap[y][x] = 1;
            }
        }
*/
        int m[][] = statisticHandler.recognizeHCSpots(1000, 1000, 100, 100, arrayOfPersons);
        statisticHandler.createSpotObjects(1000, 1000, 100, 100, m);
    }

    /**
     * deletes content of hotcoldspots and computes them new, so only a momentary picture is shown
     */
    public void clearEverything() {
        //this.arrayOfPersons.clear();
        this.statisticHandler.hotColdSpots.clear();


        int m[][] = statisticHandler.recognizeHCSpots(1000, 1000, 100, 100, arrayOfPersons);
        this.statisticHandler.createSpotObjects(1000, 1000, 100, 100, m);
    }

    /**
     * computes next position of people
     */
    public void computeNextPositionOfPersons() {
        for (Person p : arrayOfPersons) {
            if (p.getGoalStore()== null){
                goalStore = getRandomItem(arrayOfStores);
                p.setGoalStore(goalStore);

            }
           p.computeNextStep();
        }

    }


    //Empty methods, can be deleted at the end
    public void addPersons() {

    }

    public void addStore() {

    }

    public void addObject() {

    }

    /**
     * Getter or Setter
     * @return
     */
    public ArrayList<Person> getArrayOfPersons() {
        return arrayOfPersons;
    }

    /**
     * Getter or Setter
     * @param arrayOfPersons
     */
    public void setArrayOfPersons(ArrayList<Person> arrayOfPersons) {
        this.arrayOfPersons = arrayOfPersons;
    }

    /**
     * Getter or Setter
     * @return
     */
    public ArrayList<Store> getArrayOfStores() {
        return arrayOfStores;
    }

    /**
     * Getter or Setter
     * @param arrayOfStores
     */
    public void setArrayOfStores(ArrayList<Store> arrayOfStores) {
        this.arrayOfStores = arrayOfStores;
    }

    /**
     * Getter or Setter
     * @return
     */
    public ArrayList<Objects> getArrayOfObjects() {
        return arrayOfObjects;
    }

    /**
     * Getter or Setter
     * @param arrayOfObjects
     */
    public void setArrayOfObjects(ArrayList<Objects> arrayOfObjects) {
        this.arrayOfObjects = arrayOfObjects;
    }


    /**
     * Generation of new people depending on settings and day time
     *
     * @param numberOfPerson amount of person that should be generated
     * @param dayTime        the period of day time
     */
    public void generatePerson(double numberOfPerson, double dayTime) {

        int minX = mall.getDoorLeftUpper().getX();
        int maxX = mall.getDoorDownRight().getX();
        int minY = mall.getDoorLeftUpper().getY();
        int maxY = mall.getDoorDownRight().getY();

        Random rand = new Random();
        //System.out.println("DayTime: " + Math.round(dayTime)/60);
        if (Math.round(dayTime) / 60 - dayTimeInMinutes > randomNum) {
            for (int i = 0; i < (int) numberOfPerson; i++) {
                int x = rand.nextInt((maxX - minX) + 1) + minX;
                int y = rand.nextInt((maxY - minY) + 1) + minY;

                goalStore = getRandomItem(arrayOfStores);



                //Person generatedPerson = new Person(new Position(x, y), 10, simulationInstance, goalStore);

              //  Person generatedPerson = new Person(x, y, rand.nextFloat()*10, rand.nextFloat()*10);
              //  goalStore.getPersonsList().add(generatedPerson);
               // goalStore.setPersonsListToHeap();

                //System.out.println(goalStore.getLabel());

                //goalStore.getHeatMap().getPersonsList().add(generatedPerson);

                //arrayOfPersons.add(generatedPerson);

                statisticHandler.setCountOfPersons(countPersons++);
            }
            randomNum = ThreadLocalRandom.current().nextInt(1, 10);
            dayTimeInMinutes = Math.round(dayTime) / 60;
        }

    }

    /**
     * get a rantom element from any lists
     *
     * @param list list where should be found a random element
     * @param <T>  type of list
     * @return random element from a list
     */
    private <T> T getRandomItem(List<T> list) {
        Random random = new Random();
        int listSize = list.size();
        int randomIndex = random.nextInt(listSize);
        return list.get(randomIndex);
    }
}