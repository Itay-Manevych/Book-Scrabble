package test;


import java.util.ArrayList;
import java.util.HashSet;

public class Board {
    Tile[][] board;
    final int rows;
    final int cols;
    private static Board boardInstance;
    HashSet<String> stringHashSet;
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

    private boolean checkWordLocation(Word word) {
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

    private boolean checkFirstPlacement(Word word) {
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

    private boolean checkOverlappingTiles(Word word) {
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

    private boolean checkAdjacentTiles(Word word) {
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        int wordRow = word.getRow();
        int wordCol = word.getCol();
        for (int i = 0; i < wordLength; i++) {
            if (word.isVertical()) {
                if ((wordCol < cols - 1 && board[wordRow + i][wordCol + 1] != null)
                    || (wordCol > 0 && board[wordRow + i][wordCol - 1] != null)) {
                        return true;
                }
            } else {
                if ((wordRow < rows - 1 && board[wordRow + 1][wordCol + i] != null)
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

    boolean dictionaryLegal() {
        return true;
    }
    private int findStartOfWord(int row, int col, boolean isVertical) {
        int i = 0;
        int start;
        if (isVertical) {
            start = col;
            while (i < col && board[row][col - i] != null) {
                start--;
                i++;
            }
        } else {
            start = row;
            while (i < row && board[row - i][col] != null) {
                start--;
                i++;
            }
        }
        return start;
    }
    private int findEndOfWord(int row, int col, boolean isVertical) {
        int i = 0;
        int end;
        if (isVertical) {
            end = col;
            while (i < cols - col && board[row][col + i] != null) {
                end++;
                i++;
            }
        } else {
            end = row;
            while (i < rows - row && board[row + i][col] != null) {
                end++;
                i++;
            }
        }
        return end;
    }
    private void addInsertedWord(Word word, ArrayList<Word> wordsArray) {
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        int wordRow = word.getRow();
        int wordCol = word.getCol();
        Tile[] newWordTiles = new Tile[wordLength];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            if (wordTiles[i].letter == '_') {
                if(word.isVertical()) {
                    newWordTiles[i] = board[wordRow + i][wordCol];
                    sb.append(newWordTiles[i].letter);
                } else {
                    newWordTiles[i] = board[wordRow][wordCol + i];
                    sb.append(newWordTiles[i].letter);
                }
            } else {
                newWordTiles[i] = wordTiles[i];
                sb.append(newWordTiles[i].letter);
            }
        }
        wordsArray.add(new Word(newWordTiles, wordRow, wordCol, word.isVertical()));
        stringHashSet.add(sb.toString());
    }
    private void addCrossword(Word word, ArrayList<Word> wordsArray, int index, boolean isVertical) {
        int wordRow = word.getRow();
        int wordCol = word.getCol();

        int start = isVertical
                ? findStartOfWord(wordRow + index, wordCol, isVertical)
                : findStartOfWord(wordRow, wordCol + index, isVertical);
        int end = isVertical
                ? findEndOfWord(wordRow + index, wordCol, isVertical)
                : findEndOfWord(wordRow, wordCol + index, isVertical);

        Tile[] newWordTiles = new Tile[end - start + 1];
        StringBuilder sb = new StringBuilder();

        for (int j = 0, startTemp = start; startTemp <= end; j++, startTemp++) {
            newWordTiles[j] = isVertical
                    ? board[wordRow + index][startTemp]
                    : board[startTemp][wordCol + index];
            sb.append(newWordTiles[j].letter);
        }

        String wordString = sb.toString();
        if (!stringHashSet.contains(wordString)) {
            stringHashSet.add(wordString);
            wordsArray.add(new Word(
                    newWordTiles,
                    isVertical ? wordRow + index : start,
                    isVertical ? start : wordCol + index,
                    !isVertical
            ));
        }
    }

    ArrayList<Word> getWords(Word word) {
        ArrayList<Word> wordsArray = new ArrayList<Word>();
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        addInsertedWord(word, wordsArray);
        for (int i = 0; i < wordLength; i++) {
            addCrossword(word, wordsArray, i, word.isVertical());
        }
        return wordsArray;
    }
}
