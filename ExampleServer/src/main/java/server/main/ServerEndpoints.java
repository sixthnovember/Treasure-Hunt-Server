package server.main;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import server.exceptions.GameStateException;
import server.exceptions.GenericExampleException;
import server.exceptions.HalfMapException;
import server.exceptions.MoveException;
import server.exceptions.PlayerNotPartOfGameException;
import server.exceptions.PlayerRegistrationException;

@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {

	private ServerManager manager;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ServerEndpoints.class);

	public ServerEndpoints() {
		this.manager = new ServerManager();
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {
		UniqueGameIdentifier gameIdentifier = this.manager.addGame();
		return gameIdentifier;
	}

	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {
		try {
			UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
			this.manager.addPlayerToGame(gameID, newPlayerID, playerRegistration.getStudentFirstName(),
					playerRegistration.getStudentLastName(), playerRegistration.getStudentUAccount());
			ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);
			return playerIDMessage;
		} catch (GenericExampleException e) {
			logger.error(e.getMessage());
			throw new PlayerRegistrationException(e.getErrorName(), e.getMessage());
		}
	}

	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> receiveHalfMap(@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerHalfMap halfmap) {
		try {
			GameState state = (GameState) this.manager.validatePlayerHalfMap(gameID, halfmap);
			logger.info(String.format("Player with the playerid %s has sent a correct halfmap!",
					halfmap.getUniquePlayerID()));
			return new ResponseEnvelope<>(state);
		} catch (PlayerNotPartOfGameException e) {
			logger.error(e.getMessage());
			this.manager.playerSentMap(gameID, new UniquePlayerIdentifier(halfmap.getUniquePlayerID()));
			GameState state = this.manager.getGameState(gameID,
					new UniquePlayerIdentifier(halfmap.getUniquePlayerID()));
			return new ResponseEnvelope<>(state);
		} catch (GenericExampleException e) {
			logger.error(e.getMessage());
			this.manager.playerSentMap(gameID, new UniquePlayerIdentifier(halfmap.getUniquePlayerID()));
			this.manager.endGame(gameID, new UniquePlayerIdentifier(halfmap.getUniquePlayerID()));
			throw new HalfMapException(e.getErrorName(), e.getMessage());
		}
	}

	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> getGameState(@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @PathVariable UniquePlayerIdentifier playerID) {
		try {
			GameState state = this.manager.getGameState(gameID, playerID);
			return new ResponseEnvelope<>(state);
		} catch (GenericExampleException e) {
			logger.error(e.getMessage());
			throw new GameStateException(e.getErrorName(), e.getMessage());
		}
	}

	@RequestMapping(value = "/{gameID}/moves", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> receiveMove(@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerMove playerMove) {
		try {
			GameState state = (GameState) this.manager.validateMove(gameID, playerMove);
			logger.info(String.format("Player with the playerid %s has send a correct move!",
					playerMove.getUniquePlayerID()));
			return new ResponseEnvelope<>(state);
		} catch (GenericExampleException e) {
			logger.error(e.getMessage());
			this.manager.endGame(gameID, new UniquePlayerIdentifier(playerMove.getUniquePlayerID()));
			throw new MoveException(e.getErrorName(), e.getMessage());
		}
	}

	@ExceptionHandler({ GenericExampleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}
