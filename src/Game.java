import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

/**
 * The Game class represents a game of Go.
 * 
 * @author Chris Hartung
 *
 */
public class Game extends AbstractGame {
    private Player player1;
    private Player player2;
    private boolean onePlayerGame;
    private boolean isPlayer1Black;

    /**
     * @return player1
     */
    public Player getPlayer1() {
	return player1;
    }

    /**
     * @return player2
     */
    public Player getPlayer2() {
	return player2;
    }

    /**
     * @return isPLayer1Black
     */
    public boolean isPlayer1Black() {
	return isPlayer1Black;
    }

    /**
     * This method returns to game play if players pass consecutively to end the
     * game but can't agree on which stones are dead
     */
    public void continuePlay() {
	gameOver = false;
	lastMoveWasPass = false;
	selectionPhaseOver();
	sgfRemovePasses();
	gui.drawBoard();
    }

    /**
     * This method ends the game.
     */
    public void gameOver() {
	scorekeeper = new Score(board, komi);
	scorekeeper.categorizePoints();
	if (!scorekeeper.checkIfStonesArePlaced() && (resignedPlayer == null) &&
		(timedOutPlayer == null)) {
	    gameOver = false;
	    gui.invalidMove("Board is empty - please place a stone.");
	    lastMoveWasPass = false;
	    sgfRemovePasses();
	    gui.drawBoard();
	} else {
	    gameOver = true;
	    if (!onePlayerGame && (resignedPlayer == null) &&
		    (timedOutPlayer == null)) {
		selectingDeadStones = true;
		selector = new DeadStoneSelector(this);
	        gui.drawBoard();
	        JOptionPane.showMessageDialog(gui.getFrame(), 
		        "Please select all dead stones. Once both players\n" + 
		        "agree on which stones are dead, press the Calculate\n" + 
	                "Score button. If no agreement can be reached, you may\n" + 
		        "Continue Play to resolve the dispute.");
	    } else {
	        finalizeScore();
	    }
	}
    }
        
    /**
     * This method creates and writes game data to the sgfStringBuilder.
     */
    public void initializeSgfStringBuilder() {
	// create the StringBuilder
	sgfStringBuilder = new StringBuilder();
	
	// indicate that this is a Go game with file format 4
	sgfStringBuilder.append("(;FF[4]GM[1]");
	
	// add board size
	sgfStringBuilder.append("SZ[" + numRows + "]");
	
	// add handicap (if nonzero) and komi
	if (handicap > 0) {
	    sgfStringBuilder.append("HA[" + handicap + "]");
	}
	sgfStringBuilder.append("KM[" + komi + "]");
	
	// add area for the result, to be updated later
	sgfStringBuilder.append("RE[%]");

	// add player names
	String blackName, whiteName;
	if (isPlayer1Black) {
	    blackName = player1.getName();
	    whiteName = player2.getName();
	} else {
	    blackName = player2.getName();
	    whiteName = player1.getName();
	}
	sgfStringBuilder.append("PB[" + blackName + "]PW[" +
		whiteName + "]");
	
	// add ruleset
	sgfStringBuilder.append("RU[Chinese]");
	
	// add current date
	DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
	LocalDateTime currentDate = LocalDateTime.now();
	sgfStringBuilder.append("DT[" + dateFormatter.format(currentDate) + "]");
	
	// add a newline before placing stones
	sgfStringBuilder.append("\n");
	
	if (handicap > 0) {
	    // indicate that the handicap stones will be placed on the board
	    // before game start in the output .sgf file
	    sgfStringBuilder.append("AB");
	    lineLength += 2;
	}
    }
    
    /**
     * This method processes a mouse click on the column and row given as
     * inputs.
     * 
     * @param x The column which was clicked
     * @param y The row which was clicked
     */
    public void processMouseClick(int x, int y) {
	// selectionPhase variable is used in case selectingDeadStones changes
	boolean selectionPhase = selectingDeadStones;
	
	// mouse clicks do nothing if game is over and it isn't dead stone
	// selection phase
	if (!gameOver || selectionPhase) {
	    try {
		// both players process the mouse click; nothing happens if it
		// is not their turn or if they are a computer
		boolean player1Moved = player1.processMouseClick(x, y);
		boolean player2Moved = player2.processMouseClick(x, y);

		// no further action is needed if the mouse click had no effect
		// or if it is dead stone selection phase
		if ((player1Moved || player2Moved) && !selectionPhase) {
		    // update sgfStringBuilder if a stone was placed
		    if ((player1Moved && isPlayer1Black) ||
			    (player2Moved && !isPlayer1Black)) {
			updateStringBuilder('B', x, y);
		    } else {
			updateStringBuilder('W', x, y);
		    }
		    
		    // reset byo-yomi timers
		    if (timer != null) {
			timer.resetByoYomi();
		    }
		    // decrement handicap counter if necessary
		    if (handicapCounter > 1) {
			decrementHandicapCounter();
			gui.handicapMessage();
		    } else if (handicapCounter == 1) {
			decrementHandicapCounter();

			// after the last handicap stone is placed, the other
			// player gets to play
			nextPlayersTurn();
		    } else {
			// if no handicap stones need to be placed, once the
			// current Player places a stone or passes, it is the
			// next Player's turn
			nextPlayersTurn();
		    }		    
		}
		gui.drawBoard();

		// once the human player has made a move, the computer player
		// needs to be notified that it is their turn
		if (onePlayerGame) {
		    player2.notifyComputer();
		}
	    } catch (IllegalArgumentException e) {
		gui.invalidMove(e.getMessage());
		gui.drawBoard();
	    }
	}
    }

    /**
     * This method processes a mouse click on the given button.
     * 
     * @param buttonID An integer identifying which button was clicked
     */
    public void processMouseClick(int buttonID) {
	processMouseClick(buttonID, buttonID);
    }
    
    /**
     * This method creates a game.
     * 
     * @param gui  The UserInterface which displays this game
     * @param menu The MainMenu used to initialize this game
     */
    public Game(UserInterface gui, MainMenu menu) {
	this.gui = gui;
	blackToMove = true;
	numRows = menu.getNumRows();
	handicap = menu.getHandicap();
	handicapCounter = handicap;
	komi = menu.getKomi();
	onePlayerGame = menu.isOnePlayerGame();
	String player1ColorString = menu.getPlayer1Color();
	isPlayer1Black = true;
	if (player1ColorString.equals("White") ||
		(player1ColorString.equals("Random") && Math.random() < 0.5)) {
	    isPlayer1Black = false;
	}
	String player1Name = menu.getPlayer1Name();
	String player2Name = menu.getPlayer2Name();
	if (player1Name.equals("")) {
	    player1Name = "Player 1";
	}
	if (onePlayerGame) {
	    player2Name = "Computer";
	} else if (player2Name.equals("")) {
	    player2Name = "Player 2";
	}
	board = new Board(numRows);
	player1 = new Player(this, player1Name, isPlayer1Black, false);
	player2 = new Player(this, player2Name, !isPlayer1Black, onePlayerGame);
	if (menu.isTimed()) {
	    timer = new GameTimer(this, menu);
	}
	lastMoveWasPass = false;
	gameOver = false;
	initializeSgfStringBuilder();
    }

}
