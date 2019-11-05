/**
 * @author Chuan
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Score {

    private int blackScore, whiteScore, size;

    private String finalBoardPositions;
    private Point[][] finalPositions;
    private ArrayList<Area> areas = new ArrayList<>();

    private ArrayList<Point> emptyLocations = new ArrayList<>();
    private ArrayList<Point> blackStones = new ArrayList<>();
    private ArrayList<Point> whiteStones = new ArrayList<>();

    private ArrayList<Area> blackAreas = new ArrayList<>();
    private ArrayList<Area> whiteAreas = new ArrayList<>();
    private ArrayList<Point> neutralPositions = new ArrayList<>();

    public Score(Board b) {
	this.size = b.getSize();
//	areas = new ArrayList<Area>();
//	emptyLocations = new ArrayList<Point>();
	ArrayList<String> boardPositions = b.getBoardPositions();
	finalBoardPositions = boardPositions.get(boardPositions.size() - 1);
	finalPositions = new Point[size][size];
    }

    /**
     * Categorizes each point/intersection on the board into 3 groups: black, white, empty
     */
    public void categorizePoints() {
	for (String str : finalBoardPositions.split(",")) {
	    for (int x = 0; x < size; x++) {
		for (int y = 0; y < size; y++) {
		    for (char c : str.toCharArray()) {
			if (c == 'b') {
			    Point black = new Point("b", x, y);
			    finalPositions[x][y] = black;
			    blackStones.add(black);
			} else if (c == 'w') {
			    Point white = new Point("w", x, y);
			    finalPositions[x][y] = white;
			    whiteStones.add(white);
			} else {
			    Point empty = new Point("e", x, y);
			    finalPositions[x][y] = empty;
			    emptyLocations.add(empty);
			}
		    }
		}
	    }
	}
    }

    /**
     * Given the positions of the dead stones selected by the players,
     * returns a HashSet that stores all the DeadStone objects
     * @param deadStonePositions
     * @return deadStones
     */
    public HashSet<DeadStone> getDeadStones(HashSet<Integer[]> deadStonePositions) {
	HashSet<DeadStone> deadStones = new HashSet<>();
	Color deadStoneColor = null;
	for (Integer[] position : deadStonePositions) {
	    int xPosition = position[0];
	    int yPosition = position[1];
	    if (finalPositions[xPosition][yPosition].getStatus().equals("b")) {
		deadStoneColor = Color.BLACK;
	    } else {
		deadStoneColor = Color.WHITE;
	    }
	    DeadStone ds = new DeadStone(deadStoneColor, xPosition, yPosition);
	    deadStones.add(ds);
	}
	return deadStones;
    }

    /**
     * Replaces the dead stones with empty locations for score calculation
     * since dead stones/prisoners don't count under Chinese rules
     * @param deadStones
     */
    public void removeDeadStones(HashSet<DeadStone> deadStones) {
	for (DeadStone ds : deadStones) {
	    if (ds.getColor() == Color.BLACK) {
		Point deadBlack = new Point("b", ds.getxPosition(), ds.getyPosition());
		blackStones.remove(deadBlack);
		Point empty = new Point("e", ds.getxPosition(), ds.getyPosition());
		emptyLocations.add(empty);
	    } else if (ds.getColor() == Color.WHITE) {
		Point deadWhite = new Point("w", ds.getxPosition(), ds.getyPosition());
		blackStones.remove(deadWhite);
		Point empty = new Point("e", ds.getxPosition(), ds.getyPosition());
		emptyLocations.add(empty);
	    }
	}
    }

    /**
     * Groups empty locations into areas
     */
    public void combineEmptyLocations() {
	int count = 0;
	for (Point emptyLocation : emptyLocations) {
	    boolean hasEmptyAdjacentPositions = false;
	    for (Point adjacent : Helper.getAdjacentPoints(emptyLocation, size, finalPositions)) {
		if (emptyLocations.contains(adjacent)) {
		    hasEmptyAdjacentPositions = true;
		    if (Helper.checkContainEmptyLocation(adjacent, areas)) {
			adjacent.getArea().addEmptyLocation(emptyLocation);
		    } else {
			Area a = new Area();
			a.addEmptyLocation(emptyLocation);
			areas.add(a);
		    }
		} 
	    }
	    if (!hasEmptyAdjacentPositions) {
		Area a = new Area();
		a.addEmptyLocation(emptyLocation);
		areas.add(a);
	    }
	    count++;
	    System.out.println(count);
	}
    }

    /**
     * Identifies if an area belongs to black or white, or if it is neutral 
     */
    public void checkAreaBlackOrWhite() {
	for (Area a : areas) {
	    HashSet<String> recordSurroundings = new HashSet<>();
	    for (Point emptyLocation : a.getEmptyLocations()) {
		for (Point adjacent : Helper.getAdjacentPoints(emptyLocation, size, finalPositions)) {
		    if (adjacent.getStatus().equals("b")) {
			recordSurroundings.add("b");
		    } else if (adjacent.getStatus().equals("w")) {
			recordSurroundings.add("w");
		    } else {
			recordSurroundings.add("e");
		    }
		}
	    }
	    if (recordSurroundings.contains("b") && recordSurroundings.contains("w")) { // record neutral position
		for (Point p : a.getEmptyLocations()) {
		    neutralPositions.add(p);
		}
	    } else {
		a.setAreaColor(recordSurroundings.toString());
	    }
	    if (a.getAreaColor() == Color.BLACK) {
		blackAreas.add(a);
	    } else if (a.getAreaColor() == Color.WHITE) {
		whiteAreas.add(a);
	    }
	}
    }

    /**
     * Fills the neutral positions with stones
     * If the number of neutral positions is even, each player gets half of all the neutral positions
     * If the number of neutral positions is odd, alternate the assignment of neutral positions to each player
     * by starting with the one that didn't make the last move
     * @param lastMove
     */
    public void fillNeutralPositions(String lastMove) {
	boolean addToBlack = true;
	if (neutralPositions.size() % 2 != 0) { 
	    if (lastMove.contains("b")) {
		addToBlack = !addToBlack;
	    }
	}
	for (Point p : neutralPositions) {
	    if (addToBlack) {
		blackStones.add(p);
		addToBlack = !addToBlack;
	    } else {
		whiteStones.add(p);
		addToBlack = !addToBlack;
	    }
	}
    }

    /**
     * Calculates the score by adding the number of stones and the number of empty locations
     * within all the areas captured by the same color
     * @return scores
     */
    public HashMap<String, Integer> scoring() {
	HashMap<String, Integer> scores = new HashMap<>();
	blackScore = blackStones.size();
	whiteScore = whiteStones.size();
	for (Area a : blackAreas) {
	    blackScore += a.getEmptyLocations().size();
	}
	for (Area a : whiteAreas) {
	    whiteScore += a.getEmptyLocations().size();
	}
	scores.put("blackScore", blackScore);
	scores.put("whiteScore", whiteScore);
	return scores;
    }

}
