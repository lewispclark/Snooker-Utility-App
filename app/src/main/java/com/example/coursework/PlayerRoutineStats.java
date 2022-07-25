package com.example.coursework;

/**
 * PlayerFrameStats is used to store the stats of the player in the routine without storing them in
 * the player object
 */
public class PlayerRoutineStats {
    private String routineName;

    private int score;
    private int highestBreak;
    private int fastestTime;
    private int timesPlayed;

    //    Constructors
    public PlayerRoutineStats() {

    }

    public PlayerRoutineStats(String routineName, int score, int highestBreak, int fastestTime, int timesPlayed) {
        this.routineName = routineName;
        this.score = score;
        this.highestBreak = highestBreak;
        this.fastestTime = fastestTime;
        this.timesPlayed = timesPlayed;
    }

    //    Getters
    public String getRoutineName() {
        return this.routineName;
    }

    public int getScore() {
        return this.score;
    }

    public int getHighestBreak() {
        return this.highestBreak;
    }

    public int getFastestTime() {
        return this.fastestTime;
    }

    public int getTimesPlayed() {
        return this.timesPlayed;
    }

    //    Setters
    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHighestBreak(int highestBreak) {
        this.highestBreak = highestBreak;
    }

    public void setFastestTime(int fastestTime) {
        this.fastestTime = fastestTime;
    }

    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

}
