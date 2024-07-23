package server.map;

import server.player.Terrain;

public class MapNode {

	private PlayerPositionState playerPositionState;
	private Terrain terrain;
	private TreasureState treasureState;
	private CastleState castleState;
	private int x;
	private int y;

	public MapNode(PlayerPositionState playerPositionState, Terrain terrain, TreasureState treasureState,
			CastleState castleState, int x, int y) {
		super();
		this.playerPositionState = playerPositionState;
		this.terrain = terrain;
		this.treasureState = treasureState;
		this.castleState = castleState;
		this.x = x;
		this.y = y;
	}

	public PlayerPositionState getPlayerPositionState() {
		return playerPositionState;
	}

	public void setPlayerPositionState(PlayerPositionState playerPositionState) {
		this.playerPositionState = playerPositionState;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public TreasureState getTreasureState() {
		return treasureState;
	}

	public void setTreasureState(TreasureState treasureState) {
		this.treasureState = treasureState;
	}

	public CastleState getCastleState() {
		return castleState;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
