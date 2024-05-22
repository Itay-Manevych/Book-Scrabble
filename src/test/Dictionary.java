package test;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;

public class Dictionary {
    CacheManager existingWords;
    CacheManager nonExistingWords;
    BloomFilter bloomFilter;

    String[] fileNames;
    public Dictionary(String... fileNames) {
        this.fileNames = fileNames.clone();
        existingWords = new CacheManager(400, new LRU());
        nonExistingWords = new CacheManager(100, new LFU());
        bloomFilter = new BloomFilter(256, "MD5","SHA1");
        for (String fileName : fileNames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                String[] words;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    words = line.split(" ");
                    for(String word: words) {
                        bloomFilter.add(word);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean query(String word) {
        if (existingWords.query(word)) {
            return true;
        } else if (nonExistingWords.query(word)) {
            return false;
        }

        if (bloomFilter.contains(word)) {
            existingWords.add(word);
            return true;
        } else {
            nonExistingWords.add(word);
            return false;
        }
    }

    boolean challenge(String word) {
        try {
            boolean bool = IOSearcher.search(word, fileNames);
            if (bool) {
                existingWords.add(word);
                return true;
            } else {
                nonExistingWords.add(word);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
