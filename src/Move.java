import java.awt.Color;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a single move. When being imported from an sgf file, moves could potentially have annotations.
 * When it's a practice problem, moves may have parent and child moves
 * @author morrowch
 *
 */
public class Move {

	private Color color;
	private int x;
	private int y;
	private String annotation = "";
	private ArrayList<Move> responses = new ArrayList<Move>(); // Possible responses to a move, defined in a problem file
	private Move parent; // Parent move in a solution tree
	private Boolean isLastMove = false;
	private int moveNumber = 0;
	private Boolean isPass = false;

	/**
	 * Constructor
	 * @param color
	 * @param x
	 * @param y
	 */
	public Move(Color color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructor of place-holder move
	 */
	public Move() {
		color = Color.WHITE;
	}

	/**
	 * Parses an individual move from the solution text of a sgf file.
	 * 
	 * @param moveString
	 * @return
	 */
	public Move(String moveString) {
		// Check that the move matches the expected form from the sgf file
		Matcher moveMatch = Pattern.compile("(B|W)\\[(\\w\\w)?\\]").matcher(moveString);
		if (moveMatch.find()) {
			this.color = (moveMatch.group(1).equals("B")) ? Color.BLACK : Color.WHITE;
			// If the characters within the brackets are null or 'tt', the move is a pass
			if (moveMatch.group(2) == null || moveMatch.group(2).equals("tt")) {
				isPass = true;
			} else {
				this.x = moveMatch.group(2).charAt(0) - 'a';
				this.y = moveMatch.group(2).charAt(1) - 'a';
			}
			// Check if the move has an associated comment
			Matcher moveAnnotation = Pattern.compile("C\\[(.+)\\]").matcher(moveString);
			if (moveAnnotation.find()) {
				this.annotation = moveAnnotation.group(1);
			} else if (isPass) {
				String colorString = (color.equals(Color.BLACK)) ? "Black" : "White";
				this.annotation = colorString + " passes";
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getAnnotation() {
		return annotation;
	}

	/**
	 * 
	 * @param annotation
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/**
	 * 
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * Override of the toString method. Prints the color and location of the move, as well as the move number
	 * if it is defined
	 */
	@Override
	public String toString() { 
		String colorString = (color.equals(Color.BLACK)) ? "Black" : "White";
		if (moveNumber > 0) {
			if (isPass) {
				return moveNumber + ". " + colorString + " pass";
			}
			return moveNumber + ". " + colorString + " " + x + " " + y;
		}
		return colorString + " " + x + " " + y;
	}

	/**
	 * Override of the equals method. This checks if both the color and coordinates are the same (but note that
	 * this does not check the move number)
	 */
	@Override
	public boolean equals(Object o) { 
		Move otherMove = (Move) o;
		if (color.equals(otherMove.getColor()) && x == otherMove.getX() && y == otherMove.getY()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Move> getResponses() {
		return responses;
	}

	/**
	 * Adds a response to the list of possible responses. This also sets the parent of the repsonse being set
	 * to the current problem
	 * @param response
	 */
	public void addResponse(Move response) {
		responses.add(response);
		response.setParent(this);
	}

	/**
	 * 
	 * @param parent
	 */
	public void setParent(Move parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * @return
	 */
	public Move getParent() {
		return parent;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean getIsLastMove() {
		return isLastMove;
	}

	/**
	 * 
	 * @param isLastMove
	 */
	public void setIsLastMove(Boolean isLastMove) {
		this.isLastMove = isLastMove;
	}

	/**
	 * 
	 * @param n
	 */
	public void setMoveNumber(int n) {
		moveNumber = n;
	}

	/**
	 * 
	 * @return
	 */
	public int getMoveNumber() {
		return moveNumber;
	}

	/**
	 * 
	 * @return
	 */
	public Boolean getIsPass() {
		return isPass;
	}

}
