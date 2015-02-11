package com.complover116.qar_1;
import android.graphics.*;

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
		//System.out.println(Rect.intersects(r1,r2));
		return Rect.intersects(r1,r2);
	}
	public void draw(Canvas g2d, Paint p){
		g2d.drawRect(x,y,x+width,y+height,p);
		//System.out.println("Rectangle drawn");
	}
}
