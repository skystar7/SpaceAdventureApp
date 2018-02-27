package com.tigerfixonline.spaceadventureapp.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.VectorDrawable;

import com.tigerfixonline.spaceadventureapp.GameView;
import com.tigerfixonline.spaceadventureapp.R;

/**
 * Created by Ahmad on 2/8/2018.
 */

public class Rocket {

    /* TAG for our logs */
    private static String TAG = Rocket.class.getName();

    /* coordinates */
    private int x;
    private int y;

    /* drawable resources */
    private VectorDrawable rocket;

    /* bounding rectangle */
    private Rect mCollisionDetection;


    public Rocket(Context context, int x, int y) {
        this.x = x;
        this.y = y;
        rocket = GameView.getVectorDrawable(context, R.drawable.rocket);
        rocket.setBounds(0, 0, rocket.getIntrinsicWidth(), rocket.getIntrinsicHeight());
        mCollisionDetection = new Rect(x, y, rocket.getIntrinsicWidth(), rocket.getIntrinsicHeight());
    }

    public void update() {
        x = x + 20;

        mCollisionDetection.left = x;
        mCollisionDetection.top = y;
        mCollisionDetection.right = x + rocket.getIntrinsicWidth();
        mCollisionDetection.bottom = y + rocket.getIntrinsicHeight();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(x, y - (rocket.getIntrinsicHeight() / 2));
        rocket.draw(canvas);
        canvas.restore();
    }

    public boolean noHit() {
        return (x > GameView.screenWidth ? true : false);
    }



    public Rect getmCollisionDetection() {
        return mCollisionDetection;
    }

}
