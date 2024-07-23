package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.PlayerNotPartOfGameException;
import server.game.Game;

public class PlayerPartOfGameBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkPlayerPartOfGameBusinessRule(games, gameid, playerid);
	}

	@Override
	public void checkGameState(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid) {
		checkPlayerPartOfGameBusinessRule(games, gameid, playerid);
	}

	@Override
	public void checkMove(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			EMove move) {
		checkPlayerPartOfGameBusinessRule(games, gameid, playerid);
	}

	public void checkPlayerPartOfGameBusinessRule(ArrayList<Game> games, UniqueGameIdentifier gameid,
			UniquePlayerIdentifier playerid) {
		for (int i = 0; i < games.size(); ++i) {
			if (games.get(i).getGameid().equals(gameid)) {
				for (int j = 0; j < games.get(i).getPlayers().size(); ++j) {
					if (games.get(i).getPlayers().get(j).getPlayerid().equals(playerid)) {
						return;
					}
				}
				throw new PlayerNotPartOfGameException("PlayerNotPartOfGameException",
						"The player is not part of this game!");
			}
		}
	}

}
