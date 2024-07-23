package server.businessrules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.MapSizeException;
import server.game.Game;

public class HalfMapDimensionBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkHalfMapDimensionBusinessRule(halfmap);
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

	private void checkHalfMapDimensionBusinessRule(PlayerHalfMap halfmap) {

		Set<Integer> xDim = new HashSet<>();
		Set<Integer> yDim = new HashSet<>();

		for (PlayerHalfMapNode node : halfmap.getMapNodes()) {
			xDim.add(node.getX());
			yDim.add(node.getY());
		}

		if (xDim.size() != BusinessRules.xDimension) {
			throw new MapSizeException("MapSizeException", "The dimension is wrong on the x axis!");
		}

		if (yDim.size() != BusinessRules.yDimension) {
			throw new MapSizeException("MapSizeException", "The dimension is wrong on the y axis!");
		}

	}

}
