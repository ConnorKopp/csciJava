import java.util.Scanner;

public class Go {
    //static String[][] board =  new String[9][9];
    /*
        {null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null ,null ,null},
        {null ,null ,null ,null ,null ,null ,null ,null ,null}
    */
    static String[][] board = {
        {null, null, null, null, null, null, null, null, null},
        {null, null, "@", "@", null, null, null, null, null},
        {null, "@", "O", "O", "@", null, null, null, null},
        {null, null, "@", "O", "O", "@", null, null, null},
        {null, "@", "O", "O", "@", null, null, null, null},
        {null, null, "@", "O", "@", null, null, null, null},
        {null, null, "@", "O", "O", null, null, null, null},
        {null, "@", "O", "O", "@", null, null ,null ,null},
        {null ,null ,"@" ,"@" ,null ,null ,null ,null ,null}
    };

    static boolean[][] lives = new boolean[9][9];
    static boolean [][] beenChecked = new boolean[9][9];
    static int blackScore = 0;
    static int whiteScore = 0;

    static boolean touchingBlack = false;
    static boolean touchingWhite = false;

    static final String BLACK = "@";
    static final String WHITE = "O";


    static boolean checkLiberty(String[][] board, int row, int col, String color) {
        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            return false;
        }

        if (beenChecked[row][col]) {
            return false;
        }

        // empty space = liberty
        if (board[row][col] == null) {
            return true;
        }

        if (!board[row][col].equals(color)) {
            return false;
        }

        beenChecked[row][col] = true;

        // check 4 directions
        if (checkLiberty(board, row + 1, col, color)) return true;
        if (checkLiberty(board, row - 1, col, color)) return true;
        if (checkLiberty(board, row, col + 1, color)) return true;
        if (checkLiberty(board, row, col - 1, color)) return true;

        return false;
    }
    
    static void removeGroup(int row, int col, String color) {

        if (row < 0 || row >= 9 || col < 0 || col >= 9) return;

        if (board[row][col] == null) return;

        if (!board[row][col].equals(color)) return;

        board[row][col] = null;
        if (color.equals(BLACK)) {
            whiteScore++;
        } else {
            blackScore++;
        }

        removeGroup(row + 1, col, color);
        removeGroup(row - 1, col, color);
        removeGroup(row, col + 1, color);
        removeGroup(row, col - 1, color);
    }

    static void resetBeenChecked() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                beenChecked[i][j] = false;
            }
        }
    }

    static int checkTerritory(int row, int col) {
        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            return 0;
        }

        if (beenChecked[row][col]) {
            return 0;
        }

        if (board[row][col] != null) {
            if(board[row][col].equals(BLACK))touchingBlack = true;
            if(board[row][col].equals(WHITE))touchingWhite = true;
            return 0;
        }

        beenChecked[row][col] = true;

        int size=1;
        
        size+=checkTerritory(row + 1, col);
        size+=checkTerritory(row - 1, col);
        size+=checkTerritory(row, col + 1);
        size+=checkTerritory(row, col - 1);

        return size;
    }

    static void calculateTerritory() {
        resetBeenChecked();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == null && !beenChecked[i][j]) {
                    touchingBlack = false;
                    touchingWhite = false;
                    int territorySize = checkTerritory(i, j);
                    if (touchingBlack && !touchingWhite) {
                        blackScore += territorySize;
                    } else if (touchingWhite && !touchingBlack) {
                        whiteScore += territorySize;
                    }
                }
            }
        }
    }

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
        System.out.println(blackScore + " - " + whiteScore);
    }

    static boolean placeStone(int row, int col, String stone) {
        if (row < 0 || row >= 9 || col < 0 || col >= 9) return false;
        if (board[row][col] != null) return false;

        board[row][col] = stone;
        String enemy;

        if (stone.equals(BLACK)){
            enemy = WHITE;
        } else {
            enemy = BLACK;
        }
        
        resetBeenChecked();
        if (row+1 < 9 && board[row+1][col] != null && board[row+1][col].equals(enemy)) {
            resetBeenChecked();
            if (!checkLiberty(board, row + 1, col, enemy)) {
                removeGroup(row + 1, col, enemy);
            }
        }

        if (row-1 >= 0 && board[row-1][col] != null && board[row-1][col].equals(enemy)) {
            resetBeenChecked();
            if (!checkLiberty(board, row - 1, col, enemy)) {
                removeGroup(row - 1, col, enemy);
            }
        }

        if (col+1 < 9 && board[row][col+1] != null && board[row][col+1].equals(enemy)) {
            resetBeenChecked();
            if (!checkLiberty(board, row, col + 1, enemy)) {
                removeGroup(row, col + 1, enemy);
            }
        }

        if (col-1 >= 0 && board[row][col-1] != null && board[row][col-1].equals(enemy)) {
            resetBeenChecked();
            if (!checkLiberty(board, row, col - 1, enemy)) {
                removeGroup(row, col - 1, enemy);
            }
        }

        // check suicide
        resetBeenChecked();
        if(!checkLiberty(board,row,col,stone)){
            board[row][col] = null;
            return false;
        }

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
            if (row == -1) {
                    calculateTerritory();
                    printBoard(board);
                break;
            }

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