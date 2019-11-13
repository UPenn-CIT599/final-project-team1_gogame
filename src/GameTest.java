import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.time.format.*;
import org.junit.jupiter.api.*;

/**
 * The GameTest class contains unit tests for the elements of the Game class
 * that can be tested without user input.
 * 
 * @author Chris Hartung
 *
 */
class GameTest {
    
    @Test
    void testGetGui() {
	// testing getGui getter
	UserInterface gui = new UserInterface();
	Game game = new Game(gui, new MainMenu(null));
	assertEquals(game.getGui(), gui);
    }
    
    @Test
    void testBlackToMove() {
	// testing blackToMove getter
	Game game = new Game(null, new MainMenu(null));
	// black moves first
	assertTrue(game.blackToMove());
	
	// after black moves it is white's turn
	game.nextPlayersTurn();
	assertFalse(game.blackToMove());
    }

    @Test
    void testGetNumRows() {
	// testing getNumRows getter
	Game game = new Game(null, new MainMenu(null));
	// default is 19x19 board
	assertEquals(game.getNumRows(), 19);
    }

    @Test
    void testGetHandicap() {
	// testing getHandicap getter
	Game game = new Game(null, new MainMenu(null));
	
	// default is 0
	assertEquals(game.getHandicap(), 0);
    }

    @Test
    void testGetHandicapCounter() {
	// testing getHandicapCounter getter
	Game game = new Game(null, new MainMenu(null));
	
	// default is 0
	assertEquals(game.getHandicapCounter(), 0);
	
	// testing decrementHandicapCounter method
	game.decrementHandicapCounter();
	assertEquals(game.getHandicapCounter(), -1);
    }

    @Test
    void testGetKomi() {
	// testing getKomi getter
	Game game = new Game(null, new MainMenu(null));
	
	// default is 6.5
	assertEquals(game.getKomi(), 6.5);
    }

    @Test
    void testIsPlayer1Black() {
	// testing isPlayer1Black getter
	Game game = new Game(null, new MainMenu(null));
	
	// default is true
	assertTrue(game.isPlayer1Black());
    }

    @Test
    void testWasLastMovePass() {
	// testing lastMoveWasPass getter and setter
	UserInterface gui = new UserInterface();
	Game game = new Game(gui, new MainMenu(null));
	
	// initial value should be false
	assertFalse(game.wasLastMovePass());
	
	// should be true once someone passes
	game.getPlayer1().pass();
	assertTrue(game.wasLastMovePass());
	
	// verify setter works
	game.setLastMoveWasPass(false);
	assertFalse(game.wasLastMovePass());
    }

    @Test
    void testGetResignedPlayer() {
	// testing resignedPlayer getter and setter
	Game game = new Game(null, new MainMenu(null));
	
	// initial value should be null
	assertNull(game.getResignedPlayer());
	
	// should be the player's name when a player resigns
	game.setResignedPlayer("Bob");
	assertEquals(game.getResignedPlayer(), "Bob");
    }

    @Test
    void testIsGameOver() {
	// testing isGameOver getter
	Game game = new Game(null, new MainMenu(null));

	// initial value should be false
	assertFalse(game.isGameOver());
    }

    @Test
    void testIsSelectingDeadStones() {
	// testing isSelectingDeadStones getter
	Game game = new Game(new UserInterface(), new MainMenu(null));
	
	// initial value should be false
	assertFalse(game.isSelectingDeadStones());
    }

    @Test
    void testGetSelector() {
	// testing getSelector getter
	Game game = new Game(null, new MainMenu(null));

	// initial value should be null
	assertNull(game.getSelector());
    }

    @Test
    void testGetFinalMoveColor() {
	// testing finalMoveColor getter and setter
	Game game = new Game(null, new MainMenu(null));

	// initial value should be null
	assertNull(game.getFinalMoveColor());
	
	// verify setter works
	game.setFinalMoveColor("black");
	assertEquals(game.getFinalMoveColor(), "black");
    }

    @Test
    void testGetFinalScore() {
	// testing getFinalScore getter
	Game game = new Game(null, new MainMenu(null));

	// initial value should be null
	assertNull(game.getFinalScore());
    }

    @Test
    void testGetSgfStringBuilder() {
	// testing sgf StringBuilder methods
	Game game = new Game(new UserInterface(), new MainMenu(null));

	// StringBuilder is initialized in constructor
	
	// add stones
	game.updateStringBuilder('B', 0, 1);
	game.updateStringBuilder('W', 19, 19);
	game.updateStringBuilder('B', 19, 19);
	game.sgfRemovePasses();
	game.updateStringBuilder('W', 2, 3);
	game.updateStringBuilder('B', 4, 5);
	game.updateStringBuilder('W', 2, 3);
	game.updateStringBuilder('B', 4, 5);
	game.updateStringBuilder('W', 2, 3);
	game.updateStringBuilder('B', 4, 5);
	game.updateStringBuilder('W', 2, 3);
	game.updateStringBuilder('B', 4, 5);
	game.updateStringBuilder('W', 2, 3);
	game.updateStringBuilder('B', 4, 5);
	game.updateStringBuilder('W', 2, 3);
	game.updateStringBuilder('B', 19, 19);
	game.updateStringBuilder('W', 19, 19);
	game.sgfRemovePasses();
	game.updateStringBuilder('B', 4, 5);
	game.updateStringBuilder('W', 2, 3);
	game.updateStringBuilder('B', 4, 5);
	
	// verify that StringBuilder was written properly
	DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
	LocalDateTime currentDate = LocalDateTime.now();
	String expectedSgf = "(;FF[4]GM[1]SZ[19]KM[6.5]RE[%]PB[Player 1]" +
		"PW[Player 2]RU[Chinese]DT[" +
		dateFormatter.format(currentDate) +
		"]\n;B[ab];W[cd];B[ef];W[cd];B[ef];W[cd];B[ef];W[cd];B[ef]" +
		";W[cd];B[ef];W[cd];B[ef]\n;W[cd];B[ef]";
	assertEquals(game.getSgfStringBuilder().toString(), expectedSgf);
    }

    @Test
    void testNextPlayersTurn() {
	// testing nextPlayersTurn method
	Game game = new Game(null, new MainMenu(null));
	assertTrue(game.blackToMove());
	game.nextPlayersTurn();
	assertFalse(game.blackToMove());
	game.nextPlayersTurn();
	assertTrue(game.blackToMove());
    }

}
