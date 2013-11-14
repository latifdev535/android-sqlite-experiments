package com.couchbaselabs.droidsqliteexprmnts.experiments;

import android.database.sqlite.SQLiteDatabase;

import com.couchbaselabs.droidsqliteexprmnts.helpers.DBHelper;
import com.couchbaselabs.droidsqliteexprmnts.helpers.ExperimentResult;
import com.couchbaselabs.droidsqliteexprmnts.helpers.RunnableExperiment;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**

 The following error should appear in the logs:

 ```
 SQLiteConnectionPool The connection pool for database has been unable to grant a connection to thread
 SQLiteConnectionPool(14004): Connections: 0 active, 1 idle, 0 available.
 ```

 ## Here's what turned out to be happening

 * Create a single SQLiteDatabase object that is shared among all threads
 * WriterThread spawned
 * ReaderThread spawned
 * WriterThread opens a transaction and inserts some data
 * WriterThread calls .join() on ReaderThread to wait for it to finish
 * ReaderThread attempts to read some data
 * Deadlock!

 ## Digging into the deadlock

 * WriterThread has an open transaction, and therefore is holding on to the one and only connection owned by the single SQLiteDatabase object
 * ReaderThread is trying to get a new connection to execute its statement, but cannot because WriterThread is holding the only one available
 * WriterThread is waiting for ReaderThread to finish so it can finish it's transaction and release the connection.
 * Deadlock!

 */
public class ThreadsSingleConnectionDeadlock extends RunnableExperiment {

    /**
     * Constructor
     */
    public ThreadsSingleConnectionDeadlock(CountDownLatch countDownLatch, File baseDirectory) {
        super(countDownLatch, baseDirectory);
    }

    @Override
    public void run() {

        // open a SQLiteDatabase object with a randomly named db
        SQLiteDatabase sqliteDatabase = DBHelper.openRandomDatabase(baseDirectory);
        DBHelper.createTestTables(sqliteDatabase);

        // create a ReaderThread and pass it the database
        ReaderThreadRunnable readerThreadRunnable = new ReaderThreadRunnable(sqliteDatabase);
        Thread readerThread = new Thread(readerThreadRunnable);

        // create a WriterThread and pass it the ReaderThread and the database
        WriterThreadRunnable writerThreadRunnable = new WriterThreadRunnable(sqliteDatabase, readerThread);
        Thread writerThread = new Thread(writerThreadRunnable);

        // start the writer thread (which will start the reader thread)
        writerThread.start();

        // join the writer thread (this will never unblock in the case of a deadlock)
        try {
            writerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // return "success" (this will never get returned if there is a deadlock)
        experimentResult = new ExperimentResult("Success");

        countDownLatch.countDown();

    }

    static class ReaderThreadRunnable implements Runnable {

        private SQLiteDatabase sqliteDatabase;

        ReaderThreadRunnable(SQLiteDatabase sqliteDatabase) {
            this.sqliteDatabase = sqliteDatabase;
        }

        @Override
        public void run() {
            DBHelper.runFakeQuery(sqliteDatabase);
        }

    }

    static class WriterThreadRunnable implements Runnable {

        private SQLiteDatabase sqliteDatabase;
        private Thread readerThread;

        WriterThreadRunnable(SQLiteDatabase sqliteDatabase, Thread readerThread) {
            this.sqliteDatabase = sqliteDatabase;
            this.readerThread = readerThread;
        }

        @Override
        public void run() {

            boolean shouldCommit = false;
            try {
                DBHelper.beginTransaction(sqliteDatabase);
                DBHelper.doFakeInsert(sqliteDatabase);
                shouldCommit = true;
                readerThread.start();
                readerThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DBHelper.endTransaction(sqliteDatabase, shouldCommit);
            }

        }
    }

}


