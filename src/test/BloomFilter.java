package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public class BloomFilter {

    BitSet bitSet;
    int size;
    ArrayList<String> algsList;
    public BloomFilter(int size, String... algs) {
        this.size = size;
        this.algsList = new ArrayList<>();
        Collections.addAll(algsList, algs);
        bitSet = new BitSet(size);
    }

    public void add(String word) {
        for (String alg : algsList) {
            try {
                MessageDigest md = MessageDigest.getInstance(alg);
                byte[] bytes = md.digest(word.getBytes());
                BigInteger bigInt = new BigInteger(bytes);
                int indexInBitSet = Math.abs(bigInt.intValue());
                indexInBitSet = indexInBitSet % bitSet.size();
                bitSet.set(indexInBitSet);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean contains(String word) {
        for (String alg : algsList) {
            try {
                MessageDigest md = MessageDigest.getInstance(alg);
                byte[] bytes = md.digest(word.getBytes());
                BigInteger bigInt = new BigInteger(bytes);
                int indexInBitSet = Math.abs(bigInt.intValue());
                indexInBitSet = indexInBitSet % bitSet.size();
                if (!bitSet.get(indexInBitSet)) {
                    return false;
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitSet.length(); i++) {
            sb.append(bitSet.get(i) ? "1" : "0");
        }
        return sb.toString();
    }
}
