package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.CastleCountException;
import server.game.Game;

public class CastleCountBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkCastleCountBusinessRule(halfmap);
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

	private void checkCastleCountBusinessRule(PlayerHalfMap halfmap) {

		int castleCounter = 0;

		for (PlayerHalfMapNode node : halfmap.getMapNodes()) {
			if (node.getTerrain() == ETerrain.Grass) {
				if (node.isFortPresent()) {
					castleCounter += 1;
				}
			}
		}

		if (castleCounter < BusinessRules.amountOfCastlesAllowed) {
			throw new CastleCountException("CastleCountException", "There are not enough castles on the map!");
		}

		if (castleCounter > BusinessRules.amountOfCastlesAllowed) {
			throw new CastleCountException("CastleCountException", "There are too many castles on the map!");
		}

	}

}
