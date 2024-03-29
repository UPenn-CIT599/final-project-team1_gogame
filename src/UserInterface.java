import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;

import javax.swing.*;

/*
 * Portions of the UserInterface class are based on the following:
 * 
 * Title: GraphicsProgram 
 * Author: Landofcode.com 
 * Date: unknown
 * Availability: http://www.landofcode.com/java-tutorials/java-graphics.php
 * 
 * Other portions are based on the following:
 * 
 * Title: PennDraw
 * Author: Robert Sedgewick, Kevin Wayne, and Benedict Brown
 * Date: 2016
 * Availability: https://www.cis.upenn.edu/~cis110/current/PennDraw.java
 */
/**
 * The UserInterface class is a graphical user interface for a game of Go.
 * 
 * @author Chris Hartung
 *
 */
public class UserInterface extends JPanel implements MouseListener {
    private JFrame frame;
    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    private AbstractGame game;
    private boolean isPlayer1Black;
    private boolean replayMode;
    private boolean practiceProblem;
    private String player1Name;
    private String player2Name;
    private static final int imageSize = 700;
    private static final int borderSize = 100;
    private int numRows;
    private int lineSpacing;
    private int pieceRadius;
    private int boardSize;
    private static double pieceRadiusAsPercentOfLineSpacing = 0.4;
    private Rectangle restartReplayButton = new Rectangle(100, 610, 120, 30);
    private Rectangle nextTurnButton = new Rectangle(290, 610, 120, 30);
    private Rectangle replayMainMenuButton = new Rectangle(480, 610, 120, 30);
    private Rectangle restartPracticeButton = new Rectangle(180, 610, 120, 30);
    private Rectangle practiceMainMenuButton = new Rectangle(400, 610, 120, 30);
    private Rectangle passButton = new Rectangle(100, 610, 120, 30);
    private Rectangle resignButton = new Rectangle(290, 610, 120, 30);
    private Rectangle gameMainMenuButton = new Rectangle(480, 610, 120, 30);
    private Rectangle calculateScoreButton = new Rectangle(100, 610, 200, 30);
    private Rectangle continuePlayButton = new Rectangle(400, 610, 200, 30);
    private Rectangle annotationArea = new Rectangle(100, 60, 500, 30);
    private static Color backgroundColor = Color.LIGHT_GRAY;
    private static Color lineColor = Color.DARK_GRAY;
    private static Color textColor = Color.BLACK;
    private static Color buttonColor = Color.WHITE;
    private static Color deadStoneColor = Color.RED;
    private static final Font MESSAGE = new Font(Font.DIALOG, Font.PLAIN, 28);
    private static final Font BUTTON = new Font(Font.DIALOG, Font.BOLD, 16);
    private static final Font TIMER = new Font(Font.DIALOG, Font.BOLD, 22);
    private String messageLine1 = "";
    private String messageLine2 = "";
    private MainMenu mainMenu;
    private EndGameMenu endGameMenu;
    private String annotation;
    
    private static String CONFIRM_MAIN_MENU = 
	    "Are you sure you want to return to the main menu?";
    private static String CONFIRM_RESTART = "Are you sure you want to restart?";
    private static String LOST_PROGRESS_WARNING = 
	    "Your game progress will be lost.";
    
    /**
     * @return the frame
     */
    public JFrame getFrame() {
	return frame;
    }

    /**
     * @return the game
     */
    public AbstractGame getGame() {
	return game;
    }
    
    /**
     * @return replayMode
     */
    public boolean isReplayMode() {
	return replayMode;
    }
    
    /**
     * @return practiceProblem
     */
    public boolean isPracticeProblem() {
	return practiceProblem;
    }
    
    /**
     * @return the endGameMenu
     */
    public EndGameMenu getEndGameMenu() {
	return endGameMenu;
    }

    /**
     * This method sets the name of a player based on the contents of an sgf
     * file.
     * 
     * @param name    The name to set
     * @param isBlack True if it is the black player's name and false if it is
     *                the white player's name
     */
    public void setName(String name, boolean isBlack) {
	if (isBlack == isPlayer1Black) {
	    player1Name = name;
	} else {
	    player2Name = name;
	}
    }
    
    /**
     * This method returns the name of the Player who is playing black.
     * 
     * @return The black Player's name
     */
    public String blackPlayerName() {
	if (isPlayer1Black) {
	    return player1Name;
	} else {
	    return player2Name;
	}
    }

    /**
     * This method returns the name of the Player who is playing white.
     * 
     * @return The white Player's name
     */
    public String whitePlayerName() {
	if (isPlayer1Black) {
	    return player2Name;
	} else {
	    return player1Name;
	}
    }
    
    /**
     * This method creates a UserInterface.
     */
    public UserInterface() {
	setBackground(backgroundColor);
	setSize(imageSize, imageSize);
	setDoubleBuffered(true);
	frame = new JFrame("Go");
	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	frame.addWindowListener(new WindowAdapter() {
	    /*
	     * The below code comes from the following:
	     * 
	     * Title: exitListener 
	     * Author: mKorbel 
	     * Date: 2011
	     * Availability: 
	     * https://stackoverflow.com/questions/6084039/create-custom-operation-for-setdefaultcloseoperation
	     */
	    @Override
	    public void windowClosing(WindowEvent e) {
	        int confirm = JOptionPane.showOptionDialog(
	             frame, "Are you sure you want to exit the game?", 
	             "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
	             JOptionPane.QUESTION_MESSAGE, null, null, null);
	        if (confirm == 0) {
	           System.exit(0);
	        }
	    }
	});	
	frame.add(this);
	frame.setLayout(null);
	frame.setSize(imageSize, imageSize);
	image = new BufferedImage(imageSize, imageSize,
		BufferedImage.TYPE_INT_ARGB);
	this.addMouseListener(this);
	setToolTipText("Go");
    }

    /**
     * This method initializes the game after the game options have been
     * selected in the MainMenu.
     */
    public void initializeGame() {
	replayMode = mainMenu.isReplayMode();
	practiceProblem = mainMenu.isPracticeProblem();
	numRows = mainMenu.getNumRows();
	messageLine1 = "";
	messageLine2 = "";
	if (replayMode) {
	    game = new ReplayMode(this, mainMenu);
	    numRows = game.getBoard().getSize();
	    isPlayer1Black = true;
	    player1Name = "Black";
	    player2Name = "White";
	} else if (practiceProblem) {
	    game = new PracticeProblemMode(this, mainMenu);
	    numRows = game.getBoard().getSize();
	} else {
	    game = new Game(this, mainMenu);
	    isPlayer1Black = ((Game) game).isPlayer1Black();
	    player1Name = ((Game) game).getPlayer1().getName();
	    player2Name = ((Game) game).getPlayer2().getName();
	    if (((Game) game).getHandicapCounter() > 0) {
		handicapMessage();
	    } else {
		messageLine1 = "";
	    }
	}
	int maxBoardSize = imageSize - (2 * borderSize);
	lineSpacing = maxBoardSize / numRows;
	pieceRadius = (int) (lineSpacing * pieceRadiusAsPercentOfLineSpacing);
	boardSize = lineSpacing * (numRows - 1);
	frame.setVisible(true);
	drawBoard();
    }

    /**
     * This method displays the board.
     */
    public void paint(Graphics g) {
	g.drawImage(image, 0, 0, null);
    }

    /**
     * This method returns the name of the Player whose turn it is.
     * @return The name of the player who is to move
     */
    public String currentPlayersName() {
	if (game.blackToMove() == isPlayer1Black) {
	    return player1Name;
	} else {
	    return player2Name;
	}
    }

    /**
     * This method sets a message indicating that the most recent
     * move was invalid.
     * @param reason The reason the move was invalid
     */
    public void invalidMove(String reason) {
	messageLine1 = reason;
    }

    /**
     * This method sets a message indicating what Player just moved
     * and whether that Player placed a stone or passed.
     * @param name The name of the Player who moved
     * @param pass True if the Player passed and false if they placed a stone
     */
    public void setMessage(String name, boolean pass) {
	String action = " placed a stone.";
	if (pass) {
	    action = " passed.";
	}
	messageLine1 = name + action;
    }
    
    /**
     * This method sets a message indicating how many handicap stones still need
     * to be placed.
     */
    public void handicapMessage() {
	messageLine1 = "Handicap stones remaining: " +
		((Game) game).getHandicapCounter();
    }

    /**
     * This method draws the board, including the pieces, buttons, and messages,
     * as well as the dead stones and timer if applicable.
     */
    public void drawBoard() {
	Graphics g = image.getGraphics();
	
	// clear the image
	g.setColor(backgroundColor);
	g.fillRect(0, 0, imageSize, imageSize);
	
	// draw the lines for the board
	g.setColor(lineColor);
	int start = borderSize + pieceRadius;
	int stop = borderSize + pieceRadius + boardSize;
	for (int i = 0; i < numRows; i++) {
	    g.drawLine(start, start + i * lineSpacing, stop,
		    start + i * lineSpacing);
	    g.drawLine(start + i * lineSpacing, start, start + i * lineSpacing,
		    stop);
	}
	
	// draw the pieces, dead stones (if applicable), and buttons
	drawPieces(g);
	if (!practiceProblem) {
	    if (game.getSelector() != null) {
		drawDeadStones(g);
	    }
	}
	drawButtons(g);
	
	// draw the message text
	if (game.isGameOver() && !practiceProblem && !replayMode) {
		messageLine1 = "";
	    messageLine2 = "Game Over";
	} else if (replayMode || practiceProblem) {
	    annotation = game.getAnnotation();
	    if (annotation == null) {
		messageLine2 = "";
	    } else if (annotation.length() > 35) {
		messageLine2 = annotation.substring(0, 35) + "...";
	    } else if (annotation.equals("")) {
		annotation = null;
		messageLine2 = "";
	    } else {
		messageLine2 = annotation;
	    }

	} else {
	    messageLine2 = currentPlayersName() + ", it is your turn.";
	}
	if (practiceProblem) {
		PracticeProblemMode practiceProblemMode = (PracticeProblemMode) game;
		messageLine1 = practiceProblemMode.getProblemTitle();
	}
	g.setColor(textColor);
	g.setFont(MESSAGE);
	g.drawString(messageLine1, borderSize, 50);
	g.drawString(messageLine2, borderSize, 90);
	
	if (game.getTimer() != null) {
	    drawTimer(g);
	}
	
	// run the paint method
	repaint();
    }

    /**
     * This method draws the game pieces that are on the board.
     * 
     * @param g The Graphics on which the pieces will be drawn
     */
    private void drawPieces(Graphics g) {
	for (int i = 0; i < numRows; i++) {
	    for (int j = 0; j < numRows; j++) {
		if (game.getBoard().getStoneColor(i, j) != null) {
		    g.setColor(game.getBoard().getStoneColor(i, j));
		    g.fillOval(borderSize + i * lineSpacing,
			    borderSize + j * lineSpacing, 2 * pieceRadius,
			    2 * pieceRadius);
		    g.setColor(Color.BLACK);
		    g.drawOval(borderSize + i * lineSpacing,
			    borderSize + j * lineSpacing, 2 * pieceRadius,
			    2 * pieceRadius);

		}
	    }
	}
    }

    /**
     * This method takes as input a button and the location of a mouse click and
     * returns whether that button was clicked.
     * 
     * @param button The button to be checked
     * @param mouseX The x location of the mouse click
     * @param mouseY The y location of the mouse click
     * @return True if the button was clicked and false otherwise
     */
    private boolean buttonClicked(Rectangle button, int mouseX, int mouseY) {
	return button.contains(mouseX, mouseY);
    }

    /**
     * This method draws a button with the given text at the given location.
     * 
     * @param g      The Graphics on which the button will be drawn
     * @param button The location of the button
     * @param text   The text of the button
     * @param offset The distance from the left edge of the button to the start
     *               of the text
     */
    private void drawButton(Graphics g, Rectangle button, String text,
	    int offset) {
	g.setColor(buttonColor);
	g.fillRect(button.x, button.y, button.width, button.height);
	g.setColor(lineColor);
	g.drawRect(button.x, button.y, button.width, button.height);
	g.setColor(textColor);
	g.setFont(BUTTON);
	g.drawString(text, button.x + offset, button.y + 22);
    }

    /**
     * This method draws all the buttons that are needed based on the current
     * game phase.
     * 
     * @param g The Graphics on which the buttons will be drawn
     */
    private void drawButtons(Graphics g) {
	if (replayMode) {
	    drawButton(g, restartReplayButton, "Restart", 34);
	    if (((ReplayMode)game).isOffPath()) {
	    	drawButton(g, nextTurnButton, "Back to Game", 9);
	    } else {
	    	drawButton(g, nextTurnButton, "Next Turn", 24);
	    }
	    drawButton(g, replayMainMenuButton, "Main Menu", 20);
	} else if (practiceProblem) {
	    drawButton(g, restartPracticeButton, "Restart", 34);
	    drawButton(g, practiceMainMenuButton, "Main Menu", 20);
	} else if (((Game) game).isSelectingDeadStones()) {
	    drawButton(g, calculateScoreButton, "Calculate Score", 41);
	    drawButton(g, continuePlayButton, "Continue Play", 47);
	} else {
	    drawButton(g, passButton, "Pass", 43);
	    drawButton(g, resignButton, "Resign", 35);
	    drawButton(g, gameMainMenuButton, "Main Menu", 20);
	}
    }
    
    /**
     * This method draws dots to indicate which stones are dead.
     * 
     * @param g The Graphics on which the dots will be drawn
     */
    private void drawDeadStones(Graphics g) {
	g.setColor(deadStoneColor);
	for (int i = 0; i < numRows; i++) {
	    for (int j = 0; j < numRows; j++) {
		if ((game).getSelector().isDeadStone(i, j)) {
		    g.fillOval(borderSize + i * lineSpacing + pieceRadius / 2,
			    borderSize + j * lineSpacing + pieceRadius / 2,
			    pieceRadius, pieceRadius);
		}
	    }
	}
    }
    
    /**
     * This method draws the text representing the timer.
     * 
     * @param g The Graphics on which the timer will be drawn.
     */
    private void drawTimer(Graphics g) {
	g.setFont(BUTTON);
	g.setColor(textColor);
	g.drawString("Black", 10, 150);
	g.drawString("Timer", 10, 180);
	g.setFont(TIMER);
	String blackCountdown = game.getTimer().formatTime(true);
	if (blackCountdown.startsWith("0:00:0")) {
	    g.setColor(Color.RED);
	}
	g.drawString(blackCountdown, 10, 205);
	g.setColor(textColor);
	g.setFont(BUTTON);
	g.drawString("Byo-Yomi", 10, 235);
	g.drawString("Remaining:", 10, 255);
	String blackByoYomi = Integer
		.toString(game.getTimer().remainingByoYomiPeriods(true));
	if (blackByoYomi.equals("0")) {
	    g.setColor(Color.RED);
	}
	g.setFont(TIMER);
	g.drawString(blackByoYomi, 10, 280);

	g.setFont(BUTTON);
	g.setColor(textColor);
	g.drawString("White", 10, 405);
	g.drawString("Timer", 10, 435);
	g.setFont(TIMER);
	String whiteCountdown = game.getTimer().formatTime(false);
	if (whiteCountdown.startsWith("0:00:0")) {
	    g.setColor(Color.RED);
	}
	g.drawString(game.getTimer().formatTime(false), 10, 460);
	g.setColor(textColor);
	g.setFont(BUTTON);
	g.drawString("Byo-Yomi", 10, 490);
	g.drawString("Remaining:", 10, 510);
	String whiteByoYomi = Integer
		.toString(game.getTimer().remainingByoYomiPeriods(false));
	if (whiteByoYomi.equals("0")) {
	    g.setColor(Color.RED);
	}
	g.setFont(TIMER);
	g.drawString(whiteByoYomi, 10, 535);
    }

    /**
     * This method is run when the game ends.
     */
    public void gameOver() {
	drawBoard();
	endGameMenu = new EndGameMenu(this);
    }
    
    /**
     * This method plays a replay of the game that was just completed.
     * 
     * @param replayFile The file containing the replay
     */
    public void viewReplay(File replayFile) {
	mainMenu.viewReplay(replayFile);
	initializeGame();
    }
    
    /**
     * This method runs the UserInterface.
     */
    public void run() {
	frame.setVisible(false);
	mainMenu = new MainMenu(this);
    }
    
    /**
     * This method confirms that the user wants to return to the main menu.
     * 
     * @param lostProgressWarning True if the user is being warned about
     *                            possible lost progress
     * @param mainMenu            True if a Main Menu button was pressed and
     *                            false if a Restart button was pressed
     */
    private void confirmChoice(boolean lostProgressWarning, boolean mainMenu) {
	String message = CONFIRM_MAIN_MENU;
	String title = "Main Menu Confirmation";
	int messageType = JOptionPane.QUESTION_MESSAGE;
	if (!mainMenu) {
	    message = CONFIRM_RESTART;
	    title = "Restart Confirmation";
	}
	if (lostProgressWarning) {
	    message = message + "\n" + LOST_PROGRESS_WARNING;
	    messageType = JOptionPane.WARNING_MESSAGE;
	}
	int confirm = JOptionPane.showOptionDialog(frame, message, title,
		JOptionPane.YES_NO_OPTION, messageType, null, null, null);
	if (confirm == 0) {
	    if (mainMenu) {
		run();
	    } else {
		initializeGame();
	    }
	}
    }

    /**
     * This method processes a mouse click.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
	int mouseX = e.getX();
	int mouseY = e.getY();
	if (replayMode) {
	    if (!game.isGameOver()) {
		if (buttonClicked(restartReplayButton, mouseX, mouseY)) {
		    confirmChoice(false, false);
		} else if (buttonClicked(nextTurnButton, mouseX, mouseY)) {
		    ((ReplayMode) game).NextMove();
		} else if (buttonClicked(replayMainMenuButton, mouseX,
			mouseY)) {
		    confirmChoice(false, true);
		} else {
		    processMouseClick(mouseX, mouseY);
		    ;
		}
	    }
	} else if (practiceProblem) {
	    if (buttonClicked(restartPracticeButton, mouseX, mouseY)) {
		confirmChoice(true, false);
	    } else if (buttonClicked(practiceMainMenuButton, mouseX, mouseY)) {
		confirmChoice(true, true);
	    } else {
		processMouseClick(mouseX, mouseY);;
	    }
	} else if (((Game) game).isSelectingDeadStones()) {
	    if (buttonClicked(calculateScoreButton, mouseX, mouseY)) {
		game.finalizeScore();
	    } else if (buttonClicked(continuePlayButton, mouseX, mouseY)) {
		((Game) game).continuePlay();
	    } else {
		processMouseClick(mouseX, mouseY);
	    }
	} else {
	    if (buttonClicked(passButton, mouseX, mouseY)) {
		game.processMouseClick(Player.PASS);
	    } else if (buttonClicked(resignButton, mouseX, mouseY)) {
		game.processMouseClick(Player.RESIGN);
	    } else if (buttonClicked(gameMainMenuButton, mouseX, mouseY)) {
		if (!game.isGameOver()) {
		    confirmChoice(true, true);
		}
	    } else {
		processMouseClick(mouseX, mouseY);
	    }
	}
    }
    
    /**
     * This method processes a mouse click at the given location.
     * @param mouseX The x location of the click
     * @param mouseY The y location of the click
     */
    public void processMouseClick(int mouseX, int mouseY) {
	for (int i = 0; i < numRows; i++) {
	    for (int j = 0; j < numRows; j++) {
		if (buttonClicked(
			new Rectangle(borderSize + i * lineSpacing,
				borderSize + j * lineSpacing,
				2 * pieceRadius, 2 * pieceRadius),
			mouseX, mouseY)) {
		    game.processMouseClick(i, j);
		    return;
		}
	    }
	}
    }

    /**
     * This method does nothing.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
	// do nothing
    }

    /**
     * This method does nothing.
     */
    @Override
    public void mouseExited(MouseEvent e) {
	// do nothing
    }

    /**
     * This method does nothing.
     */
    @Override
    public void mousePressed(MouseEvent e) {
	// do nothing
    }

    /**
     * This method does nothing.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
	// do nothing
    }
    
    /**
     * This method displays a tooltip containing the annotation whenever there
     * is an annotation and the mouse is hovering over the annotation area.
     */
    @Override public String getToolTipText(MouseEvent e) {
	if (annotationArea.contains(e.getX(), e.getY())) {
	    return annotation;
	} else {
	    return null;
	}
    }

}
