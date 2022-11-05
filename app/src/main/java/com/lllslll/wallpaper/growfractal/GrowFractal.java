package com.lllslll.wallpaper.growfractal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.lllslll.library.wallpaper.MathUp;

/**
 * Created by ISU on 2019-01-25.
 */

public class GrowFractal
{
    private static float firstLength, rotateDegree;

    private GrowFractal child[] = new GrowFractal[2];

    private boolean isFirst = false, haveChild = false, isReversed = false;

    private int color = Color.WHITE, endDeltaTime;

    private float x, y, length, maxLength, degree = 0, thick = 15, speed;

    public static void setScreenHeight(int h)
    {
        firstLength = h / 5.5f;
    }

    public static void setRotateDegree(float degree)
    {
        rotateDegree = degree;
    }

    public GrowFractal(float x, float y)
    {
        this.isFirst = true;
        this.x = x;
        this.y = y;
        this.length = 0;
        this.endDeltaTime = 0;
        this.setMaxLength(firstLength);
    }

    public GrowFractal(float x, float y, boolean f)
    {
        this.isFirst = f;
        this.x = x;
        this.y = y;
        this.length = 0;
        this.endDeltaTime = 0;
        this.setMaxLength(firstLength);
    }

    public void setDegree(float degree)
    {
        this.degree = degree;
    }

    public void setThickness(float t)
    {
        this.thick = t;
        if(t < 1)
            t = 1;
    }

    public void setMaxLength(float l)
    {
        this.maxLength = l;
        this.speed = this.maxLength / 1.5f;
    }

    public void draw(Canvas c, Paint p)
    {
        p.setColor(Color.WHITE);
        p.setStrokeWidth(this.thick);

        if(this.length > 0)
            c.drawLine(this.x, this.y, MathUp.getForwardX(this.x, this.degree, this.length), MathUp.getForwardY(this.y, this.degree, this.length), p);

        if(this.haveChild) {
            for(int a = 0; a < 2; a ++) {
                if(this.child[a] != null)
                    this.child[a].draw(c, p);
            }
        }
    }

    public void update(long deltaTime)
    {
        float mSpeed = this.speed * (deltaTime / 1000.0f);
        if(mSpeed < 2)
            mSpeed = 2;
        mSpeed *= this.isReversed ? -1 : 1;

        if(this.child[0] == null && this.child[1] == null)
            this.haveChild = false;

        for(int a = 0; a < 2; a ++) {
            if(this.child[a] != null)
                this.child[a].update(deltaTime);
        }

        this.grow(mSpeed);

        if(!this.isReversed) {
            if(this.maxLength > firstLength / 60.0f) {
                if(this.length >= this.maxLength) {
                    this.addChild();
                    this.isReversed = this.child[0].isReversed && this.child[1].isReversed;
                }
            } else {
                if(this.endDeltaTime >= 1500)
                    this.isReversed = true;
                this.endDeltaTime += deltaTime;
                Log.i("endDeltaTime", this.endDeltaTime+"");
            }
        }
        else {
            if(!this.haveChild) {
                if(this.isFirst && this.length <= 0)
                    this.isReversed = false;
            }
            else {
                for(int a = 0; a < 2; a ++) {
                    if(this.child[a] != null) {
                        if(this.child[a].length <= 0)
                            this.child[a] = null;
                    }
                }
            }
        }
    }

    private void grow(float g)
    {
        if(!this.isReversed & this.length < this.maxLength || this.isReversed & !this.haveChild)
            this.length += g;
        if(this.length > this.maxLength)
            this.length = this.maxLength;
    }

    private void addChild()
    {
        if(this.haveChild)
            return;

        for(int a = 0; a < 2; a++) {
            this.child[a] = new GrowFractal(MathUp.getForwardX(this.x, this.degree, this.length), MathUp.getForwardY(this.y, this.degree, this.length), false);
            this.child[a].setDegree(this.degree + (rotateDegree - (rotateDegree * 2 * a)));
            this.child[a].setMaxLength(this.maxLength / 3.0f * 2.0f);
            this.child[a].setThickness(this.thick / 5.0f * 4.0f);
        }
        this.haveChild = true;
    }
}
