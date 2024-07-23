package server.player;

public class HalfMapNode {

	private int x;
	private int y;
	private boolean castleHere;
	private Terrain terrain;

	public HalfMapNode(int x, int y, boolean castleHere, Terrain terrain) {
		this.x = x;
		this.y = y;
		this.castleHere = castleHere;
		this.terrain = terrain;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isCastleHere() {
		return castleHere;
	}

	public Terrain getTerrain() {
		return terrain;
	}

}
