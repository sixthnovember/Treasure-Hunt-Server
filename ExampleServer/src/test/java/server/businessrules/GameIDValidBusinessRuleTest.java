package server.businessrules;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import messagesbase.UniqueGameIdentifier;
import server.exceptions.GameIDNotFoundException;
import server.game.Game;

class GameIDValidBusinessRuleTest {

	@Test
	void CreateEmptyGamesList_checkRule_throwsGameIDNotFoundException() {
		ArrayList<Game> games = new ArrayList<>();
		UniqueGameIdentifier gameid = new UniqueGameIdentifier("12345");
		GameIDValidBusinessRule rule = new GameIDValidBusinessRule();
		Executable testCode = () -> rule.checkNewPlayer(games, gameid);
		Assertions.assertThrows(GameIDNotFoundException.class, testCode,
				"We expected a exception because the game id is not in the list of games.");
	}

}
