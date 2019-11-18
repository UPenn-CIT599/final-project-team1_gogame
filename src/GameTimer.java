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
	game.gameOver();
    }
}
