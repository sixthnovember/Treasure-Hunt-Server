package server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import org.slf4j.LoggerFactory;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import server.converter.HalfMapToMapConverter;
import server.exceptions.PlayerNotPartOfGameException;
import server.main.ServerManager;
import server.map.CastleState;
import server.map.Map;
import server.map.MapNode;
import server.map.TreasureState;
import server.player.HalfMap;
import server.player.Player;
import server.player.PlayerGameState;
import server.player.Terrain;

public class Game {
	private UniqueGameIdentifier gameid;
	private ArrayList<Player> players;
	private ArrayList<HalfMap> halfMaps;
	private ArrayList<Map> maps;
	private String gamestateid;
	private HashMap<UniquePlayerIdentifier, Integer> stepsToGo;
	private HashMap<UniquePlayerIdentifier, EMove> moveBefore;
	private int roundCounter;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ServerManager.class);

	public Game(UniqueGameIdentifier gameid) {
		this.gameid = gameid;
		this.players = new ArrayList<Player>();
		this.halfMaps = new ArrayList<HalfMap>();
		this.maps = new ArrayList<Map>();
		this.stepsToGo = new HashMap<>();
		this.moveBefore = new HashMap<>();
		this.roundCounter = 0;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public UniqueGameIdentifier getGameid() {
		return gameid;
	}

	public ArrayList<HalfMap> getHalfMaps() {
		return halfMaps;
	}

	public ArrayList<Map> getMaps() {
		return maps;
	}

	public String getGamestateid() {
		return gamestateid;
	}

	public void setGamestateid(String gamestateid) {
		this.gamestateid = gamestateid;
	}

	public int getRoundCounter() {
		return roundCounter;
	}

	public void increaseRoundCounter() {
		this.roundCounter += 1;
	}

	public void addPlayer(UniquePlayerIdentifier playerid, String firstName, String lastName, String uAccount) {
		Player player = new Player(playerid, firstName, lastName, uAccount);
		this.players.add(player);
		if (this.players.size() < 2) {
			this.players.get(0).setPlayerGameState(PlayerGameState.MUSTWAIT);
		} else {
			Random random = new Random();
			if (random.nextBoolean()) {
				this.players.get(0).setPlayerGameState(PlayerGameState.MUSTACT);
				this.players.get(1).setPlayerGameState(PlayerGameState.MUSTWAIT);
			} else {
				this.players.get(0).setPlayerGameState(PlayerGameState.MUSTWAIT);
				this.players.get(1).setPlayerGameState(PlayerGameState.MUSTACT);
			}
		}
	}

	public void addHalfMap(HalfMap halfmap) {
		this.halfMaps.add(halfmap);

		for (Player p : this.players) {
			if (p.getPlayerid().equals(halfmap.getPlayerid())) {
				p.setPlayerGameState(PlayerGameState.MUSTWAIT);
			} else {
				p.setPlayerGameState(PlayerGameState.MUSTACT);
			}
		}

		if (this.maps.isEmpty() && this.halfMaps.size() == 2) {
			combineMaps();
		}

	}

	private void combineMaps() {
		Random random = new Random();
		boolean is20x5 = random.nextBoolean();
		HalfMapToMapConverter converter = new HalfMapToMapConverter(this.players, this.halfMaps, is20x5);
		ArrayList<Map> maps = converter.convert();
		this.maps = maps;
	}

	public Optional<Map> getMap(UniquePlayerIdentifier playerid) {
		for (Map map : this.maps) {
			if (map.getPlayerid().equals(playerid)) {
				return Optional.of(map);
			}
		}
		return Optional.empty();
	}

	public Player getPlayer(UniquePlayerIdentifier playerid) {
		for (Player player : this.players) {
			if (player.getPlayerid().equals(playerid)) {
				return player;
			}
		}
		throw new PlayerNotPartOfGameException("PlayerNotPartOfGameException",
				"Player could not be found in this game!");
	}

	public void playerSentMap(UniquePlayerIdentifier playerid) {
		for (Player player : this.players) {
			if (player.getPlayerid().equals(playerid)) {
				player.setHalfMapSent(true);
			}
		}
	}

	public void setNewPlayerPosition(UniquePlayerIdentifier playerid, EMove move) {

		boolean positionChanged = false;
		MapNode changedPosition = new MapNode(null, null, null, null, 0, 0);

		for (Map map : this.maps) {
			if (map.getPlayerid().equals(playerid)) {

				MapNode nodePlayer = map.getPlayerPosition();
				MapNode nodeGoal = map.getGoalPosition(move);

				if (this.moveBefore.get(playerid) == null) {
					int stepsToEnter = calculateSteps(nodeGoal.getTerrain());
					int stepsToLeave = calculateSteps(nodePlayer.getTerrain());
					int stepsTotal = stepsToLeave + stepsToEnter;
					this.stepsToGo.put(playerid, stepsTotal);
					this.moveBefore.put(playerid, move);
				}

				if (!this.moveBefore.get(playerid).equals(move)) {
					logger.warn(String.format(
							"Player with the playerid %s sent a wrong move, that was not the move that was sent before!",
							playerid.getUniquePlayerID()));
					this.moveBefore.put(playerid, null);
					int stepsToEnter = calculateSteps(nodeGoal.getTerrain());
					int stepsToLeave = calculateSteps(nodePlayer.getTerrain());
					int stepsTotal = stepsToLeave + stepsToEnter;
					this.stepsToGo.put(playerid, stepsTotal);
					this.moveBefore.put(playerid, move);
					this.stepsToGo.put(playerid, this.stepsToGo.get(playerid) - 1);
				} else {
					this.stepsToGo.put(playerid, this.stepsToGo.get(playerid) - 1);
				}

				if (this.stepsToGo.get(playerid) == 0) {
					this.moveBefore.put(playerid, null);
					map.setNewPlayerPosition(nodeGoal.getX(), nodeGoal.getY());
					logger.info(String.format("Player with the playerid %s moved from (%d, %d) to (%d, %d)",
							playerid.getUniquePlayerID(), nodePlayer.getX(), nodePlayer.getY(), nodeGoal.getX(),
							nodeGoal.getY()));
					positionChanged = true;
					changedPosition = nodeGoal;
				}

			}
		}

		if (positionChanged) {
			for (Map map : this.maps) {
				if (!map.getPlayerid().equals(playerid)) {
					map.setNewEnemyPositionInOtherMap(changedPosition.getX(), changedPosition.getY());
				} else {
					map.checkIfInRange(changedPosition.getX(), changedPosition.getY());
					handleItem(playerid, changedPosition);
				}
			}

		}

	}

	private void handleItem(UniquePlayerIdentifier playerid, MapNode changedPosition) {
		for (Map map : this.maps) {
			if (map.getPlayerid().equals(playerid)) {
				for (MapNode node : map.getMapnodes()) {
					if (node.getX() == changedPosition.getX() && node.getY() == changedPosition.getY()) {
						if (node.getTreasureState() == TreasureState.MyTreasureIsPresent) {
							playerFoundTreasure(playerid);
							logger.info(String.format("Player with the playerid: %s found the treasure!",
									playerid.getUniquePlayerID()));
						}
						if (node.getCastleState() == CastleState.EnemyCastlePresent) {
							if (checkIfTreasureFound(playerid)) {
								playerFoundCastle(playerid);
								logger.info(String.format("Player with the playerid %s found the enemy castle!",
										playerid.getUniquePlayerID()));
							}
						}
					}
				}
			}
		}

	}

	private void playerFoundCastle(UniquePlayerIdentifier playerid) {
		for (Player player : this.players) {
			if (player.getPlayerid().equals(playerid)) {
				player.setCastleFound(true);
			}
		}
	}

	private boolean checkIfTreasureFound(UniquePlayerIdentifier playerid) {
		for (Player player : this.players) {
			if (player.getPlayerid().equals(playerid)) {
				return player.isTreasureFound();
			}
		}
		return false;
	}

	private void playerFoundTreasure(UniquePlayerIdentifier playerid) {
		for (Player player : this.players) {
			if (player.getPlayerid().equals(playerid)) {
				player.setTreasureFound(true);
			}
		}
	}

	private int calculateSteps(Terrain terrain) {
		if (terrain == Terrain.GRASS) {
			return 1;
		} else if (terrain == Terrain.MOUNTAIN) {
			return 2;
		} else {
			return 1;
		}
	}

}
