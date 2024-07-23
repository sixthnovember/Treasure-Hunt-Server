package server.businessrules;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import server.exceptions.MaxTwoPlayersException;
import server.game.Game;

class MaxTwoPlayersBusinessRuleTest {

	@Test
	void createGameWithTwoPlayers_checkRule_throwsMaxTwoPlayersException() {
		ArrayList<Game> games = new ArrayList<>();
		Game game = new Game(new UniqueGameIdentifier("12345"));
		game.addPlayer(UniquePlayerIdentifier.random(), "First name", "Last name", "u account");
		game.addPlayer(UniquePlayerIdentifier.random(), "First name", "Last name", "u account");
		games.add(game);
		MaxTwoPlayersBusinessRule rule = new MaxTwoPlayersBusinessRule();
		Executable testCode = () -> rule.checkNewPlayer(games, game.getGameid());
		Assertions.assertThrows(MaxTwoPlayersException.class, testCode,
				"We expected a exception because there are already two players in this game.");
	}

}
