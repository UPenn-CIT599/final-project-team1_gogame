import java.awt.Color;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * This class parses the solution tree of a problem created from an sgf file, and constructs a tree of moves.
 * At the beginning of parsing a solution, a dummy "parent" move is created, for which all initial moves are a parent.
 * Following this, each subsequent move is added as a response to a parent move.
 * In general, every move representing a solver's move should only have one response (to be made by a computer). If there
 * happen to be multiple, the first move is made by the computer as a default.
 * 
 * @author morrowch
 *
 */
public class ProblemSolution {

	private static String solutionText;
	private static String UNABLE_TO_PARSE_SOLUTION = "Unable to parse solution of problem.";

	private Color solverColor = Color.BLACK; // TODO: Defaulting to black for now, as all the problems in the practice collection are for black
	private static String[] moveStrings;
	private int i = 1; // This represents the move being added in the solution tree. There is no real "order" to these
	private ArrayList<Move> variations = new ArrayList<Move>(); // ArrayList of variations, used to keep track of nested variations in the recursive ParseSolution() method
	private ArrayList<Move> responses;

	/**
	 * Constructor takes in the contents of an sgf file
	 * @param sgfText
	 */
	public ProblemSolution(String sgfText) {
		Pattern movesPattern = Pattern.compile(";(B|W)\\[\\w\\w\\].*$");
		Matcher movesMatcher = movesPattern.matcher(sgfText);
		if (movesMatcher.find()) {
			solutionText = movesMatcher.group();
		} else {
			System.out.println(UNABLE_TO_PARSE_SOLUTION);
		}
	}

	/**
	 * Parses the solution tree of an sgf file. This creates a "dummy" first move, for which all subsequent moves are children
	 */
	public void ParseSolution() {
		moveStrings = solutionText.split(";");

		for (String mString : moveStrings) {
			System.out.println(mString);
		}
		System.out.println("\n\n");

		Move parentMove = new Move();
		parentMove.setMoveNumber(0);
		variations.add(parentMove);

		AddChildProblem(parentMove, moveStrings[i]);
		
		responses = parentMove.getResponses();
	}

	/**
	 * Recursive function which parses the solution tree of an sgf file. Each problem is added as a response to a parent move
	 * If it is the first move of the problem, it is added as a response to the dummy parent problem
	 * @param parent
	 * @param childString
	 */
	public void AddChildProblem(Move parent, String childString) {
		Move child = parseMove(childString);
		parent.addResponse(child);
		child.setMoveNumber(parent.getMoveNumber() + 1);
		System.out.println("Adding " + child + " to " + parent);

		parent = child;

		// Checks if the move is the end of a variation
		int variationEnds = GetNumberOfChars(childString, ')');
		if (variationEnds > 0) {
			String outcome = (child.getColor().equals(Color.BLACK)) ? "correct" : "incorrect";
			//System.out.println("End of " + outcome + " variation");
			child.setIsLastMove(true);
			for (int j = 0; j < variationEnds; j++) {
				if (variations.size() > 0) {
					parent = variations.get(variations.size() - 1);
					variations.remove(variations.size() - 1);
				}
			}
		} 

		// Checks if the move is the start of a variation
		if (childString.contains("(")) {
			variations.add(parent);
			//System.out.println("Starting alternative branch from move " + parent);
		}

		// Increments to the next move in the tree, and recursively calls the same function again if another move exists
		i++;
		if (i < moveStrings.length) {
			AddChildProblem(parent, moveStrings[i]);
		}

	}

	/**
	 * Parses an individual move from the solution text of a sgf file.
	 * 
	 *  TODO: Still some work to do on this part, including parsing out the comment tag, if it exists
	 * @param moveString
	 * @return
	 */
	public static Move parseMove(String moveString) {
		Matcher moveMatch = Pattern.compile("(B|W)\\[(\\w\\w)\\]").matcher(moveString);
		if (moveMatch.find()) {
			Color color = (moveMatch.group(1).equals("B")) ? Color.BLACK : Color.WHITE;
			int x = moveMatch.group(2).charAt(0) - 'a';
			int y = moveMatch.group(2).charAt(1) - 'a';
			Move move = new Move(color, x, y);
			return move;
		}
		return null;
	}

	/**
	 * Helper method which gets the number of chars in a given string. Used to determine when a move is the end of a nested variation
	 * @param s
	 * @param c
	 * @return
	 */
	public static int GetNumberOfChars(String s, char c) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Move> getResponses() {
		return responses;
	}

}
