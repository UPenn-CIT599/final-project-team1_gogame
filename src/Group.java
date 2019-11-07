import java.awt.Color;
import java.util.ArrayList;

/**
 * This class represents a "Group" of stones on the board. A group consists of one or multiple stones
 * which share liberties. Groups exist as a single unit--you cannot capture individual stones within
 * a group without capturing the entire group.
 * @author morrowch
 *
 */
public class Group {

	private Color color;
	private ArrayList<Stone> stones = new ArrayList<Stone>();

	public Group() {

	}

	/**
	 * The constructor of the group must be past an initial stone. This is because a group cannot 
	 * exist without at least one stone
	 * @param stone
	 */
	public Group(Stone stone) {
		color = stone.getColor();
		stones = new ArrayList<Stone>();
		stones.add(stone);
		stone.setGroup(this);
	}

	/**
	 * Used for when two groups become one. All the stones in the merged group must become part of
	 * this group.
	 * @param group
	 */
	public void addGroup(Group group) {
		for (Stone stone : group.getStones()) {
			stones.add(stone);
			stone.setGroup(this);
		}
	}

	/**
	 * Adds a new stone to the group
	 * @param stone
	 */
	public void addStone(Stone stone) { 
		stones.add(stone);
	}

	/**
	 * Finds all liberties for a group. A liberty is any open intersection adjacent to a group
	 * @param board
	 */
	public ArrayList<Intersection> getLiberties(Board board) {
		ArrayList<Intersection> liberties = new ArrayList<Intersection>();
		
		// Loop through each stone of the group
		for (Stone stone : stones) { 
			for (Intersection intersection : stone.getAdjacentIntersections(board)) {
				// Check if the intersection is empty, and whether it has been added to the ArrayList yet
				if ( (intersection.getStone() == null) && !liberties.contains(intersection)) {
					liberties.add(intersection);
				}
			}
		}

		return liberties;
	}

	/**
	 * Returns the stones that are a part of this group
	 * @return
	 */
	public ArrayList<Stone> getStones() {
		return stones;
	}

	/**
	 * Returns the color of the group. A group must consist of stones of the same color.
	 * @return
	 */
	public Color getColor() {
		return color;
	}

}
