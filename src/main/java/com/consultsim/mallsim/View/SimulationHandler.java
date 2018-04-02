package com.consultsim.mallsim.View;


import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.Model.Store;

import java.util.ArrayList;
import java.util.Random;

public class SimulationHandler{

    ArrayList<Person> arrayOfPersons;
    ArrayList<Store> arrayOfStores;
    ArrayList<Objects> arrayOfObjects;
    public StatisticHandler stat;
    public static int crashMap[][];

//Initialize values
    public SimulationHandler() {
        stat = new StatisticHandler();
        arrayOfPersons = new ArrayList<Person>();
        arrayOfStores = new ArrayList<Store>();
        arrayOfObjects = new ArrayList<Objects>();
        crashMap = new int[1000][1000];
        for(int i = 0; i < 1000; i++){
            for(int a = 0; a < 1000; a++){
                crashMap[i][a] = 0;
            }
        }
        fillCrashMapWithStoresAndObjects();
    }


    public void fillCrashMapWithStoresAndObjects(){
            int xPosLeftDown= 0;
            int yPosLeftDown = 0;
            int xPosUpRight = 0;
            int yPosUpRight = 0;

            for(int i = 0; i < 100; i++){
                for(int a = 0; a < 100; a++){
                    crashMap[i][a] = 10;
                }
            }

            for(Store s : arrayOfStores){
                xPosLeftDown = s.getPosition()[0];
                yPosLeftDown = s.getPosition()[1];
                xPosUpRight = s.getPosition()[2];
                yPosUpRight = s.getPosition()[3];

                for(int y = 1000-yPosUpRight; y < 1000-yPosLeftDown; y++){
                    for(int x = xPosLeftDown; x < xPosUpRight; x++){
                        crashMap[y][x] = 10;
                    }
                }
            }


            for(Objects s : arrayOfObjects){
                xPosLeftDown = s.getPosition()[0];
                yPosLeftDown = s.getPosition()[1];
                xPosUpRight = s.getPosition()[2];
                yPosUpRight = s.getPosition()[3];

                for(int y = 1000-yPosUpRight; y < 1000-yPosLeftDown; y++){
                    for(int x = xPosLeftDown; x < xPosUpRight; x++){
                        crashMap[y][x] = 10;
                    }
                }
            }
    }
    //Test function, adds specific amount of persons
    public void initializePersons(){
        fillCrashMapWithStoresAndObjects();
        Random random = new Random();
        int x;
        int y;


        for (int i = 0; i < 5; i++) {
            x = random.nextInt(1000);
            y = random.nextInt(1000);
            if(crashMap[y][x] != 10){
                arrayOfPersons.add(new Person(new Position(x,y), 0.0, this));
                //System.out.println("x: " + x + " y: " + y);
                crashMap[y][x] = 1;
            }

        }

        int m[][] = stat.recognizeHCSpots(1000,1000, 100, 100, arrayOfPersons);
        stat.createSpotObjects(1000,1000,100,100, m);
    }

    //deletes content of hotcoldspots and computes them new, so only a momentary picture is shown
    public void clearEverything(){
        //this.arrayOfPersons.clear();
        this.stat.hotColdSpots.clear();


        int m[][] = stat.recognizeHCSpots(1000,1000, 100, 100, arrayOfPersons);
        this.stat.createSpotObjects(1000,1000,100,100, m);
}


    public void computeNextPositionOfPersons(){
        for(Person p: arrayOfPersons){
            p.computeNext();
        }
    }

    public void addPersons(){

    }

    public void addStore(){

    }

    public void addObject(){

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
}
