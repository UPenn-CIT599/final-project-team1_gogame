import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.Test;

/**
 * The PlayerTest class tests methods dealing with computer players; methods
 * relating to human players have already been tested in GameTest. Note that
 * creating a computer player also creates a new thread. A NullPointerException
 * will be thrown whenever the new thread calls the drawBoard method, but that
 * exception cannot be caught here since it is not in the main thread.
 * 
 * @author Chris Hartung
 *
 */
class PlayerTest {

    static int countStonesOnBoard(Game game) {
	int count = 0;
	for (int i = 0; i < 19; i++) {
	    for (int j = 0; j < 19; j++) {
		if (game.getBoard().getStoneColor(i, j) != null) {
		    count++;
		}
	    }
	}
	return count;
    }

    @Test
    void testWhiteComputerPlayer() {
	Game game = new Game(new UserInterface(), new MainMenu(null));
	Player computer = new Player(game, "Computer", false, true);

	// since the computer is white, it should get stuck at the wait command
	// and not move
	assertEquals(countStonesOnBoard(game), 0);

	// once it is the computer's turn and the computer is notified, it
	// should wait one second and then move
	game.nextPlayersTurn();
	computer.notifyComputer();
	try {
	    Thread.sleep(1500);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	// computer should place a stone and not end the game
	assertEquals(countStonesOnBoard(game), 1);
	assertFalse(game.isGameOver());
    }

    @Test
    void testBlackComputerPlayer() {
	Game game = new Game(new UserInterface(), new MainMenu(null));
	new Player(game, "Computer", true, true);

	// since it is the computer's turn, it should wait one second and then
	// move
	try {
	    Thread.sleep(1500);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	// computer should place a stone and not end the game
	assertEquals(countStonesOnBoard(game), 1);
	assertFalse(game.isGameOver());
    }

    @Test
    void testComputerPlayerAfterPassWithEmptyBoard() {
	Game game = new Game(new UserInterface(), new MainMenu(null));
	Player computer = new Player(game, "Computer", false, true);

	// since the computer is white, it should get stuck at the wait command
	// and not move
	assertEquals(countStonesOnBoard(game), 0);

	// once it is the computer's turn and the computer is notified, it
	// should wait one second and then move
	// computer won't pass to end the game when the board is empty
	game.nextPlayersTurn();
	game.setLastMoveWasPass(true);
	computer.notifyComputer();
	try {
	    Thread.sleep(1500);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	// computer should place a stone and not end the game
	assertEquals(countStonesOnBoard(game), 1);
	assertFalse(game.isGameOver());
    }

    @Test
    void testComputerPlayerAfterPassWhenPassWins() {
	Game game = new Game(new UserInterface(), new MainMenu(null));
	Player computer = new Player(game, "Computer", false, true);

	// since the computer is white, it should get stuck at the wait command
	// and not move
	assertEquals(countStonesOnBoard(game), 0);

	// once it is the computer's turn and the computer is notified, it
	// should wait one second and then move
	// computer should pass to end the game and win
	game.getBoard().placeStone(Color.BLACK, 0, 1);
	game.getBoard().placeStone(Color.WHITE, 3, 6);
	game.nextPlayersTurn();
	game.setLastMoveWasPass(true);
	computer.notifyComputer();
	try {
	    Thread.sleep(1500);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	// computer should have ended the game by passing
	assertEquals(countStonesOnBoard(game), 2);
	assertTrue(game.isGameOver());
    }

    @Test
    void testComputerPlayerAfterPassWhenPassLoses() {
	Game game = new Game(new UserInterface(), new MainMenu(null));
	Player computer = new Player(game, "Computer", false, true);

	// since the computer is white, it should get stuck at the wait command
	// and not move
	assertEquals(countStonesOnBoard(game), 0);

	// once it is the computer's turn and the computer is notified, it
	// should wait one second and then move
	// computer should place a stone, since passing would result in a loss
	game.getBoard().placeStone(Color.BLACK, 0, 1);
	game.getBoard().placeStone(Color.BLACK, 1, 1);
	game.getBoard().placeStone(Color.BLACK, 2, 1);
	game.getBoard().placeStone(Color.BLACK, 3, 1);
	game.getBoard().placeStone(Color.BLACK, 4, 1);
	game.getBoard().placeStone(Color.BLACK, 5, 1);
	game.getBoard().placeStone(Color.BLACK, 6, 1);
	game.getBoard().placeStone(Color.BLACK, 7, 1);
	game.getBoard().placeStone(Color.BLACK, 8, 1);
	game.getBoard().placeStone(Color.BLACK, 9, 1);
	game.getBoard().placeStone(Color.WHITE, 3, 6);
	game.nextPlayersTurn();
	game.setLastMoveWasPass(true);
	computer.notifyComputer();
	try {
	    Thread.sleep(1500);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	// computer should have placed a stone instead of ending the game
	assertEquals(countStonesOnBoard(game), 12);
	assertFalse(game.isGameOver());
    }

    @Test
    void testComputerPlayerWhenGameEndsWhileWaiting() {
	Game game = new Game(new UserInterface(), new MainMenu(null));
	Player computer = new Player(game, "Computer", false, true);

	// since the computer is white, it should get stuck at the wait command
	// and not move
	assertEquals(countStonesOnBoard(game), 0);

	// make it the computer's turn
	game.nextPlayersTurn();

	// once there is a stone on the board, two passes should end the game

	// the passes will result in a NullPointerException in the main thread
	// when drawBoard is called
	game.getBoard().placeStone(Color.WHITE, 0, 0);
	game.getPlayer1().pass();
	try {
	    game.getPlayer2().pass();
	} catch (NullPointerException n) {
	    assertTrue(game.isGameOver());

	    // once notified, the computer should wake up, recognize that the
	    // game is over, and do nothing
	    computer.notifyComputer();
	    try {
		Thread.sleep(1500);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }

	    // computer should not have placed a stone since the game was
	    // already over
	    assertEquals(countStonesOnBoard(game), 1);
	    assertTrue(game.isGameOver());
	}
    }

    @Test
    void testComputerPlayerWithNoLegalMoves() {
	Game game = new Game(new UserInterface(), new MainMenu(null));
	Player computer = new Player(game, "Computer", false, true);

	// since the computer is white, it should get stuck at the wait command
	// and not move
	assertEquals(countStonesOnBoard(game), 0);

	// place black stones in a checkerboard pattern so white can't legally
	// move anywhere
	for (int i = 0; i < 19; i++) {
	    for (int j = 0; j < 19; j++) {
		if (((i + j) % 2) == 0) {
		    game.getBoard().placeStone(Color.BLACK, i, j);
		}
	    }
	}
	// 181 stones should have been placed
	assertEquals(countStonesOnBoard(game), 181);

	// once it is the computer's turn and the computer is notified, it
	// should wait one second and then attempt to move
	game.nextPlayersTurn();
	computer.notifyComputer();
	try {
	    Thread.sleep(1500);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	// computer should have been forced to pass
	assertEquals(countStonesOnBoard(game), 181);
	assertTrue(game.wasLastMovePass());
	assertFalse(game.isGameOver());
    }

}
