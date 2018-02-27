package com.tigerfixonline.spaceadventureapp.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;

import com.tigerfixonline.spaceadventureapp.GameView;
import com.tigerfixonline.spaceadventureapp.R;

import java.util.Random;

/**
 * Created by Ahmad on 2/9/2018.
 */

public class Star {

    /* TAG for our logs */
    private static String TAG = Star.class.getName();

    /* coordinates */
    private int x;
    private int y;
    private final static int SPEED = 5;

    /* drawable resources */
    private VectorDrawable star;

    /* random */
    private final static Random mRandom = new Random();

    public Star(Context context) {
        star = GameView.getVectorDrawable(context, R.drawable.star);
        star.setBounds(0, 0, star.getIntrinsicWidth(), star.getIntrinsicHeight());
        x = mRandom.nextInt(GameView.screenWidth);
        y = mRandom.nextInt(GameView.screenHeight);
    }

    public void update() {
        x = x - (SPEED + GameView.GAME_SPEED);

        if (x < (-1 * star.getIntrinsicWidth())) {
            x = mRandom.nextInt(GameView.screenWidth);
            y = mRandom.nextInt(GameView.screenHeight);
        }
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(x, y);
        star.draw(canvas);
        canvas.restore();
    }


}
