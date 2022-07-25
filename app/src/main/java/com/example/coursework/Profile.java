package com.example.coursework;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a profile of a player
 * Stores the stats of the profile and contains methods that can be used to derive more stats
 */
public class Profile implements Serializable {
    private String name;

    private int highestBreak;
    private int framesPlayed;
    private int framesWon;
    private int gamesPlayed;
    private int gamesWon;

    private ArrayList<PlayerRoutineStats> routineStats;

    //    Constructors
    public Profile() {

    }

    public Profile(String name) {
        this.name = name;
        this.highestBreak = 0;
        this.framesPlayed = 0;
        this.framesWon = 0;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.routineStats = new ArrayList<>();
    }

    public Profile(String name, int highestBreak, int framesPlayed, int framesWon, int gamesPlayed, int gamesWon) {
        this.name = name;
        this.highestBreak = highestBreak;
        this.framesPlayed = framesPlayed;
        this.framesWon = framesWon;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.routineStats = new ArrayList<>();
    }

    //    Getters
    public String getName() {
        return this.name;
    }

    public int getHighestBreak() {
        return this.highestBreak;
    }

    public int getFramesPlayed() {
        return this.framesPlayed;
    }

    public int getFramesWon() {
        return this.framesWon;
    }

    public int getGamesPlayed() {
        return this.gamesPlayed;
    }

    public int getGamesWon() {
        return this.gamesWon;
    }

    public ArrayList<PlayerRoutineStats> getRoutineStats() {
        return this.routineStats;
    }

    //    Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setHighestBreak(int highestBreak) {
        this.highestBreak = highestBreak;
    }

    public void setFramesPlayed(int framesPlayed) {
        this.framesPlayed = framesPlayed;
    }

    public void setFramesWon(int framesWon) {
        this.framesWon = framesWon;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setRoutineStats(ArrayList<PlayerRoutineStats> routineStats) {
        this.routineStats = routineStats;
    }

//    Methods

    /**
     * Calculate the win % of frames or games
     *
     * @param framesgames 0 if frames, 1 if games
     * @return win % of frames or games
     */
    public double getWinPercentage(int framesgames) {
        switch (framesgames) {
//            If calculating frames win %
            case 0:
                if (this.framesPlayed == 0) return 0;
//                Return frames win %
                return ((double) this.framesWon / (double) this.framesPlayed) * 100;
//            If calculating games win %
            case 1:
                if (this.gamesPlayed == 0) return 0;
//                Return games win %
                return ((double) this.gamesWon / (double) this.gamesPlayed) * 100;
            default:
                return 0;
        }
    }

    @NonNull
    /*
      Return the stats of the profile as a string
     */
    public String toString() {
        return "highestBreak: " + highestBreak +
                ". framesPlayed: " + framesPlayed +
                ". framesWon: " + framesWon +
                ". gamesPlayed: " + gamesPlayed +
                ". gamesWon: " + gamesWon;
    }
}
