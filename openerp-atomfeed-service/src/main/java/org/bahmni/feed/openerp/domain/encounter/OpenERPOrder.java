package org.bahmni.feed.openerp.domain.encounter;

import org.joda.time.DateTime;

import java.util.List;

public class OpenERPOrder {
    private String id;
    private List<String> productIds;
    private String visitId;
    private String type;
    private DateTime startDate;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public void setVisitType(String display) {
        this.type = display;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisitId() {
        return visitId;
    }

    public String getType() {
        return type;
    }

    public void setVisitStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public String getDescription() {
        return description;
    }
}
