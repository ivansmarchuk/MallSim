package com.consultsim.mallsim.Model.StaticObjects;

public class Spot {
    private int x;
    private int y;
    private int width;
    private int heigth;
    private int semaphor;

    //point of origin is bottom left corner!!! -> 1000 (heigth) must be subtracted from y

    public Spot(int x, int y, int width, int heigth, int semaphor){
        this.x = x;
        this.y = y;
        this.width = width;
        this.heigth = heigth;
        this.semaphor = semaphor;


        /*
        if(hctemp[i][a] == 1) {
                        hotColdSpots.add(new Spot((i+1) * (divisorwidth), a * (divisorheigth) + (divisorheigth), divisorwidth, divisorheigth, 1));
                    }if(hctemp[i][a] == 2){
                    hotColdSpots.add(new Spot((i+1) * (divisorwidth), a * (divisorheigth) + (divisorheigth), divisorwidth, divisorheigth, 2));

         */

    }

    //point of origin is bottom left corner!!! -> 1000 (heigth) must be subtracted from y


    public int getSemaphor() {
        return semaphor;
    }

    public void setSemaphor(int semaphor) {
        this.semaphor = semaphor;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }
}
