package com.tigerfixonline.spaceadventureapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {

    /* TAG for our logs */
    private static String TAG = MainActivity.class.getName();
    private ImageView playNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity created...");


        /* request a full screen window */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /* request to keep screen on */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

         /* request a landscape orientation */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

         /* inflate the layout file */
        setContentView(R.layout.activity_main);

        playNow = findViewById(R.id.play_now_btn);
        playNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity resumed...");
    }
}
