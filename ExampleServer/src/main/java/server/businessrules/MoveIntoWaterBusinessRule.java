package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.MoveIntoWaterException;
import server.game.Game;
import server.map.Map;
import server.map.MapNode;
import server.player.Terrain;

public class MoveIntoWaterBusinessRule implements IRule {

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
		checkMoveIntoWaterBusinessRule(games, gameid, playerid, move);
	}

	private void checkMoveIntoWaterBusinessRule(ArrayList<Game> games, UniqueGameIdentifier gameid,
			UniquePlayerIdentifier playerid, EMove move) {
		for (Game game : games) {
			if (game.getGameid().equals(gameid)) {
				Map map = game.getMap(playerid).get();
				MapNode node = map.getGoalPosition(move);
				if (node.getTerrain() == Terrain.WATER) {
					throw new MoveIntoWaterException("MoveIntoWaterException", "You can not move into a water field!");
				}
			}
		}
	}

}
