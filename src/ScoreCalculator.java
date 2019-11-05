/**
 * @author Chuan
 */
import java.util.HashMap;
import java.util.HashSet;

public class ScoreCalculator {
	
	private HashMap<String, Integer> scores = new HashMap<>();
	private String winner;
	
	/**
	 * This method calculates the scores from both black and white by: 
	 * 1. categorize each point/intersection on the board into 3 groups: black, white, empty
	 * 2. remove dead stones from the board and replace them with empty locations
	 * 3. group empty locations into areas
	 * 4. identify if an area belongs to black or white, or if it is neutral 
	 * 5. fill the neutral positions with stones
	 * 6. count empty locations that belong to black (or white), count black (or white) stones, 
	 * add up the counts and get the score
	 * @param size - board size
	 */
	
	public void calculateScores(int size) {
		Board board = new Board(size);
		Score score = new Score(board);
		score.categorizePoints();
		HashSet<Integer[]> deadStonePositions = new HashSet<>(); 
		/*
		 * need a method that returns a HashSet containing the x and y positions of the dead stones selected by players
		 */
		HashSet<DeadStone> deadStones = score.getDeadStones(deadStonePositions);
		score.removeDeadStones(deadStones);
		score.combineEmptyLocations();
		score.checkAreaBlackOrWhite();
		String lastMove = "black";
		/*
		 * need a method that returns whom made the last move 
		 * for now it's set to be black
		 */
		score.fillNeutralPositions(lastMove);
		scores = score.scoring();
	}
	
	/**
	 * returns the scores of black and white without taking komi into account
	 * @return scores
	 */
	public HashMap<String, Integer> getScores() {
		return scores;
	}

	

}
