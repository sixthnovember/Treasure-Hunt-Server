package server.player;

import messagesbase.UniquePlayerIdentifier;

public class Player {
	private UniquePlayerIdentifier playerid;
	private String firstName;
	private String lastName;
	private String uAccount;
	private PlayerGameState playerGameState;
	private boolean halfMapSent;
	private boolean treasureFound;
	private boolean castleFound;

	public Player(UniquePlayerIdentifier playerid, String firstName, String lastName, String uAccount) {
		super();
		this.playerid = playerid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.uAccount = uAccount;
		this.playerGameState = PlayerGameState.MUSTWAIT;
		this.halfMapSent = false;
		this.treasureFound = false;
		this.castleFound = false;
	}

	public UniquePlayerIdentifier getPlayerid() {
		return playerid;
	}

	public void setPlayerGameState(PlayerGameState playerGameState) {
		this.playerGameState = playerGameState;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getuAccount() {
		return uAccount;
	}

	public PlayerGameState getPlayerGameState() {
		return playerGameState;
	}

	public boolean isHalfMapSent() {
		return halfMapSent;
	}

	public void setHalfMapSent(boolean halfMapSent) {
		this.halfMapSent = halfMapSent;
	}

	public boolean isTreasureFound() {
		return treasureFound;
	}

	public void setTreasureFound(boolean treasureFound) {
		this.treasureFound = treasureFound;
	}

	public boolean isCastleFound() {
		return castleFound;
	}

	public void setCastleFound(boolean castleFound) {
		this.castleFound = castleFound;
	}

}
