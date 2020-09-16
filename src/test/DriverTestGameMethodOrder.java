package test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.HuntTheWumpus;
import game.Events;

public class DriverTestGameMethodOrder {

	private String[] args;
	private File outputFile;
	private BufferedReader outputReader;
	private PrintStream commandLine;
	private FileOutputStream fos;

	@Before
	public void init() throws FileNotFoundException {
		this.outputFile = new File("output.txt");
		this.fos = new FileOutputStream(this.outputFile);
		this.commandLine = System.out;
		System.setOut(new PrintStream(this.fos));
	}

	@After
	public void cleanup() throws IOException {
		System.in.close();
		System.setOut(this.commandLine);
		this.fos.close();
		this.outputReader.close();
		this.outputFile.delete();
		// comment out the above line to preserve output.txt for testing
	}

	@Test
	public void senseDangerCalledAtGameStart() throws IOException {
		this.args = new String[] { "2", "1", "0", "0" };
		System.setIn(new FileInputStream("DriverTest12.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String senseDangerBatsMessage = appendLines("", 1);
		assertTrue(senseDangerBatsMessage.equals(Events.SENSE_DANGER_BATS.getEventMessage()));
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void senseDangerCalledAfterUserInput() throws IOException {
		this.args = new String[] { "2", "1", "0", "0" };
		System.setIn(new FileInputStream("DriverTest13.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String senseDangerBatsMessageFirst = appendLines("", 1);
		assertTrue(senseDangerBatsMessageFirst.equals(Events.SENSE_DANGER_BATS.getEventMessage()));
		String nextLine = appendLines("", 1);
		if (!nextLine.equals(Events.NO_MOVE.getEventMessage() + Events.NORTH.getEventMessage())) {
			this.outputReader.readLine();
			// this code is called if the bat cave is generated north to skip
			// the Events.BAT_TELEPORT message
		}
		String senseDangerBatsMessageSecond = appendLines("", 1);
		assertTrue(senseDangerBatsMessageSecond.equals(Events.SENSE_DANGER_BATS.getEventMessage()));
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void moveWumpusCalledAfterSenseDangerAfterUserInput() throws IOException {
		this.args = new String[] { "100", "0", "0", "1" };
		System.setIn(new FileInputStream("DriverTest14.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String wumpusMoveMessage = appendLines("", 1);
		if (!wumpusMoveMessage.equals(Events.WUMPUS_MOVE.getEventMessage())) {
			// a 100 cavern map is used to maximize the odds that the wumpus
			// doesn't spawn next to the player and that the wumpus does move
			// rather than staying still due to trying to move into a wall;
			// however, sometimes you will get unlucky
			System.in.close();
			this.outputReader.close();
			this.fos.close();
			this.fos = new FileOutputStream(this.outputFile);
			System.setOut(new PrintStream(this.fos));
			// ^ these lines reset the System.in and System.out buffers,
			// required because cleanup() will not be called before the next
			// line
			moveWumpusCalledAfterSenseDangerAfterUserInput();
			return;
		}
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void gameEndsAfterKillingWumpus() throws IOException {
		this.args = new String[] { "2", "0", "0", "1" };
		System.setIn(new FileInputStream("DriverTest15.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String senseDangerWumpusMessage = appendLines("", 1);
		assertTrue(senseDangerWumpusMessage.equals(Events.SENSE_DANGER_WUMPUS.getEventMessage()));
		String arrowShotNorthMessage = appendLines("", 1);
		assertTrue(arrowShotNorthMessage.equals(Events.ARROW_SHOT.getEventMessage() + Events.NORTH.getEventMessage()));
		String dieOrWinMessage = appendLines("", 1);
		if (!dieOrWinMessage.equals(Events.USER_KILL_WUMPUS.getEventMessage())) {
			// we only have a 1 in 4 chance of the wumpus's cave spawning north
			// so this will run a few times but c'est la vie. if it only runs
			// once it won't test death by arrow, so that will be tested
			// separately as well
			assertTrue(dieOrWinMessage.equals(Events.ARROW_DEATH.getEventMessage()));
			// the rest of the time ^ this should happen, meaning you died
			String gameOverMessage = appendLines("", 1);
			assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
			assertTrue(this.outputReader.readLine() == null);
			System.in.close();
			this.outputReader.close();
			this.fos.close();
			this.fos = new FileOutputStream(this.outputFile);
			System.setOut(new PrintStream(this.fos));
			// ^ these lines reset the System.in and System.out buffers,
			// required because cleanup() will not be called before the next
			// line
			moveWumpusCalledAfterSenseDangerAfterUserInput();
			return;
		}
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void gameEndsAfterDeathFromArrow() throws IOException {
		this.args = new String[] { "1", "0", "0", "0" };
		System.setIn(new FileInputStream("DriverTest16.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String arrowShotNorthMessage = appendLines("", 1);
		assertTrue(arrowShotNorthMessage.equals(Events.ARROW_SHOT.getEventMessage() + Events.NORTH.getEventMessage()));
		String arrowDeathMessage = appendLines("", 1);
		assertTrue(arrowDeathMessage.equals(Events.ARROW_DEATH.getEventMessage()));
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void gameEndsAfterDeathFromPit() throws IOException {
		this.args = new String[] { "2", "0", "1", "0" };
		System.setIn(new FileInputStream("DriverTest17.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String senseDangerWumpusMessage = appendLines("", 1);
		assertTrue(senseDangerWumpusMessage.equals(Events.SENSE_DANGER_PIT.getEventMessage()));
		String moveOrDieMessage = appendLines("", 1);
		// DriverTest17.txt contains all four inputs in the w a s d order. we'll
		// run through the while loop rather than working to confirm the
		// movement direction because that functionality is tested elsewhere
		while (!moveOrDieMessage.equals(Events.PIT_DEATH.getEventMessage())) {
			moveOrDieMessage = appendLines("", 1);
			if (moveOrDieMessage == null) {
				assertTrue(false);
				// if this assert happens, something has gone seriously wrong,
				// because you spawned a pit next to the users cavern, moved in
				// all 4 directions, then didn't die
			}
		}
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void gameEndsAfterDeathFromWumpus() throws IOException {
		this.args = new String[] { "2", "0", "0", "1" };
		System.setIn(new FileInputStream("DriverTest17.txt"));
		// we can use the same test input here; these tests should always be the
		// same
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String senseDangerWumpusMessage = appendLines("", 1);
		assertTrue(senseDangerWumpusMessage.equals(Events.SENSE_DANGER_WUMPUS.getEventMessage()));
		String moveOrDieMessage = appendLines("", 1);
		// DriverTest17.txt contains all four inputs in the w a s d order. we'll
		// run through the while loop rather than working to confirm the
		// movement direction because that functionality is tested elsewhere
		while (!moveOrDieMessage.equals(Events.WUMPUS_DEATH.getEventMessage())) {
			moveOrDieMessage = appendLines("", 1);
			if (moveOrDieMessage == null) {
				assertTrue(false);
				// if this assert happens, something has gone seriously wrong,
				// because you spawned a wumpus next to the users cavern, moved
				// in all 4 directions, then didn't die
			}
		}
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	private String appendLines(String string, int lineNumber) throws IOException {
		String output = string;
		for (int i = 0; i < lineNumber; i++) {
			output = appendLine(output);
		}
		return output;
	}

	private String appendLine(String string) throws IOException {
		String append = this.outputReader.readLine();
		if (append == null) {
			return null;
		}
		String newString = string + "\n" + append;
		return newString;
	}

}
