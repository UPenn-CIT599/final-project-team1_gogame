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
		if (offPath) {
			offPath = false;
			//ArrayList<Move> moves = (ArrayList<Move>) replayGame.getMoves().subList(0, moveNumber);
			Board board = new Board(replayGame.getBoardSize());
			Move nextMove = new Move();
			try {
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
			Move move = replayGame.getMove(moveNumber);

			if (move.getIsLastMove()) {
				gameOver = true;
			} else {
				moveNumber++;
			}
			try {
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
		// TODO Auto-generated method stub

	}

}
