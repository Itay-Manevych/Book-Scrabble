package test;

import java.util.Queue;
import java.util.LinkedList;

public class LRU implements CacheReplacementPolicy{
    Queue<String> queue;
    public LRU() {
        Queue<String> queue = new LinkedList<String>();
    }
    @Override
    public void add(String word) {
        queue.removeIf(s -> s.equals(word));
        queue.add(word);
    }
    @Override
    public String remove() {
        return queue.remove();
    }
}
