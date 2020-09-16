package game.map;

public enum MapObjects {

	EMPTY("Empty"), BATS("Bats"), PIT("Pit"), WUMPUS("Wumpus"), ARROW("Arrow"), PLAYER("Player");

	String objectName;

	MapObjects(String name) {
		this.objectName = name;
	}

	public String getObjectName() {
		return this.objectName;
	}

}
