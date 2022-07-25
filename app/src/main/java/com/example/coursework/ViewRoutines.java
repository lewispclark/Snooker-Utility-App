package com.example.coursework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Backend of the ViewRoutines page
 * Get routines and display in list on layout
 */
public class ViewRoutines extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routines);

//        Initialise routine presets
        Routine[] routines = new Routine[2];
        routines[0] = new Routine("Routine 1", "White can be placed anywhere", "snooker_routine_1_foreground");
        routines[0].setBalls(0, 1, 1, 1, 1, 1, 1);
        routines[1] = new Routine("Routine 2", "White can be placed anywhere", "snooker_routine_2_foreground");
        routines[1].setBalls(12, 1, 1, 1, 1, 1, 1);

//        Get table layout from layout
        TableLayout table = findViewById(R.id.tbl_routines_layout);
        int id = 1;
//        Get context
        Context context = getApplicationContext();

//        For each routine
        for (Routine routine : routines) {
//            Create row
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.routine_row, null);
            row.setId(id);
//            Set preview image for row
            int drawableId = this.getResources().getIdentifier(routine.getImageName(), "mipmap", context.getPackageName());
            ((ImageView) row.findViewById(R.id.attrib_preview)).setImageResource(drawableId);
//            Set name and description of row
            ((TextView) row.findViewById(R.id.attrib_name)).setText(routine.getName());
            ((TextView) row.findViewById(R.id.attrib_description)).setText(routine.getDescription());
//            When user clicks on row, go to view routine page and send routine as intent
            row.setOnClickListener(view -> {
                System.out.println(view.getId());
                Intent intent = new Intent(getApplicationContext(), ViewRoutine.class);
                intent.putExtra("routine", routine);
                startActivity(intent);
            });
//            Add row to table
            table.addView(row);
            id++;
        }

//        When user swipes right, go back to main menu
        findViewById(R.id.scrollbar).setOnTouchListener(new OnSwipeTouchListener(ViewRoutines.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.container).setOnTouchListener(new OnSwipeTouchListener(ViewRoutines.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

//        When user clicks go back button, go back to main menu
        Button btnBack = findViewById(R.id.btn_go_back);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }
}