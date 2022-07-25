package com.example.coursework;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

/**
 * The backend of the GameStats page.
 * Functionality is added to display the stats of the game and upload them to the database.
 */
public class GameStats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_stats);

//        Get intent sent from FrameStats screen
        Intent pastIntent = getIntent();
//        Get game object from intent
        Game game = (Game) pastIntent.getSerializableExtra("game");

//        Start upload game stats service to upload stats to the firestore database
        Intent serviceIntent = new Intent(this, UploadGameStats.class);
        serviceIntent.putExtra("game", game);
        startService(serviceIntent);

//        Get database instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Add game to database with a generated ID
        db.collection("game")
                .add(game)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

//        Set values on elements from layout
        ((TextView) findViewById(R.id.tv_player_1_name)).setText(game.getPlayer1().getName());
        ((TextView) findViewById(R.id.tv_player_2_name)).setText(game.getPlayer2().getName());
        ((TextView) findViewById(R.id.tv_highest_break)).setText(String.format(Locale.getDefault(), "%d", game.getHighestBreak()));
        ((TextView) findViewById(R.id.tv_fastest_frame)).setText(game.getFastestFrame());
        ((TextView) findViewById(R.id.tv_player_1_frames)).setText(String.format(Locale.getDefault(), "%d", game.getPlayer1().getFramesWon()));
        ((TextView) findViewById(R.id.tv_player_2_frames)).setText(String.format(Locale.getDefault(), "%d", game.getPlayer2().getFramesWon()));
        ((TextView) findViewById(R.id.tv_player_1_score)).setText(String.format(Locale.getDefault(), "%d", game.getPlayerTotalScore(1)));
        ((TextView) findViewById(R.id.tv_player_2_score)).setText(String.format(Locale.getDefault(), "%d", game.getPlayerTotalScore(2)));
        ((TextView) findViewById(R.id.tv_player_1_highest_break)).setText(String.format(Locale.getDefault(), "%d", game.getPlayerHighestBreak(1)));
        ((TextView) findViewById(R.id.tv_player_2_highest_break)).setText(String.format(Locale.getDefault(), "%d", game.getPlayerHighestBreak(2)));

//        When user clicks go back button, go back to game
        Button btnMenu = findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

//        When user clicks share button, share game stats
        Button btnShare = findViewById(R.id.btn_share);
        btnShare.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String body = game.toString();
            intent.putExtra(Intent.EXTRA_TEXT, "I just played this game!\n" + body);
            startActivity(Intent.createChooser(intent, "Share using:"));
        });
    }
}