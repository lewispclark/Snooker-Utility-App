package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Backend of the ViewProfile page.
 * Contains methods to show profile stats and delete profile
 */
public class ViewProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

//        Get past intent from previous page
        Intent pastIntent = getIntent();
//        Get profile from past intent
        Profile profile = (Profile) pastIntent.getSerializableExtra("profile");

//        Get elements from front end and set values
        ((TextView) findViewById(R.id.tv_view_profile_title)).setText(profile.getName());
        ((TextView) findViewById(R.id.tv_highest_break)).setText(String.valueOf(profile.getHighestBreak()));
        ((TextView) findViewById(R.id.tv_frames_played)).setText(String.valueOf(profile.getFramesPlayed()));
        ((TextView) findViewById(R.id.tv_frames_won)).setText(String.valueOf(profile.getFramesWon()));
        ((TextView) findViewById(R.id.tv_frames_won_percentage)).setText(String.format(Locale.getDefault(), "%.2f", profile.getWinPercentage(0)));
        ((TextView) findViewById(R.id.tv_games_played)).setText(String.valueOf(profile.getGamesPlayed()));
        ((TextView) findViewById(R.id.tv_games_won)).setText(String.valueOf(profile.getGamesWon()));
        ((TextView) findViewById(R.id.tv_games_won_percentage)).setText(String.format(Locale.getDefault(), "%.2f", profile.getWinPercentage(1)));

        System.out.println(pastIntent.getStringExtra("profileID"));

//        If user clicks delete profile button, delete profile from local storage
        Button btnDeleteProfile = findViewById(R.id.btn_delete_profile);
        btnDeleteProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfilesMenu.class);
//            Get firestore database instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            Get profileID from past intent
            String profileID = pastIntent.getStringExtra("profileID");

            //noinspection MismatchedQueryAndUpdateOfCollection
            Map<String, Object> updates = new HashMap<>();
            updates.put(profileID, FieldValue.delete());

//            Delete player from player collection on firestore
            db.collection("player").document(profileID)
                    .delete()
                    .addOnSuccessListener(aVoid -> System.out.println("DocumentSnapshot successfully deleted!"))
                    .addOnFailureListener(e -> System.out.println("Error deleting document"));

//            Navigate back to profiles menu
            startActivity(intent);
        });

//        When user swipes right, go back to profiles menu page
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(ViewProfile.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), ProfilesMenu.class);
                startActivity(intent);
            }
        });

//        When user presses go back button, go back to profiles menu page
        Button btnBack = findViewById(R.id.btn_go_back);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfilesMenu.class);
            startActivity(intent);
        });
    }
}