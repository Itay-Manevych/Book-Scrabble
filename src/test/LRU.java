package test;

import java.util.Queue;
import java.util.LinkedList;

public class LRU implements CacheReplacementPolicy{
    Queue<String> queue;
    public LRU() {
        queue = new LinkedList<String>();
    }
    @Override
    public void add(String word) {
        if (queue.isEmpty()) {
            queue.add(word);
        } else {
            queue.removeIf(s -> s.equals(word));
            queue.add(word);
        }
    }
    @Override
    public String remove() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove();
    }
}
