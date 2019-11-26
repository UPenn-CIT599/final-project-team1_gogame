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
	private Color solverColor = Color.BLACK;
	private static ArrayList<String> moveStrings;
	private int i = 0; // This represents the move being added in the solution tree. There is no real "order" to these
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
		moveStrings = new ArrayList<String>();  
		
		Pattern singleMove = Pattern.compile("([B|W]\\[\\w\\w\\].*?(?=(;B|;W|$)))");
		Matcher singleMoveMatcher = singleMove.matcher(solutionText);
		while (singleMoveMatcher.find()) {
			moveStrings.add(singleMoveMatcher.group(1));
		}

		Move parentMove = new Move();
		parentMove.setMoveNumber(0);
		variations.add(parentMove);

		AddChildProblem(parentMove, moveStrings.get(i));

		responses = parentMove.getResponses();
	}

	/**
	 * Recursive function which parses the solution tree of an sgf file. Each problem is added as a response to a parent move
	 * If it is the first move of the problem, it is added as a response to the dummy parent problem
	 * @param parent
	 * @param childString
	 */
	public void AddChildProblem(Move parent, String childString) {
		//Move child = parseMove(childString);
		Move child = new Move(childString);
		if (i == 0) {
			solverColor = child.getColor();
		}
		parent.addResponse(child);
		child.setMoveNumber(parent.getMoveNumber() + 1);

		parent = child;

		// Checks if the move is the end of a variation
		int variationEnds = GetNumberOfChars(childString, ')');
		if (variationEnds > 0) {
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
		}

		// Increments to the next move in the tree, and recursively calls the same function again if another move exists
		i++;
		if (i < moveStrings.size()) {
			AddChildProblem(parent, moveStrings.get(i));
		}

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
	
	/**
	 * 
	 * @return
	 */
	public Color getSolverColor() {
		return solverColor;
	}

}
