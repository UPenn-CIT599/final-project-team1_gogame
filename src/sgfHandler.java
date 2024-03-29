import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;

/**
 * Public class to handle sgf files
 * @author morrowch
 *
 */
public class sgfHandler {

	private Pattern stonePositions = Pattern.compile("(A(B|W)(\\[\\w\\w\\])+)");
	private Pattern stoneIntersections = Pattern.compile("\\[(\\w\\w)\\]");
	private String sgfText;
	private ArrayList<Move> moves = new ArrayList<Move>();
	private Problem problem;
	private File sgfFile;
	private ReplayGame replayGame;

	private String UNABLE_TO_PARSE_SOLUTION = "Unable to parse solution of problem.";

	/**
	 * Gets the problem
	 * @return
	 */
	public Problem getProblem() {
		return problem;
	}

	/**
	 * Method to read and extract the text from a .sgf file
	 * @param sgfFile
	 */
	public void readSgfFile(File file) {
		sgfFile = file;
		sgfText = "";
		try {
			Scanner s = new Scanner(file);
			while (s.hasNextLine()) {
				// Save all text as a single string
				sgfText += s.nextLine();
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Construct a problem from a given sgf file.
	 */
	public void constructProblem() {

		String caption = getCaption();

		// Find lines that correspond to "Add black" or "Add white"
		Matcher w = stonePositions.matcher(sgfText);
		try {
			while (w.find()) {
				String stonePositionsLine = w.group();
				// Determine whether the stones are white or black
				Color color = (stonePositionsLine.charAt(1) == 'B') ? Color.BLACK : Color.WHITE;
				// Extract the list of intersections for that stone color
				Matcher intersections = stoneIntersections.matcher(stonePositionsLine);
				while (intersections.find()) {
					String intersection = intersections.group(1);
					// Get the numeric representation of the intersections (subtracting 'a' to index at 0)
					int x = intersection.charAt(0) - 'a';
					int y = intersection.charAt(1) - 'a';
					// Place the stone on the board
					Move move = new Move(color, x, y);
					moves.add(move);
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error constructing board from provided sgf file.");
			e.printStackTrace();
		}

		ProblemSolution solution = new ProblemSolution(sgfText);
		try {
			solution.ParseSolution();
		} catch(Exception e)
		{
			System.out.println(UNABLE_TO_PARSE_SOLUTION);
			System.out.println(e);
			solution = null;
		}
		Board board = new Board(getBoardSize(), moves);
		problem = new Problem(board, caption, solution);
	}

	/**
	 * Called when the sgf file loaded in is a game to replay
	 */
	public void constructReplayGame() throws IllegalArgumentException {
		replayGame = new ReplayGame(sgfText);
		try {
			replayGame.ParseMoves();
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * Finds the size of the game board based on the tag SZ. If now tag is found, it defaults to returning 19x19
	 * @return
	 */
	public int getBoardSize() {
		int boardSize = 19;
		Matcher boardSizeTag = Pattern.compile("SZ\\[(\\d+)\\]").matcher(sgfText);
		if (boardSizeTag.find()) {
			boardSize = Integer.parseInt(boardSizeTag.group(1));
		}
		return boardSize;
	}

	/**
	 * Finds the caption for the board based on the tag GN. Defaults to an empty string if none is found.
	 * @return
	 */
	public String getCaption() {
		String caption = "Problem";
		// Caption tags are specified by 'GN'
		Matcher problemCaptionTag = Pattern.compile("GN\\[(\\.+)\\]").matcher(sgfText);
		if (problemCaptionTag.find()) {
			caption = problemCaptionTag.group(1);
		} 
		return caption;
	}

	public ReplayGame getReplayGame() {
		return replayGame;
	}

}
