/**
 * @author Chuan
 */
public class Point {
	
	private int xPosition;
	private int yPosition;
	private String status;
	private Area area;
	private int chainGroup;
	private String logicalChainGroup;
	private int value; 

	/**
	 * Point is essentially intersection on the board.
	 * @param s - status of a point, which is one of the three options: black, white, empty
	 * @param x - x position of a point
	 * @param y - y position of a point
	 */
	public Point(String s, int x, int y) {
		status = s;
		xPosition = x;
		yPosition = y;
	}

	/**
	 * @return the xPosition
	 */
	public int getxPosition() {
		return xPosition;
	}

	/**
	 * @return the yPosition
	 */
	public int getyPosition() {
		return yPosition;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * @return the area
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}
	
	/**
	 * @return the chainGroup
	 */
	public int getChainGroup() {
		return chainGroup;
	}
	
	/**
	 * @param chainGroup the chainGroup to set
	 */
	public void setChainGroup(int chainGroup) {
		this.chainGroup = chainGroup;
	}
	
	/**
	 * @return the logicalChainGroup
	 */
	public String getLogicalChainGroup() {
		return logicalChainGroup;
	}
	
	/**
	 * @param logicalChainGroup the logicalChainGroup to set
	 */
	public void setLogicalChainGroup(String logicalChainGroup) {
		this.logicalChainGroup = logicalChainGroup;
	}
	
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

}
