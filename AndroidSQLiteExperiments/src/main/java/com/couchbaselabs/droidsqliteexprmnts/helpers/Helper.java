package com.couchbaselabs.droidsqliteexprmnts.helpers;

public class Helper {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
