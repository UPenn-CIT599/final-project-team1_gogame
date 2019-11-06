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

	/**
	 * Method to read and extract the text from a .sgf file
	 * @param sgfFile
	 */
	public static void readSgfFile(String sgfFile) {

		File file = new File(sgfFile);
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

	public static Board constructBoard() {
		Board board = new Board(19);

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

		return board;
	}

}
