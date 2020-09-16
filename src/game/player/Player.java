package game.player;

import java.util.ArrayList;

import game.arrow.Arrow;
import game.map.Cavern;

public class Player {
	private Cavern currentCavern;
	private ArrayList<Arrow> arrowArray;

	public Player() {
		this.initializeArrows();
	}

	private void initializeArrows() {
		this.arrowArray = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			this.arrowArray.add(new Arrow());
		}
	}

	public void setCurrentCavern(Cavern newCavern) {
		this.currentCavern = newCavern;
	}

	public Cavern getCurrentCavern() {
		return this.currentCavern;
	}

	public void setArrowArray(ArrayList<Arrow> arrows) {
		this.arrowArray = arrows;
	}

	public ArrayList<Arrow> getArrowArray() {
		return this.arrowArray;
	}

	public Arrow getArrow(int arrowIndex) {
		return this.arrowArray.get(arrowIndex);
	}

	public void setArrow(int arrowIndex, Arrow arrow) {
		this.arrowArray.set(arrowIndex, arrow);
	}
}
