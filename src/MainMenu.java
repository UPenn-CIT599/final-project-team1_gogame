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
public class MainMenu implements ActionListener {
    private UserInterface gui;
    private JFrame frame;
    private String player1Name = "";
    private String player1Name1 = "";
    private String player1Name2 = "";
    private String player2Name = "";
    private boolean replayMode = true;
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
    private JFileChooser fileChooser;
    private File replayFile = null;
    private boolean practiceProblem = false;
    private boolean readyToPlay = false;
    
    private static String SELECT_FILE = "Select File";
    private static String REPLAY = "Replay";
    private static String PRACTICE = "Practice Problem";
    private static String START_GAME = "Start Game";
    private static String START_REPLAY = "Start";

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
	String select = "Select game mode";
	String onePlayer = "1 Player Game";
	String twoPlayer = "2 Player Game";
	String openFile = "Open File";
	JPanel comboBoxPane = createBoxLayoutPanel();
	JTextField welcome = createTextField("Welcome to Go!");
	welcome.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
	String[] comboBoxItems = { select, onePlayer, twoPlayer, openFile };
	JComboBox<String> selectGameMode = new JComboBox<>(comboBoxItems);
	selectGameMode.setEditable(false);
	selectGameMode.setMaximumSize(new Dimension(135, 50));
	comboBoxPane.add(welcome);
	comboBoxPane.add(selectGameMode);
	
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

	JPanel onePlayerCard = createBoxLayoutPanel();
	onePlayerCard.add(namePanel1);
	onePlayerCard.add(colorPanel1);
	onePlayerCard.add(boardSizePanel1);
	onePlayerCard.add(handicapPanel1);
	onePlayerCard.add(komiPanel1);
	onePlayerCard.add(buttonPanel1);

	JPanel twoPlayerCard = createBoxLayoutPanel();
	twoPlayerCard.add(namePanel2);
	twoPlayerCard.add(colorPanel2);
	twoPlayerCard.add(namePanel3);
	twoPlayerCard.add(boardSizePanel2);
	twoPlayerCard.add(handicapPanel2);
	twoPlayerCard.add(komiPanel2);
	twoPlayerCard.add(buttonPanel2);

	JPanel replayCard = createBoxLayoutPanel();

	JPanel selectedFilePanel = createBoxLayoutPanel();
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
	JTextField chooseFileLabel = createTextField(
		"Please choose a file.");
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
	
	// The choose file button uses its own ActionListener rather than the
	// one used by the other buttons so that it can more easily reference
	// the components it needs to enable.
	chooseFileButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		int returnVal = fileChooser.showOpenDialog(replayCard);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    replayFile = fileChooser.getSelectedFile();
		    isPracticeProblemLabel.setEnabled(true);
		    replayButton.setEnabled(true);
		    practiceProblemButton.setEnabled(true);
		    startReplayButton.setEnabled(true);
		    selectedFileLabel
			    .setText("You selected the following file:");
		    selectedFileName.setText(replayFile.getName());
		}
	    }

	});
	chooseFilePanel.add(chooseFileLabel);
	chooseFilePanel.add(chooseFileButton);
	chooseFilePanel
		.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

	replayCard.add(chooseFilePanel);
	replayCard.add(selectedFilePanel);
	replayCard.add(isPracticeProblemPanel);
	replayCard.add(startReplayPanel);

	JPanel cards = new JPanel(new CardLayout());
	cards.add(selectCard, select);
	cards.add(onePlayerCard, onePlayer);
	cards.add(twoPlayerCard, twoPlayer);
	cards.add(replayCard, openFile);

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
	JPanel boardSizePanel = createBoxLayoutPanel();
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
	
	JPanel komiPanel = createBoxLayoutPanel();
	JTextField komiChooserLabel = new JTextField(
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
	JPanel handicapPanel = createBoxLayoutPanel();
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
	return handicapPanel;
    }
    
    /**
     * This method creates a JPanel to start the game.
     * 
     * @param numPlayers The number of human Players in the game
     * @return The JPanel used to start the game
     */
    private JPanel createStartButtonPanel(int numPlayers) {
	JPanel buttonPanel = new JPanel();
	JButton button = new JButton(START_GAME);
	button.setActionCommand(numPlayers + START_GAME);
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
    
    
    private JRadioButton createRadioButton(String text, int keyEvent) {
	JRadioButton button = new JRadioButton(text);
	button.setMnemonic(keyEvent);
	button.setActionCommand(text);
	button.addActionListener(this);
	return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();
	if (command.equals(REPLAY)) {
	    replayMode = true;
	    practiceProblem = false;
	} else if (command.equals(PRACTICE)) {
	    replayMode = false;
	    practiceProblem = true;
	} else if (command.equals(START_REPLAY)) {
	    readyToPlay = true;
	    frame.dispose();
	    gui.initializeGame();
	    
	} else if (command.contains(START_GAME)) {
	    int numPlayers = Integer.parseInt(command.substring(0, 1));
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
	    practiceProblem = false;
	    readyToPlay = true;
	    frame.dispose();
	    gui.initializeGame();
	}
    }
}
