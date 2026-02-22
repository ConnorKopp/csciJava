import java.util.ArrayList;
import java.util.Scanner;


public class Rubics {
    
    public static void main(String[] args) {
        String[][] cube =
        {
            {"w", "w", "w"},
            {"w", "w", "w"},
            {"w", "w", "w"},

            {"B", "B", "B"},
            {"B", "B", "B"},
            {"B", "B", "B"},

            {"Y", "Y", "Y"},
            {"Y", "Y", "Y"},
            {"Y", "Y", "Y"},

            {"G", "G", "G"},
            {"G", "G", "G"},
            {"G", "G", "G"},

            {"R", "R", "R"},
            {"R", "R", "R"},
            {"R", "R", "R"},

            {"O", "O", "O"},
            {"O", "O", "O"},
            {"O", "O", "O"},
        };
    ArrayList<String> history = new ArrayList<>();



    boolean testMode = args.length > 0;

    if (testMode) {
        // ===== TEST MODE =====
        for (String move : args) {
            move = move.toUpperCase();

            if (rotateFace(cube, move)) {
                history.add(move);
            }
            // invalid moves are ignored silently in test mode
        }

        printCube(cube);
        return;
    }

    // ===== INTERACTIVE MODE =====
        printCube(cube);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter a move (e.g., U, U', 2U, or solve): ");
            String move = scanner.nextLine();
            if (move.equals("solve")){
                printSolveSequence(history);
            }
            else if (rotateFace(cube, move.toUpperCase())){
                history.add(move.toUpperCase());
                printCube(cube);
            }
        }
    }


    public static void printSolveSequence(ArrayList<String> history) {
        System.out.println("Solve sequence:");

        for (int i = history.size() - 1; i >= 0; i--) {
            System.out.print(reverseMove(history.get(i)) + " ");
        }
        System.out.println("\n");
    }
    public static String reverseMove(String move) {
        if (move.startsWith("2")) return move;   // 180° is its own inverse
        if (move.endsWith("'")) return move.substring(0, 1);
        return move + "'";
    }
    public static void printCube(String[][] cube) {
        for (int face = 0; face < 6; face++) {
            int start = face * 3;

            for (int r = 0; r < 3; r++) {
                System.out.println(
                    cube[start + r][0] + "|" +
                    cube[start + r][1] + "|" +
                    cube[start + r][2]
                );
            }
            System.out.println();
        }
    }
    public static boolean rotateFace(String[][] cube, String move) {
        switch (move) {
            case "U":
                // Rotate the top face (white)
                String temp = cube[0][0];
                cube[0][0] = cube[2][0];
                cube[2][0] = cube[2][2];
                cube[2][2] = cube[0][2];
                cube[0][2] = temp;

                temp = cube[0][1];
                cube[0][1] = cube[1][0];
                cube[1][0] = cube[2][1];
                cube[2][1] = cube[1][2];
                cube[1][2] = temp;

                // Rotate the adjacent edges
                temp = cube[3][0];
                String temp1 = cube[3][1];
                String temp2 = cube[3][2];

                cube[3][0] = cube[15][0];
                cube[3][1] = cube[15][1];
                cube[3][2] = cube[15][2];

                cube[15][0] = cube[9][0];
                cube[15][1] = cube[9][1];
                cube[15][2] = cube[9][2];


                cube[9][0] = cube[12][0];
                cube[9][1] = cube[12][1];
                cube[9][2] = cube[12][2];

                cube[12][0] = temp;
                cube[12][1] = temp1;
                cube[12][2] = temp2;
                
                return true;
            case "U'":
                // Rotate the top face counter-clockwise
                temp = cube[0][0];
                cube[0][0] = cube[0][2];
                cube[0][2] = cube[2][2];
                cube[2][2] = cube[2][0];
                cube[2][0] = temp;

                temp = cube[0][1];
                cube[0][1] = cube[1][2];
                cube[1][2] = cube[2][1];
                cube[2][1] = cube[1][0];
                cube[1][0] = temp;

                // Rotate the adjacent edges counter-clockwise
                temp = cube[3][0];
                temp1 = cube[3][1];
                temp2 = cube[3][2];
                cube[3][0] = cube[12][0];
                cube[3][1] = cube[12][1];
                cube[3][2] = cube[12][2];

                cube[12][0] = cube[9][0];
                cube[12][1] = cube[9][1];
                cube[12][2] = cube[9][2];

                cube[9][0] = cube[15][0];
                cube[9][1] = cube[15][1];
                cube[9][2] = cube[15][2];

                cube[15][0] = temp;
                cube[15][1] = temp1;
                cube[15][2] = temp2;


                return true;
            case "2U":
                // Rotate the top face 180 degrees
                temp = cube[0][0];
                cube[0][0] = cube[2][2];
                cube[2][2] = temp;

                temp = cube[0][1];
                cube[0][1] = cube[2][1];
                cube[2][1] = temp;

                temp = cube[0][2];
                cube[0][2] = cube[2][0];
                cube[2][0] = temp;

                temp = cube[1][0];
                cube[1][0] = cube[1][2];
                cube[1][2] = temp;

                // Rotate the adjacent edges 180 degrees
                temp = cube[3][0];
                temp1 = cube[3][1];
                temp2 = cube[3][2];
                cube[3][0] = cube[9][0];
                cube[3][1] = cube[9][1];
                cube[3][2] = cube[9][2];
                cube[9][0] = temp;
                cube[9][1] = temp1;
                cube[9][2] = temp2;

                temp = cube[12][0];
                temp1 = cube[12][1];
                temp2 = cube[12][2];
                cube[12][0] = cube[15][0];
                cube[12][1] = cube[15][1];
                cube[12][2] = cube[15][2];
                cube[15][0] = temp;
                cube[15][1] = temp1;
                cube[15][2] = temp2;


                return true;
            case "D":
                // Rotate the bottom face (yellow)
                temp = cube[6][0];
                cube[6][0] = cube[8][0];
                cube[8][0] = cube[8][2];
                cube[8][2] = cube[6][2];
                cube[6][2] = temp;

                temp = cube[6][1];
                cube[6][1] = cube[7][0];
                cube[7][0] = cube[8][1];
                cube[8][1] = cube[7][2];
                cube[7][2] = temp;
            
                // Rotate the adjacent edges
                temp = cube[5][0];
                temp1 = cube[5][1];
                temp2 = cube[5][2];

                cube[5][0] = cube[14][0];
                cube[5][1] = cube[14][1];
                cube[5][2] = cube[14][2];

                cube[14][0] = cube[11][0];
                cube[14][1] = cube[11][1];
                cube[14][2] = cube[11][2];

                cube[11][0] = cube[17][0];
                cube[11][1] = cube[17][1];
                cube[11][2] = cube[17][2];

                cube[17][0] = temp;
                cube[17][1] = temp1;
                cube[17][2] = temp2;
                return true;

            case "D'":
                // Rotate the bottom face counter-clockwise
                temp = cube[6][0];
                cube[6][0] = cube[6][2];
                cube[6][2] = cube[8][2];
                cube[8][2] = cube[8][0];
                cube[8][0] = temp;

                temp = cube[6][1];
                cube[6][1] = cube[7][2];
                cube[7][2] = cube[8][1];
                cube[8][1] = cube[7][0];
                cube[7][0] = temp;

                // Rotate the adjacent edges counter-clockwise
                temp = cube[5][0];
                temp1 = cube[5][1];
                temp2 = cube[5][2];

                cube[5][0] = cube[17][0];
                cube[5][1] = cube[17][1];
                cube[5][2] = cube[17][2];

                cube[17][0] = cube[11][0];
                cube[17][1] = cube[11][1];
                cube[17][2] = cube[11][2];

                cube[11][0] = cube[14][0];
                cube[11][1] = cube[14][1];
                cube[11][2] = cube[14][2];

                cube[14][0] = temp;
                cube[14][1] = temp1;
                cube[14][2] = temp2;
                return true;
            case "2D":
                // Rotate the bottom face 180 degrees
                temp = cube[6][0];
                cube[6][0] = cube[8][2];
                cube[8][2] = temp;

                temp = cube[6][1];
                cube[6][1] = cube[8][1];
                cube[8][1] = temp;

                temp = cube[6][2];
                cube[6][2] = cube[8][0];
                cube[8][0] = temp;

                temp = cube[7][0];
                cube[7][0] = cube[7][2];
                cube[7][2] = temp;

                // Rotate the adjacent edges 180 degrees
                temp = cube[5][0];
                temp1 = cube[5][1];
                temp2 = cube[5][2];
                cube[5][0] = cube[11][0];
                cube[5][1] = cube[11][1];
                cube[5][2] = cube[11][2];
                cube[11][0] = temp;
                cube[11][1] = temp1;
                cube[11][2] = temp2;

                temp = cube[14][0];
                temp1 = cube[14][1];
                temp2 = cube[14][2];
                cube[14][0] = cube[17][0];
                cube[14][1] = cube[17][1];
                cube[14][2] = cube[17][2];
                cube[17][0] = temp;
                cube[17][1] = temp1;
                cube[17][2] = temp2;
                return true;
            case "R":
                // Rotate the right face(blue) clockwise
                temp = cube[3][0];
                cube[3][0] = cube[5][0];
                cube[5][0] = cube[5][2];
                cube[5][2] = cube[3][2];
                cube[3][2] = temp;

                temp = cube[3][1];
                cube[3][1] = cube[4][0];
                cube[4][0] = cube[5][1];
                cube[5][1] = cube[4][2];
                cube[4][2] = temp;

                // Rotate the adjacent edges
                temp = cube[0][2];
                temp1 = cube[1][2];
                temp2 = cube[2][2];
                cube[0][2] = cube[12][2];
                cube[1][2] = cube[13][2];
                cube[2][2] = cube[14][2];

                cube[12][2] = cube[6][2];
                cube[13][2] = cube[7][2];
                cube[14][2] = cube[8][2];

                cube[6][2] = cube[17][0];
                cube[7][2] = cube[16][0];
                cube[8][2] = cube[15][0];

                cube[15][0] = temp2;
                cube[16][0] = temp1;
                cube[17][0] = temp;
                
                return true;
            case "R'":
                // Rotate the right face counter-clockwise
                temp = cube[3][0];
                cube[3][0] = cube[3][2];
                cube[3][2] = cube[5][2];
                cube[5][2] = cube[5][0];
                cube[5][0] = temp;

                temp = cube[3][1];
                cube[3][1] = cube[4][2];
                cube[4][2] = cube[5][1];
                cube[5][1] = cube[4][0];
                cube[4][0] = temp;

                // Rotate the adjacent edges counter-clockwise
                temp = cube[0][2];
                temp1 = cube[1][2];
                temp2 = cube[2][2];
                cube[0][2] = cube[17][0];
                cube[1][2] = cube[16][0];
                cube[2][2] = cube[15][0];

                cube[15][0] = cube[8][2];
                cube[16][0] = cube[7][2];
                cube[17][0] = cube[6][2];

                cube[6][2] = cube[12][2];
                cube[7][2] = cube[13][2];
                cube[8][2] = cube[14][2];

                cube[12][2] = temp;
                cube[13][2] = temp1;
                cube[14][2] = temp2;
                
                return true;
            case "2R":
                // Rotate the right face 180 degrees
                temp = cube[3][0];
                cube[3][0] = cube[5][2];
                cube[5][2] = temp;

                temp = cube[3][1];
                cube[3][1] = cube[5][1];
                cube[5][1] = temp;

                temp = cube[3][2];
                cube[3][2] = cube[5][0];
                cube[5][0] = temp;

                temp = cube[4][0];
                cube[4][0] = cube[4][2];
                cube[4][2] = temp;

                // Rotate the adjacent edges 180 degrees
                temp = cube[0][2];
                temp1 = cube[1][2];
                temp2 = cube[2][2];
                cube[0][2] = cube[6][2];
                cube[1][2] = cube[7][2];
                cube[2][2] = cube[8][2];
                cube[6][2] = temp;
                cube[7][2] = temp1;
                cube[8][2] = temp2;

                temp = cube[12][2];
                temp1 = cube[13][2];
                temp2 = cube[14][2];
                cube[12][2] = cube[17][0];
                cube[13][2] = cube[16][0];
                cube[14][2] = cube[15][0];
                cube[15][0] = temp2;
                cube[16][0] = temp1;
                cube[17][0] = temp;
                
                return true;
            case "L":
                // Rotate the left face (green) clockwise
                temp = cube[9][0];
                cube[9][0] = cube[11][0];
                cube[11][0] = cube[11][2];
                cube[11][2] = cube[9][2];
                cube[9][2] = temp;

                temp = cube[9][1];
                cube[9][1] = cube[10][0];
                cube[10][0] = cube[11][1];
                cube[11][1] = cube[10][2];
                cube[10][2] = temp;

                // Rotate the adjacent edges clockwise
                temp = cube[0][0];
                temp1 = cube[1][0];
                temp2 = cube[2][0];
                cube[0][0] = cube[17][2];
                cube[1][0] = cube[16][2];
                cube[2][0] = cube[15][2];

                cube[17][2] = cube[6][0];
                cube[16][2] = cube[7][0];
                cube[15][2] = cube[8][0];

                cube[6][0] = cube[12][0];
                cube[7][0] = cube[13][0];
                cube[8][0] = cube[14][0];

                cube[12][0] = temp;
                cube[13][0] = temp1;
                cube[14][0] = temp2;
                return true;
            case "L'":
                // Rotate the left face counter-clockwise
                temp = cube[9][0];
                cube[9][0] = cube[9][2];
                cube[9][2] = cube[11][2];
                cube[11][2] = cube[11][0];
                cube[11][0] = temp;

                temp = cube[9][1];
                cube[9][1] = cube[10][2];
                cube[10][2] = cube[11][1];
                cube[11][1] = cube[10][0];
                cube[10][0] = temp;

                // Rotate the adjacent edges counter-clockwise
                temp = cube[0][0];
                temp1 = cube[1][0];
                temp2 = cube[2][0];
                cube[0][0] = cube[12][0];
                cube[1][0] = cube[13][0];
                cube[2][0] = cube[14][0];

                cube[12][0] = cube[6][0];
                cube[13][0] = cube[7][0];
                cube[14][0] = cube[8][0];

                cube[6][0] = cube[17][2];
                cube[7][0] = cube[16][2];
                cube[8][0] = cube[15][2];

                cube[17][2] = temp;
                cube[16][2] = temp1;
                cube[15][2] = temp2;
                
                return true;
            case "2L":
                // Rotate the left face 180 degrees
                temp = cube[9][0];
                cube[9][0] = cube[11][2];
                cube[11][2] = temp;

                temp = cube[9][1];
                cube[9][1] = cube[11][1];
                cube[11][1] = temp;

                temp = cube[9][2];
                cube[9][2] = cube[11][0];
                cube[11][0] = temp;

                temp = cube[10][0];
                cube[10][0] = cube[10][2];
                cube[10][2] = temp;

                // Rotate the adjacent edges 180 degrees
                temp = cube[0][0];
                temp1 = cube[1][0];
                temp2 = cube[2][0];
                cube[0][0] = cube[6][0];
                cube[1][0] = cube[7][0];
                cube[2][0] = cube[8][0];
                cube[6][0] = temp;
                cube[7][0] = temp1;
                cube[8][0] = temp2;

                temp = cube[12][0];
                temp1 = cube[13][0];
                temp2 = cube[14][0];
                cube[12][0] = cube[17][2];
                cube[13][0] = cube[16][2];
                cube[14][0] = cube[15][2];
                cube[15][2] = temp2;
                cube[16][2] = temp1;
                cube[17][2] = temp;
                
                return true;
            case "F":
                // Rotate the front face (red) clockwise
                temp = cube[12][0];
                cube[12][0] = cube[14][0];
                cube[14][0] = cube[14][2];
                cube[14][2] = cube[12][2];
                cube[12][2] = temp;

                temp = cube[13][0];
                cube[13][0] = cube[14][1];
                cube[14][1] = cube[13][2];
                cube[13][2] = cube[12][1];
                cube[12][1] = temp;

                // Rotate the adjacent edges
                temp = cube[3][0];
                temp1 = cube[4][0];
                temp2 = cube[5][0];

                cube[3][0] = cube[2][0];
                cube[4][0] = cube[2][1];
                cube[5][0] = cube[2][2];

                cube[2][0] = cube[11][2];
                cube[2][1] = cube[10][2];
                cube[2][2] = cube[9][2];

                cube[9][2] = cube[6][0];
                cube[10][2] = cube[6][1];
                cube[11][2] = cube[6][2];

                cube[6][0] = temp2;
                cube[6][1] = temp1;
                cube[6][2] = temp;
                return true;
            case "F'":
                // Rotate the front face counter-clockwise
                temp = cube[12][0];
                cube[12][0] = cube[12][2];
                cube[12][2] = cube[14][2];
                cube[14][2] = cube[14][0];
                cube[14][0] = temp;

                temp = cube[13][0];
                cube[13][0] = cube[12][1];
                cube[12][1] = cube[13][2];
                cube[13][2] = cube[14][1];
                cube[14][1] = temp;

                // Rotate the adjacent edges counter-clockwise
                temp = cube[3][0];
                temp1 = cube[4][0];
                temp2 = cube[5][0];

                cube[3][0] = cube[6][2];
                cube[4][0] = cube[6][1];
                cube[5][0] = cube[6][0];

                cube[6][0] = cube[9][2];
                cube[6][1] = cube[10][2];
                cube[6][2] = cube[11][2];

                cube[9][2] = cube[2][2];
                cube[10][2] = cube[2][1];
                cube[11][2] = cube[2][0];

                cube[2][0] = temp;
                cube[2][1] = temp1;
                cube[2][2] = temp2;

                return true;
            case "2F":
                // Rotate the front face 180 degrees
                temp = cube[12][0];
                cube[12][0] = cube[14][2];
                cube[14][2] = temp;

                temp = cube[13][0];
                cube[13][0] = cube[13][2];
                cube[13][2] = temp;

                temp = cube[14][0];
                cube[14][0] = cube[12][2];
                cube[12][2] = temp;

                temp = cube[12][1];
                cube[12][1] = cube[14][1];
                cube[14][1] = temp;

                // Rotate the adjacent edges 180 degrees
                temp = cube[3][0];
                temp1 = cube[4][0];
                temp2 = cube[5][0];

                cube[3][0] = cube[11][2];
                cube[4][0] = cube[10][2];
                cube[5][0] = cube[9][2];

                cube[11][2] = temp;
                cube[10][2] = temp1;
                cube[9][2] = temp2;

                temp = cube[6][0];
                temp1 = cube[6][1];
                temp2 = cube[6][2];

                cube[6][2] = cube[2][0];
                cube[6][1] = cube[2][1];
                cube[6][0] = cube[2][2];
            
                cube[2][0] = temp2;
                cube[2][1] = temp1;
                cube[2][2] = temp;
                
                return true;
            case "B":
                // Rotate the back face (orange) clockwise
                temp = cube[15][0];
                cube[15][0] = cube[17][0];
                cube[17][0] = cube[17][2];
                cube[17][2] = cube[15][2];
                cube[15][2] = temp;

                temp = cube[15][1];
                cube[15][1] = cube[16][0];
                cube[16][0] = cube[17][1];
                cube[17][1] = cube[16][2];
                cube[16][2] = temp;

                // Rotate the adjacent edges
                temp = cube[3][2];
                temp1 = cube[4][2];
                temp2 = cube[5][2];

                cube[3][2] = cube[8][2];
                cube[4][2] = cube[8][1];
                cube[5][2] = cube[8][0];

                cube[8][0] = cube[9][0];
                cube[8][1] = cube[10][0];
                cube[8][2] = cube[11][0];

                cube[9][0] = cube[0][2];
                cube[10][0] = cube[0][1];
                cube[11][0] = cube[0][0];

                cube[0][0] = temp;
                cube[0][1] = temp1;
                cube[0][2] = temp2;
                
                return true;
            case "B'":
                // Rotate the back face counter-clockwise
                temp = cube[15][0];
                cube[15][0] = cube[15][2];
                cube[15][2] = cube[17][2];
                cube[17][2] = cube[17][0];
                cube[17][0] = temp;

                temp = cube[15][1];
                cube[15][1] = cube[16][2];
                cube[16][2] = cube[17][1];
                cube[17][1] = cube[16][0];
                cube[16][0] = temp;

                // Rotate the adjacent edges counter-clockwise
                temp = cube[3][2];
                temp1 = cube[4][2];
                temp2 = cube[5][2];

                cube[3][2] = cube[0][0];
                cube[4][2] = cube[0][1];
                cube[5][2] = cube[0][2];

                cube[0][0] = cube[11][0];
                cube[0][1] = cube[10][0];
                cube[0][2] = cube[9][0];

                cube[9][0] = cube[8][0];
                cube[10][0] = cube[8][1];
                cube[11][0] = cube[8][2];

                cube[8][0] = temp2;
                cube[8][1] = temp1;
                cube[8][2] = temp;
                
                return true;
            case "2B":
                // Rotate the back face 180 degrees
                temp = cube[15][0];
                cube[15][0] = cube[17][2];
                cube[17][2] = temp;

                temp = cube[15][1];
                cube[15][1] = cube[16][1];
                cube[16][1] = temp;

                temp = cube[15][2];
                cube[15][2] = cube[17][0];
                cube[17][0] = temp;

                temp = cube[16][0];
                cube[16][0] = cube[16][2];
                cube[16][2] = temp;

                // Rotate the adjacent edges 180 degrees
                temp = cube[3][2];
                temp1 = cube[4][2];
                temp2 = cube[5][2];

                cube[3][2] = cube[11][0];
                cube[4][2] = cube[10][0];
                cube[5][2] = cube[9][0];

                cube[11][0] = temp;
                cube[10][0] = temp1;
                cube[9][0] = temp2;

                temp = cube[0][0];
                temp1 = cube[0][1];
                temp2 = cube[0][2];

                cube[0][0] = cube[8][2];
                cube[0][1] = cube[8][1];
                cube[0][2] = cube[8][0];

                cube[8][0] = temp2;
                cube[8][1] = temp1;
                cube[8][2] = temp;
                return true;
            
            //case "solve":
                //printSolveSequence(history);

            default:
                System.out.println("Invalid move: " + move);
                return false;
        }
    }
}
