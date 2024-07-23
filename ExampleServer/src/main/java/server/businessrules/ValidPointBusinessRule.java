package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.InvalidPointException;
import server.game.Game;

public class ValidPointBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkValidPointBusinessRule(halfmap);
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

	private void checkValidPointBusinessRule(PlayerHalfMap halfmap) {
		for (PlayerHalfMapNode node : halfmap.getMapNodes()) {
			if (node.getX() > BusinessRules.xAxisMax) {
				throw new InvalidPointException("InvalidPointException",
						"There is a point with an invalid (larger) x coordinate!");
			}
			if (node.getY() > BusinessRules.yAxisMax) {
				throw new InvalidPointException("InvalidPointException",
						"There is a point with an invalid (larger) y coordinate!");
			}
			if (node.getX() < BusinessRules.xAxisMin) {
				throw new InvalidPointException("InvalidPointException",
						"There is a point with an invalid (smaller) x coordinate!");
			}
			if (node.getY() < BusinessRules.yAxisMin) {
				throw new InvalidPointException("InvalidPointException",
						"There is a point with an invalid (smaller) y coordinate!");
			}
		}
	}

}
