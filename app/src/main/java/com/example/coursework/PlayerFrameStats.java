package com.example.coursework;

import java.io.Serializable;

/**
 * PlayerFrameStats is used to store the stats of the player in the frame without storing them in
 * the player object
 */
public class PlayerFrameStats implements Serializable {
    private Player player;
    private int score;
    private int highestBreak;

    //    Constructor
    public PlayerFrameStats(Player player, int score, int highestBreak) {
        this.player = player;
        this.score = score;
        this.highestBreak = highestBreak;
    }

    //    Getters
    public Player getPlayer() {
        return this.player;
    }

    public int getScore() {
        return this.score;
    }

    public int getHighestBreak() {
        return this.highestBreak;
    }

    //    Setters
    public void getPlayer(Player player) {
        this.player = player;
    }

    public void getScore(int score) {
        this.score = score;
    }

    public void getHighestBreak(int highestBreak) {
        this.highestBreak = highestBreak;
    }
}
