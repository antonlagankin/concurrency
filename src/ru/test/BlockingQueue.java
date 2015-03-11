package ru.test;

import java.util.ArrayList;
import java.util.List;

public class BlockingQueue<T> {

    private final Object lock = new Object();

    private final List<T> list;

    private final int capacity;

    public BlockingQueue(final int capacity) {
        this.capacity = capacity;
        list = new ArrayList<T>(capacity);
    }

    public void push(T item) throws InterruptedException {
        synchronized (lock) {
            while (list.size() >= capacity) {
                lock.wait();
            }
            list.add(item);
            lock.notifyAll();
        }
    }

    public T pop() throws InterruptedException {
        synchronized (lock) {
            while (list.isEmpty()) {
                lock.wait();
            }
            T item = list.remove(list.size() - 1);
            lock.notifyAll();
            return item;
        }
    }

}
