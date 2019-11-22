import java.awt.Color;
import java.util.*;

/**
 * This represents a Go problem which is constructed from an sgf file. A problem has a starting board position, and
 * possibly a solution as well.
 * @author morrowch
 *
 */
public class Problem {
	
	private Board board;
	private Color solverColor;
	private String caption;
	private Boolean hasSolution;
	private ProblemSolution solution;
	
	/**
	 * Constructor takes in a board, player to start, caption, and solution (which may be null)
	 * @param board
	 * @param blackToMove
	 * @param caption
	 * @param solution
	 */
	public Problem(Board board, String caption, ProblemSolution solution) {
		this.board = board;
		this.solverColor = solution.getSolverColor();
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
	public Color getSolverColor() {
		return solverColor;
	}
	
	/**
	 * 
	 * @param solution
	 */
	public void setSolution(ProblemSolution solution) {
		this.solution = solution;
	}
	
	/**
	 * 
	 * @return
	 */
	public ProblemSolution getSolution() {
		return solution;
	}
	
	public String getCaption() {
		return caption;
	}

}
