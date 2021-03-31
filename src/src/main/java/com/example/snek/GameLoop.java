package com.example.snek;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.Observer;

public class GameLoop extends Thread{
    private static final double MAX_UPS = 40.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;
    private boolean isRunning=false;
    private SurfaceHolder surfaceHolder;
    private Game game;
    private double averageUPS;
    private double averageFPS;
    private int eTime;

    public GameLoop(Game game, SurfaceHolder surfaceHolder){
        this.game = game;
        this.surfaceHolder = surfaceHolder;
        this.eTime=0;
    }
    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        isRunning=true;
        start();
    }

    @Override
    public void run() {
        super.run();

        int updateCount=0;
        int frameCount=0;

        long startTime;
        long elapsedTime;
        long sleepTime;
        //game loop
        Canvas canvas=null;
        startTime=System.currentTimeMillis();
        while(isRunning){
            //update/render
            try{
                canvas=surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    game.update();
                    updateCount++;
                    game.draw(canvas);
                }

            }catch(IllegalArgumentException e){
                e.printStackTrace();
            }
            finally {
                if(canvas!=null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                }
            }
            //pause
            elapsedTime=System.currentTimeMillis()-startTime;
            sleepTime=(long)(updateCount*UPS_PERIOD-elapsedTime);
            if(sleepTime>0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //skip
            while (sleepTime<0 && updateCount<MAX_UPS-1){
                game.update();
                updateCount++;
                elapsedTime=System.currentTimeMillis()-startTime;
                sleepTime=(long)(updateCount*UPS_PERIOD-elapsedTime);
            }
            //calc
            elapsedTime=System.currentTimeMillis()-startTime;
            if(elapsedTime>=1000){
                averageUPS = updateCount/(1E-3*elapsedTime);
                averageFPS = frameCount/(1E-3*elapsedTime);
                updateCount=0;
                frameCount=0;
                startTime=System.currentTimeMillis();
            }
            eTime++;
            game.update();
        }
    }
    public long getElapsedTime(){ return eTime;}
}
