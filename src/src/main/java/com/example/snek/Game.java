package com.example.snek;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final GameLoop gameLoop;
    private final Player player;
    private final Food food;


    private int xbound;
    private int ybound;
    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);

        this.xbound= Resources.getSystem().getDisplayMetrics().widthPixels;
        this.ybound= Resources.getSystem().getDisplayMetrics().heightPixels;
        gameLoop = new GameLoop(this, surfaceHolder);
        player = new Player(getContext(), 500,2*500,0,60, this.xbound, this.ybound);
        food = new Food(getContext(), 500,500,30, this.xbound, this.ybound, player);
    }
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(event.getX()>500){
                    player.setTurn(-1);
                }
                else{
                    player.setTurn(1);
                }
                return true;
            case MotionEvent.ACTION_UP:
                player.setTurn(0);
        }
        return super.onTouchEvent(event);
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) { gameLoop.startLoop(); }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) { }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) { }

    public void drawUPS(Canvas canvas){
        String averageUPS=Double.toString(gameLoop.getAverageUPS());
        Paint paint=new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.text);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: "+averageUPS, 100,100, paint);
    }
    public void drawFPS(Canvas canvas){
        String averageFPS=Double.toString(gameLoop.getAverageFPS());
        Paint paint=new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.text);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: "+averageFPS, 100,100, paint);
    }

    public void drawScore(Canvas canvas){
        String score=Integer.toString(food.getScore());
        Paint paint=new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.text);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("score: "+score, 100,200, paint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawFPS(canvas);
        drawFPS(canvas);
        drawScore(canvas);

        int i=0;
        Segment[] s=food.getSegs();
        while(i<s.length-1 && s[i].getActive()){
            s[i].draw(canvas);
            i++;
        }
        player.draw(canvas);
        food.draw(canvas);
        //food.getHitbox().draw(canvas);
    }

    public void update() {
        food.update();
        Segment[] s=food.getSegs();
        int i=s.length-1;
        while(i>-1){
            s[i].update();
            i--;
        }
        player.update();
        food.getHitbox().update();
    }
}
