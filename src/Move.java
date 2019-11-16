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
	private ArrayList<Move> responses = new ArrayList<Move>();
	private Move parent;
	
	private Boolean isLastMove = false;
	private int moveNumber = 0;
	
	public Move(Color color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
	}
	
	public Move() {
		color = Color.WHITE;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public Color getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
    public String toString() { 
		String colorString = (color.equals(Color.BLACK)) ? "Black" : "White";
		if (moveNumber > 0) {
			return moveNumber + ". " + colorString + " " + x + " " + y;
		}
        return colorString + " " + x + " " + y;
    }
	
	@Override
    public boolean equals(Object o) { 
		Move otherMove = (Move) o;
		if (color.equals(otherMove.getColor()) && x == otherMove.getX() && y == otherMove.getY()) {
			return true;
		}
		return false;
	}

	public ArrayList<Move> getResponses() {
		return responses;
	}

	public void addResponse(Move response) {
		responses.add(response);
		response.setParent(this);
	}
	
	public void setParent(Move parent) {
		this.parent = parent;
	}
	
	public Move getParent() {
		return parent;
	}

	public Boolean getIsLastMove() {
		return isLastMove;
	}

	public void setIsLastMove(Boolean isLastMove) {
		this.isLastMove = isLastMove;
	}
	
	public void setMoveNumber(int n) {
		moveNumber = n;
	}
	
	public int getMoveNumber() {
		return moveNumber;
	}
	
}
