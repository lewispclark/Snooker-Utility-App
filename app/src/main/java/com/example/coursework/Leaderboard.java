package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Leaderboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

//        Get intent from last page
        Intent pastIntent = getIntent();
//        Get routine name from intent
        String routineName = pastIntent.getStringExtra("routineName");
//        Get firestore database instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Get table element from page
        TableLayout table = findViewById(R.id.tbl_leaderboard_layout);

        ArrayList<PlayerRoutineStats> tableRows = new ArrayList<>();

//        Get players from database
        db.collection("player")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int id = 1;
//                        For each row in the database
                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Convert row to profile
                            Map<String, Object> profile = document.getData();
//                            For each routine stat in profile
                            for (Map<String, Object> routineStat : (ArrayList<Map<String, Object>>) Objects.requireNonNull(profile.get("routineStats"))) {
                                if (Objects.equals(routineStat.get("routineName"), routineName)) {
//                                    Add stat to leaderboard table
                                    tableRows.add(new PlayerRoutineStats((String) profile.get("name"),
                                            ((Long) Objects.requireNonNull(routineStat.get("score"))).intValue(),
                                            ((Long) Objects.requireNonNull(routineStat.get("highestBreak"))).intValue(),
                                            ((Long) Objects.requireNonNull(routineStat.get("fastestTime"))).intValue(),
                                            ((Long) Objects.requireNonNull(routineStat.get("timesPlayed"))).intValue()));
                                }
                            }
                        }

//                        Sort table by highestBreak
                        Collections.sort(tableRows, (t1, t2) -> t2.getHighestBreak() - t1.getHighestBreak());
//                        Add each row to the table element on the layout
                        for (PlayerRoutineStats tableRow : tableRows) {
                            System.out.println(tableRow.getScore());
                            TableRow row = (TableRow) LayoutInflater.from(Leaderboard.this).inflate(R.layout.leaderboard_row, null);
                            row.setId(id);
//                            Set attributes on row
                            ((TextView) row.findViewById(R.id.attrib_name)).setText(tableRow.getRoutineName());
                            ((TextView) row.findViewById(R.id.attrib_time)).setText(Game.secondsToString(tableRow.getFastestTime()));
                            ((TextView) row.findViewById(R.id.attrib_highest_break)).setText(String.format(Locale.getDefault(), "%d", tableRow.getHighestBreak()));
                            ((TextView) row.findViewById(R.id.attrib_total_score)).setText(String.format(Locale.getDefault(), "%d", tableRow.getScore()));
//                            Add row to table
                            table.addView(row);
                            id++;
                        }
                    } else {
                        System.out.println("get failed with " + task.getException());
                    }
                });

//        When user swipes right, go back to view routines
        findViewById(R.id.scrollbar).setOnTouchListener(new OnSwipeTouchListener(Leaderboard.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), ViewRoutines.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(Leaderboard.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), ViewRoutines.class);
                startActivity(intent);
            }
        });

//        Go back to view routines page when back button is pressed
        Button btnBack = findViewById(R.id.btn_go_back);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ViewRoutines.class);
            startActivity(intent);
        });
    }
}
