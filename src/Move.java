import java.awt.Color;

public class Move {
	
	private Color color;
	private int x;
	private int y;
	private String annotation;
	
	public Move(Color color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public Color getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
