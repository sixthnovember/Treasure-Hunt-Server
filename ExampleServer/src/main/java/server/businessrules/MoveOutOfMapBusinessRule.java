package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.GoalPositionNotFoundExcception;
import server.exceptions.MoveOutOfMapException;
import server.game.Game;
import server.map.Map;
import server.map.MapNode;

public class MoveOutOfMapBusinessRule implements IRule {

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
		checkMoveOutOfMapBusinessRule(games, gameid, playerid, move);
	}

	private void checkMoveOutOfMapBusinessRule(ArrayList<Game> games, UniqueGameIdentifier gameid,
			UniquePlayerIdentifier playerid, EMove move) {
		for (Game game : games) {
			if (game.getGameid().equals(gameid)) {
				Map map = game.getMap(playerid).get();
				try {
					MapNode node = map.getGoalPosition(move);
				} catch (GoalPositionNotFoundExcception e) {
					throw new MoveOutOfMapException("MoveOutOfMapException", "You can not move out of the map!");
				}
			}
		}
	}

}
