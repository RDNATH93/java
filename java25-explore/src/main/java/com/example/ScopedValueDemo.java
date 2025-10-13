package com.example;

import java.util.UUID;

/**
 * Allows you to share immutable data which can not be changed accross methods
 * within a thread
 * and it's child thread. It is designed to be simpler and more efficient than
 * ThreadLocal
 */
public class ScopedValueDemo {

    private static final ScopedValue<String> requestId = ScopedValue.newInstance();

    void main() {
        handleRequest(UUID.randomUUID().toString());
    }

    void handleRequest(String reqId) {
        ScopedValue.where(requestId, reqId).run(() -> {
            log("Start Processing Request");

            // Simulate some Processing
            authenticate();
            try {
                fetchData();
            } catch (Exception e) {
                // logic to handle Exception
            }
            log("Finished Processing Request");
        });
    }

    void authenticate() {
        log("Authenticating Request");
    }

    void fetchData() throws InterruptedException {
        log("Fetching data from db");

        //capture the current binding value
        String currentReqId = requestId.get();

        Thread t = new Thread(()->{
            ScopedValue.where(requestId,currentReqId).run(()->{
                log("Child thread fetching related data");
            });
        });
        t.start();
        t.join();  //wait for child
    }

    static void log(String message){
        IO.println("["+requestId.get()+"] "+message);
    }

}

class ThreadLocalDemo {
    void main() {
        ThreadLocal<String> currentUser = new ThreadLocal<>();
        currentUser.set("Alice");

        new Thread(() -> {
            // currentUser.set("Alice"); //if we don't set the value, it will result as null
            IO.println("Inner User: " + currentUser.get());
        }).start();

        IO.println("Outer User: " + currentUser.get());
    }
}
