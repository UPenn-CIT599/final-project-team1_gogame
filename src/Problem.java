import java.util.*;

public class Problem {
	
	private Board board;
	private Boolean blackToMove;
	private String caption;
	private Boolean hasSolution;
	private ArrayList<String> solution;
	
	public Problem(Board board, Boolean blackToMove, String caption, ArrayList<String> solution) {
		this.board = board;
		this.blackToMove = blackToMove;
		this.caption = caption;
	}
	
	/**
	 * 
	 * @return
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean getBlackToMove() {
		return blackToMove;
	}

}
