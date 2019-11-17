import java.util.*;

public class GameTimerTask extends TimerTask {
    private GameTimer timer;

    public GameTimerTask(GameTimer timer) {
	this.timer = timer;
    }

    @Override
    public void run() {
	if (timer.getGame().blackToMove()) {
	    timer.getBlackTimer().countdown();
	} else {
	    timer.getWhiteTimer().countdown();
	}
    }

}
