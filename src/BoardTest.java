import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import org.junit.jupiter.api.Test;

class BoardTest {

	@Test
	final void test1() {

		// Make a few sample moves, capturing one black stone in the process
		Board board = new Board(5);
		board.placeStone(Color.BLACK, 0, 0);
		board.placeStone(Color.WHITE, 0, 1);
		board.placeStone(Color.BLACK, 3, 3);
		board.placeStone(Color.WHITE, 1, 0);
		board.placeStone(Color.BLACK, 3, 4);


		assertEquals(board.getCapturedBlackStones(), 1);
		assertEquals(board.getCapturedWhiteStones(), 0);
		assertEquals(board.getGroups().size(), 3);
		assertEquals(board.getStones().size(), 4);

		// Check board positions
		assertEquals(board.getBoardPositions().get(0), 
				"00,01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(1), 
				"b00,01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(2), 
				"b00,w01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(3), 
				"b00,w01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,b33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(4), 
				"00,w01,02,03,04,w10,11,12,13,14,20,21,22,23,24,30,31,32,b33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(5), 
				"00,w01,02,03,04,w10,11,12,13,14,20,21,22,23,24,30,31,32,b33,b34,40,41,42,43,44,");
	}


	@Test
	public void noLibertiesException() {
		Board board = new Board(5);

		board.placeStone(Color.BLACK, 1, 0);
		board.placeStone(Color.WHITE, 1, 1);
		board.placeStone(Color.BLACK, 0, 1);

		try {
			board.placeStone(Color.WHITE, 0, 0);
			fail("No liberties exception was not thrown");
		}
		catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), board.NO_LIBERTIES_MESSAGE);
		}

		assertEquals(board.getCapturedBlackStones(), 0);
		assertEquals(board.getCapturedWhiteStones(), 0);
		assertEquals(board.getGroups().size(), 3);
		assertEquals(board.getStones().size(), 3);

		// Check board positions
		assertEquals(board.getBoardPositions().get(0), 
				"00,01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(1), 
				"00,01,02,03,04,b10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(2), 
				"00,01,02,03,04,b10,w11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(3), 
				"00,b01,02,03,04,b10,w11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
	}

	@Test
	public void koException() {
		Board board = new Board(5);

		board.placeStone(Color.BLACK, 0, 0);
		board.placeStone(Color.WHITE, 1, 0);
		board.placeStone(Color.BLACK, 1, 1);
		board.placeStone(Color.WHITE, 0, 1);
		board.placeStone(Color.BLACK, 0, 2);
		board.placeStone(Color.WHITE, 3, 3);
		board.placeStone(Color.BLACK, 0, 0);

		try {
			board.placeStone(Color.WHITE, 0, 1);
			fail("Ko exception was not thrown");
		}
		catch (IllegalArgumentException e) {
			assertEquals(e.getMessage(), board.KO_MESSAGE);
		}

		assertEquals(board.getCapturedBlackStones(), 1);
		assertEquals(board.getCapturedWhiteStones(), 1);
		assertEquals(board.getGroups().size(), 5);
		assertEquals(board.getStones().size(), 5);

		// Check board positions
		assertEquals(board.getBoardPositions().get(0), 
				"00,01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(1), 
				"b00,01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(2), 
				"b00,01,02,03,04,w10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(3), 
				"b00,01,02,03,04,w10,b11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(4), 
				"00,w01,02,03,04,w10,b11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(5), 
				"00,w01,b02,03,04,w10,b11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(6), 
				"00,w01,b02,03,04,w10,b11,12,13,14,20,21,22,23,24,30,31,32,w33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(7), 
				"b00,01,b02,03,04,w10,b11,12,13,14,20,21,22,23,24,30,31,32,w33,34,40,41,42,43,44,");

	}

	@Test
	public void testPass() {
		Board board = new Board(5);

		board.placeStone(Color.BLACK, 3, 3);
		board.placeStone(Color.WHITE, 1, 1);
		board.pass();
		board.pass();

		assertEquals(board.getBoardPositions().get(0), 
				"00,01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(1), 
				"00,01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,b33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(2), 
				"00,01,02,03,04,10,w11,12,13,14,20,21,22,23,24,30,31,32,b33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(3), 
				"00,01,02,03,04,10,w11,12,13,14,20,21,22,23,24,30,31,32,b33,34,40,41,42,43,44,");
		assertEquals(board.getBoardPositions().get(4), 
				"00,01,02,03,04,10,w11,12,13,14,20,21,22,23,24,30,31,32,b33,34,40,41,42,43,44,");
	}

	@Test
	public void simpleFiveByFiveGame() {
		Board board = new Board(5);

		board.placeStone(Color.BLACK, 3, 3);
		board.placeStone(Color.WHITE, 1, 1);
		board.placeStone(Color.BLACK, 1, 3);
		board.placeStone(Color.WHITE, 3, 1);
		board.placeStone(Color.BLACK, 1, 2);
		board.placeStone(Color.WHITE, 2, 1);
		board.placeStone(Color.BLACK, 2, 2);
		board.placeStone(Color.WHITE, 3, 2);
		board.placeStone(Color.BLACK, 4, 2);
		board.placeStone(Color.WHITE, 2, 3);
		board.placeStone(Color.BLACK, 2, 4); // Captures a white stone
		board.placeStone(Color.WHITE, 4, 1);
		board.placeStone(Color.BLACK, 4, 3);
		board.placeStone(Color.WHITE, 0, 2);
		board.placeStone(Color.BLACK, 0, 3);
		board.placeStone(Color.WHITE, 0, 1);

		assertEquals(board.getCapturedBlackStones(), 0);
		assertEquals(board.getCapturedWhiteStones(), 1);
		assertEquals(board.getGroups().size(), 4);
		assertEquals(board.getStones().size(), 15);
	}




}
