import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PracticeProblem implements GameViewer {

	private sgfHandler sgf = new sgfHandler();
	private UserInterface gui;
	private Board board;
	private boolean blackToMove;
	private String finalMoveColor;
	private int numRows;
	private Problem problem;
	private Boolean hasSolution = false; // Indicates whether the problem has a solution
	private Boolean onPath = true; // Indicates whether the solver is on a solution path
	private ProblemSolution solution = null;
	private ArrayList<Move> onPathMoves = null;
	private Move lastMove;
	private int RESPONSE_DELAY = 1000;

	@Override
	public void gameOver() {
		// Called when the problem is over

	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public String getFinalMoveColor() {
		return finalMoveColor;
	}

	@Override
	public HashMap<String, Double> getFinalScore() {
		// This method is not used in Practice Problem mode
		return null;
	}

	@Override
	public UserInterface getGui() {
		return gui;
	}

	@Override
	public double getKomi() {
		// This method is not used in Practice Problem Mode
		return 0;
	}

	@Override
	public int getNumRows() {
		return numRows;
	}

	@Override
	public String getResignedPlayer() {
		// This method is not used in Practice Problem mode
		return null;
	}

	@Override
	public boolean isGameOver() {
		// This method is not used in Practice Problem mode
		return false;
	}

	@Override
	public void processMouseClick(int buttonID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMouseClick(int x, int y) {
		Color color = (blackToMove) ? Color.BLACK : Color.WHITE;
		lastMove = new Move(color, x, y);
		try {
			board.placeStone(color, x, y);
			nextPlayersTurn();
			gui.drawBoard();

			if (hasSolution && onPath) {
				Respond();
			}
		} catch (IllegalArgumentException e) {
			gui.invalidMove(e.getMessage());
			gui.drawBoard();
		}

	}

	@Override
	public void setFinalMoveColor(String finalMoveColor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLastMoveWasPass(boolean lastMoveWasPass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setResignedPlayer(String resignedPlayer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean wasLastMovePass() {
		// TODO Auto-generated method stub
		return false;
	}

	public void nextPlayersTurn() {
		blackToMove = !blackToMove;
	}

	public PracticeProblem(UserInterface gui, MainMenu mainMenu) {
		this.gui = gui;
		File sgfFile = mainMenu.getReplayFile();
		sgf.readSgfFile(sgfFile);
		problem = sgf.getProblem();

		if (problem.getSolution() != null) {
			hasSolution = true;
			solution = problem.getSolution();
			onPathMoves = solution.getResponses();
		}

		board = problem.getBoard();
		blackToMove = problem.getBlackToMove();

	}

	public void Respond() {
		try {

			for (Move move : onPathMoves) {
				if (move.equals(lastMove)) {
					if (move.getIsLastMove()) {
						System.out.println("End of path");
						onPath = false;
					} else {
						try {
							Thread.sleep(RESPONSE_DELAY);
							Move computerMove = move.getResponses().get(0);
							board.placeStone(computerMove.getColor(), computerMove.getX(), computerMove.getY());
							nextPlayersTurn();
							gui.drawBoard();

						} catch (IllegalArgumentException e) {
							gui.invalidMove(e.getMessage());
							gui.drawBoard();
						}
					}
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean blackToMove() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void finalizeScore() {
		// TODO Auto-generated method stub
		
	}

}
