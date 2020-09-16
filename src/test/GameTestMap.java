package test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import game.Game;
import game.map.Map;
import game.map.MapObjects;
import game.player.Player;

public class GameTestMap {

	private Game game;
	private Map map;
	private Player player;
	private int defaultMapSize = 50;
	private int defaultBatNumber = 5;
	private int defaultPitNumber = 5;
	private int defaultWumpusNumber = 1;

	@Before
	public void init() {
		this.game = new Game();
		this.map = this.game.getMap();
		this.player = this.game.getPlayer();
	}

	@Test
	public void hasInstanceOfMap() {
		assertTrue(this.map != null);
	}

	@Test
	public void hasInstanceOfPlayer() {
		assertTrue(this.player != null);
	}

	@Test
	public void hasDefaultMapSize() {
		assertTrue(this.map.getCaverns().size() == this.defaultMapSize);
	}

	@Test
	public void hasDefaultBatNumber() {
		assertTrue(this.map.findObjects(MapObjects.BATS).size() == this.defaultBatNumber);
	}

	@Test
	public void hasDefaultPitNumber() {
		assertTrue(this.map.findObjects(MapObjects.PIT).size() == this.defaultPitNumber);
	}

	@Test
	public void hasDefaultWumpusNumber() {
		assertTrue(this.map.findObjects(MapObjects.WUMPUS).size() == this.defaultWumpusNumber);
	}

	@Test
	public void hasPlayerLocation() {
		assertTrue(this.player.getCurrentCavern() != null);
	}

	@Test
	public void playerLocationIsFirstCavern() {
		assertTrue(this.player.getCurrentCavern().equals(this.map.getFirstCavern()));
	}

}
