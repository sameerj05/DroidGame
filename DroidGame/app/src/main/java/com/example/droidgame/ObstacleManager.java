package com.example.droidgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;


import java.util.ArrayList;



public class ObstacleManager {
    //higher index is lower on screen is higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;

    private int score = 0;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color){
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();
        populateObstacles();

    }

    public boolean playerCollide(RectPlayer player){
        for(Obstacle ob: obstacles){
            if(ob.playerCollide(player))
                Constants.noHits++;
            Constants.soundPool.play(Constants.hitsound, 1, 1, 0, 0, 1);

            this.color = Color.RED;
                return true;
        }
        return false;
    }



    private void populateObstacles(){
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while(currY < 0){
            int xStart = (int) (Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;

        }

    }

    public void update(){
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = ((float)Math.sqrt((5 + startTime - initTime)/2000.0))*Constants.SCREEN_HEIGHT/(10000.0f);
        for(Obstacle ob : obstacles){
            ob.incrementY(speed * elapsedTime);
        }
        if(obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT){
            int xStart = (int) (Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight,  color, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
            score++;
        }
    }

    public void draw(Canvas canvas){
        for(Obstacle ob : obstacles)
            ob.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(80);
        paint.setColor(Color.MAGENTA);
        String sc= "Score=" + String.valueOf(score);
        canvas.drawText("" + sc, 50, 30 + paint.descent() - paint.ascent(), paint);
       String hits= "# of Hits= " + String.valueOf(Constants.noHits);
        System.out.println(hits);
       //      paint.setTextSize(40);
    //    paint.setTextAlign(Paint.Align.LEFT);
      //  canvas.drawText(hits,canvas.getWidth() - 300f, 100, paint);
    }
  /*  public void startMusic() {
    try {
        Constants.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Constants.mp.reset();

            }
        });
        Constants.mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                Constants.mp.start();
            }
        });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        public void initMusic () {
            try {

                    Constants.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Constants.mp.reset();

                        }
                    });
                Constants.mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        Constants.mp.setLooping(true);
                        Constants.mp.setVolume(1F,1F);
                        Constants.mp.start();
                    }
                });


                } catch(Exception e){
                e.printStackTrace();
            }
        }*/

}

