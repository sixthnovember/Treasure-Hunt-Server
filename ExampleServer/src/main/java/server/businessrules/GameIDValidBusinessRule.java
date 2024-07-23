package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.GameIDNotFoundException;
import server.game.Game;

public class GameIDValidBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		checkGameIDValidBusinessRule(games, gameid);
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkGameIDValidBusinessRule(games, gameid);
	}

	@Override
	public void checkGameState(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid) {
		checkGameIDValidBusinessRule(games, gameid);
	}

	@Override
	public void checkMove(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			EMove move) {
		checkGameIDValidBusinessRule(games, gameid);
	}

	private void checkGameIDValidBusinessRule(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		for (int i = 0; i < games.size(); ++i) {
			if (games.get(i).getGameid().equals(gameid)) {
				return;
			}
		}
		throw new GameIDNotFoundException("GameIDNoFoundException",
				"There is active game with the given game id: " + gameid);
	}

}
