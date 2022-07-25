package com.example.coursework;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Represents the game of snooker
 * Contains attributes to represent the state of the game and the frames within it.
 * Methods are implemented to return various required values for the game
 */
public class Game implements Serializable {
    //    Order in which the colours should be potted
    public static String[] coloursOrder = {"Yellow", "Green", "Brown", "Blue", "Pink", "Black"};
    //    Attributes
    private Player player1;
    private Player player2;
    private List<Frame> frames = new ArrayList<>();
    private Player gameWinner;
    private Player gameLoser;
    private int currentFrame;
    private int snookersRequired;

    //    Constructor
    public Game(int frameCount, String player1, String player1ProfileID, String player2, String player2ProfileID, int snookersRequired) {
        this.player1 = new Player(player1, player1ProfileID);
        this.player2 = new Player(player2, player2ProfileID);
        this.currentFrame = 1;
        this.snookersRequired = snookersRequired;
//        Generate the list of frames
        for (int i = 0; i < frameCount; i++) {
            this.frames.add(new Frame(this.player1, this.player2, this.snookersRequired));
        }
    }

    //    Getters
    public List<Frame> getFrames() {
        return this.frames;
    }

    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public Player getGameWinner() {
        return this.gameWinner;
    }

    public Player getGameLoser() {
        return this.gameLoser;
    }

    public int getCurrentFrame() {
        return this.currentFrame;
    }

    public int getSnookersRequired() {
        return this.snookersRequired;
    }

    //    Setters
    public void setFrames(List<Frame> frames) {
        this.frames = frames;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setGameWinner(Player gameWinner) {
        this.gameWinner = gameWinner;
    }

    public void setGameLoser(Player gameLoser) {
        this.gameLoser = gameLoser;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void setSnookersRequired(int snookersRequired) {
        this.snookersRequired = snookersRequired;
    }

    /**
     * Resets the scores of each players and increments the frame number
     */
    public void nextFrame() {
        this.player1.clearScore();
        this.player2.clearScore();
        this.currentFrame += 1;
    }

    /**
     * Gets the fastest frame of all the frames in the game
     *
     * @return the elapsed time of the fastest frame of the game
     */
    public String getFastestFrame() {
//        Set fastest frame to the time of frame 1
        int fastestFrame = this.frames.get(0).getElapsedTime();
//        Check if frame with faster time than frame 1 exists
        for (int i = 1; i < this.player1.getFramesWon() + this.player2.getFramesWon(); i++) {
            if (this.frames.get(i).getElapsedTime() < fastestFrame) {
                fastestFrame = this.frames.get(i).getElapsedTime();
            }
        }
//        Return fastest elapsed time formatted as a time
        return secondsToString(fastestFrame);
    }

    /**
     * Get the highest break of the player over all the frames
     *
     * @param player to find the highest break of
     * @return the highest break of the player over all the frames
     */
    public int getPlayerHighestBreak(int player) {
        int highestBreak = 0;

        for (int i = 0; i < this.player1.getFramesWon() + this.player2.getFramesWon(); i++) {
            if (this.frames.get(i).getElapsedTime() > 0) {
                if (player == 1 && this.frames.get(i).getPlayer1FrameStats().getHighestBreak() > highestBreak) {
                    highestBreak = this.frames.get(i).getPlayer1FrameStats().getHighestBreak();
                } else if (player == 2 && this.frames.get(i).getPlayer2FrameStats().getHighestBreak() > highestBreak) {
                    highestBreak = this.frames.get(i).getPlayer2FrameStats().getHighestBreak();
                }
            }
        }

        return highestBreak;
    }

    /**
     * Get the highest break of all the frames in the game
     *
     * @return the highest break of all the frames in the game
     */
    public int getHighestBreak() {
        return Math.max(getPlayerHighestBreak(1), getPlayerHighestBreak(2));
    }

    /**
     * Get the total score for the specified player from all the frames in the game
     *
     * @param player to get the total score of
     * @return total score of the specified player
     */
    public int getPlayerTotalScore(int player) {
        int totalScore = 0;
//        Add the total score of the player for each frame to the total
        for (int i = 0; i < this.player1.getFramesWon() + this.player2.getFramesWon(); i++) {
            if (player == 1) {
                totalScore += this.frames.get(i).getPlayer1FrameStats().getScore();
            } else {
                totalScore += this.frames.get(i).getPlayer2FrameStats().getScore();
            }
        }
        return totalScore;
    }

    /**
     * Convert the seconds in value to a formatted string in minutes and seconds
     *
     * @param seconds integer value
     * @return formatted time in minutes and seconds
     */
    public static String secondsToString(int seconds) {
        int Min = seconds / 60;
        int Sec = seconds % 60;

        return "" + Min + ":" + String.format(Locale.getDefault(), "%02d", Sec);
    }

    /**
     * Check if the game has ended based on the number of frames that have been won
     *
     * @return true if the game has ended, false otherwise
     */
    public boolean checkGameEnd() {
//        Set default to false
        boolean gameEnd = false;
//        If player has won more than half of the frames to be played
        if (this.player1.getFramesWon() > Math.floor((double) frames.size() / 2)) {
//            Set the winner and loser of the game and return true
            this.gameWinner = player1;
            this.gameLoser = player2;
            gameEnd = true;
        } else if (this.player2.getFramesWon() > Math.floor((double) frames.size() / 2)) {
            this.gameWinner = player2;
            this.gameLoser = player1;
            gameEnd = true;
        }

        return gameEnd;
    }

    /**
     * Output game as string
     *
     * @return game stats as string
     */
    @NonNull
    public String toString(){
        return String.format("%s stats:\n%s.\n%s stats:\n%s", player1.getName(), player1.toString(), player2.getName(), player2.toString());
    }
}
