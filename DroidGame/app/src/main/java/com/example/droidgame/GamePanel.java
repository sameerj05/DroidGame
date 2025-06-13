package com.example.droidgame;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread thread;
    private SceneManager manager;

    public GamePanel(Context context){
        super(context);
        manager = new SceneManager();
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);



        setFocusable(true);
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry =  true;
        while(true){
            try{
                thread.setRunning(false);
                thread.join();
            }catch(Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        manager.recieveTouch(event);
        return true;
        //return super.onTouchEvent(event);
    }

    public void update(){
        manager.update();
    }

    public void draw(Canvas canvas){
       super.draw(canvas);
     //   canvas.drawColor(0x00AAAAAA);
     //  Constants.d.setBounds(canvas.getClipBounds().left,canvas.getClipBounds().top, canvas.getClipBounds().right,canvas.getClipBounds().bottom);
        super.draw(canvas);
        manager.draw(canvas);
    }


}
