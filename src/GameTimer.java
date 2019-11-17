import java.util.*;

public class GameTimer {
//    private int blackMainTime;
//    private int whiteMainTime;
//    private boolean blackMainTimeOver;
//    private boolean whiteMainTimeOver;
//    private int blackByoYomiPeriods;
//    private int whiteByoYomiPeriods;
//    private int byoYomiLength;
//    private int blackCountdownTimer;
//    private int whiteCountdownTimer;
    private PlayerTimer blackTimer;
    private PlayerTimer whiteTimer;
    private Game game;
    private Timer timer;

    /**
     * This is the number of milliseconds that pass between timer refreshes.
     */
    public static final int TIMER_REFRESH_RATE = 20;

//    /**
//     * @return the blackMainTime
//     */
//    public int getBlackMainTime() {
//	return blackMainTime;
//    }
//
//    /**
//     * @param blackMainTime the blackMainTime to set
//     */
//    public void setBlackMainTime(int blackMainTime) {
//	this.blackMainTime = blackMainTime;
//    }
//
//    /**
//     * @return the whiteMainTime
//     */
//    public int getWhiteMainTime() {
//	return whiteMainTime;
//    }
//
//    /**
//     * @param whiteMainTime the whiteMainTime to set
//     */
//    public void setWhiteMainTime(int whiteMainTime) {
//	this.whiteMainTime = whiteMainTime;
//    }
//
//    /**
//     * @return the blackMainTimeOver
//     */
//    public boolean isBlackMainTimeOver() {
//	return blackMainTimeOver;
//    }
//
//    /**
//     * @param blackMainTimeOver the blackMainTimeOver to set
//     */
//    public void setBlackMainTimeOver(boolean blackMainTimeOver) {
//	this.blackMainTimeOver = blackMainTimeOver;
//    }
//
//    /**
//     * @return the whiteMainTimeOver
//     */
//    public boolean isWhiteMainTimeOver() {
//	return whiteMainTimeOver;
//    }
//
//    /**
//     * @param whiteMainTimeOver the whiteMainTimeOver to set
//     */
//    public void setWhiteMainTimeOver(boolean whiteMainTimeOver) {
//	this.whiteMainTimeOver = whiteMainTimeOver;
//    }
//
//    /**
//     * @return the blackByoYomiPeriods
//     */
//    public int getBlackByoYomiPeriods() {
//	return blackByoYomiPeriods;
//    }
//
//    /**
//     * @param blackByoYomiPeriods the blackByoYomiPeriods to set
//     */
//    public void setBlackByoYomiPeriods(int blackByoYomiPeriods) {
//	this.blackByoYomiPeriods = blackByoYomiPeriods;
//    }
//
//    /**
//     * @return the whiteByoYomiPeriods
//     */
//    public int getWhiteByoYomiPeriods() {
//	return whiteByoYomiPeriods;
//    }
//
//    /**
//     * @param whiteByoYomiPeriods the whiteByoYomiPeriods to set
//     */
//    public void setWhiteByoYomiPeriods(int whiteByoYomiPeriods) {
//	this.whiteByoYomiPeriods = whiteByoYomiPeriods;
//    }
//
//    /**
//     * @return the byoYomiLength
//     */
//    public int getByoYomiLength() {
//	return byoYomiLength;
//    }
//
//    /**
//     * @param byoYomiLength the byoYomiLength to set
//     */
//    public void setByoYomiLength(int byoYomiLength) {
//	this.byoYomiLength = byoYomiLength;
//    }
//
//    /**
//     * @return the blackCountdownTimer
//     */
//    public int getBlackCountdownTimer() {
//	return blackCountdownTimer;
//    }
//
//    /**
//     * @param blackCountdownTimer the blackCountdownTimer to set
//     */
//    public void setBlackCountdownTimer(int blackCountdownTimer) {
//	this.blackCountdownTimer = blackCountdownTimer;
//    }
//
//    /**
//     * @return the whiteCountdownTimer
//     */
//    public int getWhiteCountdownTimer() {
//	return whiteCountdownTimer;
//    }
//
//    /**
//     * @param whiteCountdownTimer the whiteCountdownTimer to set
//     */
//    public void setWhiteCountdownTimer(int whiteCountdownTimer) {
//	this.whiteCountdownTimer = whiteCountdownTimer;
//    }

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

    public GameTimer(Game game, MainMenu menu) {
	this.game = game;
//	blackMainTime = menu.getMainTime() * 60000;
//	whiteMainTime = blackMainTime;
//	blackByoYomiPeriods = menu.getNumByoYomiPeriods();
//	whiteByoYomiPeriods = blackByoYomiPeriods;
//	byoYomiLength = menu.getByoYomiLength() * 1000;
//	if (blackMainTime > 0) {
//	    blackCountdownTimer = blackMainTime;
//	    whiteCountdownTimer = whiteMainTime;
//	    blackMainTimeOver = false;
//	    whiteMainTimeOver = false;
//	} else {
//	    blackCountdownTimer = byoYomiLength;
//	    whiteCountdownTimer = byoYomiLength;
//	    blackMainTimeOver = true;
//	    whiteMainTimeOver = true;
//	}
	blackTimer = new PlayerTimer(menu.getMainTime(),
		menu.getNumByoYomiPeriods(), menu.getByoYomiLength(), true);
	whiteTimer = new PlayerTimer(menu.getMainTime(),
		menu.getNumByoYomiPeriods(), menu.getByoYomiLength(), false);
	timer = new Timer();
	timer.scheduleAtFixedRate(new GameTimerTask(this), 0,
		TIMER_REFRESH_RATE);
    }
}
