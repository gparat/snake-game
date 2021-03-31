package com.example.snek;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Player {
    private double x;
    private double y;
    private double angle;
    private double angleR;
    public double pa;
    private double speed;
    private double radius;
    private Paint paint;
    private int xbound;
    private int ybound;
    private int color;
    private int eye1;
    private int eye2;
    private int turning;

    public Player(Context context, double X, double Y, double a, double r, int xb, int yb){
        this.x=X;
        this.y=Y;
        this.pa=-999999.8;
        this.angle=a;
        this.angleR=Math.toRadians(this.angle);
        this.radius=r;
        this.xbound=xb;
        this.ybound=yb;
        this.speed=5;
        this.turning=0;

        paint=new Paint();
        this.color= ContextCompat.getColor(context, R.color.player);
        this.eye1=ContextCompat.getColor(context, R.color.sclera);
        this.eye2=ContextCompat.getColor(context, R.color.pupil);
    }

    public void draw(Canvas canvas) {
        paint.setColor(color);
        canvas.drawCircle((float)x, (float)y, (float)radius, paint);
        paint.setColor(eye1);
        canvas.drawCircle((float)(x+((this.radius*.66)*Math.sin(angleR+Math.toRadians(45)))), (float)(y+(((this.radius*.66)*Math.cos(angleR+Math.toRadians(45))))), (float)radius/3, paint);
        canvas.drawCircle((float)(x+((this.radius*.66)*Math.sin(angleR-Math.toRadians(45)))), (float)(y+(((this.radius*.66)*Math.cos(angleR-Math.toRadians(45))))), (float)radius/3, paint);
        paint.setColor(eye2);
        canvas.drawCircle((float)(x+((this.radius*.80)*Math.sin(angleR+Math.toRadians(45)))), (float)(y+(((this.radius*.80)*Math.cos(angleR+Math.toRadians(45))))), (float)radius/5, paint);
        canvas.drawCircle((float)(x+((this.radius*.80)*Math.sin(angleR-Math.toRadians(45)))), (float)(y+(((this.radius*.80)*Math.cos(angleR-Math.toRadians(45))))), (float)radius/5, paint);
    }

    public void update() {
        angle+=turning*5;
        this.angleR=Math.toRadians(this.angle);
        if (x+this.radius >= xbound) {
            this.angle *=-1;
            this.x-=this.radius;
        }
        else if(x-this.radius <= 0){
            this.angle *=-1;
            this.x+=this.radius;
        }
        if (y+this.radius >= ybound) {
            this.angle -=180;
            this.y-=this.radius;
        }
        else if(y-this.radius <= 0){
            this.angle -=180;
            this.y+=this.radius;
        }
        this.x += this.speed*Math.sin(this.angleR);
        this.y += this.speed*Math.cos(this.angleR);
    }

    public double getX(){return this.x;}
    public double getY(){return this.y;}
    public double getPa(){return this.pa;}
    public double getRadius(){return this.radius;}
    public void setX(int n){}
    public void setY(int n){}
    public double getAngle(){return this.angle;}
    public void setAngle(double n){this.angle=n;}
    public void setTurn(int n){
        this.turning=n;
    }
    public double getSpeed() {return this.speed;}
    public double getAngleR() {return this.angleR;}
}
