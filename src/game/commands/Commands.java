package game.commands;

import game.map.Directions;

public enum Commands {

	W("w", Directions.NORTH), S("s", Directions.SOUTH), D("d", Directions.EAST), A("a", Directions.WEST), I("i",
			Directions.NORTH), K("k", Directions.SOUTH), L("l",
					Directions.EAST), J("j", Directions.WEST), SKIP("skip", null), END("end", null);

	String key;
	Directions keyDirection;

	public static Commands getCommand(String input) {
		for (Commands command : Commands.values()) {
			if (command.key.equals(input)) {
				return command;
			}
		}
		return null;
	}

	Commands(String keyName, Directions direction) {
		this.key = keyName;
		this.keyDirection = direction;
	}

	public String getUserInput() {
		return this.key;
	}

	public Directions getDirection() {
		return this.keyDirection;
	}

}
