import java.util.HashMap;

public class PracticeProblem implements GameViewer {
	
	private sgfHandler sgf;
	private UserInterface gui;
    private Board board;
    private boolean blackToMove;
    private String finalMoveColor;
    private int numRows;

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
		// This method is not used in Practice Problem mode
		
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
		// TODO Auto-generated method stub
		
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

}
