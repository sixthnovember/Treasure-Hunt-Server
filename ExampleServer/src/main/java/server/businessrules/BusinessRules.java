package server.businessrules;

import java.util.ArrayList;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.game.Game;

public class BusinessRules {

	public static final int waterMinFields = 7;
	public static final int mountainMinFields = 5;
	public static final int grassMinFields = 24;
	public static final int amountOfCastlesAllowed = 1;
	public static final int playerHalfMapSize = 50;
	public static final int xDimension = 10;
	public static final int yDimension = 5;
	public static final int xAxisMin = 0;
	public static final int yAxisMin = 0;
	public static final int xAxisMax = 9;
	public static final int yAxisMax = 4;
	public static final int waterLimitxAxisMin = 4;
	public static final int waterLimityAxisMin = 2;
	public static final int waterLimitxAxisMax = 4;
	public static final int waterLimityAxisMax = 2;

	private static ArrayList<IRule> rules;

	static {
		rules = new ArrayList<IRule>();
		rules.add(new GameIDValidBusinessRule());
		rules.add(new MaxTwoPlayersBusinessRule());
		rules.add(new PlayerPartOfGameBusinessRule());
		rules.add(new TwoPlayersBeforeSendingMapBusinessRule());
		rules.add(new SendHalfMapOnceBusinessRule());
		rules.add(new MapSizeBusinessRule());
		rules.add(new HalfMapDimensionBusinessRule());
		rules.add(new ValidPointBusinessRule());
		rules.add(new TerrainCountBusinessRule());
		rules.add(new CastleCountBusinessRule());
		rules.add(new CastleOnGrassBusinessRule());
		rules.add(new WaterOnBorderBusinessRule());
		rules.add(new NoIslandBusinessRule());
		rules.add(new MapBeforeMoveBusinessRule());
		rules.add(new MoveOutOfMapBusinessRule());
		rules.add(new MoveIntoWaterBusinessRule());
	}

	public static void checkAddPlayerBussinesRules(ArrayList<Game> games, UniqueGameIdentifier gameid) {
		for (IRule iRule : rules) {
			iRule.checkNewPlayer(games, gameid);
		}
	}

	public static void checkPlayerHalfMapBussinesRules(ArrayList<Game> games, UniqueGameIdentifier gameid,
			UniquePlayerIdentifier playerid, PlayerHalfMap halfmap) {
		for (IRule iRule : rules) {
			iRule.checkHalfMap(games, gameid, playerid, halfmap);
		}
	}

	public static void checkGameStateBussinesRules(ArrayList<Game> games, UniqueGameIdentifier gameid,
			UniquePlayerIdentifier playerid) {
		for (IRule iRule : rules) {
			iRule.checkGameState(games, gameid, playerid);
		}
	}

	public static void checkMoveBussinesRules(ArrayList<Game> games, UniqueGameIdentifier gameid,
			UniquePlayerIdentifier playerid, EMove move) {
		for (IRule iRule : rules) {
			iRule.checkMove(games, gameid, playerid, move);
		}
	}

}