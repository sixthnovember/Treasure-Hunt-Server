package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.MapSizeException;
import server.game.Game;

public class MapSizeBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkMapSizeBusinessRule(halfmap);
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

	private void checkMapSizeBusinessRule(PlayerHalfMap halfmap) {
		if (halfmap.getMapNodes().size() < BusinessRules.playerHalfMapSize) {
			throw new MapSizeException("MapSizeException", "The map is too small!");
		}
		if (halfmap.getMapNodes().size() > BusinessRules.playerHalfMapSize) {
			throw new MapSizeException("MapSizeException", "The map is too large!");
		}
	}

}
