package com.example.coursework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.HashMap;
import java.util.Objects;

/**
 * This is the backend of the Add foul page.
 * Functionality is added to the ball buttons and the foul functions are triggered on the
 * frame object.
 */
public class AddFoul extends AppCompatActivity {

    @SuppressLint("FindViewByIdCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_foul);

//        Get intent sent from in-game class
        Intent pastIntent = getIntent();
//        Get game and frame objects from intent
        Game game = (Game) pastIntent.getSerializableExtra("game");
        Frame frame = game.getFrames().get(game.getCurrentFrame() - 1);

//        Get ball buttons on page
        HashMap<String, AppCompatButton> ballButtons = new HashMap<>();
        ballButtons.put("Red", findViewById(R.id.btn_red_ball));
        ballButtons.put("Yellow", findViewById(R.id.btn_yellow_ball));
        ballButtons.put("Green", findViewById(R.id.btn_green_ball));
        ballButtons.put("Brown", findViewById(R.id.btn_brown_ball));
        ballButtons.put("Blue", findViewById(R.id.btn_blue_ball));
        ballButtons.put("Pink", findViewById(R.id.btn_pink_ball));
        ballButtons.put("Black", findViewById(R.id.btn_black_ball));

//        Iterate through ball buttons and add functionality to each one
        for (String ballButtonColour : ballButtons.keySet()) {
//            When button is clicked, trigger foul with button colour then go back to game
            Objects.requireNonNull(ballButtons.get(ballButtonColour)).setOnClickListener(view -> {
                frame.foul(ballButtonColour);
                Intent intent = new Intent(getApplicationContext(), InGame.class);
                intent.putExtra("game", game);
                startActivity(intent);
            });
        }

//        When miss ball is clicked, trigger foul with red colour (default) then go back to game
        Button btnMissBall = findViewById(R.id.btn_miss_ball);
        btnMissBall.setOnClickListener(view -> {
            frame.foul("Red");
            Intent intent = new Intent(getApplicationContext(), InGame.class);
            intent.putExtra("game", game);
            startActivity(intent);
        });

//        When user swipes right, go back to game
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(AddFoul.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), InGame.class);
                intent.putExtra("game", game);
                startActivity(intent);
            }
        });

//        When user clicks go back button, go back to game
        Button btnBack = findViewById(R.id.btn_go_back_add_foul);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), InGame.class);
            intent.putExtra("game", game);
            startActivity(intent);
        });
    }
}