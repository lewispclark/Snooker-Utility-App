package com.example.coursework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
 * Backend for the ProfilesMenu page
 * Gets the online profiles for the profiles stored locally on the device and displays on the layout
 */
public class ProfilesMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles_menu);

//        Get profiles
        String string;
//        Get context
        Context ctx = getApplicationContext();

//        Get profile IDs from local storage
        InputStreamReader inputStreamReader;
        ArrayList<String> profileIDs = new ArrayList<>();
        try (FileInputStream fileInputStream = ctx.openFileInput("savedData.txt")) {
            inputStreamReader = new InputStreamReader(fileInputStream);
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

        HashMap<String, String> nameID = new HashMap<>();

        ArrayList<Profile> profiles = new ArrayList<>();
//        Get firestore database instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Get listview element from layout
        ListView listView = findViewById(R.id.list_view);
//        When list item is clicked, navigate to view profile page
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), ViewProfile.class);
            intent.putExtra("profileID", nameID.get(listView.getItemAtPosition(position)));
            intent.putExtra("profile", profiles.get(position));
            startActivity(intent);
        });

//        Set listview adapter to profile names list
        ArrayList<String> profileNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, profileNames);
        listView.setAdapter(adapter);

//        For each profile ID, get profile name to display in dropdown
        for (String profileID : profileIDs) {
//            Get profile by ID
            db.collection("player")
                    .document(profileID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
//                            Get document
                            DocumentSnapshot document = task.getResult();
//                            If document exists in firestore database
                            if (document.exists()) {
//                                Map key-value pairs as profile details, then create profile and add to list
                                Map<String, Object> profileDetails = document.getData();
                                nameID.put(Objects.requireNonNull(Objects.requireNonNull(profileDetails).get("name")).toString(), profileID);
                                profiles.add(new Profile(Objects.requireNonNull(Objects.requireNonNull(profileDetails).get("name")).toString(),
                                        ((Long) Objects.requireNonNull(profileDetails.get("highestBreak"))).intValue(),
                                        ((Long) Objects.requireNonNull(profileDetails.get("framesPlayed"))).intValue(),
                                        ((Long) Objects.requireNonNull(profileDetails.get("framesWon"))).intValue(),
                                        ((Long) Objects.requireNonNull(profileDetails.get("gamesPlayed"))).intValue(),
                                        ((Long) Objects.requireNonNull(profileDetails.get("gamesWon"))).intValue()));
//                                Add profile to adapter to make it visible
                                adapter.add(Objects.requireNonNull(profileDetails.get("name")).toString());
                            } else {
//                                If document does not exist, remove from profile IDs list
                                profileIDs.remove(profileID);
                                System.out.println("No such document");
                            }
                        } else {
                            System.out.println("get failed with " + task.getException());
                        }
                    });
        }

//        When user swipes right, go back to main menu
        findViewById(R.id.list_view).setOnTouchListener(new OnSwipeTouchListener(ProfilesMenu.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(ProfilesMenu.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

//        When user presses go back button, go back to main menu
        Button btnBack = findViewById(R.id.btn_go_back_profiles_menu);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

//        When user presses create profile button, go to create profile page
        Button btnCreateProfile = findViewById(R.id.btn_create_profile);
        btnCreateProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CreateProfile.class);
            startActivity(intent);
        });
    }
}