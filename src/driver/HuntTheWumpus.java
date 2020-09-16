package driver;

import java.util.ArrayList;
import java.util.Scanner;

import game.Game;
import game.NotWumpusCavernException;
import game.commands.Commands;
import game.map.Cavern;
import game.map.Directions;
import game.map.MapObjects;

public class HuntTheWumpus {

	public static String errorInput = "\nInvalid User Entry, Please Try Again.";

	public static void main(String[] args) {

		Game game = new Game(args);

		Scanner scanner = new Scanner(System.in);

		game.sendWelcome();

		String userInput = scanner.nextLine();
		while (!userInput.equals("y") && !userInput.equals("n")) {
			System.out.println(errorInput);
			userInput = scanner.nextLine();
		}
		if (userInput.equals("y")) {

			game.sendRules();
			game.senseDanger();
			// Begin game with senseDanger
			boolean playerDeadOrWon = false;
			boolean gameStart = true;
			while (!playerDeadOrWon) {

				if (gameStart) {
					// this happens only the first time through the loop, when
					// the game starts
					gameStart = false;
				} else {
					// the rest of the time, starting over means incorrect input
					System.out.println(errorInput);
				}

				userInput = scanner.nextLine();
				Commands input = Commands.getCommand(userInput);

				while (input != null) { // input == null if incorrect input
					// only exit this loop for incorrect input or end condition

					if (input.equals(Commands.W) || input.equals(Commands.S) || input.equals(Commands.A)
							|| input.equals(Commands.D)) {
						playerDeadOrWon = game.movePlayer(input);
						if (playerDeadOrWon) {
							break;
						}
					}

					if (input.equals(Commands.I) || input.equals(Commands.K) || input.equals(Commands.J)
							|| input.equals(Commands.L)) {
						playerDeadOrWon = game.shootArrow(input);
						if (playerDeadOrWon) {
							break;
						}
					}

					if (input.equals(Commands.END)) {
						playerDeadOrWon = true;
						break;
					}

					// To reach here, either one of the above 3 if-loops
					// triggered or input == skip (a command not included in the
					// rules, only to be used in testing)
					// So now sense danger, then the wumpus moves

					game.senseDanger();

					ArrayList<Cavern> wumpusCaverns = game.map.findObjects(MapObjects.WUMPUS);
					for (Cavern wumpusCavern : wumpusCaverns) {
						// this allows for multiple wumpi
						try {
							playerDeadOrWon = game.moveWumpus(wumpusCavern, Directions.pickRandomDirection());
						} catch (NotWumpusCavernException e) {
							// this shouldn't happen, but caution is good
							System.out.println(e.getMessage());
							scanner.close();
							return;
						}
						if (playerDeadOrWon) {
							break;
						}
					}
					if (playerDeadOrWon) {
						break;
					}

					userInput = scanner.nextLine();
					input = Commands.getCommand(userInput);
				}
			}
			// if you're here game has ended
			game.sendEndGame();
			scanner.close();
		}
	}

}
