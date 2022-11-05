package com.lllslll.wallpaper.growfractal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.lllslll.library.wallpaper.CustomWallpaper;

/**
 * Created by ISU on 2019-01-25.
 */

public class GrowFractalWallpaper extends CustomWallpaper
{
    private boolean isFrameLimited = true;

    private int frameCount = 60, updateCount = 40, screenHeight;

    private float rotateDegree = 0;

    private GrowFractal fractal[] = null;

    public GrowFractalWallpaper()
    {
        this.setFrameLimit(this.isFrameLimited);
        this.setFrameCount(this.frameCount);
        this.setUpdateForSec(this.updateCount);
    }

    @Override
    protected void onSurfaceSizeChanged(int width, int height)
    {
        this.reset(width, height);
    }

    @Override
    protected void onSurfaceVisibilityChanged(boolean visible)
    {

    }

    @Override
    protected void onUpdate(long deltaTime)
    {
        if(this.fractal != null) {
            for(int a = 0, l = this.fractal.length; a < l; a ++) {
                if(this.fractal[a] != null)
                    this.fractal[a].update(deltaTime);
            }
        }
        this.rotateDegree += 3.0f / 1000.0f * deltaTime;
        this.rotateDegree %= 360.0f;
    }

    @Override
    protected void onDraw(Canvas canvas, Paint paint, int width, int height, long deltaTime)
    {
        canvas.drawColor(Color.BLACK);

        if(this.screenHeight != height)
            this.reset(width, height);
        canvas.save();
        canvas.rotate(this.rotateDegree, width / 2, height / 2);
        if(this.fractal != null) {
            for(int a = 0, l = this.fractal.length; a < l; a ++) {
                if(this.fractal[a] != null)
                    this.fractal[a].draw(canvas, paint);
            }
        }
        canvas.restore();
    }

    private void reset(int w, int h)
    {
        GrowFractal.setScreenHeight(h);
        GrowFractal.setRotateDegree(35);

        this.screenHeight = h;

        /*this.fractal = new GrowFractal[2];
        this.fractal[0] = new GrowFractal(w / 2, h);
        this.fractal[1] = new GrowFractal(w / 2, 0);
        this.fractal[1].setDegree(-180);*/

        this.fractal = new GrowFractal[8];
        for(int a = 0, l = this.fractal.length; a < l; a ++) {
            this.fractal[a] = new GrowFractal(w / 2, h / 2);
            this.fractal[a].setMaxLength(h / 11);
            this.fractal[a].setDegree(45 * a + 45);
            this.fractal[a].setThickness(10);
        }
    }
}
