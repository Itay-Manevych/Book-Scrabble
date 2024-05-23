package test;


import java.util.HashMap;

public class DictionaryManager {
    HashMap<String, test.Dictionary> map;
    private static DictionaryManager dictionaryManagerInstance;
    private DictionaryManager() {
        map = new HashMap<>();
        dictionaryManagerInstance = null;
    }

    private test.Dictionary getOrCreate(String bookName) {
        test.Dictionary currentDictionary = map.get(bookName);
        if (currentDictionary == null) {
            currentDictionary = new test.Dictionary(bookName);
            map.put(bookName, currentDictionary);
        }
        return currentDictionary;
    }
    private boolean search(test.Dictionary currentDictionary, String word, String searchMethod) {
        if (searchMethod.equals("query")) {
            return currentDictionary.query(word);
        } else if (searchMethod.equals("challenge")) {
            return currentDictionary.challenge(word);
        }
        return false;
    }
    boolean query(String... args) {
        boolean flag = false;
        for (int i = 0; i < args.length - 1; i++) {
            test.Dictionary currentDictionary = getOrCreate(args[i]);
            // even if we find the word in our current dictionary, we want to use search on all dictionaries for better caching.
            flag |= search(currentDictionary,args[args.length - 1], "query");
        }
        return flag;
    }
    boolean challenge(String... args) {
        boolean flag = false;
        for (int i = 0; i < args.length - 1; i++) {
            test.Dictionary currentDictionary = getOrCreate(args[i]);
            // even if we find the word in our current dictionary, we want to use search on all dictionaries for better caching.
            flag |= search(currentDictionary,args[args.length - 1], "challenge");
        }
        return flag;
    }

    public static DictionaryManager get() {
        if(dictionaryManagerInstance == null) {
            dictionaryManagerInstance = new DictionaryManager();
        }
        return dictionaryManagerInstance;
    }

    int getSize() {
        return map.size();
    }
}
