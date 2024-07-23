package server.businessrules;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import server.exceptions.MapBeforeMoveException;
import server.game.Game;

class MapBeforeMoveBusinessRuleTest {

	@Test
	void sendMoveBeforeMapExists_checkRule_throwsMapBeforeMoveException() {
		ArrayList<Game> games = new ArrayList<>();
		UniqueGameIdentifier gameid = new UniqueGameIdentifier("12345");
		Game game = new Game(gameid);
		UniquePlayerIdentifier playerid1 = UniquePlayerIdentifier.random();
		UniquePlayerIdentifier playerid2 = UniquePlayerIdentifier.random();
		game.addPlayer(playerid1, "First name", "Last name", "u account");
		game.addPlayer(playerid2, "First name", "Last name", "u account");
		games.add(game);
		MapBeforeMoveBusinessRule rule = new MapBeforeMoveBusinessRule();
		Executable testCode = () -> rule.checkMove(games, gameid, playerid1, EMove.Left);
		Assertions.assertThrows(MapBeforeMoveException.class, testCode,
				"There is no fullmap yet, please dont send a move before!");
	}

}
