package com.example.coursework;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

public class NewGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

//        Get spinner elements from layout
        Spinner player1 = findViewById(R.id.dd_player_1);
        Spinner player2 = findViewById(R.id.dd_player_2);

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
        ArrayList<String> profileNames = new ArrayList<>();
//        Set adapter to profile names list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, profileNames);

        HashMap<String, String> nameID = new HashMap<>();

//        For each profile ID in list of profile IDs
        for (String profileID : profileIDs) {
//            Get player from database with profile ID
            db.collection("player")
                    .document(profileID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
//                            Get profile document
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
//                                Put profile name in list and add to adapter
                                Map<String, Object> profileDetails = document.getData();
                                nameID.put(Objects.requireNonNull(Objects.requireNonNull(profileDetails).get("name")).toString(), profileID);
                                adapter.add(Objects.requireNonNull(profileDetails.get("name")).toString());
                            } else {
//                                Remove from list of profileIDs
                                profileIDs.remove(profileID);
                                System.out.println("No such document");
                            }
                        } else {
                            System.out.println("get failed with " + task.getException());
                        }
                    });
        }

//        Set adapters of dropdown lists
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        player1.setAdapter(adapter);
        player2.setAdapter(adapter);

//        Get spinner elements from layout
        Spinner noOfFrames = findViewById(R.id.dd_no_of_frames);
        Spinner endFrameAt = findViewById(R.id.dd_end_frame_at);

//        Get shared preferences
        SharedPreferences myPref =
                getSharedPreferences("game_players", MODE_PRIVATE);

//        Get dropdown preferences
        int lastPlayer1 = myPref.getInt("lastPlayer1", 0);
        int lastPlayer2 = myPref.getInt("lastPlayer2", 0);
        int lastNoOfFrames = myPref.getInt("lastNoOfFrames", 0);
        int lastEndFrameAt = myPref.getInt("lastEndFrameAt", 0);

//        Set dropdown selected items to shared preferences
        player1.setSelection(lastPlayer1);
        player2.setSelection(lastPlayer2);
        noOfFrames.setSelection(lastNoOfFrames);
        endFrameAt.setSelection(lastEndFrameAt);

//        When user swipes right, go back to new game page
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(NewGame.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

//        When presses go back button, go back to game
        Button btnBack = findViewById(R.id.btn_go_back_new_game);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

//        When user presses create game button, create game with settings chosen on layout
        Button btnCreateGame = findViewById(R.id.btn_create_game);
        btnCreateGame.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), InGame.class);
//            Update shared preferences
            SharedPreferences.Editor myEditor =
                    myPref.edit();
            myEditor.clear();
            myEditor.putInt("lastPlayer1", player1.getSelectedItemPosition());
            myEditor.putInt("lastPlayer2", player2.getSelectedItemPosition());
            myEditor.putInt("lastPlayer1", noOfFrames.getSelectedItemPosition());
            myEditor.putInt("lastPlayer2", endFrameAt.getSelectedItemPosition());
            myEditor.apply();

//            Create new game object and send to in game page.
            Game game = new Game(Integer.parseInt(noOfFrames.getSelectedItem().toString()), player1.getSelectedItem().toString(), nameID.get(player1.getSelectedItem().toString()), player2.getSelectedItem().toString(), nameID.get(player2.getSelectedItem().toString()), Integer.parseInt(endFrameAt.getSelectedItem().toString().substring(0, 1)));
            intent.putExtra("game", game);

            startActivity(intent);
        });
    }
}