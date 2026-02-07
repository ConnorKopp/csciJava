package csciJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class goframe {
    static String[][] board =  new String[9][9];
    private static boolean blackTurn = true;
    private static final int GAP = 30;
    private static final int MARGIN = 10;


    static void printBoard(String[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(board[i][j] == null){
                    System.out.print("+ ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }


    static boolean placeStone(int row, int col, String stone) {
        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            return false;
        }
        if (board[row][col] != null) {
            return false;
        }
        board[row][col] = stone;
        return true;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Go");
        frame.setSize(GAP * board.length, (GAP+2) * board.length);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.getContentPane().setBackground();

        // Interaction logic
        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Adjust for window header (insets)
                int x = Math.round((float) (e.getX() - MARGIN) / GAP);
                int y = Math.round((float) (e.getY() - MARGIN - frame.getInsets().top) / GAP);

                if (x >= 0 && x < board.length && y >= 0 && y < board.length && board[x][y] == null) {
                    board[x][y] = blackTurn ? "@" : "O";
                    blackTurn = !blackTurn;
                    frame.repaint();
                }
            }
        });

        // Drawing logic inside an anonymous override
        frame.add(new Component() {
            public void paint(Graphics g) {
                // Draw Board Lines
                g.setColor(Color.BLACK);
                for (int i = 0; i < board.length; i++) {
                    g.drawLine(MARGIN, MARGIN + i * GAP, MARGIN + (board.length-1) * GAP, MARGIN + i * GAP);
                    g.drawLine(MARGIN + i * GAP, MARGIN, MARGIN + i * GAP, MARGIN + (board.length-1) * GAP);
                }

                // Draw Stones
                for (int x = 0; x < board.length; x++) {
                    for (int y = 0; y < board.length; y++) {
                        if (board[x][y] != null) {
                            g.setColor(board[x][y] == "@" ? Color.BLACK : Color.WHITE);
                            g.fillOval(MARGIN + x * GAP - 12, MARGIN + y * GAP - 12, 24, 24);
                            g.setColor(Color.BLACK);
                            g.drawOval(MARGIN + x * GAP - 12, MARGIN + y * GAP - 12, 24, 24);
                        }
                    }
                }
            }
        });

        frame.setVisible(true);
    }
}