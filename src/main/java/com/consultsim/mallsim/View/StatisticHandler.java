package com.consultsim.mallsim.View;

import java.util.ArrayList;

public class StatisticHandler {

    public static void main(String[] args) {
        int[][][] y = {{{1,1,0},{1,1,2}},{{1,2,1},{1,1,0}}};
        int out = countHotColdSpots(y)[0];
        int out2 = countHotColdSpots(y)[1];
        System.out.println(out);
        System.out.println(out2);
    }


    static ArrayList<int[][]> spotArrayList = new ArrayList<int[][]>();
    //die Funktion updateHotColdSpots wird während des Schleifendurchlaufs im Simulationhandler aufgerufen und liefert Momentaufnahmen der Hot/Coldspots
    public static void updateHotColdSpots(int[][] spotArray ){
        spotArrayList.add(spotArray);

    }



    // 0 = kein Spot; 1 = HotSpot; 2 = ColdSpot
    // Wird am Ende der Simulation aufgerufen
    public static int[] countHotColdSpots(int[][][] spotArrayList){
        int hotSpots = 0;
        int coldSpots = 0;
        for (int[][] x : spotArrayList){
            for (int[] y : x){
                for (int z : y){
                    switch(z){
                        case 1: hotSpots += 1;
                                break;
                        case 2: coldSpots += 1;
                                break;
                    }

                   /* if(z == 1){
                        hotSpots += 1;
                    }
                    else if( z == 2){
                        coldSpots += 1;
                    }*/

                }
            }
            
            
        }
        int[] klrf = {hotSpots,coldSpots};
        return klrf;
    }

}
