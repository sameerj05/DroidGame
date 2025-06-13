package com.example.droidgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;



public class GameplayScene implements Scene {

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private int musiccnt = 0;
    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private long gameOverTime;
    private boolean music = false;

    private Rect r = new Rect();
    private OrientationData orientationData;
    private long frameTime;
    int elapsedTime=0;
    int eltime=0;

    public GameplayScene() {
        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4);
        System.out.println("GamePlayScene =" + playerPoint);
        player.update(playerPoint);
        Constants.INIT_TIME = (int) (System.currentTimeMillis());
        obstacleManager = new ObstacleManager(250, 350, 75, Color.BLACK);
        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();

    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4);
        player.reset();
        player.update(playerPoint);
        // reset the obstacle manager
        obstacleManager = new ObstacleManager(250, 350, 75, Color.BLACK);
        movingPlayer = false;
        Constants.INIT_TIME = (int) (System.currentTimeMillis());
        orientationData.newGame();
        frameTime = System.currentTimeMillis();
        Constants.mp.start();
    //obstacleManager.startMusic();
        Constants.noHits=0;
        elapsedTime=0;
        eltime=0;
        try {
            if (Constants.mp.isPlaying())
                System.out.println("Music started");
            else
                System.out.println("Problem with MediaPlayer");
        }catch (Exception e) {
            e.printStackTrace();
        }
        music = true;

    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void recieveTouch(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY())) {
                    movingPlayer = true;
                    System.out.println("Movement found");
                }
                if (gameOver && eltime >= 30) {
                    reset();
                    gameOver = false;

                }
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("Move");
                if (!gameOver && movingPlayer) {
                    playerPoint.set((int) event.getX(), (int) event.getY());
                    System.out.println("Move Dow");
                }
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }

    @Override
    public void update() {


        if (!gameOver) {
            System.out.println(" playerpoint(before)= " + playerPoint);
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            eltime= (int) (System.currentTimeMillis() - Constants.INIT_TIME) / 1000;

            frameTime = System.currentTimeMillis();
            if (orientationData.getOrientSet()) {
                if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                    float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                    float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                    float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / 1000f;
                    float ySpeed = pitch * Constants.SCREEN_HEIGHT / 1000f;

                    playerPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                    playerPoint.y -= Math.abs(ySpeed * elapsedTime) > 5 ? ySpeed * elapsedTime : 0;
                }
            }

            if (playerPoint.x < 0)
                playerPoint.x = 0;
            else if (playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;
            if (playerPoint.y < 0)
                playerPoint.y = 0;
            else if (playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            System.out.println(" playerpoint(after)= " + playerPoint);


            player.update(playerPoint);
            obstacleManager.update();
            obstacleManager.playerCollide(player);
            if (eltime> Constants.MAX_PLAY_TIME) {
                player.update();
                player.update(playerPoint);
                gameOver = true;
                music = false;

                try {
                    Constants.mp.pause();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                gameOverTime = System.currentTimeMillis();
            }

        }
        if (!music && !gameOver) {
   /*         obstacleManager.initMusic();
            try {
                if (Constants.mp.isPlaying())
                    System.out.println("Music started");
                else
                    System.out.println("Problem with MediaPlayer");
            }catch (Exception e) {
                e.printStackTrace();
            } */
            music = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
      //  canvas.drawColor(Color.WHITE);
      //  Constants.d.draw(canvas);
   //     canvas.getClipBounds(r);
     //   Constants.d.setBounds(r.left,r.top, r.right,r.bottom);
        player.draw(canvas);
        paint.setColor(Color.WHITE);
        obstacleManager.draw(canvas);

        Constants.d.draw(canvas);
        String Eltime = "Elapsed Time = " + String.valueOf(eltime);
        canvas.getClipBounds(r);
        paint.setTextAlign(Paint.Align.RIGHT);
        float x= r.left + r.width() /2f;
        float y= r.top + 40f ;
        paint.setTextSize(35);
        canvas.drawText(Eltime, x,  y, paint);
        if (gameOver) {
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Game Over");
        }
    }


    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}



