package com.consultsim.mallsim.View;


import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.Store;

import java.util.ArrayList;

public class SimulationHandler{

    ArrayList<Person> listOfPersons;
    ArrayList<Store> listOfStores;
    ArrayList<Object> listOfObjects;

    public SimulationHandler() {

    }


    public void computeNextPositionOfPersons(){

    }

    public void addPersons(){

    }

    public void addStore(){

    }

    public void addObject(){

    }

    public ArrayList<Person> getListOfPersons() {
        return listOfPersons;
    }

    public void setListOfPersons(ArrayList<Person> listOfPersons) {
        this.listOfPersons = listOfPersons;
    }

    public ArrayList<Store> getListOfStores() {
        return listOfStores;
    }

    public void setListOfStores(ArrayList<Store> listOfStores) {
        this.listOfStores = listOfStores;
    }

    public ArrayList<Object> getListOfObjects() {
        return listOfObjects;
    }

    public void setListOfObjects(ArrayList<Object> listOfObjects) {
        this.listOfObjects = listOfObjects;
    }
}
