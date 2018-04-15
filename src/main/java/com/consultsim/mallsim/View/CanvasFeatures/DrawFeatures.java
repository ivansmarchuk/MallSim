package com.consultsim.mallsim.View.CanvasFeatures;


import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Persons.Person;
import com.consultsim.mallsim.Model.StaticObjects.Spot;
import com.consultsim.mallsim.Model.Store;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class DrawFeatures {
    private static DrawFeatures drawInstance = null;


    public static DrawFeatures getDrawInstance() {
        if (drawInstance == null) {
            drawInstance = new DrawFeatures();
        }
        return drawInstance;
    }




    /**
     * @param gc            GraphicsContext
     * @param arrayOfStores array of stores from XML temmplate
     */
    public void drawStores(GraphicsContext gc, ArrayList<Store> arrayOfStores) {
        gc.setStroke(Color.BLACK);
        for (Store store : arrayOfStores) {
            gc.setFill(store.getColor());
            gc.fillRect(store.getPosition()[0], store.getPosition()[1],
                    store.getPosition()[2] - store.getPosition()[0],
                    store.getPosition()[3] - store.getPosition()[1]);
            gc.save();
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(gc.getFont().getName(), 13.0));
            gc.fillText(store.getLabel() + "\n", store.getPosition()[0] + 5,
                    store.getPosition()[1] + (store.getPosition()[3] - store.getPosition()[1])/2);

            gc.save();
            gc.setFill(Color.DARKGRAY);
            gc.setFont(new Font(gc.getFont().getName(), 10.0));
            gc.fillText("(Kapazität: "+ store.getPeopleCounter() + " max)", store.getPosition()[0] + 5,
                    store.getPosition()[1] + (store.getPosition()[3] - store.getPosition()[1])/2 + 15);
            gc.restore();

            gc.strokeLine(store.getDoorPosition()[0], store.getDoorPosition()[1], store.getDoorPosition()[2], store.getDoorPosition()[3]);
            //System.out.println(store.getId());
        }
    }

    /**
     * @param gc             GraphicsContext
     * @param arrayOfObjects array of objects from XML temmplate
     */
    public void drawObjects(GraphicsContext gc, ArrayList<Objects> arrayOfObjects) {
        for (Objects obj : arrayOfObjects) {
            if (obj.getLabel().contains("plant")) {
                gc.setFill(Color.GREEN);
            } else if (obj.getLabel().contains("trash bin")) {
                gc.setFill(Color.BROWN);
            }
            gc.fillRect(obj.getPosition()[0], obj.getPosition()[1],
                    obj.getPosition()[2] - obj.getPosition()[0],
                    obj.getPosition()[3] - obj.getPosition()[1]);
            //System.out.println("Object " + obj.getId());
        }

    }

    public void drawCrashMap(GraphicsContext gc, int[][] crashMap) {
        gc.setFill(Color.AQUA);
        gc.setLineWidth(0.5);
        for (int y = 0; y < 1000; y++) {
            for (int x = 0; x < 1000; x++) {
                if (crashMap[y][x] == 10){
                    gc.strokeOval(x, y, 1, 1);
                    //gc.fillOval(x, y, 1, 1);
                }
                //System.out.print(crashMap[y][x]+ " ");
            }
            // System.out.println();
        }
    }


    public void drawHotColdSpots(GraphicsContext gc, ArrayList<Spot> arrayOfSpots, double opasity) {

        for (Spot spot : arrayOfSpots) {
            if (spot.getSemaphor() == 1) {
                gc.setFill(Color.rgb(255, 64, 64, opasity));
            } else if (spot.getSemaphor() == 2) {
                gc.setFill(Color.rgb(0, 0, 139, opasity));
            }
            //gc.fillRect(0,0, 50,50);
            gc.getCanvas().setLayoutX(10);
            gc.fillRect(spot.getX(), spot.getY(), spot.getWidth(), spot.getHeigth());

        }
        //System.out.println("H/C " + s.getX() + " " + s.getY() + " " + s.getWidth() + " " + s.getHeigth());
    }

    /**
     * @param gc            GraphicsContext
     * @param arrayOfPerson array of Person
     */
    public void drawPersons(GraphicsContext gc, ArrayList<Person> arrayOfPerson) {
        for (Person p : arrayOfPerson) {
            gc.setFill(Color.BLACK);
            gc.fillOval(p.getCurrentPosition().getX(), p.getCurrentPosition().getY(), 5, 5);
        }
    }

}
