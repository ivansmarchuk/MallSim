package com.consultsim.mallsim.View;

import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.Model.StaticObjects.Spot;

import java.util.ArrayList;
import java.util.Random;


public class StatisticHandler {

    //VARIABLES

    public ArrayList<Spot> hotColdSpots;

    public static ArrayList<Person> arrayOfPerson;
    static ArrayList<int[][]> spotArrayList = new ArrayList<int[][]>();
    public int matrix[][];
    public int hcmatrix[][];
    public int newmatrix[][];
    public int counterHotSpots;
    public int counterColdSpots;


    //METHODS



    public static ArrayList<Person> getArrayOfPerson() {
        return arrayOfPerson;
    }

    public static void setArrayOfPerson(ArrayList<Person> arrayOfPerson) {
        StatisticHandler.arrayOfPerson = arrayOfPerson;
    }

    public ArrayList<Spot> getHotColdSpots() {
        return hotColdSpots;
    }

    public void createSpotObjects(int height, int width, int divisorheigth, int divisorwidth, int[][] hctemp){
        int divheight = height/divisorheigth;
        int divwidth = width/divisorwidth;
        for(int y = 0; y < divheight; y++){
            for(int x = 0; x < divwidth; x++){
                    //System.out.println(hctemp[i][a]);
                    //REBECCA: LOOK HERE ->
                    if(hctemp[y][x] == 1) {
                        hotColdSpots.add(new Spot(x * divisorwidth, 450-((y * divisorheigth)+divisorheigth), divisorwidth, divisorheigth, 1));
                    }if(hctemp[y][x] == 2){
                        hotColdSpots.add(new Spot(x * divisorwidth, 450-((y * divisorheigth) + divisorheigth), divisorwidth, divisorheigth, 2));

                }
                }
        }
    }



    public StatisticHandler(){
        this.counterHotSpots = 0;
        this.counterColdSpots = 0;
        this.hotColdSpots = new ArrayList<Spot>();
    }

    //test purposes
    public void testHotColdSpots(){
        arrayOfPerson = new ArrayList<Person>();
        Random random = new Random();
        int x;
        int y;
        for (int i = 0; i < 1; i++) {
            x = random.nextInt(450) + 1;
            y = random.nextInt(450) + 1;

            arrayOfPerson.add(new Person(new Position(x,y), 0.0));
            System.out.println("x: " + x + " y: " + y);
        }
        int m[][] = new StatisticHandler().recognizeHCSpots(1000,1000, 100, 100, arrayOfPerson);

        createSpotObjects(1000,1000,100,100, m);

    }
    public static void start() {
        int[][][] y = {{{1,1,0},{1,1,2}},{{1,2,1},{1,1,0}}};
        int out = countHotColdSpots(y)[0];
        int out2 = countHotColdSpots(y)[1];
        System.out.println(out);
        System.out.println(out2);
    }

    //die Funktion updateHotColdSpots wird während des Schleifendurchlaufs im Simulationhandler aufgerufen und liefert Momentaufnahmen der Hot/Coldspots
    public static void updateHotColdSpots(int[][] spotArray ){
        spotArrayList.add(spotArray);

    }

    public int getCounterHotSpots() {
        return counterHotSpots;
    }

    public int getCounterColdSpots() {
        return counterColdSpots;
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

//the width and heigth must be divisible by lengthwidth and lengthheigth with a rest of 0


public int[][] recognizeHCSpots(int width, int heigth, int lengthwidth, int lengthheigth, ArrayList<Person> arrayOfPersons){
        int divisorheigth = heigth/lengthheigth;
        int divisorwidth = width/lengthwidth;

        //Initialize Matrix
        matrix = new int[divisorheigth +1][divisorwidth +1];
        hcmatrix = new int[divisorheigth +1][divisorwidth +1];

        //Fill matrices with zeros
        for(int i = 0; i < divisorheigth; i++){
            for(int a = 0; a < divisorwidth; a++){
                matrix[i][a] = 0;
                hcmatrix[i][a] = 0;
            }
        }

        //Iterate through all persons and increase the counter of the field in which they currently are
        for(Person p : arrayOfPersons){
            //System.out.println(p.x/lengthheigth + " "+ p.y/lengthwidth);
            matrix[p.getCurrentPosition().getY()/lengthheigth][p.getCurrentPosition().getX()/lengthwidth] += 1;
        }


        int highestValue = searchForHighestValue(divisorheigth, divisorwidth);
        double borderLower = highestValue * 0.3;
        double borderHigher = highestValue * 0.87;

        //iterate through matrix and compute the Hot- and Coldspots given on the highest value
        for(int y = 0; y < divisorheigth; y++){
            System.out.println();
            for(int x = 0; x < divisorwidth; x++){
                if(matrix[y][x] < borderLower) {
                    hcmatrix[y][x] = 2;
                    counterColdSpots++;
                }else if(matrix[y][x] < borderHigher) {
                    hcmatrix[y][x] = 0;
                }else{
                    hcmatrix[y][x] = 1;
                    counterHotSpots++;
                }
            }
        }


        //Print out matrices
        System.out.println("Distribution matrix: ");
        System.out.println();

        for(int y = 0; y < divisorheigth; y++){
            System.out.println();
            for(int x = 0; x < divisorwidth; x++){
                System.out.print(" " + matrix[y][x] +" ");
            }
        }

        System.out.println();
        System.out.println();
        System.out.println("Matrix with Hot- and Coldspots: ");
        System.out.println();

        for(int y = 0; y < divisorheigth; y++){
            System.out.println();
            for(int x = 0; x < divisorwidth; x++){
                System.out.print(" " + hcmatrix[y][x] +" ");
            }
        }

        System.out.println();
        System.out.println(counterHotSpots);
        System.out.println(counterColdSpots);

        return hcmatrix;
    }

//searches the matrix for highest value (so it can later compute the borders to classify Hot- and Coldspots
    public int searchForHighestValue(int divisorheigth, int divisorwidth){

        int highest = 0;

        for(int i = 0; i < divisorheigth; i++){
            for(int a = 0; a < divisorwidth; a++){
                if(matrix[i][a] > highest)
                    highest = matrix[i][a];
            }
        }
        return highest;
    }





}
