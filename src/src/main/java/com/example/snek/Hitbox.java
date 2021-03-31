package com.example.snek;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Hitbox {
    private  Paint paint;
    private int color;
    private double x;
    private double y;
    public double radius;
    private Player player;
    public Hitbox(Context context, double X, double Y, double r, Player p){
        this.x=X;
        this.y=Y;
        this.radius=r;
        this.player=p;

        paint=new Paint();
        this.color= ContextCompat.getColor(context, R.color.seg2);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float)x, (float)y, (float)radius, paint);
    }
    public void update(){
        setPosition((player.getX()+(getRadius())*Math.sin(player.getAngleR())),(player.getY()+((getRadius())*Math.cos(player.getAngleR()))));
    }
    public void setPosition(double X, double Y){
        this.x=X;
        this.y=Y;
    }
    public double getX(){return this.x;}
    public double getY(){return this.y;}
    public double getRadius() {return this.radius;}
}
