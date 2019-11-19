import java.text.DecimalFormat;
import java.util.*;

/**
 * The GameTimer class runs the countdown timers for both Players in a Game of
 * Go.
 * 
 * @author Chris Hartung
 *
 */
public class GameTimer {
    private PlayerTimer blackTimer;
    private PlayerTimer whiteTimer;
    private Game game;
    private Timer timer;
    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("00");

    /**
     * This is the number of milliseconds that pass between timer refreshes.
     */
    public static final int TIMER_REFRESH_RATE = 20;

    /**
     * @return the game
     */
    public Game getGame() {
	return game;
    }

    /**
     * @return the blackTimer
     */
    public PlayerTimer getBlackTimer() {
	return blackTimer;
    }

    /**
     * @return the whiteTimer
     */
    public PlayerTimer getWhiteTimer() {
	return whiteTimer;
    }

    /**
     * This method creates a GameTimer for the given game based on the
     * specifications found in the given MainMenu.
     * 
     * @param game The Game that is being timed
     * @param menu The MainMenu for the Game
     */
    public GameTimer(Game game, MainMenu menu) {
	this.game = game;
	blackTimer = new PlayerTimer(menu.getMainTime(),
		menu.getNumByoYomiPeriods(), menu.getByoYomiLength(), true,
		this);
	whiteTimer = new PlayerTimer(menu.getMainTime(),
		menu.getNumByoYomiPeriods(), menu.getByoYomiLength(), false,
		this);
	timer = new Timer();
	timer.scheduleAtFixedRate(new GameTimerTask(this), 500,
		TIMER_REFRESH_RATE);
    }

    /**
     * This method resets the byo-yomi timer for both players.
     */
    public void resetByoYomi() {
	blackTimer.resetByoYomi();
	whiteTimer.resetByoYomi();
    }

    /**
     * This method indicates that the given player has timed out.
     * 
     * @param isBlack True if the black player timed out and false if the white
     *                player timed out
     */
    public void timeOut(boolean isBlack) {
	String timedOutPlayer = "";
	if (game.isPlayer1Black() == isBlack) {
	    timedOutPlayer = game.getPlayer1().getName();
	} else {
	    timedOutPlayer = game.getPlayer2().getName();
	}
	game.setTimedOutPlayer(timedOutPlayer);
	if (isBlack) {
	    game.setFinalMoveColor("white");
	} else {
	    game.setFinalMoveColor("black");
	}
	game.gameOver();
    }

    /**
     * This method returns the time on the countdown timer for the given player
     * in the format in which it will be displayed.
     * 
     * @param isBlack True if checking the black player's timer and false if
     *                checking the white player's timer
     * @return The remaining time on the given player's countdown timer,
     *         formatted as a String
     */
    public String formatTime(boolean isBlack) {
	int remainingTime = 0;
	if (isBlack) {
	    remainingTime = blackTimer.getCountdownTimer();
	} else {
	    remainingTime = whiteTimer.getCountdownTimer();
	}
	int hours = remainingTime / 3600000;
	remainingTime -= (3600000 * hours);
	int minutes = remainingTime / 60000;
	remainingTime -= (60000 * minutes);
	int seconds = (int) Math.ceil(remainingTime / 1000.0);
	if (seconds == 60) {
	    seconds = 0;
	    minutes++;
	}
	if (minutes == 60) {
	    minutes = 0;
	    hours++;
	}
	return hours + ":" + TIME_FORMAT.format(minutes) + ":" +
		TIME_FORMAT.format(seconds);
    }

    /**
     * This method returns the number of byo-yomi periods remaining for the
     * specified player.
     * 
     * @param isBlack True if checking the black player and false if checking
     *                the white player
     * @return The number of byo-yomi periods remaining for the given player
     */
    public int remainingByoYomiPeriods(boolean isBlack) {
	if (isBlack) {
	    return blackTimer.getByoYomiPeriods();
	} else {
	    return whiteTimer.getByoYomiPeriods();
	}
    }
}
