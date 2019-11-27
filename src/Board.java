import java.awt.Color;
import java.util.ArrayList;

/**
 * This class keeps track of the current state of the game board. This includes a list of all 
 * stones currently in the game, which intersections they occupy, and to which groups they belong
 * This class is in charge of validating moves made by the players. In addition, it is responsible for
 * updating the position of the board in accordance with the rules of Go, once a move has been made.
 * @author morrowch
 *
 */
public class Board {

	private Intersection[][] intersections;
	private ArrayList<Group> groups;
	private ArrayList<Stone> stones;
	private int capturedWhiteStones;
	private int capturedBlackStones;
	private int size;
	private ArrayList<String> boardPositions; // Keeps a record of the game position after each move
	private String annotation = "";

	// Invalid move messages
	public String NO_LIBERTIES_MESSAGE = "Placed stone has no liberties";
	public String KO_MESSAGE = "Move violates rule of Ko";
	public String SUPER_KO_MESSAGE = "Move violates rule of Super Ko";

	/**
	 * Constructor, creating an array of intersections based on the desired size of the board
	 * @param size
	 */
	public Board(int size) {
		this.size = size;
		groups = new ArrayList<Group>();
		stones = new ArrayList<Stone>();
		boardPositions = new ArrayList<String>();
		String boardPosition = "";

		intersections = new Intersection[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Intersection intersection = new Intersection(x, y);
				intersections[x][y] = intersection;
				boardPosition += Integer.toString(x) + Integer.toString(y) + ",";
			}
		}
		boardPositions.add(boardPosition);
		capturedBlackStones = 0;
		capturedWhiteStones = 0;
	}

	public Board(int size, ArrayList<Move> moves) { 
		this(size);
		try {
			for (Move move : moves) {
				this.placeStone(move.getColor(), move.getX(), move.getY());
			}
		} catch(Exception e) { 
			System.out.println("Unable to construct board with given list of moves.");
		}
	}

	/**
	 * placeStone method with move as an input
	 * @param move
	 * @throws IllegalArgumentException
	 */
	public void placeStone(Move move) throws IllegalArgumentException {
		Color color = move.getColor();
		this.annotation = move.getAnnotation();
		if (move.getIsPass()) {
			this.pass();
		} else {
			int x = move.getX();
			int y = move.getY();
			this.placeStone(color, x, y);
		}
	}


	/**
	 * Method to place a stone on the board. The general flow is to first validate the move,
	 * and then make the corresponding updates to the board position.
	 * @param color
	 * @param x
	 * @param y
	 */
	public void placeStone(Color color, int x, int y) throws IllegalArgumentException {

		// Check whether intersection exists
		if (x >= size || y >= size) {
			throw new IllegalArgumentException("Invalid intersection specified");
		}
		
		// Check whether the intersection is already occupied
		if (intersections[x][y].getStone() != null) {
			throw new IllegalArgumentException("A stone is already on that intersection");
		}

		// Create a new stone with given color and location
		Stone placedStone = new Stone(color, x, y, this);

		// Update stones and intersections. Note that these changes need to be reverted in the event that the move is invalid
		stones.add(placedStone);
		intersections[x][y].setStone(placedStone);
		// Create a new group for the placed stone. In the event that this stone is adjacent to other stones of the same color, those stones' groups will be merged into this one
		Group placedStoneGroup = new Group(placedStone);
		placedStone.setGroup(placedStoneGroup);

		// Create two ArrayLists to keep track of which groups need to be merged and which need to be captured
		ArrayList<Group> mergedGroups = new ArrayList<Group>();
		ArrayList<Group> capturedGroups = new ArrayList<Group>();

		// Validate the move
		try {
			validateMove(placedStone, placedStoneGroup, mergedGroups, capturedGroups);
		}
		catch(IllegalArgumentException e) {
			throw e;
		}

		// If the move was valid, update the board position
		updateBoard(placedStone, placedStoneGroup, mergedGroups, capturedGroups);
	}

	/**
	 * Validates that the move is legal without changing the state of the board (too much).
	 * A move is invalid if either the placed stone would immediately die (has no liberties),
	 * or if the rules of Ko or Super Ko are violated.
	 * Once the move has been validated, the actual updates to the board are performed by a different method.
	 * This is to avoid needing to "undo" a bunch of changes to the board state if the move is deemed invalid
	 * after making several changes to the board state.
	 * 
	 * @param placedStone
	 * @param placedStoneGroup
	 * @param mergedGroups
	 * @param capturedGroups
	 */
	public void validateMove(Stone placedStone, Group placedStoneGroup, ArrayList<Group> mergedGroups, ArrayList<Group> capturedGroups) {

		// Indicates whether a group has been captured
		Boolean groupIsCaptured = false;

		// Cycles through each of the intersections adjacent to the placed stone
		for (Intersection intersection : placedStone.getAdjacentIntersections(this)) {			
			if (intersection.getStone() == null) {continue;}
			Stone adjacentStone = intersection.getStone();

			// If the stone is adjacent to a stone of the same color, then that stone's group should be merged into the placed stone's group
			if (adjacentStone.getColor() == placedStone.getColor()) {
				if (!mergedGroups.contains(adjacentStone.getGroup())) {
					mergedGroups.add(adjacentStone.getGroup());
				}
			}
			// Otherwise, check if the adjacent opposing stone is captured or not.
			else {
				if (adjacentStone.getGroup().getLiberties(this).size() == 0) {
					if (!capturedGroups.contains(adjacentStone.getGroup())) {
						capturedGroups.add(adjacentStone.getGroup());
						// Flag that a group has been captured. This is used later in checking that Ko is not violated
						groupIsCaptured = true;
					}
				}
			}
		}

		// Create a temporary group to validate that it will have liberties (i.e. that it is a valid group)

		Group validateGroup = new Group(placedStone);
		for (Group mergedGroup : mergedGroups) {
			validateGroup.addGroup(mergedGroup);
		}

		if (!groupIsCaptured && validateGroup.getLiberties(this).size() == 0) {
			// If the group doesn't have liberties, the move is invalid... undo the changes caused by the placed stone and throw the appropriate exception
			placedStoneGroup.getStones().remove(placedStone);
			placedStone.getIntersection().setStone(null);
			stones.remove(placedStone);
			throw new IllegalArgumentException(NO_LIBERTIES_MESSAGE);
		}

		// Check if Ko is violated
		// Firstly grab all captured stones, so as not to include them in the updated board position
		String boardPosition = "";
		ArrayList<Stone> capturedStones = new ArrayList<Stone>();
		for (Group capturedGroup : capturedGroups) {
			for (Stone capturedStone : capturedGroup.getStones()) {
				if (!capturedStones.contains(capturedStone)) {
					capturedStones.add(capturedStone);
				}
			}
		}

		// Cycle through each stone on the board and record which stones are on which intersections (not including captured stones)
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if ((intersections[x][y].getStone() == null) || capturedStones.contains(intersections[x][y].getStone())) {
					boardPosition += Integer.toString(x) + Integer.toString(y) + ","; 
				} else if (intersections[x][y].getStone().getColor() == Color.WHITE) {
					boardPosition += "w" + Integer.toString(x) + Integer.toString(y) + ","; 
				} else if (intersections[x][y].getStone().getColor() == Color.BLACK) {
					boardPosition += "b" + Integer.toString(x) + Integer.toString(y) + ",";
				}
			}
		}

		// Check if the position prior to the new one is identical. If it is, this move violates Ko
		if ((boardPositions.size() > 1) && (boardPosition.equals(boardPositions.get(boardPositions.size() - 2)))) {
			// Undo all changes caused by placing a stone
			placedStoneGroup.getStones().remove(placedStone);
			placedStone.getIntersection().setStone(null);
			stones.remove(placedStone);
			throw new IllegalArgumentException(KO_MESSAGE);
		}

		// Check if the position prior to the new one has occurred some time previously. If so, this move violates Super Ko
		for (String priorPosition : boardPositions) {
			if (boardPosition.equals(priorPosition)) {
				// Again, undo all changes caused by placing a stone
				placedStoneGroup.getStones().remove(placedStone);
				placedStone.getIntersection().setStone(null);
				stones.remove(placedStone);
				throw new IllegalArgumentException(SUPER_KO_MESSAGE);
			}
		}

		// Lastly, if the move has been validated, add the updated position to the running list of board positions
		boardPositions.add(boardPosition);
	}

	/**
	 * Updates all groups on the board after verifying that the performed move is valid. 
	 * The inputs include the groups to be merged and groups to be captured that were determined during move validation
	 * @param placedStone
	 * @param placedStoneGroup
	 * @param mergedGroups
	 * @param capturedGroups
	 */
	public void updateBoard(Stone placedStone, Group placedStoneGroup, ArrayList<Group> mergedGroups, ArrayList<Group> capturedGroups) {

		placedStone.setGroup(placedStoneGroup);
		if (!groups.contains(placedStoneGroup)) {
			groups.add(placedStoneGroup);
		}

		// For each of the groups to be merged, merge them in
		for (Group mergedGroup : mergedGroups) {
			placedStoneGroup.addGroup(mergedGroup);
			groups.remove(mergedGroup);
		}

		// For each of the groups that are captured, capture them
		for (Group capturedGroup : capturedGroups) {
			// Firstly remove the individual stones
			for (Stone capturedStone : capturedGroup.getStones()) {
				capturedStone.getIntersection().setStone(null);
				stones.remove(capturedStone);
			}

			groups.remove(capturedGroup);

			// Then remove the groups as a whole
			if (capturedGroup.getColor() == Color.WHITE) {
				capturedWhiteStones += capturedGroup.getStones().size();
			} else {
				capturedBlackStones += capturedGroup.getStones().size();
			}	
		}

	}

	/**
	 * Passes a move. The only effect is that the board positions are updated with a duplicate position of the board
	 */
	public void pass() {
		// Cycle through each stone on the board and record which stones are on which intersections
		String boardPosition = "";
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if ((intersections[x][y].getStone() == null)) {
					boardPosition += Integer.toString(x) + Integer.toString(y) + ","; 
				} else if (intersections[x][y].getStone().getColor() == Color.WHITE) {
					boardPosition += "w" + Integer.toString(x) + Integer.toString(y) + ","; 
				} else if (intersections[x][y].getStone().getColor() == Color.BLACK) {
					boardPosition += "b" + Integer.toString(x) + Integer.toString(y) + ",";
				}
			}
		}

		boardPositions.add(boardPosition);
	}


	/**
	 * Returns list of all intersections on the board
	 * @return
	 */
	public Intersection[][] getIntersections() {
		return intersections;
	}

	/**
	 * Returns the current list of all groups on the board
	 * @return
	 */
	public ArrayList<Group> getGroups() {
		return groups;
	}

	/**
	 * Returns current list of stones on the board
	 * @return
	 */
	public ArrayList<Stone> getStones() {
		return stones;
	}

	/**
	 * Returns number of captured white stones
	 * @return
	 */
	public int getCapturedWhiteStones() {
		return capturedWhiteStones;
	}

	/**
	 * Returns list of captured black stones
	 * @return
	 */
	public int getCapturedBlackStones() {
		return capturedBlackStones;
	}

	/**
	 * Returns size (vertical or horizontal dimenstion)
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns list of past board positions
	 * @return
	 */
	public ArrayList<String> getBoardPositions() {
		return boardPositions;
	}

	/**
	 * Gets the color of the stone at a given intersection. If no stone is at the intersection, the method returns null
	 * @param x
	 * @param y
	 * @return
	 */
	public Color getStoneColor(int x, int y) {
		Color color = null;
		if (intersections[x][y].getStone() != null) {
			color = intersections[x][y].getStone().getColor();
		}
		return color;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

}
