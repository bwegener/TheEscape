package edu.orangecoastcollege.escapethecatcher;

public class Player {
    private int mRow;
    private int mCol;

    public void move(int[][] gameBoard, String direction) {


        // COMPLETED: Implement the logic for the move operation
        // COMPLETED: If the gameBoard is obstacle free in the direction requested,
        // COMPLETED: Move the player in the intended direction.  Otherwise, do nothing (player loses turn)

        // Check for obstacles, if obstacle, do nothing, if empty space, move there
        if(direction.equals("LEFT") && gameBoard[mRow][mCol-1] != BoardCodes.OBSTACLE)
            mCol--;
        else if(direction.equals("RIGHT") && gameBoard[mRow][mCol+1] != BoardCodes.OBSTACLE)
            mCol++;
        else if(direction.equals("UP") && gameBoard[mRow-1][mCol] != BoardCodes.OBSTACLE)
            mRow--;
        else if(direction.equals("DOWN") && gameBoard[mRow+1][mCol] != BoardCodes.OBSTACLE)
            mRow++;
    }

    public void setRow(int row) {
        mRow = row;
    }

    public int getRow() {
        return mRow;
    }

    public void setCol(int col) {
        mCol = col;
    }

    public int getCol() {
        return mCol;
    }

}
