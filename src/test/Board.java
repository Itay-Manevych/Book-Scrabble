package test;


import java.util.ArrayList;
import java.util.HashSet;

public class Board {
    Tile[][] board;
    final int rows;
    final int cols;
    private static Board boardInstance;
    HashSet<String> stringHashSet;

    private enum BonusType {
        DOUBLE_LETTER_SCORE,
        TRIPLE_LETTER_SCORE,
        DOUBLE_WORD_SCORE,
        TRIPLE_WORD_SCORE
    }

    BonusType[][] bonuses;

    private Board() {
        rows = 15;
        cols = 15;
        board = new Tile[rows][cols];
        stringHashSet = new HashSet<String>();
        boardInstance = null;
        bonuses = new BonusType[][] {
                { BonusType.TRIPLE_WORD_SCORE, null, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.TRIPLE_WORD_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, null, BonusType.TRIPLE_WORD_SCORE },
                { null, BonusType.DOUBLE_WORD_SCORE, null, null, null, BonusType.TRIPLE_LETTER_SCORE, null, null, null, BonusType.TRIPLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_WORD_SCORE, null },
                { null, null, BonusType.DOUBLE_WORD_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_WORD_SCORE, null, null },
                { BonusType.DOUBLE_LETTER_SCORE, null, null, BonusType.DOUBLE_WORD_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_WORD_SCORE, null, null, BonusType.DOUBLE_LETTER_SCORE },
                { null, null, null, null, BonusType.DOUBLE_WORD_SCORE, null, null, null, null, null, BonusType.DOUBLE_WORD_SCORE, null, null, null, null },
                { null, BonusType.TRIPLE_LETTER_SCORE, null, null, null, BonusType.TRIPLE_LETTER_SCORE, null, null, null, BonusType.TRIPLE_LETTER_SCORE, null, null, null, BonusType.TRIPLE_LETTER_SCORE, null },
                { null, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, null },
                { BonusType.TRIPLE_WORD_SCORE, null, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_WORD_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, null, BonusType.TRIPLE_WORD_SCORE },
                { null, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, null },
                { null, BonusType.TRIPLE_LETTER_SCORE, null, null, null, BonusType.TRIPLE_LETTER_SCORE, null, null, null, BonusType.TRIPLE_LETTER_SCORE, null, null, null, BonusType.TRIPLE_LETTER_SCORE, null },
                { null, null, null, null, BonusType.DOUBLE_WORD_SCORE, null, null, null, null, null, BonusType.DOUBLE_WORD_SCORE, null, null, null, null },
                { BonusType.DOUBLE_LETTER_SCORE, null, null, BonusType.DOUBLE_WORD_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_WORD_SCORE, null, null, BonusType.DOUBLE_LETTER_SCORE },
                { null, null, BonusType.DOUBLE_WORD_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_WORD_SCORE, null, null },
                { null, BonusType.DOUBLE_WORD_SCORE, null, null, null, BonusType.TRIPLE_LETTER_SCORE, null, null, null, BonusType.TRIPLE_LETTER_SCORE, null, null, null, BonusType.DOUBLE_WORD_SCORE, null },
                { BonusType.TRIPLE_WORD_SCORE, null, null, BonusType.DOUBLE_LETTER_SCORE, null, null, null, BonusType.TRIPLE_WORD_SCORE, null, null, null, BonusType.DOUBLE_LETTER_SCORE, null, null, BonusType.TRIPLE_WORD_SCORE }
        };
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
    private boolean checkAdjacentTile(Tile tile, int row, int col) {
        if (col < 14 && board[row][col + 1] != null) {
            return true;
        } else if (col == 14 && tile.letter == '_' && board[row][col] != null) {
            return true;
        }

        if (col > 0 && board[row][col - 1] != null) {
            return true;
        } else if (col == 0 && tile.letter == '_' && board[row][col] != null) {
            return true;
        }

        if (row < 14 && board[row + 1][col] != null) {
            return true;
        } else if (row == 14 && tile.letter == '_' && board[row][col] != null) {
            return true;
        }

        if (row > 0 && board[row - 1][col] != null) {
            return true;
        } else if (col == 0 && tile.letter == '_' && board[row][col] != null) {
            return true;
        }
        return false;
    }
    private boolean checkAdjacentTiles(Word word) {
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        int wordRow = word.getRow();
        int wordCol = word.getCol();
        for (int i = 0; i < wordLength; i++) {
            if (word.isVertical()) {
               if(checkAdjacentTile(wordTiles[i], wordRow + i, wordCol)) {
                   return true;
               }
            } else {
                if(checkAdjacentTile(wordTiles[i] ,wordRow, wordCol + i)) {
                    return true;
                }
            }
        }
        return false;
    }
    boolean isWordEmpty(Word word) {
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        for (int i = 0; i < wordLength; i++) {
            Tile currentTile = wordTiles[i];
            if (currentTile.letter != '_') {
                return false;
            }
        }
        return true;
    }
    boolean wordTileCount(Word word) {
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        for (int i = 0; i < wordLength; i++) {
            Tile currentTile = wordTiles[i];
            if (currentTile == null) {
                return false;
            }
        }
        return true;
    }
    boolean checkBoardEmpty() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }
    boolean boardLegal(Word word) {
        if (!wordTileCount(word)) {
            return false;
        }
        if (isWordEmpty(word)) {
            return false;
        }
        if (!checkWordLocation(word)) {
            return false;
        }
        if (!checkFirstPlacement(word)) {
            return false;
        }
        if (checkAdjacentTiles(word)) {
            if(!checkOverlappingTiles(word)) {
                return false;
            } else {
                return true;
            }
        } else if (checkBoardEmpty()) {
            return true;
        }
        return false;
    }

    boolean dictionaryLegal(Word word) {
        return true;
    }
    private int findStartOfWord(int row, int col, boolean isVertical) {
        int i = 0;
        int start;
        if (isVertical) {
            start = col;
            while (i < col && board[row][col - i - 1] != null) {
                start--;
                i++;
            }
        } else {
            start = row;
            while (i < row && board[row - i - 1][col] != null) {
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
            while (i + 1 < cols - col && board[row][col + i + 1] != null) {
                end++;
                i++;
            }
        } else {
            end = row;
            while (i + 1 < rows - row && board[row + i + 1][col] != null) {
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
                if (word.isVertical()) {
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

        if (start == end) {
            return;
        }

        Tile[] newWordTiles = new Tile[end - start + 1];
        StringBuilder sb = new StringBuilder();

        for (int j = 0, startTemp = start; startTemp <= end; j++, startTemp++) {
            newWordTiles[j] = isVertical
                    ? board[wordRow + index][startTemp] == null ? word.tiles[index] : board[wordRow + index][startTemp]
                    : board[startTemp][wordCol + index] == null ? word.tiles[index] : board[startTemp][wordCol + index];
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
    boolean checkSubWord(ArrayList<Word> wordArrayList, Word word) {
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        int wordRow = word.getRow();
        int wordCol = word.getCol();

        if (word.isVertical()) {
            if ((wordRow > 0 && board[wordRow - 1][wordCol] != null)
            || (wordRow == 0 && wordTiles[0].letter == '_' && board[wordRow][wordCol] != null)) {
                int start = findStartOfWord(wordRow, wordCol, !word.isVertical());
                int end = findEndOfWord(wordRow, wordCol, !word.isVertical());
                if (end >= wordRow && board[wordRow][wordCol] == null) {
                    end = wordRow + wordLength - 1;
                }
                if (start == end) {
                    return false;
                }

                Tile[] newWordTiles = new Tile[end - start + 1];
                StringBuilder sb = new StringBuilder();
                int j = 0;
                int k = 0;
                int startTemp = start;
                boolean flag = false;
                while (startTemp <= end) {
                    if (board[startTemp][wordCol] == null) {
                        newWordTiles[j] = wordTiles[k];
                        sb.append(newWordTiles[j].letter);
                        k++;
                        flag = true;
                    } else {
                        newWordTiles[j] = board[startTemp][wordCol];
                        sb.append(newWordTiles[j].letter);
                        if (flag) {
                            k++;
                        }
                    }
                    j++;
                    startTemp++;
                }
                String wordString = sb.toString();
                if (!stringHashSet.contains(wordString)) {
                    stringHashSet.add(wordString);
                    wordArrayList.add(new Word(newWordTiles,start,wordCol,word.isVertical()));
                }
                return true;
            }
            else if ((wordRow + wordLength < 14 && board[wordRow + wordLength][wordCol] != null)
            || (wordRow + wordLength == 14 &&  wordTiles[wordLength - 1].letter == '_' && board[wordRow + wordLength][wordCol] != null)) {
                int start = findStartOfWord(wordRow, wordCol, !word.isVertical());
                int end = findEndOfWord(wordRow, wordCol, !word.isVertical());
                if (end >= wordRow && board[wordRow][wordCol] == null) {
                    int i = 0;
                    end = wordLength - 1;
                    while (wordLength + i < rows - wordRow && board[wordRow + wordLength + i][wordCol] != null) {
                        i++;
                        end++;
                    }
                }
                if (start == end) {
                    return false;
                }

                Tile[] newWordTiles = new Tile[end - start + 1];
                StringBuilder sb = new StringBuilder();
                int j = 0;
                int k = 0;
                int startTemp = start;
                boolean flag = false;
                while (startTemp <= end) {
                    if (board[startTemp][wordCol] == null) {
                        newWordTiles[j] = wordTiles[k];
                        sb.append(newWordTiles[j].letter);
                        k++;
                        flag = true;
                    } else {
                        newWordTiles[j] = board[startTemp][wordCol];
                        sb.append(newWordTiles[j].letter);
                        if (flag) {
                            k++;
                        }
                    }
                    j++;
                    startTemp++;
                }
                String wordString = sb.toString();
                if (!stringHashSet.contains(wordString)) {
                    stringHashSet.add(wordString);
                    wordArrayList.add(new Word(newWordTiles,start,wordCol,word.isVertical()));
                }
                return true;
            }
            return false;
        } else {
            if ((wordCol > 0  && board[wordRow][wordCol - 1] != null)
            || (wordCol == 0 && wordTiles[0].letter == '_') && board[wordRow][wordCol] != null) {
                int start = findStartOfWord(wordRow, wordCol, !word.isVertical());
                int end = findEndOfWord(wordRow, wordCol, !word.isVertical());
                if (end >= wordCol && board[wordRow][wordCol] == null) {
                    end = wordCol + wordLength - 1;
                }
                if (start == end) {
                    return false;
                }

                Tile[] newWordTiles = new Tile[end - start + 1];
                StringBuilder sb = new StringBuilder();
                int j = 0;
                int k = 0;
                int startTemp = start;
                boolean flag = false;
                while (startTemp <= end) {
                    if (board[wordRow][startTemp] == null) {
                        newWordTiles[j] = wordTiles[k];
                        sb.append(newWordTiles[j].letter);
                        k++;
                        flag = true;
                    } else {
                        newWordTiles[j] = board[wordRow][startTemp];
                        sb.append(newWordTiles[j].letter);
                        if (flag) {
                            k++;
                        }
                    }
                    j++;
                    startTemp++;
                }
                String wordString = sb.toString();
                if (!stringHashSet.contains(wordString)) {
                    stringHashSet.add(wordString);
                    wordArrayList.add(new Word(newWordTiles,wordRow,start,word.isVertical()));
                }
                return true;
            } else if ((wordCol + wordLength < 14 && board[wordRow][wordCol + wordLength] != null)
                    || ((wordCol + wordLength == 14 && wordTiles[wordLength - 1].letter == '_') && board[wordRow][wordCol] != null)) {
                int start = findStartOfWord(wordRow, wordCol, !word.isVertical());
                int end = findEndOfWord(wordRow, wordCol, !word.isVertical());
                if (end >= wordCol && board[wordRow][wordCol] == null) {
                    int i = 0;
                    end = wordLength - 1;
                    while (wordLength + i < cols - wordCol && board[wordRow][wordCol + wordLength + i] != null) {
                        i++;
                        end++;
                    }
                }
                if (start == end) {
                    return false;
                }

                Tile[] newWordTiles = new Tile[end - start + 1];
                StringBuilder sb = new StringBuilder();
                int j = 0;
                int k = 0;
                int startTemp = start;
                boolean flag = false;
                while (startTemp <= end) {
                    if (board[wordRow][startTemp] == null) {
                        newWordTiles[j] = wordTiles[k];
                        sb.append(newWordTiles[j].letter);
                        k++;
                        flag = true;
                    } else {
                        newWordTiles[j] = board[wordRow][startTemp];
                        sb.append(newWordTiles[j].letter);
                        if (flag) {
                            k++;
                        }
                    }
                    j++;
                    startTemp++;
                }
                String wordString = sb.toString();
                if (!stringHashSet.contains(wordString)) {
                    stringHashSet.add(wordString);
                    wordArrayList.add(new Word(newWordTiles,wordRow,wordCol,word.isVertical()));
                }
                return true;
            }
            return false;
        }
    }
    ArrayList<Word> getWords(Word word) {
        ArrayList<Word> wordsArray = new ArrayList<Word>();
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        for (int i = 0; i < wordLength; i++) {
            addCrossword(word, wordsArray, i, word.isVertical());
        }
        if(!checkSubWord(wordsArray,word)) {
            addInsertedWord(word, wordsArray);
        }
        return wordsArray;
    }
    private int calculateTileScore(Word word, int row, int col, int index) {
        Tile currentTile = board[row][col];
        if (currentTile == null) {
            currentTile = word.getTiles()[index];
        }
        BonusType currentBonus = bonuses[row][col];
        if (row == rows / 2 && col == cols / 2) {
            bonuses[row][col] = null;
        }
        int score = 0;

        if (currentBonus == BonusType.DOUBLE_LETTER_SCORE) {
            score += currentTile.score * 2;
        } else if (currentBonus == BonusType.TRIPLE_LETTER_SCORE) {
            score += currentTile.score * 3;
        } else {
            score += currentTile.score;
        }

        return score;
    }
    int getScore(Word word) {
        int wordLength = word.getTiles().length;
        int wordRow = word.getRow();
        int wordCol = word.getCol();
        int score = 0;
        boolean doubleWordMultiplierFlag = false;
        boolean tripleWordMultiplierFlag = false;
        int doubleWordMultiplierCount = 0;
        int tripleWordMultiplierCount = 0;

        for (int i = 0; i < wordLength; i++) {
            int row = word.isVertical() ? wordRow + i : wordRow;
            int col = word.isVertical() ? wordCol : wordCol + i;

            BonusType currentBonus = bonuses[row][col];
            score += calculateTileScore(word, row, col, i);

            if (currentBonus == BonusType.DOUBLE_WORD_SCORE) {
                doubleWordMultiplierFlag = true;
                doubleWordMultiplierCount++;
            } else if (currentBonus == BonusType.TRIPLE_WORD_SCORE) {
                tripleWordMultiplierFlag = true;
                tripleWordMultiplierCount++;
            }
        }

        if (doubleWordMultiplierFlag) {
            while (doubleWordMultiplierCount > 0) {
                score *= 2;
                doubleWordMultiplierCount--;
            }
        } if (tripleWordMultiplierFlag) {
            while (tripleWordMultiplierCount > 0) {
                score *= 3;
                tripleWordMultiplierCount--;
            }
        }

        return score;
    }
    private void placeWord(Word word) {
        Tile[] wordTiles = word.getTiles();
        int wordLength = wordTiles.length;
        int wordRow = word.getRow();
        int wordCol = word.getCol();
        for (int i = 0; i < wordLength; i++) {
            Tile currentTile = wordTiles[i];
            int row = word.isVertical() ? wordRow + i : wordRow;
            int col = word.isVertical() ? wordCol: wordCol + i;
            if (currentTile.letter != '_') {
                board[row][col] = currentTile;
            }
        }
    }
    int tryPlaceWord(Word word) {
        if (!(boardLegal(word))) {
            return 0;
        }
        ArrayList<Word> wordsArray = getWords(word);
        for (Word w : wordsArray) {
            if (!(dictionaryLegal(w))) {
                return 0;
            }
        }
        placeWord(word);
        int score = 0;
        for (Word w : wordsArray) {
            score += getScore(w);
        }
        return score;
    }
}
