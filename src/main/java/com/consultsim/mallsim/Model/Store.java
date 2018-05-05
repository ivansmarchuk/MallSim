package com.consultsim.mallsim.Model;


import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Vector;

public class Store {
    private int id;
    private int position[];
    private int doorPosition[];
    private String label;
    private String interestingFor[];
    private Color color;
    private int peopleCounter;
    private int[][] HeatMap=new int[1002][1002];

    public Store() {
        position=new int[4];
        doorPosition=new int[4];
    }

    public void setId(int id) {
        this.id=id;
    }

    /**
     * The heatmap stores the path distance from the goal to every tile on the map.     *
     *
     * @param crashMap map with all object where the pathfinding isn't possible
     */
    public void generateHeatMap(int[][] crashMap) {
        int x;
        int y;
        int dist;
        System.out.println("Heatmap called");
        int goalX=getPosition()[0] + (int) Math.ceil((getPosition()[2] - getPosition()[0]) / 2);
        int goalY=getPosition()[1] + (int) Math.ceil((getPosition()[3] - getPosition()[1]) / 2);

        for (int i=1; i < 1001; i++) {
            for (int j=1; j < 1001; j++) {
                if (crashMap[j - 1][i - 1] == 10)
                    HeatMap[i][j]=-1;

            }
        }
        //Rand befüllen
        for (int k=0; k < 1002; k++) {
            HeatMap[0][k]=HeatMap[k][0]=HeatMap[1001][k]=HeatMap[k][1001]=-1;
        }
        LinkedList<Vector<Integer>> q=new LinkedList<>();
        Vector<Integer> vector=new Vector<>();
        Vector<Integer> next;
        vector.add(goalX);
        vector.add(goalY);
        q.addFirst(vector);
        while (!q.isEmpty()) {
            vector=q.removeLast();
            x=vector.get(0);
            y=vector.get(1);
            dist=HeatMap[x][y];
            for (int i=-1; i <= 1; i++) {
                for (int j=-1; j <= 1; j++) {
                    if (i + j != 0 && i * j == 0 && HeatMap[x + i][y + j] == 0) {
                        next=new Vector<>();
                        next.add(x + i);
                        next.add(y + j);
                        q.addFirst(next);
                        HeatMap[x + i][y + j]=dist + 1;
                    }
                }
            }
        }
        System.out.println("Heatmap generated");
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
        return HeatMap[x + 1][y + 1];
    }

}
