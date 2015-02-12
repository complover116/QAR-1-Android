package com.complover116.qar_1;

public class TickerThread implements Runnable {

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// SAY WHAT?
			System.err.println("SAY WHAT?");
		}
		Map.init();
		CurGame.lvl.loadMap(Map.map2);
		System.out.println("TickerThread has started...");
		while(true) {
			long tickstart = System.nanoTime();
			Ticker.tick();
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
