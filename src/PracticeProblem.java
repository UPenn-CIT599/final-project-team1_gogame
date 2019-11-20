import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the "Practice Problem" mode, in which a problem sgf file is loaded with a tree of 
 * possible moves and responses. If the player chooses plays that are defined in the tree of the problem,
 * the computer automatically plays the response.
 * @author morrowch
 *
 */
public class PracticeProblem extends AbstractGame {

	private sgfHandler sgf = new sgfHandler();
	private Problem problem;
	private Boolean hasSolution = false; // Indicates whether the problem has a solution
	private Boolean onPath = true; // Indicates whether the solver is on a solution path
	private ProblemSolution solution = null;
	private ArrayList<Move> onPathMoves = null;
	private Move lastMove;
	private int RESPONSE_DELAY = 1000;
	private Object notifier;
	private String caption;
	private Boolean solved = false;

	@Override
	public void gameOver() {
		// Called when the problem is over
	}

	@Override
	public void processMouseClick(int buttonID) {
		// TODO Auto-generated method stub
	}

	/**
	 * Called when a player makes a move. If a solution exists for the problem and the move is on the path of the solution,
	 * the computer makes the appropriate response.
	 */
	@Override
	public void processMouseClick(int x, int y) {
		Color color = (blackToMove) ? Color.BLACK : Color.WHITE;
		lastMove = new Move(color, x, y);
		onPath = false;
		Boolean moveIsOnPath = false;
		if (!gameOver) {
			for (Move move : onPathMoves) {
				if (move.equals(lastMove)) {
					onPath = true;
					moveIsOnPath = true;
					lastMove = move;
					caption = move.getAnnotation();

					if (move.getIsLastMove() || move.getResponses().size() == 0) {
						onPath = false;
						solved = true;
						gameOver = true;
						board.setAnnotation(caption);
					}
				}
			}

			if (!moveIsOnPath) {
				caption = "Wrong. Off path.";
				gameOver = true;
				onPath = false;
			}
		}

		try {
			board.placeStone(lastMove);
			nextPlayersTurn();
			board.setAnnotation(caption);
			gui.drawBoard();

			// Check if the move is one the path
			if (hasSolution && onPath) {
				synchronized (notifier) {
					notifier.notify();
				}
			} else if (hasSolution) {
				// TODO: Let the player no that they're off path
			}
		} catch (IllegalArgumentException e) {
			gui.invalidMove(e.getMessage());
			gui.drawBoard();
		}

	}

	/**
	 * Constructor for the practice problem mode
	 * @param gui
	 * @param mainMenu
	 */
	public PracticeProblem(UserInterface gui, MainMenu mainMenu) {
		this.gui = gui;
		File sgfFile = mainMenu.getReplayFile();
		sgf.readSgfFile(sgfFile);
		sgf.constructProblem();
		problem = sgf.getProblem();

		if (problem.getSolution() != null) {
			hasSolution = true;
			solution = problem.getSolution();
			onPathMoves = solution.getResponses();
		}

		board = problem.getBoard();
		blackToMove = problem.getBlackToMove();
		gameOver = false;

		InitializeResponder();

	}

	private void InitializeResponder() {

		notifier = new Object();

		Thread responderThread = new Thread() {
			public void run() {
				while (onPath) {
					while (blackToMove) {
						synchronized (notifier) {
							try {
								// wait until it's this player's turn
								notifier.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					Respond();
				}
			}
		};
		responderThread.start();
	}

	/**
	 * If a solution was constructed for the given problem, this makes the responses to a players move based on the
	 * solution tree defined in the problem sgf file
	 */
	public void Respond() {
		try {
			Thread.sleep(RESPONSE_DELAY);
			Move computerMove = lastMove.getResponses().get(0);

			if (computerMove.getAnnotation().length() > 1)
			{
				caption = computerMove.getAnnotation();
				board.setAnnotation(caption);
			}
			board.placeStone(computerMove);
			if (computerMove.getResponses().size() > 0) {
				onPathMoves = computerMove.getResponses();
			} else {
				onPath = false;
				solved = false;
				gameOver = true;
			}
			nextPlayersTurn();
			gui.drawBoard();
		} catch (IllegalArgumentException e) {
			gui.invalidMove(e.getMessage());
			gui.drawBoard();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 

	}

	/**
	 * 
	 */
	@Override
	public boolean blackToMove() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 */
	@Override
	public void finalizeScore() {
		// TODO This doesn't apply to Practice Problem mode

	}

}
