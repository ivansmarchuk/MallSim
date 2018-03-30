package com.consultsim.mallsim.View;

import com.consultsim.mallsim.Model.Persons.Person;

import java.util.ArrayList;

public class StatisticHandler {

    public int matrix[][];
    public int hcmatrix[][];
    public int counterHotSpots;
    public int counterColdSpots;

    public StatisticHandler(){
        this.counterHotSpots = 0;
        this.counterColdSpots = 0;
    }


    public static void start() {
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


//the width and heigth must be divisible by lengthwidth and lengthheigth with a rest of 0


public void recognizeHCSpots(int width, int heigth, int lengthwidth, int lengthheigth, ArrayList<Person> arrayOfPersons){
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
            matrix[p.getCurrentPosition().getX()/lengthheigth][p.getCurrentPosition().getY()/lengthwidth] += 1;
        }


        int highestValue = searchForHighestValue(divisorheigth, divisorwidth);
        double borderLower = highestValue * 0.28;
        double borderHigher = highestValue * 0.87;

        //iterate through matrix and compute the Hot- and Coldspots given on the highest value
        for(int i = 0; i < divisorheigth; i++){
            System.out.println();
            for(int a = 0; a < divisorwidth; a++){
                if(matrix[i][a] < borderLower) {
                    hcmatrix[i][a] = 2;
                    counterColdSpots++;
                }else if(matrix[i][a] < borderHigher) {
                    hcmatrix[i][a] = 0;
                }else {
                    hcmatrix[i][a] = 1;
                    counterHotSpots++;
                }
            }
        }


        //Print out matrices
        System.out.println("Distribution matrix: ");
        System.out.println();

        for(int i = 0; i < divisorheigth; i++){
            System.out.println();
            for(int a = 0; a < divisorwidth; a++){
                System.out.print(" " + matrix[i][a] +" ");
            }
        }

        System.out.println();
        System.out.println();
        System.out.println("Matrix with Hot- and Coldspots: ");
        System.out.println();

        for(int i = 0; i < divisorheigth; i++){
            System.out.println();
            for(int a = 0; a < divisorwidth; a++){
                System.out.print(" " + hcmatrix[i][a] +" ");
            }
        }

        System.out.println();
        System.out.println(counterHotSpots);
        System.out.println(counterColdSpots);


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
