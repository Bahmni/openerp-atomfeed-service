/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.job;


public enum Jobs {
    CUSTOMER_FEED("customer.feed.generator.uri"),
    SALEORDER_FEED("saleorder.feed.generator.uri"),
    REFERENCE_DATA_FEED("referencedata.feed.generator.uri"),
    OPENELIS_SALEORDER_FEED("openelis.saleorder.feed.generator.uri"),
    DRUG_FEED("drug.feed.generator.uri"),
    LAB_TEST_FEED("lab.feed.generator.uri"),
    LAB_PANEL_FEED("lab.feed.generator.uri"),
    RADIOLOGY_TEST_FEED("lab.feed.generator.uri"),
    SALEABLE_FEED("saleable.feed.generator.uri");

    private final String feedUriRef;

    Jobs(String feedUriRef) {
        this.feedUriRef = feedUriRef;
    }

    public String getFeedUriRef() {
        return feedUriRef;
    }
}
