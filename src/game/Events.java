package game;

import game.map.Directions;

public enum Events {
	WELCOME("\nWelcome to Hunt The Wumpus!!\n\nyou wish to start a game (y/n)?"), RULES(
			"\nRules for Hunt the Wumpus: \n1. You can move North (w), South (s), East (d), or West (a) using the wasd letter keys.\n\n2. Your goal is to kill the Wumpus with one of your arrows. The Wumpus walks around the caverns randomly.\nIf the Wumpus is one cavern away from you, you can smell the Wumpus (he smells really bad...)\nIf the Wumpus walks into you or you walk into the Wumpus, then you DIE.\n\n3. You start with 5 arrows. You shoot them North(i), South(k), East(l), or West(j) using the ijkl letter keys.\nAn arrow continues in the direction it was shot until it hits a wall or other object.\nIf you walk into a cavern with a dropped arrow, you automatically pick it up. You only have 5 arrows after all.\nIf you shoot an arrow into a wall directly next to you, you will DIE.\n\n4. There are Bats in these caverns...\nIf you walk into a cavern with bats, you will be flown to random location.\nIf you shoot an arrow and it lands in a bat cavern, the arrow will kill the bats, and you can then retrieve it.\nIf you are one cavern away from bats, you will hear screaching noises.\n\n5. There are Pits in these caverns...\nIf you walk into a cavern with pits, you will fall into the pit and DIE.\nIf you shoot an arrow and it flies over a pit, the arrow will fall down the pit, and you will be unable to reclaim it.\nIf you are one cavern away from a pit, you will hear blustering wind.\n\nGOOD LUCK!!!\n(end the game at any time by typing 'end')\n"), NORTH(
					" North.",
					Directions.NORTH), SOUTH(" South.", Directions.SOUTH), EAST(" East.", Directions.EAST), WEST(
							" West.",
							Directions.WEST), MOVE("\nUser moved"), NO_MOVE("\nUser cannot move"), BAT_TELEPORT(
									"\nYou have encountered bats! You are being flown to another location..."), PIT_DEATH(
											"\nYou have fallen into a pit and died."), WUMPUS_DEATH(
													"\nYou have been trampled by the Wumpus... Whomp, whomp :("), SENSE_DANGER_BATS(
															"\nYou hear screeching noises."), SENSE_DANGER_PIT(
																	"\nYou feel blustering wind."), SENSE_DANGER_WUMPUS(
																			"\nYou smell something really bad."), WUMPUS_MOVE(
																					"\nYou hear the Wumpus's trampling echo through the caverns..."), WUMPUS_DESTROY_ARROW(
																							"\nA loud SNAP! The Wumpus trampled one of your arrows :("), FOUND_ARROW(
																									"\nCongrats, you found one of your arrows."), NO_ARROWS(
																											"\nNo Usable Arrows Available :("), ARROW_SHOT(
																													"\nUser shot arrow"), ARROW_DEATH(
																															"\nArrow rebounded. You die."), ARROW_FALLS(
																																	"\nDead end, arrow falls. Pick up arrow to reclaim it."), USER_KILL_BATS(
																																			"\nUser killed bats. Pick-up arrow to reclaim it."), PIT_DESTROY_ARROW(
																																					"\nUser lost arrow in pit."), USER_KILL_WUMPUS(
																																							"\nYou killed the Wumpus. YOU WON!!!!"), ARROW_MOVE(
																																									"\nEmpty cavern, arrow continues"), GAME_OVER(
																																											"\nGame Over"), INCORRECT_ARGUMENTS(
																																													"StartGame.class takes no arguments or 4 int arguments: mapsize, batnumber, pitnumber, wumpusnumber. The default settings will be used now.");

	String eventMessage;
	Directions eventDirection;

	public static Events getDirectionEvent(Directions direction) {
		for (Events event : Events.values()) {
			if (event.getEventDirection() == direction) {
				return event;
			}
		}
		return null;
	}

	Events(String message) {
		this.eventMessage = message;
	}

	Events(String message, Directions direction) {
		this.eventMessage = message;
		this.eventDirection = direction;
	}

	public String getEventMessage() {
		return this.eventMessage;
	}

	public Directions getEventDirection() {
		return this.eventDirection;
	}

}
