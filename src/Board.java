import java.awt.Color;
import java.util.ArrayList;

public class Board {

    private Intersection[][] intersections;
    private ArrayList<Group> groups;
    private ArrayList<Stone> stones;
    private int capturedWhiteStones;
    private int capturedBlackStones;
    private int size;
    private ArrayList<String> boardPositions; // Keeps a record of the game position after each move

    public String NO_LIBERTIES_MESSAGE = "Placed stone has no liberties";
    public String KO_MESSAGE = "Move violates rule of Ko";

    public Board(int size) {
	this.size = size;
	groups = new ArrayList<Group>();
	stones = new ArrayList<Stone>();
	boardPositions = new ArrayList<String>();

	intersections = new Intersection[size][size];
	for (int x = 0; x < size; x++) {
	    for (int y = 0; y < size; y++) {
		Intersection intersection = new Intersection(x, y);
		intersections[x][y] = intersection;
	    }
	}
	capturedBlackStones = 0;
	capturedWhiteStones = 0;
    }


    /**
     * When a stone is placed, a new stone is created, it is added to the total list of stones, and the intersection is updated
     * updatedGroups is then called
     * removedCapturedGroups is then called
     * @param color
     * @param x
     * @param y
     */
    public void placeStone(Color color, int x, int y) throws IllegalArgumentException {

	if (intersections[x][y].getStone() != null) {
	    throw new IllegalArgumentException("A stone is already on that intersection");
	}

	Stone placedStone = new Stone(color, x, y, this); 	// Create a new stone with given color and location

	stones.add(placedStone);							// Update stones
	intersections[x][y].setStone(placedStone); 			// Update that board location
	Group placedStoneGroup = new Group(placedStone);
	placedStone.setGroup(placedStoneGroup);

	ArrayList<Group> mergedGroups = new ArrayList<Group>();
	ArrayList<Group> capturedGroups = new ArrayList<Group>();

	try {
	    validateMove(placedStone, placedStoneGroup, mergedGroups, capturedGroups);
	}
	catch(IllegalArgumentException e) {
	    throw e;
	}

	updateBoard(placedStone, placedStoneGroup, mergedGroups, capturedGroups);

    }

    /**
     * Validates that the move is legal without changing the state of the board (too much)
     * @param placedStone
     * @param placedStoneGroup
     * @param mergedGroups
     * @param capturedGroups
     */
    public void validateMove(Stone placedStone, Group placedStoneGroup, ArrayList<Group> mergedGroups, ArrayList<Group> capturedGroups) {

	Boolean groupIsCaptured = false;

	for (Intersection intersection : placedStone.getAdjacentIntersections(this)) {			
	    if (intersection.getStone() == null) {continue;}
	    Stone adjacentStone = intersection.getStone();

	    if (adjacentStone.getColor() == placedStone.getColor()) { // If the stone is adjacent to a stone of the same color...

		if (!mergedGroups.contains(adjacentStone.getGroup())) {
		    mergedGroups.add(adjacentStone.getGroup());
		}
	    }
	    else {
		if (adjacentStone.getGroup().getLiberties(this).size() == 0) {
		    if (!capturedGroups.contains(adjacentStone.getGroup())) {
			capturedGroups.add(adjacentStone.getGroup()); // Else, if the adjacent stone is of the opposite color, check if that group is captured
			groupIsCaptured = true;
		    }
		}
	    }
	}

	Group validateGroup = new Group(placedStone);
	for (Group mergedGroup : mergedGroups) {
	    validateGroup.addGroup(mergedGroup);
	}

	if (!groupIsCaptured && validateGroup.getLiberties(this).size() == 0) {
	    // Remove placed stone, update liberties again
	    placedStoneGroup.getStones().remove(placedStone);
	    placedStone.getIntersection().setStone(null);
	    stones.remove(placedStone);
	    throw new IllegalArgumentException(NO_LIBERTIES_MESSAGE);
	}

	// Check if Ko is violated:

	String boardPosition = "";
	ArrayList<Stone> capturedStones = new ArrayList<Stone>();
	for (Group capturedGroup : capturedGroups) {
	    for (Stone capturedStone : capturedGroup.getStones()) {
		if (!capturedStones.contains(capturedStone)) {
		    capturedStones.add(capturedStone);
		}
	    }
	}

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

	if ((boardPositions.size() > 1) && (boardPosition.equals(boardPositions.get(boardPositions.size() - 2)))) {
	    placedStoneGroup.getStones().remove(placedStone);
	    placedStone.getIntersection().setStone(null);
	    stones.remove(placedStone);
	    throw new IllegalArgumentException(KO_MESSAGE);
	}

	boardPositions.add(boardPosition);
    }

    /*
     * Updates all groups on the board after verifying that the performed move is valid
     */
    public void updateBoard(Stone placedStone, Group placedStoneGroup, ArrayList<Group> mergedGroups, ArrayList<Group> capturedGroups) {

	placedStone.setGroup(placedStoneGroup);
	if (!groups.contains(placedStoneGroup)) {
	    groups.add(placedStoneGroup);
	}

	for (Group mergedGroup : mergedGroups) {
	    placedStoneGroup.addGroup(mergedGroup);
	    groups.remove(mergedGroup);
	}

	for (Group capturedGroup : capturedGroups) {
	    for (Stone capturedStone : capturedGroup.getStones()) {
		capturedStone.getIntersection().setStone(null);
		stones.remove(capturedStone);
	    }

	    groups.remove(capturedGroup);

	    if (capturedGroup.getColor() == Color.WHITE) {
		capturedWhiteStones += capturedGroup.getStones().size();
	    } else {
		capturedBlackStones += capturedGroup.getStones().size();
	    }	
	}

    }


    public Intersection[][] getIntersections() {
	return intersections;
    }

    public ArrayList<Group> getGroups() {
	return groups;
    }

    public ArrayList<Stone> getStones() {
	return stones;
    }

    public int getCapturedWhiteStones() {
	return capturedWhiteStones;
    }

    public int getCapturedBlackStones() {
	return capturedBlackStones;
    }

    public int getSize() {
	return size;
    }

    public ArrayList<String> getBoardPositions() {
	return boardPositions;
    }

    public Color getStoneColor(int x, int y) {
	Color color = null;
	if (intersections[x][y].getStone() != null) {
	    color = intersections[x][y].getStone().getColor();
	}
	return color;
    }

}
