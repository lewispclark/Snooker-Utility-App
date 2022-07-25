package com.example.coursework;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * A service to allow uploading of routine stats to firestore database in the background
 * Containers methods for creating and starting service.
 */
public class UploadRoutineStats extends Service {
    public UploadRoutineStats() {
    }

    private static final String TAG =
            "com.example.coursework";

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");
//        Get firestore database instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Get game object from intent
        Routine routine = (Routine) intent.getSerializableExtra("routine");

//        Create profile with temporary name
        Profile profile = new Profile("temp");

//        Update profile
        db.collection("player")
                .document(routine.getPlayer().getProfileID())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        Get profile document
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                            If document exists, create map of key-value pairs for profile details
                            Map<String, Object> playerDetails = document.getData();
//                            Update profile details
                            profile.setName(((String) playerDetails.get("name")));
                            profile.setHighestBreak(((Long) playerDetails.get("highestBreak")).intValue());
                            profile.setFramesPlayed(((Long) playerDetails.get("framesPlayed")).intValue());
                            profile.setFramesWon(((Long) playerDetails.get("framesWon")).intValue());
                            profile.setGamesPlayed(((Long) playerDetails.get("gamesPlayed")).intValue());
                            profile.setGamesWon(((Long) playerDetails.get("gamesWon")).intValue());

//                            Get profile stats
                            ArrayList<Object> onlineRoutineStats = (ArrayList<Object>) playerDetails.get("routineStats");
                            ArrayList<PlayerRoutineStats> updatedRoutineStats = new ArrayList<>();

//                            Iterate through routine stats that have already been stored for the user
                            boolean exists = false;
                            for (Object playerRoutineStats : Objects.requireNonNull(onlineRoutineStats)) {
                                Map<String, Object> routineStats = (Map<String, Object>) playerRoutineStats;
                                PlayerRoutineStats updated = new PlayerRoutineStats();
//                                If user has already played and uploaded stats from routine
                                if (Objects.equals(routineStats.get("routineName"), routine.getName())) {
//                                    Update routine stats
                                    updated.setRoutineName((String) routineStats.get("routineName"));
                                    updated.setScore((Math.max(routine.getPlayer().getScore(), ((Long) Objects.requireNonNull(routineStats.get("score"))).intValue())));
                                    updated.setHighestBreak((Math.max(routine.getHighestBreak(), ((Long) Objects.requireNonNull(routineStats.get("highestBreak"))).intValue())));
                                    updated.setFastestTime((Math.min(routine.getElapsedTime(), ((Long) Objects.requireNonNull(routineStats.get("fastestTime"))).intValue())));
                                    updated.setTimesPlayed(((Long) Objects.requireNonNull(routineStats.get("timesPlayed"))).intValue() + 1);
                                    exists = true;
                                } else {
//                                    Update routine stats without comparing other stats
                                    updated.setRoutineName((String) routineStats.get("routineName"));
                                    updated.setScore(((Long) Objects.requireNonNull(routineStats.get("score"))).intValue());
                                    updated.setHighestBreak(((Long) Objects.requireNonNull(routineStats.get("highestBreak"))).intValue());
                                    updated.setFastestTime(((Long) Objects.requireNonNull(routineStats.get("fastestTime"))).intValue());
                                    updated.setTimesPlayed(((Long) Objects.requireNonNull(routineStats.get("timesPlayed"))).intValue());
                                }
//                                Add updated stats to routine stats
                                updatedRoutineStats.add(updated);
                            }

//                            If user has not played routine before
                            if (!exists) {
//                                Create new routine stats object and add to array
                                updatedRoutineStats.add(new PlayerRoutineStats(routine.getName(), routine.getPlayer().getScore(), routine.getHighestBreak(), routine.getElapsedTime(), 1));
                            }

//                            Set routine stats on profile
                            profile.setRoutineStats(updatedRoutineStats);

//                            Send new routine stats to database
                            db.collection("player")
                                    .document(routine.getPlayer().getProfileID())
                                    .set(profile);

                        } else {
                            System.out.println("No such document");
                        }
                    } else {
                        System.out.println("get failed with " + task.getException());
                    }
                });

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service onDestroy");
    }
}