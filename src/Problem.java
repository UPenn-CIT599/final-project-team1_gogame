import java.util.*;

public class Problem {
	
	private Board board;
	private Boolean blackToMove;
	private String caption;
	private Boolean hasSolution;
	private ProblemSolution solution;
	
	public Problem(Board board, Boolean blackToMove, String caption, ProblemSolution solution) {
		this.board = board;
		this.blackToMove = blackToMove;
		this.caption = caption;
		this.solution = solution;
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
	
	public void setSolution(ProblemSolution solution) {
		this.solution = solution;
	}
	
	public ProblemSolution getSolution() {
		return solution;
	}

}
