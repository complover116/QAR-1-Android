package com.complover116.qar_1;

public class CurGame {
	public static Level lvl = new Level();
	public static boolean isServer = true;
    public static Main mClass;
    public static int color = 0;
    public static String hostID = "ERROR";
    public static final byte STATUS_ENGAGING_MULTIPLAYER = 0;
    public static final byte STATUS_CHOOSING_HOST = 1;
    public static final byte STATUS_PREGAME_MENU = 2;
    public static final byte STATUS_INGAME = 3;
    public static void keyPress(int key) {
        if(!CurGame.isServer){
				mClass.sendKey(key, true);
		}
        for(int i = 0; i < CurGame.lvl.players.size(); i ++)
            CurGame.lvl.players.get(i).keyPressed(key);
    }
    public static void keyRelease(int key) {
        if(!CurGame.isServer){
            mClass.sendKey(key, false);
        }
        for(int i = 0; i < CurGame.lvl.players.size(); i ++)
            CurGame.lvl.players.get(i).keyReleased(key);
    }
}
