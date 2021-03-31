package com.example.snek;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class Segment {
    private double x;
    private double y;
    private double px;
    private double py;
    private double radius;
    private double radiust;
    private int color;
    private int id;
    private Paint paint;
    private Player player;
    private boolean active;
    private Segment[] seg;
    private int ticks;
    private Hitbox hitbox;

    private SoundPool mSP;
    private int mBeepID = -1;

    public Segment(Context context, int k, double r, Player p, Segment[] s, int yellow, Hitbox h){
        this.x=0;
        this.y=0;
        this.radius=0;
        this.radiust=r;
        this.id=k;
        this.player=p;
        this.active=false;
        this.seg=s;
        this.ticks=0;
        this.hitbox=h;

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
        if(yellow==-1){
            this.color= ContextCompat.getColor(context, R.color.seg);
        }
        else{
            this.color= ContextCompat.getColor(context, R.color.player);
        }
        paint.setColor(color);
    }

    public void setPosition(double positionX, double positionY) {
        this.x=positionX;
        this.y=positionY;
    }

    public boolean hitCheck(Hitbox p){
        double prox=Math.sqrt(((this.x-p.getX())*(this.x-p.getX()))+((this.y-p.getY())*(this.y-p.getY())));
        if(this.radius+hitbox.getRadius()>=prox){
            return true;
        }
        return false;
    }

    public void update(){
        if(active){
            if(this.id==0){
                setPosition(player.getX(), player.getY());
            }
            else{
                setPosition(seg[id-1].getx(), seg[id-1].gety());
            }
            if(seg[id].radius<seg[id].radiust){
                this.radius++;
            }
            if(this.id>this.radius+10&&hitCheck(hitbox)&&this.ticks==0){
                mSP.play(mBeepID, 1, 1, 0, 0, 1);
                ticks=10;
            }
            if(ticks>0){
                ticks--;
            }
        }
    }

    public void spawn(){
        this.active=true;
    }
    public void draw(Canvas canvas){canvas.drawCircle((float)x, (float)y, (float)radius, paint);}
    public boolean getActive(){return this.active;}
    public double getx(){return this.x;}
    public double gety(){return this.y;}
}
