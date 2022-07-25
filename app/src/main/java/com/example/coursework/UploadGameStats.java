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
 * A service to allow uploading of game stats to firestore database in the background
 * Containers methods for creating and starting service.
 */
public class UploadGameStats extends Service {
    public UploadGameStats() {
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
        Game game = (Game) intent.getSerializableExtra("game");

//        Create winner and loser profiles with temporary names
        Profile winnerProfile = new Profile("temp");
        Profile loserProfile = new Profile("temp");

//        Update winner profile
        db.collection("player")
                .document(game.getGameWinner().getProfileID())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        Get profile document
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                            If document exists, create map of key-value pairs for profile details
                            Map<String, Object> playerDetails = document.getData();
//                            Update winnerProfile details
                            winnerProfile.setName(((String) Objects.requireNonNull(playerDetails).get("name")));
                            winnerProfile.setHighestBreak(((Long) Objects.requireNonNull(playerDetails.get("highestBreak"))).intValue());
                            winnerProfile.setFramesPlayed(((Long) Objects.requireNonNull(playerDetails.get("framesPlayed"))).intValue());
                            winnerProfile.setFramesWon(((Long) Objects.requireNonNull(playerDetails.get("framesWon"))).intValue());
                            winnerProfile.setGamesPlayed(((Long) Objects.requireNonNull(playerDetails.get("gamesPlayed"))).intValue());
                            winnerProfile.setGamesWon(((Long) Objects.requireNonNull(playerDetails.get("gamesWon"))).intValue());

//                            Get Profile routine stats
                            ArrayList<Object> onlineRoutineStats = (ArrayList<Object>) playerDetails.get("routineStats");
                            ArrayList<PlayerRoutineStats> updatedRoutineStats = new ArrayList<>();

//                            Set profile routine stats
                            for (Object playerRoutineStats : Objects.requireNonNull(onlineRoutineStats)) {
                                Map<String, Object> routineStats = (Map<String, Object>) playerRoutineStats;
                                PlayerRoutineStats updated = new PlayerRoutineStats();
                                updated.setRoutineName((String) routineStats.get("routineName"));
                                updated.setScore(((Long) Objects.requireNonNull(routineStats.get("score"))).intValue());
                                updated.setHighestBreak(((Long) Objects.requireNonNull(routineStats.get("highestBreak"))).intValue());
                                updated.setFastestTime(((Long) Objects.requireNonNull(routineStats.get("fastestTime"))).intValue());
                                updated.setTimesPlayed(((Long) Objects.requireNonNull(routineStats.get("timesPlayed"))).intValue());
                                updatedRoutineStats.add(updated);
                            }
                            winnerProfile.setRoutineStats(updatedRoutineStats);

//                            Update winner profile with new game stats
                            winnerProfile.setHighestBreak((Math.max(game.getGameWinner().getHighestBreak(), winnerProfile.getHighestBreak())));
                            winnerProfile.setFramesPlayed(winnerProfile.getFramesPlayed() + game.getGameWinner().getFramesWon() + game.getGameLoser().getFramesWon());
                            winnerProfile.setFramesWon(winnerProfile.getFramesWon() + game.getGameWinner().getFramesWon());
                            winnerProfile.setGamesPlayed(winnerProfile.getGamesPlayed() + 1);
                            winnerProfile.setGamesWon(winnerProfile.getGamesWon() + 1);

//                            Send newly updated profile to database
                            db.collection("player")
                                    .document(game.getGameWinner().getProfileID())
                                    .set(winnerProfile);
                        } else {
                            System.out.println("No such document");
                        }
                    } else {
                        System.out.println("get failed with " + task.getException());
                    }
                });

//        Update loser profile
        db.collection("player")
                .document(game.getGameLoser().getProfileID())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        Get profile document
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                            If document exists, create map of key-value pairs for profile details
                            Map<String, Object> playerDetails = document.getData();
//                            Update winnerProfile details
                            loserProfile.setName(((String) Objects.requireNonNull(playerDetails).get("name")));
                            loserProfile.setHighestBreak(((Long) Objects.requireNonNull(playerDetails.get("highestBreak"))).intValue());
                            loserProfile.setFramesPlayed(((Long) Objects.requireNonNull(playerDetails.get("framesPlayed"))).intValue());
                            loserProfile.setFramesWon(((Long) Objects.requireNonNull(playerDetails.get("framesWon"))).intValue());
                            loserProfile.setGamesPlayed(((Long) Objects.requireNonNull(playerDetails.get("gamesPlayed"))).intValue());
                            loserProfile.setGamesWon(((Long) Objects.requireNonNull(playerDetails.get("gamesWon"))).intValue());

//                            Get Profile routine stats
                            ArrayList<Object> onlineRoutineStats = (ArrayList<Object>) playerDetails.get("routineStats");
                            ArrayList<PlayerRoutineStats> updatedRoutineStats = new ArrayList<>();

//                            Set profile routine stats
                            for (Object playerRoutineStats : Objects.requireNonNull(onlineRoutineStats)) {
                                Map<String, Object> routineStats = (Map<String, Object>) playerRoutineStats;
                                PlayerRoutineStats updated = new PlayerRoutineStats();
                                updated.setRoutineName((String) routineStats.get("routineName"));
                                updated.setScore(((Long) Objects.requireNonNull(routineStats.get("score"))).intValue());
                                updated.setHighestBreak(((Long) Objects.requireNonNull(routineStats.get("highestBreak"))).intValue());
                                updated.setFastestTime(((Long) Objects.requireNonNull(routineStats.get("fastestTime"))).intValue());
                                updated.setTimesPlayed(((Long) Objects.requireNonNull(routineStats.get("timesPlayed"))).intValue());
                                updatedRoutineStats.add(updated);
                            }
                            loserProfile.setRoutineStats(updatedRoutineStats);

//                            Update winner profile with new game stats
                            loserProfile.setHighestBreak((Math.max(game.getGameLoser().getHighestBreak(), loserProfile.getHighestBreak())));
                            loserProfile.setFramesPlayed(loserProfile.getFramesPlayed() + game.getGameWinner().getFramesWon() + game.getGameLoser().getFramesWon());
                            loserProfile.setFramesWon(loserProfile.getFramesWon() + game.getGameLoser().getFramesWon());
                            loserProfile.setGamesPlayed(loserProfile.getGamesPlayed() + 1);

//                            Send newly updated profile to database
                            db.collection("player")
                                    .document(game.getGameLoser().getProfileID())
                                    .set(loserProfile);
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