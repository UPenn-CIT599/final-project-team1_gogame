import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
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
public class UserInterface extends Canvas implements MouseListener {
    private JFrame frame;
    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    private Game game;
    private boolean isPlayer1Black;
    private boolean demoMode;
    private String player1Name;
    private String player2Name;
//    private boolean onePlayerGame;
//    private Object notifier = new Object();
    private static final int imageSize = 700;
    private static final int borderSize = 100;
    private int numRows;
    private int lineSpacing;
    private int pieceRadius;
    private int boardSize;
    private static double pieceRadiusAsPercentOfLineSpacing = 0.4;
    private Rectangle previousButton = new Rectangle(100, 610, 120, 30);
    private Rectangle nextButton = new Rectangle(290, 610, 120, 30);
    private Rectangle demoMainMenuButton = new Rectangle(480, 610, 120, 30);
    private Rectangle passButton = new Rectangle(165, 610, 120, 30);
    private Rectangle gameMainMenuButton = new Rectangle(415, 610, 120, 30);
    private static Color backgroundColor = Color.lightGray;
    private static Color lineColor = Color.darkGray;
    private static Color textColor = Color.black;
    private static Color buttonColor = Color.white;
    private String messageLine1;
    private String messageLine2;
    private MainMenu mainMenu;
    private EndGameMenu endGameMenu;

//    public Object getNotifier() {
//	return notifier;
//    }
    
    public Game getGame() {
	return game;
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
	frame = new JFrame("Go");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.add(this);
	frame.setLayout(null);
	frame.setSize(imageSize, imageSize);
	image = new BufferedImage(imageSize, imageSize,
		BufferedImage.TYPE_INT_ARGB);
	this.addMouseListener(this);
    }

    /**
     * This method initializes the game after the game options have been
     * selected in the MainMenu.
     */
    public void initializeGame() {
	demoMode = mainMenu.isDemoMode();
//	onePlayerGame = menu.isOnePlayerGame();
	numRows = mainMenu.getNumRows();
	game = new Game(this, mainMenu);
	isPlayer1Black = game.isPlayer1Black();
	player1Name = game.getPlayer1().getName();
	player2Name = game.getPlayer2().getName();
	frame.setVisible(true);
	int maxBoardSize = imageSize - (2 * borderSize);
	lineSpacing = maxBoardSize / numRows;
	pieceRadius = (int) (lineSpacing * pieceRadiusAsPercentOfLineSpacing);
	boardSize = lineSpacing * (numRows - 1);
	if (game.getHandicapCounter() > 0) {
	    handicapMessage();
	} else {
	    messageLine1 = "";
	}
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
	if (reason.equals("handicap pass")) {
	    messageLine1 = "Place all handicap stones before passing.";
	}
	else {
	    messageLine1 = "Invalid move, please try again.";
	}
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
		game.getHandicapCounter();
    }

    /**
     * This method draws the board, including the pieces, buttons, and messages.
     */
    public void drawBoard() {
	Graphics g = image.getGraphics();
	g.setColor(backgroundColor);
	g.fillRect(0, 0, imageSize, imageSize);
	g.setColor(lineColor);
	int start = borderSize + pieceRadius;
	int stop = borderSize + pieceRadius + boardSize;
	for (int i = 0; i < numRows; i++) {
	    g.drawLine(start, start + i * lineSpacing, stop,
		    start + i * lineSpacing);
	    g.drawLine(start + i * lineSpacing, start, start + i * lineSpacing,
		    stop);
	}
	drawPieces(g);
	drawButtons(g);
	if (game.isGameOver()) {
	    messageLine1 = "";
	    messageLine2 = "Game Over";
	} else {
	    messageLine2 = currentPlayersName() + ", it is your turn.";
	}
	g.setColor(textColor);
	g.setFont(new Font(Font.DIALOG, Font.PLAIN, 28));
	g.drawString(messageLine1, borderSize, 50);
	g.drawString(messageLine2, borderSize, 90);
	repaint();
    }

    private void drawPieces(Graphics g) {
	for (int i = 0; i < numRows; i++) {
	    for (int j = 0; j < numRows; j++) {
		if (game.getBoard().getStoneColor(i, j) != null) {
		    g.setColor(game.getBoard().getStoneColor(i, j));
		    g.fillOval(borderSize + i * lineSpacing,
			    borderSize + j * lineSpacing, 2 * pieceRadius,
			    2 * pieceRadius);
		    g.setColor(Color.black);
		    g.drawOval(borderSize + i * lineSpacing,
			    borderSize + j * lineSpacing, 2 * pieceRadius,
			    2 * pieceRadius);

		}
	    }
	}
    }

    /**
     * This method takes as input a button and the location of a mouse click
     * and returns whether that button was clicked.
     * @param button The button to be checked
     * @param mouseX The x location of the mouse click
     * @param mouseY The y location of the mouse click
     * @return True if the button was clicked and false otherwise
     */
    private boolean buttonClicked(Rectangle button, int mouseX, int mouseY) {
	return button.contains(mouseX, mouseY);
    }

    private void drawButton(Graphics g, Rectangle button, String text,
	    int offset) {
	g.setColor(buttonColor);
	g.fillRect(button.x, button.y, button.width, button.height);
	g.setColor(lineColor);
	g.drawRect(button.x, button.y, button.width, button.height);
	g.setColor(textColor);
	g.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
	g.drawString(text, button.x + offset, button.y + 22);
    }

    private void drawButtons(Graphics g) {
	if (demoMode) {
	    drawButton(g, previousButton, "Previous", 28);
	    drawButton(g, nextButton, "Next", 43);
	    drawButton(g, demoMainMenuButton, "Main Menu", 20);
	} else {
	    drawButton(g, passButton, "Pass", 43);
	    drawButton(g, gameMainMenuButton, "Main Menu", 20);
	}
    }

    /**
     * This method is run when the game ends. TODO
     */
    public void gameOver() {
	endGameMenu = new EndGameMenu(this);
    }

    /**
     * This method runs the UserInterface.
     */
    public void run() {
	frame.setVisible(false);
	mainMenu = new MainMenu(this);
    }

    /**
     * This method processes a mouse click.
     */
    @Override
    public void mouseClicked(MouseEvent arg0) {
	int mouseX = arg0.getX();
	int mouseY = arg0.getY();
	if (demoMode) {
	    if (buttonClicked(previousButton, mouseX, mouseY)) {
		// TODO
	    } else if (buttonClicked(nextButton, mouseX, mouseY)) {
		// TODO
	    } else if (buttonClicked(demoMainMenuButton, mouseX, mouseY)) {
		run();
	    }
	} else {
	    if (buttonClicked(passButton, mouseX, mouseY)) {
		game.processMouseClick(Player.PASS);
	    } else if (buttonClicked(gameMainMenuButton, mouseX, mouseY)) {
		if (!game.isGameOver()) {
		    run(); // TODO create warning popup
		}
	    } else {
		for (int i = 0; i < numRows; i++) {
		    for (int j = 0; j < numRows; j++) {
			if (buttonClicked(
				new Rectangle(borderSize + i * lineSpacing,
					borderSize + j * lineSpacing,
					2 * pieceRadius, 2 * pieceRadius),
				mouseX, mouseY)) {
			    game.processMouseClick(i, j);
			}
		    }
		}
	    }
	}
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
	// do nothing

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
	// do nothing

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
	// do nothing

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
	// do nothing

    }

}
