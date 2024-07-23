package server.game;

import java.util.ArrayList;
import java.util.Random;

import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;
import server.converter.MapToFullMapConverter;
import server.player.Player;
import server.player.PlayerGameState;

public class GameStateCreator {

	private final static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private final static int gameStateIDLength = 5;
	private Game game;
	private UniquePlayerIdentifier playerid;

	public GameStateCreator(Game game, UniquePlayerIdentifier playerid) {
		this.game = game;
		this.playerid = playerid;
	}

	public GameState receiveGameState(boolean gameStateChanged) {

		ArrayList<PlayerState> playerstates = new ArrayList<>();

		for (Player player : this.game.getPlayers()) {
			if (player.getPlayerid().equals(this.playerid)) {
				PlayerState playerstate = new PlayerState(player.getFirstName(), player.getLastName(),
						player.getuAccount(), convertToEPlayerGameState(player.getPlayerGameState()),
						player.getPlayerid(), player.isTreasureFound());
				playerstates.add(playerstate);
			} else {
				PlayerState playerstate = new PlayerState(player.getFirstName(), player.getLastName(),
						player.getuAccount(), convertToEPlayerGameState(player.getPlayerGameState()),
						UniquePlayerIdentifier.random(), player.isTreasureFound());
				playerstates.add(playerstate);
			}
		}

		if (gameStateChanged) {
			game.setGamestateid(createGameStateID());
		} else {
			if (game.getGamestateid() == null) {
				game.setGamestateid(createGameStateID());
			}
		}

		String gamestateid = game.getGamestateid();

		if (!game.getMap(playerid).isPresent()) {
			return new GameState(playerstates, gamestateid);
		}

		if (game.getHalfMaps().isEmpty()) {
			return new GameState(playerstates, gamestateid);
		}

		boolean hideTrueEnemyPosition = false;

		if (game.getRoundCounter() < 15) {
			hideTrueEnemyPosition = true;
		}

		boolean showTreasure = game.getMap(playerid).get().isShowTreasure();
		boolean showEnemyCastle = game.getMap(playerid).get().isShowEnemyCastle();
		boolean treasureFound = game.getPlayer(playerid).isTreasureFound();
		MapToFullMapConverter converter = new MapToFullMapConverter(game.getMap(playerid).get(), hideTrueEnemyPosition,
				showTreasure, showEnemyCastle, treasureFound);
		FullMap fullmap = converter.convert();

		return new GameState(fullmap, playerstates, gamestateid);

	}

	private EPlayerGameState convertToEPlayerGameState(PlayerGameState playerGameState) {
		if (playerGameState == PlayerGameState.MUSTWAIT) {
			return EPlayerGameState.MustWait;
		} else if (playerGameState == PlayerGameState.MUSTACT) {
			return EPlayerGameState.MustAct;
		} else if (playerGameState == PlayerGameState.WON) {
			return EPlayerGameState.Won;
		} else {
			return EPlayerGameState.Lost;
		}
	}

	private String createGameStateID() {
		Random random = new Random();
		String id = "";
		while (id.length() < gameStateIDLength) {
			id += alphabet.charAt(random.nextInt(alphabet.length()));
		}
		return id;
	}

}