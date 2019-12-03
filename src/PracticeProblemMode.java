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
public class PracticeProblemMode extends AbstractGame {

	private sgfHandler sgf = new sgfHandler();
	private Problem problem;
	private Boolean solverIsBlack;
	private Boolean hasSolution = false; // Indicates whether the problem has a solution
	private Boolean onPath = true; // Indicates whether the solver is on a solution path
	private ProblemSolution solution = null;
	private ArrayList<Move> onPathMoves = null;
	private Move lastMove;
	private int RESPONSE_DELAY = 1000;
	private Object notifier;
	private String caption;
	private String problemTitle;

	/**
	 * Called when a player makes a move. If a solution exists for the problem and the move is on the path of the solution,
	 * the computer makes the appropriate response.
	 */
	@Override
	public void processMouseClick(int x, int y) {
		Color color = (blackToMove) ? Color.BLACK : Color.WHITE;
		// Construct the move based on where the player clicked
		lastMove = new Move(color, x, y);
		// Default the move to being off path until at least one response is found
		onPath = false;
		if (!gameOver) {
			for (Move move : onPathMoves) {
				if (move.equals(lastMove)) {
					// If at least one response was found, then the move is on path
					onPath = true;
					lastMove = move;
					caption = move.getAnnotation();

					// Check if the move is the last in the solution branch
					if (move.getIsLastMove() || move.getResponses().size() == 0) {
						gameOver = true;
						board.setAnnotation(caption);
					}
				}
			}

			// If no response is found for the move, gameOver is set to true
			if (!onPath) {
				caption = "Wrong. Off path.";
				gameOver = true;
			}
		}

		// Make the move the player selected
		try {
			board.placeStone(lastMove);
			nextPlayersTurn();
			board.setAnnotation(caption);
			if (gameOver && onPath) {
				gameOver();
			}
			gui.drawBoard();

			// Check if the move is one the path
			if (hasSolution && onPath && !gameOver) {
				synchronized (notifier) {
					notifier.notify();
				}
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
	public PracticeProblemMode(UserInterface gui, MainMenu mainMenu) {
		this.gui = gui;
		File sgfFile = mainMenu.getReplayFile();
		sgf = new sgfHandler();
		sgf.readSgfFile(sgfFile);
		sgf.constructProblem();
		problem = sgf.getProblem();

		if (problem.getSolution() != null) {
			hasSolution = true;
			solution = problem.getSolution();
			onPathMoves = solution.getResponses();
		}

		// Set which player moves first
		solverIsBlack = (problem.getSolverColor().equals(Color.BLACK)) ? true : false;
		blackToMove = (solverIsBlack) ? true : false;
		problemTitle = problem.getCaption();
		board = problem.getBoard();
		gameOver = false;
		InitializeResponder();

	}

	private void InitializeResponder() {

		notifier = new Object();
		// Start a new thread for the computer to make its responses
		Thread responderThread = new Thread() {
			public void run() {
				// While the player is on the solution tree of the problem
				while (onPath) {
					while (blackToMove == solverIsBlack) {
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
			// Get the first of the possible responses (there will generally be just one)
			Move computerMove = lastMove.getResponses().get(0);
			if (computerMove.getAnnotation().length() > 1)
			{
				caption = computerMove.getAnnotation();
				board.setAnnotation(caption);
			}
			// Make the computer move
			board.placeStone(computerMove);
			// Check for valid responses by the player
			if (computerMove.getResponses().size() > 0) {
				onPathMoves = computerMove.getResponses();
			} else {
				// If there are no valid responses, then the problem is over
				onPath = false;
				gameOver = true;
			}
			nextPlayersTurn();
			gui.drawBoard();
		} catch (IllegalArgumentException e) {
			gui.invalidMove("Invalid SGF file: " + e.getMessage());
			gui.drawBoard();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 

	}
	
	/**
	 * 
	 * @return
	 */
	public String getProblemTitle() {
		return problemTitle;
	}

	/**
	 * Overrides the gameOver AbstractGame method. This simply calls the gameOver method in the gui class
	 */
	@Override
	public void gameOver() {
		gui.gameOver();
	}

	/**
	 * 
	 */
	@Override
	public void processMouseClick(int buttonID) {
		// Not used in PracticeProblemMode
	}

}
