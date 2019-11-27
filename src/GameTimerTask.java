import java.util.*;

/**
 * The GameTimerTask class determines what happens when a GameTimer ticks down.
 * 
 * @author Chris Hartung
 *
 */
public class GameTimerTask extends TimerTask {
    private GameTimer timer;
    private int count = 0;

    /**
     * This method creates a GameTimerTask for the given GameTimer.
     * 
     * @param timer the GameTimer that is running this GameTimerTask
     */
    public GameTimerTask(GameTimer timer) {
	this.timer = timer;
    }

    /**
     * This method runs the timer.
     */
    @Override
    public void run() {
	if (!timer.getGame().isGameOver()) {
	    if (timer.getGame().blackToMove()) {
		timer.getBlackTimer().countdown();
	    } else {
		timer.getWhiteTimer().countdown();
	    }
	    count++;
	    // refresh the board 10 times per second
	    if (count >= (100 / GameTimer.TIMER_REFRESH_RATE)) {
		count = 0;
		try {
		    timer.getGame().getGui().drawBoard();
		} catch (NullPointerException e) {
		    // NullPointerException may occur if the game takes too long
		    // to load. When a NullPointerException is caught, the timer
		    // does nothing until the next tick - gameplay should not be
		    // significantly affected unless the computer is extremely
		    // slow, in which case timed games are not recommended.
		}
	    }
	}
    }

}
