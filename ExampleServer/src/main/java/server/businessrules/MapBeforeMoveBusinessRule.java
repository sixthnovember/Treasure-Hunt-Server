package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.MapBeforeMoveException;
import server.game.Game;

public class MapBeforeMoveBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkGameState(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkMove(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			EMove move) {
		checkMapBeforeMoveBusinessRule(games, gameid);
	}

	private void checkMapBeforeMoveBusinessRule(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		for (Game game : games) {
			if (game.getGameid().equals(gameid)) {
				if (game.getMaps().isEmpty()) {
					throw new MapBeforeMoveException("MapBeforeMoveException",
							"There is no fullmap yet, please dont send a move before!");
				}
			}

		}
	}

}
