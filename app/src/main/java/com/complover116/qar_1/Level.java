package com.complover116.qar_1;

import java.util.ArrayList;

public class Level {
	public ArrayList<Platform> platforms = new ArrayList<Platform>();
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<TAD> TADs = new ArrayList<TAD>();
	public void loadMap(Map map) {
		TADs.clear();
		players.clear();
		platforms.clear();
		
		for(int i = 0; i < map.platforms.size(); i ++) {
			this.platforms.add(map.platforms.get(i));
		}
		Player player1 = new Player(-100, -100, 1);
		player1.SetControls(CharData.D, CharData.A, CharData.W, CharData.S);
		players.add(player1);
		
		Player player2 = new Player(-100, -100, 2);
		player2.SetControls(CharData.Right, CharData.Left, CharData.Up, CharData.Down);
		players.add(player2);
		
		Player player3 = new Player(-100, -100, 3);
		player3.SetControls(CharData.K, CharData.H, CharData.U, CharData.J);
		players.add(player3);
		
		Player player4 = new Player(-100, -100, 4);
		player4.SetControls(CharData.NumPlus, CharData.Num8, CharData.Ast, CharData.Num9);
		players.add(player4);
		//this.platforms.add(new Platform(new Rectangle(10, 10, 50, 50), 0));
	}
	public Level() {
			loadMap(Map.map2);	
	}
}
