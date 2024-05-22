package test;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;

public class LFU implements CacheReplacementPolicy {
    private class Element {
        String string;
        int frequency;

        public Element(String string, int frequency) {
            this.string = string;
            this.frequency = frequency;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Element element = (Element) o;
            return Objects.equals(string, element.string);
        }
    }
    PriorityQueue<Element> priorityQueue;
    HashMap<String,Integer> hashMap;
    public LFU() {
        priorityQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.frequency));
        hashMap = new HashMap<String, Integer>();
    }
    @Override
    public void add(String word) {
        int wordFrequency = hashMap.getOrDefault(word,0);
        Element elementToAdd = new Element(word, wordFrequency + 1);
        Element elementToRemove = new Element(word, wordFrequency);
        if (wordFrequency == 0) {
            priorityQueue.add(elementToAdd);
            hashMap.put(word, wordFrequency + 1);
        } else {
            priorityQueue.remove(elementToRemove);
            hashMap.remove(word);
            priorityQueue.add(elementToAdd);
            hashMap.put(word, wordFrequency + 1);
        }
    }

    @Override
    public String remove() {
        Element elementToRemove = priorityQueue.poll();
        if (elementToRemove.frequency > 1) {
            priorityQueue.add(new Element(elementToRemove.string, elementToRemove.frequency - 1));
            hashMap.put(elementToRemove.string, elementToRemove.frequency - 1);
        } else {
            hashMap.remove(elementToRemove.string);
        }
        return elementToRemove.string;
    }
}
