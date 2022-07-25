package com.example.coursework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * Backend for InRoutine page.
 * Functionality is added to update routine objects and the elements on the screen when balls
 * are potted. Functionality is added to the balls so they trigger functions on the routine.
 */
public class InRoutine extends AppCompatActivity {
    //    Variables used for elapsed time
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes;

    HashMap<String, BallButton> ballButtons = new HashMap<>();

    /**
     * Update screen based on routine object
     *
     * @param routine that is currently been played
     */
    public void updateScreen(Routine routine) {

//        If routine has ended, take player to the routine stats screen
        if (routine.hasEnded()) {
            Intent intent = new Intent(getApplicationContext(), RoutineStats.class);
//            Finalise elapsed time variable
            routine.setElapsedTime((int) (UpdateTime / 1000));
            intent.putExtra("routine", routine);
            startActivity(intent);
        }

//        Set text of elements on the screen
        ((TextView) findViewById(R.id.tv_score)).setText(String.format(Locale.getDefault(), "(%d)", routine.getPlayer().getScore()));
        ((TextView) findViewById(R.id.tv_current_break_label)).setText(String.format(Locale.getDefault(), "(%d)", routine.getCurrentBreak()));
        ((TextView) findViewById(R.id.tv_points_remaining)).setText(String.format(Locale.getDefault(), "(%d)", routine.getRemainingScore()));

//        Only enable the buttons that can be pressed without fouling
        for (String ball : routine.getBalls().keySet()) {
//            If there is at least one ball remaining of the colour specified
            if (Objects.requireNonNull(routine.getBalls().get(ball)).getRemaining() > 0) {
//                If the ball is not red
                if (!"Red".equals(ball)) {
//                    Set the ball to usable if red was the last pot or there are no reds left and it is the next colour
                    Objects.requireNonNull(ballButtons.get(ball)).usable("Red".equals(routine.getLastPot())
                            || (Objects.requireNonNull(routine.getBalls().get("Red")).getRemaining() < 1
                            && Frame.getNextColour(routine.getBalls()) == routine.getBalls().get(ball)));
                } else {
//                    Set the ball to usable if the last ball potted was not red
                    Objects.requireNonNull(ballButtons.get(ball)).usable(!"Red".equals(routine.getLastPot()));
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
        setContentView(R.layout.activity_in_routine);

//        Get intent from last page
        Intent pastIntent = getIntent();
//        Get routine from last page
        Routine routine = (Routine) pastIntent.getSerializableExtra("routine");
//        Set routine player
        routine.setPlayer((Player) pastIntent.getSerializableExtra("player"));

//        Set routine name text
        ((TextView) findViewById(R.id.tv_view_routine_title)).setText(routine.getName());
//        Set start time and start timer
        StartTime = SystemClock.uptimeMillis();
        startTimer();

//        Configure ballButtons map to point to elements on the layout
        ballButtons.put("Red", findViewById(R.id.btn_red_ball));
        ballButtons.put("Yellow", findViewById(R.id.btn_yellow_ball));
        ballButtons.put("Green", findViewById(R.id.btn_green_ball));
        ballButtons.put("Brown", findViewById(R.id.btn_brown_ball));
        ballButtons.put("Blue", findViewById(R.id.btn_blue_ball));
        ballButtons.put("Pink", findViewById(R.id.btn_pink_ball));
        ballButtons.put("Black", findViewById(R.id.btn_black_ball));

//        Update screen with routine
        updateScreen(routine);

//        Iterate through ball buttons and add functionality to each one
        for (String ballButtonColour : ballButtons.keySet()) {
//            When button is clicked, trigger pot with button colour
            Objects.requireNonNull(ballButtons.get(ballButtonColour)).setOnClickListener(view -> {
                routine.pot(ballButtonColour);
                updateScreen(routine);
            });
        }

//        Add functionality to miss ball
        Button btnMissBall = findViewById(R.id.btn_miss_ball);
        btnMissBall.setOnClickListener(view -> {
            routine.endBreak();
            updateScreen(routine);
        });

//        When user swipes right, go back to view routines
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(InRoutine.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), ViewRoutines.class);
                startActivity(intent);
            }
        });

//        When user swipes right, go back to view routines
        Button btnBack = findViewById(R.id.btn_go_back);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ViewRoutines.class);
            startActivity(intent);
        });

//        Send the user to the routine stats screen when they press the end routine
        Button btnEndRoutine = findViewById(R.id.btn_end_routine);
        btnEndRoutine.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RoutineStats.class);
//            End break in routine
            routine.endBreak();
//            Finalise elapsed time
            routine.setElapsedTime((int) (UpdateTime / 1000));
            intent.putExtra("routine", routine);
            startActivity(intent);
        });
    }

    /**
     * Start the timer
     */
    public void startTimer() {
        TextView elapsedTime = findViewById(R.id.tv_time_elapsed);
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