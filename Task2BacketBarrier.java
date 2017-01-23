package C10TODO;

import java.util.LinkedList;
import java.util.Queue;


public class Task2BacketBarrier {

    static class BacketBarrier implements Bucket, Drop {

        final Queue<Thread> dropsQueue = new LinkedList<>();
        int dropsCount = 0;
        final Object dropsWaitingMonitor = new Object();
        final Object dropsLeakingMonitor = new Object();

        @Override
        public void awaitDrop() throws InterruptedException {
            synchronized (dropsWaitingMonitor) {
                if (dropsCount == 0) {
                    System.out.println("awaitDrop: " + Thread.currentThread().getName() + "start waiting for the drop");
                    dropsWaitingMonitor.wait();
                }

                dropsCount--;
                System.out.println("awaitDrop: " + Thread.currentThread().getName() + "finish waiting for the drop");
            }
        }

        @Override
        public void leak() throws InterruptedException {
            if (dropsQueue.isEmpty()) {
                System.out.println("leak: dropsQueue is empty");
                return;
            }

            final Thread drop = dropsQueue.poll();

            synchronized (drop) {
                System.out.println("leak: Drop " + drop.getName() + "was leaked");
                drop.notify();
            }
        }

        @Override
        public void arrived() throws InterruptedException {

            final Thread drop = Thread.currentThread();
            final String dropName = drop.getName();

            synchronized (dropsWaitingMonitor) {
                dropsQueue.add(Thread.currentThread());
                dropsCount++;

                System.out.println("arrived: Drop from " + dropName + "was added to Bucket");

                dropsWaitingMonitor.notify();
            }

            synchronized (drop) {
                dropsLeakingMonitor.wait();
                System.out.println("arrived: Drop from " + dropName + " was leaked");
            }

        }

    }

    public static void main(String[] args) {

    }

}