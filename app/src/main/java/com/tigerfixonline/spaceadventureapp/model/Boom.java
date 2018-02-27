package com.tigerfixonline.spaceadventureapp.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;

import com.tigerfixonline.spaceadventureapp.GameView;
import com.tigerfixonline.spaceadventureapp.R;

/**
 * Created by Ahmad on 2/9/2018.
 */

public class Boom {


    /* TAG for our logs */
    private static String TAG = Boom.class.getName();

    /* coordinates */
    private int x;
    private int y;

    /* drawable resources */
    private VectorDrawable boom;


    public Boom(Context context, int x, int y) {
        this.x = x;
        this.y = y;
        boom = GameView.getVectorDrawable(context, R.drawable.boom);
        boom.setBounds(0, 0, boom.getIntrinsicWidth(), boom.getIntrinsicHeight());
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(x, y);
        boom.draw(canvas);
        canvas.restore();
    }


}
