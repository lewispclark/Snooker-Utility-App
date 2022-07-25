package com.example.coursework;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

/**
 * Player object to store attributes and scores of player during games and routines
 */
public class Player implements Serializable {
    private String name;
    private String profileID;

    private int score;
    private int highestBreak;
    private int framesWon;

    //    Constructors
    public Player(String name, String profileID) {
        this.profileID = profileID;
        this.name = name;
        this.score = 0;
        this.highestBreak = 0;
        this.framesWon = 0;
    }

    public Player(String name, int highestBreak, int score, int framesWon) {
        this.name = name;
        this.highestBreak = highestBreak;
        this.score = score;
        this.framesWon = framesWon;
    }

    //    Getters
    public String getName() {
        return this.name;
    }

    public String getProfileID() {
        return this.profileID;
    }

    public int getScore() {
        return this.score;
    }

    public int getHighestBreak() {
        return this.highestBreak;
    }

    public int getFramesWon() {
        return this.framesWon;
    }

    //    Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHighestBreak(int highestBreak) {
        this.highestBreak = highestBreak;
    }

    public void setFramesWon(int framesWon) {
        this.framesWon = framesWon;
    }

    /**
     * Convert player stats to a frame stats object
     *
     * @return player frame stats object containing stats of player
     */
    public PlayerFrameStats toFrameStats() {
        return new PlayerFrameStats(this, this.score, this.highestBreak);
    }

    /**
     * Clear score and break of the player
     */
    public void clearScore() {
        this.score = 0;
        this.highestBreak = 0;
    }

    /**
     * Return stats as string for display
     *
     * @return stats as string
     */
    @NonNull
    public String toString(){
        return String.format(Locale.getDefault(), "Frames won: %d\n Score: %d\n Highest break: %d\n", framesWon, score, highestBreak);
    }
}
