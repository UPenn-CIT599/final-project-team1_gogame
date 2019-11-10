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
    private HashMap<String, Integer> finalScore;

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
    public HashMap<String, Integer> getFinalScore() {
	return finalScore;
    }
    
    /**
     * This method returns to game play if players pass consecutively to end the
     * game but can't agree on which stones are dead
     */
    public void continuePlay() {
	selectionPhaseOver();
	gameOver = false;
	gui.drawBoard();
    }

    /**
     * This method ends the game.
     */
    public void gameOver() {
	gameOver = true;
	scorekeeper = new Score(board);
	scorekeeper.categorizePoints();
	if (!onePlayerGame && (resignedPlayer == null)) {
	    selectingDeadStones = true;
//	    gui.selectDeadStones();
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
    
    /**
     * This method finalizes the score and triggers the GUI's gameOver method.
     */
    public void finalizeScore() {
	if (selectingDeadStones) {
	    scorekeeper.removeDeadStones(
		    scorekeeper.getDeadStones(selector.deadStoneHashSet()));
	    selectionPhaseOver();
	}
	scorekeeper.combineEmptyLocations();
	scorekeeper.checkAreaBlackOrWhite();
	scorekeeper.fillNeutralPositions(finalMoveColor);
	finalScore = scorekeeper.scoring();
	gui.gameOver();
    }
    
    /**
     * This method indicates that the players are done selecting dead stones.
     */
    public void selectionPhaseOver() {
	selectingDeadStones = false;
	selector = null;
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
		if (player1Moved || player2Moved) {
		    if (handicapCounter > 1) {
			decrementHandicapCounter();
			gui.handicapMessage();
		    } else if (handicapCounter == 1) {
			decrementHandicapCounter();
			nextPlayersTurn();
		    } else if (!selectionPhase) {
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
    }

}
