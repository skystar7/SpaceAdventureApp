package com.tigerfixonline.spaceadventureapp;

import android.app.Activity;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Test extends Activity {

    /* TAG for our logs */
    private static String TAG = Test.class.getName();
    private VectorDrawable animatedVectorDrawable;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        imageView = findViewById(R.id.animated_drawable);


        Button button = findViewById(R.id.animate_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                imageView.animate()
                        .scaleX(1.25f)
                        .scaleY(1.25f)
                        .translationZ(1.0f)
                        .setDuration(100);
            }
        });
    }

    private void vibrate() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP: {
                Log.d(TAG, "release...");
//                playerSpeeding.stop();
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "pressing down...");
//                playerSpeeding.start();
                break;
            }

        }

        return true;
    }
}
