package com.example.coursework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * OnSwipeTouchListener used to implement swiping to go back.
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    //    Constructor
    public OnSwipeTouchListener(Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    //
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        /*
          Calculate direction of swipe and trigger respective method
         */
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
//                Get difference in x and y coordinates
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
//                If x difference is greater than y difference
                if (Math.abs(diffX) > Math.abs(diffY)) {
//                    If x difference is greater than swipe threshold and swipe speed is greater than velocity threshold
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                        If x difference is positive, trigger swipe right method
                        if (diffX > 0) {
                            onSwipeRight();
//                        Else trigger swipe left method
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
//                    If y difference is greater than swipe threshold and swipe speed is greater than velocity threshold
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                    If y difference is positive, trigger swipe bottom method
                    if (diffY > 0) {
                        onSwipeBottom();
//                    Else trigger swipe top method
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    //    Empty methods that can be later implemented
    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}

