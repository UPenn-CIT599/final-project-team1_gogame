import java.util.*;

/**
 * The AbsractGame class represents a game of Go that can be displayed by a
 * UserInterface and receive user input from the same UserInterface.
 * 
 * @author Chris Hartung
 *
 */
public abstract class AbstractGame {
    // some instance variables may not be needed by all subclasses
    protected UserInterface gui;
    protected Board board;
    protected boolean blackToMove;
    protected int numRows;
    protected int handicap;
    protected int handicapCounter;
    protected double komi;
    protected boolean lastMoveWasPass;
    protected String finalMoveColor;
    protected boolean gameOver;
    protected String resignedPlayer; // the name of the player who resigned, or
				     // null if no one has resigned
    protected String timedOutPlayer; // the name of the player who timed out,
				     // or null if no one has timed out
    protected Score scorekeeper;
    protected boolean selectingDeadStones = false;
    protected DeadStoneSelector selector;
    protected HashMap<String, Double> finalScore;
    protected GameTimer timer;
    protected StringBuilder sgfStringBuilder = new StringBuilder();
    protected static final int lineLengthLimit = 80; // maximum line length for
						     // output sgf file
    protected int lineLength = 0; // the length of the current line in
				  // sgfStringBuilder

    /**
     * @return the gui
     */
    public UserInterface getGui() {
	return gui;
    }

    /**
     * @return the board
     */
    public Board getBoard() {
	return board;
    }

    /**
     * @return blackToMove
     */
    public boolean blackToMove() {
	return blackToMove;
    }

    /**
     * @return the numRows
     */
    public int getNumRows() {
	return numRows;
    }

    /**
     * @return the handicap
     */
    public int getHandicap() {
	return handicap;
    }

    /**
     * @return the handicapCounter
     */
    public int getHandicapCounter() {
	return handicapCounter;
    }

    /**
     * @return the komi
     */
    public double getKomi() {
	return komi;
    }

    /**
     * @return lastMoveWasPass
     */
    public boolean wasLastMovePass() {
	return lastMoveWasPass;
    }

    /**
     * @return the resignedPlayer
     */
    public String getResignedPlayer() {
	return resignedPlayer;
    }

    /**
     * @param resignedPlayer the resignedPlayer to set
     */
    public void setResignedPlayer(String resignedPlayer) {
	this.resignedPlayer = resignedPlayer;
    }

    /**
     * @return the timedOutPlayer
     */
    public String getTimedOutPlayer() {
	return timedOutPlayer;
    }

    /**
     * @param timedOutPlayer the timedOutPlayer to set
     */
    public void setTimedOutPlayer(String timedOutPlayer) {
	this.timedOutPlayer = timedOutPlayer;
    }

    /**
     * @param lastMoveWasPass the lastMoveWasPass to set
     */
    public void setLastMoveWasPass(boolean lastMoveWasPass) {
	this.lastMoveWasPass = lastMoveWasPass;
    }

    /**
     * @return gameOver
     */
    public boolean isGameOver() {
	return gameOver;
    }

    /**
     * @return selectingDeadStones
     */
    public boolean isSelectingDeadStones() {
	return selectingDeadStones;
    }

    /**
     * @return the selector
     */
    public DeadStoneSelector getSelector() {
	return selector;
    }

    /**
     * @return the finalMoveColor
     */
    public String getFinalMoveColor() {
	return finalMoveColor;
    }

    /**
     * @param finalMoveColor the finalMoveColor to set
     */
    public void setFinalMoveColor(String finalMoveColor) {
	this.finalMoveColor = finalMoveColor;
    }

    /**
     * @return the finalScore
     */
    public HashMap<String, Double> getFinalScore() {
	return finalScore;
    }
    
    /**
     * @return the timer
     */
    public GameTimer getTimer() {
	return timer;
    }

    /**
     * @return the sgfStringBuilder
     */
    public StringBuilder getSgfStringBuilder() {
	return sgfStringBuilder;
    }

    /**
     * This method ends the game.
     */
    public abstract void gameOver();

    /**
     * This method finalizes the score and triggers the GUI's gameOver method.
     */
    public void finalizeScore() {
	if (selectingDeadStones) {
	    HashSet<DeadStone> deadStones = scorekeeper
		    .getDeadStones(selector.deadStoneHashSet());
	    sgfRemoveDeadStones(deadStones);
	    scorekeeper.removeDeadStones(deadStones);
	    selectionPhaseOver();
	}
	scorekeeper.checkAreaOwnership();
	int sekiCount = scorekeeper.checkSeki();
	finalScore = scorekeeper.scoring(finalMoveColor, sekiCount);
	if (sgfStringBuilder != null) {
	    sgfStringBuilder.append(")");
	}
	gui.gameOver();
	updateSgfResult();
    }

    /**
     * This method indicates that the players are done selecting dead stones.
     */
    public void selectionPhaseOver() {
	selectingDeadStones = false;
	if (!gameOver) {
	    selector = null;
	}
    }

    /**
     * This method switches which player is to move next.
     */
    public void nextPlayersTurn() {
	blackToMove = !blackToMove;
    }

    /**
     * This method reduces the handicap counter by one.
     */
    public void decrementHandicapCounter() {
	handicapCounter--;
    }

    /**
     * This method returns a String indicating, in the format used by sgf files,
     * where a stone was placed or removed.
     * 
     * @param x The column where the stone was placed/removed
     * @param y The row where the stone was placed/removed
     * @return The location where the stone was placed/removed, in the format
     *         used by sgf files
     */
    public String sgfLocationString(int x, int y) {
	// 97 is added to x and y because ASCII values for lowercase letters
	// start at 97
	return "[" + ((char) (x + 97)) + ((char) (y + 97)) + "]";
    }

    /**
     * This method adds characters representing the indicated move to the
     * sgfStringBuilder.
     * 
     * @param color Either 'B' if black moved or 'W' if white moved
     * @param x     The column that was moved on, or 19 to indicate a pass
     * @param y     The row that was moved on, or 19 to indicate a pass
     */
    public void updateStringBuilder(char color, int x, int y) {
	if ((x >= 0) && (x <= 19)) {
	    // start a new line if necessary
	    if (lineLength > (lineLengthLimit - 6)) {
		sgfStringBuilder.append("\n");
		lineLength = 0;
	    }

	    if (handicapCounter == 0) {
		sgfStringBuilder.append(";" + color);
		lineLength += 2;
	    }
	    sgfStringBuilder.append(sgfLocationString(x, y));
	    lineLength += 4;

	    // start a new line after the last handicap stone is placed
	    if (handicapCounter == 1) {
		sgfStringBuilder.append("\n");
		lineLength = 0;
	    }
	}
    }

    /**
     * This method indicates in the sgf file which stones were removed as dead
     * 
     * @param deadStones The set of dead stones that were removed
     */
    public void sgfRemoveDeadStones(HashSet<DeadStone> deadStones) {
	if (deadStones.size() > 0) {
	    sgfStringBuilder.append(
		    "\nC[The following stones were considered dead and removed:]AE");
	    for (DeadStone deadStone : deadStones) {
		sgfStringBuilder.append(sgfLocationString(
			deadStone.getxPosition(), deadStone.getyPosition()));
	    }
	}
    }

    /**
     * This method updates the results tag in the sgfStringBuilder.
     */
    public void updateSgfResult() {
	// get the String representing the result
	char winner = gui.getEndGameMenu().getWinnerColor();
	double scoreDifferential = gui.getEndGameMenu().getScoreDifferential();
	String results = "";
	if (resignedPlayer != null) {
	    results = winner + "+R";
	} else if (timedOutPlayer != null) {
	    results = winner + "+T";
	} else if (winner == '0') {
	    results = "0";
	} else {
	    results = winner + "+" +
		    EndGameMenu.SCORE_FORMAT.format(scoreDifferential);
	}

	// replace the % that was used as a placeholder with the result String
	int location = sgfStringBuilder.indexOf("%");
	sgfStringBuilder.replace(location, location + 1, results);
    }

    /**
     * This method is used to remove the record of two consecutive passes from
     * the sgfStringBuilder when those passes do not end the game.
     */
    public void sgfRemovePasses() {
	// since each move adds 6 characters, removing two passes is the same as
	// removing the last 12 characters, or 13 if there is a line break
	int length = sgfStringBuilder.length();
	int charsToRemove = 12;
	if (sgfStringBuilder.lastIndexOf("\n") > (length - charsToRemove)) {
	    charsToRemove++;
	}
	sgfStringBuilder.delete(length - charsToRemove, length);

	// correct current line length
	lineLength = sgfStringBuilder.length() -
		sgfStringBuilder.lastIndexOf("\n");
    }

    /**
     * This method processes a mouse click on the column and row given as
     * inputs.
     * 
     * @param x The column which was clicked
     * @param y The row which was clicked
     */
    public abstract void processMouseClick(int x, int y);

    /**
     * This method processes a mouse click on the given button.
     * 
     * @param buttonID An integer identifying which button was clicked
     */
    public abstract void processMouseClick(int buttonID);
    
    /**
     * 
     * @return
     */
    public String getAnnotation() {
    	return board.getAnnotation();
    }
}
