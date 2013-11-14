package com.couchbaselabs.droidsqliteexprmnts.helpers;

import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.concurrent.CountDownLatch;

public class RunnableExperiment implements Runnable {

    /**
     * This is used to signal when the experiment is over
     */
    protected CountDownLatch countDownLatch;

    /**
     * The result of the experiment
     */
    protected ExperimentResult experimentResult;

    /**
     * The base directory where test databases should be created under
     */
    protected File baseDirectory;

    /**
     * Constructor
     *
     * @param countDownLatch to signal when the experiment is over
     * @param baseDirectory where test databases should be created
     */
    public RunnableExperiment(CountDownLatch countDownLatch, File baseDirectory) {
        this.countDownLatch = countDownLatch;
        this.baseDirectory = baseDirectory;
    }

    @Override
    public void run() {
        throw new RuntimeException("This should be overridden by a subclass");
    }

    public ExperimentResult getExperimentResult() {
        return experimentResult;
    }


}
