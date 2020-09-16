package game.arrow;

import game.map.Cavern;

public class Arrow {

	private boolean playerHasArrow;
	private Cavern cavern;

	public Arrow() {
		this.playerHasArrow = true;
	}

	public void setPlayerHasArrow(boolean playerHasArrow) {
		this.playerHasArrow = playerHasArrow;
	}

	public boolean getPlayerHasArrow() {
		return this.playerHasArrow;
	}

	public void setCavern(Cavern cavern) {
		this.cavern = cavern;
	}

	public Cavern getCavern() {
		return this.cavern;
	}

}
