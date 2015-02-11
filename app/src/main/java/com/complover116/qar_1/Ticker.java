package com.complover116.qar_1;

public class Ticker {
	public static int ticktime = 0;
	public static void tick() {
		
		
		//TICK PLAYERS
		for(int i = 0; i < CurGame.lvl.players.size(); i ++)
			CurGame.lvl.players.get(i).tick();
		//TICK TADs
				for(int i = 0; i < CurGame.lvl.TADs.size(); i ++){
					if(CurGame.lvl.TADs.get(i).isDead()) {
						CurGame.lvl.TADs.remove(i);
						i --;
					} else {
						CurGame.lvl.TADs.get(i).tick();
					}
				}
		/*if(Loader.isServer) {
			ServerThread.sendData();
		}*/
	}
}
