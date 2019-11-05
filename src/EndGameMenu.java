import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * The EndGameMenu class represents the end of game menu for a game of Go.
 * 
 * @author Chris Hartung
 *
 */
public class EndGameMenu {
    private JFrame frame;
    private Container pane;
    private JPanel buttonPanel;
    private String blackPlayerName;
    private String whitePlayerName;
    private double blackScore;
    private double whiteScore;
    private static DecimalFormat scoreFormat = new DecimalFormat("#.#");

    /*
     * A portion of the below method is based on the following:
     * 
     * Title: BoxLayoutDemo 
     * Author: Oracle 
     * Date: 2008
     * Availability: 
     * https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/BoxLayoutDemoProject/src/layout/BoxLayoutDemo.java
     */
    /**
     * This method creates an EndGameMenu for the given UserInterface.
     * 
     * @param gui The UserInterface associated with this EndGameMenu
     */
    public EndGameMenu(UserInterface gui) {
	frame = new JFrame("Go - Game Over");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setAlwaysOnTop(true);
	pane = frame.getContentPane();
	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
	blackPlayerName = gui.blackPlayerName();
	whitePlayerName = gui.whitePlayerName();
	HashMap<String, Integer> scores = gui.getGame().getFinalScore();
	blackScore = scores.get("blackScore");
	whiteScore = scores.get("whiteScore") + gui.getGame().getKomi();
	
	/*
	 * A portion of the below code is based on the following:
	 * 
	 * Title: CenterAlign 
	 * Author: Ranjith 
	 * Date: unknown
	 * Availability: 
	 * http://www.icsej.com/answers/747/center-align-text-in-jtextarea-jtextpane
	 */
	JTextPane scoreDisplay = new JTextPane();
	scoreDisplay.setText("Game over.\n\nFinal Score:\n" + blackPlayerName +
		": " + scoreFormat.format(blackScore) + "\n" + whitePlayerName +
		": " + scoreFormat.format(whiteScore) + "\n\n" + winner());
	scoreDisplay.setEditable(false);
	StyledDocument doc = scoreDisplay.getStyledDocument();
	SimpleAttributeSet center = new SimpleAttributeSet();
	StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
	doc.setParagraphAttributes(0, doc.getLength(), center, false);
	scoreDisplay.setOpaque(false);
	JPanel scoreDisplayPanel = new JPanel();
	scoreDisplayPanel.add(scoreDisplay);
	pane.add(scoreDisplayPanel);
	buttonPanel = new JPanel();
	buttonPanel.setLayout(new GridLayout(2, 2, 20, 10));
	buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
	pane.add(buttonPanel);
	JButton viewReplayButton = addAButton("View Replay");
	viewReplayButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	    }

	});
	JButton saveButton = addAButton("Save Replay");
	saveButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	    }

	});
	JButton playAgainButton = addAButton("Play Again");
	playAgainButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		frame.dispose();
		gui.initializeGame();
	    }

	});
	JButton mainMenuButton = addAButton("Main Menu");
	mainMenuButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		frame.dispose();
		gui.run();
	    }

	});
	
	frame.pack();
	frame.setVisible(true);
    }
    
    /**
     * This method creates a JButton with the given text, adds it to the button
     * panel, and then returns that JButton
     * 
     * @param text The text to be displayed on the button
     * @return The JButton that was created
     */
    private JButton addAButton(String text) {
	JButton button = new JButton(text);
	button.setAlignmentX(Component.CENTER_ALIGNMENT);
	buttonPanel.add(button);
	return button;
    }
    
    /**
     * This method returns a String indicating which Player has won, or if the
     * game is a tie.
     * 
     * @return A String indicating which Player has won, or if the game is a
     *         tie.
     */
    private String winner() {
	if (blackScore > whiteScore) {
	    return blackPlayerName + " wins!";
	} else if (whiteScore > blackScore) {
	    return whitePlayerName + " wins!";
	} else {
	    return "Tie game!";
	}
    }
}
