import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.JOptionPane;

/**
 * The Game class represents a game of Go.
 * 
 * @author Chris Hartung
 *
 */
public class Game implements GameViewer {
    private UserInterface gui;
    private Board board; //TODO
    //private TestBoard board; // TODO
    private Player player1;
    private Player player2;
    private boolean blackToMove;
    private int numRows;
    private int handicap;
    private int handicapCounter;
    private double komi;
    private boolean onePlayerGame;
    private boolean isPlayer1Black;
    private boolean lastMoveWasPass;
    private String finalMoveColor;
    private boolean gameOver;
    private String resignedPlayer; // the name of the player who resigned, or
				   // null if no one has resigned
    private Score scorekeeper;
    private boolean selectingDeadStones = false;
    private DeadStoneSelector selector;
    private HashMap<String, Double> finalScore;
    private StringBuilder sgfStringBuilder = new StringBuilder();
    private static final int lineLengthLimit = 80; // maximum line length for
						   // output sgf file
    private int lineLength = 0; // the length of the current line in
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
    public Board getBoard() { // TODO
	return board;
    }

//    /**
//     * @return the board
//     */
//    public TestBoard getBoard() { // TODO
//	return board;
//    }

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
     * @return isPLayer1Black
     */
    public boolean isPlayer1Black() {
	return isPlayer1Black;
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
     * @param resignedPlayer the resignedPlayer to set
     */
    public void setResignedPlayer(String resignedPlayer) {
	this.resignedPlayer = resignedPlayer;
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
     * @return the sgfStringBuilder
     */
    public StringBuilder getSgfStringBuilder() {
	return sgfStringBuilder;
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
	scorekeeper = new Score(board);
	scorekeeper.categorizePoints();
	if (!scorekeeper.checkIfStonesArePlaced() && (resignedPlayer == null)) {
	    gameOver = false;
	    gui.invalidMove("Board is empty - please place a stone.");
	    lastMoveWasPass = false;
	    sgfRemovePasses();
	    gui.drawBoard();
//	    JOptionPane.showMessageDialog(gui.getFrame(),
//	            "No stone has been placed. Please place a stone.");
	} else {
	    gameOver = true;
	    if (!onePlayerGame && (resignedPlayer == null)) {
	        selectingDeadStones = true;
//	        gui.selectDeadStones();
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
	//scorekeeper.combineEmptyLocations();
	scorekeeper.checkAreaOwnership();
	//scorekeeper.fillNeutralPositions(finalMoveColor);
	int sekiCount = scorekeeper.checkSeki();
	finalScore = scorekeeper.scoring(finalMoveColor, sekiCount);
	sgfStringBuilder.append(")");
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
     * This method writes game data to the sgfStringBuilder
     */
    public void initializeSgfStringBuilder() {
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
     * This method returns a String indicating, in the format used by sgf files,
     * where a stone was placed or removed
     * 
     * @param x The column where the stone was placed/removed
     * @param y The row where the stone was placed/removed
     * @return The location where the stone was placed/removed, in the format
     *         used by sgf files
     */
    public String sgfLocationString(int x, int y) {
	return "[" + ((char) (x + 97)) + ((char) (y + 97)) + "]";
    }

    /**
     * This method adds characters representing the indicated move to the
     * sgfStringBuilder
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
	    // ASCII values for lower case letters start at 97
	    sgfStringBuilder
		    .append(sgfLocationString(x, y));
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
	char winner = gui.getEndGameMenu().getWinnerColor();
	double scoreDifferential = gui.getEndGameMenu().getScoreDifferential();
	String results = "";
	if (resignedPlayer != null) {
	    results = winner + "+R";
	} else if (winner == '0') {
	    results = "0";
	} else {
	    results = winner + "+" +
		    EndGameMenu.SCORE_FORMAT.format(scoreDifferential);
	}
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
    public void processMouseClick(int x, int y) {
	boolean selectionPhase = selectingDeadStones;
	if (!gameOver || selectionPhase) {
	    try {
		boolean player1Moved = player1.processMouseClick(x, y);
		boolean player2Moved = player2.processMouseClick(x, y);
		if ((player1Moved || player2Moved) && !selectionPhase) {
		    if ((player1Moved && isPlayer1Black) ||
			    (player2Moved && !isPlayer1Black)) {
			updateStringBuilder('B', x, y);
		    } else {
			updateStringBuilder('W', x, y);
		    }
		    if (handicapCounter > 1) {
			decrementHandicapCounter();
			gui.handicapMessage();
		    } else if (handicapCounter == 1) {
			decrementHandicapCounter();
			nextPlayersTurn();
		    } else {
//			if ((player1Moved && isPlayer1Black) ||
//				(player2Moved && !isPlayer1Black)) {
//			    updateStringBuilder('B', x, y);
//			    System.out.println("b");
//			} else {
//			    updateStringBuilder('W', x, y);
//			    System.out.println("w");
//			}
			nextPlayersTurn();
		    }
		    gui.drawBoard();
		}
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
	board = new Board(numRows); // TODO
//	board = new Board(numRows); // TODO
	player1 = new Player(this, player1Name, isPlayer1Black, false);
	player2 = new Player(this, player2Name, !isPlayer1Black, onePlayerGame);
	lastMoveWasPass = false;
	gameOver = false;
	initializeSgfStringBuilder();
    }

}
