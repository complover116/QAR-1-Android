package com.complover116.qar_1;
import android.graphics.*;


public class Particle implements TAD{
	public double x;
	public double y;
	
	public int shape;
	public int color;
	
	public int lifetime;
	public Particle(double x, double y, int shape, int color, int lt) {
		this.x = x;
		this.y = y;
		this.shape = shape;
		this.color = color;
		this.lifetime = lt;
	}
	@Override
	public void tick() {
		this.lifetime --;
	}

	@Override
	public void draw(Canvas g2d) {
		Paint p = new Paint();
		p.setColor(color);
		switch(shape) {
		case 1:
			
			g2d.drawRect((float)(this.x - lifetime/2),(float)(this.y - lifetime/2), (float)(this.lifetime+x),(float) (this.lifetime+y), p);
		break;
		}
	}

	@Override
	public boolean isDead() {
		if(this.lifetime < 0) return true;
		return false;
	}
	
}
