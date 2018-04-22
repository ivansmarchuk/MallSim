package com.consultsim.mallsim.Model;

import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.View.SimulationHandler;
import com.consultsim.mallsim.View.StatisticHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeatMap {
    private SimulationHandler simulationHandler = SimulationHandler.getSimulationInstance();
    private static final int TILE_SIZE = 10;
    private static final int PARTICLE_SIZE = 5;
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

    ArrayList<Person> personsAL = new ArrayList<>();

    private int[] particleRaster;

    private int[][] densityArray;

    private Random r = new Random();


    public HeatMap(int width, int height, int goalX, int goalY){
        Person person =  new Person( 450, 2 , r.nextFloat()*10, r.nextFloat()*10 );
        personsAL.add(person);
        simulationHandler.arrayOfPersons.add(person);


        this.goalX = goalX;
        this.goalY = goalY;


        WIDTH = width;
        HEIGHT = height;

        TILE_COUNT_X = 1 + (WIDTH / TILE_SIZE);
        TILE_COUNT_Y = 1 + (HEIGHT / TILE_SIZE);
        traversable = new boolean[TILE_COUNT_X][TILE_COUNT_Y];
        distance = new float[TILE_COUNT_X][TILE_COUNT_Y];
        densityArray = new int[WIDTH][HEIGHT];

        generateHeatMap();

    }

    private void generateHeatMap() {

        //TODO here schould be initialized crashmap

        for(int xi = 0; xi < TILE_COUNT_X; xi++){
            for(int yi = 0; yi < TILE_COUNT_Y; yi++){
                traversable[xi][yi] = true;

                if( (xi == TILE_COUNT_X/3 || xi == TILE_COUNT_X*2/3 || xi == TILE_COUNT_X/2 ) && yi != TILE_COUNT_Y/3 /*&& yi != TILE_COUNT_Y*2/3*/ ){
                    //traversable[xi][yi] = false;
                }
                if(r.nextFloat() < .1f && yi != TILE_COUNT_Y/3){
                   // traversable[xi][yi] = false;
                }
                if(xi < 9 && yi < 5){
                    traversable[xi][yi] = true;
                }

              }
        }
        distance(TILE_COUNT_X/4,TILE_COUNT_Y/4);

    }

    public void addPersons(){
        distance(TILE_COUNT_X/4,TILE_COUNT_Y/4);
        for(int i = 0; i < 2; i++){
            if(traversable[(int)(450/TILE_SIZE)][(int)(20/TILE_SIZE)]){

                //TODO GENERATE PERSON



            }
        }
    }

    // calculates the distance from x,y to every point considered traversable[][]
    public void distance( int startX, int startY ){

        float[][] tempDistance = new float[TILE_COUNT_X][TILE_COUNT_Y];

        for(int xi = 0; xi < TILE_COUNT_X; xi++){
            for(int yi = 0; yi < TILE_COUNT_Y; yi++){
                tempDistance[xi][yi] = 0;
            }
        }

        ArrayList<Node> heap = new ArrayList<Node>();

        //add first node to it.
        if(traversable[startX][startY]){
            heap.add( new Node(startX,startY,0) );
            tempDistance[startX][startY] = .1f;
        }else{
            // startx and starty are outside of acceptable range
        }

        while( heap.size() > 0 ){

            Node n = heap.get(0);

            int x = n.getX();
            int y = n.getY();
            float d = n.getD();


            //Make an array containing the (x,y,d) and then go through it checking x,y

            float[] distGrid // a 3*8 array containing (dx,dy,d) for nodes
                    = {
                    -1, 0, 1,
                    0,-1, 1,
                    0, 1, 1,
                    1, 0, 1,
                    -1,-1, 1.4f,
                    -1, 1, 1.4f,
                    1,-1, 1.4f,
                    1, 1, 1.4f,};


            for( int i = 0; i < distGrid.length; i+= 3){

                int tempX = x + (int)distGrid[i+0];
                int tempY = y + (int)distGrid[i+1];
                float tempD = d + distGrid[i+2];

                // if traversable[][] && if in map && if new will be smaller than old
                if(tempX >= 0 && tempY >= 0 && tempX < TILE_COUNT_X && tempY < TILE_COUNT_Y && traversable[tempX][tempY] && (tempDistance[tempX][tempY] == 0 || tempDistance[tempX][tempY] > tempD)){
                    heap.add( new Node( tempX, tempY, tempD ) );
                    tempDistance[tempX][tempY] = tempD;
                }

            }


            heap.remove(0);
        }

        if(traversable[startX][startY]){
            distance = tempDistance;
        }

        acceleration();



    }

    private void acceleration() {

        float[][] tempxAccelGrid = new float[TILE_COUNT_X][TILE_COUNT_Y];
        float[][] tempyAccelGrid = new float[TILE_COUNT_X][TILE_COUNT_Y];

        for(int xi = 0; xi < TILE_COUNT_X; xi++){
            for(int yi = 0; yi < TILE_COUNT_Y; yi++){

                tempxAccelGrid[xi][yi] = 0;
                tempyAccelGrid[xi][yi] = 0;

                int leftTile = xi-1 > 0 ? xi-1 : xi;
                int rightTile = xi+1 < TILE_COUNT_X ? xi+1 : xi;
                int upTile = yi-1 > 0 ? yi-1: yi;
                int downTile = yi+1 < TILE_COUNT_Y ? yi+1 : yi;

                leftTile = traversable[leftTile][yi] ? leftTile : xi;
                rightTile = traversable[rightTile][yi] ? rightTile : xi;
                upTile = traversable[xi][upTile] ? upTile : yi;
                downTile = traversable[xi][downTile] ? downTile : yi;

                tempxAccelGrid[xi][yi] = distance[leftTile][yi] - distance[rightTile][yi] ;
                tempyAccelGrid[xi][yi] = distance[xi][upTile] - distance[xi][downTile] ;

            }
        }

        xAccelGrid = tempxAccelGrid;
        yAccelGrid = tempyAccelGrid;

    }

    public void tick() {


        for(int xi = 0; xi < WIDTH; xi++){
            for(int yi = 0; yi < HEIGHT; yi++){

                densityArray[xi][yi] = 0;//densityArray[xi][yi]/2;

            }
        }

        for(int i = 0; i < personsAL.size(); i++){

            // get particle and information
            Person p = personsAL.get(i);

            float xPos = p.getxPos();
            float yPos = p.getyPos();
            float xVel = p.getxVel();
            float yVel = p.getyVel();

            // if in simulated world (TILE) range
            if(xPos +xVel <= TILE_COUNT_X*TILE_SIZE && xPos+xVel >= 0 && yPos+yVel <= TILE_COUNT_Y*TILE_SIZE && yPos+yVel >= 0){

                xVel += xAccelGrid[(int)xPos/TILE_SIZE][(int)yPos/TILE_SIZE];
                yVel += yAccelGrid[(int)xPos/TILE_SIZE][(int)yPos/TILE_SIZE];

            }else /* if not in simulation any more */{
                personsAL.remove(i);
            }

            // if in viewing range
            if (xPos + xVel < WIDTH - PARTICLE_SIZE && xPos + xVel > PARTICLE_SIZE && yPos + yVel < HEIGHT - PARTICLE_SIZE && yPos + yVel > PARTICLE_SIZE) {

                // if no collision
                if (traversable[ (int) ((xPos + xVel) / TILE_SIZE)][(int) ((yPos + yVel) / TILE_SIZE)]) {

                    xPos += xVel;
                    yPos += yVel;

                    xVel += .2 * (densityArray[(int) xPos][(int) yPos] - densityArray[(int) xPos + PARTICLE_SIZE][(int) yPos]);
                    yVel += .2 * (densityArray[(int) xPos][(int) yPos] - densityArray[(int) xPos][(int) yPos + PARTICLE_SIZE]);

                    xVel = .9f * xVel;
                    yVel = .9f * yVel;

                } else /* if collision */{

                    float Vel = .5f * sqrt(xVel * xVel + yVel * yVel);

                    // if x making it collide
                    if (!traversable[ (int) ((xPos + xVel) / TILE_SIZE)][(int) (yPos / TILE_SIZE)]) {

                        yVel = yVel > 0 ? Vel : -Vel;
                        xVel = -.5f * xVel;

                    }else /* if x not making it collide */{
                        xPos += xVel;
                    }

                    // if y making it collide
                    if (!traversable[ (int) (xPos / TILE_SIZE)][(int) ((yPos + yVel) / TILE_SIZE)]) {

                        xVel = xVel > 0 ? Vel : -Vel;
                        yVel = -.5f * yVel;

                    }else /* if y not making it collide */{
                        yPos += yVel;
                    }

                }
            } else /* if not in viewing range */ {

                xPos += xVel;
                yPos += yVel;

            }
            p.setPerson( xPos, yPos, xVel, yVel);


            // I still simulate if in TILE (simulated) area, but can't draw unless in WIDTH (viewable)
            if(xPos < WIDTH - PARTICLE_SIZE && xPos > PARTICLE_SIZE && yPos < HEIGHT - PARTICLE_SIZE && yPos > PARTICLE_SIZE){ // draw on densityArray



                for(int xi = 0; xi < PARTICLE_SIZE; xi++){
                    for(int yi = 0; yi < PARTICLE_SIZE; yi++){
                        densityArray[ (int)(xPos+xi)][ (int)(yPos+yi)] += 1;
                    }
                }

            }

        }

        //repaint();
    }

    public float sqrt( float x ){
        return x * Float.intBitsToFloat(0x5f3759d5 - (Float.floatToIntBits(x) >> 1));
    }


    public ArrayList<Person> getPersonsList() {
        return personsAL;
    }

    public void setPersonsList(ArrayList<Person> personsList) {
        this.personsAL=personsList;
    }



}
