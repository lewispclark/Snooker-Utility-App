package com.example.coursework;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This is the backend of the Create Profile page.
 * Functionality is added to the create profile and go back buttons
 */
public class CreateProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

//        When create profile button is pressed
        Button btnCreateProfile = findViewById(R.id.btn_create_profile);
        btnCreateProfile.setOnClickListener(view -> {

//            Create new profile with player name
            Profile newProfile = new Profile(((EditText) findViewById(R.id.ed_player_name)).getText().toString(), 0, 0, 0, 0, 0);

            // Firebase online storage
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Add new profile to firestore database with a generated ID
            db.collection("player")
                    .add(newProfile)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                            Add to local file
                        FileOutputStream fOut = null;
//                        Open file
                        try {
                            fOut = openFileOutput("savedData.txt", MODE_APPEND);
                        } catch (FileNotFoundException e) {
                            FileOutputStream fos;
//                            Create new file if one doesn't exist
                            try {
                                fos = new FileOutputStream("savedData.txt");
                                fos.write((documentReference.getId() + "\n").getBytes());
                                fos.flush();
                                fos.close();
                            } catch (IOException fileNotFoundException) {
                                fileNotFoundException.printStackTrace();
                            }
                        }
//                        Write new profile id to local file
                        try {
                            assert fOut != null;
                            fOut.write((documentReference.getId() + "\n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

//            Go to main menu
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

//        When user swipes right, go back to in profiles menu
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(CreateProfile.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), ProfilesMenu.class);
                startActivity(intent);
            }
        });

//        When user clicks go back button, go back to game
        Button btnBack = findViewById(R.id.btn_go_back);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfilesMenu.class);
            startActivity(intent);
        });
    }
}