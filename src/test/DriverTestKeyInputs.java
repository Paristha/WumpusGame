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

public class DriverTestKeyInputs {

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
		this.args = new String[] { "1", "0", "0", "0" };
	}

	@After
	public void cleanup() throws IOException {
		System.setOut(this.commandLine);
		this.fos.close();
		this.outputReader.close();
		this.outputFile.delete();
		// comment out the above line to preserve output.txt for testing
	}

	@Test
	public void nKeyClosesProgram() throws IOException {
		System.setIn(new FileInputStream("DriverTest0.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void yKeyStartsGameAndEndEndsGame() throws IOException {
		System.setIn(new FileInputStream("DriverTest1.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void incorrectInputBeforeGameStartSendsErrorInput() throws IOException {
		System.setIn(new FileInputStream("DriverTest2.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String errorInput = appendLines("", 1);
		assertTrue(errorInput.equals(HuntTheWumpus.errorInput));
		// ^ we're only testing one incorrect input as there are infinite
		// incorrect inputs
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void incorrectInputAfterGameStartSendsErrorInput() throws IOException {
		System.setIn(new FileInputStream("DriverTest3.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String errorInput = appendLines("", 1);
		assertTrue(errorInput.equals(HuntTheWumpus.errorInput));
		// ^ we're only testing one incorrect input as there are infinite
		// incorrect inputs
		this.outputReader.readLine();
		// ^ called to skip another newline, errorInput is printed with println
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void testWKeyMovesNorth() throws IOException {
		System.setIn(new FileInputStream("DriverTest4.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String userCantMoveNorthMessage = appendLines("", 1);
		assertTrue(userCantMoveNorthMessage.equals(Events.NO_MOVE.getEventMessage() + Events.NORTH.getEventMessage()));
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void testSKeyMovesSouth() throws IOException {
		System.setIn(new FileInputStream("DriverTest5.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String userCantMoveNorthMessage = appendLines("", 1);
		assertTrue(userCantMoveNorthMessage.equals(Events.NO_MOVE.getEventMessage() + Events.SOUTH.getEventMessage()));
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void testDKeyMovesEast() throws IOException {
		System.setIn(new FileInputStream("DriverTest6.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String userCantMoveNorthMessage = appendLines("", 1);
		assertTrue(userCantMoveNorthMessage.equals(Events.NO_MOVE.getEventMessage() + Events.EAST.getEventMessage()));
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void testAKeyMovesWest() throws IOException {
		System.setIn(new FileInputStream("DriverTest7.txt"));
		HuntTheWumpus.main(this.args);
		this.outputReader = new BufferedReader(new FileReader(this.outputFile));
		this.outputReader.readLine();
		// ^ called to skip first newline, because appendLine puts the newline
		String welcomeMessage = appendLines("", 3);
		// ^ this means Events.WELCOME is 3 lines long
		assertTrue(welcomeMessage.equals(Events.WELCOME.getEventMessage()));
		String rulesMessage = appendLines("", 25);
		assertTrue(rulesMessage.equals(Events.RULES.getEventMessage()));
		String userCantMoveNorthMessage = appendLines("", 1);
		assertTrue(userCantMoveNorthMessage.equals(Events.NO_MOVE.getEventMessage() + Events.WEST.getEventMessage()));
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void testIKeyShootsNorth() throws IOException {
		System.setIn(new FileInputStream("DriverTest8.txt"));
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
	public void testKKeyShootsSouth() throws IOException {
		System.setIn(new FileInputStream("DriverTest9.txt"));
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
		assertTrue(arrowShotNorthMessage.equals(Events.ARROW_SHOT.getEventMessage() + Events.SOUTH.getEventMessage()));
		String arrowDeathMessage = appendLines("", 1);
		assertTrue(arrowDeathMessage.equals(Events.ARROW_DEATH.getEventMessage()));
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void testLKeyShootsEast() throws IOException {
		System.setIn(new FileInputStream("DriverTest10.txt"));
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
		assertTrue(arrowShotNorthMessage.equals(Events.ARROW_SHOT.getEventMessage() + Events.EAST.getEventMessage()));
		String arrowDeathMessage = appendLines("", 1);
		assertTrue(arrowDeathMessage.equals(Events.ARROW_DEATH.getEventMessage()));
		String gameOverMessage = appendLines("", 1);
		assertTrue(gameOverMessage.equals(Events.GAME_OVER.getEventMessage()));
		assertTrue(this.outputReader.readLine() == null);
	}

	@Test
	public void testJKeyShootsWest() throws IOException {
		System.setIn(new FileInputStream("DriverTest11.txt"));
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
		assertTrue(arrowShotNorthMessage.equals(Events.ARROW_SHOT.getEventMessage() + Events.WEST.getEventMessage()));
		String arrowDeathMessage = appendLines("", 1);
		assertTrue(arrowDeathMessage.equals(Events.ARROW_DEATH.getEventMessage()));
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
		String append = string + "\n" + this.outputReader.readLine();
		return append;
	}
}
