package com.consultsim.mallsim.Model.StaticObjects;


import com.consultsim.mallsim.Model.Store;

import java.util.LinkedList;
import java.util.Vector;

public class EntranceDoor extends Store {

    private int doorPosition[];
    public EntranceDoor(int[] doorPosition){
        this.doorPosition = doorPosition;
    }

    @Override
    public int[] getPosition() {
        return doorPosition;
    }


}