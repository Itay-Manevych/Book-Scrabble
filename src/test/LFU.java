package test;


import java.util.PriorityQueue;

public class LFU implements CacheReplacementPolicy {
    PriorityQueue<String> priorityQueue;
    public LFU() {
        priorityQueue = new PriorityQueue<>();
    }
    @Override
    public void add(String word) {
        priorityQueue.add(word);
    }

    @Override
    public String remove() {
        return priorityQueue.poll();
    }
}
