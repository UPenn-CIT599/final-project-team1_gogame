import java.awt.Color;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.PUBLIC_MEMBER;

public class ProblemSolution {

	private static String solutionText;
	private static String UNABLE_TO_PARSE_SOLUTION = "Unable to parse solution of problem.";

	private Color solverColor = Color.BLACK;
	private static String[] moveStrings;
	private int i = 1;
	private ArrayList<Move> variations = new ArrayList<Move>();
	
	private ArrayList<Move> responses;

	public ProblemSolution(String sgfText) {
		Pattern movesPattern = Pattern.compile(";(B|W)\\[\\w\\w\\].*$");
		Matcher movesMatcher = movesPattern.matcher(sgfText);
		if (movesMatcher.find()) {
			solutionText = movesMatcher.group();
		} else {
			System.out.println(UNABLE_TO_PARSE_SOLUTION);
		}
	}

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

	public void AddChildProblem(Move parent, String childString) {
		Move child = parseMove(childString);
		parent.addResponse(child);
		child.setMoveNumber(parent.getMoveNumber() + 1);
		System.out.println("Adding " + child + " to " + parent);

		parent = child;

		int variationEnds = GetNumberOfChars(childString, ')');
		if (variationEnds > 0) {
			String outcome = (child.getColor().equals(Color.BLACK)) ? "correct" : "incorrect";
			System.out.println("End of " + outcome + " variation");
			child.setIsLastMove(true);
			for (int j = 0; j < variationEnds; j++) {
				if (variations.size() > 0) {
					parent = variations.get(variations.size() - 1);
					variations.remove(variations.size() - 1);
				}
			}
		} 

		if (childString.contains("(")) {
			variations.add(parent);
			System.out.println("Starting alternative branch from move " + parent);
		}

		i++;
		if (i < moveStrings.length) {
			AddChildProblem(parent, moveStrings[i]);
		}

	}

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

	public static int GetNumberOfChars(String s, char c) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}
	
	public ArrayList<Move> getResponses() {
		return responses;
	}

}
