package com.consultsim.mallsim.Model.Persons;


import com.consultsim.mallsim.Model.Configuration;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.Model.Store;
import com.consultsim.mallsim.View.SimulationHandler;
import javafx.scene.shape.Circle;

import java.util.Random;
import java.util.ArrayList;

/**
 * A presentation of a person in the simulation
 */
public class Person {

    private Position currentPosition;
    private Position nextPosition;

    private double speed;
    private Random random;
    private SimulationHandler simulationHandler;
    private static int nextID = 0;
    private int id;
    public static ArrayList<Store> arrayOfStores;
    //TO DO chrashmapObjects!!!
    private String interestedIn;    // hier könnte auch je nachdem ein StringArray hin
    private int movedSince;
    private double radius;
    int timeInShop;
    boolean inShop;
    int timeSearching;
    private int detourX;
    private int detourY;

    //Variables for the maze solving algorithm
    //Es wird sinnvoll sein, das Maze auszulagern, wegen Speicherplatz
    private boolean[][] wasHere = new boolean[1000][1000];
    private boolean[][] correctPath = new boolean[1000][1000]; // The solution to the maze
    private int goalX;
    private int goalY;

    public Person (Position pos, double speed, SimulationHandler simulationHandler){
        //radius
        this.radius = Configuration.PERSON_RADIUS;
        this.currentPosition = pos;
        this.nextPosition = pos;
        this.speed = speed;
        this.random = new Random();
        this.simulationHandler = simulationHandler;
        this.id = Person.nextID++;
        this.movedSince = 0;
        //this.interestedIn = generateInterest();
        this.interestedIn = "nothing";
        inShop = false;
        timeInShop = 0;
        timeSearching = 0;
        detourX = 0;
        detourY = 0;

        if (interestedIn != "nothing") {
            int[] goal = getGoalCoordinates();
            goalX = goal[0];
            goalY = goal[1];
            //findCompleteWayWithGoalMaze(currentX, currentY);
        }
        else{
            goalX = -1;
            goalY = -1;
        }

    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    //compute next position (simple and randomized)
    public void computeNext(){

        int direction;
        int currentX = this.currentPosition.getX();
        int currentY = this.currentPosition.getY();
        int nextX = currentX;
        int nextY = currentY;


        //System.out.println("Rand: " + direction);

        //REBECCA: INSERT YOUR CODE HERE
        //TO DO: Create Function to compute the position of the people

        if (goalX == -1){
            //System.out.println("No shop found or needed");
            // Was passiert dann?
            //compute next position (simple and randomized)
            direction = (int) random.nextInt(5);
        }

        else{
            //direction = findDirectionWithGoal(goalX, goalY, currentX, currentY);
            direction = findPathfromMaze(currentX, currentY);
        }


           /* if(inShop && (timeInShop < 20) ){
                //TO DO: dont leave shop yet -> check if move is still in the shop
            }*/

           nextX = movePerson(direction, currentX, currentY)[0];
           nextY = movePerson(direction, currentX, currentY)[1];

            if (interestedIn != "nothing" &&(nextX == goalX && nextY == goalY)){
                inShop = true;
                goalX = -1;
                goalY = -1;
            }

                //check if valid move
                //if condition here 
                if(isValidMove(nextX, nextY)){
                    //System.out.println("true");
                    this.currentPosition.setX(nextX);
                    this.currentPosition.setY(nextY);
                    //System.out.println("nexty: " + nextY + " nextx: " + nextX);
                    simulationHandler.crashMap[nextY][nextX] = 1;
                    simulationHandler.crashMap[currentY][currentX] = 0;
                }else{
                    simulationHandler.crashMap[currentY][currentX]++;
                }

                if(simulationHandler.crashMap[currentY][currentX] == 4){
                    nextPosition = handleCollision(nextX, nextY, currentX, currentY);
                    simulationHandler.crashMap[nextPosition.getY()][nextPosition.getX()] = 1;
                    simulationHandler.crashMap[currentY][currentX] = 0;
                    movedSince = 0;
                    this.currentPosition.setX(nextPosition.getX());
                    this.currentPosition.setY(nextPosition.getY());
                }


            //System.out.println("X: " + this.getCurrentPosition().getX()  + " Y: " + this.getCurrentPosition().getY());


    }

    private Position handleCollision(int nextX, int nextY, int currentX, int currentY){
        int tempx;
        int tempy;
        int found = 0;
        Position tempPos = new Position(currentX, currentY);

        int chooseDirection = (int) random.nextInt(4);
        System.out.println("chooseDir: " + chooseDirection);
        switch (chooseDirection){
            case 0: {
                for(int y = -10 ; y < 10; y++){
                    for(int x = -10; x < 10; x++){
                        tempy = currentY + y;
                        tempx = currentX + x;
                        if(tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000){
                            if((simulationHandler.crashMap[tempy][tempx] == 0) && found == 0){
                                tempPos = new Position(tempx, tempy);
                                found = 1;
                            }else if((simulationHandler.crashMap[tempy][tempx] < 5) && (simulationHandler.crashMap[tempy][tempx] != 0)){
                                simulationHandler.crashMap[tempy][tempx] = 1;
                            }
                        }


                    }
                }
                break;
            }
            case 1:
                for(int y = 10 ; y < -10; y--){
                    for(int x = 10; x < -10; x--){
                        tempy = currentY + y;
                        tempx = currentX + x;
                        if(tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000){
                            if((simulationHandler.crashMap[tempy][tempx] == 0) && found == 0){
                                tempPos = new Position(tempx, tempy);
                                found = 1;
                            }else if((simulationHandler.crashMap[tempy][tempx] < 5) && (simulationHandler.crashMap[tempy][tempx] != 0)){
                                simulationHandler.crashMap[tempy][tempx] = 1;
                            }
                        }


                    }
                }
                break;

            case 2:
                for(int y = -10 ; y < 10; y++){
                    for(int x = 10; x < -10; x--){
                        tempy = currentY + y;
                        tempx = currentX + x;
                        if(tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000){
                            if((simulationHandler.crashMap[tempy][tempx] == 0) && found == 0){
                                tempPos = new Position(tempx, tempy);
                                found = 1;
                            }else if((simulationHandler.crashMap[tempy][tempx] < 5) && (simulationHandler.crashMap[tempy][tempx] != 0)){
                                simulationHandler.crashMap[tempy][tempx] = 1;
                            }
                        }


                    }
                }
                break;

            case 3:
                for(int y = 10 ; y < -10; y--){
                    for(int x = -10; x < 10; x++){
                        tempy = currentY + y;
                        tempx = currentX + x;
                        if(tempx >= 0 && tempy >= 0 && tempx < 1000 & tempy < 1000){
                            if((simulationHandler.crashMap[tempy][tempx] == 0) && found == 0){
                                tempPos = new Position(tempx, tempy);
                                found = 1;
                            }else if((simulationHandler.crashMap[tempy][tempx] < 5) && (simulationHandler.crashMap[tempy][tempx] != 0)){
                                simulationHandler.crashMap[tempy][tempx] = 1;
                            }
                        }


                    }
                }
                break;

        }




        return tempPos;
    }

    //check, if the next position is a valid one
    public boolean isValidMove(int nextX, int nextY){
        if(nextX < 0 || nextY < 0 || nextX > 999 || nextY > 999){
            return false;
        }
            for(Person p: simulationHandler.getArrayOfPersons()){
                int x = p.getCurrentPosition().getX() - nextX;
                int y = p.getCurrentPosition().getY() - nextY;
                double distance = Math.sqrt(x*x + y*y);
                //System.out.println("Dist: " + distance);

                if((distance < 11) && (p.id != this.id)){
                    return false;
                }

                if(simulationHandler.crashMap[nextY][nextX] == 10){
                    return false;
                }
            }
            return true;
        }

    public String generateInterest(){
        //choose what the person wants to buy
        //hier fehlen noch die richtigen Kategorien, die es dann auch für die Geschäfte geben muss
        int temp = (int) random.nextInt(5);
        switch (temp) {
            case 0:
                //random movement
                return "nothing";
            case 1:
                return "clothing";
            case 2:
                return "food";
            case 3:
                return "book";
            case 4:
                return "other";
            default:
                return "error in generate interest()";
        }

    }

    public int[] getGoalCoordinates(){
        int goalX = -1;
        int goalY = -1;
        int interestingShops[] = new int[100];  //Ich weiß noch nicht wie viele Shops es gibt
        int nrInterestingShops = 0;
        int temp;
        int divisorForDoor;


        for (Store s : arrayOfStores) {
            for (int i = 0; i < 2; i++) {
                //condition: only 2 tags on each store
                if (interestedIn == s.getInterestingFor()[i]) {
                    interestingShops[nrInterestingShops] = s.getId();
                    nrInterestingShops++;
                }
            }
        }

        if (nrInterestingShops != 0) {

            temp = (int) random.nextInt(nrInterestingShops);
            //get shop by id
            for (Store s : arrayOfStores) {
                if (s.getId() == interestingShops[temp]) {
                    divisorForDoor = (int) random.nextInt(20);
                    //@param: divisorForDoor is used, so the customers will have different goals in the door,
                    // and will not all head for the middle, which may cause collisions

                    //condition: 0 is lower than 2
                    //condition: 1 is lower than 3
                    // by definition of the xml-File

                    goalX = s.getDoorPosition()[0] + (int) Math.ceil(Math.abs(s.getDoorPosition()[2] - s.getDoorPosition()[0]) / 20 * divisorForDoor);
                    goalY = s.getDoorPosition()[1] + (int) Math.ceil(Math.abs(s.getDoorPosition()[3] - s.getDoorPosition()[1]) / 20 * divisorForDoor);
                    break;
                }
            }
        }
        int[] goal = {goalX,goalY};
        return goal;
    }

    public int[] movePerson(int direction, int currentX, int currentY){

        int nextStep = 4;
        int[] newPosition = {currentX, currentY};

        switch (direction) {
            case 0:
                //right
                if ((currentX + nextStep)<= 1000) {
                    newPosition[0] = currentX + nextStep;
                }
                break;

            case 1:
                //down
                if ((currentY + nextStep)<= 1000) {
                    newPosition[1] = currentY + nextStep;
                }
                break;

            case 2:
                //left
                if ((currentX - nextStep) >= 0) {
                    newPosition[0] = currentX - nextStep;
                }
                break;

            case 3:
                //up
                if ((currentY - nextStep) >= 0) {
                    newPosition[1] = currentY - nextStep;
                }
                break;

            case 4:
                //wait
                break;
        }

        return newPosition;
    }

    public int findDirectionWithGoal(int goalX, int goalY, int currentX, int currentY){

        int temp = (int) random.nextInt(2);
        int nextDirection;

        if (detourX != 0){
            goalX = - goalX;
            currentX = - currentX;
        }
        if (detourY != 0){
            goalY = - goalY;
            currentY = - currentY;
        }

        if(goalX < currentX){
            //move left
            nextDirection = 2;

            if (simulationHandler.crashMap[movePerson(2, currentX, currentY)[0]][currentY] == 10){
                for (int i = currentX; i < 1000 ;i++){  //1000 als Grenze des Kaufhauses, hier noch mal nach Variable schauen
                    if (simulationHandler.crashMap[i][currentY] == 10){
                        detourX = currentX - i;
                        break;
                    }
                }
            }
        }

        else if(goalX > currentX){
            //move right
            nextDirection = 0;
            if (simulationHandler.crashMap[movePerson(0, currentX, currentY)[0]][currentY] == 10){
                for (int i = currentX; i >= 0 ;i--){  //1000 als Grenze des Kaufhauses, hier noch mal nach Variable schauen
                    if (simulationHandler.crashMap[i][currentY] == 10){
                        detourX = currentX - i;
                        break;
                    }
                }
            }

        }

        else{
            nextDirection = 4;
        }


        if(goalY > currentY){
            //move down
            nextDirection = 1;
            if (simulationHandler.crashMap[currentX][movePerson(1, currentX, currentY)[1]] == 10){
                for (int i = currentX; i < 1000 ;i++){  //1000 als Grenze des Kaufhauses, hier noch mal nach Variable schauen
                    if (simulationHandler.crashMap[currentX][i] == 10){
                        detourY = currentY - i;
                        break;
                    }
                }
            }
        }

        else if(goalY < currentY){
            //move up
            nextDirection = 3;
            if (simulationHandler.crashMap[currentX][movePerson(1, currentX, currentY)[1]] == 10){
                for (int i = currentX; i >= 0 ;i--){  //1000 als Grenze des Kaufhauses, hier noch mal nach Variable schauen
                    if (simulationHandler.crashMap[currentX][i] == 10){
                        detourY = currentY - i;
                        break;
                    }
                }
            }
        }
        else{
            nextDirection = 4;
        }

        //TO DO: check if  detour still applies

       // simulationHandler.crashMap[movePerson(nextDirection[temp], currentX, currentY)[0]][movePerson(nextDirection[temp], currentX, currentY)[1]]

return nextDirection;
    }

    public void findCompleteWayWithGoalMaze(int currentX, int currentY){

        //This method will compute the complete path -> Performance might be pretty bad
        //To save space only save the directions or compute direction to save performance -> try

        // The maze is the crashmap, with objects being 10 and everything else being okay
        boolean[][] wasHere = new boolean[1000][1000];
        boolean[][] correctPath = new boolean[1000][1000]; // The solution to the maze
        //int startX, startY; // Starting X and Y values of maze
        //int endX, endY;     // Ending X and Y values of maze

        for (int row = 0; row < simulationHandler.crashMap.length; row++)
            // Sets boolean Arrays to default values
            for (int col = 0; col < simulationHandler.crashMap[row].length; col++){
                wasHere[row][col] = false;
                correctPath[row][col] = false;
            }
        boolean b = recursiveSolve(currentX, currentY);
        // Will leave you with a boolean array (correctPath)
        // with the path indicated by true values.
        // If b is false, there is no solution to the maze

    }

    public boolean recursiveSolve(int x, int y) {
        if (x == goalX && y == goalY) return true; // If you reached the end
        if (simulationHandler.crashMap[x][y] == 10 || wasHere[x][y]) return false;
        // If you are on a wall or already were here
        wasHere[x][y] = true;
        if (x != 0) // Checks if not on left edge
            if (recursiveSolve(x-1, y)) { // Recalls method one to the left
                correctPath[x][y] = true; // Sets that path value to true;
                return true;
            }
        if (x != 1000 - 1) // Checks if not on right edge
            if (recursiveSolve(x+1, y)) { // Recalls method one to the right
                correctPath[x][y] = true;
                return true;
            }
        if (y != 0)  // Checks if not on top edge
            if (recursiveSolve(x, y-1)) { // Recalls method one up
                correctPath[x][y] = true;
                return true;
            }
        if (y != 1000 - 1) // Checks if not on bottom edge
            if (recursiveSolve(x, y+1)) { // Recalls method one down
                correctPath[x][y] = true;
                return true;
            }
        return false;
    }

    public int findPathfromMaze(int currentX, int currentY){

      if(correctPath[currentX + 1][currentY] == true){
          //right
          return 0;
      }
      else if(correctPath[currentX - 1][currentY] == true){
            //left
          return 2;
        }
      else if(correctPath[currentX][currentY + 1] == true){
          //down
          return 1;
      }
      else if(correctPath[currentX][currentY - 1] == true){
          //up
          return 3;
      }
      else{
          System.out.println("No path possible");
          return 4;
      }

    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;

    }


}