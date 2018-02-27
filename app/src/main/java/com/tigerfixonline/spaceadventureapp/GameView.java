package com.tigerfixonline.spaceadventureapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.VectorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tigerfixonline.spaceadventureapp.model.Boom;
import com.tigerfixonline.spaceadventureapp.model.Comet;
import com.tigerfixonline.spaceadventureapp.model.Enemy;
import com.tigerfixonline.spaceadventureapp.model.Player;
import com.tigerfixonline.spaceadventureapp.model.PlayerBoom;
import com.tigerfixonline.spaceadventureapp.model.Rocket;
import com.tigerfixonline.spaceadventureapp.model.Star;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by Ahmad on 2/7/2018.
 */

public class GameView extends SurfaceView implements Runnable, SensorEventListener {

    /* TAG for our logs */
    private static String TAG = GameView.class.getName();

    /* context */
    private Context mContext;

    /* object that hold our surface to draw on */
    private final SurfaceHolder mSurfaceHolder;

    /* secondary dedicated thread for the game, separate from the main GUI thread */
    private Thread mGameThread;

    /* tracking whether or not the game is running */
    private volatile boolean running;

    /*
    Game Objects
     */
    private Player mPlayer;
    private PlayerBoom mPlayerBoom;
    private VectorDrawable mRound;
    private List<Enemy> mEnemyList = new ArrayList<>();
    private List<Boom> mBooms = new ArrayList<>();
    private List<Boom> mBoomsToDraw;
    private List<Star> mStars = new ArrayList<>();
    private Comet mComet;


    /*
    Accelerometer sensor
     */
    private final SensorManager mSensorManager;
    private final Sensor mAccelerometerSensor;
    private float y;

    /*
    Screen dimintions
     */
    public static int screenWidth;
    public static int screenHeight;

    /*
     * Sound References
     */
    private MediaPlayer mGameLoop;
    private SoundPool mSoundPool;
    private int mRocket;
    private int mExplosion;
    private int mCrash;

    /*
     * Game settings
     */
    private final int NUMBER_OF_ENMEIES = 5;
    private final int NUMBER_OF_STARS = 20;
    private int ROUND = 5;
    private boolean isOver = false;
    private int SCORE;
    public static int GAME_SPEED;

    /* Timer */
    private final Timer boomsTimer = new Timer();
    private final Timer cometTimer = new Timer();

    /*
     * Main font used
     */
    private Typeface mTypeface;
    private final Paint mGamePaint;
    private Bitmap bg;


    public GameView(Context context, int width, int height) {
        super(context);
        mContext = context;
        mSurfaceHolder = getHolder();
        screenWidth = width;
        screenHeight = height;

        mPlayer = new Player(context);
        mRound = getVectorDrawable(context, R.drawable.round);
        mRound.setBounds(0, 0, mRound.getIntrinsicWidth(), mRound.getIntrinsicHeight());

        for (int i = 0; i < NUMBER_OF_ENMEIES; i++) {
            mEnemyList.add(new Enemy(mContext));
        }

        for (int i = 0; i < NUMBER_OF_STARS; i++) {
            mStars.add(new Star(mContext));
        }

        mComet = new Comet(mContext);
        cometTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mComet.changeXY();
            }
        }, 0, 10_000);


        /* init accelerometer */
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


         /* init sounds */
        mGameLoop = MediaPlayer.create(context, R.raw.space_trance);
        mGameLoop.setLooping(true);
        mGameLoop.setVolume(0.20f, 0.20f);
        mGameLoop.start();


        /* init sound effects */
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        mSoundPool = new SoundPool.Builder().setMaxStreams(/* MAX sounds simultaneously */3).setAudioAttributes(audioAttributes).build();
        mRocket = mSoundPool.load(context, R.raw.lazer, 1);
        mExplosion = mSoundPool.load(context, R.raw.explosion, 1);
        mCrash = mSoundPool.load(context, R.raw.crash, 1);

        /* init font */
        /* drawing speed */
        mGamePaint = new Paint();
        mGamePaint.setAntiAlias(true);
        mTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Baloo-Regular.ttf");
        mGamePaint.setTypeface(mTypeface);

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.space_bg);
        bg = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }


    @Override
    public void run() {
        while (running) {
            update();
            draw();
        }
    }

    private void update() {
        mPlayer.update(y);

        for (Enemy enemy : mEnemyList) {
            enemy.update();
            if (enemy.getmCollisionDetection().intersect(mPlayer.getmCollisionDetection())) {
                decrementRound();
            }
            for (Rocket rocket : new ArrayList<>(mPlayer.getmRockets())) {
                if (enemy.getmCollisionDetection().intersect(rocket.getmCollisionDetection()) && enemy.getX() < screenWidth) {
                    SCORE = SCORE + 200;
                    /* Game speed */
                    if (SCORE > 0 && SCORE % 5_000 == 0) {
                        GAME_SPEED = GAME_SPEED + 5;
                        vibrate();
                    }
                    mPlayer.removeRocket(rocket);
                    mBooms.add(new Boom(mContext, enemy.getX(), enemy.getY()));
                    enemy.move();
                    mSoundPool.play(mExplosion, 0.15f, 0.15f, 0, 0, 1.0f);
                }
            }
        }

        for (Rocket rocket : new ArrayList<>(mPlayer.getmRockets())) {
            if (rocket.noHit())
                mPlayer.removeRocket(rocket);
        }

        System.gc();


        for (Star star : mStars) {
            star.update();
        }

        mComet.update();


        /* GAME OVER */
        if (ROUND == 0) {
            isOver = true;
            running = false;
            GAME_SPEED = 0;
            mPlayer.hide();
            mGameLoop.pause();
        }

    }

    private void decrementRound() {
        vibrate();
        ROUND--;
        mPlayerBoom = new PlayerBoom(mContext, mPlayer.getX(), mPlayer.getY());
        removePlayerBoomAfterDelay();
        mPlayer.reset();
        mSoundPool.play(mCrash, 0.20f, 0.20f, 0, 0, 1.0f);

        for (Enemy e : mEnemyList) {
            e.reset();
        }

    }

    private void draw() {

        if (!mSurfaceHolder.getSurface().isValid()) {
            Log.d(TAG, "Surface is not available...");
            return;
        }

        Canvas canvas = mSurfaceHolder.lockCanvas();

        /* draw background */
//        canvas.drawColor(Color.argb(255, 32, 35, 75));


        canvas.drawBitmap(bg, 0, 0, null);

        /* drawing stars */
        for (Star star : mStars) {
            star.draw(canvas);
        }

        /* drawing comet */
        mComet.draw(canvas);


        /* draw round */
        mGamePaint.setColor(Color.argb(226, 255, 85, 85));
        mGamePaint.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.roundSize));
        String currentRound = String.valueOf(ROUND);
        canvas.drawText("x " + currentRound, mRound.getIntrinsicWidth() + 20, mContext.getResources().getDimensionPixelSize(R.dimen.padding_top), mGamePaint);
        drawRound(canvas);


        /* draw player */
        mPlayer.draw(canvas);

        /* drawing enemies */
        for (Enemy enemy : mEnemyList) {
            enemy.draw(canvas);
        }

        /* drawing boom effects */
        mBoomsToDraw = new ArrayList<>(mBooms);
        for (Boom boom : mBoomsToDraw) {
            boom.draw(canvas);
            removeBoomsAfterDelay(boom);
        }
        if (mPlayerBoom != null)
            mPlayerBoom.draw(canvas);

        System.gc();

        /* paint score */
        mGamePaint.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.scoreSize));
        mGamePaint.setColor(Color.argb(255, 170, 238, 255));
        String score = "Score: 999999";
        canvas.drawText("Score: " + NumberFormat.getNumberInstance().format(SCORE), (float) (canvas.getWidth() / 2) - mGamePaint.measureText(score) / 2, mContext.getResources().getDimensionPixelSize(R.dimen.padding_top), mGamePaint);

        /* GAME OVER */
        if (isOver) {
            String text = mContext.getResources().getString(R.string.game_over);
            mGamePaint.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.gameOverSize));
            mGamePaint.setColor(Color.WHITE);
            float width = mGamePaint.measureText(text);
            float height = mGamePaint.ascent() + mGamePaint.descent();
            canvas.drawText(text, (float) (canvas.getWidth() / 2) - width / 2, (float) (canvas.getHeight()) / 2 - height / 2, mGamePaint);
        }


        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void drawRound(Canvas canvas) {
        canvas.save();
        canvas.translate(20, mContext.getResources().getDimensionPixelSize(R.dimen.padding_top) - mRound.getIntrinsicHeight());
        mRound.draw(canvas);
        canvas.restore();
    }

    private void removePlayerBoomAfterDelay() {
        boomsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mPlayerBoom = null;
                mBoomsToDraw = new ArrayList<>(mBooms);
            }
        }, 500);
    }

    private void removeBoomsAfterDelay(final Boom boom) {
        boomsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mBooms.remove(boom);
            }
        }, 500);
    }


    public static VectorDrawable getVectorDrawable(Context context, int resourceID) {
        return (VectorDrawable) context.getResources().getDrawable(resourceID, null);
    }


    /**
     * handling input
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP: {
                Log.d(TAG, "release...");
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "pressing down...");
                if (running) {
                    mPlayer.fire();
                    mSoundPool.play(mRocket, 0.5f, 0.5f, 0, 0, 1.0f);
                }
                break;
            }

        }

        return true;
    }


    public void pause()
     /* switch running state */ {
        running = false;
        while (true) {
            /*  wait for the the gameThread to finish then terminate */
            try {
                mGameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!mGameThread.isAlive())
                Log.d(TAG, "GameThread terminated");

            /* un-registering sensor on pause to save power */
            mSensorManager.unregisterListener(this);

            /* pause game loop */
            mGameLoop.pause();
            GAME_SPEED = 0;

            break;
        }

    }

    public void resume() {
         /* switch running state */

        Log.d(TAG, "GameRunnable Resumed");
        running = true;
         /* construct a new thread*/
        mGameThread = new Thread(this);
         /* naming our game thread*/
        mGameThread.setName("GameThread");
        mGameThread.start();
        Log.d(TAG, "GameThread started");

        /* registering sensor on resume to save power */
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        /* stop game loop */
        mGameLoop.start();

    }


    public void vibrate() {
        Log.d(TAG, "vibrate called...");

        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) mContext.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) mContext.getSystemService(VIBRATOR_SERVICE)).vibrate(500);
        }
    }


    public void releaseResources() {
        mGameLoop.release();
        mSoundPool.release();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            y = event.values[1];
//            Log.d(TAG, "ACCELEROMETER Y: " + y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
