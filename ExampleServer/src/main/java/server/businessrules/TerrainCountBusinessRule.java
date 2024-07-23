package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.TerrainCountException;
import server.game.Game;

public class TerrainCountBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkTerrainCountBusinessRule(halfmap);
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

	public void checkTerrainCountBusinessRule(PlayerHalfMap halfmap) {
		int grassCounter = 0;
		int mountainCounter = 0;
		int waterCounter = 0;

		for (PlayerHalfMapNode node : halfmap.getMapNodes()) {
			if (node.getTerrain() == ETerrain.Grass) {
				grassCounter += 1;
			} else if (node.getTerrain() == ETerrain.Mountain) {
				mountainCounter += 1;
			} else {
				waterCounter += 1;
			}
		}

		if (grassCounter < BusinessRules.grassMinFields) {
			throw new TerrainCountException("TerrainCountException", "There are not enough grass fields!");
		}

		if (mountainCounter < BusinessRules.mountainMinFields) {
			throw new TerrainCountException("TerrainCountException", "There are not enough mountain fields!");
		}

		if (waterCounter < BusinessRules.waterMinFields) {
			throw new TerrainCountException("TerrainCountException", "There are not enough water fields!");
		}

	}

}
