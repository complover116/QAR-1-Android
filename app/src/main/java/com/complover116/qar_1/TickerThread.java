package com.complover116.qar_1;

public class TickerThread implements Runnable {
    public Main mClass;
    public byte time = 0;
    public TickerThread(Main m) {
        mClass = m;
    }
	@Override
	public void run() {
        while(DrawThread.surfaceholder== null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // SAY WHAT?
                System.err.println("SAY WHAT?");
            }
        }
		Map.init();
		CurGame.lvl.loadMap(Map.map1);
		System.out.println("TickerThread has started...");
		while(true) {
			long tickstart = System.nanoTime();


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
            if(CurGame.isServer)
            mClass.sendData();


			DrawThread.paint();
			int ttMillis = (int) ((System.nanoTime()- tickstart)/1000000);
			if(ttMillis < 20) {
				try {
					Thread.sleep(20 - ttMillis);
				} catch (InterruptedException e) {
                    // SAY WHAT?
                    System.err.println("SAY WHAT?");
				}
			}
		}
	}

}
