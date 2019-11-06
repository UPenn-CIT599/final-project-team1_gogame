import java.awt.*;

/**
 * The Player class represents one of the players in a game of Go.
 * 
 * @author Chris Hartung
 *
 */
public class Player {
    private Game game;
    private String name;
    private Color color;
    private boolean isBlack;
    private boolean isComputer;
    private Object notifier;
    private static int computerMoveDelay = 1000; // the number of milliseconds a
						 // computer player will wait to
						 // move
    private static int maxAttempts = 20; // a computer player will pass after
					 // attempting this many illegal moves

    public static final int PASS = 8675309;

    public String getName() {
	return name;
    }

    /**
     * This method creates a Player with the given variables.
     * 
     * @param game       The Game this Player is playing
     * @param name       The Player's name
     * @param isBlack    True if the Player is playing black and false if they
     *                   are playing white
     * @param isComputer True if the Player is a computer and false if they are
     *                   human
     */
    public Player(Game game, String name, boolean isBlack, boolean isComputer) {
	this.game = game;
	this.name = name;
	this.isBlack = isBlack;
	this.isComputer = isComputer;
	if (isBlack) {
	    this.color = Color.BLACK;
	} else {
	    this.color = Color.WHITE;
	}
	if (isComputer) {
	    initializeComputer();
	}
    }

    /**
     * This method creates and runs a new Thread governing computer actions.
     */
    private void initializeComputer() {
	// create an Object which will be used to notify the computer when it is
	// their turn
	notifier = new Object();

	// create a new Thread so human and computer players can wait on each
	// other's actions
	Thread computerThread = new Thread() {
	    public void run() {
		while (!game.isGameOver()) {
		    while (isBlack != game.blackToMove()) {
			synchronized (notifier) {
			    try {
				// wait until it's this player's turn
				notifier.wait();
			    } catch (InterruptedException e) {
				e.printStackTrace();
			    }
			}
		    }

		    // check whether the game is over
		    if (game.isGameOver()) {
			return;
		    }
		    try {
			// wait a set amount of time before moving
			Thread.sleep(computerMoveDelay);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		    // pass when doing so will end the game (for testing
		    // purposes) TODO
		    if (game.wasLastMovePass()) {
			pass();
			game.nextPlayersTurn();
		    } else {
			boolean movedYet = false;
			int count = 0;
			// try to place a stone up to the maximum number of
			// times
			while (!movedYet && (count < maxAttempts)) {
			    // attempt to place a stone in a random location
			    try {
				int x = (int) (Math.random() *
					game.getNumRows());
				int y = (int) (Math.random() *
					game.getNumRows());
				game.getBoard().placeStone(color, x, y);
				if (game.getHandicapCounter() <= 0) {
				    game.getGui().setMessage(name, false);
				    game.nextPlayersTurn();
				} else if (game.getHandicapCounter() == 1) {
				    game.decrementHandicapCounter();
				    game.getGui().setMessage(name, false);
				    game.nextPlayersTurn();
				}
				else {
				    game.decrementHandicapCounter();
				    game.getGui().handicapMessage();
				}
				game.setLastMoveWasPass(false);
				movedYet = true;
			    }
			    catch (IllegalArgumentException e) {
				// if handicap stones haven't all been placed,
				// don't increase the counter in order to
				// prevent passing
				if (game.getHandicapCounter() <= 0) {
				    count++;
				}
			    }
			}
			// pass if the maximum number of attempts was reached
			// and all attempts were unsuccessful
			if (count == maxAttempts) {
			    pass();
			    game.nextPlayersTurn();
			}
		    }
		    game.getGui().drawBoard();
		}
	    }
	};
	computerThread.start();
    }

    /**
     * This method indicates that the Player is passing.
     */
    private void pass() {
//	game.getBoard().pass(); // TODO
	if (game.wasLastMovePass()) {
	    String color = "white";
	    if (isBlack) {
		color = "black";
	    }
	    game.setFinalMoveColor(color);
	    game.gameOver();
	} else {
	    game.setLastMoveWasPass(true);
	    game.getGui().setMessage(name, true);
	}
    }

    /**
     * This method processes a mouse click on the column and row given as inputs
     * and returns true if the Player either played or passed and false
     * otherwise.
     * 
     * @param x The column which was clicked
     * @param y The row which was clicked
     * @return True if the mouse click results in the Player either playing a
     *         stone or passing, and false if the Player does nothing
     */
    public boolean processMouseClick(int x, int y)
	    throws IllegalArgumentException {
	if (isComputer || (isBlack != game.blackToMove())) {
	    return false;
	} else if (x == PASS) {
	    if (game.getHandicapCounter() > 0) {
		throw new IllegalArgumentException(
			"Place all handicap stones before passing.");
	    }
	    pass();
	    return true;
	} else if (game.isSelectingDeadStones()) {
	   game.getSelector().selectStone(x, y); 
	   return true;
	} else {
	    game.getBoard().placeStone(color, x, y);
	    game.setLastMoveWasPass(false);
	    game.getGui().setMessage(name, false);
	    return true;
	}
    }

    /**
     * This method is used to notify a computer player that it is their turn.
     */
    public void notifyComputer() {
	synchronized (notifier) {
	    notifier.notify();
	}
    }

}
