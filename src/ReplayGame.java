import java.awt.Color;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a game reconstructed from an sgf file
 * @author morrowch
 *
 */
public class ReplayGame {

	private String sgfText;
	private Board board;
	private ArrayList<Move> moves;
	private String result = "";
	private Boolean firstMoveBlack;
	private int boardSize = 19;
	private ArrayList<Intersection> deadStoneIntersections; 

	public ReplayGame(String sgfText) {
		this.sgfText = sgfText;
	}

	public void ParseMoves() {
		moves = new ArrayList<Move>();  

		// Parse out the individual moves of a game
		Pattern singleMove = Pattern.compile("([B|W]\\[(\\w\\w)?\\].*?(?=(;B|;W|$)))");
		Matcher singleMoveMatcher = singleMove.matcher(sgfText);
		while (singleMoveMatcher.find()) {
			Move move = new Move(singleMoveMatcher.group(1));
			move.setMoveNumber(moves.size());
			move.setAnnotation("Move: " + (move.getMoveNumber()+1) + ". " + move.getAnnotation());
			moves.add(move);
		}
		firstMoveBlack = moves.get(0).getColor().equals(Color.BLACK) ? true : false;
		Move lastMove = moves.get(moves.size() - 1);
		lastMove.setIsLastMove(true);

		// Find dead stones indicated by the DS tag, defined for the purposes of this app
		Pattern deadStones = Pattern.compile("((DS)(\\[\\w\\w\\])*)");
		Pattern deadStoneIntersection = Pattern.compile("\\[(\\w\\w)\\]");
		Matcher deadStonesMatcher = deadStones.matcher(sgfText);
		deadStoneIntersections = new ArrayList<Intersection>();
		try {
			while (deadStonesMatcher.find()) {
				String stonePositionsLine = deadStonesMatcher.group();
				// Extract the list of intersections for that stone color
				Matcher intersections = deadStoneIntersection.matcher(stonePositionsLine);
				while (intersections.find()) {
					String intersectionString = intersections.group(1);
					// Get the numeric representation of the intersections (subtracting 'a' to index at 0)
					int x = intersectionString.charAt(0) - 'a';
					int y = intersectionString.charAt(1) - 'a';
					Intersection intersection = new Intersection(x, y);
					deadStoneIntersections.add(intersection);
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error constructing board from provided sgf file.");
			e.printStackTrace();
		}

		Matcher boardSizeTag = Pattern.compile("SZ\\[(\\d+)\\]").matcher(sgfText);
		if (boardSizeTag.find()) {
			boardSize = Integer.parseInt(boardSizeTag.group(1));
		}
		board = new Board(boardSize);

		Matcher resultTag = Pattern.compile("RE\\[(0|B|W)(\\+)?(R|([0-9]*\\.?[0-9]*))\\]").matcher(sgfText);
		if (resultTag.find()) {
			String winningPlayer = resultTag.group(1).equals("B") ? "Black" : "White";
			String winningResult = resultTag.group(3).charAt(0) == 'R' ? "resignation" : resultTag.group(3) + " points";
			result = winningPlayer + " wins by " + winningResult;
			lastMove.setAnnotation(result);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * 
	 * @param moveNumber
	 * @return
	 */
	public Move getMove(int moveNumber) {
		return moves.get(moveNumber);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Move> getMoves() {
		return moves;
	}

	/**
	 * 
	 * @return
	 */
	public String getResult() {
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean getFirstMoveBlack() {
		return firstMoveBlack;
	}

	/**
	 * 
	 * @return
	 */
	public int getBoardSize() {
		return boardSize;
	}

	/**
	 * 
	 * @param board
	 */
	public void setBoard(Board board) {
		this.board = board;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Intersection> getDeadStoneIntersections() {
		return deadStoneIntersections;
	}

}
