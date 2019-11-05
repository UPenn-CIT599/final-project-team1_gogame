import java.util.*;

/**
 * The DeadStoneSelector class is used to select dead stones.
 * 
 * @author Chris Hartung
 *
 */
public class DeadStoneSelector {
    private Board board;
    private int size;
    private boolean[][] deadStoneLocations;

    /**
     * This method creates a DeadStoneSelector for the given board.
     * 
     * @param board The board whose dead stones will be selected
     */
    public DeadStoneSelector(Game game) {
	board = game.getBoard();
	size = board.getSize();
	deadStoneLocations = new boolean[size][size];
    }

    /**
     * This method checks whether there is a dead stone at the given location.
     * 
     * @param x The column being checked
     * @param y The row being checked
     * @return True if there is a dead stone at the given location and false if
     *         either the location contains a living stone or the location is
     *         empty
     */
    public boolean isDeadStone(int x, int y) {
	return deadStoneLocations[x][y];
    }

    /**
     * This method takes as input the location the user clicked. If there is a
     * stone at that location, this method switches the status of that stone
     * from alive to dead or vice versa. If there is no stone at that location,
     * this method does nothing.
     * 
     * @param x The column that was clicked
     * @param y The row that was clicked
     */
    public void selectStone(int x, int y) {
	if (board.getStoneColor(x, y) != null) {
	    deadStoneLocations[x][y] = !deadStoneLocations[x][y];
	}
    }

    /**
     * This method returns a HashSet containing the locations of all dead
     * stones.
     * 
     * @return A HashSet of the locations of dead stones, where each location is
     *         represented as an Integer[] with two elements, the x position and
     *         then the y position.
     */
    public HashSet<Integer[]> deadStoneHashSet() {
	HashSet<Integer[]> locations = new HashSet<>();
	for (int i = 0; i < size; i++) {
	    for (int j = 0; j < size; j++) {
		if (deadStoneLocations[i][j]) {
		    Integer[] location = { i, j };
		    locations.add(location);
		}
	    }
	}
	return locations;
    }

}
