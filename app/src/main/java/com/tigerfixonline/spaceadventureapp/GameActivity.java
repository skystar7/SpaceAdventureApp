package com.tigerfixonline.spaceadventureapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class GameActivity extends Activity {

    /* TAG for our logs */
    private static String TAG = MainActivity.class.getName();
    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


          /* request a full screen window */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /* request to keep screen on */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* request a landscape orientation */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        /*  save the display size in a point object */
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        defaultDisplay.getSize(displaySize);

        /* constructing our gameView */
        mGameView = new GameView(this, displaySize.x, displaySize.y);

        /* setting our ContentView to gameView */
        setContentView(mGameView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "GameActivity resumed...");

         /* resume the game thread*/
        mGameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "GameActivity paused...");

        /* pause the game thread*/
        mGameView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mGameView.releaseResources();
    }
}
