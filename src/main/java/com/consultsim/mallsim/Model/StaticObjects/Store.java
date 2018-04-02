package com.consultsim.mallsim.Model.StaticObjects;

import com.consultsim.mallsim.Model.Position;

public class Store {
  private int id;
  private int position[];
  private int doorPosition[];
  private String label;
  private String interestingFor[];
  private String color;
  private int peopleCounter;
  private Position posLeftUpper;
  private Position posDownRight;

  public Position getPosLeftUpper() {
    return posLeftUpper;
  }

  public void setPosLeftUpper(Position posLeftUpper) {
    this.posLeftUpper = posLeftUpper;
  }

  public Position getPosDownRight() {
    return posDownRight;
  }

  public void setPosDownRight(Position posDownRight) {
    this.posDownRight = posDownRight;
  }

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

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public int getPeopleCounter() {
    return peopleCounter;
  }

  public void setPeopleCounter(int peopleCounter) {
    this.peopleCounter = peopleCounter;
  }
}
