package com.complover116.qar_1;

import java.io.Serializable;
import java.nio.ByteBuffer;
import android.graphics.*;

public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5377012459758221762L;
	public static final double speedX = 10;
	public double x;
	public double y;
	public int skin;
	public boolean joined = false;
	public int rightKey;
	public int leftKey;
	public int jumpKey;
	public int fireKey;
	public int hitdelay = 10;
	public int health = 3;
	public int firedelay= 250;
	public byte horizMov = 0;
	public double yVel = 0;
	boolean onGround = false;
	public byte jumpsLeft = 1;
	boolean looksRight = true;
	public Player(int x, int y, int skin) {
		this.x = x;
		this.y = y;
		this.skin = skin;
	}
	public String getHealth() {
		if(health >= 0) return ""+this.health; else return "X";
	}
	public void update(ByteBuffer data) {
		this.x = data.getDouble();
		this.y = data.getDouble();
		this.looksRight = data.get() == 1;
		this.yVel = data.getDouble();
		this.horizMov = data.get();
		this.health = data.get();
	}
	public void downdate(ByteBuffer data) {
		data.putDouble(this.x);
		data.putDouble(this.y);
		if(this.looksRight){
			data.put((byte) 1);
		} else 
			data.put((byte) 0);
		data.putDouble(this.yVel);
		data.put(this.horizMov);
		data.put((byte) this.health);
	}
	public void spawn() {
		this.x = (int)(Math.random()*650 + 100);
		this.y = (int)(Math.random()*650 + 100);
	}
	public void SetControls(int rKey, int lKey, int uKey, int fKey) {
		rightKey = rKey;
		leftKey = lKey;
		jumpKey = uKey;
		fireKey = fKey;
	}
	public void tick() {
		firedelay --;
		hitdelay --;
			this.yVel += 1;
			double newY = y + this.yVel;
			boolean flag = true;
			for(int i = 0; i < CurGame.lvl.platforms.size(); i ++){
			Rectangle r = CurGame.lvl.platforms.get(i).rect;
			if(new Rectangle((int) x, (int)newY, 32,32).intersects(r)){
				flag = false;
				
				if(this.yVel > 0){
				this.jumpsLeft = 1;
				if(CurGame.lvl.platforms.get(i).type == 2&& this.firedelay < 0) {
					if(CurGame.lvl.platforms.get(i).owner == this.skin) {
						CurGame.lvl.platforms.get(i).captureProgress ++;
					} else {
						CurGame.lvl.platforms.get(i).captureProgress --;
						if(CurGame.lvl.platforms.get(i).captureProgress < 1) {
							CurGame.lvl.platforms.get(i).owner = this.skin;
						}
					}
				}
				if(CurGame.lvl.platforms.get(i).type == 3&&this.hitdelay<-10) {
					//SoundHandler.playSound("/sound/effects/hurt2.wav");
					this.getHit();
				}
				if(CurGame.lvl.platforms.get(i).type == 1) {this.jumpsLeft = 2;
				if(!this.onGround)
					//SoundHandler.playSound("/sound/effects/candj.wav");
					
					CurGame.lvl.TADs.add(new Particle(this.x+40-Math.random()*48,this.y+38-Math.random()*12,1, Color.rgb(0,255,0), 10));
				}
				if(!this.onGround)
					for(int j = 0; j < 10; j ++)
						CurGame.lvl.TADs.add(new Particle(this.x+40-Math.random()*48,this.y+38-Math.random()*12,1, Color.rgb(0,0,0), 10));
				this.onGround = true;
				this.y = r.getY() - 32;
				
				
				} else {
					this.y = r.getY() + r.getHeight();
				}
				this.yVel = 0;
				break;
			}
			}
			if(flag){
			y = newY;
			}
		double newX = x + horizMov*speedX;
		flag = true;
		for(int i = 0; i < CurGame.lvl.platforms.size(); i ++){
		Rectangle r = CurGame.lvl.platforms.get(i).rect;
		if(new Rectangle((int) newX, (int)y, 32,32).intersects(r)){
			flag = false;
			if(horizMov == -1) {
				this.x = r.getX()+r.getWidth();
			} else {
				this.x = r.getX()-32;
			}
			break;
		}
		}
		if(flag){
			if(this.onGround&& this.x!= newX)
		CurGame.lvl.TADs.add(new Particle(this.x+40-Math.random()*48,this.y+38-Math.random()*12,1, Color.rgb(0,0,0), 10));
		x = newX;
		} else {
			
			horizMov = 0;
		}
	}
	public static int getSkinColor(int skin) {
		if(skin == 1) return Color.rgb(0,0,200);
		if(skin == 2) return Color.rgb(200,0,0);
		if(skin == 3) return Color.rgb(0,200,0);
		if(skin == 4) return Color.rgb(200,200,0);
		return Color.rgb(0,0,0);
	}
	public int getSkinColor() {
		return getSkinColor(this.skin);
	}
	public void keyPressed(int key) {
		if(key == rightKey){horizMov = 1; this.looksRight = true;}
		if(key == leftKey) {horizMov = -1; this.looksRight = false;}
		if(key == jumpKey&&this.jumpsLeft > 0) {
			if(!this.joined) {
				this.spawn();
				this.joined = true;
			}
			//SoundHandler.playSound("/sound/effects/jump.wav");
			jumpsLeft -= 1;
			yVel = -20;
			if(!this.onGround){
				
				for(int i = 0; i < 10; i ++)
				CurGame.lvl.TADs.add(new Particle(this.x+32-Math.random()*32,this.y+32-Math.random()*32,1, getSkinColor(), 10));
			}
			onGround = false;
		}
		if(key == fireKey){
			if(firedelay < 0){
			if(Math.random() > 0.5) {
				//SoundHandler.playSound("/sound/effects/fire1.wav");
			} else {
				//SoundHandler.playSound("/sound/effects/fire2.wav");
			}
			int fireColor = getSkinColor();
			if(CurGame.isServer)
			if(this.looksRight)
			CurGame.lvl.TADs.add(new Projectile(this.x, this.y, this.skin, 15+this.horizMov*speedX, this.yVel, fireColor));
			else
				CurGame.lvl.TADs.add(new Projectile(this.x, this.y, this.skin, -15+this.horizMov*speedX, this.yVel, fireColor));
			firedelay = 30;
		}
		}
	}
	public void getHit() {
		if(this.hitdelay>0) return;
		this.hitdelay = 20;
		
		//HEALTH
		this.health --;
		//PHYSICAL REACTION
		this.yVel = -30;
		this.horizMov = 0;
		
		//PARTICLES
		for(int i = 0; i < 10; i ++)
			CurGame.lvl.TADs.add(new Particle(this.x+32-Math.random()*32,this.y+32-Math.random()*32,1, getSkinColor(), 10));
		if(this.health < 0) {
			for(int i = 0; i < 40; i ++)
				CurGame.lvl.TADs.add(new Particle(this.x+32-Math.random()*32,this.y+32-Math.random()*32,1, getSkinColor(), 10));
			this.SetControls(0, 0, 0, 0);
			this.x = -100;
			this.y = -100;
		}
	}
	public void keyReleased(int key) {
		if(key == rightKey&&horizMov == 1) horizMov = 0;
		if(key == leftKey&&horizMov == -1) horizMov = 0;
	}
}
