package com.complover116.qar_1;

import java.util.ArrayList;

public class Map {
	public ArrayList<Platform> platforms = new ArrayList<Platform>();
	public static final Map map1 = new Map();
	public static final Map map2 = new Map();
	public static void init() {
		map1.platforms.add(new Platform(new Rectangle(0, 0, 10, 800), 0));
		map1.platforms.add(new Platform(new Rectangle(790, 0, 10, 800), 0));
		map1.platforms.add(new Platform(new Rectangle(0, 760, 800, 10), 0));
		map1.platforms.add(new Platform(new Rectangle(0, 600, 100, 10), 0));
		map1.platforms.add(new Platform(new Rectangle(0, 250, 100, 10), 0));
		map1.platforms.add(new Platform(new Rectangle(700, 600, 100, 10), 0));
		map1.platforms.add(new Platform(new Rectangle(700, 250, 100, 10), 0));
		map1.platforms.add(new Platform(new Rectangle(300, 450, 200, 10), 1));
		
		map2.platforms.add(new Platform(new Rectangle(0, 0, 10, 800), 0));
		map2.platforms.add(new Platform(new Rectangle(790, 0, 10, 800), 0));
		map2.platforms.add(new Platform(new Rectangle(0, 760, 800, 10), 0));
		
		map2.platforms.add(new Platform(new Rectangle(100, 650, 250, 10), 0));
		map2.platforms.add(new Platform(new Rectangle(450, 650, 250, 10), 0));
		map2.platforms.add(new Platform(new Rectangle(300, 565, 200, 10), 0));
		map2.platforms.add(new Platform(new Rectangle(50, 500, 50, 10), 0));
		map2.platforms.add(new Platform(new Rectangle(700, 500, 50, 10), 0));
		map2.platforms.add(new Platform(new Rectangle(300, 250, 200, 200), 0));
		map2.platforms.add(new Platform(new Rectangle(160, 250, 200, 10), 0));
		map2.platforms.add(new Platform(new Rectangle(450, 250, 200, 10), 0));
		map2.platforms.add(new Platform(new Rectangle(150, 440, 150, 10), 1));
		map2.platforms.add(new Platform(new Rectangle(500, 440, 150, 10), 1));
		map2.platforms.add(new Platform(new Rectangle(400, 150, 10, 100), 0));
		map2.platforms.add(new Platform(new Rectangle(380, 130, 50, 20), 3));
	}
}
