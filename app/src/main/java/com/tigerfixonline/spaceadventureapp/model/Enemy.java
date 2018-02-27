package com.tigerfixonline.spaceadventureapp.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.VectorDrawable;
import android.util.Log;

import com.tigerfixonline.spaceadventureapp.GameView;
import com.tigerfixonline.spaceadventureapp.R;

import java.util.Random;

/**
 * Created by Ahmad on 2/9/2018.
 */

public class Enemy {

    /* TAG for our logs */
    private static String TAG = Enemy.class.getName();

    /* context */
    private Context mContext;

    /* drawable resources */
    private VectorDrawable mEnmey;

    /* coordinates */
    private int x;
    private int y;
    private final static int SPEED = 15;


    /* random */
    private final static Random mRandom = new Random();

    /* bounding rectangle */
    private Rect mCollisionDetection;

    public Enemy(Context context) {
        mEnmey = GameView.getVectorDrawable(context, R.drawable.enmey);
        mEnmey.setBounds(0, 0, mEnmey.getIntrinsicWidth(), mEnmey.getIntrinsicHeight());
        x = GameView.screenWidth + mRandom.nextInt(GameView.screenWidth);
        y = mRandom.nextInt(GameView.screenHeight);
        mCollisionDetection = new Rect(x, y, mEnmey.getIntrinsicWidth(), mEnmey.getIntrinsicHeight());
    }

    public void update() {
        x = x - (SPEED + GameView.GAME_SPEED);

        if (x < (-1 * mEnmey.getIntrinsicWidth())) {
            x = mRandom.nextInt(GameView.screenWidth) + GameView.screenWidth;
            y = mRandom.nextInt(GameView.screenHeight) + (mEnmey.getIntrinsicHeight() * 2);
        }

        mCollisionDetection.left = x;
        mCollisionDetection.top = y;
        mCollisionDetection.right = x + mEnmey.getIntrinsicWidth();
        mCollisionDetection.bottom = y + mEnmey.getIntrinsicHeight();
    }

    public void reset() {
        x = GameView.screenWidth + mRandom.nextInt(GameView.screenWidth);
        y = mRandom.nextInt(GameView.screenHeight);
        mCollisionDetection.left = x;
        mCollisionDetection.top = y;
        mCollisionDetection.right = x + mEnmey.getIntrinsicWidth();
        mCollisionDetection.bottom = y + mEnmey.getIntrinsicHeight();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(x, y);
        mEnmey.draw(canvas);
        canvas.restore();
    }

    public void move() {
        x = mRandom.nextInt(GameView.screenWidth) + GameView.screenWidth;
    }

    public Rect getmCollisionDetection() {
        return mCollisionDetection;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
