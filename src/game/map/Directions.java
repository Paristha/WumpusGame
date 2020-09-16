package game.map;

public enum Directions {
	NORTH(0, 1), SOUTH(0, -1), EAST(-1, 0), WEST(1, 0);

	int x;
	int y;

	Directions(int directionX, int directionY) {
		this.x = directionX;
		this.y = directionY;
	}

	public static Directions pickRandomDirection() {
		Directions[] directions = Directions.values();
		int directionIndex = Double.valueOf(Math.floor(Math.random() * directions.length)).intValue();
		return directions[directionIndex];
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

}
