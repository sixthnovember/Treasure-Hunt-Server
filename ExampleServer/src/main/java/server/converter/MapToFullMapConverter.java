package server.converter;

import java.util.ArrayList;
import java.util.Random;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;
import server.map.CastleState;
import server.map.Map;
import server.map.MapNode;
import server.map.PlayerPositionState;
import server.map.TreasureState;
import server.player.Terrain;

public class MapToFullMapConverter implements IConverter<FullMap> {

	private Map map;
	private boolean enemyPlaced;
	private boolean hideTrueEnemyPosition;
	private boolean showTreasure;
	private boolean showEnemyCastle;
	private boolean treasureFound;

	public MapToFullMapConverter(Map map, boolean hideTrueEnemyPosition, boolean showTreasure, boolean showEnemyCastle,
			boolean treasureFound) {
		this.map = map;
		this.enemyPlaced = false;
		this.hideTrueEnemyPosition = hideTrueEnemyPosition;
		this.showTreasure = showTreasure;
		this.showEnemyCastle = showEnemyCastle;
		this.treasureFound = treasureFound;
	}

	@Override
	public FullMap convert() {
		ArrayList<MapNode> mapnodes = this.map.getMapnodes();
		ArrayList<FullMapNode> fullmapnodes = new ArrayList<>();
		for (MapNode mapnode : mapnodes) {
			EPlayerPositionState ePlayerPositionState = convertToEPlayerPositionState(mapnode.getPlayerPositionState());
			ETerrain eterrain = convertToETerrain(mapnode.getTerrain());
			ETreasureState eTreasureState = convertToETreasureState(mapnode.getTreasureState());
			EFortState eFortState = convertToEFortState(mapnode.getCastleState());
			int x = mapnode.getX();
			int y = mapnode.getY();
			fullmapnodes.add(new FullMapNode(eterrain, ePlayerPositionState, eTreasureState, eFortState, x, y));
		}
		FullMap fullmap = new FullMap(fullmapnodes);
		return fullmap;
	}

	private EFortState convertToEFortState(CastleState castleState) {
		if (castleState == CastleState.MyCastlePresent) {
			return EFortState.MyFortPresent;
		} else if (castleState == CastleState.EnemyCastlePresent) {
			if (this.showEnemyCastle) {
				return EFortState.EnemyFortPresent;
			} else {
				return EFortState.NoOrUnknownFortState;
			}
		} else {
			return EFortState.NoOrUnknownFortState;
		}
	}

	private ETreasureState convertToETreasureState(TreasureState treasureState) {
		if (treasureState == TreasureState.MyTreasureIsPresent) {
			if (this.showTreasure) {
				if (this.treasureFound) {
					return ETreasureState.NoOrUnknownTreasureState;
				} else {
					return ETreasureState.MyTreasureIsPresent;
				}
			} else {
				return ETreasureState.NoOrUnknownTreasureState;
			}
		} else {
			return ETreasureState.NoOrUnknownTreasureState;
		}
	}

	private ETerrain convertToETerrain(Terrain terrain) {
		if (terrain == Terrain.GRASS) {
			return ETerrain.Grass;
		} else if (terrain == Terrain.MOUNTAIN) {
			return ETerrain.Mountain;
		} else {
			return ETerrain.Water;
		}
	}

	private EPlayerPositionState convertToEPlayerPositionState(PlayerPositionState playerPositionState) {
		if (playerPositionState == PlayerPositionState.MyPlayerPosition) {
			if (hideTrueEnemyPosition) {
				if (!this.enemyPlaced) {
					Random random = new Random();
					if (random.nextInt(10) < 5) {
						this.enemyPlaced = true;
						return EPlayerPositionState.BothPlayerPosition;
					}
				}
			}
			return EPlayerPositionState.MyPlayerPosition;
		} else if (playerPositionState == PlayerPositionState.EnemyPlayerPosition) {
			if (hideTrueEnemyPosition) {
				return EPlayerPositionState.NoPlayerPresent;
			} else {
				return EPlayerPositionState.EnemyPlayerPosition;
			}
		} else if (playerPositionState == PlayerPositionState.BothPlayerPosition) {
			if (hideTrueEnemyPosition) {
				return EPlayerPositionState.MyPlayerPosition;
			} else {
				return EPlayerPositionState.BothPlayerPosition;
			}

		} else {
			if (hideTrueEnemyPosition) {
				if (!this.enemyPlaced) {
					Random random = new Random();
					if (random.nextInt(10) < 5) {
						this.enemyPlaced = true;
						return EPlayerPositionState.EnemyPlayerPosition;
					}
				}
			}
			return EPlayerPositionState.NoPlayerPresent;
		}
	}

}
