package org.bahmni.feed.openerp.job;


public enum Jobs {
    CUSTOMER_FEED("customer.feed.generator.uri"),
    SALEORDER_FEED("saleorder.feed.generator.uri"),
    REFERENCE_DATA_FEED("referencedata.feed.generator.uri"),
    OPENELIS_SALEORDER_FEED("openelis.saleorder.feed.generator.uri"),
    DRUG_FEED("drug.feed.generator.uri"),
    LAB_FEED("lab.feed.generator.uri"),
    SALEABLE_FEED("saleable.feed.generator.uri");

    private final String feedUriRef;

    Jobs(String feedUriRef) {
        this.feedUriRef = feedUriRef;
    }

    public String getFeedUriRef() {
        return feedUriRef;
    }
}
