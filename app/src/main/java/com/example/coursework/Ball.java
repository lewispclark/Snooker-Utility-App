package com.example.coursework;

import java.io.Serializable;

/**
 * Represents a ball in the game
 */
public class Ball implements Serializable {
    //    Attributes
    private String colour;
    private int points;
    private int remaining;

    //    Constructor
    public Ball(String colour, int points, int remaining) {
        this.colour = colour;
        this.points = points;
        this.setRemaining(remaining);
    }

    //    Getters
    public String getColour() {
        return this.colour;
    }

    public int getPoints() {
        return points;
    }

    public int getRemaining() {
        return this.remaining;
    }

    //    Setters
    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }
}
