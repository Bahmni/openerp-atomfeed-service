package org.bahmni.feed.openerp;


public enum Jobs {
    CUSTOMER_FEED("customer.feed.generator.uri"),
    SALEORDER_FEED("saleorder.feed.generator.uri"),
    REFERENCE_DATA_FEED("referencedata.feed.generator.uri"),
    OPENELIS_SALEORDER_FEED("openelis.saleorder.feed.generator.uri"),
    DRUG_FEED("drug.feed.generator.uri"),
    LAB_FEED("lab.feed.generator.uri");

    private final String feedUri;

    Jobs(String feedUri) {
        this.feedUri = feedUri;
    }

    public String getFeedUri() {
        return feedUri;
    }
}
