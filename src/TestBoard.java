import java.awt.Color;

/**
 * The TestBoard class is used to test the UserInterface. You can switch between
 * using Board and TestBoard by commenting/uncommenting the lines in the Game
 * class that have the TODO flag. All methods used in this class need to have an
 * equivalent in the Board class.
 * 
 * @author Chris Hartung
 *
 */
public class TestBoard {
    private Color[][] intersections;
    private int size;

    /**
     * This method creates a TestBoard of the given size.
     * 
     * @param size The number of rows and columns in the TestBoard
     */
    public TestBoard(int size) {
	this.size = size;
	this.intersections = new Color[size][size];
    }

    /**
     * This method places a stone of the given color in the given location. If
     * that move is illegal, it throws an IllegalArgument exception.
     * 
     * @param color The color of the stone being placed
     * @param x     The column in which the stone is being placed
     * @param y     The row in which the stone is being placed
     * @throws IllegalArgumentException Indicates that the given move is illegal
     */
    public void placeStone(Color color, int x, int y)
	    throws IllegalArgumentException {
	if (Math.random() < 0.25) {
	    System.out.println("invalid");
	    throw new IllegalArgumentException(
		    "Invalid move, please try again.");
	}
	if (intersections[x][y] == null) {
	    intersections[x][y] = color;
	} else {
	    intersections[x][y] = null;
	}
    }

    /**
     * This method returns the color of the stone in the given location. If
     * there is no stone there, it returns null.
     * 
     * @param x The column of the intersection being checked
     * @param y The row of the intersection being checked
     * @return The color of the stone in the given location
     */
    public Color getStoneColor(int x, int y) {
	if (intersections[x][y] == null) {
	    return null;
	} else {
	    return intersections[x][y];
	}
    }

    /**
     * This method updates the previousPositions ArrayList when a player passes.
     */
    public void pass() {
	// update previousPositions
    }
}
