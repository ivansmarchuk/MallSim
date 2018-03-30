package com.consultsim.mallsim.Model.StaticObjects;


import javafx.scene.paint.Color;

public class Objects {
  private int position[];
  private int id;
  private String label;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  private Color color;

  public int[] getPosition() {
    return position;
  }

  public void setPosition(int[] position) {
    this.position = position;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }
}
