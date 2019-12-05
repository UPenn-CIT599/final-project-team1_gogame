import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.time.*;
import java.time.format.*;
import org.junit.jupiter.api.*;

/**
 * 
 * @author morrowch
 *
 */
public class ReplayGameTest {
	
	@Test
    void ReplayGameTest() {
		String replayGameString = "(;FF[4]GM[1]SZ[19]KM[6.5]RE[W+R]PB[Player 1]PW[Computer]RU[Chinese]DT[2019-11-25]"
				+ ";B[ji];W[cb];B[nj];W[ks];B[hm];W[gd])";
		
		ReplayGame rg = new ReplayGame(replayGameString);
		rg.ParseMoves();
		assertEquals(rg.getBoard().getSize(), 19);
		assertEquals(rg.getMoves().size(), 6);
		assertEquals(rg.getBlackPlayer(), "Player 1");
		assertEquals(rg.getWhitePlayer(), "Computer");
		
		assertEquals(rg.getMoves().get(0).getMoveNumber(), 0);
		assertEquals(rg.getMoves().get(0).getX(), 9);
		assertEquals(rg.getMoves().get(0).getY(), 8);
		assertEquals(rg.getMoves().get(0).getColor(), Color.BLACK);
		
		assertEquals(rg.getMoves().get(1).getMoveNumber(), 1);
		assertEquals(rg.getMoves().get(1).getX(), 2);
		assertEquals(rg.getMoves().get(1).getY(), 1);
		assertEquals(rg.getMoves().get(1).getColor(), Color.WHITE);
		
		assertEquals(rg.getMoves().get(2).getMoveNumber(), 2);
		assertEquals(rg.getMoves().get(2).getX(), 13);
		assertEquals(rg.getMoves().get(2).getY(), 9);
		assertEquals(rg.getMoves().get(2).getColor(), Color.BLACK);
		
		assertEquals(rg.getMoves().get(3).getMoveNumber(), 3);
		assertEquals(rg.getMoves().get(3).getX(), 10);
		assertEquals(rg.getMoves().get(3).getY(), 18);
		assertEquals(rg.getMoves().get(3).getColor(), Color.WHITE);
		
		assertEquals(rg.getMoves().get(4).getMoveNumber(), 4);
		assertEquals(rg.getMoves().get(4).getX(), 7);
		assertEquals(rg.getMoves().get(4).getY(), 12);
		assertEquals(rg.getMoves().get(4).getColor(), Color.BLACK);
		
		assertEquals(rg.getMoves().get(5).getMoveNumber(), 5);
		assertEquals(rg.getMoves().get(5).getX(), 6);
		assertEquals(rg.getMoves().get(5).getY(), 3);
		assertEquals(rg.getMoves().get(5).getColor(), Color.WHITE);
	}

}
