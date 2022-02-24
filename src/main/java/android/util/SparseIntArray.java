package android.util;

import java.util.HashMap;
import java.util.Map;

public class SparseIntArray implements Cloneable {
    private final Map<Integer, Integer> map;

    public SparseIntArray() {
        this(10);
    }

    public SparseIntArray(int initialCapacity) {
        this.map = new HashMap<>();
    }

    public android.util.SparseIntArray clone() {
        throw new RuntimeException("Stub!");
    }

    public int get(int key) {
        throw new RuntimeException("Stub!");
    }

    public int get(int key, int valueIfKeyNotFound) {
        return map.getOrDefault(key, valueIfKeyNotFound);
    }

    public void delete(int key) {
        throw new RuntimeException("Stub!");
    }

    public void removeAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public void put(int key, int value) {
        map.put(key, value);
    }

    public int size() {
        throw new RuntimeException("Stub!");
    }

    public int keyAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public int valueAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public int indexOfKey(int key) {
        throw new RuntimeException("Stub!");
    }

    public int indexOfValue(int value) {
        throw new RuntimeException("Stub!");
    }

    public void clear() {
        throw new RuntimeException("Stub!");
    }

    public void append(int key, int value) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}