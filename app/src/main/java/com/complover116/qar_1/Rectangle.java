package com.complover116.qar_1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Rectangle
{
	public int width;
	public int height;
	public int x;
	public int y;
	public Rectangle(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}
	public int getX() { return x;}
	public int getY() { return y;}
	public int getWidth() { return width;}
	public int getHeight() { return height;}
	public boolean intersects(Rectangle r){
		Rect r1 = new Rect(x, y, x+width, y+height);
		Rect r2 = new Rect(r.x, r.y, r.x+r.width, r.y+r.height);
		return Rect.intersects(r1,r2);
	}
	public void draw(Canvas g2d, Paint p){
		g2d.drawRect(x,y,x+width,y+height,p);
		//System.out.println("Rectangle drawn");
	}
    public void drawOutline(Canvas g2d, Paint p){
        g2d.drawRect(x,y,x+width,y+height,p);
        Paint p2 = new Paint();
        p2.setColor(Color.WHITE);
        g2d.drawRect(x+5,y+5,x+width-5,y+height-5,p2);
        //System.out.println("Rectangle drawn");
    }
}
