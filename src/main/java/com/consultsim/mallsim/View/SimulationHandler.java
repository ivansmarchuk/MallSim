package com.consultsim.mallsim.View;


import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.Model.StaticObjects.Mall;
import com.consultsim.mallsim.Model.Store;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class SimulationHandler {

    double dayTimeInMinutes = 540;
    double randomNum =  1.0;
    private int countPersons = 0;
    private Mall mall;

    public static int crashMap[][];
    public StatisticHandler statisticHandler;
    ArrayList<Person> arrayOfPersons;
    ArrayList<Store> arrayOfStores;
    ArrayList<Objects> arrayOfObjects;
    private static SimulationHandler simulationInstance = null;

    public int getCountOfPersons() {
        return countOfPersons;
    }

    public void setCountOfPersons(int countOfPersons) {
        this.countOfPersons = countOfPersons;
    }

    private int countOfPersons = 0;

    //Initialize values
    public SimulationHandler() {

        statisticHandler = StatisticHandler.getStatisticInstance();
        mall = Mall.getMallInstance();

        arrayOfPersons = new ArrayList<Person>();
        arrayOfStores = new ArrayList<Store>();
        arrayOfObjects = new ArrayList<Objects>();
        crashMap = new int[1000][1000];
        for (int i = 0; i < 1000; i++) {
            for (int a = 0; a < 1000; a++) {
                crashMap[i][a] = 0;
            }
        }
        fillCrashMapWithStoresAndObjects();
    }

    /**
     *Singleton pattern
     * @return only one instance
     */
    public static SimulationHandler getSimulationInstance() {
        if (simulationInstance == null) {
            simulationInstance = new SimulationHandler();
        }
        return simulationInstance;
    }


    public void fillCrashMapWithStoresAndObjects() {
        int xPosLeftUpper = 0;
        int yPosLeftUpper = 0;
        int xPosDownRight = 0;
        int yPosDownRight = 0;


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
                    if (y == yPosLeftUpper || y == yPosDownRight -1) {
                        crashMap[y][x] = 10;
                    }
                    else if (x==xPosLeftUpper || x == xPosDownRight -1){
                        crashMap[y][x] = 10;
                    }
                }
            }
            // clear a border where is a door
            if(s.getDoorPosition()[1] == s.getDoorPosition()[3]){
                if (s.getDoorPosition()[1] == s.getPosition()[1]){
                    for (int x = s.getDoorPosition()[0]; x < s.getDoorPosition()[2]; x++) {
                        crashMap[s.getDoorPosition()[1]][x] = 1;
                    }
                }else {
                    for (int x = s.getDoorPosition()[0]; x < s.getDoorPosition()[2]; x++) {
                        crashMap[s.getDoorPosition()[1]-1][x] = 1;
                    }
                }
            }else{
                if (s.getDoorPosition()[0] == s.getPosition()[0]){
                    for (int y = s.getDoorPosition()[1]; y < s.getDoorPosition()[3]; y++) {
                        crashMap[y][s.getDoorPosition()[0]] = 1;
                    }
                }else {
                    for (int y = s.getDoorPosition()[1]; y < s.getDoorPosition()[3]; y++) {
                        crashMap[y][s.getDoorPosition()[0]-1] = 1;
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

    //Test function, adds specific amount of persons
    public void initializePersons() {
        fillCrashMapWithStoresAndObjects();
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

    //deletes content of hotcoldspots and computes them new, so only a momentary picture is shown
    public void clearEverything() {
        //this.arrayOfPersons.clear();
        this.statisticHandler.hotColdSpots.clear();


        int m[][] = statisticHandler.recognizeHCSpots(1000, 1000, 100, 100, arrayOfPersons);
        this.statisticHandler.createSpotObjects(1000, 1000, 100, 100, m);
    }


    public void computeNextPositionOfPersons() {
        for (Person p : arrayOfPersons) {

            p.computeNext();
        }

    }

    public void addPersons() {

    }

    public void addStore() {

    }

    public void addObject() {

    }

    public ArrayList<Person> getArrayOfPersons() {
        return arrayOfPersons;
    }

    public void setArrayOfPersons(ArrayList<Person> arrayOfPersons) {
        this.arrayOfPersons = arrayOfPersons;
    }

    public ArrayList<Store> getArrayOfStores() {
        return arrayOfStores;
    }

    public void setArrayOfStores(ArrayList<Store> arrayOfStores) {
        this.arrayOfStores = arrayOfStores;
    }

    public ArrayList<Objects> getArrayOfObjects() {
        return arrayOfObjects;
    }

    public void setArrayOfObjects(ArrayList<Objects> arrayOfObjects) {
        this.arrayOfObjects = arrayOfObjects;
    }

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

                arrayOfPersons.add(new Person(new Position(x, y), 10, SimulationHandler.getSimulationInstance()));
                statisticHandler.setCountOfPersons(countPersons++);
            }

            randomNum = ThreadLocalRandom.current().nextInt(1, 10);
            //arrayOfPerson.remove(arrayOfPerson.size()-1);
            dayTimeInMinutes = Math.round(dayTime) / 60;
        }

    }
}