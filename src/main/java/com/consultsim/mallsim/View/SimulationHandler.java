package com.consultsim.mallsim.View;


import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.Model.Store;

import java.util.ArrayList;
import java.util.Random;

public class SimulationHandler{

    ArrayList<Person> arrayOfPersons;
    ArrayList<Store> arrayOfStores;
    ArrayList<Object> arrayOfObjects;
    public StatisticHandler stat;

    public SimulationHandler() {
        stat = new StatisticHandler();
        arrayOfPersons = new ArrayList<Person>();
        arrayOfStores = new ArrayList<Store>();
        arrayOfObjects = new ArrayList<Object>();
    }

    public void initializePersons(){
        Random random = new Random();
        int x;
        int y;
        
        for (int i = 0; i < 400; i++) {
            x = random.nextInt(1000) + 1;
            y = random.nextInt(1000) + 1;

            arrayOfPersons.add(new Person(new Position(x,y), 0.0));
            System.out.println("x: " + x + " y: " + y);
        }

        int m[][] = stat.recognizeHCSpots(1000,1000, 100, 100, arrayOfPersons);
        stat.createSpotObjects(1000,1000,100,100, m);
    }

    public void clearEverything(){
        this.arrayOfPersons.clear();
        this.stat.hotColdSpots.clear();
    }


    public void computeNextPositionOfPersons(){

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

    public ArrayList<Object> getArrayOfObjects() {
        return arrayOfObjects;
    }

    public void setArrayOfObjects(ArrayList<Object> arrayOfObjects) {
        this.arrayOfObjects = arrayOfObjects;
    }
}
