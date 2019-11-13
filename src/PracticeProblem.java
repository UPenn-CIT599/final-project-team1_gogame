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
	private Boolean hasSolution;

	@Override
	public boolean blackToMove() {
		return blackToMove;
	}

	@Override
	public void finalizeScore() {
		// This method is not used in Practice Problem mode		
	}

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
		try {
			board.placeStone(color, x, y);
			nextPlayersTurn();
			gui.drawBoard();
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

		board = problem.getBoard();
		blackToMove = problem.getBlackToMove();

	}

}
