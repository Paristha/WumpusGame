package test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import game.Events;
import game.Game;
import game.commands.Commands;
import game.map.Cavern;
import game.map.Map;
import game.map.MapObjects;

public class GameTestArrows {

	private Game game;
	private Map map;

	@Before
	public void init() {
		this.game = new Game();
		this.map = this.game.getMap();
	}

	@Test
	public void shootArrowDecreasesArrowCount() throws Exception {
		int firstArrowIndex = this.game.pickArrow();
		this.game.shootArrow(Commands.I);
		int secondArrowIndex = this.game.pickArrow();
		assertTrue(firstArrowIndex != secondArrowIndex);
	}

	@Test
	public void eventTriggeredWhenNotEnoughArrows() {
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		assertTrue(this.game.popLastEvent() == Events.NO_ARROWS);
	}

	@Test
	public void eventNotTriggeredWhenEnoughArrows() {
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		assertTrue(this.game.popLastEvent() != Events.NO_ARROWS);
	}

	@Test
	public void eventTriggersWhenShootArrow() throws Exception {
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
		this.game.shootArrow(Commands.I);
		this.game.popLastEvent();
		// ^ called because last event would be outcome of arrow being shot
		assertTrue(this.game.popLastEvent() == Events.NORTH);
		assertTrue(this.game.popLastEvent() == Events.ARROW_SHOT);
	}

	@Test
	public void eventTriggeredWhenArrowDeadEnds() {
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 1), MapObjects.EMPTY);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
		this.game.shootArrow(Commands.I);
		assertTrue(this.game.popLastEvent() == Events.ARROW_FALLS);
	}

	@Test
	public void eventTriggeredWhenArrowHitsBats() {
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 1), MapObjects.BATS);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
		this.game.shootArrow(Commands.I);
		assertTrue(this.game.popLastEvent() == Events.USER_KILL_BATS);
	}

	@Test
	public void eventTriggeredWhenArrowFallsInPit() {
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 1), MapObjects.PIT);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
		this.game.shootArrow(Commands.I);
		assertTrue(this.game.popLastEvent() == Events.PIT_DESTROY_ARROW);
	}

	@Test
	public void eventTriggeredWhenArrowHitsWumpus() {
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 1), MapObjects.WUMPUS);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
		this.game.shootArrow(Commands.I);
		assertTrue(this.game.popLastEvent() == Events.USER_KILL_WUMPUS);
	}

	@Test
	public void eventTriggeredWhenArrowKillsUser() {
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 1), MapObjects.EMPTY);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
		this.game.shootArrow(Commands.J);
		assertTrue(this.game.popLastEvent() == Events.ARROW_DEATH);
	}

	@Test
	public void eventTriggeredWhenArrowPassesThroughCavern() {
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 1), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 2), MapObjects.EMPTY);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
		this.game.shootArrow(Commands.I);
		this.game.popLastEvent();
		assertTrue(this.game.popLastEvent() == Events.getDirectionEvent(Commands.I.getDirection()));
		assertTrue(this.game.popLastEvent() == Events.ARROW_MOVE);
	}

	@Test
	public void eventTriggeredWhenUserPicksUpArrow() {
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 1), MapObjects.EMPTY);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
		this.game.shootArrow(Commands.I);
		this.game.movePlayer(Commands.W);
		assertTrue(this.game.popLastEvent() == Events.FOUND_ARROW);
	}

	@Test
	public void arrowRecievedWhenUserPicksUpArrow() {
		int arrowsPickedUp = 1;
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 1), MapObjects.EMPTY);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
		this.game.shootArrow(Commands.I);
		int numberOfShotArrowsBefore = this.game.pickArrow();
		this.game.movePlayer(Commands.W);
		int numberOfShotArrowsAfter = this.game.pickArrow();
		assertTrue(numberOfShotArrowsAfter == numberOfShotArrowsBefore - arrowsPickedUp);
	}

	@Test
	public void multipleArrowsRecievedWhenUserPicksUpMultipleArrows() {
		int arrowsPickedUp = 3;
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 1), MapObjects.EMPTY);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		this.game.shootArrow(Commands.I);
		int numberOfShotArrowsBefore = this.game.pickArrow();
		this.game.movePlayer(Commands.W);
		int numberOfShotArrowsAfter = this.game.pickArrow();
		assertTrue(numberOfShotArrowsAfter == numberOfShotArrowsBefore - arrowsPickedUp);
	}
}
