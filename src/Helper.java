/**
 * @author Chuan
 */
import java.util.ArrayList;

public class Helper {
	
	/**
	 * This method returns the adjacent points of a given point,
	 * which is quite similar to Christian's getAdjacentIntersections method under the Stone class.
	 * @param p - the given point
	 * @param boardSize
	 * @param boardPosition
	 * @return adjacentPoints
	 */
	public static ArrayList<Point> getAdjacentPoints(Point p, int boardSize, Point[][] boardPosition) {
		ArrayList<Point> adjacentPoints = new ArrayList<>();
		if (p.getxPosition() > 0) {
			adjacentPoints.add(boardPosition[p.getxPosition() - 1][p.getyPosition()]);
		}
		if (p.getxPosition() < boardSize - 1) {
			adjacentPoints.add(boardPosition[p.getxPosition() + 1][p.getyPosition()]);
		}
		if (p.getyPosition() > 0) {
			adjacentPoints.add(boardPosition[p.getxPosition()][p.getyPosition() - 1]);
		}
		if (p.getyPosition() < boardSize - 1) {
			adjacentPoints.add(boardPosition[p.getxPosition()][p.getyPosition() + 1]);
		}
		return adjacentPoints;
	}
	
	/**
	 * This method checks if an empty location has already been included in the ArrayList. 
	 * @param emptyLocation
	 * @param areas
	 * @return boolean
	 */
	public static boolean checkContainEmptyLocation(Point emptyLocation, ArrayList<Area> areas) {
		boolean containEmptyLocation = false;
		for (Area a : areas) {
			if (a.getEmptyLocations().contains(emptyLocation)) {
				containEmptyLocation = true;
			}
		}
		return containEmptyLocation;
	}
}
