/**
 * The TestUserInterface class is used in place of a UserInterface in JUnit
 * tests in order to prevent the NullPointerExceptions that unit testing with a
 * UserInterface would cause.
 * 
 * @author Chris Hartung
 *
 */
public class TestUserInterface extends UserInterface {

    private static final long serialVersionUID = 1L;

    /**
     * This method returns a new Game.
     * 
     * @return a new Game
     */
    @Override
    public Game getGame() {
	return new Game(this, new MainMenu(this));
    }

    /**
     * This method returns a new EndGameMenu.
     * 
     * @return a new EndGameMenu
     */
    @Override
    public EndGameMenu getEndGameMenu() {
	return new EndGameMenu(this);
    }

    /**
     * This method does nothing.
     */
    @Override
    public void drawBoard() {
	// do nothing
    }

    /**
     * This method does nothing.
     */
    @Override
    public void gameOver() {
	// do nothing
    }

}
