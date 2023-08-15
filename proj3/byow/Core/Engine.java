package byow.Core;
















import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;








import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;
import java.util.Random;












public class Engine implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 45;
    private int displayRow = HEIGHT - HEIGHT / 5;
    private int displayCol = WIDTH / 2;
    private Random rand;
    private int[][] board = new int[WIDTH][HEIGHT];
    private boolean gameOver = false;
    private Long seed;
    private Point[][] totalRooms;
    private Point avatarLocation;
    private TETile[][] finalWorldFrame;
    private boolean initialized = false;
    private String input = "";
    private int numRooms;
    private boolean fromLoad = false;
    private int x;
    private int y;
    private String name = "you";
    private char charSelected = Tileset.AVATAR.character();
    private Point miniLoc;




    private TETile wall = Tileset.WALL;
    private TETile floor = Tileset.FLOOR;
    private TETile bomb = Tileset.pixelMushroom;
    private TETile goal = Tileset.pixelTarget;
    private TETile miniGame = Tileset.key;
    private TETile nothing = Tileset.NOTHING;
    String seedInput = "";
    private TETile[][] miniGameBoard = new TETile[20][20];
    private TERenderer miniter = new TERenderer();
    private int collected = 0;


    private String up = "w";
    private String upUp = "W";
    private String left = "a";
    private String leftL = "A";
    private String down = "s";
    private String downD = "S";
    private String right = "d";
    private String rightR = "D";




    public Point[][] roomsGenerator() {
        numRooms = 9 + rand.nextInt(9) + 9;
        totalRooms = new Point[numRooms][];
        for (int i = 0; i < numRooms; i++) {
            int roomHeight = 5 + rand.nextInt(5);
            int roomWidth = 5 + rand.nextInt(5);
            int col = rand.nextInt(WIDTH - roomWidth);
            int row = rand.nextInt(HEIGHT - roomHeight);
            while (!boardChecker(roomWidth, roomHeight, row, col)) {
                roomHeight = 5 + rand.nextInt(3);
                roomWidth = 5 + rand.nextInt(3);
                col = rand.nextInt(WIDTH - roomWidth);
                row = rand.nextInt(HEIGHT - roomHeight);
            }
            Point[] myRooms = new Point[roomWidth * roomHeight];
            int counter = 0;
            for (int j = col; j < col + roomWidth; j++) {
                for (int k = row; k < row + roomHeight; k++) {
                    if (j == col
                            || k == row
                            || j == col + roomWidth - 1
                            || k == row + roomHeight - 1) {
                        myRooms[counter] = new Point(j, k, true);
                        board[j][k] = 2;
                    } else {
                        myRooms[counter] = new Point(j, k, false);
                        board[j][k] = 1;
                    }
                    if (j == col + Math.floorDiv(roomWidth, 2) && k == row + Math.floorDiv(roomHeight, 2)) {
                        myRooms[counter].setMidPoint();
                    }
                    counter++;
                }
            }
            totalRooms[i] = myRooms;
        }
        return totalRooms;
    }


    public boolean boardChecker(int width, int height, int row, int column) {
        int top = row + height;
        int right = column + width;
        if (top > HEIGHT - 5
                || row < 5
                || column < 5
                || right > WIDTH - 5
                || width < 5
                || height < 5) {
            return false;
        } else {
            for (int i = row; i < row + height; i++) {
                for (int j = column; j < column + width; j++) {
                    if (board[j][i] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public Point getMidpoint(Point[] roomPoints) {
        for (int i = 0; i < roomPoints.length; i++) {
            if (roomPoints[i].isMidPoint()) {
                return roomPoints[i];
            }
        }
        return null;
    }








    public void hallGenerator(Point[] startEnd) {
        int disX = Math.abs(startEnd[0].getCol() - startEnd[1].getCol());
        int disY = Math.abs(startEnd[0].getRow() - startEnd[1].getRow());
        int startx = startEnd[0].getCol();
        int starty = startEnd[1].getRow();
        if (startEnd[0].getCol() - startEnd[1].getCol() > 0) {
            startx = startEnd[1].getCol();
            hallx(startx, disX, startEnd[1]);
            startEnd[1] = new Point(startx + disX, startEnd[1].getRow(),  false);
        } else if (startEnd[0].getCol() - startEnd[1].getCol() < 0) {
            startx = startEnd[0].getCol();
            hallx(startx, disX, startEnd[0]);
            startEnd[0] = new Point(startx + disX, startEnd[0].getRow(), false);
        }
        if (startEnd[0].getRow() - startEnd[1].getRow() > 0) {
            starty = startEnd[1].getRow();
            hally(starty, disY, startEnd[1]);
        } else if (startEnd[0].getRow() - startEnd[1].getRow() < 0) {
            starty = startEnd[0].getRow();
            hally(starty, disY, startEnd[0]);
        }
    }








    public void hallx(int startCol, int distance, Point startPoint) {
        int curr = startCol;
        while (curr <= startCol + distance) {
            if (board[curr][startPoint.getRow() + 1] == 2 && board[curr][startPoint.getRow() - 1] == 1) {
                curr += 1;
                continue;
            } else if (board[curr][startPoint.getRow() + 1] == 1 && board[curr][startPoint.getRow() - 1] == 1) {
                board[curr][startPoint.getRow()] = 1;
            } else if (board[curr][startPoint.getRow()] == 0) {
                board[curr][startPoint.getRow() + 1] = 2;
                board[curr][startPoint.getRow()] = 1;
                board[curr][startPoint.getRow() - 1] = 2;
            } else if (board[curr][startPoint.getRow()] == 2
                    && board[curr][startPoint.getRow() + 1] == 2
                    && board[curr][startPoint.getRow() - 1] == 2) {
                board[curr][startPoint.getRow()] = 1;
            } else if (board[curr][startPoint.getRow() - 1] == 0
                    && (board[curr][startPoint.getRow() + 1] == 1
                    || board[curr][startPoint.getRow() + 1] == 2)) {
                board[curr][startPoint.getRow()] = 1;
                board[curr][startPoint.getRow() - 1] = 2;
            } else if (board[curr][startPoint.getRow()] == 2
                    && (board[curr][startPoint.getRow() + 1] == 0
                    || board[curr][startPoint.getRow() + 1] == 1)) {
                board[curr][startPoint.getRow()] = 1;
                board[curr][startPoint.getRow() + 1] = 2;
            }
            curr += 1;
        }
        if (board[curr][startPoint.getRow()] == 0) {
            board[curr - 1][startPoint.getRow()] = 2;
        }
    }








    public void hally(int startRow, int distance, Point startPoint) {
        int curr = startRow;
        int col = startPoint.getCol();
        while (curr <= startRow + distance) {
            if (board[col][curr] == 0) {
                board[startPoint.getCol() + 1][curr] = 2;
                board[startPoint.getCol()][curr] = 1;
                board[startPoint.getCol() - 1][curr] = 2;
            } else if (board[col][curr] == 2
                    && board[col - 1][curr] == 0) {
                board[col][curr] = 1;
                board[col - 1][curr] = 2;
            } else if (board[col][curr] == 2
                    && board[col + 1][curr] == 0) {
                board[col][curr] = 1;
                board[col + 1][curr] = 2;
            } else if (board[col][curr] == 2
                    && board[col + 1][curr] == 2
                    && board[col - 1][curr] == 2) {
                board[col][curr] = 1;
            } else if (board[col][curr] == 2
                    && board[col + 1][curr] == 1) {
                board[col][curr] = 1;
                board[col + 1][curr] = 2;
            }
            curr += 1;
        }
        if (board[startPoint.getCol()][curr] == 0) {
            board[startPoint.getCol()][curr - 1] = 2;
        }
        if (board[startPoint.getCol()][curr] == 2 && board[startPoint.getCol()][curr + 1] == 1) {
            board[startPoint.getCol()][curr] = 1;
        }
    }




    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        //handles the individual
        ter = new TERenderer();
        String command = "";
        StdDraw.setCanvasSize(WIDTH * 8 * 2, HEIGHT * 8 * 2);
        Font font = new Font("Monaco", Font.BOLD, 6 * 5);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.white);
        StdDraw.text(displayCol, displayRow, "CS61BL Game");
        StdDraw.text(displayCol, displayRow - Math.floorDiv(displayRow, 5), "New Game (N) "
                + "\n Load Game (L) "
                + "\n Quit (Q)"
                + "\n Select Avatar (A)");
        StdDraw.text(displayCol, displayRow - Math.floorDiv(displayRow, 3), "Name Your Avatar (M)"
                + "\n Read Lore (R)"
                + "\n Change Movement Keys (C)");
        StdDraw.text(displayCol, displayRow - Math.floorDiv(displayRow, 2), "Choose theme (T)");
        StdDraw.show();
        boolean correctCommand = false;
        while (!correctCommand) {
            if (StdDraw.hasNextKeyTyped()) {
                command = Character.toString(StdDraw.nextKeyTyped());
            }
            if (command.equals("N") || command.equals("n")) {
                correctCommand = true;
                StdDraw.clear(Color.black);
                StdDraw.clear(Color.BLACK);
                StdDraw.text(displayCol, displayRow, "Please Enter a Seed and Hit s");
                seedInput = "";
                StdDraw.show();
                while (true) {
                    if (StdDraw.hasNextKeyTyped()) {
                        StdDraw.clear(Color.black);
                        String typed = Character.toString(StdDraw.nextKeyTyped());
                        seedInput = seedInput + typed;
                        StdDraw.text(displayCol, displayRow - 5 - 5, seedInput);
                        StdDraw.show();
                        if (typed.equals("s")) {
                            ter.renderFrame(interactWithInputString(seedInput));
                            startGame();
                            break;
                        }
                    }
                }
            } else if (command.equals("M")
                    || command.equals("m")) {
                correctCommand = true;
                avatarName();
            } else if (command.equals("L")
                    || command.equals("l")) {
                correctCommand = true;
                loadGame();
            } else if (command.equals("Q")
                    || command.equals("q")) {
                correctCommand = true;
                System.exit(0);
            } else if (command.equals("A")
                    || command.equals("a")) {
                correctCommand = true;
                avatarSelection();
            } else if (command.equals("R")
                    || command.equals("r")) {
                correctCommand = true;
                displayLore();
            } else if (command.equals("c")
                    || command.equals("C")) {
                correctCommand = true;
                chooseKeys();
            } else if (command.equals("t")
                    || command.equals("T")) {
                correctCommand = true;
                chooseTheme();
            }
        }
    }

    public void chooseTheme() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.text(displayCol, displayRow, "What theme would you like?");
        StdDraw.text(displayCol, displayRow - Math.floorDiv(displayRow, 3), "Water (W)" +
                "\n Pixel World (P) " +
                "\n Desert (D)" +
                "\n Original (O)");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                StdDraw.clear(Color.black);
                String answer = Character.toString(StdDraw.nextKeyTyped());
                switch (answer) {
                    case "w", "W" -> {
                        wall = Tileset.GRASS;
                        floor = Tileset.WATER;
                    }
                    case "P", "p" -> {
                        wall = Tileset.pixelWall;
                        floor = Tileset.pixelGrass;
                        nothing = Tileset.pixelNothing;
                    }
                    case "D", "d" -> {
                        wall = Tileset.MOUNTAIN;
                        floor = Tileset.SAND;
                    }
                    default -> {
                        wall = Tileset.WALL;
                        floor = Tileset.FLOOR;
                    }
                }
                break;
            }
        }
        interactWithKeyboard();
    }


    public void chooseKeys() {
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "Please select a key for moving up");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                up = Character.toString(StdDraw.nextKeyTyped());
                upUp = up;
                break;
            }
        }
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "Please select a key for moving down");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                down = Character.toString(StdDraw.nextKeyTyped());
                downD = down;
                break;
            }
        }
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "Please select a key for moving left");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                left = Character.toString(StdDraw.nextKeyTyped());
                leftL = left;
                break;
            }
        }
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "Please select a key for moving right");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                right = Character.toString(StdDraw.nextKeyTyped());
                rightR = right;
                break;
            }
        }
        interactWithKeyboard();
    }
    public void displayLore() {
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "All your life, you have been poor...");
        StdDraw.show();
        StdDraw.pause(2000);
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "Tired of living in poverty, you venture into a dungeon");
        StdDraw.text(displayCol, displayRow - 5 - 5, "where you know there is a secret room filled with coins");
        StdDraw.show();
        StdDraw.pause(3000);
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "While getting in was easy, you soon discovered you were trapped!");
        StdDraw.text(displayCol, displayRow - 5 - 5, "escape the dungeon by looking for the target");
        StdDraw.text(displayCol, displayRow - 5 - 5 - 5 - 5, "remember, not to overlook the key to your happiness");
        StdDraw.text(displayCol, displayRow - 5 - 5 - 5 - 5 - 5 - 5,
                "and be warned.... DO NOT MOVE IN THE DARK");
        StdDraw.show();
        StdDraw.pause(7000);
        StdDraw.clear(Color.black);
        interactWithKeyboard();
    }
    public void avatarName() {
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "Please enter your avatar's name and click 0");
        StdDraw.show();
        name = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String key = Character.toString(StdDraw.nextKeyTyped());
                if (key.equals("0")) {
                    Tileset.AVATAR = new TETile(Tileset.AVATAR.character(), Color.white, Color.black, name);
                    break;
                }
                name = name + key;
                StdDraw.clear(Color.BLACK);
                StdDraw.text(displayCol, displayRow, name);
                StdDraw.show();
            }
        }
        interactWithKeyboard();
    }
    public void avatarSelection() {
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "Press the letter you would like your avatar to be");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                charSelected = StdDraw.nextKeyTyped();
                Tileset.AVATAR = new TETile(charSelected, Color.white, Color.black, name);
                break;
            }
        }
        interactWithKeyboard();
    }
    public void startGame() {
        while (true) {
            if (initialized
                    && (x != (int) StdDraw.mouseX()
                    || y != (int) StdDraw.mouseY())) {
                ter.renderFrame(finalWorldFrame);
                mousePos();
                displayDate();
                blackOut();
            }
            if (fromLoad) {
                ter.renderFrame(interactWithInputString(input));
                mousePos();
                displayDate();
                fromLoad = false;
                blackOut();
            }
            if (StdDraw.hasNextKeyTyped()) {
                String c = Character.toString(StdDraw.nextKeyTyped());
                input = input + c;
                if (((c.equals("s") || c.equals("S")) && !initialized)
                        || c.equals("q") || c.equals("Q")
                        || c.equals("l") || c.equals("L")
                        || c.equals(up) || c.equals(upUp)
                        || c.equals(down) || c.equals(downD)
                        || c.equals(right) || c.equals(rightR)
                        || c.equals(left) || c.equals(leftL)) {
                    ter.renderFrame(interactWithInputString(input));
                    mousePos();
                    displayDate();
                    blackOut();
                }
            }
        }
    }
    public boolean dark = false;
    public void blackOut() {
        int chance = rand.nextInt(1, 20);
        if (chance == 7) {
            StdDraw.clear(Color.black);
            StdDraw.text(displayCol, displayRow, "DON'T MOVE!");
            StdDraw.show();
            dark = true;
            moveinDark();
            ter.renderFrame(finalWorldFrame);
        }
    }
    private boolean inMini = false;

    public void moveinDark() {
        while (dark && !inMini) {
            StdDraw.pause(3000);
            if (StdDraw.hasNextKeyTyped()) {
                lose();
                break;
            }
            dark = false;
        }
    }




    public void displayDate() {
        Date time = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE MMM d HH:mm yyyy Z");
        StdDraw.textRight(WIDTH, HEIGHT - 1, dateformat.format(time));
        StdDraw.show();
    }




    public void mousePos() {
        x = (int) StdDraw.mouseX();
        y = (int) StdDraw.mouseY();
        StdDraw.setPenColor(Color.white);
        Font fontSmall = new Font("Monaco", Font.BOLD, 5 * 4);
        StdDraw.setFont(fontSmall);
        StdDraw.line(0, HEIGHT - 2, WIDTH, HEIGHT - 2);
        if (x >= 0 && x < WIDTH
                && y >= 0 && y < HEIGHT) {
            StdDraw.textLeft(0, HEIGHT - 1, finalWorldFrame[x][y].description());
        }
        StdDraw.show();
    }
    public void loadGame() {
        fromLoad = true;
        In in = new In("saved.txt");
        String seedLine = in.readLine();
        String typedSeed = seedLine + "s";
        String avatarCol = in.readLine();
        String avatarRow = in.readLine();
        String avatarName = in.readLine();
        String avatarChar = in.readLine();
        if (gameOver) {
            StdDraw.clear(Color.black);
            StdDraw.text(displayCol, displayRow, "The last game has already ended. Please press q to quit");
            StdDraw.show();
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    char quit = StdDraw.nextKeyTyped();
                    if (quit == 'q'
                            || quit == 'Q') {
                        System.exit(0);
                    }
                }
            }
        } else {
            input = typedSeed;
            avatarLocation = new Point(Integer.parseInt(avatarCol), Integer.parseInt(avatarRow), false);
            Tileset.AVATAR = new TETile(avatarChar.charAt(0), Color.white, Color.black, avatarName);
            startGame();
        }
    }
    public void saveGame() {
        Out out = new Out("saved.txt");
        out.print(seed
                + "\n" + avatarLocation.getCol()
                + "\n" + avatarLocation.getRow()
                + "\n" + name
                + "\n" + charSelected);
    }

    public void moveUp() {
        int col = avatarLocation.getCol();
        int row = avatarLocation.getRow();
        if (finalWorldFrame[col][row + 1] == floor) {
            finalWorldFrame[col][row] = floor;
            finalWorldFrame[col][row + 1] = Tileset.AVATAR;
            avatarLocation = new Point(col, row + 1, false);
        } else if (finalWorldFrame[col][row + 1] == goal) {
            win();
        } else if (finalWorldFrame[col][row + 1] == bomb) {
            lose();
        } else if (finalWorldFrame[col][row + 1] == miniGame) {
            finalWorldFrame[col][row] = floor;
            finalWorldFrame[col][row + 1] = Tileset.AVATAR;
            avatarLocation = new Point(col, row + 1, false);
            initiateMinigame();
        }
    }
    public void moveDown() {
        int col = avatarLocation.getCol();
        int row = avatarLocation.getRow();
        if (finalWorldFrame[col][row - 1] == floor) {
            finalWorldFrame[col][row] = floor;
            finalWorldFrame[col][row - 1] = Tileset.AVATAR;
            avatarLocation = new Point(col, row - 1, false);
        } else if (finalWorldFrame[col][row - 1] == goal) {
            win();
        } else if (finalWorldFrame[col][row - 1] == bomb) {
            lose();
        } else if (finalWorldFrame[col][row - 1] == miniGame) {
            finalWorldFrame[col][row] = floor;
            finalWorldFrame[col][row - 1] = Tileset.AVATAR;
            avatarLocation = new Point(col, row - 1, false);
            initiateMinigame();
        }
    }
    public void moveLeft() {
        int col = avatarLocation.getCol();
        int row = avatarLocation.getRow();
        if (finalWorldFrame[col - 1][row] == floor) {
            finalWorldFrame[col][row] = floor;
            finalWorldFrame[col - 1][row] = Tileset.AVATAR;
            avatarLocation = new Point(col - 1, row, false);
        } else if (finalWorldFrame[col - 1][row] == goal) {
            win();
        } else if (finalWorldFrame[col - 1][row] == bomb) {
            lose();
        } else if (finalWorldFrame[col - 1][row] == miniGame) {
            finalWorldFrame[col][row] = floor;
            finalWorldFrame[col - 1][row] = Tileset.AVATAR;
            avatarLocation = new Point(col - 1, row, false);
            initiateMinigame();
        }
    }
    public void moveRight() {
        int col = avatarLocation.getCol();
        int row = avatarLocation.getRow();
        if (finalWorldFrame[col + 1][row] == floor) {
            finalWorldFrame[col][row] = floor;
            finalWorldFrame[col + 1][row] = Tileset.AVATAR;
            avatarLocation = new Point(col + 1, row, false);
        } else if (finalWorldFrame[col + 1][row] == goal) {
            win();
        } else if (finalWorldFrame[col + 1][row] == bomb) {
            lose();
        } else if (finalWorldFrame[col + 1][row] == miniGame) {
            finalWorldFrame[col][row] = floor;
            finalWorldFrame[col + 1][row] = Tileset.AVATAR;
            avatarLocation = new Point(col + 1, row, false);
            initiateMinigame();
        }
    }




    public void initiateMinigame() {
        saveGame();
        inMini = true;
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "Collect all the coins!");
        StdDraw.show();
        StdDraw.pause(2000);
        StdDraw.clear(Color.black);
        for (int col = 0; col < miniGameBoard.length; col++) {
            for (int row = 0; row < miniGameBoard.length; row++) {
                if ((col == 0
                        || col == miniGameBoard.length - 1)
                        || (row == 0
                        || row == miniGameBoard.length - 1)) {
                    miniGameBoard[col][row] = wall;
                } else {
                    miniGameBoard[col][row] = floor;
                }
            }
        }
        miniGameBoard[1][1] = Tileset.AVATAR;
        miniLoc = new Point(1, 1, false);
        for (int i = 0; i < 5; i++) {
            generateCollectible();
        }
        miniter.renderFrame(miniGameBoard);
        miniGameActions();
        if (collected >= 5) {
            StdDraw.clear(Color.black);
            StdDraw.text(displayCol, displayRow, "Congrats! You beat the minigame");
            StdDraw.show();
            StdDraw.pause(2000);
            StdDraw.clear(Color.black);
            inMini = false;
            loadGame();
        }
    }




    public void miniGameActions() {
        String move = "";
        while (collected < 5) {
            if (StdDraw.hasNextKeyTyped()) {
                String thisMove = Character.toString(StdDraw.nextKeyTyped());
                move = move + thisMove;
                if (thisMove.equals(up)
                        || thisMove.equals(upUp)) {
                    miniMoveUp();
                    miniter.renderFrame(miniGameBoard);
                } else if (thisMove.equals(left)
                        || thisMove.equals(leftL)) {
                    miniMoveLeft();
                    miniter.renderFrame(miniGameBoard);
                } else if (thisMove.equals(down)
                        || thisMove.equals(downD)) {
                    miniMoveDown();
                    miniter.renderFrame(miniGameBoard);
                } else if (thisMove.equals(right)
                        || thisMove.equals(rightR)) {
                    miniMoveRight();
                    miniter.renderFrame(miniGameBoard);
                }
            }
        }
    }
    public void miniMoveUp() {
        int col = miniLoc.getCol();
        int row = miniLoc.getRow();
        if (miniGameBoard[col][row + 1] == floor) {
            miniGameBoard[col][row] = floor;
            miniGameBoard[col][row + 1] = Tileset.AVATAR;
            miniLoc = new Point(col, row + 1, false);
        } else if (miniGameBoard[col][row + 1] == Tileset.coins) {
            miniGameBoard[col][row] = floor;
            miniGameBoard[col][row + 1] = Tileset.AVATAR;
            miniLoc = new Point(col, row + 1, false);
            collected++;
        }
    }

    public void miniMoveDown() {
        int col = miniLoc.getCol();
        int row = miniLoc.getRow();
        if (miniGameBoard[col][row - 1] == floor) {
            miniGameBoard[col][row] = floor;
            miniGameBoard[col][row - 1] = Tileset.AVATAR;
            miniLoc = new Point(col, row - 1, false);
        } else if (miniGameBoard[col][row - 1] == Tileset.coins) {
            miniGameBoard[col][row] = floor;
            miniGameBoard[col][row - 1] = Tileset.AVATAR;
            miniLoc = new Point(col, row - 1, false);
            collected++;
        }
    }
    public void miniMoveLeft() {
        int col = miniLoc.getCol();
        int row = miniLoc.getRow();
        if (miniGameBoard[col - 1][row] == floor) {
            miniGameBoard[col][row] = floor;
            miniGameBoard[col - 1][row] = Tileset.AVATAR;
            miniLoc = new Point(col - 1, row, false);
        } else if (miniGameBoard[col - 1][row] == Tileset.coins) {
            miniGameBoard[col][row] = floor;
            miniGameBoard[col - 1][row] = Tileset.AVATAR;
            miniLoc = new Point(col - 1, row, false);
            collected++;
        }
    }
    public void miniMoveRight() {
        int col = miniLoc.getCol();
        int row = miniLoc.getRow();
        if (miniGameBoard[col + 1][row] == floor) {
            miniGameBoard[col][row] = floor;
            miniGameBoard[col + 1][row] = Tileset.AVATAR;
            miniLoc = new Point(col + 1, row, false);
        } else if (miniGameBoard[col + 1][row] == Tileset.coins) {
            miniGameBoard[col][row] = floor;
            miniGameBoard[col + 1][row] = Tileset.AVATAR;
            miniLoc = new Point(col + 1, row, false);
            collected++;
        }
    }

    public void generateCollectible() {
        int colx = rand.nextInt(miniGameBoard.length - 1);
        int coly = rand.nextInt(miniGameBoard.length - 1);
        while (miniGameBoard[colx][coly] != floor) {
            colx = rand.nextInt(miniGameBoard.length - 1);
            coly = rand.nextInt(miniGameBoard.length - 1);
        }
        miniGameBoard[colx][coly] = Tileset.coins;
    }

    public void win() {
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "You win! Press r if you would like to play again, or q to exit");
        StdDraw.show();
        String decision = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                decision = decision + StdDraw.nextKeyTyped();
            }
            if (decision.equals("r")
                    || decision.equals("R")) {
                initialized = false;
                avatarLocation = null;
                interactWithKeyboard();
                break;
            } else if (decision.equals("Q")
                    || decision.equals("q")) {
                System.exit(0);
            }
        }
    }




    public void lose() {
        StdDraw.clear(Color.black);
        StdDraw.text(displayCol, displayRow, "You lost! Press r if you would like to restart, or q to exit");
        StdDraw.show();
        char decision = ' ';
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                decision = StdDraw.nextKeyTyped();
            }
            if (decision == 'r'
                    || decision == 'R') {
                initialized = false;
                avatarLocation = null;
                interactWithKeyboard();
                break;
            } else if (decision == 'Q'
                    || decision == 'q') {
                System.exit(0);
            }
        }
    }
    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param stringInput the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public void initializeWorld(String stringInput) {
        initialized = true;
        ter.initialize(WIDTH, HEIGHT);
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        board = new int[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                finalWorldFrame[i][j] = nothing;
            }
        }
        seed = Long.parseLong(stringInput);
        rand = new Random(seed);
        Point[][] point = roomsGenerator();
        for (Point[] i : point) {
            for (Point p : i) {
                if (p.isWall()) {
                    finalWorldFrame[p.getCol()][p.getRow()] = wall;
                } else {
                    finalWorldFrame[p.getCol()][p.getRow()] = floor;
                }
            }
        }








        for (int j = 0; j < totalRooms.length - 1; j++) {
            Point[] pointArr = new Point[2];
            pointArr[0] = (getMidpoint(totalRooms[j]));
            pointArr[1] = (getMidpoint(totalRooms[j + 1]));
            hallGenerator(pointArr);
        }




        for (int col = 0; col < WIDTH; col++) {
            for (int row = 0; row < HEIGHT; row++) {
                if (board[col][row] == 1) {
                    finalWorldFrame[col][row] = floor;
                } else if (board[col][row] == 2) {
                    finalWorldFrame[col][row] = wall;
                }
            }
        }




        generateGoal();
        int numTraps = 5 + rand.nextInt(5);
        for (int i = 0; i < numTraps; i++) {
            generateFakes();
        }
        miniGameDoor();
        if (avatarLocation == null) {
            int spawnRoom = rand.nextInt(numRooms);
            int spawnLocation = rand.nextInt(totalRooms[spawnRoom].length);
            Point spawnPoint = totalRooms[spawnRoom][spawnLocation];
            while (board[spawnPoint.getCol()][spawnPoint.getRow()] != 1) {
                spawnRoom = rand.nextInt(numRooms);
                spawnLocation = rand.nextInt(totalRooms[spawnRoom].length);
                spawnPoint = totalRooms[spawnRoom][spawnLocation];
            }
            finalWorldFrame[spawnPoint.getCol()][spawnPoint.getRow()] = Tileset.AVATAR;
            board[spawnPoint.getCol()][spawnPoint.getRow()] = -1;
            avatarLocation = spawnPoint;
        } else {
            finalWorldFrame[avatarLocation.getCol()][avatarLocation.getRow()] = Tileset.AVATAR;
            board[avatarLocation.getCol()][avatarLocation.getRow()] = -1;
        }
    }




    public void generateGoal() {
        int goalx = rand.nextInt(WIDTH);
        int goaly = rand.nextInt(HEIGHT);
        while (finalWorldFrame[goalx][goaly] != floor) {
            goalx = rand.nextInt(WIDTH);
            goaly = rand.nextInt(HEIGHT);
        }
        finalWorldFrame[goalx][goaly] = goal;
    }
    public void generateFakes() {
        int fakex = rand.nextInt(WIDTH);
        int fakey = rand.nextInt(HEIGHT);
        while (finalWorldFrame[fakex][fakey] != wall) {
            fakex = rand.nextInt(WIDTH);
            fakey = rand.nextInt(HEIGHT);
        }
        finalWorldFrame[fakex][fakey] = bomb;
    }




    public void miniGameDoor() {
        int minix = rand.nextInt(WIDTH);
        int miniy = rand.nextInt(HEIGHT);
        while (finalWorldFrame[minix][miniy] != floor) {
            minix = rand.nextInt(WIDTH);
            miniy = rand.nextInt(HEIGHT);
        }
        finalWorldFrame[minix][miniy] = miniGame;
    }

    public TETile[][] interactWithInputString(String inputStr) {
        // gives the result after typing on keyboard
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
//        processString(inputStr);
        String prevInput;
        String currInput = inputStr.substring(inputStr.length() - 1);
        if (inputStr.length() > 1) {
            prevInput = inputStr.substring(inputStr.length() - 2, inputStr.length() - 1);
        } else {
            prevInput = "";
        }
        if ((currInput.equals("s") || currInput.equals("S"))
                && !initialized) {
            initializeWorld(inputStr.substring(0, inputStr.length() - 1));
        } else if (currInput.equals(up) || currInput.equals(upUp)) {
            moveUp();
        } else if (currInput.equals(left) || currInput.equals(leftL)) {
            moveLeft();
        } else if (currInput.equals(down) || currInput.equals(downD)) {
            moveDown();
        } else if (currInput.equals(right) || currInput.equals(rightR)) {
            moveRight();
        } else if (prevInput.equals(":")
                && (currInput.equals("Q") || (currInput.equals("q")))) {
            saveGame();
            System.exit(0);
        } else if (currInput.equals("l") || currInput.equals("L")) {
            loadGame();
        }
        return finalWorldFrame;
    }
}






