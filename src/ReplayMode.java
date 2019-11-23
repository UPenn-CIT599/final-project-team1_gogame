import java.io.File;
import java.util.ArrayList;

public class ReplayMode extends AbstractGame {

	private sgfHandler sgf = new sgfHandler();
	private String caption;
	private ReplayGame replayGame;
	private int moveNumber = 0;

	public ReplayMode(UserInterface gui, MainMenu mainMenu) {
		this.gui = gui;
		File sgfFile = mainMenu.getReplayFile();
		sgf.readSgfFile(sgfFile);
		sgf.constructReplayGame();
		replayGame = sgf.getReplayGame();
		gameOver = false;

		board = replayGame.getBoard();
	}

	public void NextMove() {
		if (!gameOver) {
			Move move = replayGame.getMove(moveNumber);
			if (move.getIsLastMove()) {
				gameOver = true;
			} else {
				moveNumber++;
			}
			try {
				board.placeStone(move);
				// TODO: Check if last move
				gui.drawBoard();
			} catch (IllegalArgumentException e) {
				gui.invalidMove(e.getMessage());
				gui.drawBoard();
			}
		}
	}

	@Override
	public void gameOver() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMouseClick(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMouseClick(int buttonID) {
		// TODO Auto-generated method stub

	}

}
