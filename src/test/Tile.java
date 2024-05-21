package test;
import java.util.Objects;
import java.util.Random;
public class Tile {
    public final char letter;

    public final int score;

    public Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    public static class Bag {
        int[] letterCount;
        int[] initialLetterCount;
        Tile[] tiles;

        Random random;

        private static Bag bagInstance;

        private Bag() {
            letterCount = new int[] { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };

            initialLetterCount = new int[] { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };

            tiles = new Tile[] { new Tile('A', 1), new Tile('B', 3), new Tile('C', 3), new Tile('D', 2), new Tile('E', 1),
                    new Tile('F', 4), new Tile('G', 2), new Tile('H', 4), new Tile('I', 1),
                    new Tile('J', 8), new Tile('K', 5), new Tile('L', 1), new Tile('M', 3),
                    new Tile('N', 1), new Tile('O', 1), new Tile('P', 3), new Tile('Q', 10),
                    new Tile('R', 1), new Tile('S', 1), new Tile('T', 1), new Tile('U', 1),
                    new Tile('V', 4), new Tile('W', 8), new Tile('X', 8), new Tile('Y', 4), new Tile('Z', 10) };

            random = new Random();

            bagInstance = null;
        }
        boolean checkEmptyBag() {
            for (int i = 0; i < 26; ++i) {
                if (this.letterCount[i] != 0) {
                    return false;
                }
            }
            return true;
        }

        Tile getRand() {
            if (this.checkEmptyBag()) {
                return null;
            }

            int index;
            Tile tile;

            do {
                int randomIndex = this.random.nextInt(tiles.length);
                tile = this.tiles[randomIndex];
                index = tile.letter - 65;
            } while (this.letterCount[index] == 0);

            letterCount[index] -= 1;

            return tile;
        }

        Tile getTile(char c) {
            if (c == '_') {
                return new Tile('_', 0);
            }

            if (!(c >= 'A' && c <= 'Z')) {
                return null;
            }

            if (checkEmptyBag()) {
                return null;
            }

            int index = c - 65;
            if (letterCount[index] == 0) {
                return null;
            }

            letterCount[index] -= 1;
            return tiles[index];
        }

        void put(Tile tile) {
            int index = tile.letter - 65;
            if (this.letterCount[index] != this.initialLetterCount[index]) {
                letterCount[index] += 1;
            }
        }

        int size() {
            int size = 0;

            for (int count : letterCount) {
                size += count;
            }
            return size;
        }

        int[] getQuantities() {
            return letterCount.clone();
        }

        public static Bag getBag() {
            if (bagInstance == null) {
                bagInstance = new Bag();
            }
            return bagInstance;
        }

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }
}