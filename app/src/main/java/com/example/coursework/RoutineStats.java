package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * Back end for the RoutineStats page
 * Contains logic for displaying the stats of the routine on the layout
 */
public class RoutineStats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_stats);

//        Get intent from last page
        Intent pastIntent = getIntent();
//        Get routine from past intent
        Routine routine = (Routine) pastIntent.getSerializableExtra("routine");

//        Get elements from layout and set values
        ((TextView) findViewById(R.id.tv_elapsed_time)).setText(Game.secondsToString(routine.getElapsedTime()));
        ((TextView) findViewById(R.id.tv_highest_break)).setText(String.format(Locale.getDefault(), "%d", routine.getHighestBreak()));

//        Trigger update routine stats service to update routine stats in background
        Intent serviceIntent = new Intent(this, UploadRoutineStats.class);
        serviceIntent.putExtra("routine", routine);
        startService(serviceIntent);

//        When user swipes right, go back to view routines page
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(RoutineStats.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), ViewRoutines.class);
                startActivity(intent);
            }
        });

//        When user presses go back button, go back to view routines page
        Button btnBack = findViewById(R.id.btn_go_back);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ViewRoutines.class);
            startActivity(intent);
        });

//        When user clicks share button, share routine stats
        Button btnShare = findViewById(R.id.btn_share);
        btnShare.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String body = routine.toString();
            intent.putExtra(Intent.EXTRA_TEXT, "I just played this routine!\n" + body);
            startActivity(Intent.createChooser(intent, "Share using:"));
        });
    }
}