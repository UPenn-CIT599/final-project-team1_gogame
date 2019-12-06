import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

class ScoreTest {
	
	/**
	 * This test checks if the categorizePoints method in the Score class properly
	 * takes in the final positions from the Board class and categorizes the points 
	 * into the 3 groups: black, white, and empty.
	 * 
	 * The test begins with placing a few stones on the board and then verifies the 
	 * color, and the x and y positions of the placed stones.
	 */
	@Test
	void categorizePointsTest() {
		double komi = 7.5;
		Board board = new Board(5);
		board.placeStone(Color.BLACK, 0, 0);
		board.placeStone(Color.WHITE, 0, 1);
		board.placeStone(Color.BLACK, 3, 3);
		board.placeStone(Color.WHITE, 1, 0);
		board.placeStone(Color.BLACK, 3, 4);
		Score score = new Score(board, komi);
		score.categorizePoints();
		
		assertEquals(score.getFinalBoardPositions(), 
				"00,w01,02,03,04,w10,11,12,13,14,20,21,22,23,24,30,31,32,b33,b34,40,41,42,43,44,");
		
		assertEquals(score.getBlackStones().get(0).getStatus(), "b");
		assertEquals(score.getBlackStones().get(0).getxPosition(), 3);
		assertEquals(score.getBlackStones().get(0).getyPosition(), 3);
		
		assertEquals(score.getBlackStones().get(1).getStatus(), "b");
		assertEquals(score.getBlackStones().get(1).getxPosition(), 3);
		assertEquals(score.getBlackStones().get(1).getyPosition(), 4);
		
		assertEquals(score.getWhiteStones().get(0).getStatus(), "w");
		assertEquals(score.getWhiteStones().get(0).getxPosition(), 0);
		assertEquals(score.getWhiteStones().get(0).getyPosition(), 1);
		
		assertEquals(score.getWhiteStones().get(1).getStatus(), "w");
		assertEquals(score.getWhiteStones().get(1).getxPosition(), 1);
		assertEquals(score.getWhiteStones().get(1).getyPosition(), 0);
		
		assertEquals(score.getEmptyLocations().get(0).getStatus(), "e");
		assertEquals(score.getEmptyLocations().get(0).getxPosition(), 0);
		assertEquals(score.getEmptyLocations().get(0).getyPosition(), 0);
	}
	
	/**
	 * This test checks if the dead stones are correctly removed, the ownership of
	 * each area is correctly determined, and the final scores are correctly calculated.
	 * 
	 * The game used for testing has one dead white stone at the x=4, y=2 position. There 
	 * are 3 areas in total: one belongs to white and two belong to black. Each area has
	 * 5, 4 and 2 empty points, respectively.
	 * 
	 * Before removing the dead white stone, there are 8 black stones, 7 white stones, and 
	 * 10 empty points on the board. After removing the dead white stone, there should be 8 
	 * black stones, 6 white stones, and 11 empty points.
	 * 
	 * The black score = 8 stones + 6 empty points enclosed = 14
	 * The white score = 6 stones + 5 empty points enclosed + 7.5 seki = 18.5
	 */
	@Test
	void deadStonesAndAreaOwnershipAndScoringTests() {
		double komi = 7.5;
		Board board = new Board(5);
		board.placeStone(Color.BLACK, 3, 1);
		board.placeStone(Color.WHITE, 1, 3);
		board.placeStone(Color.BLACK, 3, 3);
		board.placeStone(Color.WHITE, 1, 1);
		board.placeStone(Color.BLACK, 2, 2);
		board.placeStone(Color.WHITE, 1, 2);
		board.placeStone(Color.BLACK, 2, 0);
		board.placeStone(Color.WHITE, 1, 0);
		board.placeStone(Color.BLACK, 2, 4);
		board.placeStone(Color.WHITE, 1, 4);
		board.placeStone(Color.BLACK, 2, 3);
		board.placeStone(Color.WHITE, 2, 1);
		board.placeStone(Color.BLACK, 3, 0);
		board.placeStone(Color.WHITE, 4, 2);
		board.placeStone(Color.BLACK, 4, 3);
		Score score = new Score(board, komi);
		score.categorizePoints();
		HashSet<Integer[]> deadStonePositions = new HashSet<>();
		Integer[] deadStoneLocation = new Integer[2];
		deadStoneLocation[0] = 4;
		deadStoneLocation[1] = 2;
		deadStonePositions.add(deadStoneLocation);
		HashSet<DeadStone> deadStones = score.getDeadStones(deadStonePositions);
		
		assertEquals(deadStones.iterator().next().getColor(), Color.WHITE);
		assertEquals(deadStones.iterator().next().getxPosition(), 4);
		assertEquals(deadStones.iterator().next().getyPosition(), 2);
		
		assertEquals(score.getBlackStones().size(), 8);
		assertEquals(score.getWhiteStones().size(), 7);
		assertEquals(score.getEmptyLocations().size(), 10);
		
		score.removeDeadStones(deadStones);
		assertEquals(score.getFinalPositions()[4][2].getStatus(), "e");
		assertEquals(score.getBlackStones().size(), 8);
		assertEquals(score.getWhiteStones().size(), 6);
		assertEquals(score.getEmptyLocations().size(), 11);
		
		score.checkAreaOwnership();
		assertEquals(score.getAreas().size(), 3);
		assertEquals(score.getAreas().get(0).getAreaColor(), Color.WHITE);
		assertEquals(score.getAreas().get(0).getEmptyLocations().size(), 5);
		assertEquals(score.getAreas().get(1).getAreaColor(), Color.BLACK);
		assertEquals(score.getAreas().get(1).getEmptyLocations().size(), 4);
		assertEquals(score.getAreas().get(2).getAreaColor(), Color.BLACK);
		assertEquals(score.getAreas().get(2).getEmptyLocations().size(), 2);
		assertEquals(score.getNeutralPositions().size(), 0);
		
		int sekiCount = score.checkSeki();
		HashMap<String, Double> finalScores = score.scoring("b", sekiCount);
		assertEquals(finalScores.get("blackScore"), 14);
		assertEquals(finalScores.get("whiteScore"), 18.5);
	}
	
	/**
	 * This test checks if seki can be identified, the number of neutral points that
	 * constitute seki is correctly computed, and the final scores are correctly calculated.
	 * 
	 * The game used for testing has one seki neutral point at the x=5, y=5 position. There 
	 * is one normal neutral point (also called dame) at the the x=3, y=0 position.
	 * 
	 * There is no dead stone in this game. There are 16 black stones and 17 white stones.
	 * There are 9 points that belong to black and 5 points that belong to white. The last
	 * move is made by white, meaning the one dame will be assigned to black. The seki neutral
	 * point will be divided evenly between black and white, meaning each gets 0.5 point.
	 * 
	 * The black score = 16 stones + 9 empty points enclosed + 1 dame + 0.5 seki = 26.5
	 * The white score = 17 stones + 5 empty points enclosed + 0.5 seki + 7.5 seki = 30
	 */
	@Test
	void sekiAndScoringTests() {
		double komi = 7.5;
		Board board = new Board(7);
		board.placeStone(Color.BLACK, 6, 5);
		board.placeStone(Color.WHITE, 6, 4);
		board.placeStone(Color.BLACK, 5, 6);
		board.placeStone(Color.WHITE, 6, 2);
		board.placeStone(Color.BLACK, 6, 1);
		board.placeStone(Color.WHITE, 5, 2);
		board.placeStone(Color.BLACK, 5, 1);
		board.placeStone(Color.WHITE, 5, 4);
		board.placeStone(Color.BLACK, 4, 2);
		board.placeStone(Color.WHITE, 5, 3);
		board.placeStone(Color.BLACK, 4, 3);
		board.placeStone(Color.WHITE, 4, 4);
		board.placeStone(Color.BLACK, 3, 4);
		board.placeStone(Color.WHITE, 4, 5);
		board.placeStone(Color.BLACK, 3, 5);
		board.placeStone(Color.WHITE, 4, 6);
		board.placeStone(Color.BLACK, 3, 6);
		board.placeStone(Color.WHITE, 2, 2);
		board.placeStone(Color.BLACK, 2, 3);
		board.placeStone(Color.WHITE, 1, 3);
		board.placeStone(Color.BLACK, 1, 4);
		board.placeStone(Color.WHITE, 1, 2);
		board.placeStone(Color.BLACK, 3, 2);
		board.placeStone(Color.WHITE, 3, 1);
		board.placeStone(Color.BLACK, 4, 1);
		board.placeStone(Color.WHITE, 2, 0);
		board.placeStone(Color.BLACK, 4, 0);
		board.placeStone(Color.WHITE, 0, 4);
		board.placeStone(Color.BLACK, 0, 5);
		board.placeStone(Color.WHITE, 0, 3);
		board.placeStone(Color.BLACK, 1, 5);
		board.placeStone(Color.WHITE, 0, 1);
		board.pass();
		board.placeStone(Color.WHITE, 2, 1);
		Score score = new Score(board, komi);
		score.categorizePoints();
		score.checkAreaOwnership();
		int sekiCount = score.checkSeki();
		HashMap<String, Double> finalScores = score.scoring("w", sekiCount);
		
		assertEquals(score.getNeutralPositions().size(), 2);
		assertEquals(score.getNeutralPositions().get(0).getxPosition(), 3);
		assertEquals(score.getNeutralPositions().get(0).getyPosition(), 0);
		assertEquals(score.getNeutralPositions().get(1).getxPosition(), 5);
		assertEquals(score.getNeutralPositions().get(1).getyPosition(), 5);
		
		assertEquals(sekiCount, 1);
		
		assertEquals(finalScores.get("blackScore"), 26.5);
		assertEquals(finalScores.get("whiteScore"), 30);
	}

}
