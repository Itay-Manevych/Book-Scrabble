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
        if (queue.size() == 0) {
            queue.add(word);
        } else {
            queue.removeIf(s -> s.equals(word));
            queue.add(word);
        }
    }
    @Override
    public String remove() {
        return queue.remove();
    }
}
