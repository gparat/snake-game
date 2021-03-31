package com.example.snek;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class Food {
    private double x;
    private double y;
    private double radius;
    private int xbound;
    private int ybound;
    private int color;
    private int score;
    private Paint paint;
    private Player player;
    private Segment[] seg;
    private int current;
    private int at;
    private int yellow;
    private SoundPool mSP;
    private int mBeepID = -1;
    private Hitbox hitbox;

    public Food(Context context, double X, double Y, double r, int xb, int yb, Player p){
        this.x=X;
        this.y=Y;
        this.radius=r;
        this.xbound=xb;
        this.ybound=yb;
        this.player=p;
        this.score=0;
        int current =0;
        int at =0;
        this.yellow=-1;

        hitbox=new Hitbox(context,0,0, p.getRadius()/2,p);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        mBeepID = mSP.load(context, R.raw.bop, 1);

        paint=new Paint();
        this.color= ContextCompat.getColor(context, R.color.eat);
        paint.setColor(color);

        seg= new Segment[1000];
        for(int i=0; i<seg.length; i++){
            seg[i]=new Segment(context, i, player.getRadius(),p,seg,yellow,hitbox);
            if(i%5==0){
                yellow*=-1;
            }
        }
    }
    public void draw(Canvas canvas) {
        canvas.drawCircle((float)x, (float)y, (float)radius, paint);
    }
    public void update(){
        if (x+this.radius >= xbound) {
            this.x-=this.radius;
        }
        else if(x-this.radius <= 0){
            this.x+=this.radius;
        }
        if (y+this.radius >= ybound) {
            this.y-=this.radius;
        }
        else if(y-this.radius <= 0){
            this.y+=this.radius;
        }
        if(hitCheck(player)){
            setPosition(Math.random()*getXbound(), Math.random()*getYbound());
            score++;
            at +=5;
            if(at<seg.length-1 && !seg[current].getActive()){
                while(current!=at){
                    seg[current].spawn();
                    current++;
                }
            }
            mSP.play(mBeepID, 1, 1, 0, 0, 1);
        }
    }

    public boolean hitCheck(Player p){
        double prox=Math.sqrt(((this.x-p.getX())*(this.x-p.getX()))+((this.y-p.getY())*(this.y-p.getY())));
        if(this.radius+p.getRadius()>=prox){
            return true;
        }
        return false;
    }

    public void setPosition(double positionX, double positionY) {
        this.x=positionX;
        this.y=positionY;
    }

    public int getScore(){return this.score;}
    public int getXbound(){return this.xbound;}
    public int getYbound(){return this.ybound;}
    public Segment[] getSegs(){return this.seg;}
    public Hitbox getHitbox(){
        return this.hitbox;
    }
}
