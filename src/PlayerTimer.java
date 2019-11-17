
public class PlayerTimer {
    private int mainTime;
    private boolean mainTimeOver;
    private int byoYomiPeriods;
    private int byoYomiLength;
    private int countdownTimer;
    private boolean isBlack;
    
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
    
    public PlayerTimer(int mainTime, int byoYomiPeriods,
	    int byoYomiLength, boolean isBlack) {
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
    }
    
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
		// TODO this player loses the game
	    }
	}
    }
}
