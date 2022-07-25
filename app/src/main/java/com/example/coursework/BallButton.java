package com.example.coursework;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Type of button that is usable in game, usability can be toggled
 */
public class BallButton extends androidx.appcompat.widget.AppCompatButton {
    //    Constructors
    public BallButton(Context context) {
        super(context);
    }

    public BallButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Disable ball button and make invisible if false, otherwise enable and make visible
     *
     * @param bool true if button should be usable, false otherwise
     */
    public void usable(Boolean bool) {
        this.setEnabled(bool);
        this.setVisibility(bool ? View.VISIBLE : View.INVISIBLE);
    }
}
