import java.util.*;

/**
 * The GameViewer interface allows classes that implement it to be displayed by
 * a UserInterface and to receive user input from the same UserInterface.
 * 
 * @author Chris Hartung
 *
 */
public interface GameViewer {

    /**
     * @return blackToMove
     */
    boolean blackToMove();
    
//    /**
//     * This method returns to game play if players pass consecutively to end the
//     * game but can't agree on which stones are dead
//     */
//    void continuePlay();
    
//    /**
//     * This method reduces the handicap counter by one.
//     */
//    void decrementHandicapCounter();

    /**
     * This method finalizes the score and triggers the GUI's gameOver method.
     */
    void finalizeScore();

    /**
     * This method ends the game.
     */
    void gameOver();

    /**
     * @return the board
     */
    Board getBoard();

    /**
     * @return the finalMoveColor
     */
    String getFinalMoveColor(); // this shouldn't be needed once the scoring
				// system is fixed TODO

    /**
     * @return the finalScore
     */
    HashMap<String, Double> getFinalScore();

    /**
     * @return the gui
     */
    UserInterface getGui();

//    /**
//     * @return the handicap
//     */
//    int getHandicap();
    
//    /**
//     * @return the handicapCounter
//     */
//    int getHandicapCounter();
    
    /**
     * @return the komi
     */
    double getKomi();

    /**
     * @return the numRows();
     */
    int getNumRows();

//    /**
//     * @return player1
//     */
//    Player getPlayer1();
    
//    /**
//     * @return player2
//     */
//    Player getPlayer2();
    
    /**
     * @return the resignedPlayer
     */
    String getResignedPlayer();

//    /**
//     * @return the selector
//     */
//    DeadStoneSelector getSelector();
    
    /**
     * @return gameOver
     */
    boolean isGameOver();

//    /**
//     * @return isPlayer1Black
//     */
//    boolean isPlayer1Black();
    
//    /**
//     * @return selectingDeadStones
//     */
//    boolean isSelectingDeadStones();
    
//    /**
//     * This method switches which player is to move next.
//     */
//    void nextPlayersTurn();
    
    /**
     * This method processes a mouse click on the given button.
     * 
     * @param buttonID An integer identifying which button was clicked
     */
    void processMouseClick(int buttonID);

    /**
     * This method processes a mouse click on the column and row given as
     * inputs.
     * 
     * @param x The column which was clicked
     * @param y The row which was clicked
     */
    void processMouseClick(int x, int y);

//    /**
//     * This method indicates that the players are done selecting dead stones.
//     */
//    void selectionPhaseOver();
    
    /**
     * @param finalMoveColor the finalMoveColor to set
     */
    void setFinalMoveColor(String finalMoveColor); // this shouldn't be needed
						   // once the scoring system is
						   // fixed TODO

    /**
     * @param lastMoveWasPass the lastMoveWasPass to set
     */
    void setLastMoveWasPass(boolean lastMoveWasPass);

    /**
     * @param resignedPlayer the resignedPlayer to set
     */
    void setResignedPlayer(String resignedPlayer);

    /**
     * @return lastMoveWasPass
     */
    boolean wasLastMovePass();
}
