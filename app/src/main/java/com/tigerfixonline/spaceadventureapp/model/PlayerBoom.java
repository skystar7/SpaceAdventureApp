package com.tigerfixonline.spaceadventureapp.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;

import com.tigerfixonline.spaceadventureapp.GameView;
import com.tigerfixonline.spaceadventureapp.R;

/**
 * Created by Ahmad on 2/9/2018.
 */

public class PlayerBoom {


    /* TAG for our logs */
    private static String TAG = PlayerBoom.class.getName();

    /* coordinates */
    private int x;
    private int y;

    /* drawable resources */
    private VectorDrawable playerBoom;


    public PlayerBoom(Context context, int x, int y) {
        this.x = x;
        this.y = y;
        playerBoom = GameView.getVectorDrawable(context, R.drawable.player_boom);
        playerBoom.setBounds(0, 0, playerBoom.getIntrinsicWidth(), playerBoom.getIntrinsicHeight());
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(x, y);
        playerBoom.draw(canvas);
        canvas.restore();
    }


}
