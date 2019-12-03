import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

/**
 * This class represents an instance of Replay Mode, in which a user may play through the moves of an sgf file representing
 * a game of Go. It extends the AbstractGame class.
 * @author morrowch
 *
 */
public class ReplayMode extends AbstractGame {

	private sgfHandler sgf = new sgfHandler();
	private String caption;
	private ReplayGame replayGame;
	private int moveNumber = 0;
	private Boolean offPath = false;

	public ReplayMode(UserInterface gui, MainMenu mainMenu) {
		this.gui = gui;
		File sgfFile = mainMenu.getReplayFile();
		sgf.readSgfFile(sgfFile);
		sgf.constructReplayGame();
		replayGame = sgf.getReplayGame();
		blackToMove = replayGame.getFirstMoveBlack();
		gameOver = false;

		board = replayGame.getBoard();
	}

	public void NextMove() {
		// If offPath, the board needs to be set back to how it was when the player branched off
		if (offPath) {
			offPath = false;
			// Create a new board
			Board board = new Board(replayGame.getBoardSize());
			Move nextMove = new Move();
			try {
				// Re-make all moves made up to the branching point
				for (int i = 0; i < moveNumber; i++) {
					nextMove = replayGame.getMove(i);
					board.placeStone(nextMove);
				}
				blackToMove = (nextMove.getColor().equals(Color.BLACK)) ? false : true;
				this.board = board;
				gui.drawBoard();

			} catch (IllegalArgumentException e) {
				gui.invalidMove("Invalid SGF file: " + e.getMessage());
				gameOver = true;
				gui.drawBoard();
			}
		}
		else if (!gameOver) {
			// If the game isn't over and still on the main line, get the next move
			Move move = replayGame.getMove(moveNumber);
			if (move.getIsLastMove()) {
				gameOver = true;
			} else {
				moveNumber++;
			}
			try {
				// Make the move
				board.placeStone(move);
				nextPlayersTurn();
				gui.drawBoard();
			} catch (IllegalArgumentException e) {
				gui.invalidMove("Invalid SGF file: " + e.getMessage());
				gameOver = true;
				gui.drawBoard();
			}

			if (gameOver) {
				gameOver();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public Boolean isOffPath() {
		return offPath;
	}

	/**
	 * Called when the game is over
	 */
	@Override
	public void gameOver() {
		// Color any selected dead stones based on the 'DS' tag (these are determined in the ReplayGame class)
		selector = new DeadStoneSelector(this);
		for (Intersection deadStoneIntersection : replayGame.getDeadStoneIntersections()) {
			selector.selectStone(deadStoneIntersection.getxPosition(), deadStoneIntersection.getyPosition());
		}
		gui.gameOver();
	}

	/**
	 * Called when the user selects an intersection on the board
	 */
	@Override
	public void processMouseClick(int x, int y) {
		// If the player clicks on an intersection, then make that move, and set offPath to true
		Color color = (blackToMove) ? Color.BLACK : Color.WHITE;
		Move move = new Move(color, x, y);
		try {
			board.placeStone(move);
			offPath = true;
			board.setAnnotation("Branching off move " + moveNumber);
			nextPlayersTurn();
			gui.drawBoard();
		} catch (IllegalArgumentException e) {
			gui.invalidMove(e.getMessage());
			gui.drawBoard();
		}

	}

	@Override
	public void processMouseClick(int buttonID) {
		// Not used in this class

	}

}
