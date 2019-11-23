import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplayGame {
	
	private ArrayList<String> moveStrings;
	private String sgfText;
	private Board board;
	private ArrayList<Move> moves;
	
	public ReplayGame(String sgfText) {
		this.sgfText = sgfText;
	}
	
	public void ParseMoves() {
		moves = new ArrayList<Move>();  
		
		Pattern singleMove = Pattern.compile("([B|W]\\[\\w\\w\\].*?(?=(;B|;W|$)))");
		Matcher singleMoveMatcher = singleMove.matcher(sgfText);
		while (singleMoveMatcher.find()) {
			Move move = new Move(singleMoveMatcher.group(1));
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
	}
	
	public Board getBoard() {
		return board;
	}
	
	public Move getMove(int moveNumber) {
		return moves.get(moveNumber);
	}

}
