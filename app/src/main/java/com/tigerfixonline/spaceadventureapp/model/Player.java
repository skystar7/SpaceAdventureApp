package com.tigerfixonline.spaceadventureapp.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.tigerfixonline.spaceadventureapp.GameView;
import com.tigerfixonline.spaceadventureapp.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by Ahmad on 2/8/2018.
 */

public class Player {

    /* TAG for our logs */
    private static String TAG = Player.class.getName();

    /* context */
    private Context mContext;

    /* drawable resources */
    private VectorDrawable player;

    /* coordinates */
    private int x;
    private int y;
    private final static int TILTING_BIAS = 7;

    /* rockets */
    private List<Rocket> mRockets = new ArrayList<>();


    /* bounding rectangle */
    private Rect mCollisionDetection;

    public Player(Context context) {
        mContext = context;
        player = GameView.getVectorDrawable(context, R.drawable.player_speeding);
        player.setBounds(0, 0, player.getIntrinsicWidth(), player.getIntrinsicHeight());
        reset();
        mCollisionDetection = new Rect(x, y, player.getIntrinsicWidth(), player.getIntrinsicHeight());
    }

    public void update(float yAxis) {
//        Log.d(TAG, "Math.round(yAxis): " + Math.round(yAxis));
        int round = Math.round(yAxis);
        this.y = this.y + (round > 0 ? round + (TILTING_BIAS) : round - (TILTING_BIAS));

        /* update rockets */
        List<Rocket> view = new ArrayList<>(mRockets);
        for (Rocket rocket : view)
            rocket.update();
        mRockets = view;

        /*
        control limits
         */
        if (y < -player.getIntrinsicHeight()) {
            y = -player.getIntrinsicHeight();
        }
        if (y > GameView.screenHeight + player.getIntrinsicHeight()) {
            y = GameView.screenHeight + player.getIntrinsicHeight();
        }

        mCollisionDetection.left = x;
        mCollisionDetection.top = y;
        mCollisionDetection.right = x + player.getIntrinsicWidth();
        mCollisionDetection.bottom = y + player.getIntrinsicHeight();
    }

    public void reset() {
         /* place it at half of its size on the horizontal axis */
        x = player.getIntrinsicWidth() / 2;

        /* place it at the middle of screen on the vertical axis */
        y = GameView.screenHeight / 2;
    }

    public void hide() {
        x = -500;
        y = -500;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(x, y);
        player.draw(canvas);
        canvas.restore();

         /* draw rockets wrap in a new list to avoid concurrent modification */
        for (Rocket rocket : new ArrayList<>(mRockets))
            rocket.draw(canvas);

        System.gc();

    }

    public void fire() {
        Rocket rocket = new Rocket(mContext, x + player.getIntrinsicWidth(), y + (player.getIntrinsicHeight() / 2));
        mRockets.add(rocket);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void removeRocket(Rocket rocket) {
        mRockets.remove(rocket);
    }

    public List<Rocket> getmRockets() {
        return mRockets;
    }

    public Rect getmCollisionDetection() {
        return mCollisionDetection;
    }

}
