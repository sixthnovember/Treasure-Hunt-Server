package server.map;

import java.util.ArrayList;

import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import server.exceptions.EnemyPlayerNotFoundExcception;
import server.exceptions.GoalPositionNotFoundExcception;
import server.exceptions.PlayerNotFoundExcception;
import server.player.Terrain;

public class Map {

	private UniquePlayerIdentifier playerid;
	private ArrayList<MapNode> mapnodes;
	private MapNode treasureNode;
	private MapNode enemyCastleNode;
	private boolean showTreasure;
	private boolean showEnemyCastle;
	private ArrayList<MapNode> nodesThatRevealTreasure;
	private ArrayList<MapNode> nodesThatRevealEnemyCastle;

	public Map(UniquePlayerIdentifier playerid, ArrayList<MapNode> mapnodes) {
		this.playerid = playerid;
		this.mapnodes = mapnodes;
		this.showTreasure = false;
		this.showEnemyCastle = false;
		this.nodesThatRevealTreasure = new ArrayList<>();
		this.nodesThatRevealEnemyCastle = new ArrayList<>();
		saveTreasurePosition();
		saveEnemyCastlePosition();
		saveNodesThatRevealTreasure();
		saveNodesThatRevealEnemyCastle();
	}

	public UniquePlayerIdentifier getPlayerid() {
		return playerid;
	}

	public ArrayList<MapNode> getMapnodes() {
		return mapnodes;
	}

	public void setPlayerid(UniquePlayerIdentifier playerid) {
		this.playerid = playerid;
	}

	public MapNode getTreasureNode() {
		return treasureNode;
	}

	public MapNode getEnemyCastleNode() {
		return enemyCastleNode;
	}

	public boolean isShowTreasure() {
		return showTreasure;
	}

	public boolean isShowEnemyCastle() {
		return showEnemyCastle;
	}

	public MapNode getPlayerPosition() {
		for (MapNode node : this.mapnodes) {
			if (node.getPlayerPositionState() == PlayerPositionState.BothPlayerPosition
					|| node.getPlayerPositionState() == PlayerPositionState.MyPlayerPosition) {
				return node;
			}
		}
		throw new PlayerNotFoundExcception("PlayerNotFoundExcception",
				"Could not find the player position on the map!");
	}

	public MapNode getEnemyPosition() {
		for (MapNode node : this.mapnodes) {
			if (node.getPlayerPositionState() == PlayerPositionState.BothPlayerPosition
					|| node.getPlayerPositionState() == PlayerPositionState.EnemyPlayerPosition) {
				return node;
			}
		}
		throw new EnemyPlayerNotFoundExcception("EnemyPlayerNotFoundExcception",
				"Could not find the enemy player position on the map!");
	}

	public MapNode getGoalPosition(EMove move) {
		MapNode playerNode = getPlayerPosition();
		int xPlayer = playerNode.getX();
		int yPlayer = playerNode.getY();
		int xGoal = xPlayer;
		int yGoal = yPlayer;
		if (move == EMove.Right) {
			xGoal += 1;
		} else if (move == EMove.Left) {
			xGoal -= 1;
		} else if (move == EMove.Down) {
			yGoal += 1;
		} else {
			yGoal -= 1;
		}
		for (MapNode node : this.mapnodes) {
			if (node.getX() == xGoal && node.getY() == yGoal) {
				return node;
			}
		}
		throw new GoalPositionNotFoundExcception("GoalPositionNotFoundExcception",
				"The position the player wants to go does not exist on the map!");
	}

	private void saveTreasurePosition() {
		for (MapNode node : this.mapnodes) {
			if (node.getTreasureState() == TreasureState.MyTreasureIsPresent) {
				this.treasureNode = node;
			}
		}
	}

	private void saveEnemyCastlePosition() {
		for (MapNode node : this.mapnodes) {
			if (node.getCastleState() == CastleState.EnemyCastlePresent) {
				this.enemyCastleNode = node;
			}
		}
	}

	public void setNewPlayerPosition(int x, int y) {

		MapNode playerNode = getPlayerPosition();

		for (MapNode node : this.mapnodes) {
			if (node.getX() == playerNode.getX() && node.getY() == playerNode.getY()) {
				if (node.getPlayerPositionState() == PlayerPositionState.BothPlayerPosition) {
					node.setPlayerPositionState(PlayerPositionState.EnemyPlayerPosition);
				} else {
					node.setPlayerPositionState(PlayerPositionState.NoPlayerPresent);
				}
			}

		}

		for (MapNode node : this.mapnodes) {
			if (node.getX() == x && node.getY() == y) {
				if (node.getPlayerPositionState() == PlayerPositionState.EnemyPlayerPosition) {
					node.setPlayerPositionState(PlayerPositionState.BothPlayerPosition);
				} else {
					node.setPlayerPositionState(PlayerPositionState.MyPlayerPosition);
				}
			}
		}

	}

	public void checkIfInRange(int x, int y) {
		for (MapNode node : this.nodesThatRevealTreasure) {
			if (node.getX() == x && node.getY() == y) {
				this.showTreasure = true;
			}
		}
		for (MapNode node : this.nodesThatRevealEnemyCastle) {
			if (node.getX() == x && node.getY() == y) {
				this.showEnemyCastle = true;
			}
		}
	}

	public void setNewEnemyPositionInOtherMap(int x, int y) {
		MapNode enemyNode = getEnemyPosition();
		for (MapNode node : this.mapnodes) {
			if (node.getX() == enemyNode.getX() && node.getY() == enemyNode.getY()) {
				if (node.getPlayerPositionState() == PlayerPositionState.BothPlayerPosition) {
					node.setPlayerPositionState(PlayerPositionState.MyPlayerPosition);
				} else {
					node.setPlayerPositionState(PlayerPositionState.NoPlayerPresent);
				}
			}
		}
		for (MapNode node : this.mapnodes) {
			if (node.getX() == x && node.getY() == y) {
				if (node.getPlayerPositionState() == PlayerPositionState.MyPlayerPosition) {
					node.setPlayerPositionState(PlayerPositionState.BothPlayerPosition);
				} else {
					node.setPlayerPositionState(PlayerPositionState.EnemyPlayerPosition);
				}
			}
		}
	}

	public void addEnemyPosition(MapNode enemyPosition) {
		for (MapNode node : this.mapnodes) {
			if (node.getX() == enemyPosition.getX() && node.getY() == enemyPosition.getY()) {
				if (node.getPlayerPositionState() == PlayerPositionState.MyPlayerPosition) {
					node.setPlayerPositionState(PlayerPositionState.BothPlayerPosition);
				} else {
					node.setPlayerPositionState(PlayerPositionState.EnemyPlayerPosition);
				}
			}
		}
	}

	private void saveNodesThatRevealTreasure() {
		ArrayList<MapNode> neighbors = getNeigbours(this.treasureNode);
		this.nodesThatRevealTreasure.add(this.treasureNode);
		for (MapNode node : neighbors) {
			if (node.getTerrain() == Terrain.MOUNTAIN) {
				this.nodesThatRevealTreasure.add(node);
			}
		}
	}

	private void saveNodesThatRevealEnemyCastle() {
		ArrayList<MapNode> neighbors = getNeigbours(this.enemyCastleNode);
		this.nodesThatRevealEnemyCastle.add(this.enemyCastleNode);
		for (MapNode node : neighbors) {
			if (node.getTerrain() == Terrain.MOUNTAIN) {
				this.nodesThatRevealEnemyCastle.add(node);
			}
		}
	}

	private ArrayList<MapNode> getNeigbours(MapNode target) {
		ArrayList<MapNode> neighbors = new ArrayList<>();
		for (MapNode node : this.mapnodes) {
			int xDiff = Math.abs(node.getX() - target.getX());
			int yDiff = Math.abs(node.getY() - target.getY());
			if (xDiff == 0 && yDiff == 0) {
				continue;
			}
			if (xDiff < 2 && yDiff < 2) {
				neighbors.add(node);
			}
		}
		return neighbors;
	}

}
