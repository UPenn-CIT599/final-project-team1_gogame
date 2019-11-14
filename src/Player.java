import java.awt.*;
import java.util.HashMap;

import javax.swing.JOptionPane;

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
    private String colorString;
    private boolean isComputer;
    private Object notifier;
    private static int computerMoveDelay = 1000; // the number of milliseconds a
						 // computer player will wait to
						 // move
    private static int maxAttempts = 20; // a computer player will pass after
					 // attempting this many illegal moves

    /**
     * Used to indicate that the Pass button has been clicked.
     */
    public static final int PASS = -1;
    /**
     * Used to indicate that the Resign button has been clicked.
     */
    public static final int RESIGN = -2;

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
	    this.colorString = "black";
	} else {
	    this.color = Color.WHITE;
	    this.colorString = "white";
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
		    
		    // if the last move was pass, computer could end the game by
		    // passing
		    if (game.wasLastMovePass()) {
			// check what the final score would be if the current
			// position were the final position
			Score scorekeeper = new Score(game.getBoard());
			scorekeeper.categorizePoints();
			if (scorekeeper.checkIfStonesArePlaced()) {
			    scorekeeper.checkAreaOwnership();
			    int sekiCount = scorekeeper.checkSeki();
			    HashMap<String, Double> score = scorekeeper
				    .scoring(colorString, sekiCount);
			    double blackScore = score.get("blackScore");
			    double whiteScore = score.get("whiteScore");
			    boolean blackWins = (blackScore > (whiteScore +
				    game.getKomi()));
			    
			    // pass if doing so would win the game
			    if (blackWins == isBlack) {
				pass();
				game.nextPlayersTurn();
				return;
			    }
			}			
		    } 
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
			    game.updateStringBuilder(
				    colorString.toUpperCase().charAt(0), x, y);
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

		    game.getGui().drawBoard();
		}
	    }
	};
	computerThread.start();
    }

    /**
     * This method indicates that the Player is passing.
     */
    public void pass() {
	game.getBoard().pass();
	game.updateStringBuilder(colorString.toUpperCase().charAt(0), 19, 19);

	// end the game if this is the second consecutive pass, otherwise
	// display a message stating that this Player passed
	if (game.wasLastMovePass()) {
	    game.setFinalMoveColor(colorString);
	    game.gameOver();
	} else {
	    game.setLastMoveWasPass(true);
	    game.getGui().setMessage(name, true);
	}
    }
    
    /**
     * This method indicates that the Player is resigning.
     */
    public void resign() {
	// determine which Player was the last to place a stone and update
	// finalMoveColor accordingly
	if ((isBlack && game.wasLastMovePass()) ||
		(!isBlack && !game.wasLastMovePass())) {
	    game.setFinalMoveColor("black");
	} else {
	    game.setFinalMoveColor("white");
	}
	
	// state that this Player resigned
	game.setResignedPlayer(name);
	
	// end the game
	game.gameOver();
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
	// do nothing if this Player is a computer or if it is not this Player's
	// turn
	if (isComputer || (isBlack != game.blackToMove())) {
	    return false;
	}
	// pass if the pass button was clicked, unless handicap stones still
	// need to be placed
	else if (x == PASS) {
	    if (game.getHandicapCounter() > 0) {
		throw new IllegalArgumentException(
			"Place all handicap stones before passing.");
	    }
	    pass();
	    return true;
	}
	// if the resign button was clicked, ask for confirmation before
	// resigning
	else if (x == RESIGN) {
	    int confirm = JOptionPane.showOptionDialog(game.getGui().getFrame(),
		    name + ", are you sure you want to resign?",
		    "Resign Confirmation", JOptionPane.YES_NO_OPTION,
		    JOptionPane.WARNING_MESSAGE, null, null, null);
	    if (confirm == 0) {
		resign();
		return true;
	    } else {
		return false;
	    }
	}
	// if it is dead stone selection phase, inform the deadStoneSelector
	// which space was clicked
	else if (game.isSelectingDeadStones()) {
	    game.getSelector().selectStone(x, y);
	    return true;
	}
	// if an intersection is clicked during gameplay, attempt to place a
	// stone on that intersection
	else {
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
