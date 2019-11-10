/**
 * @author Chuan
 */
import java.util.HashSet;
import java.awt.Color;

public class Area {
	
	private HashSet<Point> emptyLocationsWithinArea = new HashSet<>();
	private Color areaColor;

	/**
	 * Adds a new empty location to the area
	 * @param emptyLocation
	 */
	public void setEmptyLocation(HashSet<Point> emptyPoints) { 
		emptyLocationsWithinArea = emptyPoints;
		for (Point p : emptyPoints) {
			p.setArea(this);
		}
	}
	
	/**
	 * Returns all the empty locations within an area
	 * @return emptyLocationsWithinArea
	 */
	public HashSet<Point> getEmptyLocations() {
		return emptyLocationsWithinArea;
	}
	
	/**
	 * Sets the color of an area
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
