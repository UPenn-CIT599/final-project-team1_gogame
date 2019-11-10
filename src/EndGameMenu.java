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
public class EndGameMenu {
    private UserInterface gui;
    private JFrame frame;
    private Container pane;
    private JPanel buttonPanel;
    private String blackPlayerName;
    private String whitePlayerName;
    private double blackScore;
    private double whiteScore;
    private double scoreDifferential;
    private JFileChooser fileChooser;
    private File replayFile;
    
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
	this.gui = gui;
	frame = new JFrame("Go - Game Over");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setAlwaysOnTop(true);
	pane = frame.getContentPane();
	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
	blackPlayerName = gui.blackPlayerName();
	whitePlayerName = gui.whitePlayerName();
	HashMap<String, Double> scores = gui.getGame().getFinalScore();
	blackScore = scores.get("blackScore");
	whiteScore = scores.get("whiteScore") + gui.getGame().getKomi();
	scoreDifferential = Math.abs(blackScore - whiteScore);
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
		// TODO open the replay file
	    }

	});
	JButton saveButton = addAButton("Save Replay");
	saveButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		saveReplay();
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
		    return;
		}
	    }
	    try {
		FileWriter fw = new FileWriter(replayFile);
		PrintWriter pw = new PrintWriter(fw);
		pw.println("Testing .sgf writer"); // TODO
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
	String winText = " wins by " + scoreFormat.format(scoreDifferential) +
		" points!";
	String resignText = " resigned. ";
	String resignWinText = " wins!";
	if (gui.getGame().getResignedPlayer() != null) {
	    String resignedPlayer = gui.getGame().getResignedPlayer();
	    String winningPlayer = "";
	    if (resignedPlayer.equals(blackPlayerName)) {
		winningPlayer = whitePlayerName;
	    } else {
		winningPlayer = blackPlayerName;
	    }
	    return resignedPlayer + resignText + winningPlayer + resignWinText;
	} else if (blackScore > whiteScore) {
	    return blackPlayerName + winText;
	} else if (whiteScore > blackScore) {
	    return whitePlayerName + winText;
	} else {
	    return "Tie game!";
	}
    }
}
