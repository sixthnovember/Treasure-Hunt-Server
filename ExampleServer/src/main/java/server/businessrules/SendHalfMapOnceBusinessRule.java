package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.SendHalfMapTwiceException;
import server.game.Game;

public class SendHalfMapOnceBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkSendHalfMapOnceBusinessRule(games, gameid, playerid);
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

	private void checkSendHalfMapOnceBusinessRule(ArrayList<Game> games, UniqueGameIdentifier gameid,
			UniquePlayerIdentifier playerid) {
		for (int i = 0; i < games.size(); ++i) {
			if (games.get(i).getGameid().equals(gameid)) {
				for (int j = 0; j < games.get(i).getPlayers().size(); ++j) {
					if (games.get(i).getPlayers().get(j).getPlayerid().equals(playerid)) {
						if (games.get(i).getPlayers().get(j).isHalfMapSent()) {
							throw new SendHalfMapTwiceException("SendHalfMapTwiceException",
									"Player has already sent a map!");
						}
					}
				}
			}
		}
	}

}
