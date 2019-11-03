
/**
 * The Game class represents a game of Go.
 * 
 * @author Chris Hartung
 *
 */
public class Game {
    private UserInterface gui;
//    private Board board; //TODO
    private TestBoard board; // TODO
    private Player player1;
    private Player player2;
    private boolean blackToMove;
    private int numRows;
    private int handicap;
    private int handicapCounter;
    private boolean onePlayerGame;
    private boolean isPlayer1Black;
    private boolean lastMoveWasPass;
    private boolean gameOver;

    public UserInterface getGui() {
	return gui;
    }

//    public Board getBoard() { // TODO
//	return board;
//    }

    public TestBoard getBoard() { // TODO
	return board;
    }

    public Player getPlayer1() {
	return player1;
    }

    public Player getPlayer2() {
	return player2;
    }

    public boolean blackToMove() {
	return blackToMove;
    }

    public int getNumRows() {
	return numRows;
    }
    
    public int getHandicap() {
	return handicap;
    }
    
    public int getHandicapCounter() {
	return handicapCounter;
    }

    public boolean isPlayer1Black() {
	return isPlayer1Black;
    }

    public boolean wasLastMovePass() {
	return lastMoveWasPass;
    }

    public void setLastMoveWasPass(boolean lastMoveWasPass) {
	this.lastMoveWasPass = lastMoveWasPass;
    }

    public boolean isGameOver() {
	return gameOver;
    }

    /**
     * This method ends the game.
     */
    public void gameOver() {
	gameOver = true;
	gui.gameOver();
    }

    /**
     * This method switches which player is to move next.
     */
    public void nextPlayersTurn() {
	blackToMove = !blackToMove;
    }
    
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
	if (!gameOver) {
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
		    }
		    else {
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
//	board = new Board(numRows); // TODO
	board = new TestBoard(numRows); // TODO
	player1 = new Player(this, player1Name, isPlayer1Black, false);
	player2 = new Player(this, player2Name, !isPlayer1Black, onePlayerGame);
	lastMoveWasPass = false;
	gameOver = false;
    }

}
