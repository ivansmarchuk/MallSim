package com.consultsim.mallsim.View;

import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.StaticObjects.Spot;

import java.util.*;


public class StatisticHandler {

    private static StatisticHandler statisticInstance = null;
    static ArrayList<int[][]> spotArrayList = new ArrayList<int[][]>();
    private static ArrayList<Person> arrayOfPerson;
    public ArrayList<Spot> hotColdSpots;


    private int matrix[][];
    private int hcmatrix[][];
    private int counterHotSpots;
    private int counterColdSpots;
    private int countOfPersons;
    TreeMap<String, Integer> hm = new TreeMap<>();

    /**
     * return countOfPersons
     * @return
     */
    public int getCountOfPersons() {
        return countOfPersons;
    }

    /**
     * Sets countOfPersons
     */
    public void setCountOfPersons(int countOfPersons) {
        this.countOfPersons = countOfPersons;
    }

    /**
     * Konstructor
     */
    public StatisticHandler() {
        this.counterHotSpots = 0;
        this.counterColdSpots = 0;
        this.hotColdSpots = new ArrayList<Spot>();
    }
    /**
     *Singleton pattern
     * @return only one instance
     */
    public static StatisticHandler getStatisticInstance() {
        if (statisticInstance == null) {
            statisticInstance = new StatisticHandler();
        }
        return statisticInstance;
    }

    /**
     * Return ArrayList of Persons
     * @return
     */
    public static ArrayList<Person> getArrayOfPerson() {
        return arrayOfPerson;
    }

    /**
     * Sets ArrayList of Persons
     * @param arrayOfPerson
     */
    public static void setArrayOfPerson(ArrayList<Person> arrayOfPerson) {
        StatisticHandler.arrayOfPerson = arrayOfPerson;
    }

//    public static void start() {
//        int[][][] y = {{{1, 1, 0}, {1, 1, 2}}, {{1, 2, 1}, {1, 1, 0}}};
//        int out = countHotColdSpots(y)[0];
//        int out2 = countHotColdSpots(y)[1];
//        System.out.println(out);
//        System.out.println(out2);
//    }

    /**
     * Function is called while iterating through the loop in the simulation handler and returns momentarely captures of the Hot-/and Coldspots
     * die Funktion updateHotColdSpots wird während des Schleifendurchlaufs im Simulationhandler aufgerufen und liefert Momentaufnahmen der Hot/Coldspots
     * @param spotArray
     */
    public static void updateHotColdSpots(int[][] spotArray) {
        spotArrayList.add(spotArray);

    }

    /**
     * Counts current Hotspots
     * @param spotArray Array which contains the Hot/-and Coldspots (Hot = 1, Cold = -1, 0: None)
     * @return
     */
    public static int[] countCurrentHotColdSpots(int[][] spotArray) {
        int currentHotSpots = 0;
        int currentColdSpots = 0;

        for (int[] y : spotArray) {
            for (int z : y) {
                switch (z) {
                    case 1:
                        currentHotSpots += 1;
                        break;
                    case -1:
                        currentColdSpots += 1;
                        break;
                }
            }

        }
        int[] tmp = {currentHotSpots,currentColdSpots};
        return tmp;

    }

    /**
     * 0 = kein Spot; 1 = HotSpot; -1 = ColdSpot
     * Is being called at the end of the Simulation
     * @param spotArrayList
     * @return
     */
    public static int[] countHotColdSpots(int[][][] spotArrayList) {
        int hotSpots = 0;
        int coldSpots = 0;
        for (int[][] x : spotArrayList) {
            for (int[] y : x) {
                for (int z : y) {
                    switch (z) {
                        case 1:
                            hotSpots += 1;
                            break;
                        case -1:
                            coldSpots += 1;
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
        int[] klrf = {hotSpots, coldSpots};
        return klrf;
    }

    /**
     * returns Hot-/and Coldspots
     * @return
     */
    public ArrayList<Spot> getHotColdSpots() {

        return hotColdSpots;
    }


    /**
     * Fills collection with Hot-/and Colspots so they can later be represented visually on the GUI
     * @param height
     * @param width
     * @param divisorheigth
     * @param divisorwidth
     * @param hctemp
     */
    public void createSpotObjects(int height, int width, int divisorheigth, int divisorwidth, int[][] hctemp) {
        int divheight = height / divisorheigth;
        int divwidth = width / divisorwidth;
        for (int y = 0; y < divheight; y++) {
            for (int x = 0; x < divwidth; x++) {
                //System.out.println(hctemp[i][a]);
                //REBECCA: LOOK HERE ->
                if (hctemp[y][x] == 1) {
                    hotColdSpots.add(new Spot(x * divisorwidth, ((y * divisorheigth)), divisorwidth, divisorheigth, 1));

                }
                if (hctemp[y][x] == -1) {
                    hotColdSpots.add(new Spot(x * divisorwidth, ((y * divisorheigth)), divisorwidth, divisorheigth, 2));

                }
            }
        }
    }

    /**
     * Test purposes
     */
    public void testHotColdSpots() {
        hotColdSpots.clear();
        arrayOfPerson = new ArrayList<Person>();
        Random random = new Random();
        int x;
        int y;

        int m[][] = new StatisticHandler().recognizeHCSpots(1000, 1000, 100, 100, arrayOfPerson);

        createSpotObjects(1000, 1000, 100, 100, m);

    }

    /**
     * returns counterHotSpots
     * @return
     */
    public int getCounterHotSpots() {
        return counterHotSpots;
    }

    /**
     * returns counterColdSpots
     * @return
     */

    public int getCounterColdSpots() {
        return counterColdSpots;
    }

    /**
     * Untested
     * @param spotArrayList
     * @return
     */
    public int[][] hottestColdestSpots(int[][][] spotArrayList) {
        int[][] hottestColdestSpotsMatrix = new int[10 + 1][10 + 1];
        //fill with 0 MUSS GEÄNDERT WERDEN FALLS HEIGHT/WIDTH ZU GLOBALE VARIABLE WIRD
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                hottestColdestSpotsMatrix[i][j] = 0;
            }
        }
        //int hottestColdestSpotsMatrix = new int[1][1];
        for (int[][] x : spotArrayList) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    hottestColdestSpotsMatrix[i][j] += x[i][j];
                }
            }
        }
        return hottestColdestSpotsMatrix;
    }

    //the width and heigth must be divisible by lengthwidth and lengthheigth with a rest of 0

    /**
     * Counts the persons in the separate areas of the simulation field and assignes them to a specific
     * area
     * After that, the highest person count in an area is searched. Based on this, the Hot-/and Coldspots are being
     * computed
     * @param width
     * @param heigth
     * @param lengthwidth
     * @param lengthheigth
     * @param arrayOfPersons
     * @return
     */
    public int[][] recognizeHCSpots(int width, int heigth, int lengthwidth, int lengthheigth, ArrayList<Person> arrayOfPersons) {
        int divisorheigth = heigth / lengthheigth;
        int divisorwidth = width / lengthwidth;

        //Initialize Matrix
        matrix = new int[divisorheigth + 1][divisorwidth + 1];
        hcmatrix = new int[divisorheigth + 1][divisorwidth + 1];

        //Fill matrices with zeros
        for (int i = 0; i < divisorheigth; i++) {
            for (int a = 0; a < divisorwidth; a++) {
                matrix[i][a] = 0;
                if (!hm.containsKey(""+i+a)){
                    hm.put(""+i+a, 0);
                }
                hcmatrix[i][a] = 0;
            }
        }

        //Iterate through all persons and increase the counter of the field in which they currently are
        for (Person p : arrayOfPersons) {
            //System.out.println(p.x/lengthheigth + " "+ p.y/lengthwidth);
            matrix[p.getCurrentPosition().getY() / lengthheigth][p.getCurrentPosition().getX() / lengthwidth] += 1;
        }


        int highestValue = searchForHighestValue(divisorheigth, divisorwidth);
        double borderLower = highestValue * 0.04;
        double borderHigher = highestValue * 0.91;

        //iterate through matrix and compute the Hot- and Coldspots given on the highest value
        for (int y = 0; y < divisorheigth; y++) {
            //System.out.println();
            for (int x = 0; x < divisorwidth; x++) {
                if (matrix[y][x] < borderLower) {
                    //matrix[y][x] -= 1;
                    hm.put(""+x+y, hm.get(""+x+y)-1);
                    hcmatrix[y][x] = -1;

                } else if (matrix[y][x] < borderHigher) {
                    hcmatrix[y][x] = 0;
                } else {
                    //hm.put(""+x+y, hm.get(""+x+y)+ 1);
                    hcmatrix[y][x] = 1;
                    matrix[y][x] +=1;
                }
            }
        }
        counterHotSpots = countCurrentHotColdSpots(hcmatrix)[0];
        counterColdSpots = countCurrentHotColdSpots(hcmatrix)[1];



        //Print out matrices
       /* System.out.println("Distribution matrix: ");
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
*/
        return hcmatrix;
    }

    /**
     * searches the matrix for highest value (so it can later compute the borders to classify Hot- and Coldspots
     * @param divisorheigth
     * @param divisorwidth
     * @return
     */
    private int searchForHighestValue(int divisorheigth, int divisorwidth) {

        int highest = 0;

        for (int i = 0; i < divisorheigth; i++) {
            for (int a = 0; a < divisorwidth; a++) {
                if (matrix[i][a] > highest)
                    highest = matrix[i][a];
            }
        }
        return highest;
    }

    public NavigableSet<Map.Entry<String, Integer>> getHm() {
        return entriesSortedByValues(hm);
    }

    static <K,V extends Comparable<? super V>>
    NavigableSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        NavigableSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

}
