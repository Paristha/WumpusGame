package test;

import static org.junit.Assert.assertTrue;

import java.util.EmptyStackException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import game.Events;
import game.Game;
import game.NotWumpusCavernException;
import game.commands.Commands;
import game.map.Cavern;
import game.map.Directions;
import game.map.Map;
import game.map.MapObjects;

public class GameTestMoving {

	private Game game;
	private Map map;

	@Before
	public void init() {
		this.game = new Game();
		HashMap<Cavern, MapObjects> caverns = new HashMap<Cavern, MapObjects>();
		caverns.put(new Cavern(0, 0), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 1), MapObjects.EMPTY);
		caverns.put(new Cavern(1, 1), MapObjects.WUMPUS);
		caverns.put(new Cavern(0, 2), MapObjects.EMPTY);
		caverns.put(new Cavern(-1, 2), MapObjects.PIT);
		caverns.put(new Cavern(0, 3), MapObjects.EMPTY);
		caverns.put(new Cavern(1, 3), MapObjects.BATS);
		caverns.put(new Cavern(0, 4), MapObjects.EMPTY);
		caverns.put(new Cavern(0, 5), MapObjects.BATS);
		caverns.put(new Cavern(1, 4), MapObjects.PIT);
		caverns.put(new Cavern(-1, 4), MapObjects.WUMPUS);
		this.map = new Map(caverns);
		this.game.setMap(this.map);
	}

	// The map looks like this (W=Wumpus, B=Bats, P=Pit, U=User/Player, *=Empty
	// Cavern)
	// (B)
	// (P)(*)(W)
	// (B)(*)
	// (*)(P)
	// (W)(*)
	// (U)

	@Test
	public void eventTriggeredWhenPlayerMovesIntoEmpty() {
		this.game.movePlayer(Commands.W);
		assertTrue(this.game.popLastEvent() == Events.NORTH);
		assertTrue(this.game.popLastEvent() == Events.MOVE);
	}

	@Test
	public void eventTriggeredWhenPlayerCantMove() {
		this.game.movePlayer(Commands.A);
		assertTrue(this.game.popLastEvent() == Events.WEST);
		assertTrue(this.game.popLastEvent() == Events.NO_MOVE);
	}

	@Test
	public void eventTriggeredWhenPlayerMovesIntoWumpus() {
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.A);
		assertTrue(this.game.popLastEvent() == Events.WUMPUS_DEATH);
	}

	@Test
	public void eventTriggeredWhenPlayerMovesIntoPit() {
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.D);
		assertTrue(this.game.popLastEvent() == Events.PIT_DEATH);
	}

	@Test
	public void eventTriggeredWhenPlayerMovesIntoBats() {
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.A);
		assertTrue(this.game.popLastEvent() == Events.BAT_TELEPORT);
	}

	@Test
	public void eventNotTriggeredWhenSenseDangerWithNothingNear() {
		this.game.senseDanger();
		boolean emptyStackExceptionThrown = false;
		try {
			this.game.popLastEvent();
		} catch (EmptyStackException e) {
			emptyStackExceptionThrown = true;
		}
		assertTrue(emptyStackExceptionThrown);
	}

	@Test
	public void eventTriggeredWhenSenseDangerWithWumpusNear() {
		this.game.movePlayer(Commands.I);
		this.game.senseDanger();
		assertTrue(this.game.popLastEvent() == Events.SENSE_DANGER_WUMPUS);
	}

	@Test
	public void eventTriggeredWhenSenseDangerWithPitNear() {
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.I);
		this.game.senseDanger();
		assertTrue(this.game.popLastEvent() == Events.SENSE_DANGER_PIT);
	}

	@Test
	public void eventTriggeredWhenSenseDangerWithBatsNear() {
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.I);
		this.game.senseDanger();
		assertTrue(this.game.popLastEvent() == Events.SENSE_DANGER_BATS);
	}

	@Test
	public void allEventsTriggeredWhenSenseDangerWithEverythingNear() {
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.I);
		this.game.movePlayer(Commands.I);
		this.game.senseDanger();
		assertTrue(this.game.popLastEvent() == Events.SENSE_DANGER_PIT);
		assertTrue(this.game.popLastEvent() == Events.SENSE_DANGER_WUMPUS);
		assertTrue(this.game.popLastEvent() == Events.SENSE_DANGER_BATS);
	}

	@Test
	public void eventNotTriggeredWhenWumpusCantMove() throws NotWumpusCavernException {
		this.game.moveWumpus(new Cavern(-1, 4), Directions.EAST);
		boolean emptyStackExceptionThrown = false;
		try {
			this.game.popLastEvent();
		} catch (EmptyStackException e) {
			emptyStackExceptionThrown = true;
		}
		assertTrue(emptyStackExceptionThrown);
	}

	@Test
	public void eventTriggeredWhenWumpusMovesIntoEmpty() throws NotWumpusCavernException {
		this.game.moveWumpus(new Cavern(-1, 4), Directions.WEST);
		assertTrue(this.game.popLastEvent() == Events.WUMPUS_MOVE);
	}

	@Test
	public void eventNotTriggeredWhenWumpusCantMoveIntoPit() throws NotWumpusCavernException {
		this.game.moveWumpus(new Cavern(-1, 4), Directions.WEST);
		this.game.popLastEvent(); // getting rid of WUMPUS_MOVE event
		this.game.moveWumpus(new Cavern(0, 4), Directions.WEST);
		boolean emptyStackExceptionThrown = false;
		try {
			this.game.popLastEvent();
		} catch (EmptyStackException e) {
			emptyStackExceptionThrown = true;
		}
		assertTrue(emptyStackExceptionThrown);
	}

	@Test
	public void eventNotTriggeredWhenWumpusCantMoveIntoWumpus() throws NotWumpusCavernException {
		this.game.moveWumpus(new Cavern(-1, 4), Directions.WEST);
		this.game.popLastEvent(); // getting rid of WUMPUS_MOVE event
		this.game.moveWumpus(new Cavern(0, 4), Directions.SOUTH);
		this.game.popLastEvent(); // getting rid of WUMPUS_MOVE event
		this.game.moveWumpus(new Cavern(0, 3), Directions.SOUTH);
		this.game.popLastEvent(); // getting rid of WUMPUS_MOVE event
		this.game.moveWumpus(new Cavern(0, 2), Directions.SOUTH);
		this.game.popLastEvent(); // getting rid of WUMPUS_MOVE event
		this.game.moveWumpus(new Cavern(0, 1), Directions.WEST);
		boolean emptyStackExceptionThrown = false;
		try {
			this.game.popLastEvent();
		} catch (EmptyStackException e) {
			emptyStackExceptionThrown = true;
		}
		assertTrue(emptyStackExceptionThrown);
	}

	@Test
	public void eventTriggeredWhenWumpusMovesIntoBats() throws NotWumpusCavernException {
		this.game.moveWumpus(new Cavern(-1, 4), Directions.WEST);
		this.game.popLastEvent(); // getting rid of first WUMPUS_MOVE event
		this.game.moveWumpus(new Cavern(0, 4), Directions.NORTH);
		assertTrue(this.game.popLastEvent() == Events.WUMPUS_MOVE);
	}

	@Test
	public void eventTriggeredWhenWumpusMovesIntoPlayer() throws NotWumpusCavernException {
		this.game.moveWumpus(new Cavern(1, 1), Directions.EAST);
		this.game.moveWumpus(new Cavern(0, 1), Directions.SOUTH);
		assertTrue(this.game.popLastEvent() == Events.WUMPUS_DEATH);
	}

	@Test
	public void eventTriggeredWhenWumpusStepsOnArrow() throws NotWumpusCavernException {
		this.game.shootArrow(Commands.I);
		this.game.moveWumpus(new Cavern(-1, 4), Directions.WEST);
		this.game.moveWumpus(new Cavern(0, 4), Directions.NORTH);
		assertTrue(this.game.popLastEvent() == Events.WUMPUS_DESTROY_ARROW);
	}

	@Test
	public void exceptionThrownWhenEmptyCavernSentToWumpusMove() throws NotWumpusCavernException {
		boolean notWumpusCavernExceptionThrown = false;
		try {
			this.game.moveWumpus(new Cavern(0, 0), Directions.pickRandomDirection());
		} catch (NotWumpusCavernException e) {
			notWumpusCavernExceptionThrown = true;
		}
		assertTrue(notWumpusCavernExceptionThrown);
	}

}
