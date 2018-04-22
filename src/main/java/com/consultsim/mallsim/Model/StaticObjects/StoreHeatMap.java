package com.consultsim.mallsim.Model.StaticObjects;

import com.consultsim.mallsim.Model.Store;

import java.util.concurrent.Callable;

public class StoreHeatMap implements  Runnable{

    private int[][] crashMap;
    private Store store;

    @Override
    public void run() {


        //System.out.println("PROCESSING PERSONAL_DATA : calling back-end method");

        //System.out.println("PROCESSING PERSONAL_DATA : received data successfully");
        store.generateHeatMap(crashMap);
        System.out.println("Heatmap done");

    }

    public void setCrashMap(int[][] crashMap){
        this.crashMap = crashMap;
    }

    public void setStore(Store store){
        this.store = store;
    }

}
