package com.couchbaselabs.droidsqliteexprmnts.helpers;

public class ExperimentResult {

    /**
     * A description of the result
     */
    private String resultDescription;

    /**
     * Constructor
     */
    public ExperimentResult(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }
}
