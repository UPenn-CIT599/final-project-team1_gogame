import java.awt.Color;
import java.util.ArrayList;

public class Group {

	private Color color;
	private ArrayList<Stone> stones = new ArrayList<Stone>();

	public Group() {

	}

	public Group(Stone stone) {
		color = stone.getColor();
		stones = new ArrayList<Stone>();
		stones.add(stone);
		stone.setGroup(this);
	}

	/**
	 * Used for when two groups become one
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

		for (Stone stone : stones) { 
			for (Intersection intersection : stone.getAdjacentIntersections(board)) {
				if ( (intersection.getStone() == null) && !liberties.contains(intersection)) {
					liberties.add(intersection);
				}
			}
		}

		return liberties;

	}

	public ArrayList<Stone> getStones() {
		return stones;
	}

	public Color getColor() {
		return color;
	}

}
