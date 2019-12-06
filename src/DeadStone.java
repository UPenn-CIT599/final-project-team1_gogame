/**
 * @author Chuan
 */
import java.awt.Color;

public class DeadStone {
	
	private int xPosition;
	private int yPosition;
	private Color stoneColor;
	
	public DeadStone(Color c, int x, int y) {
		stoneColor = c;
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
	 * @param xPosition the xPosition to set
	 */
	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}
	
	/**
	 * @param yPosition the yPosition to set
	 */
	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	
	/**
	 * @return the stoneColor
	 */
	public Color getColor() {
		return stoneColor;
	}

}
