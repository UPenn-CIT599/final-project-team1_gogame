import java.util.ArrayList;
import java.awt.Color;

public class Stone {
	
	private Color color;
	private Intersection intersection;
	private Group group;
	
	public Stone(Color color, int x, int y, Board board) {
		this.color = color;
		intersection = board.getIntersections()[x][y];
	}
	
	/**
	 * Finds the intersections that are adjacent to the board
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

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Color getColor() {
		return color;
	}

	public Intersection getIntersection() {
		return intersection;
	}

}
