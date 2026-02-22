import java.util.Scanner;

public class App {
    static String[][] checkCube =   {
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
    static String[][] cube =   {
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

    public static void main(String[] args) throws Exception {

        RubiksCube Cube = new RubiksCube();

        Scanner scn = new Scanner(System.in);

        System.out.println("Would you like to see the solved cube (1) or checkered cube (2)? (1,2) ");
        int ans = scn.nextInt();
        
        
        if(ans == 1)
            Cube.show(cube);
        else
            Cube.show(checkCube);

        scn.close();

    }
}