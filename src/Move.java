import java.awt.Color;
import java.util.ArrayList;

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
	private String annotation;
	private ArrayList<Move> responses = new ArrayList<Move>(); // Possible responses to a move, defined in a problem file
	private Move parent; // Parent move in a solution tree
	private Boolean isLastMove = false;
	private int moveNumber = 0;
	
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
	
}
