package com.tigerfixonline.spaceadventureapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.VectorDrawable;

import com.tigerfixonline.spaceadventureapp.GameView;
import com.tigerfixonline.spaceadventureapp.R;

import java.util.Random;

/**
 * Created by Ahmad on 2/10/2018.
 */

public class Comet {


    /* TAG for our logs */
    private static String TAG = Comet.class.getName();

    /* coordinates */
    private int x;
    private int y;

    private Bitmap rock;

    /* random */
    private final static Random mRandom = new Random();


    public Comet(Context context) {
        x = mRandom.nextInt(GameView.screenWidth);
        y = mRandom.nextInt(GameView.screenHeight);
        rock = BitmapFactory.decodeResource(context.getResources(), R.drawable.rock);
    }

    public void update() {
        x = x + 30;
        y = y + 10;

    }

    public void changeXY() {
        if (x > GameView.screenWidth && y > GameView.screenHeight) {
            x = mRandom.nextInt(GameView.screenWidth);
            y = -rock.getHeight();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(rock, x, y, null);
    }

}
