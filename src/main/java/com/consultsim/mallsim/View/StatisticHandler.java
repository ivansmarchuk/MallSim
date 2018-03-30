package com.consultsim.mallsim.View;

public class StatisticHandler {

    public static void main(String[] args) {
        int[][][] y = {{{1,1,0},{1,1,2}},{{1,2,1},{1,1,0}}};
        int out = countHotColdSpots(y)[0];
        int out2 = countHotColdSpots(y)[1];
        System.out.println(out);
        System.out.println(out2);
    }

    
// 0 = kein spot; 1 = HotSpot; 2 = ColdSpot
    public static int[] countHotColdSpots(int[][][] spotArray){
        int hotSpots = 0;
        int coldSpots = 0;
        for (int[][] x : spotArray){
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
