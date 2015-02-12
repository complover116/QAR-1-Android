package com.complover116.qar_1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.HashMap;
public class DrawThread implements Runnable
{
	public static SurfaceHolder surfaceholder;
	public static HashMap<String,Bitmap> images = new HashMap<String, Bitmap>();
	public static void paint(){
		Canvas sas = surfaceholder.lockCanvas();
		sas.drawColor(Color.WHITE);
		Paint p2 = new Paint();
		p2.setColor(Color.BLACK);
        //DRAW CONTROLS
        sas.drawRect(0,0,Render.leftBound,Render.height/2, p2);
        sas.drawRect(Render.height/2,0,Render.leftBound,Render.height, p2);
        sas.drawRect(0,Render.rightBound,Render.width,Render.height/2, p2);
        sas.drawRect(Render.height/2,Render.rightBound,Render.width,Render.height, p2);
        //DRAW Q1 WORLD
        sas.save();
        sas.translate(Render.leftBound, 0);
        sas.scale(Render.canvaScale, Render.canvaScale);
		//p2.setAlpha(40);
		//RENDER PLATFORMS
		//System.out.println(CurGame.lvl.platforms.size());
		for(int i = 0; i < CurGame.lvl.platforms.size(); i ++){
			int color = Color.BLUE;
			switch(CurGame.lvl.platforms.get(i).type) {
				case 0:
					color = Color.BLACK;
					break;
				case 1:
					color = Color.GREEN;
					break;
				case 2:
					/*g2d.setColor(Player.getSkinColor(CurGame.lvl.platforms.get(i).owner));
					g2d.draw(new Rectangle(CurGame.lvl.platforms.get(i).rect.x,CurGame.lvl.platforms.get(i).rect.y - 40, CurGame.lvl.platforms.get(i).rect.width, 8));
					g2d.fill(new Rectangle(CurGame.lvl.platforms.get(i).rect.x,CurGame.lvl.platforms.get(i).rect.y - 40, (int)((double)CurGame.lvl.platforms.get(i).rect.width*((double)CurGame.lvl.platforms.get(i).captureProgress/(double)250)), 8));
*/

					break;
				case 3:
					color = Color.RED;
					break;
			}
			Paint p = new Paint();
			p.setColor(color);
			CurGame.lvl.platforms.get(i).rect.draw(sas, p);
		}
		//RENDER PLAYERS
		for(int i = 0; i < CurGame.lvl.players.size(); i++)
			if(CurGame.lvl.players.get(i).looksRight)
				sas.drawBitmap(images.get("player"+CurGame.lvl.players.get(i).skin), (int)CurGame.lvl.players.get(i).x, (int)CurGame.lvl.players.get(i).y,new Paint());
			else
				sas.drawBitmap(images.get("player"+CurGame.lvl.players.get(i).skin+"_left"), (int)CurGame.lvl.players.get(i).x, (int)CurGame.lvl.players.get(i).y,new Paint());

		//RENDER PROJECTILES
		for(int i = 0; i < CurGame.lvl.TADs.size(); i++) {
			CurGame.lvl.TADs.get(i).draw(sas);
		}

		//RENDER PLAYER's health
		/*g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g2d.setColor(new Color(0, 0, 255));
		g2d.drawImage(ResourceContainer.images.get("/img/player1.png"), AffineTransform.getTranslateInstance(50, 810), null);
		g2d.drawString(CurGame.lvl.players.get(0).getHealth(), 100, 840);

		g2d.setColor(new Color(255, 0, 0));
		g2d.drawImage(ResourceContainer.images.get("/img/player2.png"), AffineTransform.getTranslateInstance(150, 810), null);
		g2d.drawString(CurGame.lvl.players.get(1).getHealth(), 200, 840);

		g2d.setColor(new Color(0, 255, 0));
		g2d.drawImage(ResourceContainer.images.get("/img/player3.png"), AffineTransform.getTranslateInstance(250, 810), null);
		g2d.drawString(CurGame.lvl.players.get(2).getHealth(), 300, 840);

		g2d.setColor(new Color(155, 155, 0));
		g2d.drawImage(ResourceContainer.images.get("/img/player4.png"), AffineTransform.getTranslateInstance(350, 810), null);
		g2d.drawString(CurGame.lvl.players.get(3).getHealth(), 400, 840);
		if(ClientThread.timeout > ClientThread.timeoutLow) {
			g2d.setColor(new Color(255,0,0));
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			g2d.drawString("WARNING:No message from server for "+ClientThread.timeout/10+" seconds", 100, 500);
		}*/
        sas.restore();
		surfaceholder.unlockCanvasAndPost(sas);
	}
	public void run() {
		new Thread(new TickerThread()).start();
		//while(true){
		//DRAW
		//Canvas sas = surfaceholder.lockCanvas();
		//sas.drawColor(Color.GREEN);
		//sas.drawBitmap(player1,Render.x,Render.y,new Paint());
		//surfaceholder.unlockCanvasAndPost(sas);
		//}
	}
	public DrawThread(SurfaceHolder surfhold, Resources resources) {
		images.put("player1", BitmapFactory.decodeResource(resources, R.drawable.player1));
		images.put("player2", BitmapFactory.decodeResource(resources, R.drawable.player2));
		images.put("player3", BitmapFactory.decodeResource(resources, R.drawable.player3));
		images.put("player4", BitmapFactory.decodeResource(resources, R.drawable.player4));
		images.put("player1_left", BitmapFactory.decodeResource(resources, R.drawable.player1_left));
		images.put("player2_left", BitmapFactory.decodeResource(resources, R.drawable.player2_left));
		images.put("player3_left", BitmapFactory.decodeResource(resources, R.drawable.player3_left));
		images.put("player4_left", BitmapFactory.decodeResource(resources, R.drawable.player4_left));
		surfaceholder = surfhold;
	}
}
