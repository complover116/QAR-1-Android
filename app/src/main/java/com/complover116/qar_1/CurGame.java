package com.complover116.qar_1;

public class CurGame {
	public static Level lvl = new Level();
	public static boolean isServer = true;
    public static void keyPress(int key) {
        /*if(!Loader.isServer){
				ClientThread.sendKey(e.getKeyCode(), true);
		}*/
        for(int i = 0; i < CurGame.lvl.players.size(); i ++)
            CurGame.lvl.players.get(i).keyPressed(key);
    }
    public static void keyRelease(int key) {
        /*if(!Loader.isServer){
				ClientThread.sendKey(e.getKeyCode(), false);
		}*/
        for(int i = 0; i < CurGame.lvl.players.size(); i ++)
            CurGame.lvl.players.get(i).keyReleased(key);
    }
}
