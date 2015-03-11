package ru.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueLocks<T> {

    private Lock lock = new ReentrantLock();
    private Condition hasSpace = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    private final List<T> list;

    private final int capacity;

    public BlockingQueueLocks(final int capacity) {
        this.capacity = capacity;
        list = new ArrayList<T>(capacity);
    }

    public void push(T item) throws InterruptedException {
        lock.lock();
        try {
            while (list.size() >= capacity) {
                hasSpace.await();
            }
            list.add(item);
            notEmpty.signalAll();
        }
        finally {
            lock.unlock();
        }
    }

    public T pop() throws InterruptedException {
        lock.lock();
        try {
            while (list.isEmpty()) {
                notEmpty.await();
            }
            T item = list.remove(list.size() - 1);
            hasSpace.signalAll();
            return item;
        }
        finally {
            lock.unlock();
        }
    }

}
