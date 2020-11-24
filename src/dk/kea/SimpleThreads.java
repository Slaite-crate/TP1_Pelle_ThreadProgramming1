package dk.kea;

import java.util.Random;

public class SimpleThreads {

    // Display a message, preceded by
    // the name of the current thread
    static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }

    private static class MessageLoop implements Runnable {
        private int countMessage = 0;

        public void run() {
            String[] importantInfo = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };

            try {
                for (int i = 0; i < importantInfo.length; i++) {
                    Random r = new Random();

                    int j = r.nextInt(4)+1;
                    // Pause for 4 seconds
                    Thread.sleep(1000*j);
                    // Print a message
                    threadMessage(importantInfo[i]);
                    countMessage++;
                }
            } catch (InterruptedException e) {
                threadMessage("I wasn't done!");
            }
        }

        public int getCountMessage() {
            return countMessage;
        }
    }

    public static void main(String args[]) throws InterruptedException {

        // Delay, in milliseconds before
        // we interrupt MessageLoop
        // thread (default one hour).
        long patience = 1000 * 60 * 60;

        // If command line argument
        // present, gives patience
        // in seconds.
        /*
        if (args.length > 0) {
         /*
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }
        */

        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        MessageLoop ta = new MessageLoop();
        MessageLoop ua = new MessageLoop();
        Thread t = new Thread(ta);
        Thread u = new Thread(ua);
        t.setName("thread t");
        u.setName("thread u");
        t.start();
        u.start();

        threadMessage("Waiting for MessageLoop thread to finish");
        // loop until MessageLoop
        // thread exits
        while (t.isAlive() || u.isAlive()) {
            threadMessage("Still waiting...");
            // Wait maximum of 1 second
            // for MessageLoop thread
            // to finish.
            t.join(1000);
            if (((System.currentTimeMillis() - startTime) > patience) && (t.isAlive() || u.isAlive())) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                u.interrupt();
                // Shouldn't be long now
                // -- wait indefinitely
                t.join();
                u.join();
            }
            if (((System.currentTimeMillis() - startTime) > 3000) && (t.isAlive() && u.isAlive())){
                if (ta.getCountMessage() > ua.getCountMessage()){
                    t.interrupt();
                    System.out.println("t number = " + ta.getCountMessage() + " u number = " + ua.getCountMessage());
                } else {
                    u.interrupt();
                    System.out.println("t number = " + ta.getCountMessage() + " u number = " + ua.getCountMessage());
                }
            }
        }
        threadMessage("Finally!");
    }
}
