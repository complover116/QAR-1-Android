package com.complover116.qar_1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by complover116 on 12.02.2015.
 */
public class Q1Button {
    public Rectangle dimensions;
    public String image;
    public int pointerid;
    public boolean active = false;
    public int key;
    public Q1Button(String img, Rectangle dims, int key) {
        image = img;
        dimensions = dims;
        this.key = key;
    }
    public void draw(Canvas sas) {
        if(dimensions == null){Log.w("Q1", "Cannot draw Q1Button, not initialized!"); return;}
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        if(active)
        p.setColor(Color.GREEN);
        dimensions.drawOutline(sas, p);
        sas.save();
        float scale = dimensions.width/32;
        sas.translate(dimensions.x + 5, dimensions.y + 5);
        sas.scale(scale, scale);
        if (active) {
            sas.drawBitmap(DrawThread.images.get(image+"_on"), 0, 0, new Paint());
        } else {
            sas.drawBitmap(DrawThread.images.get(image), 0, 0, new Paint());
        }
        sas.restore();
    }

    public void checkPress(MotionEvent event, boolean special) {
        if(dimensions.intersects(new Rectangle((int)event.getX(event.getActionIndex()),(int)event.getY(event.getActionIndex()),1,1))) {
           // System.out.println("intersects!");
            pointerid = event.getPointerId(event.getActionIndex());
            active = true;
            CurGame.keyPress(key);
        }
    }

    public void checkRelease(MotionEvent event, boolean special) {
        if(pointerid == event.getPointerId(event.getActionIndex())){
            active = false;
            CurGame.keyRelease(key);
        }
    }
}
