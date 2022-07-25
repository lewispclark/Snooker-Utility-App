package com.example.coursework;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a routine that can be played through by the user
 * Contains attributes that describe the routine and the state it is in.
 * Methods are used to derive extra fields from the routine and update fields.
 */
public class Routine implements Serializable {

    private String name;
    private String description;
    private String imageName;
    private String lastPot = null;

    private Player player;

    private int highestBreak;
    private int currentBreak;
    private int elapsedTime;

    private final HashMap<String, Ball> balls = new HashMap<>();

    //    Constructors
    public Routine() {
    }

    public Routine(String name, String description, String imageName) {
        this.name = name;
        this.description = description;
        this.imageName = imageName;

        this.highestBreak = 0;
        this.currentBreak = 0;
        this.elapsedTime = 0;
    }

//    Getters

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getImageName() {
        return this.imageName;
    }

    public String getLastPot() {
        return this.lastPot;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getHighestBreak() {
        return this.highestBreak;
    }

    public int getCurrentBreak() {
        return this.currentBreak;
    }

    public int getElapsedTime() {
        return this.elapsedTime;
    }

    public HashMap<String, Ball> getBalls() {
        return this.balls;
    }

//    Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setLastPot(String lastPot) {
        this.lastPot = lastPot;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setHighestBreak(int highestBreak) {
        this.highestBreak = highestBreak;
    }

    public void setCurrentBreak(int currentBreak) {
        this.currentBreak = currentBreak;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setBalls(int redBalls, int yellowBalls, int greenBalls, int brownBalls,
                         int blueBalls, int pinkBalls, int blackBalls) {
        this.balls.put("Red", new Ball("Red", 1, redBalls));
        this.balls.put("Yellow", new Ball("Yellow", 2, yellowBalls));
        this.balls.put("Green", new Ball("Green", 3, greenBalls));
        this.balls.put("Brown", new Ball("Brown", 4, brownBalls));
        this.balls.put("Blue", new Ball("Blue", 5, blueBalls));
        this.balls.put("Pink", new Ball("Pink", 6, pinkBalls));
        this.balls.put("Black", new Ball("Black", 7, blackBalls));
    }

//    Methods

    /**
     * Calculate how many points can possibly be scored if all reds are potted with blacks
     *
     * @return remaining score
     */
    public int getRemainingScore() {
//        Get total points that can be scored on reds
        int total = Objects.requireNonNull(this.balls.get("Red")).getRemaining() * (Objects.requireNonNull(this.balls.get("Red")).getPoints() + Objects.requireNonNull(this.balls.get("Black")).getPoints());

//        For each ball colour, add to the total
        for (String colour : balls.keySet()) {
            if (!"Red".equals(colour)) {
                total += Objects.requireNonNull(this.balls.get(colour)).getPoints() * Objects.requireNonNull(this.balls.get(colour)).getRemaining();
            }
        }

        return total;
    }

    /**
     * Check if the routine has ended
     *
     * @return true if the routine has ended, false otherwise
     */
    public boolean hasEnded() {
//        Check if each ball has more than 1 of it remaining in the list of balls
        for (String ballColour : this.balls.keySet()) {
//            If there is a ball remaining on the table, return false
            if (Objects.requireNonNull(balls.get(ballColour)).getRemaining() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Pot a ball in the routine, update the score and break of the player and routine
     *
     * @param colour of the ball that has been potted
     */
    public void pot(String colour) {
//        If there are reds left in the routine
        if (Objects.requireNonNull(this.balls.get("Red")).getRemaining() > 0) {
//            If the colour potted is red
            if ("Red".equals(colour)) {
//                Decrement the number of reds on the frame by 1
                this.balls.put("Red", new Ball("Red", 1, Objects.requireNonNull(this.balls.get("Red")).getRemaining() - 1));
            }
        } else if (!"Red".equals(this.lastPot)) {
//            Decrement the number of the colour ball on the frame by 1
            this.balls.put(colour, new Ball(colour, Objects.requireNonNull(this.balls.get(colour)).getPoints(), Objects.requireNonNull(this.balls.get(colour)).getRemaining() - 1));
        }
//        Update last pot
        this.lastPot = colour;
//        Update current player score and break
        this.player.setScore(this.player.getScore() + Objects.requireNonNull(this.balls.get(colour)).getPoints());
        this.currentBreak += Objects.requireNonNull(this.balls.get(colour)).getPoints();
//        End break if there are no balls left on the table
        if (Frame.getNextColour(this.balls) == null) {
            endBreak();
        }
    }

    /**
     * Update the highest break if necessary then end the break
     */
    public void endBreak() {
//        If highest  break, update highest break
        if (this.currentBreak > this.highestBreak) {
            this.highestBreak = this.currentBreak;
        }
        if (this.currentBreak > this.player.getHighestBreak()) {
            this.player.setHighestBreak(this.currentBreak);
        }
//        Set last pot to null
        this.lastPot = null;
//        End break
        this.currentBreak = 0;
    }

    /**
     * Output routine stats as string
     *
     * @return routine stats as string
     */
    @NonNull
    public String toString(){
        return String.format(Locale.getDefault(), "Routine name: %s\n Score: %d\n Highest break: %d\n Elapsed time: %s\n", name, player.getScore(), player.getHighestBreak(), Game.secondsToString(getElapsedTime()));
    }
}
