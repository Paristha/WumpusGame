package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import game.map.Cavern;
import game.map.Directions;
import game.map.Map;
import game.map.MapObjects;

public class MapTest {

	private Map map;
	private int mapsize = 1;
	private HashMap<Cavern, MapObjects> caverns;
	private HashMap<Cavern, MapObjects> potentialCaverns;
	private Cavern firstCavern;

	@Before
	public void makeOneCavernMap() {
		this.map = new Map(this.mapsize, 0, 0, 0);
		this.caverns = this.map.getCaverns();
		this.potentialCaverns = this.map.getPotentialCaverns();
		this.firstCavern = this.map.getFirstCavern();
	}

	@Test
	public void mapHasOneCavern() {
		assertFalse(this.caverns.isEmpty());
		assertTrue(this.caverns.size() == this.mapsize);
	}

	@Test
	public void checkOnlyCavernIsFirstCavern() {
		Cavern onlyCavern = (Cavern) this.caverns.keySet().toArray()[0];
		assertTrue(this.firstCavern.equals(onlyCavern));
	}

	@Test
	public void checkFirstCavernCoordinates() {
		assertTrue(this.firstCavern.getX() == 0 && this.firstCavern.getY() == 0);
	}

	@Test
	public void checkFourPotentialCaverns() {
		assertTrue(this.potentialCaverns.size() == 4);
	}

	@Test
	public void checkPotentialCavernsAreMovedCaverns() {
		ArrayList<Cavern> movedCaverns = new ArrayList<Cavern>();
		for (Directions direction : Directions.values()) {
			movedCaverns.add(this.firstCavern.move(direction));
		}
		for (Cavern potentialCavern : this.potentialCaverns.keySet()) {
			assertTrue(movedCaverns.contains(potentialCavern));
		}
		for (Cavern movedCavern : movedCaverns) {
			assertTrue(this.potentialCaverns.keySet().contains(movedCavern));
		}
	}

	@Test
	public void checkTwoCaverns() {
		this.mapsize = 2;
		this.caverns = new Map(this.mapsize, 0, 0, 0).getCaverns();
		assertTrue(this.caverns.size() == this.mapsize);
	}

	@Test
	public void checkTwoCavernsHasSixPotentialCaverns() {
		this.potentialCaverns = new Map(2, 0, 0, 0).getPotentialCaverns();
		assertTrue(this.potentialCaverns.size() == 6);
	}

	@Test
	public void checkFiftyCaverns() {
		this.mapsize = 50;
		this.caverns = new Map(this.mapsize, 0, 0, 0).getCaverns();
		assertTrue(this.caverns.size() == this.mapsize);
	}

	@Test
	public void checkFiftyCavernsAreConnected() {
		this.caverns = new Map(50, 0, 0, 0).getCaverns();
		for (Cavern cavern : this.caverns.keySet()) {
			ArrayList<Cavern> neighboringCaverns = Map.generateNeighbors(cavern);
			boolean isConnected = false;
			for (Cavern neighboringCavern : neighboringCaverns) {
				if (this.caverns.containsKey(neighboringCavern)) {
					isConnected = true;
				}
			}
			assertTrue(isConnected);
		}
	}

	@Test
	public void checkOneCavernAlwaysEmpty() {
		this.map = new Map(1, 1, 0, 0);
		this.caverns = this.map.getCaverns();
		assertTrue(this.caverns.get(this.map.getFirstCavern()) == MapObjects.EMPTY);
		this.map = new Map(1, 0, 1, 0);
		this.caverns = this.map.getCaverns();
		assertTrue(this.caverns.get(this.map.getFirstCavern()) == MapObjects.EMPTY);
		this.map = new Map(1, 0, 0, 1);
		this.caverns = this.map.getCaverns();
		assertTrue(this.caverns.get(this.map.getFirstCavern()) == MapObjects.EMPTY);
	}

	@Test
	public void checkMakesOneBatCavern() {
		this.map = new Map(2, 1, 0, 0);
		ArrayList<Cavern> batCaverns = this.map.findObjects(MapObjects.BATS);
		assertTrue(batCaverns.size() == 1);
	}

	@Test
	public void checkMakesTwoBatCavern() {
		this.map = new Map(3, 2, 0, 0);
		ArrayList<Cavern> batCaverns = this.map.findObjects(MapObjects.BATS);
		assertTrue(batCaverns.size() == 2);
	}

	@Test
	public void checkBatCavernIsNotFirstCavern() {
		this.map = new Map(2, 1, 0, 0);
		ArrayList<Cavern> batCaverns = this.map.findObjects(MapObjects.BATS);
		Cavern batCavern = batCaverns.get(0);
		assertTrue(!this.firstCavern.equals(batCavern));
	}

	@Test
	public void checkMakesOnePitCavern() {
		this.map = new Map(2, 0, 1, 0);
		ArrayList<Cavern> pitCaverns = this.map.findObjects(MapObjects.PIT);
		assertTrue(pitCaverns.size() == 1);
	}

	@Test
	public void checkMakesTwoPitCavern() {
		this.map = new Map(3, 0, 2, 0);
		ArrayList<Cavern> batCaverns = this.map.findObjects(MapObjects.PIT);
		assertTrue(batCaverns.size() == 2);
	}

	@Test
	public void checkPitCavernIsNotFirstCavern() {
		this.map = new Map(2, 0, 1, 0);
		ArrayList<Cavern> pitCaverns = this.map.findObjects(MapObjects.PIT);
		Cavern pitCavern = pitCaverns.get(0);
		assertTrue(!this.firstCavern.equals(pitCavern));
	}

	@Test
	public void checkMakesOneWumpusCavern() {
		this.map = new Map(2, 0, 0, 1);
		ArrayList<Cavern> wumpusCaverns = this.map.findObjects(MapObjects.WUMPUS);
		assertTrue(wumpusCaverns.size() == 1);
	}

	@Test
	public void checkMakesTwoWumpusCavern() {
		this.map = new Map(3, 0, 0, 2);
		ArrayList<Cavern> batCaverns = this.map.findObjects(MapObjects.WUMPUS);
		assertTrue(batCaverns.size() == 2);
	}

	@Test
	public void checkWumpusCavernIsNotFirstCavern() {
		this.map = new Map(2, 0, 0, 1);
		ArrayList<Cavern> wumpusCaverns = this.map.findObjects(MapObjects.WUMPUS);
		Cavern wumpusCavern = wumpusCaverns.get(0);
		assertTrue(!this.firstCavern.equals(wumpusCavern));
	}

	@Test
	public void checkWumpusCavernHasPriority() {
		this.map = new Map(2, 1, 1, 1);
		ArrayList<Cavern> wumpusCaverns = this.map.findObjects(MapObjects.WUMPUS);
		assertTrue(wumpusCaverns.size() == 1);
	}

	@Test
	public void checkMakesOneOfEachCavern() {
		this.map = new Map(50, 1, 1, 1);
		ArrayList<Cavern> batCaverns = this.map.findObjects(MapObjects.BATS);
		assertTrue(batCaverns.size() == 1);
		ArrayList<Cavern> pitCaverns = this.map.findObjects(MapObjects.PIT);
		assertTrue(pitCaverns.size() == 1);
		ArrayList<Cavern> wumpusCaverns = this.map.findObjects(MapObjects.WUMPUS);
		assertTrue(wumpusCaverns.size() == 1);
	}

	@Test
	public void checkMakesTwoOfEachCavern() {
		this.map = new Map(50, 2, 2, 2);
		ArrayList<Cavern> batCaverns = this.map.findObjects(MapObjects.BATS);
		assertTrue(batCaverns.size() == 2);
		ArrayList<Cavern> pitCaverns = this.map.findObjects(MapObjects.PIT);
		assertTrue(pitCaverns.size() == 2);
		ArrayList<Cavern> wumpusCaverns = this.map.findObjects(MapObjects.WUMPUS);
		assertTrue(wumpusCaverns.size() == 2);
	}

}
