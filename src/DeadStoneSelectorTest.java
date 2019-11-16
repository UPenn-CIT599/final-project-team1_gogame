import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.util.*;
import org.junit.jupiter.api.Test;

/**
 * The DeadStoneSelectorTest includes unit tests for the DeadStoneSelector
 * class.
 * 
 * @author Chris Hartung
 *
 */
class DeadStoneSelectorTest {

    @Test
    void testDeadStoneSelector() {
	// initialize variables
	Game game = new Game(null, new MainMenu(null));
	Board board = game.getBoard();
	DeadStoneSelector selector = new DeadStoneSelector(game);
	
	// place stones on board
	board.placeStone(Color.BLACK, 0, 2);
	board.placeStone(Color.WHITE, 1, 4);
	board.placeStone(Color.BLACK, 3, 3);
	
	// check that live stones are not counted as dead
	assertFalse(selector.isDeadStone(0, 2));
	
	// check that empty spaces are not counted as dead stones
	assertFalse(selector.isDeadStone(1, 1));
	
	// simulate clicks on a stone and an empty space
	selector.selectStone(0, 2);
	selector.selectStone(1, 1);

	// check that selected stone is dead and selected empty space is not
	assertTrue(selector.isDeadStone(0, 2));
	assertFalse(selector.isDeadStone(1, 1));
	
	// check that stones can be selected and deselected
	selector.selectStone(3, 3);
	assertTrue(selector.isDeadStone(3, 3));
	selector.selectStone(3, 3);
	assertFalse(selector.isDeadStone(3, 3));
	
	// get HashSet of dead stones
	HashSet<Integer[]> deadStones = selector.deadStoneHashSet();
	
	// check that HashSet contains only the stone selected
	assertEquals(deadStones.size(), 1);
	for (Integer[] deadStone : deadStones) {
	    assertEquals(deadStone[0], 0);
	    assertEquals(deadStone[1], 2);
	}
    }

}
