package com.consultsim.mallsim.Model.StaticObjects;


import com.consultsim.mallsim.Model.Store;

public class EntranceDoor extends Store {

    private int doorPosition[];
    private int[][] HeatMap  = new int[1002][1002];

    public EntranceDoor(int[] doorPosition){
        this.doorPosition = doorPosition;
    }

    @Override
    public int[] getPosition() {
        return doorPosition;
    }

    /*
    @Override
    public void generateHeatMap(int[][] crashMap){

        //condition: 0 is lower than 2
        //condition: 1 is lower than 3
        // by definition of the xml-File
        //Mitte des Stores
        System.out.println("Heatmap EntranceDoor called");
        int goalX = getPosition()[0] + (int) Math.ceil((getPosition()[2] - getPosition()[0]) / 2);
        int goalY = getPosition()[1] + (int) Math.ceil((getPosition()[3] - getPosition()[1]) / 2);
        //System.out.println(goalX + " " + goalY);
        int wave = 1;
        //int nextWave = 2;
        int fieldsVisited = 0;

        for (int i = 1; i < 1001; i++) {
            for (int j = 1; j < 1001; j++) {
                if (crashMap[j - 1][i - 1] == 10) {
                    HeatMap[i][j] = -1;
                    fieldsVisited++;
                } else {
                    //HeatMap[i][j] = 0;
                }
            }
        }

        //Rand befüllen
        int k = 0;
        for (int l = 0; l < 1002; l++){
            HeatMap[k][l] = -1;
            HeatMap[l][k] = -1;
        }
        k = 1001;
        for (int l = 0; l < 1002; l++){
            HeatMap[k][l] = -1;
            HeatMap[l][k] = -1;
        }


            HeatMap[goalX][goalY] = wave;
            fieldsVisited ++;



        while (fieldsVisited != (1000000)){

            for (int i = 1; i <1001; i++){
                for (int j =1; j < 1001; j++){
                    if ((HeatMap[i][j] == 0) && ((HeatMap[i+1][j] == wave)||(HeatMap[i-1][j] == wave)||(HeatMap[i][j+1] == wave)||(HeatMap[i][j-1] == wave))){
                        HeatMap[i][j] = wave +1;
                        fieldsVisited ++;
                    }
                }
            }
            wave ++;
            //nextWave = wave + 1;
        }

//      for (int i = 0; i <300; i ++){
//          for (int j = 1; j < 300; j ++) {
//              System.out.print(" " + HeatMap[j][i]);
//          }
//          System.out.println("");
//      }
        System.out.println("Heatmap generated");
    }
*/

}