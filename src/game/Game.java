package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import game.arrow.Arrow;
import game.commands.Commands;
import game.map.Cavern;
import game.map.Directions;
import game.map.Map;
import game.map.MapObjects;
import game.player.Player;

public class Game {

	public Map map;
	public HashMap<Cavern, String> caverns;
	public Player player;
	private Stack<Events> eventStack = new Stack<Events>();

	private int defaultMapSize = 50;
	private int defaultBatNumber = 5;
	private int defaultPitNumber = 5;
	private int defaultWumpusNumber = 1;

	public Game() {
		createMap();
		createPlayer();
	}

	public Game(String[] args) {
		createMap(args);
		createPlayer();
	}

	public void sendWelcome() {
		triggerEvent(Events.WELCOME);
	}

	public void sendRules() {
		triggerEvent(Events.RULES);
	}

	public void sendEndGame() {
		triggerEvent(Events.GAME_OVER);
	}

	public void triggerEvent(Events event) {
		System.out.print(event.getEventMessage());
		this.eventStack.push(event);
	}

	private void createMap() {
		this.map = new Map(this.defaultMapSize, this.defaultBatNumber, this.defaultPitNumber, this.defaultWumpusNumber);
	}

	private void createMap(String[] args) {
		if (args == null) {
			this.map = new Map(this.defaultMapSize, this.defaultBatNumber, this.defaultPitNumber,
					this.defaultWumpusNumber);
		}
		try {
			this.map = new Map(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]),
					Integer.valueOf(args[3]));
		} catch (Exception e) {
			triggerEvent(Events.INCORRECT_ARGUMENTS);
			this.map = new Map(this.defaultMapSize, this.defaultBatNumber, this.defaultPitNumber,
					this.defaultWumpusNumber);
		}
	}

	private void createPlayer() {
		this.player = new Player();
		this.player.setCurrentCavern(this.map.getFirstCavern());
	}

	public boolean movePlayer(Commands input) {
		Directions inputDirection = input.getDirection();
		Cavern newCavern = this.player.getCurrentCavern().move(inputDirection);
		MapObjects newCavernContents = this.map.checkCavernContents(newCavern);
		if (newCavernContents == null) {
			triggerEvent(Events.NO_MOVE);
			triggerEvent(Events.getDirectionEvent(inputDirection));
			return false;
		}
		triggerEvent(Events.MOVE);
		triggerEvent(Events.getDirectionEvent(inputDirection));
		if (newCavernContents == MapObjects.ARROW) {
			this.retrieveArrow(newCavern);
			this.player.setCurrentCavern(newCavern);
			return false;
		}
		if (newCavernContents == MapObjects.BATS) {
			newCavern = this.map.pickRandomEmptyCavern();
			if (newCavern == null) {
				// this shouldn't happen but caution is good
				return false;
			}
			this.player.setCurrentCavern(newCavern);
			triggerEvent(Events.BAT_TELEPORT);
			return false;
		}
		if (newCavernContents == MapObjects.PIT) {
			triggerEvent(Events.PIT_DEATH);
			return true;
		}
		if (newCavernContents == MapObjects.WUMPUS) {
			triggerEvent(Events.WUMPUS_DEATH);
			return true;
		}
		// we assume that only the case where newCavernContents ==
		// MapObjects.EMPTY can reach here
		this.player.setCurrentCavern(newCavern);
		return false;
	}

	public void senseDanger() {
		ArrayList<Cavern> cavernNeighbors = Map.generateNeighbors(this.player.getCurrentCavern());
		for (Cavern neighbor : cavernNeighbors) {
			MapObjects neighborContents = this.map.checkCavernContents(neighbor);
			if (neighborContents != null && neighborContents != MapObjects.EMPTY) {
				if (neighborContents == MapObjects.BATS) {
					triggerEvent(Events.SENSE_DANGER_BATS);
				}
				if (neighborContents == MapObjects.PIT) {
					triggerEvent(Events.SENSE_DANGER_PIT);
				}
				if (neighborContents == MapObjects.WUMPUS) {
					triggerEvent(Events.SENSE_DANGER_WUMPUS);
				}
			}

		}
	}

	public boolean moveWumpus(Cavern wumpusCavern, Directions direction) throws NotWumpusCavernException {
		if (this.map.checkCavernContents(wumpusCavern) != MapObjects.WUMPUS) {
			throw new NotWumpusCavernException("A non-wumpus cavern was passed to moveWumpus.");
		}
		Cavern newCavern = wumpusCavern.move(direction);
		MapObjects newCavernContents = this.map.checkCavernContents(newCavern);
		if (newCavernContents == null || newCavernContents == MapObjects.PIT
				|| newCavernContents == MapObjects.WUMPUS) {
			// in these cases the Wumpus doesn't move
			return false;
		}
		triggerEvent(Events.WUMPUS_MOVE);
		if (newCavern.equals(this.player.getCurrentCavern())) {
			triggerEvent(Events.WUMPUS_DEATH);
			return true;
		}
		if (newCavernContents == MapObjects.ARROW) {
			this.map.fillCavern(wumpusCavern, MapObjects.EMPTY);
			this.map.fillCavern(newCavern, MapObjects.WUMPUS);
			triggerEvent(Events.WUMPUS_DESTROY_ARROW);
			return false;
		}
		if (newCavernContents == MapObjects.BATS) {
			this.map.fillCavern(this.player.getCurrentCavern(), MapObjects.PLAYER);
			// ^ this temporarily prevents the Wumpus from being teleported on
			// top of the user
			newCavern = this.map.pickRandomEmptyCavern();
			if (newCavern == null) {
				// this shouldn't happen, but caution is good
				return false;
			}
			this.map.fillCavern(wumpusCavern, MapObjects.EMPTY);
			this.map.fillCavern(newCavern, MapObjects.WUMPUS);
			this.map.fillCavern(this.player.getCurrentCavern(), MapObjects.EMPTY);
			// ^ this puts things back to normal
			return false;
		}
		// we assume that only the case where newCavernContents ==
		// MapObjects.EMPTY can reach here
		this.map.fillCavern(wumpusCavern, MapObjects.EMPTY);
		this.map.fillCavern(newCavern, MapObjects.WUMPUS);
		return false;
	}

	public void retrieveArrow(Cavern arrowCavern) {
		ArrayList<Arrow> arrowArrayCopy = this.player.getArrowArray();
		for (int i = 0; i < arrowArrayCopy.size(); i++) {
			Arrow arrow = arrowArrayCopy.get(i);
			if (arrow.getCavern() != null && arrow.getCavern().equals(arrowCavern)) {
				triggerEvent(Events.FOUND_ARROW);
				arrow.setPlayerHasArrow(true);
				arrow.setCavern(null);
				arrowArrayCopy.set(i, arrow);
			}
		}
		this.player.setArrowArray(arrowArrayCopy);
		this.map.fillCavern(arrowCavern, MapObjects.EMPTY);
	}

	public boolean shootArrow(Commands input) {
		int arrowIndex = pickArrow();
		if (arrowIndex == -1) {
			triggerEvent(Events.NO_ARROWS);
			return false;
		}
		loseArrow(arrowIndex);
		triggerEvent(Events.ARROW_SHOT);
		triggerEvent(Events.getDirectionEvent(input.getDirection()));
		return moveArrow(arrowIndex, input);
	}

	public int pickArrow() {
		for (int i = 0; i < this.player.getArrowArray().size(); i++) {
			if (this.player.getArrowArray().get(i).getPlayerHasArrow()) {
				return i;
			}
		}
		return -1;
	}

	private void loseArrow(int arrowIndex) {
		Arrow arrow = this.player.getArrow(arrowIndex);
		arrow.setPlayerHasArrow(false);
		arrow.setCavern(this.player.getCurrentCavern());
		this.player.setArrow(arrowIndex, arrow);
	}

	private boolean moveArrow(int arrowIndex, Commands input) {
		Directions direction = input.getDirection();
		Arrow shotArrow = this.player.getArrow(arrowIndex);
		Cavern oldCavern = shotArrow.getCavern();
		Cavern newCavern = shotArrow.getCavern().move(direction);
		MapObjects oldCavernContents = this.map.checkCavernContents(oldCavern);
		MapObjects newCavernContents = this.map.checkCavernContents(newCavern);
		if (newCavernContents == null) {
			if (shotArrow.getCavern().equals(this.player.getCurrentCavern())) {
				triggerEvent(Events.ARROW_DEATH);
				return true;
			}
			if (oldCavernContents == MapObjects.EMPTY || oldCavernContents == MapObjects.ARROW) {
				this.map.fillCavern(oldCavern, MapObjects.ARROW);
				shotArrow.setCavern(oldCavern);
				this.player.setArrow(arrowIndex, shotArrow);
				triggerEvent(Events.ARROW_FALLS);
				return false;
			}
		}
		if (newCavernContents == MapObjects.BATS) {
			this.map.fillCavern(newCavern, MapObjects.ARROW);
			shotArrow.setCavern(newCavern);
			this.player.setArrow(arrowIndex, shotArrow);
			triggerEvent(Events.USER_KILL_BATS);
			return false;
		}
		if (newCavernContents == MapObjects.PIT) {
			shotArrow.setCavern(null);
			this.player.setArrow(arrowIndex, shotArrow);
			triggerEvent(Events.PIT_DESTROY_ARROW);
			return false;
		}
		if (newCavernContents == MapObjects.WUMPUS) {
			triggerEvent(Events.USER_KILL_WUMPUS);
			return true;
		}
		// we assume that only the case where newCavernContents ==
		// MapObjects.EMPTY can reach here
		triggerEvent(Events.ARROW_MOVE);
		triggerEvent(Events.getDirectionEvent(direction));
		shotArrow.setCavern(newCavern);
		this.player.setArrow(arrowIndex, shotArrow);
		return moveArrow(arrowIndex, input);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Events popLastEvent() {
		return this.eventStack.pop();
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Map getMap() {
		return this.map;
	};

}
