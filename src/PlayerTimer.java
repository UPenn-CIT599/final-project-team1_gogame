/**
 * The PlayerTimer class represents a timer for one of the Players in a Game of
 * Go.
 * 
 * @author Chris Hartung
 *
 */
public class PlayerTimer {
    private int mainTime;
    private boolean mainTimeOver;
    private int byoYomiPeriods;
    private int byoYomiLength;
    private int countdownTimer;
    private boolean isBlack;
    private GameTimer gameTimer;

    /**
     * @return the mainTime
     */
    public int getMainTime() {
	return mainTime;
    }

    /**
     * @param mainTime the mainTime to set
     */
    public void setMainTime(int mainTime) {
	this.mainTime = mainTime;
    }

    /**
     * @return the mainTimeOver
     */
    public boolean isMainTimeOver() {
	return mainTimeOver;
    }

    /**
     * @param mainTimeOver the mainTimeOver to set
     */
    public void setMainTimeOver(boolean mainTimeOver) {
	this.mainTimeOver = mainTimeOver;
    }

    /**
     * @return the byoYomiPeriods
     */
    public int getByoYomiPeriods() {
	return byoYomiPeriods;
    }

    /**
     * @param byoYomiPeriods the byoYomiPeriods to set
     */
    public void setByoYomiPeriods(int byoYomiPeriods) {
	this.byoYomiPeriods = byoYomiPeriods;
    }

    /**
     * @return the byoYomiLength
     */
    public int getByoYomiLength() {
	return byoYomiLength;
    }

    /**
     * @param byoYomiLength the byoYomiLength to set
     */
    public void setByoYomiLength(int byoYomiLength) {
	this.byoYomiLength = byoYomiLength;
    }

    /**
     * @return the countdownTimer
     */
    public int getCountdownTimer() {
	return countdownTimer;
    }

    /**
     * @param countdownTimer the countdownTimer to set
     */
    public void setCountdownTimer(int countdownTimer) {
	this.countdownTimer = countdownTimer;
    }
    
    /**
     * This method resets the byo-yomi timer if the main timer is out.
     */
    public void resetByoYomi() {
	if (mainTimeOver) {
	    countdownTimer = byoYomiLength;
	}
    }

    /**
     * This method creates a PlayerTimer based on the given inputs.
     * 
     * @param mainTime       The amount of time on the main timer in minutes
     * @param byoYomiPeriods The number of byo-yomi periods
     * @param byoYomiLength  The length of the byo-yomi periods in seconds
     * @param isBlack        True if the timer is timing the black player and
     *                       false if it is timing the white player
     * @param gameTimer      The GameTimer which runs this PlayerTimer
     */
    public PlayerTimer(int mainTime, int byoYomiPeriods, int byoYomiLength,
	    boolean isBlack, GameTimer gameTimer) {
	this.mainTime = mainTime * 60000;
	this.byoYomiPeriods = byoYomiPeriods;
	this.byoYomiLength = byoYomiLength * 1000;
	if (mainTime > 0) {
	    mainTimeOver = false;
	    countdownTimer = this.mainTime;
	} else {
	    mainTimeOver = true;
	    countdownTimer = this.byoYomiLength;
	}
	this.isBlack = isBlack;
	this.gameTimer = gameTimer;
    }

    /**
     * This method counts down the timer.
     */
    public void countdown() {
	countdownTimer -= GameTimer.TIMER_REFRESH_RATE;
	if (countdownTimer <= 0) {
	    if (!mainTimeOver) {
		mainTimeOver = true;
		countdownTimer = byoYomiLength;
	    } else if (byoYomiPeriods > 0) {
		byoYomiPeriods--;
		countdownTimer = byoYomiLength;
	    } else {
		gameTimer.timeOut(isBlack);
	    }
	}
    }
}
