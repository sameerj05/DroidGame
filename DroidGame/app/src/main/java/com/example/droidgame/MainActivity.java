package com.example.droidgame;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.CURRENT_CONTEXT = this.getApplicationContext();
        Constants.d = getResources().getDrawable(R.drawable.food);
        setContentView(new GamePanel(this));


        try {
            Constants.mp = MediaPlayer.create(this, R.raw.epic);
            Constants.mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Constants.soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        Constants.hitsound= Constants.soundPool.load(this,R.raw.blast, 1);
    }

}


