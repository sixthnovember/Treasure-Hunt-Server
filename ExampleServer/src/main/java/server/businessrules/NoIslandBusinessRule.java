package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.IslandException;
import server.game.Game;

public class NoIslandBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkNoIslandBusinessRule(halfmap);
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

	private void checkNoIslandBusinessRule(PlayerHalfMap halfmap) {

		ETerrain[][] halfmapToCheck = new ETerrain[BusinessRules.xAxisMax + 1][BusinessRules.yAxisMax + 1];
		boolean[][] visited = new boolean[BusinessRules.xAxisMax + 1][BusinessRules.yAxisMax + 1];

		for (PlayerHalfMapNode node : halfmap.getMapNodes()) {
			halfmapToCheck[node.getX()][node.getY()] = node.getTerrain();
		}

		boolean validStartFound = false;

		for (int x = BusinessRules.xAxisMin; x < BusinessRules.xAxisMax + 1 && !validStartFound; ++x) {
			for (int y = BusinessRules.yAxisMin; y < BusinessRules.yAxisMax + 1 && !validStartFound; ++y) {
				if (halfmapToCheck[x][y] != ETerrain.Water) {
					floodFillAlgorithm(halfmapToCheck, visited, x, y);
					validStartFound = true;
				}
			}
		}

		for (int x = BusinessRules.xAxisMin; x < BusinessRules.xAxisMax + 1; ++x) {
			for (int y = BusinessRules.yAxisMin; y < BusinessRules.yAxisMax + 1; ++y) {
				if (halfmapToCheck[x][y] != ETerrain.Water) {
					if (!visited[x][y]) {
						throw new IslandException("IslandException",
								"The following field could not be visited: (" + x + ", " + y + ")");
					}
				}
			}
		}

	}

	// TAKEN FROM START
	// https://www.sanfoundry.com/java-program-flood-fill-algorithm/
	// took the idea of the implementation of the flood fill algorithm
	private void floodFillAlgorithm(ETerrain[][] halfmap, boolean[][] visited, int x, int y) {

		if (x < BusinessRules.xAxisMin || x > BusinessRules.xAxisMax || y < BusinessRules.yAxisMin
				|| y > BusinessRules.yAxisMax) {
			return;
		}

		if (halfmap[x][y] == ETerrain.Water) {
			return;
		}

		if (visited[x][y]) {
			return;
		}

		visited[x][y] = true;

		floodFillAlgorithm(halfmap, visited, x + 1, y);
		floodFillAlgorithm(halfmap, visited, x - 1, y);
		floodFillAlgorithm(halfmap, visited, x, y + 1);
		floodFillAlgorithm(halfmap, visited, x, y - 1);
	}
	// TAKEN FROM END https://www.sanfoundry.com/java-program-flood-fill-algorithm/

}
