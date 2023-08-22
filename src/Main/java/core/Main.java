package main.java.core;

import main.java.game.GameBoard;
import main.java.game.GameLogicDriver;
import main.java.game.InfoPanel;
import main.java.startingInput.InputTextBox;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static int cardRows = 0;
    private static int cardCols = 0;
    private static volatile boolean validInput = false;

    public static void main(String[] args) {
        JFrame window = new JFrame();

        getUserDimensionsInput(window);

        while (!validInput) Thread.onSpinWait();

        startCardGame(window);
    }

    /**
     * Creates a window that takes user input as to how many rows and columns of cards they want the game to have
     */
    private static void getUserDimensionsInput(JFrame window) {
        resetWindowSettings(window);

        // Components contained in the panel that goes in the window
        JLabel label1 = new JLabel("<html>Enter number of rows (min. 1, max. 6):</html>");
        JLabel label2 = new JLabel("<html>Enter number of columns (min. 4, max. 14):</html>");
        JLabel errorLabel = new JLabel("<html>note: <em>rows * columns</em> must be positive and even</html>");
        errorLabel.setPreferredSize(new Dimension(300, 40));

        InputTextBox inputBox1 = new InputTextBox();
        InputTextBox inputBox2 = new InputTextBox();

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> checkUserInput(inputBox1, inputBox2, errorLabel));

        // Setting up the window
        window.setPreferredSize(new Dimension(400, 400));

        // Setting up the panel
        JPanel inputPanel = new JPanel(new GridBagLayout());

        // Setting layout constraints for each component then adding them to the panel
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridy = 0;
        c.insets = new Insets(20, 0, 10, 0);
        inputPanel.add(label1, c);

        c.gridy = 1;
        c.insets = new Insets(10, 0, 10, 0);
        inputPanel.add(inputBox1, c);

        c.gridy = 2;
        c.insets = new Insets(20, 0, 10, 0);
        inputPanel.add(label2, c);

        c.gridy = 3;
        c.insets = new Insets(10, 0, 10, 0);
        inputPanel.add(inputBox2, c);

        c.gridy = 4;
        c.insets = new Insets(20, 100, 10,100);
        inputPanel.add(doneButton, c);

        c.gridy = 5;
        c.insets = new Insets(10, 0, 10, 0);
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        inputPanel.add(errorLabel, c);

        // Adding panel to the window and applying window settings
        window.add(inputPanel);

        applyWindowSettings(window);
        window.revalidate();
        window.repaint();
    }

    /**
     * Is called when the user clicks the "Done" button. Converts the user's input into ints and validates those numbers
     * @param textBox1 the text box where user enters the number of rows for the game
     * @param textBox2 the text box where user enters the number of columns for the game
     * @param errorLabel the JLabel that displays any messages relating to erroneous user input
     */
    private static void checkUserInput(InputTextBox textBox1, InputTextBox textBox2, JLabel errorLabel) {
        try {
            cardRows = Integer.parseInt(textBox1.getUserInput());
            cardCols = Integer.parseInt(textBox2.getUserInput());

            if ((cardRows * cardCols) % 2 == 1 && cardRows > 0 && cardCols > 0) {
                System.out.println("Number of cards in rows * columns must be even and both must be positive");
                errorLabel.setText("<html>Error: <em>rows * columns</em> must be positive and even</html>");
            }
            else if (cardRows < 1 || cardRows > 6) {
                System.out.println("Invalid number of cardRows");
                errorLabel.setText("<html>Error: Invalid number of rows of cards</html>");
            }
            else if (cardCols < 4 || cardCols > 14) {
                System.out.println("Invalid number of cardCols");
                errorLabel.setText("<html>Error: Invalid number of columns of cards</html>");
            }
            else {
                validInput = true;
            }
        }
        catch (Exception e) {
            System.out.println("Error reading integer value");
            errorLabel.setText("<html>Error: Cannot have non-integer value in either input boxes</html>");
        }
    }

    /**
     * Creates the window that contains the card matching game
     */
    private static void startCardGame(JFrame window) {
        resetWindowSettings(window);

        // Creates the cardRows by cardCols large grid of GamePanels in one JPanel
        GameBoard GB = new GameBoard(cardRows, cardCols);

        // Creating the match attempts info panel that goes on the bottom of the window
        InfoPanel IP = new InfoPanel(cardCols);

        // Adding in the two main panels to the window
        GridBagConstraints c = new GridBagConstraints();
        window.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.BOTH;

        c.gridy = 0;
        window.add(GB, c);

        c.gridy = 1;
        window.add(IP, c);

        // Adding required attributes to the GameLogicDriver
        GameLogicDriver.setGameBoard(GB);
        GameLogicDriver.setInfoPanel(IP);

        applyWindowSettings(window);
        window.revalidate();
        window.repaint();

        // Starts the game
        GameLogicDriver.startGame();
    }

    /**
     * Applies this game's default window settings to the given JFrame object
     * @param window the JFrame object to apply this game's default settings to
     */
    private static void applyWindowSettings(JFrame window) {
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Memory Game");
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setBackground(Color.WHITE);
    }

    /**
     * Resets the given JFrame's settings
     * @param window the JFrame object to reset the settings of
     */
    private static void resetWindowSettings(JFrame window) {
        window.getContentPane().removeAll();
        window.setPreferredSize(null);
    }
}