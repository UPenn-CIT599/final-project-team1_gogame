/**
 * @author Chuan
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Score {
	
	/**
	 * Definition of seki (a Go term):
	 * Seki is a rare case that happens in a Go game. It happens when two groups of stones 
	 * of different colors that are adjacent to each other are both alive because neither 
	 * is able to capture the other. 
	 * In other words, these two groups of stones have common point(s) as their liberties 
	 * so that if one wants to capture the other by filling these point(s), it will run out 
	 * of liberties first and be captured by the other. This is same with the other group.
	 * Therefore, neither player attempts to fill these point(s) and they become neutral 
	 * positions at the end of a game.
	 */

	private int size;
    private double blackScore, whiteScore;

    private String finalBoardPositions;
    private Point[][] finalPositions;

	private ArrayList<Area> areas = new ArrayList<>();

    private ArrayList<Point> emptyLocations = new ArrayList<>();
    private ArrayList<Point> blackStones = new ArrayList<>();
	private ArrayList<Point> whiteStones = new ArrayList<>();

    private ArrayList<Point> neutralPositions = new ArrayList<>();

    public Score(Board b) {
    	this.size = b.getSize();
    	ArrayList<String> boardPositions = b.getBoardPositions();
    	finalBoardPositions = boardPositions.get(boardPositions.size() - 1);
    	finalPositions = new Point[size][size];
    }

    /**
     * According to the final board positions, 
     * this method categorizes each point on the board into 3 groups: 
     * black, white, and empty.
     */
    public void categorizePoints() {
    	String[] point = finalBoardPositions.split(",");
    	int count = 0;
    	for (int x = 0; x < size; x++) {
    		for (int y = 0; y < size; y++) {
    			char[] c = point[count].toCharArray();
    			if (c[0] == 'b') {
    				Point black = new Point("b", x, y);
    				finalPositions[x][y] = black;
    				blackStones.add(black);
    			} else if (c[0] == 'w') {
    				Point white = new Point("w", x, y);
    				finalPositions[x][y] = white;
    				whiteStones.add(white);
    			} else {
    				Point empty = new Point("e", x, y);
    				finalPositions[x][y] = empty;
    				emptyLocations.add(empty);
    			}
    			count++;
    		}
	    }
	}
    
    /**
     * Given the positions of the dead stones selected by the players,
     * this method returns a HashSet that stores all the DeadStone objects.
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
     * This method replaces the dead stones with empty locations 
     * in order to calculate the scores correctly.
     * @param deadStones
     */
    public void removeDeadStones(HashSet<DeadStone> deadStones) {
    	for (DeadStone ds : deadStones) {
    		if (ds.getColor() == Color.BLACK) {
    			blackStones.removeIf(ws -> ws.getxPosition() == ds.getxPosition() && ws.getyPosition() == ds.getyPosition());
    			Point empty = new Point("e", ds.getxPosition(), ds.getyPosition());
    			emptyLocations.add(empty);
    			finalPositions[ds.getxPosition()][ds.getyPosition()] = empty;
    		} else if (ds.getColor() == Color.WHITE) {
    			whiteStones.removeIf(ws -> ws.getxPosition() == ds.getxPosition() && ws.getyPosition() == ds.getyPosition());
    			Point empty = new Point("e", ds.getxPosition(), ds.getyPosition());
    			emptyLocations.add(empty);
    			finalPositions[ds.getxPosition()][ds.getyPosition()] = empty;
    		}
    	}
    }

	/**
     * This method determines the ownership of all the areas on the board.
     * An area belongs to either black or white, or does not belong to any side (neutral). 
     */
    public void checkAreaOwnership() {
    	for (Point empty : emptyLocations) {
    		if (empty.getArea() != null) {
    			continue;
    		} else {
    			ArrayList<HashSet<Point>> emptyPointsAndBorders = ScoreHelper.getChainAndFindReach(empty, size, finalPositions);
        		HashSet<Point> emptyPoints = emptyPointsAndBorders.get(0);
        		Area area = new Area();
        		area.setEmptyLocation(emptyPoints);
            	HashSet<Point> borders = emptyPointsAndBorders.get(1); 
            	ArrayList<Point> borderList = new ArrayList<>();
            	borderList.addAll(borders);
        		String possibleBorderColor = borderList.get(0).getStatus();
        		boolean sameBorderColor = true;
        		for (Point borderPoint : borders) {
        			if (!borderPoint.getStatus().equals(possibleBorderColor)) {
        				sameBorderColor = false;
        				break;
        			}
        		}
        		if (sameBorderColor) {
        			area.setAreaColor(possibleBorderColor);
        		} else {
        			area.setAreaColor("e");
        		}
        		areas.add(area);
    		}
    	}
    	
    	for (Area a : areas) {
    		if (a.getAreaColor() == Color.GRAY) {
    			for (Point neutral : a.getEmptyLocations()) {
    				neutralPositions.add(neutral);
    			}
    		}
    	}
    }
    
    /**
     * This method checks whether a rare case called seki is present on the board
     * and returns the number of neutral positions that occur because of seki.
     * @return sekiCount
     */
    public int checkSeki() {
    	int sekiCount = 0;
    	Point[][] pseudoBlack = finalPositions;
    	Point[][] pseudoWhite = finalPositions;
    	for (Point neutral : neutralPositions) {
    		Point original = finalPositions[neutral.getxPosition()][neutral.getyPosition()];
    		Point black = new Point("b", neutral.getxPosition(), neutral.getyPosition());
    		pseudoBlack[neutral.getxPosition()][neutral.getyPosition()] = black;
    		ArrayList<HashSet<Point>> pseudoBlackChainsAndBorders = ScoreHelper.getChainAndFindReach(black, size, pseudoBlack);
    		HashSet<Point> pseudoBlackReach = pseudoBlackChainsAndBorders.get(1);
    		int pseudoBlackLiberties = ScoreHelper.countLiberties(pseudoBlackReach);
    		Point white = new Point("w", neutral.getxPosition(), neutral.getyPosition());
    		pseudoWhite[neutral.getxPosition()][neutral.getyPosition()] = white;
    		ArrayList<HashSet<Point>> pseudoWhiteChainsAndBorders = ScoreHelper.getChainAndFindReach(white, size, pseudoWhite);
    		HashSet<Point> pseudoWhiteReach = pseudoWhiteChainsAndBorders.get(1);
    		int pseudoWhiteLiberties = ScoreHelper.countLiberties(pseudoWhiteReach);
    		if ((pseudoBlackLiberties == 1) && (pseudoWhiteLiberties == 1)) {
    			sekiCount++;
    		} 
    		pseudoBlack[neutral.getxPosition()][neutral.getyPosition()] = original;
    		pseudoWhite[neutral.getxPosition()][neutral.getyPosition()] = original;
    	}
    	return sekiCount;
    }

    /**
     * This method calculates and returns the scores of both black and white.
     * The base score is calculated by adding the number of stones and the number of empty locations
     * within all the areas owned by the same color.
     * Additional score consists of half of the number of neutral positions in the seki case and 
     * half of the remaining neutral positions (called dames).
     * The neutral positions in the seki case are evenly divided between the two players exactly, 
     * meaning if the number of these neutral positions is odd, a .5 score will occur.
     * If the number of dames is even, each player will get exactly half of the total dames.
     * If the number of dames is odd, the player who made the last move will get one score less than 
     * the other.
     * @param lastMove
     * @param sekiCount
     * @return scores
     */
    public HashMap<String, Double> scoring(String lastMove, int sekiCount) {
    	HashMap<String, Double> scores = new HashMap<>();
    	blackScore = blackStones.size();
    	whiteScore = whiteStones.size();
    	int countNeutralPositions = 0;
    	for (Area a : areas) {
    		if (a.getAreaColor() == Color.BLACK) {
    			blackScore += a.getEmptyLocations().size();
    		} else if (a.getAreaColor() == Color.WHITE) {
    			whiteScore += a.getEmptyLocations().size();
    		} else {
    			countNeutralPositions += a.getEmptyLocations().size();
    		}
    	}
    	blackScore += (countNeutralPositions - sekiCount) / 2;
		whiteScore += (countNeutralPositions - sekiCount) / 2;
		if ((countNeutralPositions - sekiCount) % 2 != 0) {
    		if (lastMove.contains("b")) {
    			whiteScore++;
    		} else {
    			blackScore++;
    		}
    	}
		blackScore += sekiCount / 2.0;
		whiteScore += sekiCount / 2.0;
    	scores.put("blackScore", blackScore);
    	scores.put("whiteScore", whiteScore);
    	return scores;
    }

}
