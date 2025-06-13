package com.example.droidgame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Constants {
    public static int SCREEN_WIDTH=1080;
    public static int SCREEN_HEIGHT=1920;
    public static Context CURRENT_CONTEXT;
    public static long INIT_TIME=(int)(System.currentTimeMillis());
    public static long MAX_PLAY_TIME = 30;
    public static MediaPlayer mp;
    public static int noHits=0;
    public static int hitsound=0;
    public static SoundPool soundPool;
    public static Drawable d;
}
