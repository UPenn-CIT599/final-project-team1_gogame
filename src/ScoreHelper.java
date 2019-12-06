import java.util.ArrayList;
import java.util.HashSet;

public class ScoreHelper {
	
	/**
	 * This class contains helper methods for the Score class.
	 */
	
	/**
	 * Definitions:
	 * Chain is a group of adjacent points.
	 * Reach is all the adjacent points surrounding a chain.
	 */
	
	/**
	 * Given a point, the board size, and the board position, 
	 * this method returns the chain that this point is in and 
	 * the reach of this chain.
	 * This method uses some code found online at:
     * https://www.moderndescartes.com/essays/implementing_go/
	 * @param p - the given point
	 * @param size
	 * @param finalPositions
	 * @return chainAndReach
	 */
	public static ArrayList<HashSet<Point>> getChainAndFindReach(Point p, int size, Point[][] finalPositions) {
    	ArrayList<Point> frontier = new ArrayList<>();
    	frontier.add(p);
    	HashSet<Point> reach = new HashSet<>();
    	HashSet<Point> chain = new HashSet<>(); 
    	chain.add(p);
    	while (frontier.size() > 0) {
    		Point currentPoint = frontier.get(frontier.size()-1);
    		frontier.remove(frontier.size()-1);
    		chain.add(currentPoint);
    		for (Point neighbor : Helper.getAdjacentPoints(currentPoint, size, finalPositions)) {
    			String neighborColor = finalPositions[neighbor.getxPosition()][neighbor.getyPosition()].getStatus();
    			if ((neighborColor.equals(currentPoint.getStatus())) && (!chain.contains(neighbor))) {
    				frontier.add(neighbor);
    			} else if (!neighborColor.equals(currentPoint.getStatus())) {
    				reach.add(neighbor);
    			}
    		}
    	}
    	ArrayList<HashSet<Point>> chainAndReach = new ArrayList<>();
    	chainAndReach.add(chain);
    	chainAndReach.add(reach);
    	return chainAndReach;
    }
	
	/**
	 * Given the reach of a chain, this method counts the number of liberties of this chain.
	 * @param reach
	 * @return libertyCount
	 */
	public static int countLiberties(HashSet<Point> reach) {
		int libertyCount = 0;
			for (Point neighbor : reach) {
				if (neighbor.getStatus().equals("e")) {
					libertyCount++;
				}
			}
		return libertyCount;
	}

}
