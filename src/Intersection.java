
public class Intersection {
	
	private int xPosition;
	private int yPosition;
	private Stone stone;
	
	public Intersection(int x, int y) {
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
	
	public Stone getStone() {
		return stone;
	}
	
	public void setStone(Stone stone) {
		this.stone = stone;
	}

}
