import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeFrame extends JFrame {
    private TicTacToeButton[][] buttons;
    private JButton quitButton;
    private String currentPlayer;
    private int moveCount;

    public TicTacToeFrame() {
        setTitle("Silas's Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeGame();
        createButtons();
        addQuitButton();

        setMinimumSize(new Dimension(300, 350));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeGame() {
        currentPlayer = "X";
        moveCount = 0;
        buttons = new TicTacToeButton[3][3];
    }

    private void createButtons() {
        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new TicTacToeButton(row, col);
                buttons[row][col].setPreferredSize(new Dimension(80, 80));
                buttons[row][col].addActionListener(new TicTacToeListener());
                boardPanel.add(buttons[row][col]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }

    private void addQuitButton() {
        quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(quitButton);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private class TicTacToeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            TicTacToeButton button = (TicTacToeButton) e.getSource();
            if (button.getText().equals("")) {
                button.setText(currentPlayer);
                moveCount++;

                if (checkForWin()) {
                    JOptionPane.showMessageDialog(TicTacToeFrame.this,
                            "Player " + currentPlayer + " wins!");
                    if (playAgain()) {
                        resetGame();
                    } else {
                        System.exit(0);
                    }
                } else if (isTie()) {
                    JOptionPane.showMessageDialog(TicTacToeFrame.this,
                            "It's a tie!");
                    if (playAgain()) {
                        resetGame();
                    } else {
                        System.exit(0);
                    }
                } else {
                    currentPlayer = (currentPlayer.equals("X")) ? "O" : "X";
                }
            } else {
                JOptionPane.showMessageDialog(TicTacToeFrame.this,
                        "Invalid move. Try again.");
            }
        }
    }

    private boolean checkForWin() {
        return checkRows() || checkColumns() || checkDiagonals();
    }

    private boolean checkRows() {
        for (int row = 0; row < 3; row++) {
            if (checkLine(buttons[row][0], buttons[row][1], buttons[row][2])) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumns() {
        for (int col = 0; col < 3; col++) {
            if (checkLine(buttons[0][col], buttons[1][col], buttons[2][col])) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals() {
        return checkLine(buttons[0][0], buttons[1][1], buttons[2][2]) ||
                checkLine(buttons[0][2], buttons[1][1], buttons[2][0]);
    }

    private boolean checkLine(TicTacToeButton b1, TicTacToeButton b2, TicTacToeButton b3) {
        return !b1.getText().equals("") &&
                b1.getText().equals(b2.getText()) &&
                b2.getText().equals(b3.getText());
    }

    private boolean isTie() {
        if (moveCount < 5) return false; // Can't be a tie before 5 moves

        // Check for potential wins
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    // Try both X and O in this empty spot
                    if (wouldWin("X", row, col) || wouldWin("O", row, col)) {
                        return false; // A win is still possible
                    }
                }
            }
        }

        // If we get here, no win is possible despite empty spaces
        return true;
    }

    private boolean wouldWin(String player, int row, int col) {
        // Temporarily place the player's symbol
        buttons[row][col].setText(player);

        // Check if this creates a win
        boolean win = checkForWin();

        // Undo the move
        buttons[row][col].setText("");

        return win;
    }

    private void resetGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
            }
        }
        currentPlayer = "X";
        moveCount = 0;
    }

    private boolean playAgain() {
        int response = JOptionPane.showConfirmDialog(this,
                "Do you want to play Silas's Tic Tac Toe again?", "Play Again?",
                JOptionPane.YES_NO_OPTION);
        return response == JOptionPane.YES_OPTION;
    }
}