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
	
	public Color getColor() {
		return stoneColor;
	}

}
