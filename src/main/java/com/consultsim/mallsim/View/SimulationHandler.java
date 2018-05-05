package com.consultsim.mallsim.View;


import com.consultsim.mallsim.Model.Configuration;
import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.Model.StaticObjects.EntranceDoor;
import com.consultsim.mallsim.Model.StaticObjects.Mall;
import com.consultsim.mallsim.Model.Store;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class SimulationHandler {

    private static SimulationHandler simulationInstance;
    public int crashMap[][];
    StatisticHandler statisticHandler;
    ArrayList<Person> arrayOfPersons;
    ArrayList<Store> arrayOfStores;
    private EntranceDoor entranceDoor;
    ArrayList<Objects> arrayOfObjects;
    private double dayTimeInMinutes=540;
    private int countPersons=0;
    private Mall mall;
    private int countOfPersons=0;

    //Initialize values
    private SimulationHandler() {

        statisticHandler=StatisticHandler.getStatisticInstance();
        mall=Mall.getMallInstance();

        arrayOfPersons=new ArrayList<>();
        arrayOfStores=new ArrayList<>();
        arrayOfObjects=new ArrayList<>();
        crashMap=new int[1000][1000];

        //fillCrashMapWithStoresAndObjects();
    }

    /**
     * Singleton pattern
     *
     * @return only one instance
     */
    static SimulationHandler getSimulationInstance() {
        if (simulationInstance == null) {
            simulationInstance=new SimulationHandler();
        }
        return simulationInstance;
    }

    /**
     * Getter of setter
     *
     * @return
     */
    public int getCountOfPersons() {
        return countOfPersons;
    }

    /**
     * Getter of setter
     *
     * @param countOfPersons
     */
    public void setCountOfPersons(int countOfPersons) {
        this.countOfPersons=countOfPersons;
    }

    /**
     * inserts the stores and objects people cannot walk through into the crashmap (sets values to)
     */
    void fillCrashMapWithStoresAndObjects() {
        int xPosLeftUpper;
        int yPosLeftUpper;
        int xPosDownRight;
        int yPosDownRight;


        /*
          example:
          0 0 0 0 0 0
          0 x x x x 0
          0 x 0 0 x 0
          0 x 0 0 x 0
          0 x 0 x x 0
          0 0 0 0 0 0
          where x - wall, 0- free place

         */
        for (Store s : arrayOfStores) {
            xPosLeftUpper=s.getPosition()[0];
            yPosLeftUpper=s.getPosition()[1];
            xPosDownRight=s.getPosition()[2];
            yPosDownRight=s.getPosition()[3];

            //fill crashmap only an border with 10
            for (int y=yPosLeftUpper; y < yPosDownRight; y++) {
                for (int x=xPosLeftUpper; x < xPosDownRight; x++) {


                    if (y == yPosLeftUpper || y == yPosDownRight - 1) {
                        crashMap[y][x]=10;

                        if (y - 1 != -1 && y + 1 != 1000) {
                            crashMap[y - 1][x]=10;
                            crashMap[y + 1][x]=10;
                        }
                    } else if (x == xPosLeftUpper || x == xPosDownRight - 1) {
                        if (x - 1 != -1 && x + 1 != 1000) {
                            crashMap[y][x - 1]=10;
                            crashMap[y][x + 1]=10;
                        }
                        crashMap[y][x]=10;

                    }

                    //crashMap[y][x] = 10;

                }
            }
            // clear a border where is a door

            if (s.getDoorPosition()[1] == s.getDoorPosition()[3]) {
                if (s.getDoorPosition()[1] == s.getPosition()[1]) {
                    for (int x=s.getDoorPosition()[0]; x < s.getDoorPosition()[2]; x++) {
                        crashMap[s.getDoorPosition()[1] - 1][x]=1;
                        crashMap[s.getDoorPosition()[1]][x]=1;
                        crashMap[s.getDoorPosition()[1] + 1][x]=1;


                    }
                } else {
                    for (int x=s.getDoorPosition()[0]; x < s.getDoorPosition()[2]; x++) {
                        crashMap[s.getDoorPosition()[1] - 2][x]=1;
                        crashMap[s.getDoorPosition()[1] - 1][x]=1;
                        crashMap[s.getDoorPosition()[1]][x]=1;
                    }
                }
            } else {
                if (s.getDoorPosition()[0] == s.getPosition()[0]) {
                    for (int y=s.getDoorPosition()[1]; y < s.getDoorPosition()[3]; y++) {
                        crashMap[y][s.getDoorPosition()[0] - 1]=1;
                        crashMap[y][s.getDoorPosition()[0]]=1;
                        crashMap[y][s.getDoorPosition()[0] + 1]=1;
                    }
                } else {
                    for (int y=s.getDoorPosition()[1]; y < s.getDoorPosition()[3]; y++) {
                        crashMap[y][s.getDoorPosition()[0] - 2]=1;
                        crashMap[y][s.getDoorPosition()[0] - 1]=1;
                        crashMap[y][s.getDoorPosition()[0]]=1;
                    }
                }
            }


        }


        for (Objects s : arrayOfObjects) {
            xPosLeftUpper=s.getPosition()[0];
            yPosLeftUpper=s.getPosition()[1];
            xPosDownRight=s.getPosition()[2];
            yPosDownRight=s.getPosition()[3];

            for (int y=yPosLeftUpper; y < yPosDownRight; y++) {
                for (int x=xPosLeftUpper; x < xPosDownRight; x++) {
                    crashMap[y][x]=10;
                }
            }
        }
    }

    /**
     * Test function, adds specific amount of persons
     */
    void initializePersons() {
        int m[][]=statisticHandler.recognizeHCSpots(1000, 1000, 100, 100, arrayOfPersons);
        statisticHandler.createSpotObjects(1000, 1000, 100, 100, m);
    }

    /**
     * deletes content of hotcoldspots and computes them new, so only a momentary picture is shown
     */
    void clearEverything() {
        //this.arrayOfPersons.clear();
        this.statisticHandler.hotColdSpots.clear();


        int m[][]=statisticHandler.recognizeHCSpots(1000, 1000, 100, 100, arrayOfPersons);
        this.statisticHandler.createSpotObjects(1000, 1000, 100, 100, m);
    }

    /**
     * computes next position of people
     */
    void computeNextPositionOfPersons(double dayTime) {
        ListIterator<Person> iter=arrayOfPersons.listIterator(arrayOfPersons.size());
        while (iter.hasPrevious()) {
            Person person=iter.previous();
            if (person.isGoalMallDoor())
                iter.remove();
            else {
                if (Math.round(dayTime) / 60 > Configuration.TIME_OUT) {
                    if (!person.getCurrentPosition().equals(entranceDoor))
                        person.setCurrentGoalStore(entranceDoor);
                }
                person.computeNext();
            }
        }


    }

    /**
     * Getter or Setter
     *
     * @return array of person
     */
    public ArrayList<Person> getArrayOfPersons() {
        return arrayOfPersons;
    }

    /**
     * Getter or Setter
     *
     * @param arrayOfPersons array of persons
     */
    public void setArrayOfPersons(ArrayList<Person> arrayOfPersons) {
        this.arrayOfPersons=arrayOfPersons;
    }

    /**
     * Getter or Setter
     *
     * @return
     */
    public ArrayList<Store> getArrayOfStores() {
        return arrayOfStores;
    }

    /**
     * Getter or Setter
     *
     * @param arrayOfStores
     */
    void setArrayOfStores(ArrayList<Store> arrayOfStores) {
        this.arrayOfStores=arrayOfStores;
    }

    /**
     * Getter or Setter
     *
     * @return
     */
    public ArrayList<Objects> getArrayOfObjects() {
        return arrayOfObjects;
    }

    /**
     * Getter or Setter
     *
     * @param arrayOfObjects
     */
    void setArrayOfObjects(ArrayList<Objects> arrayOfObjects) {
        this.arrayOfObjects=arrayOfObjects;
    }

    /**
     * Generation of new people depending on settings and day time
     *
     * @param numberOfPerson amount of person that should be generated
     * @param dayTime        the period of day time
     */
    void generatePerson(double numberOfPerson, double dayTime) {
        int randTime;
        int minX=mall.getDoorLeftUpper().getX()-100;
        int maxX=mall.getDoorDownRight().getX()-100;
        int minY=mall.getDoorLeftUpper().getY();
        int maxY=mall.getDoorDownRight().getY();

        //System.out.println("DayTime: " + Math.round(dayTime)/60);
        if (numberOfPerson > 5) {
            randTime=ThreadLocalRandom.current().nextInt(5, 20);
        } else {
            randTime=ThreadLocalRandom.current().nextInt(1, 10);
        }
        if (Math.round(dayTime) / 60 - dayTimeInMinutes > randTime && dayTimeInMinutes < 1080) {

            for (int i=0; i < (int) numberOfPerson; i++) {
                int x=ThreadLocalRandom.current().nextInt(minX, maxX);
                int y=ThreadLocalRandom.current().nextInt(minY, maxY);
                //random number from 0 to 4
                int nrGoalStores=(int) (Math.random() * 6);
                Store[] goalStores=new Store[6];
                ArrayList<Store> goalStoresList=new ArrayList<>();
                //System.out.println(nrGoalStores);
                for (int m=0; m <= nrGoalStores; m++) {
                    //goalStores[m] = getRandomItem(arrayOfStores);
                    goalStoresList.add(getRandomItem(arrayOfStores));
                    //System.out.println(goalStore.getLabel());
                }
                if (countPersons < 951) {
                    goalStoresList.add(entranceDoor);
                    //arrayOfPersons.add(new Person(new Position(x, y), 10, simulationInstance, goalStores));
                    arrayOfPersons.add(new Person(new Position(x, y), 10, simulationInstance, goalStoresList));
                    statisticHandler.setCountOfPersons(countPersons++);
                }
                //System.out.println(goalStores[0] + " " + goalStores[1] + " " + goalStores[2] + " " + goalStores[3] + " " + goalStores[4] + " " + goalStores[5]);
            }
            dayTimeInMinutes =Math.round(dayTime) / 60;
        }

    }

    public EntranceDoor getEntranceDoor() {
        return entranceDoor;
    }

    void setEntranceDoor(EntranceDoor entranceDoor) {
        this.entranceDoor=entranceDoor;
    }

    /**
     * get a rantom element from any lists
     *
     * @param list list where should be found a random element
     * @param <T>  type of list
     * @return random element from a list
     */
    private <T> T getRandomItem(List<T> list) {
        Random random=new Random();
        int listSize=list.size();
        int randomIndex=random.nextInt(listSize);
        return list.get(randomIndex);
    }

    public void setDayTimeInMinutes(double dayTimeInMinutes) {
        this.dayTimeInMinutes=dayTimeInMinutes;
    }
}