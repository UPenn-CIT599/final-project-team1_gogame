import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

class ScoreTest {

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
