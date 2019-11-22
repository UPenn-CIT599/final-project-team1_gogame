import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplayGame {
	
	private ArrayList<String> moveStrings;
	private String sgfText;
	private Board board;
	private ArrayList<Move> moves;
	private int boardSize;
	
	public ReplayGame(String sgfText, sgfHandler sgfHandler) {
		this.sgfText = sgfText;
		boardSize = sgfHandler.getBoardSize();
	}
	
	public void ParseMoves() {
		moveStrings = new ArrayList<String>();  
		
		Pattern singleMove = Pattern.compile("([B|W]\\[\\w\\w\\].*?(?=(;B|;W|$)))");
		Matcher singleMoveMatcher = singleMove.matcher(sgfText);
		while (singleMoveMatcher.find()) {
			moveStrings.add(singleMoveMatcher.group(1));
		}
	}
	
	public int getBoardSize() {
		return boardSize;
	}

}
