package com.consultsim.mallsim.Model;


import javafx.scene.paint.Color;

public class Store {


    HeatMap heatMap;
    private int id;
    private int position[];
    private int doorPosition[];
    private String label;
    private String interestingFor[];
    private Color color;
    private int peopleCounter;
    private int[][] HeatMap=new int[1002][1002];
    //private SimulationHandler simulationHandler;
    private int[][] crashMap=new int[1000][1000];

    public Store() {
        position=new int[4];
        doorPosition=new int[4];
        //generateHeatMap(simulationHandler);



        heatMap = new HeatMap(Configuration.CANVAS_WIDTH_SIZE, Configuration.CANVAS_HEIGHT_SIZE,(position[0]+position[2])/2, (position[1]+position[3])/2);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public void generateHeatMap(int[][] crashMap) {

        //condition: 0 is lower than 2
        //condition: 1 is lower than 3
        // by definition of the xml-File
        //Mitte des Stores
        this.crashMap=crashMap;
        int goalX=getPosition()[0] + (int) Math.ceil((getPosition()[2] - getPosition()[0]) / 2);
        int goalY=getPosition()[1] + (int) Math.ceil((getPosition()[3] - getPosition()[1]) / 2);
        int wave=1;
        int fieldsVisited=0;

        for (int i=1; i < 1001; i++) {
            for (int j=1; j < 1001; j++) {
                if (crashMap[i - 1][j - 1] == 10) {
                    HeatMap[i][j]=-1;
                    fieldsVisited++;
                } else {
                    HeatMap[i][j]=0;
                }
            }
        }

        int k=0;
        for (int l=0; l < 1002; l++) {
            HeatMap[k][l]=-1;
            HeatMap[l][k]=-1;
        }
        k=1001;
        for (int l=0; l < 1002; l++) {
            HeatMap[k][l]=-1;
            HeatMap[l][k]=-1;
        }


        HeatMap[goalX][goalY]=wave;
        fieldsVisited++;

        while (fieldsVisited != (1000000)) {

            for (int i=0; i < 1000; i++) {
                for (int j=0; j < 1000; j++) {
                    if ((HeatMap[i + 1][j + 1] == 0) && ((HeatMap[i + 2][j + 1] == wave) || (HeatMap[i][j + 1] == wave) || (HeatMap[i + 1][j + 2] == wave) || (HeatMap[i + 1][j] == wave))) {
                        HeatMap[i + 1][j + 1]=wave + 1;
                        fieldsVisited++;
                    }
                }
            }
            wave++;
          /*if (fieldsVisited > 990000){
              System.out.println("Soon");
          }*/
        }
/*
      for (int i = 0; i <300; i ++){
          for (int j = 1; j < 300; j ++) {
              System.out.print(" " + HeatMap[j][i]);
          }
          System.out.println("");
      }
*/
    }


    public int countPeopleInStore() {
        return 0;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position=position;
    }

    public int[] getDoorPosition() {
        return doorPosition;
    }

    public void setDoorPosition(int[] doorPosition) {
        this.doorPosition=doorPosition;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label=label;
    }

    public String[] getInterestingFor() {
        return interestingFor;
    }

    public void setInterestingFor(String[] interestingFor) {
        this.interestingFor=interestingFor;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color=color;
    }

    public int getPeopleCounter() {
        return peopleCounter;
    }

    public void setPeopleCounter(int peopleCounter) {
        this.peopleCounter=peopleCounter;
    }

    public int getHeatMapValue(int x, int y) {
        return HeatMap[x][y];
    }

    public void generateHeapMapForStories(double canvasWidthSize, double canvasHeightSize, int[][] crashMap) {


    }
}
