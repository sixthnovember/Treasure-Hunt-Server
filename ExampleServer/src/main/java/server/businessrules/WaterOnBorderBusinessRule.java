package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.WaterOnBorderException;
import server.game.Game;

public class WaterOnBorderBusinessRule implements IRule {

	@Override
	public void checkNewPlayer(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkHalfMap(ArrayList<Game> games, UniqueGameIdentifier gameid, UniquePlayerIdentifier playerid,
			PlayerHalfMap halfmap) {
		checkWaterOnBorderBusinessRule(halfmap);
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

	private void checkWaterOnBorderBusinessRule(PlayerHalfMap halfmap) {

		ETerrain[][] terrainHhalfmap = new ETerrain[BusinessRules.xAxisMax + 1][BusinessRules.yAxisMax + 1];

		for (PlayerHalfMapNode node : halfmap.getMapNodes()) {
			terrainHhalfmap[node.getX()][node.getY()] = node.getTerrain();
		}

		int waterMinX = 0;
		int waterMinY = 0;
		int waterMaxX = 0;
		int waterMaxY = 0;

		for (int x = BusinessRules.xAxisMin; x < BusinessRules.xAxisMax + 1; ++x) {
			for (int y = BusinessRules.yAxisMin; y < BusinessRules.yAxisMax + 1; ++y) {
				if (terrainHhalfmap[x][y] == ETerrain.Water) {
					if (x == BusinessRules.xAxisMin) {
						waterMinY += 1;
					}
					if (x == BusinessRules.xAxisMax) {
						waterMaxY += 1;
					}
					if (y == BusinessRules.yAxisMin) {
						waterMinX += 1;
					}
					if (y == BusinessRules.yAxisMax) {
						waterMaxX += 1;
					}
				}
			}
		}

		if (waterMinX > BusinessRules.waterLimitxAxisMin) {
			throw new WaterOnBorderException("WaterOnBorderException", "There is too much water on the x min axis!");
		}

		if (waterMaxX > BusinessRules.waterLimitxAxisMax) {
			throw new WaterOnBorderException("WaterOnBorderException", "There is too much water on the x max axis!");
		}

		if (waterMinY > BusinessRules.waterLimityAxisMin) {
			throw new WaterOnBorderException("WaterOnBorderException", "There is too much water on the y min axis!");
		}

		if (waterMaxY > BusinessRules.waterLimityAxisMax) {
			throw new WaterOnBorderException("WaterOnBorderException", "There is too much water on the y max axis!");
		}

	}

}
