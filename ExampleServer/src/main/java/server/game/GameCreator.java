package server.game;

import java.util.Random;

import messagesbase.UniqueGameIdentifier;

public class GameCreator {

	private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private final int gameidLength = 5;

	public UniqueGameIdentifier generateID() {
		Random random = new Random();
		String id = "";

		while (id.length() < this.gameidLength) {
			id += this.alphabet.charAt(random.nextInt(this.alphabet.length()));
		}

		return new UniqueGameIdentifier(id);
	}
}
