package DAY18;

import java.util.*;

class Task001_hashTable {

    LinkedList<Entry>[] data = new LinkedList[10];


    public void put(String keyval, int value) {
        int index = Math.abs(keyval.hashCode() % data.length);

        if (data[index] == null) {
            data[index] = new LinkedList<>();
        }

        for (Entry e : data[index]) {
            if (e.keyval.equals(keyval)) {
                e.value = value;
                return;
            }
        }

        data[index].add(new Entry(keyval, value));
    }


    public void display() {
        for (int i = 0; i < data.length; i++) {
            System.out.print("Bucket " + i + ": ");
            if (data[i] != null) {
                for (Entry e : data[i]) {
                    System.out.print("[" + e.keyval + " = " + e.value + "] ");
                }
            }
            System.out.println();
        }
    }


    static class Entry {
        String keyval;
        int value;

        Entry(String k, int v) {
            keyval = k;
            value = v;
        }
    }


    public static void main(String[] args) {
        Task001_hashTable map = new Task001_hashTable();

        map.put("apple", 10);
        map.put("banana", 20);
        map.put("apple", 15);
        map.put("grape", 30);
        map.put("orange", 25);

        map.display(); // Show all key-value pairs
    }
}
