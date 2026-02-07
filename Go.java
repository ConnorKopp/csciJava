package csciJava;


import java.util.Scanner;

public class Go {
    static String[][] board =  new String[9][9];
    static final String BLACK = "@";
    static final String WHITE = "O";


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
        Scanner scn = new Scanner(System.in);
        String currentPlayer = BLACK;

        while (true) {
            printBoard(board);
            if (currentPlayer.equals(BLACK)) {
                System.out.println("Black's turn");
            } else {
                System.out.println("White's turn");
            }
            System.out.print("Enter row (0-8) or -1 to quit: ");

            int row = scn.nextInt();
            if (row == -1) break;

            System.out.print("Enter column (0-8): ");
            int col = scn.nextInt();
            if (!placeStone(row, col, currentPlayer)) {
                System.out.println("Invalid move. Try again.");
                continue; // same player retries
            }

            // switch turns
            currentPlayer = currentPlayer.equals(BLACK) ? WHITE : BLACK;
        }

        scn.close();
        System.out.println("Game ended.");
    }
}