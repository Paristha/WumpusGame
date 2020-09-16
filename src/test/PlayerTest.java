package test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import game.Game;
import game.player.Player;

public class PlayerTest {

	private Game game;
	private Player player;

	@Before
	public void init() {
		this.game = new Game();
		this.player = this.game.getPlayer();
	}

	@Test
	public void playerHasCurrentCavern() {
		assertTrue(this.player.getCurrentCavern() != null);
	}

	@Test
	public void playerHasArrowArray() {
		assertTrue(this.player.getArrowArray() != null);
	}

	@Test
	public void playerStartsWithFiveArrows() {
		int numberOfArrows = this.player.getArrowArray().size();
		assertTrue(numberOfArrows == 5);
	}

}
