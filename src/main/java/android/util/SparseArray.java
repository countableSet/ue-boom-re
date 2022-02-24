package android.util;

import java.util.HashMap;
import java.util.Map;

public class SparseArray<E> implements Cloneable {

    private final Map<Integer, E> map;

    public SparseArray() {
        this(10);
    }

    public SparseArray(int initialCapacity) {
        map = new HashMap<>();
    }

    public SparseArray<E> clone() {
        throw new RuntimeException("Stub!");
    }

    public E get(int key) {
        return map.get(key);
    }

    public E get(int key, E valueIfKeyNotFound) {
        return map.getOrDefault(key, valueIfKeyNotFound);
    }

    public void delete(int key) {
        throw new RuntimeException("Stub!");
    }

    public void remove(int key) {
        throw new RuntimeException("Stub!");
    }

    public void removeAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public void removeAtRange(int index, int size) {
        throw new RuntimeException("Stub!");
    }

    public void put(int key, E value) {
        map.put(key, value);
    }

    public int size() {
        throw new RuntimeException("Stub!");
    }

    public int keyAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public E valueAt(int index) {
        throw new RuntimeException("Stub!");
    }

    public void setValueAt(int index, E value) {
        throw new RuntimeException("Stub!");
    }

    public int indexOfKey(int key) {
        throw new RuntimeException("Stub!");
    }

    public int indexOfValue(E value) {
        throw new RuntimeException("Stub!");
    }

    public void clear() {
        throw new RuntimeException("Stub!");
    }

    public void append(int key, E value) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
