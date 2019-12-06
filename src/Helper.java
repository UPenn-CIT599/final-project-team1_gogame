/**
 * @author Chuan
 */
import java.util.ArrayList;
import java.util.HashSet;

public class Helper {
	
	/**
	 * This is a helper method for the getChainAndFindReach method in the ScoreHelper class
	 * and the getInfluence method in the DeadStoneIdentifier class.
	 * This method returns the adjacent points of a given point. 
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
	 * This is a helper method for the influenceOfLogicalChain method in the DeadStoneIdentifier class.
	 * This method checks if a position is on the board.
	 * Value 12345 is used to indicate positions that are not on the board.
	 * @param value
	 * @return boolean
	 */
	public static boolean checkOnBoard(int value) {
		if (value == 12345) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * This is a helper method for the influenceOfLogicalChain method in the DeadStoneIdentifier class.
	 * This method sets the point values on the board.
	 * @param pv - point values to be set
	 * @param size - board size
	 * @return pointValue - point values that have been set
	 */
	public static int[][] setPointValue(int[][] pv, int size){
		int [][] pointValue = new int[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				pointValue[x][y] = pv[x][y];
			}
		}
		return pointValue;
	}
	
	/**
	 * This is a helper method for the identifyDeadStones method in the DeadStoneIdentifier class.
	 * This method returns the positions of the stones in a given dead logical chain.
	 * @param deadLogicalChains
	 * @return deadStonePositions
	 */
	public static HashSet<Integer[]> getDeadStonePositions(HashSet<Point> deadLogicalChains){
		HashSet<Integer[]> deadStonePositions = new HashSet<>();
		for (Point p : deadLogicalChains) {
			Integer[] positions = new Integer[2];
			positions[0] = p.getxPosition();
			positions[1] = p.getyPosition();
			deadStonePositions.add(positions);
		}
		return deadStonePositions;
	}
	
	/**
	 * This is a helper method for the groupChainsByLogicalConnections method in the DeadStoneIdentifier class.
	 * This method combines the preliminary logical chain groups into final logical chain groups by putting 
	 * chain groups with common components together.
	 * @param chainGroupRecord - preliminary logical chain groups
	 * @return chainGroupRecordFinal - final logical chain groups
	 */
	public static ArrayList<HashSet<Integer>> findCommonElement(ArrayList<HashSet<Integer>> chainGroupRecord){
		ArrayList<HashSet<Integer>> chainGroupRecordFinal = new ArrayList<>(); // what I need
		ArrayList<HashSet<Integer>> chainGroupRecordCopy = new ArrayList<>(); 
		chainGroupRecordCopy = (ArrayList<HashSet<Integer>>) chainGroupRecord.clone();
		HashSet<Integer> currentChain = new HashSet<>();
		HashSet<Integer> existingChain = new HashSet<>();
		HashSet<Integer> newChain = new HashSet<>();
		while (chainGroupRecordCopy.size() > 0) {
			currentChain = (HashSet<Integer>) chainGroupRecordCopy.get(chainGroupRecordCopy.size()-1).clone();
			chainGroupRecordCopy.remove(chainGroupRecordCopy.size()-1);
			boolean hasCommonElement = false;
			HashSet<Integer> commonElement = new HashSet<>();
			for (HashSet<Integer> hs : chainGroupRecordFinal) {
				HashSet<Integer> temp = (HashSet<Integer>) hs.clone();
				temp.retainAll(currentChain);
				if (temp.size() > 0) {
					hasCommonElement = true;
					commonElement = (HashSet<Integer>) hs.clone();
					break;
				}
			}
			if (hasCommonElement) {
				existingChain = (HashSet<Integer>) commonElement.clone();
				newChain = (HashSet<Integer>) existingChain.clone();
				newChain.addAll(currentChain);
				chainGroupRecordFinal.set(chainGroupRecordFinal.indexOf(existingChain), newChain);
			} else {
				chainGroupRecordFinal.add(currentChain);
			}	
		}
		return chainGroupRecordFinal;
	}
	
}
