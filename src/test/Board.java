package test;


public class Board {
    Tile[][] board;
    final int rows;
    final int cols;

    private static Board boardInstance;
    
    private Board() {
        rows = 15;
        cols = 15;
        board = new Tile[rows][cols];
        boardInstance = null;
    }
    
    Board getBoard() {
        if (boardInstance == null) {
            boardInstance = new Board();
        }
        return boardInstance;
    }
    Tile[][] getTiles() {
        Tile[][] copyBoard = new Tile[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copyBoard[i][j] = board[i][j];
            }
        }
        return copyBoard;
    }
}
