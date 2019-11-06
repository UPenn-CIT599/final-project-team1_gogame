import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 * The MainMenu class represents the main menu of the Go game.
 * 
 * @author Chris Hartung
 *
 */
public class MainMenu {
    private UserInterface gui;
    private JFrame frame;
    private String player1Name = "";
    private String player1Name1 = "";
    private String player1Name2 = "";
    private String player2Name = "";
    private boolean replayMode = false;
    private boolean onePlayerGame = false;
    private int handicap = 0;
    private int handicap1 = 0;
    private int handicap2 = 0;
    private double komi = 6.5;
    private double komi1 = 6.5;
    private double komi2 = 6.5;
    private int numRows = 19;;
    private int numRows1 = 19;
    private int numRows2 = 19;
    private String player1Color = "Black";
    private String player1Color1 = "Black";
    private String player1Color2 = "Black";
    private File replayFile = null;
    private boolean readyToPlay = false;

    /**
     * @return the player1Name
     */
    public String getPlayer1Name() {
	return player1Name;
    }

    /**
     * @return the player2Name
     */
    public String getPlayer2Name() {
	return player2Name;
    }

    /**
     * @return replayMode
     */
    public boolean isReplayMode() {
	return replayMode;
    }

    /**
     * @return onePlayerGame
     */
    public boolean isOnePlayerGame() {
	return onePlayerGame;
    }

    /**
     * @return the handicap
     */
    public int getHandicap() {
	return handicap;
    }

    /**
     * @return the komi
     */
    public double getKomi() {
	return komi;
    }
    
    /**
     * @return the numRows
     */
    public int getNumRows() {
	return numRows;
    }

    /**
     * @return the player1Color
     */
    public String getPlayer1Color() {
	return player1Color;
    }

    /**
     * @return readyToPlay
     */
    public boolean isReadyToPlay() {
	return readyToPlay;
    }

    /**
     * This method creates a MainMenu associated with the given UserInterface.
     * 
     * @param gui The UserInterface associated with this MainMenu
     */
    public MainMenu(UserInterface gui) {
	/*
	 * A portion of the below code is based on the following:
	 * 
	 * Title: CardLayoutDemo 
	 * Author: Oracle 
	 * Date: 2008
	 * Availability: 
	 * https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/CardLayoutDemoProject/src/layout/CardLayoutDemo.java
	 */
	this.gui = gui;
	frame = new JFrame("Go - Main Menu");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	String select = "Select game mode";
	String onePlayer = "1 Player Game";
	String twoPlayer = "2 Player Game";
	String replay = "Replay Mode";
	JPanel comboBoxPane = new JPanel();
//	JTextField welcome = new JTextField("Welcome to Go!");
//	welcome.setEditable(false);
//	welcome.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	JTextField welcome = createTextField("Welcome to Go!");
	welcome.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
	String[] comboBoxItems = { select, onePlayer, twoPlayer, replay };
	JComboBox<String> selectGameMode = new JComboBox<>(comboBoxItems);
	selectGameMode.setEditable(false);
	selectGameMode.setMaximumSize(new Dimension(135, 50));
	comboBoxPane.add(welcome);
	comboBoxPane.add(selectGameMode);
	comboBoxPane.setLayout(new BoxLayout(comboBoxPane, BoxLayout.Y_AXIS));
	
	JPanel namePanel1 = createNamePanel(1, 1);
	JPanel namePanel2 = createNamePanel(1, 2);
	JPanel namePanel3 = createNamePanel(2, 2);
	
	JPanel colorPanel1 = createColorPanel(1);
	JPanel colorPanel2 = createColorPanel(2);

	JPanel boardSizePanel1 = createBoardSizePanel(1);
	JPanel boardSizePanel2 = createBoardSizePanel(2);

	
	JPanel komiPanel1 = createKomiPanel(1);
	JPanel komiPanel2 = createKomiPanel(2);

	JPanel handicapPanel1 = createHandicapPanel(1, komiPanel1);
	JPanel handicapPanel2 = createHandicapPanel(2, komiPanel2);
	
	JPanel buttonPanel1 = createStartButtonPanel(1);
	JPanel buttonPanel2 = createStartButtonPanel(2);
	
	JPanel selectCard = new JPanel();

	JPanel onePlayerCard = new JPanel();
	onePlayerCard.setLayout(new BoxLayout(onePlayerCard, BoxLayout.Y_AXIS));
	onePlayerCard.add(namePanel1);
	onePlayerCard.add(colorPanel1);
	onePlayerCard.add(boardSizePanel1);
	onePlayerCard.add(handicapPanel1);
	onePlayerCard.add(komiPanel1);
	onePlayerCard.add(buttonPanel1);

	JPanel twoPlayerCard = new JPanel();
	twoPlayerCard.setLayout(new BoxLayout(twoPlayerCard, BoxLayout.Y_AXIS));
	twoPlayerCard.add(namePanel2);
	twoPlayerCard.add(colorPanel2);
	twoPlayerCard.add(namePanel3);
	twoPlayerCard.add(boardSizePanel2);
	twoPlayerCard.add(handicapPanel2);
	twoPlayerCard.add(komiPanel2);
	twoPlayerCard.add(buttonPanel2);

	JPanel replayCard = new JPanel();
	replayCard.setLayout(new BoxLayout(replayCard, BoxLayout.Y_AXIS));

	JPanel chooseFileLabelPanel = new JPanel();
//	JTextField chooseFileLabel = new JTextField(
//		"Please choose a replay file.");
	JTextField chooseFileLabel = createTextField(
		"Please choose a replay file.");
//	chooseFileLabel.setEditable(false);
//	chooseFileLabel.setHorizontalAlignment(JTextField.CENTER);
	chooseFileLabelPanel.add(chooseFileLabel);

	JPanel selectedFilePanel = new JPanel();
	selectedFilePanel
		.setLayout(new BoxLayout(selectedFilePanel, BoxLayout.Y_AXIS));
	JTextField selectedFileLabel = createTextField("");
	JTextField selectedFileName = createTextField("");
	selectedFilePanel.add(selectedFileLabel);
	selectedFilePanel.add(selectedFileName);

	JPanel startReplayPanel = new JPanel();
	JButton startReplayButton = new JButton("Start Replay");
	startReplayButton.setEnabled(false);
	startReplayButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		System.out.println("opening " + replayFile.getName()); // TODO
	    }

	});
	startReplayPanel.add(startReplayButton);

	JPanel chooseFilePanel = new JPanel();
	JButton chooseFileButton = new JButton("Select file");
	chooseFileButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		/*
		 * A portion of the below code is based on the following:
		 * 
		 * Title: FileChooserDemo 
		 * Author: Oracle 
		 * Date: 2008 
		 * Availability:
		 * https://docs.oracle.com/javase/tutorial/uiswing/examples/components/FileChooserDemoProject/src/components/FileChooserDemo.java
		 */
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new ReplayFileFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);
		int returnVal = fileChooser.showOpenDialog(replayCard);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    replayFile = fileChooser.getSelectedFile();
		    startReplayButton.setEnabled(true);
		    selectedFileLabel
			    .setText("You selected the following file:");
		    selectedFileName.setText(replayFile.getName());
		}
	    }

	});
	chooseFilePanel.add(chooseFileButton);

	replayCard.add(chooseFileLabelPanel);
	replayCard.add(chooseFilePanel);
	replayCard.add(selectedFilePanel);
	replayCard.add(startReplayPanel);

	JPanel cards = new JPanel(new CardLayout());
	cards.add(selectCard, select);
	cards.add(onePlayerCard, onePlayer);
	cards.add(twoPlayerCard, twoPlayer);
	cards.add(replayCard, replay);

	Container pane = frame.getContentPane();
	pane.add(comboBoxPane, BorderLayout.PAGE_START);
	pane.add(cards, BorderLayout.CENTER);

	selectGameMode.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, (String) evt.getItem());
	    }

	});

	frame.pack();
	frame.setVisible(true);
    }

    /*
     * The createFilteredField method comes from the following, with slight
     * modifications:
     * 
     * Title: FilterDemo 
     * Author: Paul Samsotha 
     * Date: 2017
     * Availability:
     * https://stackoverflow.com/questions/24844559/jtextfield-using-document-filter-to-filter-integers-and-periods
     */    
    /**
     * This method creates a JTextField for the user to enter their name. The
     * output field will accept a maximum of 12 characters and will only accept
     * letters, numbers, and spaces.
     * 
     * @return A filtered JTextField
     */
    private JTextField createFilteredField() {
	JTextField field = new JTextField(12);
	AbstractDocument document = (AbstractDocument) field.getDocument();
	document.setDocumentFilter(new DocumentFilter() {

	    @Override
	    public void replace(FilterBypass fb, int offs, int length,
		    String str, AttributeSet a) throws BadLocationException {

		String text = fb.getDocument().getText(0,
			fb.getDocument().getLength());

		StringBuilder builder = new StringBuilder(text);
		builder.insert(offs, str);
		String newText = builder.toString();

		if (newText.matches("[a-zA-Z[0-9[ ]]]*") &&
			(newText.length() <= 12)) {
		    super.replace(fb, offs, length, str, a);
		} else {
		    Toolkit.getDefaultToolkit().beep();
		}
	    }

	    @Override
	    public void insertString(FilterBypass fb, int offs, String str,
		    AttributeSet a) throws BadLocationException {

		String text = fb.getDocument().getText(0,
			fb.getDocument().getLength());

		StringBuilder builder = new StringBuilder(text);
		builder.insert(offs, str);
		String newText = builder.toString();

		if (newText.matches("[a-zA-Z[0-9[ ]]]*") &&
			(newText.length() <= 12)) {
		    super.insertString(fb, offs, str, a);
		} else {
		    Toolkit.getDefaultToolkit().beep();
		}
	    }
	});
	return field;
    }
    
    /**
     * This method is used by the createNamePanel method to set a Player's name.
     * 
     * @param playerNumber The number of the Player
     * @param numPlayers   The number of human Players in the game
     * @param name         The name of the Player
     */
    private void setName(int playerNumber, int numPlayers, String name) {
	if (playerNumber == 2) {
	    player2Name = name;
	} else if (numPlayers == 1) {
	    player1Name1 = name;
	} else {
	    player1Name2 = name;
	}
    }

    /**
     * This method creates a JPanel to enter the user's name.
     * 
     * @param playerNumber The number of the Player whose name will be entered
     * @param numPlayers   The number of human Players in the game
     * @return The JPanel used to enter the user's name
     */
    private JPanel createNamePanel(int playerNumber, int numPlayers) {
	JPanel namePanel = new JPanel();
//	JTextField choosePlayerNameLabel = new JTextField(
//		"Player " + playerNumber + ", please enter your name: ");
//	choosePlayerNameLabel.setEditable(false);
	JTextField choosePlayerNameLabel = createTextField(
		"Player " + playerNumber + ", please enter your name: ");
	JTextField choosePlayerName = createFilteredField();
	choosePlayerName.getDocument()
		.addDocumentListener(new DocumentListener() {

		    @Override
		    public void changedUpdate(DocumentEvent e) {
			setName(playerNumber, numPlayers,
				choosePlayerName.getText());
		    }

		    @Override
		    public void insertUpdate(DocumentEvent e) {
			setName(playerNumber, numPlayers,
				choosePlayerName.getText());
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
			setName(playerNumber, numPlayers,
				choosePlayerName.getText());
		    }

		});
	namePanel.add(choosePlayerNameLabel);
	namePanel.add(choosePlayerName);
	return namePanel;
    }
    
    /**
     * This method creates a JPanel to enter Player 1's color.
     * 
     * @param numPlayers The number of human Players in the game
     * @return The JPanel used to enter Player 1's color
     */
    private JPanel createColorPanel(int numPlayers) {
	JPanel colorPanel = new JPanel();
	String[] colorChoices = { "Black", "White", "Random" };
//	JTextField colorChooserLabel = new JTextField(
//		"Player 1, please choose a color: ");
//	colorChooserLabel.setEditable(false);
	JTextField colorChooserLabel = createTextField(
		"Player 1, please choose a color: ");
	JComboBox<String> colorChooser = new JComboBox<>(colorChoices);
	colorChooser.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent evt) {
		String color = (String) evt.getItem();
		if (numPlayers == 1) {
		    player1Color1 = color;
		} else {
		    player1Color2 = color;
		}
	    }

	});
	colorPanel.add(colorChooserLabel);
	colorPanel.add(colorChooser);
	return colorPanel;
    }
    
    /*
     * A portion of the createBoardSizePanel, createHandicapPanel, and
     * createKomiPanel methods is based on the following:
     * 
     * Title: SliderDemo2 
     * Author: Oracle 
     * Date: 2008 
     * Availability:
     * https://docs.oracle.com/javase/tutorial/uiswing/examples/components/SliderDemo2Project/src/components/SliderDemo2.java
     */
    /**
     * This method creates a JPanel to select the board size.
     * 
     * @param numPlayers The number of human Players in the game
     * @return The JPanel used to select the board size
     */
    private JPanel createBoardSizePanel(int numPlayers) {
	JPanel boardSizePanel = new JPanel();
	boardSizePanel
		.setLayout(new BoxLayout(boardSizePanel, BoxLayout.Y_AXIS));
//	JTextField boardSizeChooserLabel = new JTextField(
//		"Please choose a board size:");
//	boardSizeChooserLabel.setEditable(false);
//	boardSizeChooserLabel
//		.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	JTextField boardSizeChooserLabel = createTextField(
		"Please choose a board size:");
	JSlider boardSizeChooser = new JSlider(JSlider.HORIZONTAL, 5, 19, 19);
	boardSizeChooser.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent arg0) {
		if (!boardSizeChooser.getValueIsAdjusting()) {
		    int value = (int) boardSizeChooser.getValue();
		    if (numPlayers == 1) {
			numRows1 = value;
		    } else {
			numRows2 = value;
		    }
		}
	    }

	});
	boardSizeChooser.setMajorTickSpacing(1);
	boardSizeChooser.setPaintTicks(true);
	boardSizeChooser.setPaintLabels(true);
	boardSizePanel.add(boardSizeChooserLabel);
	boardSizePanel.add(boardSizeChooser);
	return boardSizePanel;
    }
    
    /**
     * This method creates a JPanel to select the komi.
     * 
     * @param numPlayers The number of human Players in the game
     * @return The JPanel used to select the komi
     */
    public JPanel createKomiPanel(int numPlayers) {
	double maxKomi = 8.5;
	DecimalFormat komiDecimalFormat = new DecimalFormat("#.#");
	
	JPanel komiPanel = new JPanel();
	komiPanel.setLayout(new BoxLayout(komiPanel, BoxLayout.Y_AXIS));
	JTextField komiChooserLabel = new JTextField(
		"If handicap is 0, please choose a komi:");
	komiChooserLabel.setEditable(false);
	komiChooserLabel
		.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
//	JTextField komiChooserLabel = new JTextField(
//		"If handicap is 0, please choose a komi:");
	komiPanel.add(komiChooserLabel);
	JSlider komiChooser = new JSlider(JSlider.HORIZONTAL, 0, (int) maxKomi, 6);
	
	// JSlider uses integers, so it must be relabeled and 0.5 must be added
	// to its values to correct for this
	Hashtable<Integer, JLabel> komiLabelTable = new Hashtable<>();
	for (int i = 0; i < maxKomi; i++) {
	    komiLabelTable.put(i, new JLabel(komiDecimalFormat.format(i + 0.5)));
	}
	komiChooser.setLabelTable(komiLabelTable);	
	komiChooser.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent arg0) {
		if (!komiChooser.getValueIsAdjusting()) {
		    double value = komiChooser.getValue() + 0.5;
		    if (numPlayers == 1) {
			komi1 = value;
		    } else {
			komi2 = value;
		    }
		}
	    }

	});
	komiChooser.setMajorTickSpacing(1);
	komiChooser.setPaintTicks(true);
	komiChooser.setPaintLabels(true);
	komiPanel.add(komiChooser);
	return komiPanel;
    }
    
    /**
     * This method creates a JPanel to select the handicap.
     * 
     * @param numPlayers The number of human Players in the game
     * @return The JPanel used to select the handicap
     */
    private JPanel createHandicapPanel(int numPlayers, JPanel komiPanel) {
	JPanel handicapPanel = new JPanel();
	handicapPanel.setLayout(new BoxLayout(handicapPanel, BoxLayout.Y_AXIS));
//	JTextField handicapChooserLabel = new JTextField(
//		"Please choose a handicap:");
//	handicapChooserLabel.setEditable(false);
//	handicapChooserLabel
//		.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	JTextField handicapChooserLabel = createTextField(
		"Please choose a handicap:");
	JSlider handicapChooser = new JSlider(JSlider.HORIZONTAL, 0, 9, 0);
	handicapChooser.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent arg0) {
		if (!handicapChooser.getValueIsAdjusting()) {
		    int value = (int) handicapChooser.getValue();
		    if (numPlayers == 1) {
			handicap1 = value;
		    } else {
			handicap2 = value;
		    }

		    JTextField komiChooserLabel = (JTextField) komiPanel
			    .getComponent(0);
		    JSlider komiChooser = (JSlider) komiPanel.getComponent(1);
		    if (value == 0) {
			komiChooserLabel.setEnabled(true);
			komiChooser.setEnabled(true);
		    } else {
			komiChooser.setValue(0);
			komiChooserLabel.setEnabled(false);
			komiChooser.setEnabled(false);
		    }
		}
	    }

	});
	handicapChooser.setMajorTickSpacing(1);
	handicapChooser.setPaintTicks(true);
	handicapChooser.setPaintLabels(true);
	handicapPanel.add(handicapChooserLabel);
	handicapPanel.add(handicapChooser);
	return handicapPanel; // TODO
    }
    
    /**
     * This method creates a JPanel to start the game.
     * 
     * @param numPlayers The number of human Players in the game
     * @return The JPanel used to start the game
     */
    private JPanel createStartButtonPanel(int numPlayers) {
	JPanel buttonPanel = new JPanel();
	JButton button = new JButton("Start Game");
	button.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		if (numPlayers == 1) {
		    player1Name = player1Name1;
		    player1Color = player1Color1;
		    numRows = numRows1;
		    handicap = handicap1;
		    komi = komi1;
		    onePlayerGame = true;
		} else {
		    player1Name = player1Name2;
		    player1Color = player1Color2;
		    numRows = numRows2;
		    handicap = handicap2;
		    komi = komi2;
		    onePlayerGame = false;
		}
		replayMode = false;
		readyToPlay = true;
//		frame.setVisible(false);
		frame.dispose();
		gui.initializeGame();
	    }

	});
	buttonPanel.add(button);
	return buttonPanel;
    }
    
    /**
     * This method creates and returns a JTextField with the given text
     * 
     * @param text The text to display
     * @return An empty JTextField
     */
    private JTextField createTextField(String text) {
	JTextField field = new JTextField(text);
	field.setEditable(false);
	field.setHorizontalAlignment(JTextField.CENTER);
	field.setMaximumSize(new Dimension(500, 30));
	field.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	return field;
    }
}
