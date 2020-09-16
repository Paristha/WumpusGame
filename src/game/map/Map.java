package game.map;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {

	private HashMap<Cavern, MapObjects> caverns;
	private HashMap<Cavern, MapObjects> potentialCaverns;

	private Cavern firstCavern;

	public Map(int mapsize, int bats, int pits, int wumpus) {
		this.setCaverns();
		this.setPotentialCaverns();
		this.makeMap(mapsize, bats, pits, wumpus);
	}

	public Map(HashMap<Cavern, MapObjects> premadeCaverns) {
		this.setCaverns(premadeCaverns);
	}

	public static ArrayList<Cavern> generateNeighbors(Cavern cavern) {
		ArrayList<Cavern> neighbors = new ArrayList<Cavern>();
		Directions[] directions = Directions.values();
		for (int i = 0; i < directions.length; i++) {
			neighbors.add(cavern.move(directions[i]));
		}
		return neighbors;
	}

	private void makeMap(int mapsize, int bats, int pits, int wumpus) {
		this.setFirstCavern(new Cavern(0, 0));
		this.caverns.put(this.firstCavern, MapObjects.EMPTY);
		addNeighborsToPotentialCaverns(this.firstCavern);

		if (mapsize < 2) {
			return;
		}

		for (int i = 1; i < mapsize; i++) {
			int newIndex = Double.valueOf(Math.floor(Math.random() * this.potentialCaverns.size())).intValue();
			Cavern newCavern = (Cavern) this.potentialCaverns.keySet().toArray()[newIndex];
			this.caverns.put(newCavern, MapObjects.EMPTY);
			addNeighborsToPotentialCaverns(newCavern);
			removeRealizedCaverns();
		}

		for (int i = 0; i < wumpus; i++) {
			Cavern wumpusCavern = this.pickRandomNotFirstEmptyCavern();
			this.caverns.replace(wumpusCavern, MapObjects.WUMPUS);
		}

		for (int i = 0; i < bats; i++) {
			Cavern batCavern = this.pickRandomNotFirstEmptyCavern();
			this.caverns.replace(batCavern, MapObjects.BATS);
		}

		for (int i = 0; i < pits; i++) {
			Cavern pitCavern = this.pickRandomNotFirstEmptyCavern();
			this.caverns.replace(pitCavern, MapObjects.PIT);
		}

	}

	private void addNeighborsToPotentialCaverns(Cavern cavern) {
		ArrayList<Cavern> neighbors = generateNeighbors(cavern);
		for (Cavern neighbor : neighbors) {
			this.potentialCaverns.put(neighbor, MapObjects.EMPTY);
		}
	}

	private void removeRealizedCaverns() {
		ArrayList<Cavern> realizedCaverns = new ArrayList<Cavern>();
		this.potentialCaverns.forEach((key, state) -> {
			if (this.caverns.containsKey(key)) {
				realizedCaverns.add(key);
			}
		});
		for (Cavern key : realizedCaverns) {
			this.potentialCaverns.remove(key);
		}
	}

	public Cavern pickRandomNotFirstEmptyCavern() {
		ArrayList<Cavern> emptyCaverns = this.findObjects(MapObjects.EMPTY);
		if (emptyCaverns.size() < 2) {
			return null;
		}
		int cavernIndex = Double.valueOf(Math.floor(Math.random() * (emptyCaverns.size() - 1))).intValue() + 1;
		// ^ this ensures firstCavern never picked by making index never = 1
		Cavern cavern = emptyCaverns.get(cavernIndex);
		return cavern;
	}

	public Cavern pickRandomEmptyCavern() {
		ArrayList<Cavern> emptyCaverns = this.findObjects(MapObjects.EMPTY);
		if (emptyCaverns.size() == 0) {
			return null;
		}
		int cavernIndex = Double.valueOf(Math.floor(Math.random() * emptyCaverns.size())).intValue();
		Cavern cavern = emptyCaverns.get(cavernIndex);
		return cavern;
	}

	public ArrayList<Cavern> findObjects(MapObjects object) {
		ArrayList<Cavern> foundCaverns = new ArrayList<Cavern>();
		for (Cavern cavern : this.caverns.keySet()) {
			if (this.caverns.get(cavern) == object) {
				foundCaverns.add(cavern);
			}
		}
		return foundCaverns;
	}

	public void moveCavernContents(Cavern oldCavern, Cavern newCavern) {
		MapObjects newCavernContents = this.checkCavernContents(newCavern);
		if (newCavernContents == MapObjects.EMPTY) {
			this.fillCavern(newCavern, this.checkCavernContents(oldCavern));
			this.fillCavern(oldCavern, MapObjects.EMPTY);
		}
	}

	public void fillCavern(Cavern newCavern, MapObjects newCavernContents) {
		this.caverns.put(newCavern, newCavernContents);
	}

	public MapObjects checkCavernContents(Cavern cavern) {
		if (cavern == null) {
			return null;
		}
		return this.caverns.get(cavern);
	}

	private void setCaverns() {
		this.caverns = new HashMap<Cavern, MapObjects>();
	}

	public void setCaverns(HashMap<Cavern, MapObjects> newCaverns) {
		this.caverns = newCaverns;
	}

	public HashMap<Cavern, MapObjects> getCaverns() {
		return this.caverns;
	}

	private void setPotentialCaverns() {
		this.potentialCaverns = new HashMap<Cavern, MapObjects>();
	}

	public HashMap<Cavern, MapObjects> getPotentialCaverns() {
		return this.potentialCaverns;
	}

	public void setFirstCavern(Cavern firstCavern) {
		this.firstCavern = firstCavern;
	}

	public Cavern getFirstCavern() {
		return this.firstCavern;
	}

}
