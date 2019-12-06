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
	private Boolean winByResignation = false;
	private Boolean winByTimeout = false;
	private String blackPlayer = "Black";
	private String whitePlayer = "White";
	private double pointDifferential = 0;
	private Boolean blackWins;
	private Pattern stonePositionIntersectionPattern = Pattern.compile("\\[(\\w\\w)\\]");

	public ReplayGame(String sgfText) {
		this.sgfText = sgfText;
	}

	public void ParseMoves() throws IllegalArgumentException {
		moves = new ArrayList<Move>();  

		// Parse out the individual moves of a game
		Pattern singleMove = Pattern.compile("(;[B|W]\\[(\\w\\w)?\\].*?(?=(;B|;W|$)))");
		Matcher singleMoveMatcher = singleMove.matcher(sgfText);
		// For each tag that is found by the matcher, a new move is created
		while (singleMoveMatcher.find()) {
			Move move = new Move(singleMoveMatcher.group(1));
			// Set the move number based on the number of moves already made
			move.setMoveNumber(moves.size());
			// Include the move number in the annotation
			move.setAnnotation("Move: " + (move.getMoveNumber()+1) + ". " + move.getAnnotation());
			moves.add(move);
		}
		// Determine who moves first based on the color of the first move
		firstMoveBlack = moves.get(0).getColor().equals(Color.BLACK) ? true : false;
		Move lastMove = moves.get(moves.size() - 1);
		lastMove.setIsLastMove(true);

		// Find dead stones indicated by the DS tag, defined for the purposes of this app
		Pattern deadStones = Pattern.compile("((DS)(\\[\\w\\w\\])*)");
		Pattern deadStoneIntersection = stonePositionIntersectionPattern;
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

		// Get the size of the board based on the SZ tag. If none is specified, this defaults to 19
		Matcher boardSizeTag = Pattern.compile("SZ\\[(\\d+)\\]").matcher(sgfText);
		if (boardSizeTag.find()) {
			boardSize = Integer.parseInt(boardSizeTag.group(1));
		}
		board = new Board(boardSize);

		Matcher handicapStoneMatcher = Pattern.compile("(A(B|W)(\\[\\w\\w\\])+)").matcher(sgfText);
		ArrayList<Move> handicapMoves = new ArrayList<Move>();
		while (handicapStoneMatcher.find()) {
			String stonePositionsLine = handicapStoneMatcher.group();
			Color color = (stonePositionsLine.charAt(1) == 'B') ? Color.BLACK : Color.WHITE;
			Matcher intersections = stonePositionIntersectionPattern.matcher(stonePositionsLine);
			while (intersections.find()) {
				String intersection = intersections.group(1);
				// Get the numeric representation of the intersections (subtracting 'a' to index at 0)
				int x = intersection.charAt(0) - 'a';
				int y = intersection.charAt(1) - 'a';
				// Place the stone on the board
				Move move = new Move(color, x, y);
				handicapMoves.add(move);
			}
		}

		for (Move move : handicapMoves) {
			try {
				board.placeStone(move);
			}
			catch (IllegalArgumentException e) {
				throw e;
			}
		}

		// Get the name of the black player
		Matcher blackPlayerMatcher = Pattern.compile("PB\\[(.+?)\\]").matcher(sgfText);
		if (blackPlayerMatcher.find()) {
			blackPlayer = blackPlayerMatcher.group(1);
		}

		// Get the name of the white player
		Matcher whitePlayerMatcher = Pattern.compile("PW\\[(.+?)\\]").matcher(sgfText);
		if (whitePlayerMatcher.find()) {
			whitePlayer = whitePlayerMatcher.group(1);
		}

		// Get the result of the game based on the RE tag
		Matcher resultTag = Pattern.compile("RE\\[(0|B|W)(\\+)?(R|T|([0-9]*\\.?[0-9]*))\\]").matcher(sgfText);
		if (resultTag.find()) {
			// Determine whether the winning player is black or white
			blackWins = resultTag.group(1).equals("B") ? true : false;
			String winningPlayer = blackWins ? "Black" : "White";
			// If 'R', the game was won by resignation, if 'T', by timeout. If a numeric value is specified, this is the number of points the winner won by
			String winningResult = "";
			if (resultTag.group(3).charAt(0) == 'R') {
				winByResignation = true;
				winningResult = "resignation";
			} else if (resultTag.group(3).charAt(0) == 'T') {
				winByTimeout = true;
				winningResult = "timeout";
			} else {
				pointDifferential = Double.parseDouble(resultTag.group(3));
				winningResult = resultTag.group(3) + " points";
			}
			result = winningPlayer + " wins by " + winningResult;
			lastMove.setAnnotation(result);
		} else {
			// If no tag is found, display default no result message
			result = "End of file. No result";
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

	/**
	 * 
	 * @return
	 */
	public Boolean getWinByResignation() {
		return winByResignation;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean getWinByTimeout() {
		return winByTimeout;
	}

	/**
	 * 
	 * @return
	 */
	public String getBlackPlayer() {
		return blackPlayer;
	}

	/**
	 * 
	 * @return
	 */
	public String getWhitePlayer() {
		return whitePlayer;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean getBlackWins() {
		return blackWins;
	}

	/**
	 * 
	 * @return
	 */
	public double getPointDifferential() {
		return pointDifferential;
	}

}
