package com.example.coursework;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int REQUEST_CODE = 1;
//        Set an alarm to trigger a week from when the app is started
        Intent alarmIntent = new Intent(MainActivity.this, Receiver.class);
//        Create pending intent to send reminder to user
        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Cancel pending intent (reset)
        am.cancel(pendingIntent);
//        Set reminder to repeat every 7 days
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

        Context ctx = getApplicationContext();

//        Set new game to go to new game screen if profile has been created, otherwise show error message
        Button btn_new_game = findViewById(R.id.btn_new_game);
        btn_new_game.setOnClickListener(view -> {
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(ctx, "A profile must be created before playing a game!", duration);
            toast.show();
        });

        String string;
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
                                btn_new_game.setOnClickListener(view -> {
                                    Intent intent = new Intent(getApplicationContext(), NewGame.class);
                                    startActivity(intent);
                                });
                            } else {
//                                Remove from list of profileIDs
                                System.out.println("No such document");
                            }
                        } else {
                            System.out.println("get failed with " + task.getException());
                        }
                    });
        }

//        Go to google search of snooker rules when rules button is pressed
        Button btn_rules = findViewById(R.id.btn_rules);
        btn_rules.setOnClickListener(view -> {
            Intent intent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://www.google.com/search?q=snooker+rules&oq=snooker+rules&aqs=chrome.0.69i59j0i433i512l4j69i60l3.1241j0j1&sourceid=chrome&ie=UTF-8:433"));
            startActivity(intent);
        });

//        Go to profiles page when profiles button is pressed
        Button btn_profiles = findViewById(R.id.btn_profiles);
        btn_profiles.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfilesMenu.class);
            startActivity(intent);
        });

//        Go to practice routines page when practice routines button is pressed
        Button btn_practice_routines = findViewById(R.id.btn_practice_routines);
        btn_practice_routines.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ViewRoutines.class);
            startActivity(intent);
        });

//        Go to user guide when user guide button is pressed
        Button btn_user_guide = findViewById(R.id.btn_user_guide);
        btn_user_guide.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), UserGuide.class);
            startActivity(intent);
        });
    }
}

