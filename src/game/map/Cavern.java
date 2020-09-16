package game.map;

public class Cavern {

	private int x;
	private int y;

	public Cavern(int newx, int newy) {
		this.x = newx;
		this.y = newy;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	@Override
	public boolean equals(Object obj) {
		Cavern other = (Cavern) obj;
		return (other.x == this.x && other.y == this.y);
	}

	@Override
	public int hashCode() {
		return this.x * 100 + this.y;
	}

	public Cavern move(Directions direction) {
		if (direction == null) {
			return null;
		}
		return (new Cavern(this.x + direction.getX(), this.y + direction.getY()));
	}
}