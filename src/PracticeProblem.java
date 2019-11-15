import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PracticeProblem extends AbstractGame {

	private sgfHandler sgf = new sgfHandler();
	private Problem problem;
	private Boolean hasSolution;

	@Override
	public void gameOver() {
		// Called when the problem is over

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

	public PracticeProblem(UserInterface gui, MainMenu mainMenu) {
		this.gui = gui;
		File sgfFile = mainMenu.getReplayFile();
		sgf.readSgfFile(sgfFile);
		problem = sgf.getProblem();

		board = problem.getBoard();
		blackToMove = problem.getBlackToMove();

	}

}
