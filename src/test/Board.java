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
    
    public static Board getBoard() {
        if (boardInstance == null) {
            boardInstance = new Board();
        }
        return boardInstance;
    }
    Tile[][] getTiles() {
        Tile[][] copyBoard = new Tile[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(board[i], 0, copyBoard[i], 0, cols);
        }
        return copyBoard;
    }

    boolean checkWordLocation(Word word) {
        int wordLength = word.getTiles().length;
        int wordRow = word.getRow();
        int wordCol = word.getCol();

        if (wordRow < 0 || wordRow >= rows || wordCol < 0 || wordCol >= cols) {
            return false;
        }

        if (word.isVertical()) {
            return !(word.getRow() + wordLength > rows);
        } else {
            return !(word.getCol() + wordLength > cols);
        }
    }

    boolean checkFirstPlacement(Word word) {
        int wordLength = word.getTiles().length;
        int wordRow = word.getRow();
        int wordCol = word.getCol();
        int middleRow = rows / 2;
        int middleCol = cols / 2;
        if (board[middleRow][middleCol] == null) {
            if (word.isVertical()) {
                if (wordCol != middleCol) {
                    return false;
                }
                if (wordRow > middleRow) {
                    return false;
                }
                return wordRow + wordLength >= middleRow;
            } else {
                if (wordRow != middleRow) {
                    return false;
                }
                if (wordCol > middleCol) {
                    return false;
                }
                return wordCol + wordLength >= middleCol;
            }
        }
        return true;
    }

    boolean checkOverlappingTiles(Word word) {
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        int wordRow = word.getRow();
        int wordCol = word.getCol();
        for (int i = 0; i < wordLength; i++) {
            Tile currentTile = wordTiles[i];
            if (word.isVertical()) {
                if (board[wordRow + i][wordCol] != null && currentTile.letter != '_') {
                    return false;
                }
            } else {
                if (board[wordRow][wordCol + i] != null && currentTile.letter != '_') {
                    return false;
                }
            }
        }
        return true;
    }

    boolean checkAdjacentTiles(Word word) {
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        int wordRow = word.getRow();
        int wordCol = word.getCol();
        for (int i = 0; i < wordLength; i++) {
            if (word.isVertical()) {
                if ((wordCol < 14 && board[wordRow + i][wordCol + 1] != null)
                    || (wordCol > 0 && board[wordRow + i][wordCol - 1] != null)) {
                        return true;
                }
            } else {
                if ((wordRow < 14 && board[wordRow + 1][wordCol + i] != null)
                    || (wordCol < 0 && board[wordRow - 1][wordCol + i] != null)) {
                        return true;
                }
            }
        }
        return false;
    }

    boolean boardLegal(Word word) {
        return checkWordLocation(word)
                && checkFirstPlacement(word)
                && (checkOverlappingTiles(word) || checkAdjacentTiles(word));
    }
}
