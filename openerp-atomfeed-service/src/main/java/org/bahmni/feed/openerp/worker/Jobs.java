package org.bahmni.feed.openerp.worker;

public enum Jobs {
    DRUG_JOB("create.drug");

    private final String jobRef;

    Jobs(String jobRef) {
        this.jobRef = jobRef;
    }

    public String getJobRef() {
        return jobRef;
    }
}
