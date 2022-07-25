package com.example.coursework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Backend of the ViewRoutine page.
 * Contains methods to show routine and go to next page
 */
public class ViewRoutine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routine);

//        Get intent from last page
        Intent pastIntent = getIntent();
//        Get routine from past intent
        Routine routine = (Routine) pastIntent.getSerializableExtra("routine");

//        Get elements from layout and set values
        ((TextView) findViewById(R.id.tv_view_routines_title)).setText(routine.getName());
        ((TextView) findViewById(R.id.tv_routine_description)).setText(routine.getDescription());
        Spinner player = findViewById(R.id.dd_player);

        String string;

//        Get context
        Context ctx = getApplicationContext();

//        Get profile IDs from local storage
        ArrayList<String> profileIDs = new ArrayList<>();
        try (FileInputStream fileInputStream = ctx.openFileInput("savedData.txt")) {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                try {
                    if ((string = bufferedReader.readLine()) == null) break;
                    profileIDs.add(string);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Get firestore database instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Set adapter to profile names for dropdown
        ArrayList<String> profileNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, profileNames);

//        Map profile names to profile ID
        HashMap<String, String> nameID = new HashMap<>();
        for (String profileID : profileIDs) {
//            Get profile from firestore by ID
            db.collection("player")
                    .document(profileID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
//                            Get document from database
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
//                                If document exists, get profile details
                                Map<String, Object> profileDetails = document.getData();
//                                Put name and profile ID into map and add name to adapter
                                nameID.put(Objects.requireNonNull(Objects.requireNonNull(profileDetails).get("name")).toString(), profileID);
                                adapter.add(Objects.requireNonNull(profileDetails.get("name")).toString());
                            } else {
//                                If document does not exist then remove profile ID from list
                                profileIDs.remove(profileID);
                            }
                        } else {
                            System.out.println("get failed with " + task.getException());
                        }
                    });
        }

//        Set player dropdown menu adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        player.setAdapter(adapter);

//        Set routine image
        int drawableId = this.getResources().getIdentifier(routine.getImageName(), "mipmap", ctx.getPackageName());
        ((ImageView) findViewById(R.id.img_snooker_table)).setImageResource(drawableId);

//        When start routine button is clicked, begin routine and go to routine page
        Button btnStartRoutine = findViewById(R.id.btn_start_routine);
        btnStartRoutine.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), InRoutine.class);
            intent.putExtra("routine", routine);
            intent.putExtra("player", new Player(player.getSelectedItem().toString(), nameID.get(player.getSelectedItem().toString())));
            startActivity(intent);
        });

//        When leaderboard button is clicked, go to leaderboard page
        Button btnLeaderboard = findViewById(R.id.btn_leaderboard);
        btnLeaderboard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Leaderboard.class);
            intent.putExtra("routineName", routine.getName());
            startActivity(intent);
        });

//        When user swipes right, go back to view routines page
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(ViewRoutine.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), ViewRoutines.class);
                startActivity(intent);
            }
        });

//        When user clicks go back button, go back to view routines page
        Button btnBack = findViewById(R.id.btn_go_back);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ViewRoutines.class);
            startActivity(intent);
        });
    }
}