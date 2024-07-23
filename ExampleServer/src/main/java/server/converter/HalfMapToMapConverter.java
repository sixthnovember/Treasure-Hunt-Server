package server.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import messagesbase.UniquePlayerIdentifier;
import server.map.CastleState;
import server.map.Map;
import server.map.MapNode;
import server.map.PlayerPositionState;
import server.map.TreasureState;
import server.player.HalfMap;
import server.player.HalfMapNode;
import server.player.Player;
import server.player.Terrain;

public class HalfMapToMapConverter implements IConverter<ArrayList<Map>> {

	private ArrayList<HalfMap> halfmaps;
	private boolean is20x5;
	private boolean treasurePlaced;
	private ArrayList<Player> players;

	public HalfMapToMapConverter(ArrayList<Player> players, ArrayList<HalfMap> halfMaps, boolean is20x5) {
		this.players = players;
		this.halfmaps = halfMaps;
		this.is20x5 = is20x5;
		this.treasurePlaced = false;
	}

	@Override
	public ArrayList<Map> convert() {

		ArrayList<Map> maps = new ArrayList<>();

		int width;
		int height;
		if (this.is20x5) {
			width = 20;
			height = 5;
		} else {
			width = 10;
			height = 10;
		}

		HashMap<UniquePlayerIdentifier, MapNode> playerPositions = new HashMap<UniquePlayerIdentifier, MapNode>();

		for (Player player : this.players) {
			Map map = createMap(player.getPlayerid(), width, height);
			MapNode node = map.getPlayerPosition();
			playerPositions.put(player.getPlayerid(), node);
			maps.add(map);
		}

		for (Map map : maps) {
			UniquePlayerIdentifier currentPlayerId = map.getPlayerid();
			for (Player player : this.players) {
				if (!player.getPlayerid().equals(currentPlayerId)) {
					MapNode enemyPosition = playerPositions.get(player.getPlayerid());
					map.addEnemyPosition(enemyPosition);
				}
			}
		}

		return maps;

	}

	private Map createMap(UniquePlayerIdentifier playerid, int width, int height) {
		this.treasurePlaced = false;
		ArrayList<MapNode> mapnodes = new ArrayList<>();
		Random random = new Random();
		boolean firstHalfMapFirst = random.nextBoolean();

		if (firstHalfMapFirst) {
			boolean playerMap;
			if (this.halfmaps.get(0).getPlayerid().equals(playerid)) {
				playerMap = true;
			} else {
				playerMap = false;
			}
			for (HalfMapNode node : this.halfmaps.get(0).getHalfmapnodes()) {
				mapnodes.add(convertHalfMapNodeToMapNode(node, width, height, playerMap, true));
			}
			playerMap = !playerMap;
			for (HalfMapNode node : this.halfmaps.get(1).getHalfmapnodes()) {
				mapnodes.add(convertHalfMapNodeToMapNode(node, width, height, playerMap, false));
			}
		} else {
			boolean playerMap;
			if (this.halfmaps.get(1).getPlayerid().equals(playerid)) {
				playerMap = true;
			} else {
				playerMap = false;
			}
			for (HalfMapNode node : this.halfmaps.get(1).getHalfmapnodes()) {
				mapnodes.add(convertHalfMapNodeToMapNode(node, width, height, playerMap, true));
			}
			playerMap = !playerMap;
			for (HalfMapNode node : this.halfmaps.get(0).getHalfmapnodes()) {
				mapnodes.add(convertHalfMapNodeToMapNode(node, width, height, playerMap, false));
			}
		}

		return new Map(playerid, mapnodes);
	}

	private MapNode convertHalfMapNodeToMapNode(HalfMapNode node, int width, int height, boolean playerMap,
			boolean isFirst) {

		int x = node.getX();
		int y = node.getY();

		if (!isFirst) {
			if (width == 20) { // map is 20x5
				x += 10; // move coordinate right to second half
			} else { // map is 10x10
				y += 5; // move coordinate up to second half
			}
		}

		PlayerPositionState playerPositionState;
		CastleState castleState;
		if (node.isCastleHere() && playerMap) {
			playerPositionState = PlayerPositionState.MyPlayerPosition;
			castleState = CastleState.MyCastlePresent;
		} else if (node.isCastleHere() && !playerMap) {
			playerPositionState = PlayerPositionState.NoPlayerPresent;
			castleState = CastleState.EnemyCastlePresent;
		} else {
			playerPositionState = PlayerPositionState.NoPlayerPresent;
			castleState = CastleState.NoOrUnknownCastleState;
		}
		Terrain terrain = node.getTerrain();

		TreasureState treasure = TreasureState.NoOrUnknownTreasureState;
		if (playerMap) {
			if (!treasurePlaced) {
				if (node.getTerrain() == Terrain.GRASS) {
					if (Math.random() < 0.7) {
						treasure = TreasureState.MyTreasureIsPresent;
						this.treasurePlaced = true;
					}
				}
			}
		}

		return new MapNode(playerPositionState, terrain, treasure, castleState, x, y);
	}

}
