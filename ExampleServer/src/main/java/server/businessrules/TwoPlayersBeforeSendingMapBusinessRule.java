package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.TwoPlayersBeforeSendingMapException;
import server.game.Game;

public class TwoPlayersBeforeSendingMapBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkTwoPlayersBeforeSendingMapBusinessRule(games, gameid);
	}

	@Override
	public void checkGameState(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkMove(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			EMove move) {
		// TODO Auto-generated method stub
	}

	private void checkTwoPlayersBeforeSendingMapBusinessRule(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		for (int i = 0; i < games.size(); ++i) {
			if (games.get(i).getGameid().equals(gameid)) {
				if (games.get(i).getPlayers().size() < 2) {
					throw new TwoPlayersBeforeSendingMapException("TwoPlayersBeforeSendingMapException",
							"Dont send a map before the game is full!");
				}
			}
		}
	}

}
