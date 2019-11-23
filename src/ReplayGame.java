import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplayGame {

	private ArrayList<String> moveStrings;
	private String sgfText;
	private Board board;
	private ArrayList<Move> moves;
	private String result = "";

	public ReplayGame(String sgfText) {
		this.sgfText = sgfText;
	}

	public void ParseMoves() {
		moves = new ArrayList<Move>();  

		Pattern singleMove = Pattern.compile("([B|W]\\[\\w\\w\\].*?(?=(;B|;W|$)))");
		Matcher singleMoveMatcher = singleMove.matcher(sgfText);
		while (singleMoveMatcher.find()) {
			Move move = new Move(singleMoveMatcher.group(1));
			move.setMoveNumber(moves.size());
			move.setAnnotation("Move: " + (move.getMoveNumber()+1) + ". " + move.getAnnotation());
			moves.add(move);
		}
		Move lastMove = moves.get(moves.size() - 1);
		lastMove.setIsLastMove(true);

		int boardSize = 19;
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

	public Board getBoard() {
		return board;
	}

	public Move getMove(int moveNumber) {
		return moves.get(moveNumber);
	}

	public String getResult() {
		return result;
	}

}
