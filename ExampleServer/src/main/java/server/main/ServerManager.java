package server.main;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromserver.GameState;
import server.businessrules.BusinessRules;
import server.converter.PlayerHalfMapToHalfMapConverter;
import server.exceptions.GameNotFoundException;
import server.game.Game;
import server.game.GameCreator;
import server.game.GameStateCreator;
import server.player.HalfMap;
import server.player.Player;
import server.player.PlayerGameState;

public class ServerManager {

	private ArrayList<Game> activeGames;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ServerManager.class);

	public ServerManager() {
		this.activeGames = new ArrayList<Game>();
	}

	public UniqueGameIdentifier addGame() {
		if (this.activeGames.size() == 99) {
			deleteOldestGame();
		}
		GameCreator gameCreator = new GameCreator();
		UniqueGameIdentifier gameid = gameCreator.generateID();
		this.activeGames.add(new Game(gameid));
		logger.info(String.format("The game with the gameid %s has been created!", gameid.getUniqueGameID()));
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(() -> {
			deleteGame(gameid);
		}, 10, TimeUnit.MINUTES);
		return gameid;
	}

	private void deleteGame(UniqueGameIdentifier gameid) {
		int gameindex = findGame(gameid);
		this.activeGames.remove(gameindex);
		logger.info(String.format("The game with the gameid %s has been deleted!", gameid.getUniqueGameID()));
	}

	private void deleteOldestGame() {
		deleteGame(this.activeGames.get(0).getGameid());
		logger.warn("Storage is full we have to delete the oldest game to add the new one!");
	}

	private int findGame(UniqueGameIdentifier gameid) {
		for (int i = 0; i < this.activeGames.size(); ++i) {
			if (this.activeGames.get(i).getGameid().equals(gameid)) {
				return i;
			}
		}
		throw new GameNotFoundException("GameNotFoundException", "The game is not in the list of active games!");
	}

	public void addPlayerToGame(UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid, String firstName,
			String lastName, String uAccount) {
		BusinessRules.checkAddPlayerBussinesRules(this.activeGames, gameid);
		int gameindex = findGame(gameid);
		this.activeGames.get(gameindex).addPlayer(playerid, firstName, lastName, uAccount);
		logger.info(String.format(
				"Player with the playerid %s has been registered to the game with the following gameid: %s",
				playerid.getUniquePlayerID(), gameid.getUniqueGameID()));
	}

	public GameState validatePlayerHalfMap(UniqueGameIdentifier gameid, PlayerHalfMap playerHalfmap) {
		BusinessRules.checkPlayerHalfMapBussinesRules(this.activeGames, gameid,
				new UniquePlayerIdentifier(playerHalfmap.getUniquePlayerID()), playerHalfmap);
		PlayerHalfMapToHalfMapConverter converter = new PlayerHalfMapToHalfMapConverter(playerHalfmap);
		HalfMap halfmap = converter.convert();
		int gameindex = findGame(gameid);
		this.activeGames.get(gameindex).addHalfMap(halfmap);
		playerSentMap(gameid, new UniquePlayerIdentifier(playerHalfmap.getUniquePlayerID()));
		Game game = this.activeGames.get(gameindex);
		GameStateCreator creator = new GameStateCreator(game,
				new UniquePlayerIdentifier(playerHalfmap.getUniquePlayerID()));
		GameState state = creator.receiveGameState(true);
		return state;
	}

	public void playerSentMap(UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid) {
		int gameindex = findGame(gameid);
		this.activeGames.get(gameindex).playerSentMap(new UniquePlayerIdentifier(playerid));
		logger.info(String.format("Player with the playerid %s has sent a halfmap!", playerid.getUniquePlayerID()));
	}

	public GameState getGameState(UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid) {
		BusinessRules.checkGameStateBussinesRules(this.activeGames, gameid, playerid);
		int gameindex = findGame(gameid);
		Game game = this.activeGames.get(gameindex);
		GameStateCreator creator = new GameStateCreator(game, playerid);
		GameState state = creator.receiveGameState(false);
		return state;
	}

	public ResponseEnvelope<GameState> endGame(UniqueGameIdentifier gameid,
			UniquePlayerIdentifier uniquePlayerIdentifier) {
		int gameindex = findGame(gameid);
		for (Player player : this.activeGames.get(gameindex).getPlayers()) {
			if (player.getPlayerid().equals(uniquePlayerIdentifier)) {
				player.setPlayerGameState(PlayerGameState.LOST);
			} else {
				player.setPlayerGameState(PlayerGameState.WON);
			}
		}
		GameStateCreator creator = new GameStateCreator(this.activeGames.get(gameindex),
				new UniquePlayerIdentifier(uniquePlayerIdentifier));
		GameState gamestate = creator.receiveGameState(true);
		this.activeGames.get(gameindex).setGamestateid(gamestate.getGameStateId());
		logger.info(String.format("The game with the gameid %s is over!", gameid.getUniqueGameID()));
		return new ResponseEnvelope<GameState>(gamestate);
	}

	public GameState validateMove(UniqueGameIdentifier gameid, PlayerMove playerMove) {
		BusinessRules.checkMoveBussinesRules(this.activeGames, gameid,
				new UniquePlayerIdentifier(playerMove.getUniquePlayerID()), playerMove.getMove());
		int gameindex = findGame(gameid);
		updateMap(gameindex, playerMove);
		logger.info(String.format("Player with the playerid %s moved %s!", playerMove.getUniquePlayerID(),
				playerMove.getMove().name()));
		handleState(gameindex, new UniquePlayerIdentifier(playerMove.getUniquePlayerID()));
		Game game = this.activeGames.get(gameindex);
		GameStateCreator creator = new GameStateCreator(game,
				new UniquePlayerIdentifier(playerMove.getUniquePlayerID()));
		GameState state = creator.receiveGameState(true);
		this.activeGames.get(gameindex).increaseRoundCounter();
		return state;
	}

	private void updateMap(int gameindex, PlayerMove playerMove) {
		this.activeGames.get(gameindex).setNewPlayerPosition(new UniquePlayerIdentifier(playerMove.getUniquePlayerID()),
				playerMove.getMove());
	}

	private void handleState(int gameindex, UniquePlayerIdentifier playerid) {
		boolean castleFound = false;
		for (Player player : this.activeGames.get(gameindex).getPlayers()) {
			if (player.getPlayerid().equals(playerid)) {
				if (player.isCastleFound()) {
					castleFound = true;
					logger.info(String.format("Player with the playerid %s has won the game!",
							player.getPlayerid().getUniquePlayerID()));
					break;
				}
			}
		}
		if (castleFound) {
			for (Player player : this.activeGames.get(gameindex).getPlayers()) {
				if (!player.getPlayerid().equals(playerid)) {
					endGame(this.activeGames.get(gameindex).getGameid(), playerid);
				}
			}
		} else {
			for (Player player : this.activeGames.get(gameindex).getPlayers()) {
				if (player.getPlayerid().equals(playerid)) {
					player.setPlayerGameState(PlayerGameState.MUSTWAIT);
				} else {
					player.setPlayerGameState(PlayerGameState.MUSTACT);
				}
			}
		}
	}

}
