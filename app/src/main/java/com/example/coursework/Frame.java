package com.example.coursework;

import static java.lang.Math.abs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a frame in the snooker game, contains attributes to represent the frame state and players
 * Methods are implemented to return various required values for the frame.
 */
public class Frame implements Serializable {
    private HashMap<String, Ball> balls = new HashMap<>();

    private int highestBreak;
    private int currentBreak;
    private int snookersRequiredSetting;
    private int elapsedTime;

    private Player player1;
    private Player player2;
    private Player frameWinner;
    private Player currentPlayer;
    private PlayerFrameStats player1FrameStats;
    private PlayerFrameStats player2FrameStats;

    private String lastPot = null;
    private boolean lastFrame;

    public Frame(Player player1, Player player2, int snookersRequiredSetting) {
        this.highestBreak = 0;
        this.currentBreak = 0;
        this.snookersRequiredSetting = snookersRequiredSetting;
        this.elapsedTime = 0;

        this.player1 = player1;
        this.player2 = player2;

        this.lastFrame = false;
        this.currentPlayer = this.player1;

        this.balls.put("Red", new Ball("Red", 1, 15));
        this.balls.put("Yellow", new Ball("Yellow", 2, 1));
        this.balls.put("Green", new Ball("Green", 3, 1));
        this.balls.put("Brown", new Ball("Brown", 4, 1));
        this.balls.put("Blue", new Ball("Blue", 5, 1));
        this.balls.put("Pink", new Ball("Pink", 6, 1));
        this.balls.put("Black", new Ball("Black", 7, 1));
    }

    //    Getters
    public HashMap<String, Ball> getBalls() {
        return this.balls;
    }

    public int getHighestBreak() {
        return this.highestBreak;
    }

    public int getCurrentBreak() {
        return this.currentBreak;
    }

    public void setSnookersRequiredSetting(int snookersRequiredSetting) {
        this.snookersRequiredSetting = snookersRequiredSetting;
    }

    public int getElapsedTime() {
        return this.elapsedTime;
    }

    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public Player getFrameWinner() {
        return this.frameWinner;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public PlayerFrameStats getPlayer1FrameStats() {
        return this.player1FrameStats;
    }

    public PlayerFrameStats getPlayer2FrameStats() {
        return this.player2FrameStats;
    }

    public String getLastPot() {
        return this.lastPot;
    }

    public boolean getLastFrame() {
        return this.lastFrame;
    }

    //    Setters
    public void setBalls(HashMap<String, Ball> balls) {
        this.balls = balls;
    }

    public void setHighestBreak(int highestBreak) {
        this.highestBreak = highestBreak;
    }

    public void setCurrentBreak(int currentBreak) {
        this.currentBreak = currentBreak;
    }

    public void setSnookersRequired(int snookersRequiredSetting) {
        this.snookersRequiredSetting = snookersRequiredSetting;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setFrameWinner(Player frameWinner) {
        this.frameWinner = frameWinner;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPlayer1FrameStats(PlayerFrameStats player1FrameStats) {
        this.player1FrameStats = player1FrameStats;
    }

    public void setPlayer2FrameStats(PlayerFrameStats player2FrameStats) {
        this.player2FrameStats = player2FrameStats;
    }

    public void setLastPot(String lastPot) {
        this.lastPot = lastPot;
    }

    public void setLastFrame(boolean lastFrame) {
        this.lastFrame = lastFrame;
    }

    /**
     * Get opponent of player given
     *
     * @param player to find opponent of
     * @return opponent of player given
     */
    public Player getOpponent(Player player) {
        if (player == player1) {
            return player2;
        } else {
            return player1;
        }
    }

    /**
     * Calculate how many points can possibly be scored if all reds are potted with blacks
     *
     * @return remaining score
     */
    public int getRemainingScore() {
//        Get total points that can be scored on reds
        int total = Objects.requireNonNull(this.balls.get("Red")).getRemaining() * (Objects.requireNonNull(this.balls.get("Red")).getPoints() + Objects.requireNonNull(this.balls.get("Black")).getPoints());


        for (String colour : balls.keySet()) {
            if (!"Red".equals(colour)) {
                total += Objects.requireNonNull(this.balls.get(colour)).getPoints() * Objects.requireNonNull(this.balls.get(colour)).getRemaining();
            }
        }

        return total;
    }

    /**
     * Get the lowest colour ball that can be legally potted
     *
     * @return next colour ball
     */
    public static Ball getNextColour(HashMap<String, Ball> balls) {
        for (String colour : Game.coloursOrder) {
            if (Objects.requireNonNull(balls.get(colour)).getRemaining() > 0) {
                return balls.get(colour);
            }
        }
//        If no colours are left, return null
        return null;
    }

    /**
     * Calculate player score difference
     *
     * @return score difference
     */
    public int getScoreDifference() {
        return abs(player1.getScore() - player2.getScore());
    }

    /**
     * Calculate snookers required for specified player
     *
     * @param player to find snookers required for
     * @return snookers required for specified player
     */
    public int getSnookersRequired(Player player) {
//        If player is ahead, return 0
        if (getRemainingScore() > getOpponent(player).getScore() - player.getScore()) return 0;
        double behind = getScoreDifference() - getRemainingScore();

//        If no colour balls are left return null
        if (getNextColour(this.balls) == null) {
            return -1;
        }
//        Calculate how many points will be scored from each snooker (minimum)
        double snookerPoints = Math.max(4, Objects.requireNonNull(getNextColour(this.balls)).getPoints());
//        Round snookers required to a whole number
        return (int) Math.ceil(behind / snookerPoints);
    }

    /**
     * Check if frame has ended
     *
     * @return true if frame is over, false otherwise
     */
    public boolean checkFrameEnd() {
        boolean end = false;
//        If a player needs more snookers than specified in the rules, set winners and end frame. Else continue frame
        if (((getSnookersRequired(this.player1) == -1 || getSnookersRequired(this.player1) >= this.snookersRequiredSetting) || (getSnookersRequired(this.player2) == -1 || getSnookersRequired(this.player2) >= this.snookersRequiredSetting)) && this.currentBreak == 0) {
            end = true;
            if (player1.getScore() > player2.getScore()) {
                this.frameWinner = player1;
                this.player1.setFramesWon(this.player1.getFramesWon() + 1);
            } else {
                this.frameWinner = player2;
                this.player2.setFramesWon(this.player2.getFramesWon() + 1);
            }
            this.player1FrameStats = this.player1.toFrameStats();
            this.player2FrameStats = this.player2.toFrameStats();
        }

        return end;
    }

    /**
     * Pot a ball in the frame, update the scores and breaks of the players and frame
     *
     * @param colour of the ball that has been potted
     */
    public void pot(String colour) {
//        If there are reds left in the frame
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
//        Update current players score and break
        this.currentPlayer.setScore(this.currentPlayer.getScore() + Objects.requireNonNull(this.balls.get(colour)).getPoints());
        this.currentBreak += Objects.requireNonNull(this.balls.get(colour)).getPoints();
//        End break if there are no balls left on the table
        if (getNextColour(this.balls) == null) {
            endBreak();
        }
    }

    /**
     * Update score of opponent based on colour of ball that was fouled on then end break
     *
     * @param colour of the ball that was fouled on
     */
    public void foul(String colour) {
        getOpponent(currentPlayer).setScore(getOpponent(currentPlayer).getScore() + (Math.max(Objects.requireNonNull(this.balls.get(colour)).getPoints(), 4)));
        endBreak();
    }

    /**
     * Update the highest breaks if necessary, then change the turn and end the break
     */
    public void endBreak() {
//        If highest player or frame break, update highest breaks
        if (this.currentBreak > this.highestBreak) {
            this.highestBreak = this.currentBreak;
        }
        if (this.currentBreak > this.currentPlayer.getHighestBreak()) {
            this.currentPlayer.setHighestBreak(this.currentBreak);
        }
//        Change turn
        this.currentPlayer = getOpponent(currentPlayer);

        this.lastPot = null;
//        End break
        this.currentBreak = 0;
    }
}
