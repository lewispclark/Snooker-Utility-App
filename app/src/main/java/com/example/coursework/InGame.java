package com.example.coursework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * Backend for InGame page.
 * Functionality is added to update frame and game objects and the elements on the screen when balls
 * are potted. Functionality is added to the balls so they trigger functions on the frame.
 */
public class InGame extends AppCompatActivity {
    //    Variables used for elapsed time
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes;

    HashMap<String, BallButton> ballButtons = new HashMap<>();

    /**
     * Update screen based on game and frame objects
     *
     * @param game  that the frame is a part of
     * @param frame that is currently been played
     */
    public void updateScreen(Game game, Frame frame) {
//        If frame has ended, take player to the frame stats screen
        if (frame.checkFrameEnd()) {
            Intent intent = new Intent(getApplicationContext(), FrameStats.class);
//            Finalise elapsed time variable and set last frame
            frame.setElapsedTime((int) (UpdateTime / 1000));
            frame.setLastFrame(game.checkGameEnd());
            intent.putExtra("frame", frame);
            intent.putExtra("game", game);
            startActivity(intent);
        }

//        Update numbers on screen
        ((TextView) findViewById(R.id.tv_player_1_score)).setText(String.format(Locale.getDefault(), "%d", frame.getPlayer1().getScore()));
        ((TextView) findViewById(R.id.tv_player_2_score)).setText(String.format(Locale.getDefault(), "%d", frame.getPlayer2().getScore()));
        ((TextView) findViewById(R.id.tv_current_break_label)).setText(String.format(Locale.getDefault(), "%d", frame.getCurrentBreak()));
        ((TextView) findViewById(R.id.tv_frame_no)).setText(String.format(Locale.getDefault(), "%d", game.getCurrentFrame()));
        ((TextView) findViewById(R.id.tv_points_difference)).setText(String.format(Locale.getDefault(), "%d", frame.getScoreDifference()));
        ((TextView) findViewById(R.id.tv_points_ahead_or_behind)).setText(frame.getCurrentPlayer().getScore() > frame.getOpponent(frame.getCurrentPlayer()).getScore() ? "Ahead" : "Behind");
        ((TextView) findViewById(R.id.tv_points_remaining)).setText(String.format(Locale.getDefault(), "%d", frame.getRemainingScore()));
        ((TextView) findViewById(R.id.tv_snookers_required)).setText(String.format(Locale.getDefault(), "%d", frame.getSnookersRequired(frame.getCurrentPlayer())));
        ((TextView) findViewById(R.id.tv_player_1_frames)).setText(String.format(Locale.getDefault(), "%d", game.getPlayer1().getFramesWon()));
        ((TextView) findViewById(R.id.tv_player_2_frames)).setText(String.format(Locale.getDefault(), "%d", game.getPlayer2().getFramesWon()));

//        Make pointer visible on current player to show who's turn it is
        if (frame.getCurrentPlayer() == frame.getPlayer1()) {
            (findViewById(R.id.img_player_1_turn)).setVisibility(View.VISIBLE);
            (findViewById(R.id.img_player_2_turn)).setVisibility(View.INVISIBLE);
        } else {
            (findViewById(R.id.img_player_1_turn)).setVisibility(View.INVISIBLE);
            (findViewById(R.id.img_player_2_turn)).setVisibility(View.VISIBLE);
        }


//        Only enable the buttons that can be pressed without fouling
        for (String ball : frame.getBalls().keySet()) {
//            If there is at least one ball remaining of the colour specified
            if (Objects.requireNonNull(frame.getBalls().get(ball)).getRemaining() > 0) {
//                If the ball is not red
                if (!"Red".equals(ball)) {
//                    Set the ball to usable if red was the last pot or there are no reds left and it is the next colour
                    Objects.requireNonNull(ballButtons.get(ball)).usable("Red".equals(frame.getLastPot()) || (Objects.requireNonNull(frame.getBalls().get("Red")).getRemaining() < 1 && Frame.getNextColour(frame.getBalls()) == frame.getBalls().get(ball)));
                } else {
//                    Set the ball to usable if the last ball potted was not red
                    Objects.requireNonNull(ballButtons.get(ball)).usable(!"Red".equals(frame.getLastPot()));
                }
            } else {
//                Set the ball to unusable
                Objects.requireNonNull(ballButtons.get(ball)).usable(false);
            }
        }
    }

    @SuppressLint("FindViewByIdCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);

//        Get intent from previous page
        Intent pastIntent = getIntent();
//        Get game and frame objects from intent
        Game game = (Game) pastIntent.getSerializableExtra("game");
        game.getFrames().get(game.getFrames().size() - 1).setLastFrame(true);
        Frame frame = game.getFrames().get(game.getCurrentFrame() - 1);

//        Set start time and start timer
        StartTime = SystemClock.uptimeMillis();
        startTimer();

//        Set values of elements on the page
        ((TextView) findViewById(R.id.tv_player_1_name)).setText(frame.getPlayer1().getName());
        ((TextView) findViewById(R.id.tv_player_2_name)).setText(frame.getPlayer2().getName());
        ((TextView) findViewById(R.id.tv_total_frames)).setText(String.format(Locale.getDefault(), "(%d)", game.getFrames().size()));

//        Configure ballButtons map to point to elements on the layout
        ballButtons.put("Red", findViewById(R.id.btn_red_ball));
        ballButtons.put("Yellow", findViewById(R.id.btn_yellow_ball));
        ballButtons.put("Green", findViewById(R.id.btn_green_ball));
        ballButtons.put("Brown", findViewById(R.id.btn_brown_ball));
        ballButtons.put("Blue", findViewById(R.id.btn_blue_ball));
        ballButtons.put("Pink", findViewById(R.id.btn_pink_ball));
        ballButtons.put("Black", findViewById(R.id.btn_black_ball));

//        Update the screen
        updateScreen(game, frame);

//        Iterate through ball buttons and add functionality to each one
        for (String ballButtonColour : ballButtons.keySet()) {
//            When button is clicked, trigger pot with button colour
            Objects.requireNonNull(ballButtons.get(ballButtonColour)).setOnClickListener(view -> {
                frame.pot(ballButtonColour);
                updateScreen(game, frame);
            });
        }

//        Add functionality to miss ball
        Button btnMissBall = findViewById(R.id.btn_miss_ball);
        btnMissBall.setOnClickListener(view -> {
            frame.endBreak();
            updateScreen(game, frame);
        });

//        Add functionality to foul button
        Button btnAddFoul = findViewById(R.id.btn_foul_ball);
        btnAddFoul.setOnClickListener(view -> {
//            Take user to foul page
            Intent intent = new Intent(getApplicationContext(), AddFoul.class);
            intent.putExtra("game", game);
            intent.putExtra("frame", frame);
            startActivity(intent);
        });

//        When user swipes right, go back to game
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(InGame.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), NewGame.class);
                startActivity(intent);
            }
        });

//        Go back to the new game page when pressed
        Button btnBack = findViewById(R.id.btn_go_back_in_game);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NewGame.class);
            startActivity(intent);
        });

//        Send the user to the game stats screen when they press the end game button
        Button btnEndGame = findViewById(R.id.btn_end_game);
        btnEndGame.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NewGame.class);
            startActivity(intent);
        });
    }

    /**
     * Start the timer
     */
    public void startTimer() {
        TextView elapsedTime = findViewById(R.id.tv_elapsed_time);
        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                MillisecondTime = SystemClock.uptimeMillis() - StartTime;
                UpdateTime = TimeBuff + MillisecondTime;
                Seconds = (int) (UpdateTime / 1000);
                Minutes = Seconds / 60;
                Seconds = Seconds % 60;
//                Update the text on the elapsed time section
                elapsedTime.setText(String.format(Locale.getDefault(), "%d:%02d", Minutes, Seconds));

                handler.postDelayed(this, 0);
            }
        });
    }
}