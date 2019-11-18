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
	    if (count >= (1000 / GameTimer.TIMER_REFRESH_RATE)) {
		count = 0;
		timer.getGame().getGui().drawBoard();
		System.out.println("Black: Countdown: " +
			timer.getBlackTimer().getCountdownTimer() +
			" Byo-Yomi remaining: " +
			timer.getBlackTimer().getByoYomiPeriods());
		System.out.println("White: Countdown: " +
			timer.getWhiteTimer().getCountdownTimer() +
			" Byo-Yomi remaining: " +
			timer.getWhiteTimer().getByoYomiPeriods());
	    }
	}
    }

}
