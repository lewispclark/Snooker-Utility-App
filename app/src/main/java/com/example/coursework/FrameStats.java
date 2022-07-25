package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * This is the backend of the FrameStats page.
 * Here the functionality is added to show the stats from the completed frame on the screen and
 * allow the user to either progress to the next frame or the game stats.
 */
public class FrameStats extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_stats);

//        Get intent sent from InGame
        Intent pastIntent = getIntent();
//        Get game and frame objects from intent
        Frame frame = (Frame) pastIntent.getSerializableExtra("frame");
        Game game = (Game) pastIntent.getSerializableExtra("game");

        Button btnNextFrame = findViewById(R.id.btn_next_frame);
//        If frame is last frame, show game stats button
        if (frame.getLastFrame()) {
            btnNextFrame.setText(R.string.game_stats);
            btnNextFrame.setOnClickListener(view -> {
                Intent serviceIntent = new Intent(getApplicationContext(), GameStats.class);
                serviceIntent.putExtra("game", game);
                startActivity(serviceIntent);
            });
        } else {
//            Show default (next frame) button
            btnNextFrame.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), InGame.class);
                game.nextFrame();
                intent.putExtra("game", game);
                startActivity(intent);
            });
        }

//        Get elements from layout file
        TextView player1Name = findViewById(R.id.tv_player_1_name);
        TextView player2Name = findViewById(R.id.tv_player_2_name);
        player1Name.setText(frame.getPlayer1().getName());
        player2Name.setText(frame.getPlayer2().getName());
//        Show stats on screen elements
        ((TextView) findViewById(R.id.tv_elapsed_time)).setText(Game.secondsToString(frame.getElapsedTime()));
        ((TextView) findViewById(R.id.tv_highest_break)).setText(String.format(Locale.getDefault(), "%d", frame.getHighestBreak()));
        ((TextView) findViewById(R.id.tv_player_1_score)).setText(String.format(Locale.getDefault(), "%d", frame.getPlayer1().getScore()));
        ((TextView) findViewById(R.id.tv_player_2_score)).setText(String.format(Locale.getDefault(), "%d", frame.getPlayer2().getScore()));
        ((TextView) findViewById(R.id.tv_player_1_highest_break)).setText(String.format(Locale.getDefault(), "%d", frame.getPlayer1().getHighestBreak()));
        ((TextView) findViewById(R.id.tv_player_2_highest_break)).setText(String.format(Locale.getDefault(), "%d", frame.getPlayer2().getHighestBreak()));
    }
}