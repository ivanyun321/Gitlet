package byow.Core;

import byow.TileEngine.TETile;

import java.util.ArrayList;

public class Room {
    private int height;
    private int width;
    private int[] entrance;
    private ArrayList<Point> points;
    private int roomID;
    private Point point;

    public Room(int width, int height, int roomID) {
        this.width = width;
        this.height = height;
        this.roomID = roomID;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRoomID() {
        return roomID;
    }

    public void addPoints(Point point) {
        points.add(point);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public Point getStart() {
        return point;
    }
    public void setStart(int row, int col) {
        Point point = new Point(row, col, true);
    }

    public void setEntrance(int [] entrance) {
        this.entrance = entrance;
    }

    public int[] getEntrance() {
        return entrance;
    }

    public Point getMidPoint() {
        for (Point point: this.points) {
            if ((point.getRow() == Math.floorDiv(height, 2) + this.getStart().getRow())
                    && point.getCol() == Math.floorDiv(width, 2) + this.getStart().getCol()) {
                return point;
            }
        }
        return null;
    }

    public void updateBoard(TETile[][] board, TETile wall, TETile floor) {
        for (Point point : points) {
            if (point.isWall()) {
                board[point.getCol()][point.getRow()] = wall;
            }
            board[point.getCol()][point.getRow()] = floor;
        }
    }
}


