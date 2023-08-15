package byow.Core;

public class Point {
    private int col;
    private int row;
    private boolean isWall;
    private boolean isDoor = false;
    private boolean midPoint = false;

    Point(int col, int row, boolean isWall) {
        this.col = col;
        this.row = row;
        this.isWall = isWall;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    public boolean isWall() {
        return this.isWall;
    }


    public void setMidPoint() {
        this.midPoint = true;
    }

    public boolean isMidPoint() {
        return this.midPoint;
    }
}

