
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

        
        Cube.show(cube);


    }
}