package com.na_ho.pricer.thread;

import com.na_ho.pricer.jni.QuantJNI;

import java.util.concurrent.LinkedBlockingQueue;

public class QuantLibThreadPool {
    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedBlockingQueue queue;

    public QuantLibThreadPool(int nThreads) {
        this.nThreads = nThreads;
        queue = new LinkedBlockingQueue();
        threads = new PoolWorker[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threads[i] = new PoolWorker(i);
            threads[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    private class PoolWorker extends Thread {

        private QuantJNI quantJNI = null;
        private int number;

        PoolWorker(int number) {
            super();

            quantJNI = new QuantJNI();
            quantJNI.loadDLL();
            this.number = number;
        }

        public void run() {
            QuantLibRunnable task;

            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            System.out.println("An error occurred while queue is waiting: " + e.getMessage());
                        }
                    }
                    task = (QuantLibRunnable) queue.poll();
                }

                try {
                    task.setQuantJNI(quantJNI);
                    task.run();
                    task.setQuantJNI(null);
                } catch (RuntimeException e) {
                    System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                }
            }
        }
    }
}