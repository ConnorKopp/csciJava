import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

public class RubiksCube extends JPanel implements ActionListener {

    private double angleX = -0.5, angleY = 0.5, angleZ = 0;
    private Point lastMousePos;
    private final List<Cubie> cubies = new ArrayList<>();
    private boolean isAutoRotating = true;

    private JFrame frame;
    private RubiksCube cubePanel;

    public RubiksCube() {
        setBackground(new Color(20, 20, 20));
        
        // Initialize 27 cubies (3x3x3)
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    cubies.add(new Cubie(x, y, z));
                }
            }
        }

        // Mouse listeners for rotation logic
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePos = e.getPoint();
                isAutoRotating = false; // Stop auto-rotation on click
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastMousePos != null) {
                    Point currentPos = e.getPoint();
                    // Calculate distance moved since last frame
                    double dx = currentPos.x - lastMousePos.x;
                    double dy = currentPos.y - lastMousePos.y;

                    // Update rotation angles (sensitivity of 0.01)
                    angleY += dx * 0.01;
                    angleX += dy * 0.01;

                    lastMousePos = currentPos;
                    repaint();
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        Timer timer = new Timer(16, this);
        timer.start();
    }

    public void setCubeColors(String[][] data) {
    for (Cubie cubie : cubies) {
        int gx = (int) Math.round(cubie.ox / (cubie.size + cubie.gap));
        int gy = (int) Math.round(cubie.oy / (cubie.size + cubie.gap));
        int gz = (int) Math.round(cubie.oz / (cubie.size + cubie.gap));

        int row, col;

        // ===== U face (rows 0-2)
        if (gy == 1) {
            row = 1 - gz;
            col = gx + 1;
            cubie.faceColors[4] = getColor(data[0 * 3 + row][col]);
        }

        // ===== D face (rows 6-8)
        if (gy == -1) {
            row = gz + 1;
            col = gx + 1;
            cubie.faceColors[5] = getColor(data[2 * 3 + row][col]);
        }

        // ===== F face (rows 12-14)
        if (gz == 1) {
            row = 1 - gy;
            col = gx + 1;
            cubie.faceColors[0] = getColor(data[4 * 3 + row][col]);
        }

        // ===== B face (rows 15-17)
        if (gz == -1) {
            row = 1 - gy;
            col = 1 - gx;
            cubie.faceColors[1] = getColor(data[5 * 3 + row][col]);
        }

        // ===== R face (rows 3-5)
        if (gx == 1) {
            row = 1 - gy;
            col = 1 - gz;
            cubie.faceColors[2] = getColor(data[1 * 3 + row][col]);
        }

        // ===== L face (rows 9-11)
        if (gx == -1) {
            row = 1 - gy;
            col = gz + 1;
            cubie.faceColors[3] = getColor(data[3 * 3 + row][col]);
        }
    }
    repaint();
    }

    private Color getColor(String code) {
        switch (code.toLowerCase()) {
            case "w": return Color.WHITE;
            case "y": return Color.YELLOW;
            case "r": return new Color(183, 18, 52);
            case "o": return new Color(255, 88, 0);
            case "b": return new Color(0, 70, 173);
            case "g": return new Color(0, 155, 72);
            default: return Color.BLACK;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isAutoRotating) {
            angleX += 0.005;
            angleY += 0.01;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // 1. Project all faces
        List<Face> allFaces = new ArrayList<>();
        for (Cubie cubie : cubies) {
            allFaces.addAll(cubie.getProjectedFaces(angleX, angleY, angleZ, centerX, centerY));
        }

        // 2. Sort by Z-depth (Painters Algorithm)
        Collections.sort(allFaces, (f1, f2) -> Double.compare(f2.avgZ, f1.avgZ));

        // 3. Render
        for (Face face : allFaces) {
            g2d.setColor(face.color);
            g2d.fill(face.path);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.draw(face.path);
        }
        
        // Instructions overlay
        g2d.setColor(Color.GRAY);
        g2d.drawString("Drag mouse to rotate", 20, 30);
    }

    private static class Cubie {
        double ox, oy, oz;
        double size = 50;
        double gap = 4;
        // Order: Front, Back, Right, Left, Top, Bottom
        Color[] faceColors = new Color[6];

        Cubie(double x, double y, double z) {
            this.ox = x * (size + gap);
            this.oy = y * (size + gap);
            this.oz = z * (size + gap);
            
            // Initialize with default black/hidden interior
            for(int i=0; i<6; i++) faceColors[i] = Color.BLACK;
        }

        List<Face> getProjectedFaces(double ax, double ay, double az, int cx, int cy) {
            List<Face> faces = new ArrayList<>();
            double s = size / 2.0;

            // Use the faceColors array instead of hardcoded values
            faces.add(createFace(new double[][]{{s,s,s}, {-s,s,s}, {-s,-s,s}, {s,-s,s}}, faceColors[0], ax, ay, az, cx, cy)); // Front
            faces.add(createFace(new double[][]{{s,s,-s}, {-s,s,-s}, {-s,-s,-s}, {s,-s,-s}}, faceColors[1], ax, ay, az, cx, cy)); // Back
            faces.add(createFace(new double[][]{{s,s,s}, {s,-s,s}, {s,-s,-s}, {s,s,-s}}, faceColors[2], ax, ay, az, cx, cy));   // Right
            faces.add(createFace(new double[][]{{-s,s,s}, {-s,-s,s}, {-s,-s,-s}, {-s,s,-s}}, faceColors[3], ax, ay, az, cx, cy)); // Left
            faces.add(createFace(new double[][]{{s,s,s}, {s,s,-s}, {-s,s,-s}, {-s,s,s}}, faceColors[4], ax, ay, az, cx, cy));  // Top
            faces.add(createFace(new double[][]{{s,-s,s}, {s,-s,-s}, {-s,-s,-s}, {-s,-s,s}}, faceColors[5], ax, ay, az, cx, cy)); // Bottom

            return faces;
        }

        

        private Face createFace(double[][] localVertices, Color color, double ax, double ay, double az, int cx, int cy) {
            Polygon poly = new Polygon();
            double totalZ = 0;

            for (double[] lv : localVertices) {
                double x = lv[0] + ox;
                double y = lv[1] + oy;
                double z = lv[2] + oz;

                // Rotation X
                double dy = y * Math.cos(ax) - z * Math.sin(ax);
                double dz = y * Math.sin(ax) + z * Math.cos(ax);
                y = dy; z = dz;

                // Rotation Y
                double dx = x * Math.cos(ay) + z * Math.sin(ay);
                dz = -x * Math.sin(ay) + z * Math.cos(ay);
                x = dx; z = dz;

                // Perspective Projection
                double fov = 800;
                double viewDistance = 1000;
                double scale = fov / (viewDistance + z);
                
                poly.addPoint((int) (x * scale) + cx, (int) (y * scale) + cy);
                totalZ += z;
            }

            return new Face(poly, color, totalZ / 4.0);
        }
    }

    private static class Face {
        Polygon path;
        Color color;
        double avgZ;

        Face(Polygon path, Color color, double avgZ) {
            this.path = path;
            this.color = color;
            this.avgZ = avgZ;
        }
    }

public void show(String[][] faceData) {
    if (frame == null) {   // only create window first time
        frame = new JFrame("Interactive 3D Rubik's Cube");

        cubePanel = this;  // use current panel instance

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(40, 40, 40));

        JButton UBtn = new JButton("U");
        UBtn.addActionListener(e -> {
            rotateFace(faceData,"U");
            cubePanel.setCubeColors(App.cube);
        });

        JButton UBtnPrime = new JButton("U'");
        UBtnPrime.addActionListener(e -> {
            rotateFace(faceData,"U'");
            cubePanel.setCubeColors(App.cube);
        });
        JButton UBtn2 = new JButton("2U");
        UBtn2.addActionListener(e -> {
            rotateFace(faceData,"2U");
            cubePanel.setCubeColors(App.cube);
        });

        JButton DBtn = new JButton("D");
        DBtn.addActionListener(e -> {
            rotateFace(faceData,"D");
            cubePanel.setCubeColors(App.cube);
        });
        JButton DBtnPrime = new JButton("D'");
        DBtnPrime.addActionListener(e -> {
            rotateFace(faceData,"D'");
            cubePanel.setCubeColors(App.cube);
        });
        JButton DBtn2 = new JButton("2D");
        DBtn2.addActionListener(e -> {
            rotateFace(faceData,"2D");
            cubePanel.setCubeColors(App.cube);
        });

        JButton RBtn = new JButton("R");
        RBtn.addActionListener(e -> {
            rotateFace(faceData,"R");
            cubePanel.setCubeColors(App.cube);
        });
        JButton RBtnPrime = new JButton("R'");
        RBtnPrime.addActionListener(e -> {
            rotateFace(faceData,"R'");
            cubePanel.setCubeColors(App.cube);
        });
        JButton RBtn2 = new JButton("2R");
        RBtn2.addActionListener(e -> {
            rotateFace(faceData,"2R");
            cubePanel.setCubeColors(App.cube);
        });

        JButton LBtn = new JButton("L");
        LBtn.addActionListener(e -> {
            rotateFace(faceData,"L");
            cubePanel.setCubeColors(App.cube);
        });
        JButton LBtnPrime = new JButton("L'");
        LBtnPrime.addActionListener(e -> {
            rotateFace(faceData,"L'");
            cubePanel.setCubeColors(App.cube);
        });
        JButton LBtn2 = new JButton("2L");
        LBtn2.addActionListener(e -> {
            rotateFace(faceData,"2L");
            cubePanel.setCubeColors(App.cube);
        });

        JButton FBtn = new JButton("F");
        FBtn.addActionListener(e -> {
            rotateFace(faceData,"F");
            cubePanel.setCubeColors(App.cube);
        });
        JButton FBtnPrime = new JButton("F'");
        FBtnPrime.addActionListener(e -> {
            rotateFace(faceData,"F'");
            cubePanel.setCubeColors(App.cube);
        });
        JButton FBtn2 = new JButton("2F");
        FBtn2.addActionListener(e -> {
            rotateFace(faceData,"2F");
            cubePanel.setCubeColors(App.cube);
        });

        JButton BBtn = new JButton("B");
        BBtn.addActionListener(e -> {
            rotateFace(faceData,"B");
            cubePanel.setCubeColors(App.cube);
        });
        JButton BBtnPrime = new JButton("B'");
        BBtnPrime.addActionListener(e -> {
            rotateFace(faceData,"B'");
            cubePanel.setCubeColors(App.cube);
        });
        JButton BBtn2 = new JButton("2B");
        BBtn2.addActionListener(e -> {
            rotateFace(faceData,"2B");
            cubePanel.setCubeColors(App.cube);
        });

        buttonPanel.add(UBtn);
        buttonPanel.add(UBtnPrime);
        buttonPanel.add(UBtn2);
        buttonPanel.add(DBtn);
        buttonPanel.add(DBtnPrime);
        buttonPanel.add(DBtn2);
        buttonPanel.add(RBtn);
        buttonPanel.add(RBtnPrime);
        buttonPanel.add(RBtn2);
        buttonPanel.add(LBtn);
        buttonPanel.add(LBtnPrime);
        buttonPanel.add(LBtn2);
        buttonPanel.add(FBtn);
        buttonPanel.add(FBtnPrime);
        buttonPanel.add(FBtn2);
        buttonPanel.add(BBtn);
        buttonPanel.add(BBtnPrime);
        buttonPanel.add(BBtn2);

        frame.setLayout(new BorderLayout());
        frame.add(cubePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Always update colors (no new window)
    setCubeColors(faceData);
}
    public static void rotateFace(String[][] cube, String move) {

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
                
                break;
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


                break;
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


                break;
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
                break;

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
                break;
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
                break;
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
                cube[0][2] = cube[14][2];
                cube[1][2] = cube[13][2];
                cube[2][2] = cube[12][2];

                cube[12][2] = cube[6][2];
                cube[13][2] = cube[7][2];
                cube[14][2] = cube[8][2];

                cube[6][2] = cube[17][0];
                cube[7][2] = cube[16][0];
                cube[8][2] = cube[15][0];

                cube[15][0] = temp2;
                cube[16][0] = temp1;
                cube[17][0] = temp;
                
                break;
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
                
                break;
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
                
                break;
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
                break;
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
                
                break;
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
                
                break;
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

                cube[3][0] = cube[0][0];
                cube[4][0] = cube[0][1];
                cube[5][0] = cube[0][2];

                cube[0][0] = cube[11][2];
                cube[0][1] = cube[10][2];
                cube[0][2] = cube[9][2];

                cube[9][2] = cube[8][0];
                cube[10][2] = cube[8][1];
                cube[11][2] = cube[8][2];

                cube[8][0] = temp2;
                cube[8][1] = temp1;
                cube[8][2] = temp;
                break;
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

                cube[3][0] = cube[8][2];
                cube[4][0] = cube[8][1];
                cube[5][0] = cube[8][0];

                cube[8][0] = cube[9][2];
                cube[8][1] = cube[10][2];
                cube[8][2] = cube[11][2];

                cube[9][2] = cube[0][2];
                cube[10][2] = cube[0][1];
                cube[11][2] = cube[0][0];

                cube[0][0] = temp;
                cube[0][1] = temp1;
                cube[0][2] = temp2;

                break;
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

                temp = cube[8][0];
                temp1 = cube[8][1];
                temp2 = cube[8][2];

                cube[8][0] = cube[0][0];
                cube[8][1] = cube[0][1];
                cube[8][2] = cube[0][2];
            
                cube[0][0] = temp2;
                cube[0][1] = temp1;
                cube[0][2] = temp;
                
                break;
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

                cube[3][2] = cube[6][2];
                cube[4][2] = cube[6][1];
                cube[5][2] = cube[6][0];

                cube[8][0] = cube[9][0];
                cube[8][1] = cube[10][0];
                cube[8][2] = cube[11][0];

                cube[9][0] = cube[2][2];
                cube[10][0] = cube[2][1];
                cube[11][0] = cube[2][0];

                cube[2][0] = temp;
                cube[2][1] = temp1;
                cube[2][2] = temp2;
                
                break;
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

                cube[3][2] = cube[2][0];
                cube[4][2] = cube[2][1];
                cube[5][2] = cube[2][2];

                cube[2][0] = cube[11][0];
                cube[2][1] = cube[10][0];
                cube[0][2] = cube[9][0];

                cube[9][0] = cube[6][0];
                cube[10][0] = cube[6][1];
                cube[11][0] = cube[6][2];

                cube[6][0] = temp2;
                cube[6][1] = temp1;
                cube[6][2] = temp;
                
                break;
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

                temp = cube[2][0];
                temp1 = cube[2][1];
                temp2 = cube[2][2];

                cube[2][0] = cube[6][2];
                cube[2][1] = cube[6][1];
                cube[2][2] = cube[6][0];

                cube[6][0] = temp2;
                cube[6][1] = temp1;
                cube[6][2] = temp;
                break;

            default:
                System.out.println("Invalid move: " + move);
        }

        
    }
    
}