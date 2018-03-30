package com.consultsim.mallsim.Model.StaticObjects;


import javafx.scene.paint.Color;

public class Store {
  private int id;
  private int position[];
  private int doorPosition[];
  private String label;
  private String interestingFor[];
  private Color color;
  private int peopleCounter;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Store(){
    position = new int[4];
    doorPosition = new int[4];
  }

  public int countPeopleInStore(){

    return 0;
  }

  public int[] getPosition() {
    return position;
  }

  public void setPosition(int[] position) {
    this.position = position;
  }

  public int[] getDoorPosition() {
    return doorPosition;
  }

  public void setDoorPosition(int[] doorPosition) {
    this.doorPosition = doorPosition;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String[] getInterestingFor() {
    return interestingFor;
  }

  public void setInterestingFor(String[] interestingFor) {
    this.interestingFor = interestingFor;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public int getPeopleCounter() {
    return peopleCounter;
  }

  public void setPeopleCounter(int peopleCounter) {
    this.peopleCounter = peopleCounter;
  }
}
