import java.awt.*;
import java.awt.event.*;
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
//    private UserInterface gui;
    private JFrame frame;
    private String player1Name = "";
    private String player1Name1 = "";
    private String player1Name2 = "";
    private String player2Name = "";
    private boolean demoMode = false;
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
    private boolean readyToPlay = false;

    public String getPlayer1Name() {
	return player1Name;
    }

    public String getPlayer2Name() {
	return player2Name;
    }

    public boolean isDemoMode() {
	return demoMode;
    }

    public boolean isOnePlayerGame() {
	return onePlayerGame;
    }

    public int getHandicap() {
	return handicap;
    }

    public double getKomi() {
	return komi;
    }
    
    public int getNumRows() {
	return numRows;
    }

    public String getPlayer1Color() {
	return player1Color;
    }

    public boolean isReadyToPlay() {
	return readyToPlay;
    }

    /**
     * This method creates a MainMenu associated with the given UserInterface.
     * 
     * @param gui The UserInterface associated with this MainMenu
     */
    public MainMenu(UserInterface gui) {
//	this.gui = gui;
	
//	player1Name = "";
//	player1Name1 = "";
//	player1Name2 = "";
//	player2Name = "";
//	demoMode = false;
//	onePlayerGame = false;
//	handicap = 0;
//	handicap1 = 0;
//	handicap2 = 0;
//	numRows = 19;
//	numRows1 = 19;
//	numRows2 = 19;
//	player1Color = "Black";
//	player1Color1 = "Black";
//	player1Color2 = "Black";
//	readyToPlay = false;

	/*
	 * A portion of the below code is based on the following:
	 * 
	 * Title: CardLayoutDemo 
	 * Author: Oracle 
	 * Date: 2008
	 * Availability: 
	 * https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/CardLayoutDemoProject/src/layout/CardLayoutDemo.java
	 */
	frame = new JFrame("Go - Main Menu");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	String select = "Select game mode";
	String onePlayer = "1 Player Game";
	String twoPlayer = "2 Player Game";
	String demo = "Demo Mode";
	JPanel comboBoxPane = new JPanel();
	JTextField welcome = new JTextField("Welcome to Go!");
	welcome.setEditable(false);
	welcome.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	welcome.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
	String[] comboBoxItems = { select, onePlayer, twoPlayer, demo };
	JComboBox<String> selectGameMode = new JComboBox<>(comboBoxItems);
	selectGameMode.setEditable(false);
	selectGameMode.setMaximumSize(new Dimension(135, 50));
	comboBoxPane.add(welcome);
	comboBoxPane.add(selectGameMode);
	comboBoxPane.setLayout(new BoxLayout(comboBoxPane, BoxLayout.Y_AXIS));

	JPanel namePanel1 = new JPanel();
	JTextField choosePlayer1Name1Label = new JTextField(
		"Player 1, please enter your name: ");
	choosePlayer1Name1Label.setEditable(false);
	JTextField choosePlayer1Name1 = createFilteredField();
	choosePlayer1Name1.getDocument()
		.addDocumentListener(new DocumentListener() {

		    @Override
		    public void changedUpdate(DocumentEvent e) {
			player1Name1 = choosePlayer1Name1.getText();
		    }

		    @Override
		    public void insertUpdate(DocumentEvent e) {
			player1Name1 = choosePlayer1Name1.getText();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
			player1Name1 = choosePlayer1Name1.getText();
		    }

		});
	namePanel1.add(choosePlayer1Name1Label);
	namePanel1.add(choosePlayer1Name1);

	JPanel namePanel2 = new JPanel();
	JTextField choosePlayer1Name2Label = new JTextField(
		"Player 1, please enter your name: ");
	choosePlayer1Name2Label.setEditable(false);
	JTextField choosePlayer1Name2 = createFilteredField();
	choosePlayer1Name2.getDocument()
		.addDocumentListener(new DocumentListener() {

		    @Override
		    public void changedUpdate(DocumentEvent e) {
			player1Name2 = choosePlayer1Name2.getText();
		    }

		    @Override
		    public void insertUpdate(DocumentEvent e) {
			player1Name2 = choosePlayer1Name2.getText();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
			player1Name2 = choosePlayer1Name2.getText();
		    }

		});
	namePanel2.add(choosePlayer1Name2Label);
	namePanel2.add(choosePlayer1Name2);

	JPanel namePanel3 = new JPanel();
	JTextField choosePlayer2NameLabel = new JTextField(
		"Player 2, please enter your name: ");
	choosePlayer2NameLabel.setEditable(false);
	JTextField choosePlayer2Name = createFilteredField();
	choosePlayer2Name.getDocument()
		.addDocumentListener(new DocumentListener() {

		    @Override
		    public void changedUpdate(DocumentEvent e) {
			player2Name = choosePlayer2Name.getText();
		    }

		    @Override
		    public void insertUpdate(DocumentEvent e) {
			player2Name = choosePlayer2Name.getText();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
			player2Name = choosePlayer2Name.getText();
		    }

		});
	namePanel3.add(choosePlayer2NameLabel);
	namePanel3.add(choosePlayer2Name);

	JPanel colorPanel1 = new JPanel();
	String[] colorChoices = { "Black", "White", "Random" };
	JTextField colorChooser1Label = new JTextField(
		"Player 1, please choose a color: ");
	colorChooser1Label.setEditable(false);
	JComboBox<String> colorChooser1 = new JComboBox<>(colorChoices);
	colorChooser1.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent evt) {
		player1Color1 = (String) evt.getItem();
	    }

	});
	colorPanel1.add(colorChooser1Label);
	colorPanel1.add(colorChooser1);

	JPanel colorPanel2 = new JPanel();
	JTextField colorChooser2Label = new JTextField(
		"Player 1, please choose a color: ");
	colorChooser2Label.setEditable(false);
	JComboBox<String> colorChooser2 = new JComboBox<>(colorChoices);
	colorChooser2.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent evt) {
		player1Color2 = (String) evt.getItem();
	    }

	});
	colorPanel2.add(colorChooser2Label);
	colorPanel2.add(colorChooser2);

	/*
	 * A portion of the below code is based on the following:
	 * 
	 * Title: SliderDemo2 
	 * Author: Oracle 
	 * Date: 2008
	 * Availability: 
	 * https://docs.oracle.com/javase/tutorial/uiswing/examples/components/SliderDemo2Project/src/components/SliderDemo2.java
	 */
	JPanel boardSizePanel1 = new JPanel();
	boardSizePanel1
		.setLayout(new BoxLayout(boardSizePanel1, BoxLayout.Y_AXIS));
	JTextField boardSizeChooser1Label = new JTextField(
		"Please choose a board size:");
	boardSizeChooser1Label.setEditable(false);
	boardSizeChooser1Label
		.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	JSlider boardSizeChooser1 = new JSlider(JSlider.HORIZONTAL, 5, 19, 19);
	boardSizeChooser1.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent arg0) {
		if (!boardSizeChooser1.getValueIsAdjusting()) {
		    numRows1 = (int) boardSizeChooser1.getValue();
		}
	    }

	});
	boardSizeChooser1.setMajorTickSpacing(1);
	boardSizeChooser1.setPaintTicks(true);
	boardSizeChooser1.setPaintLabels(true);
	boardSizePanel1.add(boardSizeChooser1Label);
	boardSizePanel1.add(boardSizeChooser1);

	JPanel boardSizePanel2 = new JPanel();
	boardSizePanel2
		.setLayout(new BoxLayout(boardSizePanel2, BoxLayout.Y_AXIS));
	JTextField boardSizeChooser2Label = new JTextField(
		"Please choose a board size:");
	boardSizeChooser2Label.setEditable(false);
	boardSizeChooser2Label
		.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	JSlider boardSizeChooser2 = new JSlider(JSlider.HORIZONTAL, 5, 19, 19);
	boardSizeChooser2.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent arg0) {
		if (!boardSizeChooser2.getValueIsAdjusting()) {
		    numRows2 = (int) boardSizeChooser2.getValue();
		}
	    }

	});
	boardSizeChooser2.setMajorTickSpacing(1);
	boardSizeChooser2.setPaintTicks(true);
	boardSizeChooser2.setPaintLabels(true);
	boardSizePanel2.add(boardSizeChooser2Label);
	boardSizePanel2.add(boardSizeChooser2);

	double maxKomi = 8.5;
	DecimalFormat komiDecimalFormat = new DecimalFormat("#.#");
	
	JPanel komiPanel1 = new JPanel();
	komiPanel1
		.setLayout(new BoxLayout(komiPanel1, BoxLayout.Y_AXIS));
	JTextField komiChooser1Label = new JTextField(
		"If handicap is 0, please choose a komi:");
	komiChooser1Label.setEditable(false);
	komiChooser1Label
		.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	JSlider komiChooser1 = new JSlider(JSlider.HORIZONTAL, 0, (int) maxKomi, 6);
	
	// JSlider uses integers, so it must be relabeled and 0.5 must be added
	// to its values to correct for this
	Hashtable<Integer, JLabel> komiLabelTable1 = new Hashtable<>();
	for (int i = 0; i < maxKomi; i++) {
	    komiLabelTable1.put(i, new JLabel(komiDecimalFormat.format(i + 0.5)));
	}
	komiChooser1.setLabelTable(komiLabelTable1);	
	komiChooser1.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent arg0) {
		if (!komiChooser1.getValueIsAdjusting()) {
		    komi1 = komiChooser1.getValue() + 0.5;
		}
	    }

	});
	komiChooser1.setMajorTickSpacing(1);
	komiChooser1.setPaintTicks(true);
	komiChooser1.setPaintLabels(true);
	komiPanel1.add(komiChooser1Label);
	komiPanel1.add(komiChooser1);

	JPanel komiPanel2 = new JPanel();
	komiPanel2
		.setLayout(new BoxLayout(komiPanel2, BoxLayout.Y_AXIS));
	JTextField komiChooser2Label = new JTextField(
		"If handicap is 0, please choose a komi:");
	komiChooser2Label.setEditable(false);
	komiChooser2Label
		.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	JSlider komiChooser2 = new JSlider(JSlider.HORIZONTAL, 0, (int) maxKomi, 6);
	
	// JSlider uses integers, so it must be relabeled and 0.5 must be added
	// to its values to correct for this
	Hashtable<Integer, JLabel> komiLabelTable2 = new Hashtable<>();
	for (int i = 0; i < maxKomi; i++) {
	    komiLabelTable2.put(i, new JLabel(komiDecimalFormat.format(i + 0.5)));
	}
	komiChooser2.setLabelTable(komiLabelTable2);	
	komiChooser2.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent arg0) {
		if (!komiChooser2.getValueIsAdjusting()) {
		    komi2 = komiChooser2.getValue() + 0.5;
		}
	    }

	});
	komiChooser2.setMajorTickSpacing(1);
	komiChooser2.setPaintTicks(true);
	komiChooser2.setPaintLabels(true);
	komiPanel2.add(komiChooser2Label);
	komiPanel2.add(komiChooser2);

	JPanel handicapPanel1 = new JPanel();
	handicapPanel1
		.setLayout(new BoxLayout(handicapPanel1, BoxLayout.Y_AXIS));
	JTextField handicapChooser1Label = new JTextField(
		"Please choose a handicap:");
	handicapChooser1Label.setEditable(false);
	handicapChooser1Label
		.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	JSlider handicapChooser1 = new JSlider(JSlider.HORIZONTAL, 0, 9, 0);
	handicapChooser1.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent arg0) {
		if (!handicapChooser1.getValueIsAdjusting()) {
		    handicap1 = (int) handicapChooser1.getValue();
		    if (handicap1 == 0) {
			komiChooser1Label.setEnabled(true);
			komiChooser1.setEnabled(true);
		    } else {
			komiChooser1.setValue(0);
			komiChooser1Label.setEnabled(false);
			komiChooser1.setEnabled(false);
		    }
		}
	    }

	});
	handicapChooser1.setMajorTickSpacing(1);
	handicapChooser1.setPaintTicks(true);
	handicapChooser1.setPaintLabels(true);
	handicapPanel1.add(handicapChooser1Label);
	handicapPanel1.add(handicapChooser1);

	JPanel handicapPanel2 = new JPanel();
	handicapPanel2
		.setLayout(new BoxLayout(handicapPanel2, BoxLayout.Y_AXIS));
	JTextField handicapChooser2Label = new JTextField(
		"Please choose a handicap:");
	handicapChooser2Label.setEditable(false);
	handicapChooser2Label
		.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
	JSlider handicapChooser2 = new JSlider(JSlider.HORIZONTAL, 0, 9, 0);
	handicapChooser2.addChangeListener(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent arg0) {
		if (!handicapChooser2.getValueIsAdjusting()) {
		    handicap2 = (int) handicapChooser2.getValue();
		    if (handicap2 == 0) {
			komiChooser2Label.setEnabled(true);
			komiChooser2.setEnabled(true);
		    } else {
			komiChooser2.setValue(0);
			komiChooser2Label.setEnabled(false);
			komiChooser2.setEnabled(false);
		    }
		}
	    }

	});
	handicapChooser2.setMajorTickSpacing(1);
	handicapChooser2.setPaintTicks(true);
	handicapChooser2.setPaintLabels(true);
	handicapPanel2.add(handicapChooser2Label);
	handicapPanel2.add(handicapChooser2);
	
	JPanel buttonPanel1 = new JPanel();
	JButton button1 = new JButton("Start Game");
	button1.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		player1Name = player1Name1;
		player1Color = player1Color1;
		numRows = numRows1;
		handicap = handicap1;
		komi = komi1;
		demoMode = false;
		onePlayerGame = true;
		readyToPlay = true;
//		frame.setVisible(false);
		frame.dispose();
		gui.initializeGame();
	    }

	});
	buttonPanel1.add(button1);

	JPanel buttonPanel2 = new JPanel();
	JButton button2 = new JButton("Start Game");
	button2.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		player1Name = player1Name2;
		player1Color = player1Color2;
		numRows = numRows2;
		handicap = handicap2;
		komi = komi2;
		demoMode = false;
		onePlayerGame = false;
		readyToPlay = true;
//		frame.setVisible(false);
		frame.dispose();
		gui.initializeGame();
	    }

	});
	buttonPanel2.add(button2);

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

	JPanel demoCard = new JPanel();

	JPanel cards = new JPanel(new CardLayout());
	cards.add(selectCard, select);
	cards.add(onePlayerCard, onePlayer);
	cards.add(twoPlayerCard, twoPlayer);
	cards.add(demoCard, demo);

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
}
