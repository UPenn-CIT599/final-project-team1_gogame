/**
 * @author Chuan
 */
public class Point {
	
	private int xPosition;
	private int yPosition;
	private String status;
	private Area area;
	
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

	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String s) {
		this.status = s;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area a) {
		this.area = a;
	}

}
