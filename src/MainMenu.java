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
public class MainMenu implements ActionListener, ItemListener, ChangeListener {
    private UserInterface gui;
    private JFrame frame;
    private String player1Name = "";
    private String player2Name = "";
    private boolean replayMode = true;
    private boolean onePlayerGame = false;
    private int handicap = 0;
    private double komi = 6.5;
    private int numRows = 19;;
    private String player1Color = "Black";
    private boolean timed = false;
    private int mainTime = 60; // measured in minutes
    private int numByoYomiPeriods = 5;
    private int byoYomiLength = 60; // measured in seconds
    private JFileChooser fileChooser;
    private File replayFile = null;
    private boolean practiceProblem = false;
    private boolean readyToPlay = false;
    
    private JPanel cards;
    private JPanel replayCard;
    private JPanel selectedFilePanel;
    private JPanel player2NamePanel;
    private JPanel komiPanel;
    private JPanel timerComboBoxPanel;
    
    private static String SELECT_GAME_MODE = "Select game mode";
    private static String COLOR = "Color";
    private static String BOARD_SIZE = "Board Size";
    private static String HANDICAP = "Handicap";
    private static String KOMI = "Komi";
    private static String MAIN_TIMER = "Main Timer";
    private static String NUM_BYO_YOMI = "Num Byo-Yomi";
    private static String BYO_YOMI_LENGTH = "Byo-Yomi Length";
    private static String SELECT_FILE = "Select File";
    private static String REPLAY = "Replay";
    private static String PRACTICE = "Practice Problem";
    private static String START_GAME = "Start Game";
    private static String START_REPLAY = "Start";
    private static String TIMER_ON = "On";
    private static String TIMER_OFF = "Off";

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
     * @return timed
     */
    public boolean isTimed() {
        return timed;
    }
    
    /**
     * @return the mainTime
     */
    public int getMainTime() {
        return mainTime;
    }

    /**
     * @return the numByoYomiPeriods
     */
    public int getNumByoYomiPeriods() {
        return numByoYomiPeriods;
    }

    /**
     * @return the byoYomiLength
     */
    public int getByoYomiLength() {
        return byoYomiLength;
    }

    /**
     * @return practiceProblem
     */
    public boolean isPracticeProblem() {
	return practiceProblem;
    }

    /**
     * @return readyToPlay
     */
    public boolean isReadyToPlay() {
	return readyToPlay;
    }
    
    /**
     * @return the replayFile
     */
    public File getReplayFile() {
    	return replayFile;
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
	
	// create the combo box panel that determines which card in the
	// CardLayout is visible
	String playGame = "Play Game";
	String openFile = "Open File";
	JPanel comboBoxPane = createBoxLayoutPanel();
	JTextField welcome = createTextField("Welcome to Go!");
	welcome.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
	String[] comboBoxItems = { SELECT_GAME_MODE, playGame, openFile };
	JComboBox<String> selectGameMode = new JComboBox<>(comboBoxItems);
	selectGameMode.setEditable(false);
	selectGameMode.setMaximumSize(new Dimension(135, 50));
	comboBoxPane.add(welcome);
	comboBoxPane.add(selectGameMode);
	
	// create the JPanels for the "Play Game" card
	JPanel numPlayersPanel = createNumPlayersPanel();	
	JPanel player1NamePanel = createNamePanel(1);
	JPanel player2NamePanel = createNamePanel(2);	
	JPanel colorPanel = createColorPanel();
	JPanel boardSizePanel = createBoardSizePanel();	
	JPanel komiPanel = createKomiPanel();
	JPanel handicapPanel = createHandicapPanel();	
	JPanel timerPanel = createTimerPanel(); 	
	JPanel buttonPanel = createStartButtonPanel();
	
	// create the default "Select Game Mode" card
	JPanel selectCard = new JPanel();

	// create the "Play Game" card and add all necessary panels
	JPanel playGameCard = createBoxLayoutPanel();
	playGameCard.add(numPlayersPanel);
	playGameCard.add(player1NamePanel);
	playGameCard.add(colorPanel);
	playGameCard.add(player2NamePanel);
	playGameCard.add(boardSizePanel);
	playGameCard.add(handicapPanel);
	playGameCard.add(komiPanel);
	playGameCard.add(timerPanel);
	playGameCard.add(buttonPanel);
	
	// create the "Open File" card
	replayCard = createReplayPanel();

	// set up the CardLayout
	cards = new JPanel(new CardLayout());
	cards.add(selectCard, SELECT_GAME_MODE);
	cards.add(playGameCard, playGame);
	cards.add(replayCard, openFile);
	Container pane = frame.getContentPane();
	pane.add(comboBoxPane, BorderLayout.PAGE_START);
	pane.add(cards, BorderLayout.CENTER);
	selectGameMode.setName(SELECT_GAME_MODE);
	selectGameMode.addItemListener(this);

	// assemble the main menu and make it visible
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
     * This method sets a Player's name.
     * 
     * @param playerNumber The number of the Player
     * @param name         The name of the Player
     */
    private void setName(int playerNumber, String name) {
	if (playerNumber == 2) {
	    player2Name = name;
	} else {
	    player1Name = name;
	}
    }
    
    /**
     * This method creates a JPanel to select and open an sgf file.
     * @return The JPanel to select and open an sgf file
     */
    private JPanel createReplayPanel() {
	JPanel replayPanel = createBoxLayoutPanel();

	selectedFilePanel = createBoxLayoutPanel();
	JTextField selectedFileLabel = createTextField("");
	JTextField selectedFileName = createTextField("");
	selectedFilePanel.add(selectedFileLabel);
	selectedFilePanel.add(selectedFileName);
	
	JPanel isPracticeProblemPanel = createBoxLayoutPanel();
	JTextField isPracticeProblemLabel = createTextField(
		"Is the selected file a replay or a practice problem?");
	isPracticeProblemLabel.setEnabled(false);
	JRadioButton replayButton = createRadioButton(REPLAY, KeyEvent.VK_R);
	replayButton.setSelected(true);
	replayButton.setEnabled(false);
	JRadioButton practiceProblemButton = createRadioButton(PRACTICE,
		KeyEvent.VK_P);
	practiceProblemButton.setEnabled(false);
	ButtonGroup buttonGroup = new ButtonGroup();
	buttonGroup.add(replayButton);
	buttonGroup.add(practiceProblemButton);
	JPanel isPracticeProblemButtonPanel = createBoxLayoutPanel(false);
	isPracticeProblemButtonPanel.add(replayButton);
	isPracticeProblemButtonPanel.add(practiceProblemButton);
	isPracticeProblemPanel.add(isPracticeProblemLabel);
	isPracticeProblemPanel.add(isPracticeProblemButtonPanel);
	isPracticeProblemPanel
		.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

	JPanel startReplayPanel = new JPanel();
	JButton startReplayButton = new JButton(START_REPLAY);
	startReplayButton.setEnabled(false);
	startReplayButton.setActionCommand(START_REPLAY);
	startReplayButton.addActionListener(this);
	startReplayPanel.add(startReplayButton);

	JPanel chooseFilePanel = createBoxLayoutPanel();
	JTextField chooseFileLabel = createTextField("Please choose a file.");
	chooseFileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	/*
	 * A portion of the below code is based on the following:
	 * 
	 * Title: FileChooserDemo 
	 * Author: Oracle 
	 * Date: 2008 
	 * Availability:
	 * https://docs.oracle.com/javase/tutorial/uiswing/examples/components/FileChooserDemoProject/src/components/FileChooserDemo.java
	 */
	fileChooser = new JFileChooser();
	fileChooser.setFileFilter(new ReplayFileFilter());
	fileChooser.setAcceptAllFileFilterUsed(false);
	JButton chooseFileButton = new JButton(SELECT_FILE);
	chooseFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	chooseFileButton.setActionCommand(SELECT_FILE);
	chooseFileButton.addActionListener(this);
	chooseFilePanel.add(chooseFileLabel);
	chooseFilePanel.add(chooseFileButton);
	chooseFilePanel
		.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

	replayPanel.add(chooseFilePanel);
	replayPanel.add(selectedFilePanel);
	replayPanel.add(isPracticeProblemPanel);
	replayPanel.add(startReplayPanel);
	
	return replayPanel;
    }
    
    /**
     * This method creates a JPanel to select the number of players.
     * 
     * @return The JPanel used to select the number of players
     */
    private JPanel createNumPlayersPanel() {
	JPanel numPlayersPanel = new JPanel();
	JTextField numPlayersLabel = createTextField(
		"Choose the number of players: ");
	JRadioButton onePlayerButton = createRadioButton("1", KeyEvent.VK_1);
	JRadioButton twoPlayerButton = createRadioButton("2", KeyEvent.VK_2);
	twoPlayerButton.setSelected(true);
	ButtonGroup buttonGroup = new ButtonGroup();
	buttonGroup.add(onePlayerButton);
	buttonGroup.add(twoPlayerButton);
	numPlayersPanel.add(numPlayersLabel);
	numPlayersPanel.add(onePlayerButton);
	numPlayersPanel.add(twoPlayerButton);
	return numPlayersPanel;
    }

    /**
     * This method creates a JPanel to enter the user's name.
     * 
     * @param playerNumber The number of the Player whose name will be entered
     * @return The JPanel used to enter the user's name
     */
    private JPanel createNamePanel(int playerNumber) {
	JPanel namePanel = new JPanel();
	JTextField choosePlayerNameLabel = createTextField(
		"Player " + playerNumber + ", please enter your name: ");
	JTextField choosePlayerName = createFilteredField();
	choosePlayerName.getDocument()
		.addDocumentListener(new DocumentListener() {

		    @Override
		    public void changedUpdate(DocumentEvent e) {
			setName(playerNumber, choosePlayerName.getText());
		    }

		    @Override
		    public void insertUpdate(DocumentEvent e) {
			setName(playerNumber, choosePlayerName.getText());
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
			setName(playerNumber, choosePlayerName.getText());
		    }

		});
	namePanel.add(choosePlayerNameLabel);
	namePanel.add(choosePlayerName);
	if (playerNumber == 2) {
	    player2NamePanel = namePanel;
	}
	return namePanel;
    }
    
    /**
     * This method creates a JPanel to enter Player 1's color.
     * 
     * @return The JPanel used to enter Player 1's color
     */
    private JPanel createColorPanel() {
	JPanel colorPanel = new JPanel();
	String[] colorChoices = { "Black", "White", "Random" };
	JTextField colorChooserLabel = createTextField(
		"Player 1, please choose a color: ");
	JComboBox<String> colorChooser = new JComboBox<>(colorChoices);
	colorChooser.setName(COLOR);
	colorChooser.addItemListener(this);
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
     * @return The JPanel used to select the board size
     */
    private JPanel createBoardSizePanel() {
	JPanel boardSizePanel = createBoxLayoutPanel();
	JTextField boardSizeChooserLabel = createTextField(
		"Please choose a board size:");
	JSlider boardSizeChooser = new JSlider(JSlider.HORIZONTAL, 5, 19, 19);
	boardSizeChooser.setName(BOARD_SIZE);
	boardSizeChooser.addChangeListener(this);
	boardSizeChooser.setMajorTickSpacing(1);
	boardSizeChooser.setPaintTicks(true);
	boardSizeChooser.setPaintLabels(true);
	boardSizeChooser
		.setToolTipText("The number of rows and columns on the board");
	boardSizePanel.add(boardSizeChooserLabel);
	boardSizePanel.add(boardSizeChooser);
	return boardSizePanel;
    }
    
    /**
     * This method creates a JPanel to select the komi.
     * 
     * @return The JPanel used to select the komi
     */
    public JPanel createKomiPanel() {
	double maxKomi = 8.5;
	DecimalFormat komiDecimalFormat = new DecimalFormat("#.#");
	
	komiPanel = createBoxLayoutPanel();
	JTextField komiChooserLabel = createTextField(
		"If handicap is 0, please choose a komi:");
	komiChooserLabel.setEditable(false);
	komiChooserLabel
		.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	komiPanel.add(komiChooserLabel);
	JSlider komiChooser = new JSlider(JSlider.HORIZONTAL, 0, (int) maxKomi, 6);
	
	// JSlider uses integers, so it must be relabeled and 0.5 must be added
	// to its values to correct for this
	Hashtable<Integer, JLabel> komiLabelTable = new Hashtable<>();
	for (int i = 0; i < maxKomi; i++) {
	    komiLabelTable.put(i, new JLabel(komiDecimalFormat.format(i + 0.5)));
	}
	komiChooser.setLabelTable(komiLabelTable);	
	komiChooser.setName(KOMI);
	komiChooser.addChangeListener(this);
	komiChooser.setMajorTickSpacing(1);
	komiChooser.setPaintTicks(true);
	komiChooser.setPaintLabels(true);
	komiChooser.setToolTipText("The number of points added to white's" +
		" score to make up for black's first move advantage");
	komiPanel.add(komiChooser);
	return komiPanel;
    }
    
    /**
     * This method creates a JPanel to select the handicap.
     * 
     * @return The JPanel used to select the handicap
     */
    private JPanel createHandicapPanel() {
	JPanel handicapPanel = createBoxLayoutPanel();
	JTextField handicapChooserLabel = createTextField(
		"Please choose a handicap:");
	JSlider handicapChooser = new JSlider(JSlider.HORIZONTAL, 0, 9, 0);
	handicapChooser.setName(HANDICAP);
	handicapChooser.addChangeListener(this);
	handicapChooser.setMajorTickSpacing(1);
	handicapChooser.setPaintTicks(true);
	handicapChooser.setPaintLabels(true);
	handicapChooser.setToolTipText("The number of stones black places" +
		" at the beginning of the game before white gets to play");
	handicapPanel.add(handicapChooserLabel);
	handicapPanel.add(handicapChooser);
	return handicapPanel;
    }
    
    /**
     * This method returns the String[] of options for the main timer length.
     * 
     * @return The String[] of options for the main timer length
     */
    private String[] timerLengthOptions() {
	String[] output = new String[51];
	for (int i = 0; i < 51; i++) {
	    output[i] = (i / 6) + " hours, " + ((i % 6) * 10) + " minutes";
	}
	return output;
    }

    /**
     * This method returns the String[] of options for the number of byo-yomi
     * periods.
     * 
     * @return The String[] of options for the number of byo-yomi periods
     */
    private String[] numByoYomiOptions() {
	String[] output = new String[60];
	for (int i = 0; i < 10; i++) {
	    output[i] = Integer.toString(i);
	}
	for (int i = 1; i <= 50; i++) {
	    output[i + 9] = Integer.toString(10 * i);
	}
	return output;
    }

    /**
     * This method returns the String[] of options for byo-yomi length.
     * 
     * @return The String[] of options for byo-yomi length
     */
    private String[] byoYomiLengthLabel() {
	String[] output = new String[8];
	for (int i = 1; i <= 8; i++) {
	    output[i - 1] = (i / 4) + " minutes, " + ((i % 4) * 15) +
		    " seconds";
	}
	return output;
    }
    
    /**
     * This method enables or disables all components in the given JPanel based
     * on the given boolean input.
     * 
     * @param panel   The JPanel to be enabled or disabled
     * @param enabled True if components are to be enabled and false if they are
     *                to be disabled
     */
    private void setEnabledAllComponents(JPanel panel, boolean enabled) {
	for (Component component : panel.getComponents()) {
	    if (component instanceof JPanel) {
		setEnabledAllComponents((JPanel) component, enabled);
	    } else {
		component.setEnabled(enabled);
	    }
	}
    }
    
    /**
     * This method creates a JPanel for selecting a timer.
     * @return A JPanel for selecting a timer.
     */
    private JPanel createTimerPanel() {
	JPanel timerPanel = createBoxLayoutPanel();
	JTextField isTimedLabel = createTextField("Timer:");
	JRadioButton timerOnButton = createRadioButton(TIMER_ON, KeyEvent.VK_N);
	JRadioButton timerOffButton = createRadioButton(TIMER_OFF,
		KeyEvent.VK_F);
	timerOffButton.setSelected(true);
	ButtonGroup buttonGroup = new ButtonGroup();
	buttonGroup.add(timerOnButton);
	buttonGroup.add(timerOffButton);
	JTextField mainTimerLabel = createTextField("Main Time: ");
	JTextField numByoYomiLabel = createTextField(
		"Byo-Yomi Periods: ");
	JTextField byoYomiLengthLabel = createTextField("Byo-Yomi Length:");
	JComboBox<String> mainTimerComboBox = new JComboBox<>(timerLengthOptions());
	mainTimerComboBox.setSelectedIndex(6);
	mainTimerComboBox.setName(MAIN_TIMER);
	mainTimerComboBox.addItemListener(this);
	mainTimerComboBox.setToolTipText("Each player has this much time in" +
		" total for their moves before byo-yomi begins");
	JComboBox<String> numByoYomiComboBox = new JComboBox<>(numByoYomiOptions());
	numByoYomiComboBox.setSelectedIndex(5);
	numByoYomiComboBox.setName(NUM_BYO_YOMI);
	numByoYomiComboBox.addItemListener(this);
	numByoYomiComboBox.setToolTipText("Players are allowed this many byo-yomi" + 
		" periods. If a player's time runs out and they have no byo-yomi" +
		" periods remaining, that player loses the game.");
	JComboBox<String> byoYomiLengthComboBox = new JComboBox<>(
		byoYomiLengthLabel());
	byoYomiLengthComboBox.setSelectedIndex(3);
	byoYomiLengthComboBox.setName(BYO_YOMI_LENGTH);
	byoYomiLengthComboBox.addItemListener(this);
	byoYomiLengthComboBox.setToolTipText("Once the main timer runs out," +
		" players have this much time per move, plus the specified number" +
		" of byo-yomi periods that they may use as needed.");

	JPanel timerRadioButtonPanel = new JPanel();
	timerRadioButtonPanel.add(isTimedLabel);
	timerRadioButtonPanel.add(timerOnButton);
	timerRadioButtonPanel.add(timerOffButton);
	
	timerComboBoxPanel = new JPanel();
	timerComboBoxPanel.setLayout(new GridLayout(2, 3, 5, 5));
	timerComboBoxPanel.add(mainTimerLabel);
	timerComboBoxPanel.add(numByoYomiLabel);
	timerComboBoxPanel.add(byoYomiLengthLabel);
	timerComboBoxPanel.add(mainTimerComboBox);
	timerComboBoxPanel.add(numByoYomiComboBox);
	timerComboBoxPanel.add(byoYomiLengthComboBox);
	
	setEnabledAllComponents(timerComboBoxPanel, false);
	
	timerPanel.add(timerRadioButtonPanel);
	timerPanel.add(timerComboBoxPanel);
	return timerPanel;
    }
    
    /**
     * This method creates a JPanel to start the game.
     * 
     * @param numPlayers The number of human Players in the game
     * @return The JPanel used to start the game
     */
    private JPanel createStartButtonPanel() {
	JPanel buttonPanel = new JPanel();
	JButton button = new JButton(START_GAME);
	button.setActionCommand(START_GAME);
	button.addActionListener(this);
	buttonPanel.add(button);
	return buttonPanel;
    }
    
    /**
     * This method returns a new JPanel with a BoxLayout.
     * 
     * @param vertical True if the components in the panel are to be arranged
     *                 vertically and false if they are to be arranged
     *                 horizontally
     * @return The JPanel that was created
     */
    private JPanel createBoxLayoutPanel(boolean vertical) {
	JPanel panel = new JPanel();
	int axis = 0;
	if (vertical) {
	    axis = BoxLayout.Y_AXIS;
	} else {
	    axis = BoxLayout.X_AXIS;
	}
	panel.setLayout(new BoxLayout(panel, axis));
	return panel;
    }
    
    /**
     * This method returns a new JPanel with a BoxLayout.
     * 
     * @return The JPanel that was created
     */
    private JPanel createBoxLayoutPanel() {
	return createBoxLayoutPanel(true);
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
    
    /**
     * This method creates a radio button with the given text and mnemonic
     * 
     * @param text     The text that will be displayed on the radio button
     * @param mnemonic The key code which represents the mnemonic for the button
     * @return The JRadioButton that was created
     */
    private JRadioButton createRadioButton(String text, int mnemonic) {
	JRadioButton button = new JRadioButton(text);
	button.setMnemonic(mnemonic);
	button.setActionCommand(text);
	button.addActionListener(this);
	return button;
    }
    
    /**
     * This method is called when the "View Replay" button in the EndGameMenu is
     * pressed in order to allow a replay to be viewed without returning to the
     * main menu.
     * 
     * @param replayFile The file containing the replay
     */
    public void viewReplay(File replayFile) {
	this.replayFile = replayFile;
	replayMode = true;
	practiceProblem = false;
    }

    /**
     * This method determines what happens when a JButton or JRadioButton is
     * pressed.
     * 
     * @param e The ActionEvent to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();

	// select the number of players and enable or disable the player 2 name
	// selector when the num players chooser buttons are pressed
	if (command.equals("1")) {
	    onePlayerGame = true;
	    setEnabledAllComponents(player2NamePanel, false);
	} else if (command.equals("2")) {
	    onePlayerGame = false;
	    setEnabledAllComponents(player2NamePanel, true);
	}
	// enable or disable the timer when timer chooser buttons are pressed
	else if (command.equals(TIMER_ON)) {
	    timed = true;
	    setEnabledAllComponents(timerComboBoxPanel, true);
	} else if (command.equals(TIMER_OFF)) {
	    timed = false;
	    setEnabledAllComponents(timerComboBoxPanel, false);
	}
	// choose a file when select file button is pressed
	else if (command.equals(SELECT_FILE)) {
	    int returnVal = fileChooser.showOpenDialog(replayCard);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    replayFile = fileChooser.getSelectedFile();
		setEnabledAllComponents(replayCard, true);
		((JTextField) selectedFilePanel.getComponent(0))
			.setText("You selected the following file:");
		((JTextField) selectedFilePanel.getComponent(1))
			.setText(replayFile.getName());
		}
	}
	// select replay mode if the replay button is pressed
	else if (command.equals(REPLAY)) {
	    replayMode = true;
	    practiceProblem = false;
	} 
	// select practice problem mode if the practice button is pressed
	else if (command.equals(PRACTICE)) {
	    replayMode = false;
	    practiceProblem = true;
	}
	// start the replay or practice problem when the start button is pressed
	else if (command.equals(START_REPLAY)) {
	    readyToPlay = true;
	    frame.dispose();
	    gui.initializeGame();	    
	} 
	// start the game if start game button is pressed
	else if (command.equals(START_GAME)) {
	    // verify that player 1 and player 2 have distinct names
	    boolean duplicateNames = false;
	    if (onePlayerGame) {
		if (player1Name.equals("Computer")) {
		    duplicateNames = true;
		}
	    } else if (player1Name.equals("")) {
		if (player2Name.equals("Player 1")) {
		    duplicateNames = true;
		}
	    } else if (player2Name.equals("")) {
		if (player1Name.equals("Player 2")) {
		    duplicateNames = true;
		}
	    } else if (player1Name.equals(player2Name)) {
		duplicateNames = true;
	    }
	    
	    // start the game if the names are distinct
	    if (duplicateNames) {
		JOptionPane.showMessageDialog(frame,
			"Players must have distinct names.");
	    } else {
		replayMode = false;
		practiceProblem = false;
		readyToPlay = true;
		frame.dispose();
		gui.initializeGame();
	    }
	}
    }

    /**
     * This method determines what happens when an option in a JComboBox is
     * selected.
     * 
     * @param e The ItemEvent to be processed
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
	String name = ((Component) e.getSource()).getName();
	String item = (String) e.getItem();
	
	// choose the appropriate card when an item in the game mode combo box
	// is selected
	if (name.equals(SELECT_GAME_MODE)) {
	    CardLayout cl = (CardLayout) (cards.getLayout());
	    cl.show(cards, item);
	} 
	// adjuct the color
	else if (name.equals(COLOR)) {
	    player1Color = item;
	} 
	// adjust the main timer length
	else if (name.equals(MAIN_TIMER)) {
	    String[] words = item.split(" ");
	    mainTime = (60 * Integer.parseInt(words[0])) +
		    Integer.parseInt(words[2]);
	}
	// adjust number of byo-yomi periods
	else if (name.equals(NUM_BYO_YOMI)) {
	    numByoYomiPeriods = Integer.parseInt(item);
	}
	// adjust length of byo-yomi periods
	else if (name.equals(BYO_YOMI_LENGTH)) {
	    String[] words = item.split(" ");
	    byoYomiLength = (60 * Integer.parseInt(words[0])) +
		    Integer.parseInt(words[2]);
	}
    }

    /**
     * This method determines what happens when a JSlider is adjusted.
     * 
     * @param e The ChangeEvent to be processed
     */
    @Override
    public void stateChanged(ChangeEvent e) {
	String name = ((Component) e.getSource()).getName();
	JSlider source = ((JSlider) e.getSource());
	int value = source.getValue();
	if (!source.getValueIsAdjusting()) {
	    // set the board size
	    if (name.equals(BOARD_SIZE)) {
		numRows = value;
	    } 
	    // set the handicap
	    else if (name.equals(HANDICAP)) {
		handicap = value;

		if (handicap == 0) {
		    setEnabledAllComponents(komiPanel, true);
		} else {
		    setEnabledAllComponents(komiPanel, false);
		}
	    } 
	    // set the komi
	    else if (name.equals(KOMI)) {
		komi = value + 0.5;
	    }
	}
    }
}
