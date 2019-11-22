import java.io.File;
import java.util.ArrayList;

public class ReplayMode extends AbstractGame {
	
	private sgfHandler sgf = new sgfHandler();
	private String caption;
	private ReplayGame replayGame;
	
	public ReplayMode(UserInterface gui, MainMenu mainMenu) {
		this.gui = gui;
		File sgfFile = mainMenu.getReplayFile();
		sgf.readSgfFile(sgfFile);
		sgf.constructReplayGame();
		replayGame = sgf.getReplayGame();
		
		board = new Board(replayGame.getBoardSize());
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
