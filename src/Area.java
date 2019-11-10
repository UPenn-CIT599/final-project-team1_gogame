/**
 * @author Chuan
 */
import java.util.HashSet;
import java.awt.Color;

public class Area {
	
	/**
	 * Area is a group of empty locations adjacent to each other on the board.
	 * An area belongs to either black or white, or does not belong to any side.
	 */
	
	private HashSet<Point> emptyLocationsWithinArea = new HashSet<>();
	private Color areaColor;

	/**
	 * This method assigns a group of adjacent empty locations to an area.
	 * @param emptyPoints
	 */
	public void setEmptyLocation(HashSet<Point> emptyPoints) { 
		emptyLocationsWithinArea = emptyPoints;
		for (Point p : emptyPoints) {
			p.setArea(this);
		}
	}
	
	/**
	 * This method returns all the empty locations within an area.
	 * @return emptyLocationsWithinArea
	 */
	public HashSet<Point> getEmptyLocations() {
		return emptyLocationsWithinArea;
	}
	
	/**
	 * This method sets the color of an area according to its ownership:
	 * Color black - area owned by black
	 * Color white - area owned by white
	 * Color grey - area that does not belong to any side (neutral)
	 * @param color
	 */
	public void setAreaColor(String color) {
		if (color.contains("b")) {
			areaColor = Color.BLACK;
		} else if (color.contains("w")) {
			areaColor = Color.WHITE;
		} else {
			areaColor = Color.GRAY;
		}
	}

	/**
	 * @return areaColor
	 */
	public Color getAreaColor() {
		return areaColor;
	}
	
}
