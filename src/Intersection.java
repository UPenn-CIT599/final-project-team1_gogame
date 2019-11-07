/**
 * This class represents an intersection on the board. A single stone may occupy an intersection, or the
 * intersection can be empty. An intersection can also be a liberty to an adjacent group.
 * @author morrowch
 *
 */
public class Intersection {
	
	private int xPosition;
	private int yPosition;
	private Stone stone;
	
	/**
	 * An intersection is defined by an x and y coordinate
	 * @param x
	 * @param y
	 */
	public Intersection(int x, int y) {
		xPosition = x;
		yPosition = y;
	}

	/**
	 * Returns the x coordinate of the intersection
	 * @return
	 */
	public int getxPosition() {
		return xPosition;
	}

	/**
	 * Sets the x coordinate of the intersection
	 * @param xPosition
	 */
	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	/**
	 * Gets the y coordinate of the intersection
	 * @return
	 */
	public int getyPosition() {
		return yPosition;
	}

	/**
	 * Sets the y coordinate of the intersection
	 * @param yPosition
	 */
	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	
	/**
	 * Gets the stone placed at the intersection. If no stone is on the intersection, this will return null
	 * @return
	 */
	public Stone getStone() {
		return stone;
	}
	
	/**
	 * Place a stone at this intersection
	 * @param stone
	 */
	public void setStone(Stone stone) {
		this.stone = stone;
	}

}
