package server.converter;

import java.util.ArrayList;
import java.util.Collection;

import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.player.HalfMap;
import server.player.HalfMapNode;
import server.player.Terrain;

public class PlayerHalfMapToHalfMapConverter implements IConverter<HalfMap> {

	private PlayerHalfMap playerHalfMap;

	public PlayerHalfMapToHalfMapConverter(PlayerHalfMap playerHalfMap) {
		this.playerHalfMap = playerHalfMap;
	}

	@Override
	public HalfMap convert() {
		ArrayList<HalfMapNode> halfmapnodes = convertPlayerHalfMapNodeToHalfMapNodes(this.playerHalfMap.getMapNodes());
		UniquePlayerIdentifier playerid = convertPlayerID(this.playerHalfMap.getUniquePlayerID());
		HalfMap halfmap = new HalfMap(playerid, halfmapnodes);
		return halfmap;
	}

	private UniquePlayerIdentifier convertPlayerID(String uniquePlayerID) {
		return new UniquePlayerIdentifier(uniquePlayerID);
	}

	private ArrayList<HalfMapNode> convertPlayerHalfMapNodeToHalfMapNodes(Collection<PlayerHalfMapNode> mapNodes) {
		ArrayList<HalfMapNode> nodes = new ArrayList<>();
		for (PlayerHalfMapNode node : mapNodes) {
			Terrain terrain = convertETerrainToTerrain(node.getTerrain());
			int x = node.getX();
			int y = node.getY();
			boolean castleHere = node.isFortPresent();
			nodes.add(new HalfMapNode(x, y, castleHere, terrain));
		}
		return nodes;
	}

	private Terrain convertETerrainToTerrain(ETerrain eterrain) {
		if (eterrain == ETerrain.Grass) {
			return Terrain.GRASS;
		} else if (eterrain == ETerrain.Mountain) {
			return Terrain.MOUNTAIN;
		} else {
			return Terrain.WATER;
		}
	}

}
