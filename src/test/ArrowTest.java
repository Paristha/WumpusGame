package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import game.arrow.Arrow;
import game.map.Cavern;

public class ArrowTest {
	private Arrow newArrow;
	private Cavern startingCavern;

	@Before
	public void init() {
		this.newArrow = null;
		this.newArrow = new Arrow();
	}

	@Test
	public void newArrowIsAvailable() {
		assertTrue(this.newArrow.getPlayerHasArrow());
	}

	@Test
	public void setArrowToUnusable() {
		this.newArrow.setPlayerHasArrow(false);
		assertFalse(this.newArrow.getPlayerHasArrow());
	}

	@Test
	public void setArrowLocation() {
		this.newArrow.setCavern(this.startingCavern);
		assertTrue(this.startingCavern == this.newArrow.getCavern());
	}

	@Test
	public void atStartOfGamePlayerHasArrow() {
		assertTrue(this.newArrow.getPlayerHasArrow());
	}

}
