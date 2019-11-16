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

	private static Pattern stonePositions = Pattern.compile("(A(B|W)(\\[\\w\\w\\])+)");
	private static Pattern stoneIntersections = Pattern.compile("\\[(\\w\\w)\\]");
	private static String sgfText;
	private static Problem problem;
	private static File sgfFile;

	private static String UNABLE_TO_PARSE_SOLUTION = "Unable to parse solution of problem.";

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
	public static void readSgfFile(File file) {
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

		file.getPath();

		constructProblem();
	}

	public static void constructProblem() {

		Board board = new Board(getBoardSize());
		String caption = getCaption();
		Boolean blackToPlay = getPlayerToMove();

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
					board.placeStone(color, x, y);
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
		problem = new Problem(board, blackToPlay, caption, solution);
	}

	/**
	 * Finds the size of the game board based on the tag SZ. If now tag is found, it defaults to returning 19x19
	 * @return
	 */
	public static int getBoardSize() {
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
	public static String getCaption() {
		String caption = "Problem";
		Matcher problemCaptionTag = Pattern.compile("GN\\[(\\w+)\\]").matcher(sgfText);
		if (problemCaptionTag.find()) {
			caption = problemCaptionTag.group(1);
		} 
		return caption;
	}

	/**
	 * Gets the player to move in the current position based on the PL tag.
	 * @return
	 */
	public static Boolean getPlayerToMove() {
		Boolean blackToPlay = true; // Default to having black play first (TODO: glean this from the move order of the solution)
		Matcher playerToMoveTag = Pattern.compile("PL\\[(B|W)\\]").matcher(sgfText);
		if (playerToMoveTag.find()) {
			blackToPlay = playerToMoveTag.group(1).equals("B") ? true : false;
		}
		return blackToPlay;
	}

	//	public static void getSolutionOld() {
	//		String solText = "";
	//		String path = sgfFile.getAbsolutePath();
	//		String sol = path.replace(".sgf", "_sol.sgf");
	//		
	//		File solutionFile = new File(sol);
	//		if (!solutionFile.exists()) {
	//			// TODO: Prompt user if they want to provide a solution file
	//			return;
	//		}
	
}
