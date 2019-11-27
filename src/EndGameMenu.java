import java.awt.*;
import java.awt.event.*;
import java.io.*;
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
public class EndGameMenu implements ActionListener {
    private UserInterface gui;
    private JFrame frame;
    private Container pane;
    private JPanel buttonPanel;
    private String blackPlayerName;
    private String whitePlayerName;
    private double blackScore;
    private double whiteScore;
    private double scoreDifferential;
    private char winnerColor;
    private JFileChooser fileChooser;
    private File replayFile;
    
    private static final String VIEW_REPLAY = "View Replay";
    private static final String SAVE_REPLAY = "Save Replay";
    private static final String PLAY_AGAIN = "Play Again";
    private static final String MAIN_MENU = "Main Menu";
    private static final String CONTINUE = "Continue Play";
    private static final String EXIT = "Exit";

    /**
     * This is used to display scores.
     */
    public static final DecimalFormat SCORE_FORMAT = new DecimalFormat("#.#");
 
    /**
     * @return the scoreDifferential
     */
    public double getScoreDifferential() {
        return scoreDifferential;
    }

    /**
     * @return the winnerColor
     */
    public char getWinnerColor() {
        return winnerColor;
    }

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
	this.gui = gui;
	frame = new JFrame("Go - Game Over");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setAlwaysOnTop(true);
	pane = frame.getContentPane();
	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
	blackPlayerName = gui.blackPlayerName();
	whitePlayerName = gui.whitePlayerName();
	fileChooser = new JFileChooser();
	fileChooser.setFileFilter(new ReplayFileFilter());
	fileChooser.setAcceptAllFileFilterUsed(false);
	
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
	HashMap<String, Double> scores = gui.getGame().getFinalScore();
	if (scores == null) {
	    if (gui.isPracticeProblem()) {
		scoreDisplay.setText("You have reached the end of the solution\n" + 
			"path. You may continue play if you wish.");
	    } else {
		scoreDisplay.setText("Game over.");
	    }
	} else {
	    blackScore = scores.get("blackScore");
	    whiteScore = scores.get("whiteScore");
	    scoreDifferential = Math.abs(blackScore - whiteScore);
	    scoreDisplay.setText("Game over.\n\n" + winner());
	}
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
	if (gui.isPracticeProblem()) {
	    JButton continueButton = addAButton(CONTINUE);
	    JButton exitButton = addAButton(EXIT);
	} else {
	    JButton viewReplayButton = addAButton(VIEW_REPLAY);
	    JButton saveButton = addAButton(SAVE_REPLAY);

	    // replay games cannot be saved
	    if (gui.isReplayMode()) {
		viewReplayButton.setEnabled(false);
		saveButton.setEnabled(false);
	    }
	}
	JButton playAgainButton = addAButton(PLAY_AGAIN);
	JButton mainMenuButton = addAButton(MAIN_MENU);
	
	frame.pack();
	frame.setVisible(true);
    }
    
    /**
     * This method allows the user to choose where to save the replay, and it
     * sets replayFile to the file selected by the user.
     */
    private void saveReplay() {
	int returnVal = fileChooser.showSaveDialog(buttonPanel);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    replayFile = fileChooser.getSelectedFile();
	    String filePath = replayFile.getPath();
	    if (!filePath.toLowerCase().endsWith(".sgf")) {
		replayFile = new File(filePath + ".sgf");
	    }
	    if (replayFile.exists()) {
		int confirm = JOptionPane.showOptionDialog(frame,
			"Are you sure you want to overwrite " +
				replayFile.getName() + "?",
			"File Overwrite Warning", JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE, null, null, null);
		if (confirm != 0) {
		    replayFile = null;
		    return;
		}
	    }
	    try {
		FileWriter fw = new FileWriter(replayFile);
		PrintWriter pw = new PrintWriter(fw);
		Game game = (Game) gui.getGame();
		pw.println(game.getSgfStringBuilder());
		pw.flush();
		pw.close();
	    } catch (IOException e) {
		JOptionPane.showMessageDialog(frame,
			"File writing failed.\n" + 
		        "Please select a different location to save your replay.",
			"File Writing Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
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
	button.setActionCommand(text);
	button.addActionListener(this);
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
	String winText = " wins by " + SCORE_FORMAT.format(scoreDifferential) +
		" points!";
	String resignText = " resigned. ";
	String timedOutText = " timed out. ";
	String alternateWinText = " wins!";
	if (gui.getGame().getResignedPlayer() != null) {
	    String resignedPlayer = gui.getGame().getResignedPlayer();
	    String winningPlayer = "";
	    if (resignedPlayer.equals(blackPlayerName)) {
		winningPlayer = whitePlayerName;
		winnerColor = 'W';
	    } else {
		winningPlayer = blackPlayerName;
		winnerColor = 'B';
	    }
	    return resignedPlayer + resignText + winningPlayer +
		    alternateWinText;
	} else if (gui.getGame().getTimedOutPlayer() != null) {
	    String timedOutPlayer = gui.getGame().getTimedOutPlayer();
	    String winningPlayer = "";
	    if (timedOutPlayer.equals(blackPlayerName)) {
		winningPlayer = whitePlayerName;
		winnerColor = 'W';
	    } else {
		winningPlayer = blackPlayerName;
		winnerColor = 'B';
	    }
	    return timedOutPlayer + timedOutText + winningPlayer +
		    alternateWinText;
	} else if (blackScore > whiteScore) {
	    winnerColor = 'B';
	    return blackPlayerName + winText;
	} else if (whiteScore > blackScore) {
	    winnerColor = 'W';
	    return whitePlayerName + winText;
	} else {
	    winnerColor = '0';
	    return "Tie game!";
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();
	if (command.equals(VIEW_REPLAY)) {
	    if (replayFile == null) {
		int confirm = JOptionPane.showOptionDialog(frame,
			"You must save your replay before viewing it.\n" +
				"Press OK to choose where to save it.",
			"Save Replay", JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if (confirm == 0) {
		    saveReplay();
		} else {
		    return;
		}
	    }
	    if (replayFile != null) {
		frame.dispose();
		gui.viewReplay(replayFile);
	    }
	} else if (command.equals(SAVE_REPLAY)) {
	    saveReplay();
	} else if (command.equals(PLAY_AGAIN)) {
	    frame.dispose();
	    gui.initializeGame();
	} else if (command.equals(MAIN_MENU)) {
	    frame.dispose();
	    gui.run();
	} else if (command.equals(CONTINUE)) {
	    frame.dispose();
	} else if (command.equals(EXIT)) {
	    System.exit(0);
	}
    }
}
