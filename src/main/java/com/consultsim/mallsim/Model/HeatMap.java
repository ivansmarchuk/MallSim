package com.consultsim.mallsim.Model;

import java.util.Random;

public class HeatMap {

    private static final int TILE_SIZE = 64;
    private static final int PARTICLE_SIZE = 32;
    private static int WIDTH;
    private static int HEIGHT;
    private static int TILE_COUNT_X;
    private static int TILE_COUNT_Y;

    private boolean[][] traversable;
    private float[][] distance;
    private float[][] yAccelGrid;
    private float[][] xAccelGrid;
    private int goalX;
    private int goalY;


    private int[] particleRaster;

    private int[][] densityArray;

    private Random r = new Random();

    public HeatMap(int width, int height, int goalX, int goalY){
        this.goalX = goalX;
        this.goalY = goalY;

        WIDTH = width;
        HEIGHT = height;

        TILE_COUNT_X = 1 + (WIDTH / TILE_SIZE);
        TILE_COUNT_Y = 1 + (HEIGHT / TILE_SIZE);
        traversable = new boolean[TILE_COUNT_X][TILE_COUNT_Y];
        distance = new float[TILE_COUNT_X][TILE_COUNT_Y];
        densityArray = new int[WIDTH][HEIGHT];

        generateHeatMap(450, 2);

    }

    private void generateHeatMap(float x, float y) {

        for(int xi = 0; xi < TILE_COUNT_X; xi++){
            for(int yi = 0; yi < TILE_COUNT_Y; yi++){
                traversable[xi][yi] = true;
                if( (xi == TILE_COUNT_X/3 || xi == TILE_COUNT_X*2/3 || xi == TILE_COUNT_X/2 ) && yi != TILE_COUNT_Y/3 /*&& yi != TILE_COUNT_Y*2/3*/ ){
                    traversable[xi][yi] = false;
                }
                if(r.nextFloat() < .1f && yi != TILE_COUNT_Y/3){
                    traversable[xi][yi] = false;
                }
                if(xi < 9 && yi < 5){
                    traversable[xi][yi] = true;
                }

            }
        }
        distance(TILE_COUNT_X/4,TILE_COUNT_Y/4);
        if(traversable[(int)(x/TILE_SIZE)][(int)(y/TILE_SIZE)]){

            //TODO GENERATE PERSON
            //particleAL.add( new Particle( x, y , r.nextFloat()*10, r.nextFloat()*10 ));
        }
    }

    // calculates the distance from x,y to every point considered traversable[][]
    public void distance( int startX, int startY ){
        


    }



}
