package server.player;

import java.util.ArrayList;

import messagesbase.UniquePlayerIdentifier;

public class HalfMap {

	private UniquePlayerIdentifier playerid;
	private ArrayList<HalfMapNode> halfmapnodes;

	public HalfMap(UniquePlayerIdentifier playerid, ArrayList<HalfMapNode> halfmapnodes) {
		this.playerid = playerid;
		this.halfmapnodes = halfmapnodes;
	}

	public UniquePlayerIdentifier getPlayerid() {
		return playerid;
	}

	public ArrayList<HalfMapNode> getHalfmapnodes() {
		return halfmapnodes;
	}

}
