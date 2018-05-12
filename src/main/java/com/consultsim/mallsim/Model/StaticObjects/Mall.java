package com.consultsim.mallsim.Model.StaticObjects;

import com.consultsim.mallsim.Model.Position;

public class Mall {
    private static Mall mallInstance=null;

    private Position doorLeftUpper;
    private Position doorDownRight;
    private int ySize;
    private int xSize;

    public static Mall getMallInstance() {
        if (mallInstance == null) {
            mallInstance=new Mall();
        }
        return mallInstance;
    }

    public Position getDoorLeftUpper() {
        return doorLeftUpper;
    }

    public void setDoorLeftUpper(Position doorLeftUpper) {
        this.doorLeftUpper=doorLeftUpper;
    }

    public Position getDoorDownRight() {
        return doorDownRight;
    }

    public void setDoorDownRight(Position doorDownRight) {
        this.doorDownRight=doorDownRight;
    }

    public int getySize() {
        return ySize;
    }

    public void setySize(int ySize) {
        this.ySize=ySize;
    }

    public int getxSize() {
        return xSize;
    }

    public void setxSize(int xSize) {
        this.xSize=xSize;
    }
}
