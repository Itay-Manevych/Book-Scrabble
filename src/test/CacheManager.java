package test;


import java.util.HashSet;

public class CacheManager {
	
	int size;
    CacheReplacementPolicy crp;
    HashSet<String> cache;

    public CacheManager (int size, CacheReplacementPolicy crp) {
        this.size = size;
        this.crp = crp;
        cache = new HashSet<>();
    }

    public boolean query (String word) {
        return cache.contains(word);
    }

    public void add (String word) {
        if (cache.size() == size) {
            String wordToRemove = crp.remove();
            cache.remove(wordToRemove);
        }
        crp.add(word);
        cache.add(word);
    }
}
