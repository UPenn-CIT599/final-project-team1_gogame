import java.util.ArrayList;
import java.awt.Color;

/**
 * This class represents a single stone. Stones have a color, an intersection, and a group of which they
 * are a part.
 * @author morrowch
 *
 */
public class Stone {
	
	private Color color;
	private Intersection intersection;
	private Group group;
	
	/**
	 * Stones are created with a specified color and position on the board. A parent board must also be
	 * provided, so that a stone can referene the same instance of the intersection as the parent board.
	 * @param color
	 * @param x
	 * @param y
	 * @param board
	 */
	public Stone(Color color, int x, int y, Board board) {
		this.color = color;
		intersection = board.getIntersections()[x][y];
	}
	
	/**
	 * Finds the intersections that are adjacent to itself on the board
	 * @param board
	 * @return
	 */
	public ArrayList<Intersection> getAdjacentIntersections(Board board) {
		ArrayList<Intersection> adjacentIntersections = new ArrayList<Intersection>();
		
		if (intersection.getxPosition() > 0) {
			adjacentIntersections.add(board.getIntersections()[intersection.getxPosition() - 1][intersection.getyPosition()]);
		}
		if (intersection.getxPosition() < board.getSize() - 1) {
			adjacentIntersections.add(board.getIntersections()[intersection.getxPosition() + 1][intersection.getyPosition()]);
		}
		if (intersection.getyPosition() > 0) {
			adjacentIntersections.add(board.getIntersections()[intersection.getxPosition()][intersection.getyPosition() - 1]);
		}
		if (intersection.getyPosition() < board.getSize() - 1) {
			adjacentIntersections.add(board.getIntersections()[intersection.getxPosition()][intersection.getyPosition() + 1]);
		}
		
		return adjacentIntersections;
	}

	/**
	 * Returns the group to which the stone belongs
	 * @return
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * Sets the group to which a stone belongs. A stone's group has a setter because a stone might change
	 * groups during the course of a game. However, a stone's color and intersection do not have setters,
	 * as these should not change once a stone is created.
	 * @param group
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * Gets the color of a stone
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Gets the intersection of a stone
	 * @return
	 */
	public Intersection getIntersection() {
		return intersection;
	}

}
