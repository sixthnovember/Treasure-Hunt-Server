package server.businessrules;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.TerrainCountException;
import server.game.Game;

class TerrainCountBusinessRuleTest {

	@Test
	void createHalfMapWithNotEnoughGrass_checkRule_throwsTerrainCountException() {

		ArrayList<PlayerHalfMapNode> halfMapNodes = new ArrayList<>();

		// Grass = 23 (should be 24), Water = 7, Mountain = 5
		halfMapNodes.add(new PlayerHalfMapNode(0, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 3, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(4, 4, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 0, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 1, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 2, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 3, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 4, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(6, 0, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 1, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 2, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 3, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 4, ETerrain.Mountain));

		UniquePlayerIdentifier playerid = UniquePlayerIdentifier.random();
		PlayerHalfMap playerHalfMap = new PlayerHalfMap(playerid, halfMapNodes);
		ArrayList<Game> games = new ArrayList<>();
		UniqueGameIdentifier gameid = new UniqueGameIdentifier("12345");
		games.add(new Game(gameid));

		TerrainCountBusinessRule rule = new TerrainCountBusinessRule();
		Executable testCode = () -> rule.checkHalfMap(games, gameid, playerid, playerHalfMap);
		Assertions.assertThrows(TerrainCountException.class, testCode,
				"There are  not enough grass fields on the map!");
	}

	@Test
	void createHalfMapWithNotEnoughMountains_checkRule_throwsTerrainCountException() {

		ArrayList<PlayerHalfMapNode> halfMapNodes = new ArrayList<>();

		// Grass = 24, Water = 7, Mountain = 4 (should be 5)
		halfMapNodes.add(new PlayerHalfMapNode(0, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 3, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(4, 4, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 0, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 1, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 2, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 3, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 4, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(6, 0, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 1, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 2, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 3, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 4, ETerrain.Grass));

		UniquePlayerIdentifier playerid = UniquePlayerIdentifier.random();
		PlayerHalfMap playerHalfMap = new PlayerHalfMap(playerid, halfMapNodes);
		ArrayList<Game> games = new ArrayList<>();
		UniqueGameIdentifier gameid = new UniqueGameIdentifier("12345");
		games.add(new Game(gameid));

		TerrainCountBusinessRule rule = new TerrainCountBusinessRule();
		Executable testCode = () -> rule.checkHalfMap(games, gameid, playerid, playerHalfMap);
		Assertions.assertThrows(TerrainCountException.class, testCode,
				"There are  not enough mountain fields on the map!");
	}

	@Test
	void createHalfMapWithNotEnoughWater_checkRule_throwsTerrainCountException() {

		ArrayList<PlayerHalfMapNode> halfMapNodes = new ArrayList<>();

		// Grass = 24, Water = 6 (should be 7), Mountain = 5
		halfMapNodes.add(new PlayerHalfMapNode(0, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(0, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(1, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(2, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(3, 4, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 0, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 1, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 2, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 3, ETerrain.Grass));
		halfMapNodes.add(new PlayerHalfMapNode(4, 4, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 0, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 1, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 2, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 3, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(5, 4, ETerrain.Water));
		halfMapNodes.add(new PlayerHalfMapNode(6, 0, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 1, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 2, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 3, ETerrain.Mountain));
		halfMapNodes.add(new PlayerHalfMapNode(6, 4, ETerrain.Mountain));

		UniquePlayerIdentifier playerid = UniquePlayerIdentifier.random();
		PlayerHalfMap playerHalfMap = new PlayerHalfMap(playerid, halfMapNodes);
		ArrayList<Game> games = new ArrayList<>();
		UniqueGameIdentifier gameid = new UniqueGameIdentifier("12345");
		games.add(new Game(gameid));

		TerrainCountBusinessRule rule = new TerrainCountBusinessRule();
		Executable testCode = () -> rule.checkHalfMap(games, gameid, playerid, playerHalfMap);
		Assertions.assertThrows(TerrainCountException.class, testCode,
				"There are  not enough water fields on the map!");
	}

}
